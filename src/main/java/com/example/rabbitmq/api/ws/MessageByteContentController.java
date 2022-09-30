package com.example.rabbitmq.api.ws;

import com.example.rabbitmq.api.domains.dto.MessageDTO;
import com.example.rabbitmq.api.services.MessageService;
import com.example.rabbitmq.data.service.impl.MessageServiceImpl;
import com.example.rabbitmq.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.model.relational.Database;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.util.function.Tuple2;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Set;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageByteContentController {

    private final MessageService messageService;

    private final MessageServiceImpl messageServiceDatabase;

    private static final String FETCH_MESSAGE_CONTENT = "/fetch";
    private static final String LOAD_MESSAGE_CONTENT = "/load";

    @GetMapping(FETCH_MESSAGE_CONTENT)
    public ResponseEntity<Blob> fetchMessageContent(@RequestParam("messageId") String messageId) {
        String messageContentBase64 = messageService.fetchMessageContent(messageId).split(",")[1];
        if (!isNull(messageContentBase64) && !messageContentBase64.isEmpty()) {
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

    //using for editing message and adding byte content
    @PostMapping(LOAD_MESSAGE_CONTENT)
    public ResponseEntity<Boolean> loadMessageContent(
            @RequestParam("file") MultipartFile multipartFile,
            @RequestParam("messageId") String messageId) {
        if (!isNull(multipartFile) && !multipartFile.isEmpty()) {
            MessageDTO messageDTO = messageServiceDatabase.getMessageById(messageId);
            if (!isNull(messageDTO)) {
                Tuple2<String, String> savedFileData = FileUtils.loadFileToDirectory(multipartFile, messageDTO);
                if (!isNull(savedFileData) && savedFileData.iterator().hasNext()) {
                    Set<Tuple2<String, String>> messageFileNames = messageDTO.getByteContentNames();
                    messageFileNames.add(savedFileData);
                    return ResponseEntity.ok(true);
                }
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
    }
}
