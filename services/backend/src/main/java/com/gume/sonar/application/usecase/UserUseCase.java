package com.gume.sonar.application.usecase;

import com.gume.sonar.domain.User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class UserUseCase {

    public User create(User user) {
        // To be implemented
        return null;
    }

    public User findById(UUID id) {
        // To be implemented
        return null;
    }

    public List<User> findAll() {
        // To be implemented
        return Collections.emptyList();
    }

    public User update(UUID id, User user) {
        // To be implemented
        return null;
    }

    public void delete(UUID id) {
        // To be implemented
    }
}
