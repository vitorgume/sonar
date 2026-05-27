package com.gume.sonar.application.usecase;

import com.gume.sonar.application.gateway.UserGateway;
import com.gume.sonar.domain.User;
import com.gume.sonar.domain.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserUseCase {

    private final UserGateway userGateway;

    public User create(User user) {
        return userGateway.save(user);
    }

    public User findById(UUID id) {
        return userGateway.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public List<User> findAll() {
        return userGateway.findAll();
    }

    public User findByEmail(String email) {
        return userGateway.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(null)); // You might want to create a specific exception or use a generic one, but for now we throw UserNotFoundException
    }

    public User update(UUID id, User user) {
        User existingUser = findById(id);
        
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        
        return userGateway.save(existingUser);
    }

    public void delete(UUID id) {
        User existingUser = findById(id);
        userGateway.deleteById(existingUser.getId());
    }
}
