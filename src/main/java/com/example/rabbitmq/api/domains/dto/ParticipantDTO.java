package com.example.rabbitmq.api.domains.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParticipantDTO {

    String id;

    @Builder.Default
    Instant enterAt = Instant.now();
}
