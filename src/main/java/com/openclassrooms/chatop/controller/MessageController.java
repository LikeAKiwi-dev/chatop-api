package com.openclassrooms.chatop.controller;

import com.openclassrooms.chatop.dto.MessageRequest;
import com.openclassrooms.chatop.dto.MessageResponse;
import com.openclassrooms.chatop.entity.Message;
import com.openclassrooms.chatop.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponse create(@Valid @RequestBody MessageRequest req) {

        Message m = new Message();
        m.setMessage(req.message());
        m.setUserId(req.user_id());
        m.setRentalId(req.rental_id());

        messageService.create(m);

        return new MessageResponse("Message sent with success");
    }
}
