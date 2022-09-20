package com.example.rabbitmq.api.services;

import com.example.rabbitmq.api.domains.dto.MessageDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import static com.example.rabbitmq.api.ws.ChatWsController.*;

@Log4j2
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    private final SetOperations<String, MessageDTO> setOperations;


    public static MessageDTO generateMessageDto(String messageText, String sessionId) {
        return MessageDTO.builder()
                .from(sessionId)
                .text(messageText)
                .build();
    }

    public void sendMessageToAll(String chatId, String messageText, String sessionId) {
        if (!messageText.isEmpty() && !sessionId.isEmpty()) {
            MessageDTO messageDTO = generateMessageDto(messageText, sessionId);
            setOperations.add(messageDTO.getId(), messageDTO);
            simpMessagingTemplate.convertAndSend(prepareFetchChatMessagesDestinationLink(chatId), messageDTO);
        }
    }

    private static String prepareSendMessageToAllLink(String chatId) {
        return SEND_MESSAGE_TO_ALL.replace("{chat_id}", chatId);
    }

    private static String prepareSendMessageToParticipantLink(String chatId, String participantId) {
        return SEND_MESSAGE_TO_PARTICIPANT.replace("{chat_id}", chatId).replace("{participant_id}", participantId);
    }

    private static String prepareFetchChatMessagesDestinationLink(String chatId) {
        return FETCH_CHAT_MESSAGES.replace("{chat_id}", chatId);
    }

    public static String prepareFetchPersonalChatMessagesLink(String chatId, String participantId) {
        return FETCH_PERSONAL_CHAT_MESSAGES.replace("{chat_id}", chatId).replace("{participant_id}", participantId);
    }
}
