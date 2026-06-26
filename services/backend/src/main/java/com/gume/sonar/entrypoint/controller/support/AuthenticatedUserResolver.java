package com.gume.sonar.entrypoint.controller.support;

import com.gume.sonar.application.usecase.UserUseCase;
import com.gume.sonar.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticatedUserResolver {

    private final UserUseCase userUseCase;

    public User resolve(Authentication authentication) {
        log.info("Resolvendo usuário: {}, {}", authentication.getName(), authentication.getPrincipal().toString());
        return userUseCase.findByEmail(authentication.getPrincipal().toString());
    }
}
