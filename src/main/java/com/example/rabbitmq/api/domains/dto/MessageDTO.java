package com.example.rabbitmq.api.domains.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageDTO {

    String from;

    String text;

    @Builder.Default
    Instant createdAt = Instant.now();
}
