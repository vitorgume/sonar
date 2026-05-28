package com.gume.sonar.entrypoint.controller;

import com.gume.sonar.application.usecase.LoginUseCase;
import com.gume.sonar.domain.LoginResponse;
import com.gume.sonar.entrypoint.controller.dto.LoginDto;
import com.gume.sonar.entrypoint.controller.dto.LoginResponseDto;
import com.gume.sonar.entrypoint.controller.dto.ResponseDto;
import com.gume.sonar.entrypoint.mapper.LoginResponseDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginUseCase loginUseCase;

    @Value("${spring.profiles.active}")
    private String profile;

    @PostMapping
    public ResponseEntity<ResponseDto<LoginResponseDto>> login(@RequestBody LoginDto loginDto) {
        LoginResponse loginResponse = loginUseCase.execute(loginDto.getEmail(), loginDto.getPassword());

        ResponseCookie jwtCookie = ResponseCookie.from("jwt_token", loginResponse.getToken())
                .httpOnly(true)
                .secure(!profile.equals("dev"))
                .path("/")
                .maxAge(4 * 60 * 60)
                .sameSite(profile.equals("dev") ? "Strict" : "None")
                .build();

        
        LoginResponseDto responseDto = LoginResponseDtoMapper.toDto(loginResponse);

        responseDto.setToken(null);

        ResponseDto<LoginResponseDto> response = new ResponseDto<>(responseDto);
        
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
            .body(response);
    }
}
