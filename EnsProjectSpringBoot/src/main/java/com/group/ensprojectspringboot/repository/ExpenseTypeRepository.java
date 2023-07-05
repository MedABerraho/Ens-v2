package com.group.ensprojectspringboot.repository;

import com.group.ensprojectspringboot.consts.EnsConsts;
import com.group.ensprojectspringboot.model.ExpenseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseTypeRepository extends JpaRepository<ExpenseType, Long> {

    @Modifying
    @Query(EnsConsts.UPDATE_EXPENSE_TYPE)
    void updateExpenseType(@Param("expenseTypeId") Long expenseTypeId, @Param("name") String name);
}