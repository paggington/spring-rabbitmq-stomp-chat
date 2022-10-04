package com.example.rabbitmq.api.domains.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;
import reactor.util.function.Tuple2;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageDTO implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    String id;

    String sentFrom;

    String text;

    String chatId;

    @ElementCollection
    private Set<Tuple2<String,String>> byteContentNames = new HashSet<>();

    @Builder.Default
    Boolean haveByteContent = false;

    @Builder.Default
    Long createdAt = new Date(System.currentTimeMillis()).getTime();
}
