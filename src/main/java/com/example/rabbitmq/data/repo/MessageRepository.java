package com.example.rabbitmq.data.repo;

import com.example.rabbitmq.api.domains.dto.MessageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<MessageDTO,String> {

    Page<MessageDTO> getMessageDTOByChatIdOrderByCreatedAtDesc(Pageable pageable,String chatId);
}
