package com.group.ensprojectspringboot.serviceImpl;

import com.group.ensprojectspringboot.configuration.JwtFilter;
import com.group.ensprojectspringboot.consts.EnsConsts;
import com.group.ensprojectspringboot.model.ExpenseType;
import com.group.ensprojectspringboot.model.Source;
import com.group.ensprojectspringboot.repository.ExpenseTypeRepository;
import com.group.ensprojectspringboot.repository.SourceRepository;
import com.group.ensprojectspringboot.service.ExpenseTypeService;
import com.group.ensprojectspringboot.utils.EnsUtils;
import com.zaxxer.hikari.util.FastList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class ExpenseTypeServiceImpl implements ExpenseTypeService {

    @Autowired
    ExpenseTypeRepository expenseTypeRepository;

    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> createExpenseType(Map<String, String> request) {
        try {
            if (jwtFilter.isAdmin()) {
                if (isExpenseTypeValid(request.get("name"))) {
                    expenseTypeRepository.save(getExpenseTypeFromMap(request));
                    return EnsUtils.getResponseEntity("ExpenseType is added Successfully!", HttpStatus.OK);
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

    private ExpenseType getExpenseTypeFromMap(Map<String, String> request) {
        ExpenseType expenseType = new ExpenseType();
        expenseType.setName(request.get("name"));
        return expenseType;
    }
    public boolean isExpenseTypeValid(String expenseTypeName){
        if (expenseTypeName == null || expenseTypeName.isEmpty())
            return false;
        return true;
    }

    @Override
    public ResponseEntity<List<ExpenseType>> getExpenseType() {
        try {
            return new ResponseEntity<List<ExpenseType>>(expenseTypeRepository.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<List<ExpenseType>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }



    @Override
    public ResponseEntity<String> deleteExpenseType(Long id) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional<?> optional = expenseTypeRepository.findById(id);
                if (optional.isPresent()) {
                    expenseTypeRepository.deleteById(id);
                    return EnsUtils.getResponseEntity("ExpenseType deleted successfully", HttpStatus.OK);
                } else {
                    return EnsUtils.getResponseEntity("ExpenseType id does not exist", HttpStatus.OK);
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
    public ResponseEntity<String> updateExpenseType(Map<String, String> request) {
        try {
            if (jwtFilter.isAdmin()) {
                if (isExpenseTypeValid(request.get("name"))) {
                    Long expenseTypeId = Long.valueOf(request.get("id"));
                    Optional<ExpenseType> expenseTypeOptional = expenseTypeRepository.findById(expenseTypeId);
                    if (expenseTypeOptional.isPresent()) {
                        ExpenseType expenseType = getExpenseTypeFromMap(request);
                        expenseTypeRepository.updateExpenseType(expenseTypeId, expenseType.getName());
                        return EnsUtils.getResponseEntity("ExpenseType updated successfully", HttpStatus.OK);
                    } else {
                        return EnsUtils.getResponseEntity("ExpenseType does not exist!", HttpStatus.OK);
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
