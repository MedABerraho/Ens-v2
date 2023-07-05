package com.group.ensprojectspringboot.consts;

public class EnsConsts {

    public static final String SOMETHING_WENT_WRONG = "Something went Wrong";

    public static final String INVALID_DATA = "Invalid Data";

    public static final String INVALID_ID = "Invalid id: ";

    public static final String UNAUTHORIZED_ACCESS = "Unauthorized access.";

    public static final String UPDATE_SOURCE = "UPDATE Source s SET s.sourceName =:sourceName, s.sourceDescription =:sourceDescription WHERE s.id =:sourceId";
    public static final String UPDATE_BURSARSHIP = "UPDATE Bursarship b SET b.bursaryName =:bursaryName, b.amount =:amount, b.sourceId =:sourceId WHERE b.id =:bursarshipId";
    public static final String UPDATE_EXPENSE = "UPDATE Expense e SET e.expenseName=:expenseName, e.amount=:amount, e.expenseType=:expenseType, e.bursaryId=:bursary WHERE e.id=:expenseId";
}