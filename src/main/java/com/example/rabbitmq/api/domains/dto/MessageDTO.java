package com.example.rabbitmq.api.domains.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageDTO implements Serializable {
    @Builder.Default
    String id = UUID.randomUUID().toString().substring(0, 9);

    String from;

    String text;

    @Builder.Default
    Instant createdAt = Instant.now();
}
