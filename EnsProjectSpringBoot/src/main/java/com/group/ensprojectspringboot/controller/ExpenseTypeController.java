package com.group.ensprojectspringboot.controller;

import com.group.ensprojectspringboot.consts.EnsConsts;
import com.group.ensprojectspringboot.model.ExpenseType;
import com.group.ensprojectspringboot.model.Source;
import com.group.ensprojectspringboot.service.ExpenseTypeService;
import com.group.ensprojectspringboot.service.SourceService;
import com.group.ensprojectspringboot.utils.EnsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/expenseType")
@CrossOrigin
public class ExpenseTypeController {
    @Autowired
    ExpenseTypeService expenseTypeService;


    @PostMapping("/add")
    public ResponseEntity<String> createExpensetype(@RequestBody Map<String, String> request) {
        try {
            return expenseTypeService.createExpenseType(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EnsUtils.getResponseEntity(EnsConsts.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping
    public ResponseEntity<List<ExpenseType>> getExpenseType() {
        try {
            return expenseTypeService.getExpenseType();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<List<ExpenseType>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping(path = "/delete/{id}")
    public ResponseEntity<String> deleteExpenseType(@PathVariable Long id) {
        try {
            return expenseTypeService.deleteExpenseType(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EnsUtils.getResponseEntity(EnsConsts.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping(path = "/update")
    public ResponseEntity<String> updateExpenseType(@RequestBody Map<String, String> request) {
        try {
            return expenseTypeService.updateExpenseType(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EnsUtils.getResponseEntity(EnsConsts.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
