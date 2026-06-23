package com.gume.sonar.infrastructure.provider;

import com.gume.sonar.application.gateway.JwtGateway;
import com.gume.sonar.domain.User;
import com.gume.sonar.infrastructure.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtDataProvider implements JwtGateway {

    private final JwtUtil jwtUtil;

    @Override
    public String generateToken(User user) {
        String userId = user.getId() == null ? null : user.getId().toString();
        return jwtUtil.generateToken(user.getEmail(), userId);
    }
}
