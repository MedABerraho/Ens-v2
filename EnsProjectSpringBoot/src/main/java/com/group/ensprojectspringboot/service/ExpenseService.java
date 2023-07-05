package com.group.ensprojectspringboot.service;

import com.group.ensprojectspringboot.model.Expense;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ExpenseService {
    ResponseEntity<String> createExpense(Map<String, String> request);

    ResponseEntity<List<Expense>> getExpenses();

    ResponseEntity<String> deleteExpense(Long id);

    ResponseEntity<String> updateExpense(Map<String, String> request);
}
