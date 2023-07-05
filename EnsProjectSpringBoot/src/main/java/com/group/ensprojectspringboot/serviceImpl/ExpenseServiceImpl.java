package com.group.ensprojectspringboot.serviceImpl;

import com.group.ensprojectspringboot.configuration.JwtFilter;
import com.group.ensprojectspringboot.consts.EnsConsts;
import com.group.ensprojectspringboot.model.Bursarship;
import com.group.ensprojectspringboot.model.Expense;
import com.group.ensprojectspringboot.model.ExpenseType;
import com.group.ensprojectspringboot.repository.BursarshipRepository;
import com.group.ensprojectspringboot.repository.ExpenseRepository;
import com.group.ensprojectspringboot.repository.ExpenseTypeRepository;
import com.group.ensprojectspringboot.service.BursarshipService;
import com.group.ensprojectspringboot.service.ExpenseService;
import com.group.ensprojectspringboot.utils.EnsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    ExpenseRepository expenseRepository;

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    BursarshipRepository bursarshipRepository;

    @Autowired
    BursarshipService bursarshipService;

    @Autowired
    ExpenseTypeRepository expenseTypeRepository;

    @Override
    public ResponseEntity<String> createExpense(Map<String, String> request) {
        try {
            if (jwtFilter.isAdmin()) {
                if (isExpenseValid(request.get("expenseName"), request.get("expenseTypeId"), Double.valueOf(request.get("amount")),
                        Long.valueOf(request.get("bursarship")))) {
                    expenseRepository.save(getExpenseFromMap(request));
                    return EnsUtils.getResponseEntity("Expense is added Successfully!", HttpStatus.OK);
                }
                return EnsUtils.getResponseEntity(EnsConsts.INVALID_DATA, HttpStatus.OK);
            } else {
                return EnsUtils.getResponseEntity(EnsConsts.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EnsUtils.getResponseEntity(EnsConsts.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean isExpenseValid(String expenseName, String expenseTypeId, Double amount, Long bursarship) throws Exception {
        if (bursarship == null)
            return false;
        if (expenseName == null || expenseName.isEmpty())
            return false;

        if (expenseTypeId == null || expenseTypeId.isEmpty())
            return false;

        if (amount > getTotalBursaryAmount())
            return false;
        return true;
    }

    private Double getTotalBursaryAmount() throws Exception {
        ResponseEntity<List<Bursarship>> response = bursarshipService.getBursary();
        List<Bursarship> bursarships = response.getBody();
        Double totalBursaryAmount = Double.valueOf(0);
        if (bursarships != null && !bursarships.isEmpty()) {
            for (Bursarship bursarship : bursarships) {
                totalBursaryAmount += bursarship.getAmount();
            }
        } else {
            throw new Exception("The bursary body is null or empty");
        }

        return totalBursaryAmount;
    }

    private Expense getExpenseFromMap(Map<String, String> request) {
        Expense expense = new Expense();
        expense.setExpenseName(request.get("expenseName"));
        expense.setExpensedOn(LocalDate.now());
        expense.setAmount(Double.valueOf(request.get("amount")));
        Optional<ExpenseType> expenseTypeOptional = expenseTypeRepository.findById(Long.valueOf(request.get("expenseTypeId")));
        ExpenseType expenseType = expenseTypeOptional.get();
        expense.setExpenseType(expenseType);
        Optional<Bursarship> bursarshipOptional = bursarshipRepository.findById(Long.valueOf(request.get("bursarship")));
        Bursarship bursarship = bursarshipOptional.get();
        expense.setBursarship(bursarship);
        return expense;
    }


    @Override
    public ResponseEntity<List<Expense>> getExpenses() {
        try {
            return new ResponseEntity<List<Expense>>(expenseRepository.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<List<Expense>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteExpense(Long id) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional<?> optional = expenseRepository.findById(id);
                if (optional.isPresent()) {
                    expenseRepository.deleteById(id);
                    return EnsUtils.getResponseEntity("Expense deleted successfully", HttpStatus.OK);
                } else {
                    return EnsUtils.getResponseEntity("Expense id does not exist", HttpStatus.OK);
                }
            } else {
                return EnsUtils.getResponseEntity(EnsConsts.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EnsUtils.getResponseEntity(EnsConsts.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    @Transactional
    public ResponseEntity<String> updateExpense(Map<String, String> request) {
        try {
            if (jwtFilter.isAdmin()) {
                if (isExpenseValid(request.get("expenseName"), request.get("expenseTypeId"), Double.valueOf(request.get("amount")),
                        Long.valueOf(request.get("bursaryId")))) {
                    Long expenseId = Long.valueOf(request.get("id"));
                    Optional<Expense> expenseOptional = expenseRepository.findById(expenseId);
                    if (expenseOptional.isPresent()) {
                        Expense expense = getExpenseFromMap(request);
                        expenseRepository.updateExpense(expenseId, expense.getExpenseName(), expense.getAmount(), expense.getExpenseType(), expense.getBursarship());
                        return EnsUtils.getResponseEntity("Expense updated successfully", HttpStatus.OK);
                    } else {
                        return EnsUtils.getResponseEntity("Expense does not exist!", HttpStatus.OK);
                    }
                } else {
                    return EnsUtils.getResponseEntity(EnsConsts.INVALID_DATA, HttpStatus.OK);
                }
            } else {
                return EnsUtils.getResponseEntity(EnsConsts.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EnsUtils.getResponseEntity(EnsConsts.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
