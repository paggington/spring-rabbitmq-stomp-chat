package com.example.rabbitmq.api.domains.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
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
    //yep yep i gonna save incoming file in plain string...
    String file;

    @Builder.Default
    Long createdAt = new Date(System.currentTimeMillis()).getTime();
}
