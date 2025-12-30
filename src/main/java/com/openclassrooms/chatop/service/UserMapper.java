package com.openclassrooms.chatop.service;

import com.openclassrooms.chatop.dto.UserResponse;
import com.openclassrooms.chatop.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toDto(User u) {
        return new UserResponse(
                u.getId(),
                u.getName(),
                u.getEmail(),
                u.getCreatedAt(),
                u.getUpdatedAt()
        );
    }
}
