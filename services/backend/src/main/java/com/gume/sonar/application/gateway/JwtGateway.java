package com.gume.sonar.application.gateway;

import com.gume.sonar.domain.User;

public interface JwtGateway {
    
    String generateToken(User user);
    
}
