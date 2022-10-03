package com.troupe.fileserver.resource.messaging.resource;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public class MessagingFileController {

    private static final String SAVE_FILE = "/topic.file.save.{messageId}";

    @MessageMapping(SAVE_FILE)
    public void saveMessageFile(@RequestParam("file") MultipartFile multipartFile,
                                @DestinationVariable("messageId") String messageId) {

    }
}
