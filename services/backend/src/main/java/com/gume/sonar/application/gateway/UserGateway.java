package com.gume.sonar.application.gateway;

import com.gume.sonar.domain.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserGateway {
    
    User save(User user);
    
    Optional<User> findById(UUID id);
    
    List<User> findAll();
    
    void deleteById(UUID id);
}
