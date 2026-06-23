package com.gume.sonar.application.usecase;

import com.gume.sonar.application.gateway.JwtGateway;
import com.gume.sonar.application.gateway.PasswordGateway;
import com.gume.sonar.domain.LoginResponse;
import com.gume.sonar.domain.User;
import com.gume.sonar.domain.exception.InvalidCredentialsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUseCase {

    private final UserUseCase userUseCase;
    private final JwtGateway jwtGateway;
    private final PasswordGateway passwordGateway;

    public LoginResponse execute(String email, String password) {
        User user;
        try {
            user = userUseCase.findByEmail(email);
        } catch (Exception e) {
            throw new InvalidCredentialsException();
        }

        if (user.getPassword() == null || !passwordGateway.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String token = jwtGateway.generateToken(user);

        return LoginResponse.builder()
                .userId(user.getId())
                .name(user.getName())
                .token(token)
                .build();
    }
}
