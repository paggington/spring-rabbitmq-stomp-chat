package com.example.rabbitmq.api.ws;

import com.example.rabbitmq.api.domains.dto.MessageDTO;
import com.example.rabbitmq.api.domains.dto.ParticipantDTO;
import com.example.rabbitmq.api.services.ParticipantService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ParticipantWsController {

    private final ParticipantService participantService;

    private final SimpMessagingTemplate simpMessagingTemplate;

    public static final String FETCH_PARTICIPANT_JOINED_CHAT_EVENT = "/topic/chat.{chat_id}.participant.join";
    public static final String FETCH_PARTICIPANT_LEAVED_CHAT_EVENT = "/topic/chat.{chat_id}.participant.leave";

    @SubscribeMapping(FETCH_PARTICIPANT_JOINED_CHAT_EVENT)
    public ParticipantDTO fetchParticipantJoinedChatEvent() {
        return null;
    }

    @SubscribeMapping(FETCH_PARTICIPANT_LEAVED_CHAT_EVENT)
    public ParticipantDTO fetchParticipantLeavedChatEvent() {
        return null;
    }

    public static String prepareFetchParticipantJoinedChatEvent(String chatId) {
        return FETCH_PARTICIPANT_JOINED_CHAT_EVENT.replace("{chat_id}", chatId);
    }

    public static String prepareFetchParticipantLeavedChatEvent(String chatId) {
        return FETCH_PARTICIPANT_LEAVED_CHAT_EVENT.replace("{chat_id}", chatId);
    }
}
