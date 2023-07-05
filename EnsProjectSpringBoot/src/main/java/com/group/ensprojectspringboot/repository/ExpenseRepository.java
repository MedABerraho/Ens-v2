package com.group.ensprojectspringboot.repository;

import com.group.ensprojectspringboot.consts.EnsConsts;
import com.group.ensprojectspringboot.model.Bursarship;
import com.group.ensprojectspringboot.model.Expense;
import com.group.ensprojectspringboot.model.ExpenseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    @Modifying
    @Query(EnsConsts.UPDATE_EXPENSE)
    void updateExpense(@Param("expenseId") Long expenseId,@Param("expenseName")  String expenseName,
                       @Param("amount")  Double amount,@Param("expenseType")  ExpenseType expenseType,@Param("bursary")  Bursarship bursary);
}
