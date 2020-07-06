package com.project.expensetracker.service;

import java.util.Optional;


import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.User;

import com.project.expensetracker.entity.Customer;
import com.project.expensetracker.errorhandler.ResponseObject;

public interface CustomerService {
	
	ResponseObject addUser(Customer customer);
	String findEmailId(String userId);
	ResponseObject login(String email, String password);	
	Optional findByToken(String token);
	ResponseObject emailExistForgetPassword(String email);
	ResponseObject changePassword(String email, String newPassword);
	
	
}
