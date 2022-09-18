package com.example.rabbitmq.api.services;

import com.example.rabbitmq.api.domains.Participant;
import com.example.rabbitmq.api.domains.dto.ParticipantDTO;
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

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.example.rabbitmq.api.ws.ParticipantWsController.FETCH_PARTICIPANT_JOINED_CHAT_EVENT;
import static com.example.rabbitmq.api.ws.ParticipantWsController.prepareFetchParticipantJoinedChatEvent;

@Log4j2
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParticipantService {
    //локальное хранилище участников чата
    private static final Map<String, Participant> participantMap = new ConcurrentHashMap<String, Participant>();
    //rabbit хранилище участников чата
    SetOperations<String, Participant> setOperations;

    private final SimpMessagingTemplate simpMessagingTemplate;

    public void handleSubscription(String sessionId, String participantId, String chatId) {
        Participant participant = Participant.builder()
                .id(participantId)
                .chatId(chatId)
                .sessionId(sessionId).build();

        participantMap.put(participant.getSessionId(), participant);

        setOperations.add(ParticipantKeyHelper.makeKey(chatId), participant);

        simpMessagingTemplate.convertAndSend(
                prepareFetchParticipantJoinedChatEvent(FETCH_PARTICIPANT_JOINED_CHAT_EVENT),
                ParticipantDTO.builder()
                        .id(participantId)
                        .enterAt(participant.getEnteredAt())
                        .build()
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
        final SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.wrap(abstractSubProtocolEvent.getMessage());
        //если инстанция юзера существует -> удалить из мап пользователей чата
        Optional.ofNullable(headerAccessor.getSessionId())
                .map(participantMap::remove)
                .ifPresent(participant -> {
                    setOperations.remove(ParticipantKeyHelper.makeKey(participant.getChatId()), participant);

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

    public void handleJoinChat() {

    }

    private static class ParticipantKeyHelper {
        private static final String KEY = "chat:{chat_id}:participants:key";
        private static final String HASH_KEY = "chat:{chat_id}:participants:key";

        public static String makeKey(String chatId) {
            return KEY.replace("{chat_id}", chatId);
        }
    }
}
