package com.gume.sonar.entrypoint.controller.support;

import com.gume.sonar.application.usecase.UserUseCase;
import com.gume.sonar.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticatedUserResolver {

    private final UserUseCase userUseCase;

    public User resolve(Authentication authentication) {
        return userUseCase.findByEmail(authentication.getPrincipal().toString());
    }
}
