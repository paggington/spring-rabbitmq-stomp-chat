package com.example.rabbitmq.api.services;

import com.example.rabbitmq.api.domains.dto.MessageDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageService {
    public static MessageDTO generateMessageDto(String messageText, String sessionId) {
        return MessageDTO
                .builder()
                .text(messageText)
                .from(sessionId).build();
    }
}
