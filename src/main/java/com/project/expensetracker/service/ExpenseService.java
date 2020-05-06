package com.project.expensetracker.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.project.expensetracker.entity.Expense;
import com.project.expensetracker.errorhandler.ResponseObject;

public interface ExpenseService {
	
	Optional<Expense> findExpense(String id);
	ResponseObject addExpense(Expense expense);
	ResponseObject getUpdateExpense(String id);
	ResponseObject updateExpense(String id, Expense expense);		
	ResponseObject deleteExpense(String id);
	
	ResponseObject listAllExpense(String userEmailId);
	ResponseObject listExpenseBasedOnCategory(String userId, String categoryName);
	ResponseObject findPercentageOfCategory(String userId);

}
