package com.group.ensprojectspringboot.service;

import com.group.ensprojectspringboot.model.Source;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface SourceService {
    ResponseEntity<String> createSource(Map<String, String> request);

    ResponseEntity<List<Source>> getSource();

    ResponseEntity<String> deleteSource(Long id);

    ResponseEntity<String> updateSource(Map<String, String> request);
}
