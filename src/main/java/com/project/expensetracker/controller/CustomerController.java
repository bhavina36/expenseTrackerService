package com.project.expensetracker.controller;

import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;

import javax.validation.constraints.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.expensetracker.entity.Customer;
import com.project.expensetracker.errorhandler.ResponseObject;
import com.project.expensetracker.service.CustomerService;
import com.project.expensetracker.util.HttpStatusUtil;

@RestController
@RequestMapping("/Auth")
@CrossOrigin
public class CustomerController {

	@Autowired
	private CustomerService userService;
	
	
	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<ResponseObject> addUser(@RequestBody Customer customer) {
		

		ResponseObject obj = userService.addUser(customer);	
			
		return new ResponseEntity<ResponseObject>(obj, HttpStatusUtil.getStatus(obj.getStatusCode()));

	}

	
	//public ResponseEntity<ResponseObject> login(@PathVariable("email") String email,@PathVariable("password") String password) {
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<ResponseObject> login(@RequestBody Customer user) {
		
		//String tempPassword = "$2a$10$YspLQ7Kyr1mL0dWunJTw5.5fpeRX1VExSbrig4m.CGW1Mv1yir7OC";
		
		ResponseObject obj =  userService.login(user.getEmail(), user.getPassword());
		
		return new ResponseEntity<ResponseObject>(obj,HttpStatusUtil.getStatus(obj.getStatusCode()));
	}

}
