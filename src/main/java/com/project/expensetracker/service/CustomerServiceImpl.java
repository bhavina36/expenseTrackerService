package com.project.expensetracker.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.expensetracker.data.CustomerRepository;
import com.project.expensetracker.entity.Customer;
import com.project.expensetracker.errorhandler.ResponseObject;

@Service
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private CustomerRepository userRepo;	
	
	@Override
	public ResponseObject addUser(Customer user) {
		
		ResponseObject responseObj = new ResponseObject();
				
		String email = user.getEmail();	
		
		Random random = new Random();
		String id = "user" + random.nextInt();			
		
		responseObj.setMessage("User ID/email already exists");
		responseObj.setStatusCode(HttpStatus.BAD_REQUEST.value());
		
		Customer getUserFromEmail = userRepo.findUserByEmailId(email);
		
		if(getUserFromEmail == null) {
			
			Optional<Customer> getUserFromId = userRepo.findById(id);
			 
			if(getUserFromId.isEmpty()) {										
				
				System.err.println(encoder.encode(user.getPassword()));
				
				user.setPassword(encoder.encode(user.getPassword()));
				user.setId(id);
				
//				String hashedPassword = encoder.encode(user.getPassword());
//
//		        Collection<? extends GrantedAuthority> roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
//
//		        UserDetails userDetails = new User(user.getUsername(), hashedPassword, roles);
//		        userDetailsManager.createUser(userDetails);
		        
				 userRepo.save(user);				
				 
				responseObj.setMessage("User added successfully");
				responseObj.setStatusCode(HttpStatus.OK.value());
				responseObj.setResponse(null);
			}
			
		}
				
		return responseObj;
	}
	
	@Override
	public String findEmailId(String userId) {
		
		Optional<Customer> user = userRepo.findById(userId);
		
		if(!user.isEmpty()) {
			return user.get().getEmail();
		}
		
		return null;
	}
	
	public ResponseObject login(String email, String password) {
		
		ResponseObject responseObj = new ResponseObject();
		
		//System.err.println(email + "USERServiceImpl" + password);
		
		/*
		 * boolean loginResponse = securityService.login(email, password);
		 * //userRepo.findByEmail(email);
		 * 
		 * System.err.println("final login Response:loginResponse..."+loginResponse);
		 * 
		 * if(loginResponse) {
		 * 
		 * responseObj.setMessage("Successfully login!");
		 * responseObj.setStatusCode(HttpStatus.OK.value());
		 * 
		 * return responseObj; }
		 */
					

		Customer checkUserEmail = userRepo.findUserByEmailId(email);

		
		  if (checkUserEmail != null) {
		  
			  if (checkUserEmail.getEmail().equals(email) &&
				 encoder.matches(password, checkUserEmail.getPassword())) {			  
				  
			  String token = UUID.randomUUID().toString();
			  
			  checkUserEmail.setToken(token);
			  userRepo.save(checkUserEmail);
			  
			  responseObj.setResponse(token);  
			  responseObj.setMessage("Successfully login!");
			  responseObj.setStatusCode(HttpStatus.OK.value());
			  
			  return responseObj;
			  
			  } 
		  }
		
	    responseObj.setResponse(null);  
		responseObj.setMessage("Invalid Username and password, Please try again!");
		responseObj.setStatusCode(HttpStatus.FORBIDDEN.value());

		return responseObj;

	}

	@Override
	public Optional findByToken(String token) {
		
		ResponseObject responseObj = new ResponseObject();
		
		 token= StringUtils.removeStart(token, "Bearer").trim();
		Customer getCustomerByToken = userRepo.findByToken(token);
		
		if(getCustomerByToken != null) {
			
			 User user= new User(getCustomerByToken.getEmail(), getCustomerByToken.getPassword(), true, true, true, true,
	                    AuthorityUtils.createAuthorityList("USER"));
	            return Optional.of(user);
		}
		
		return Optional.empty();
	}

	@Override
	public ResponseObject emailExistForgetPassword(String email) {
		
		ResponseObject responseObj = new ResponseObject();
		
		if(email != null) {		
			Customer checkUserEmail = userRepo.findUserByEmailId(email);
		
		  if (checkUserEmail != null) {
		  
			  if (checkUserEmail.getEmail().equals(email)) {	
				  
				  Map<String,String> map = new HashMap();
				  map.put("email", checkUserEmail.getEmail());
				  map.put("securityQuestion", checkUserEmail.getSecurityQuestion());
				  map.put("securityAnswer", checkUserEmail.getSecurityAnswer());				  
				  
			  responseObj.setResponse(map);  
			  responseObj.setMessage("user exist!");
			  responseObj.setStatusCode(HttpStatus.OK.value());
			  
			  return responseObj;
			  
			  } 
		  }
		}
				
		 responseObj.setResponse(null);  
		 responseObj.setMessage("Email is not exist!");
		 responseObj.setStatusCode(HttpStatus.FORBIDDEN.value());

		return responseObj;
	}	
	
	@Override
	public ResponseObject changePassword(String email, String  newPassword) {
		
		ResponseObject responseObj = new ResponseObject();
		
		if(email != null) {		
			Customer user = userRepo.findUserByEmailId(email);
		
		  if (user != null) {
		  
			  if (user.getEmail().equals(email)) {	
				  
				  user.setPassword(encoder.encode(newPassword));			 
			      userRepo.save(user);	
			      
			       
			      responseObj.setMessage("Successfully password updated!");
				  responseObj.setStatusCode(HttpStatus.OK.value());
				  
				  return responseObj;
			  }
			  
		  }
		}
		
		responseObj.setMessage("Something went wrong!");
		responseObj.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		return responseObj;
	}
			  

}
