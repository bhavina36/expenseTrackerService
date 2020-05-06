package com.project.expensetracker.data;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.project.expensetracker.entity.Customer;
import com.project.expensetracker.entity.Expense;

public interface CustomerRepository extends MongoRepository<Customer, String> {
	
	@Query("{'email' : ?0}")
	Customer findUserByEmailId(String emailId);
	
	@Query("{'token' : ?0}")
	Customer findByToken(String token);

}
