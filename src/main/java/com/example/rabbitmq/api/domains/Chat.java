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
public class Chat implements Serializable {
    @Builder.Default
    String id = UUID.randomUUID().toString().substring(0, 4);

    String name;

    @Builder.Default
    Long createdAt = Instant.now().toEpochMilli();
}
