package com.project.expensetracker.data;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import com.project.expensetracker.entity.Expense;


public interface ExpenseRepository extends MongoRepository<Expense, String> {
		
	@Query("{'email' : ?0}")
	List<Expense> findExpenseByEmailId(String emailId);
	
	@Query("{'email' : ?0,'category' : ?1}")
	List<Expense> findExpenseByCategory(String emailId, String category);
	
	//

}
