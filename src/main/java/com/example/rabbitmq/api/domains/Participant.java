package com.example.rabbitmq.api.domains;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Participant implements Serializable {

    String sessionId;

    @Builder.Default
    Instant enteredAt = Instant.now();

    String id;

    String chatId;

    String nickname;
}
