package com.gume.sonar.entrypoint.controller;

import com.gume.sonar.application.usecase.UserUseCase;
import com.gume.sonar.domain.User;
import com.gume.sonar.entrypoint.controller.dto.ResponseDto;
import com.gume.sonar.entrypoint.controller.dto.UserDto;
import com.gume.sonar.entrypoint.mapper.UserDtoMapper;
import com.gume.sonar.entrypoint.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserUseCase userUseCase;
    private final CurrentUser currentUser;

    @PostMapping
    public ResponseEntity<ResponseDto<UserDto>> create(@RequestBody UserDto userDto) {
        User user = UserDtoMapper.toDomain(userDto);
        User createdUser = userUseCase.create(user);
        ResponseDto<UserDto> response = new ResponseDto<>(UserDtoMapper.toDto(createdUser));
        return ResponseEntity.created(
            UriComponentsBuilder.newInstance()
                    .path("/{id}")
                    .buildAndExpand(createdUser.getId())
                    .toUri()
        ).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<UserDto>> findById(@PathVariable UUID id) {
        UUID currentUserId = currentUser.getId();
        if (currentUserId == null || !currentUserId.equals(id)) {
            return ResponseEntity.notFound().build();
        }

        User user = userUseCase.findById(currentUserId);
        ResponseDto<UserDto> response = new ResponseDto<>(UserDtoMapper.toDto(user));
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<UserDto>>> findAll() {
        UUID currentUserId = currentUser.getId();
        List<UserDto> users = currentUserId == null
                ? List.of()
                : List.of(UserDtoMapper.toDto(userUseCase.findById(currentUserId)));
        ResponseDto<List<UserDto>> response = new ResponseDto<>(users);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<UserDto>> update(@PathVariable UUID id, @RequestBody UserDto userDto) {
        UUID currentUserId = currentUser.getId();
        if (currentUserId == null || !currentUserId.equals(id)) {
            return ResponseEntity.notFound().build();
        }
        User user = UserDtoMapper.toDomain(userDto);
        User updatedUser = userUseCase.update(id, user);
        ResponseDto<UserDto> response = new ResponseDto<>(UserDtoMapper.toDto(updatedUser));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        UUID currentUserId = currentUser.getId();
        if (currentUserId == null || !currentUserId.equals(id)) {
            return ResponseEntity.notFound().build();
        }
        userUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
