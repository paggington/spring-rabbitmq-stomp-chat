package com.example.rabbitmq.data.service.impl;

import com.example.rabbitmq.api.domains.dto.MessageDTO;
import com.example.rabbitmq.data.repo.MessageRepository;
import com.example.rabbitmq.data.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@Repository
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Override
    public Page<MessageDTO> fetchMessagesForChat(String chatId, int page) {
        Pageable pageable = PageRequest.of(page, 20);

        return messageRepository.getMessageDTOByChatIdOrderByCreatedAtDesc(pageable, chatId);
    }

    @Override
    public MessageDTO saveMessage(MessageDTO messageDTO) {
        return messageRepository.save(messageDTO);
    }

    @Override
    public MessageDTO getMessageById(String id) {
        return messageRepository.findById(id).get();
    }
}
