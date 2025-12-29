package com.openclassrooms.chatop.service;

import com.openclassrooms.chatop.dto.LoginRequest;
import com.openclassrooms.chatop.dto.RegisterRequest;
import com.openclassrooms.chatop.entity.User;
import com.openclassrooms.chatop.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(RegisterRequest req) {
        userRepository.findByEmail(req.email()).ifPresent(u -> {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "EMAIL_ALREADY_USED"
            );
        });

        User u = new User();
        u.setName(req.name());
        u.setEmail(req.email());
        u.setPassword(passwordEncoder.encode(req.password()));
        return userRepository.save(u);
    }

    public User authenticate(LoginRequest req) {
        User u = userRepository.findByEmail(req.email())
                .orElseThrow(() -> new BadCredentialsException("BAD_CREDENTIALS"));

        String hash = u.getPassword();

        if (!passwordEncoder.matches(req.password(), hash)) {
            throw new BadCredentialsException("BAD_CREDENTIALS");
        }

        return u;
    }
}
