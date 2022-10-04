package com.example.rabbitmq.data.service;

import com.example.rabbitmq.api.domains.dto.MessageDTO;
import org.springframework.data.domain.Page;

public interface MessageService {

    Page<MessageDTO> fetchMessagesForChat(String chatId, int offset);

    MessageDTO saveMessage(MessageDTO messageDTO);

    MessageDTO getMessageById(String id);
}
