package com.openclassrooms.chatop.service;

import com.openclassrooms.chatop.entity.Message;
import com.openclassrooms.chatop.repository.MessageRepository;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void create(Message m) {
        messageRepository.save(m);
    }
}
