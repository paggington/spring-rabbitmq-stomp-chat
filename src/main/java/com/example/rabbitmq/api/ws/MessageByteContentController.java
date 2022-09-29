package com.example.rabbitmq.api.ws;

import com.example.rabbitmq.api.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.model.relational.Database;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageByteContentController {

    private final MessageService messageService;

    private static final String FETCH_MESSAGE_CONTENT = "/fetch";

    @GetMapping(FETCH_MESSAGE_CONTENT)
    @Cacheable(cacheNames = {"image"},sync = true)
    public ResponseEntity<Blob> fetchMessageContent(@RequestParam("messageId") String messageId) {
        String messageContentBase64 = messageService.fetchMessageContent(messageId).split(",")[1];
        if(!isNull(messageContentBase64) && !messageContentBase64.isEmpty()){
            byte[] decodedByte = Base64.getDecoder().decode(messageContentBase64);
            try {
                Blob blob = new SerialBlob(decodedByte);
                return ResponseEntity.ok(blob);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
