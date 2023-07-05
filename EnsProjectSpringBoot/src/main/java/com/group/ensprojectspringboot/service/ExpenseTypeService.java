package com.group.ensprojectspringboot.service;

import com.group.ensprojectspringboot.model.ExpenseType;
import com.group.ensprojectspringboot.model.Source;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ExpenseTypeService {
    ResponseEntity<String> createExpenseType(Map<String, String> request);

    ResponseEntity<List<ExpenseType>> getExpenseType();

    ResponseEntity<String> deleteExpenseType(Long id);

    ResponseEntity<String> updateExpenseType(Map<String, String> request);
}
