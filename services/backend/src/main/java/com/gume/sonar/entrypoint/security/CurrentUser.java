package com.gume.sonar.entrypoint.security;

import com.gume.sonar.application.usecase.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CurrentUser {

    private final UserUseCase userUseCase;

    public UUID getId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            return null;
        }

        String authName = authentication.getName();
        try {
            return UUID.fromString(authName);
        } catch (IllegalArgumentException ex) {
            return userUseCase.findByEmail(authName).getId();
        }
    }
}

