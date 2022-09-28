package com.example.rabbitmq.api.services;

import com.example.rabbitmq.api.domains.Chat;
import com.example.rabbitmq.api.domains.dto.chat.ChatDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static com.example.rabbitmq.api.ws.ChatWsController.FETCH_CREATE_CHAT_EVENT;
import static com.example.rabbitmq.api.ws.ChatWsController.FETCH_DELETE_CHAT_EVENT;

@Log4j2
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final SetOperations<String, Chat> chatSetOperations;

    private static final String KEY = "chats";

    public void createChat(String chatName) {
        Chat chat = Chat
                .builder()
                .name(chatName).build();

        chatSetOperations.add(KEY, chat);

        simpMessagingTemplate.convertAndSend(FETCH_CREATE_CHAT_EVENT,
                generateChatDto(chat)
        );
    }

    public void deleteChat(String chatId) {
        getChats()
                .stream()
                .filter(chat -> Objects.equals(chat.getId(), chatId)).findAny()
                .ifPresent(chat -> {
                    chatSetOperations.remove(KEY, chat);

                    log.info(String.format("Chat %s has been deleted.", chat.getName()));

                    simpMessagingTemplate.convertAndSend(FETCH_DELETE_CHAT_EVENT,
                            generateChatDto(chat)
                    );
                });
    }

    public Set<Chat> getChats() {
        return Optional.ofNullable(chatSetOperations.members(KEY)).orElseGet(HashSet::new);
    }

    public ChatDTO generateChatDto(Chat chat) {
        return ChatDTO.builder()
                .id(chat.getId())
                .createdAt(chat.getCreatedAt())
                .name(chat.getName()).build();
    }
}
