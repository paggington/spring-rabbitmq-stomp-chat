package com.example.rabbitmq.rest;

import com.example.rabbitmq.api.domains.dto.chat.ChatDTO;
import com.example.rabbitmq.api.services.ChatService;
import com.example.rabbitmq.api.services.ParticipantService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatRestController {

    private final ChatService chatService;

    private final ParticipantService participantService;

    public static final String FETCH_CHATS = "/api/chats";

    @GetMapping(FETCH_CHATS)
    public ResponseEntity<List<ChatDTO>> fetchChats() {
        return ResponseEntity.ok(chatService.getChats().stream().map(chatService::generateChatDto).collect(Collectors.toList()));
    }
}
