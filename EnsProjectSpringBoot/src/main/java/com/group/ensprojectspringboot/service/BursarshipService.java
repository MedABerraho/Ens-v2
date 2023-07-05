package com.group.ensprojectspringboot.service;


import com.group.ensprojectspringboot.model.Bursarship;
import com.group.ensprojectspringboot.model.Source;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface BursarshipService {
    ResponseEntity<String> createBursary(Map<String, String> request);

    ResponseEntity<List<Bursarship>> getBursary();

    ResponseEntity<String> deleteBursary(Long id);

    ResponseEntity<String> updateBursary(Map<String, String> request);
}
