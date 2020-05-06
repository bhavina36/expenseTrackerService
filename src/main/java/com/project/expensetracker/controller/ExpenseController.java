package com.project.expensetracker.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.expensetracker.entity.Expense;
import com.project.expensetracker.errorhandler.ResponseObject;
import com.project.expensetracker.service.ExpenseServiceImpl;
import com.project.expensetracker.util.HttpStatusUtil;

@RestController
@RequestMapping("/expense")
@CrossOrigin
public class ExpenseController {
	
	@Autowired
	private ExpenseServiceImpl expenseService;
		
	@RequestMapping(value= "/addExpense", method = RequestMethod.POST)
	public ResponseEntity<ResponseObject> addExpense(@RequestBody Expense expense) {	
		
		
      ResponseObject obj = expenseService.addExpense(expense);
      
		return new ResponseEntity<ResponseObject>(obj, HttpStatusUtil.getStatus(obj.getStatusCode()));		
	}
		
	@RequestMapping(value= "/updateExpense/{id}", method = RequestMethod.GET)
	public ResponseEntity<ResponseObject> getUpdateExpense(@PathVariable("id") String id) {		
				
		ResponseObject obj = expenseService.getUpdateExpense(id);
		
		return new ResponseEntity<ResponseObject>(obj,HttpStatusUtil.getStatus(obj.getStatusCode()));
	}
	
	@RequestMapping(value= "/updateExpense/{id}", method = RequestMethod.POST)	
	public ResponseEntity<ResponseObject> updateExpense(@PathVariable("id") String id, @RequestBody Expense expense) {		
				
		ResponseObject obj = expenseService.updateExpense(id, expense);
		
		return new ResponseEntity<ResponseObject>(obj,HttpStatusUtil.getStatus(obj.getStatusCode()));
	}
	
	@RequestMapping(value= "/deleteExpense/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseObject> deleteExpense(@PathVariable("id") String id) {		
				
		ResponseObject obj = expenseService.deleteExpense(id);
		
		return new ResponseEntity<ResponseObject>(obj,HttpStatusUtil.getStatus(obj.getStatusCode()));
	}
	
	@RequestMapping(value= "/listExpense/{userId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseObject> ListExpense(@PathVariable("userId")String userId) {		
		
		ResponseObject obj = expenseService.listAllExpense(userId);
		return new ResponseEntity<ResponseObject>(obj,HttpStatusUtil.getStatus(obj.getStatusCode()));
	}
	
	@RequestMapping(value= "/listCategoryExpense/{userId}/category/{categoryName}", method = RequestMethod.GET)
	public ResponseEntity<ResponseObject> ListExpenseCategory(@PathVariable("userId")String userId,@PathVariable("categoryName")String categoryName) {		
		
		ResponseObject obj = expenseService.listExpenseBasedOnCategory(userId, categoryName);
		return new ResponseEntity<ResponseObject>(obj,HttpStatusUtil.getStatus(obj.getStatusCode()));
	}
	
	@RequestMapping(value= "/percentageCategory/{userId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseObject> percentageCategory(@PathVariable("userId")String userId) {		
		
		ResponseObject obj =  expenseService.findPercentageOfCategory(userId);		
		return new ResponseEntity<ResponseObject>(obj,HttpStatusUtil.getStatus(obj.getStatusCode()));
	}

}
