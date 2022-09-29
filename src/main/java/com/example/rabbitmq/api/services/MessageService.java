package com.example.rabbitmq.api.services;

import com.example.rabbitmq.api.domains.dto.MessageDTO;
import com.example.rabbitmq.data.service.impl.MessageServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;

import static com.example.rabbitmq.api.ws.ChatWsController.*;
import static java.lang.String.format;
import static java.util.Objects.isNull;

@Log4j2
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    private final HashOperations<String, String, MessageDTO> setOperations;

    //gonna save images to redis by messageKey,makes message more scalable
    private final HashOperations<String, String, String> imageSet;

    private final MessageServiceImpl messageServiceImpl;

    private static final String CHAT_ID_SEQ = "{chat_id}";

    private static final String PARTICIPANT_ID_SEQ = "{participant_id}";

    private static final String IMAGES_DIR_SEQ = "IMAGES";

    private static final String IMAGE_NULL_BASE64 = "null";


    public static MessageDTO generateMessageDto(String messageText, String sessionId, String chatId) {
        return MessageDTO.builder()
                .sentFrom(sessionId)
                .chatId(chatId)
                .text(messageText)
                .build();
    }

    public void sendMessageToAll(String chatId, String messageText, String sessionId, String file) {
        if (!messageText.isEmpty() && !sessionId.isEmpty()) {
            MessageDTO messageDTO = generateMessageDto(messageText, sessionId, chatId);

            messageServiceImpl.saveMessage(messageDTO);

            if (!isNull(file) && !file.isEmpty() && !IMAGE_NULL_BASE64.equals(file)) {
                imageSet.put(IMAGES_DIR_SEQ, messageDTO.getId(), file);
                messageDTO.setHaveByteContent(true);
            }

            messageServiceImpl.saveMessage(messageDTO);

            setOperations.put(prepareSaveChatMessagesClusterName(chatId), messageDTO.getId(), messageDTO);

            simpMessagingTemplate.convertAndSend(prepareFetchChatMessagesDestinationLink(chatId), messageDTO);
        }
    }

    public String fetchMessageContent(String messageId) {
        String messageContentByte64 = imageSet.get(IMAGES_DIR_SEQ,messageId);
        if(isNull(messageContentByte64) || messageContentByte64.isEmpty()){
            return null;
        }
        return messageContentByte64;
    }

    public void fetchMessagesPageForChat(String chatId, int page) {
        Page<MessageDTO> messagesList = messageServiceImpl.fetchMessagesForChat(chatId, page);

        simpMessagingTemplate.convertAndSend(prepareFetchChatMessagesHistoryDestinationLink(chatId), messagesList);
    }

    private static String prepareSendMessageToAllLink(String chatId) {
        return SEND_MESSAGE_TO_ALL.replace(CHAT_ID_SEQ, chatId);
    }

    private static String prepareSendMessageToParticipantLink(String chatId, String participantId) {
        return SEND_MESSAGE_TO_PARTICIPANT.replace(CHAT_ID_SEQ, chatId).replace(PARTICIPANT_ID_SEQ, participantId);
    }

    public static String prepareFetchChatMessagesHistoryDestinationLink(String chatId) {
        return FETCH_CHAT_MESSAGES_HISTORY.replace(CHAT_ID_SEQ, chatId);
    }

    public static String prepareFetchPersonalChatMessagesLink(String chatId, String participantId) {
        return FETCH_PERSONAL_CHAT_MESSAGES.replace(CHAT_ID_SEQ, chatId).replace(PARTICIPANT_ID_SEQ, participantId);
    }

    public static String prepareFetchChatMessagesDestinationLink(String chatId) {
        return FETCH_CHAT_MESSAGES.replace(CHAT_ID_SEQ, chatId);
    }

    public static String prepareSaveChatMessagesClusterName(String chatId) {
        return format("%s:messages", chatId);
    }
}
