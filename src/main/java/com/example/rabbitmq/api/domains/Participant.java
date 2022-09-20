package com.example.rabbitmq.api.domains;

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
public class Participant implements Serializable {

    String sessionId;

    @Builder.Default
    Instant enteredAt = Instant.now();

    @Builder.Default
    String id = UUID.randomUUID().toString().substring(0,8);

    String chatId;

    String nickname;
}
