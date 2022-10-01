package com.example.rabbitmq.api.ws;

import com.example.rabbitmq.api.domains.dto.chat.ChatDTO;
import com.example.rabbitmq.api.domains.dto.MessageDTO;
import com.example.rabbitmq.data.services.ChatService;
import com.example.rabbitmq.data.services.MessageService;
import com.example.rabbitmq.data.services.ParticipantService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.*;

import static com.example.rabbitmq.data.services.MessageService.*;

@Log4j2
@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@CrossOrigin(origins = {"http://localhost:4200"})
public class ChatWsController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final ParticipantService participantService;

    private final ChatService chatService;

    private final MessageService messageService;

    public static final String CREATE_CHAT = "/topic/chat.create";
    public static final String DELETE_CHAT = "/topic/chat.delete";
    public static final String FETCH_CREATE_CHAT_EVENT = "/topic/chat.create.event";
    public static final String FETCH_DELETE_CHAT_EVENT = "/topic/chat.delete.event";
    public static final String SEND_MESSAGE_TO_ALL = "/topic/chat.{chat_id}.messages.all.send";
    public static final String SEND_MESSAGE_TO_PARTICIPANT = "/topic/chat.{chat_id}.messages.participant.{participant_id}.send";
    public static final String FETCH_CHAT_MESSAGES = "/topic/chat.{chat_id}.messages";
    public static final String FETCH_PERSONAL_CHAT_MESSAGES = "/topic/chat.{chat_id}.participants.{participant_id}.messages";

    public static final String FETCH_PAGE_CHAT_MESSAGES_REQUEST = "/topic/chat.{chat_id}.messages.{page}";
    public static final String FETCH_CHAT_MESSAGES_HISTORY = "/topic/chat.{chat_id}.messages.history";

    @MessageMapping(CREATE_CHAT)
    public void createChat(String chatName) {
        log.info("Creating chat: " + chatName);
        chatService.createChat(chatName);
    }

    @MessageMapping(DELETE_CHAT)
    public void deleteChat(String chatId) {
        chatService.deleteChat(chatId);
    }

    @SubscribeMapping(FETCH_CREATE_CHAT_EVENT)
    public ChatDTO fetchCreateChatEvent() {
        return null;
    }

    @SubscribeMapping(FETCH_DELETE_CHAT_EVENT)
    public ChatDTO fetchDeleteChatEvent() {
        return null;
    }

    @SubscribeMapping(FETCH_CHAT_MESSAGES)
    public MessageDTO fetchChatMessage() {
        return null;
    }

    @SubscribeMapping(FETCH_PERSONAL_CHAT_MESSAGES)
    public ResponseEntity<MessageDTO> fetchPersonalChatMessage(
            @DestinationVariable("chat_id") String chatId,
            @DestinationVariable("participant_id") String participantId,
            @Header String simpSessionId) {
        participantService.handleJoinChat(simpSessionId, participantId, chatId);
        return null;
    }

    @MessageMapping(SEND_MESSAGE_TO_ALL)
    public void sendMessageToAll(
            @DestinationVariable("chat_id") String chatId,
            @Header("simpSessionId") String sessionId,
            @Header("file") String imageToSend,
            String messageText) {
        messageService.sendMessageToAll(chatId, messageText, sessionId, imageToSend);
    }

    @MessageMapping(SEND_MESSAGE_TO_PARTICIPANT)
    public void sendMessageToParticipant(
            String messageText,
            @DestinationVariable("chat_id") String chatId,
            @DestinationVariable("participant_id") String participantId,
            @Header("simpSessionId") String sessionId) {

        simpMessagingTemplate.convertAndSend(
                prepareFetchPersonalChatMessagesLink(chatId, participantId),
                Objects.requireNonNull(new HashMap<>().put(generateMessageDto(messageText, sessionId, chatId), null))
        );
    }

    @MessageMapping(FETCH_PAGE_CHAT_MESSAGES_REQUEST)
    public void sendFetchRequestPagedChatMessages(
            @DestinationVariable("chat_id") String chatId,
            @DestinationVariable("page") int page) {
        messageService.fetchMessagesPageForChat(chatId, page);
    }

    @SubscribeMapping(FETCH_CHAT_MESSAGES_HISTORY)
    public Page<MessageDTO> fetchPagedChatMessages() {
        return null;
    }
}
