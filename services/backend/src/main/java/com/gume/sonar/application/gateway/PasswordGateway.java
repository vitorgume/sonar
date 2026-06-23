package com.gume.sonar.application.gateway;

public interface PasswordGateway {

    String hash(String rawPassword);

    boolean matches(String rawPassword, String hashedPassword);
}

