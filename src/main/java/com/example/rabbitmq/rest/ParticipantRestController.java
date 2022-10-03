package com.example.rabbitmq.rest;

import com.troupe.data.data.domains.dto.ParticipantDTO;
import com.example.rabbitmq.api.services.ParticipantService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParticipantRestController {

    private final ParticipantService participantService;

    public static final String FETCH_CHAT_PARTICIPANTS = "/api/chat/participants";

    @GetMapping(FETCH_CHAT_PARTICIPANTS)
    public ResponseEntity<List<ParticipantDTO>> fetchChats(@RequestParam("chatId") String chatId) {
        return ResponseEntity.ok(
                participantService.getParticipants(chatId)
                        .stream().map(participantService::generateParticipantDto)
                        .collect(Collectors.toList())
        );
    }
}
