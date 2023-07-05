package com.group.ensprojectspringboot.service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface UserService {
    ResponseEntity<String> signUp(Map<String, String> request);

    ResponseEntity<String> login(Map<String, String> request);
}
