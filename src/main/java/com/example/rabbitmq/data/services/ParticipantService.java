package com.example.rabbitmq.data.services;

import com.example.rabbitmq.api.domains.Chat;
import com.example.rabbitmq.api.domains.Participant;
import com.example.rabbitmq.api.domains.dto.ParticipantDTO;
import com.example.rabbitmq.api.ws.ChatWsController;
import com.example.rabbitmq.api.ws.ParticipantWsController;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.example.rabbitmq.api.ws.ParticipantWsController.FETCH_PARTICIPANT_JOINED_CHAT_EVENT;
import static com.example.rabbitmq.api.ws.ParticipantWsController.prepareFetchParticipantJoinedChatEvent;

@Log4j2
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParticipantService {
    //локальное хранилище участников чата
    private static final Map<String, Participant> participantMap = new ConcurrentHashMap<>();
    //rabbit хранилище участников чата
    private final SetOperations<String, Participant> setOperations;

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final ChatService chatService;

    public void handleJoinChat(String sessionId, String participantId, String chatId) {
        log.info(String.format("User %s joining chat", participantId));
        Participant participant = Participant.builder()
                .id(participantId)
                .chatId(chatId)
                .sessionId(sessionId).build();

        participantMap.put(participant.getSessionId(), participant);

        setOperations.add(ParticipantKeyHelper.makeKey(chatId), participant);

        simpMessagingTemplate.convertAndSend(
                prepareFetchParticipantJoinedChatEvent(FETCH_PARTICIPANT_JOINED_CHAT_EVENT),
                generateParticipantDto(participant)
        );
    }

    public Set<Participant> getParticipants(String chatId) {
        return Optional.ofNullable(setOperations.members(ParticipantKeyHelper.makeKey(chatId))).orElseGet(HashSet::new);
    }

    @EventListener
    public void handleUnsubscribe(SessionUnsubscribeEvent unsubscribeEvent) {
        handleLeaveChat(unsubscribeEvent);
    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent sessionDisconnectEvent) {
        handleLeaveChat(sessionDisconnectEvent);
    }

    public void handleLeaveChat(AbstractSubProtocolEvent abstractSubProtocolEvent) {
        log.info("Disconnecting...");
        final SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.wrap(abstractSubProtocolEvent.getMessage());
        //если инстанция юзера существует -> удалить из мап пользователей чата
        Optional.ofNullable(headerAccessor.getSessionId())
                .map(participantMap::remove)
                .ifPresent(participant -> {

                    String chatId = participant.getChatId();

                    Chat chatIns = chatService.getChats().stream().filter(
                            chatt -> Objects.equals(chatt.getId(), chatId)
                    ).findAny().get();

                    log.info("Participant leaved the chat. " + participant.getChatId());

                    setOperations.remove(ParticipantKeyHelper.makeKey(participant.getChatId()), participant);

                    String key = ParticipantKeyHelper.makeKey(chatId);

                    setOperations.remove(key, participant);

                    Optional
                            .ofNullable(setOperations.size(key))
                            .filter(size -> size == 0L)
                            .ifPresent(chat -> {
                                chatService.deleteChat(chatId);
                                simpMessagingTemplate.convertAndSend(
                                        ChatWsController.FETCH_DELETE_CHAT_EVENT,
                                        chatService.generateChatDto(chatIns)
                                );
                            });

                    //отправка сообщения об отключении пользователя
                    simpMessagingTemplate.convertAndSend(
                            ParticipantWsController.prepareFetchParticipantLeavedChatEvent(participant.getChatId()),
                            ParticipantDTO.builder()
                                    .enterAt(participant.getEnteredAt())
                                    .id(participant.getId())
                                    .build()
                    );
                });
    }

    private static class ParticipantKeyHelper {

        private static final String KEY = "chat:{chat_id}:participants:key";

        public static String makeKey(String chatId) {
            return KEY.replace("{chat_id}", chatId);
        }

    }

    public ParticipantDTO generateParticipantDto(Participant participant) {
        return ParticipantDTO.builder()
                .id(participant.getId())
                .enterAt(participant.getEnteredAt())
                .build();
    }
}
