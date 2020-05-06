package com.project.expensetracker.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.tomcat.util.http.fileupload.ParameterParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.project.expensetracker.data.ExpenseRepository;
import com.project.expensetracker.entity.CategoryLabel;
import com.project.expensetracker.entity.Expense;
import com.project.expensetracker.errorhandler.ResponseObject;
import com.project.expensetracker.util.CategoryUtil;

@Service
public class ExpenseServiceImpl implements ExpenseService {

	@Autowired
	private ExpenseRepository expenseRepo;

	@Autowired
	CustomerService userService;

	public ResponseObject addExpense(Expense expense) {

		ResponseObject obj = new ResponseObject();

		if (validateRequest(expense)) {
			Random random = new Random();

			String id = "exp" + random.nextInt();

			expense.setCategoryLabel(CategoryUtil.mapCategoryLabel(expense.getCategory()));
			expense.setId(id);
			
			String dateFormat = "yyyy-MM-dd";
			SimpleDateFormat format = new SimpleDateFormat(dateFormat,Locale.US);
			
			try {
				
				Date date = format.parse(expense.getTransactionDate());
				expenseRepo.save(expense);
				
				obj.setMessage("successfully add expense of user id: "+expense.getEmail());
				obj.setStatusCode(HttpStatus.OK.value());
				
			} catch (ParseException e) {
				
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				obj.setMessage("Date is not in "+dateFormat +" format");
				obj.setStatusCode(HttpStatus.BAD_REQUEST.value());
			}
	
			return obj;
		}
		obj.setMessage("Expense data is not valid");
		obj.setStatusCode(HttpStatus.BAD_REQUEST.value());

		return obj;

	}

	private boolean validateRequest(Expense expense) {
		boolean isValidRequest = false;
		if (expense != null) {

			if (expense.getAmount() > 0.0 && expense.getEmail() != null && expense.getTransactionDate() != null
					&& expense.getCategory() != null) {

				isValidRequest = true;
			}

		}

		return isValidRequest;
	}

	public ResponseObject updateExpense(String id, Expense requestExpense) {

		ResponseObject obj = new ResponseObject();

		if (id != null) {
			Optional<Expense> updateExpense = findExpense(id);

			if (validateRequest(requestExpense) && 
					!updateExpense.isEmpty()) {
				
				requestExpense.setCategoryLabel(requestExpense.getCategoryLabel());
				expenseRepo.save(requestExpense);
				
				obj.setMessage("Successfully updated expense of email id: "+ requestExpense.getEmail());
				obj.setStatusCode(HttpStatus.OK.value());

				return obj;
			}
		}

		obj.setMessage("Something went Wrong!");
		obj.setStatusCode(HttpStatus.BAD_REQUEST.value());

		return obj;
	}
	
	@Override
	public ResponseObject getUpdateExpense(String id) {
		
		ResponseObject obj = new ResponseObject();

		if (id != null) {
			Optional<Expense> getUpdateExpense = findExpense(id);
			
			obj.setMessage("Fetch Expense of id:" + id);
			obj.setResponse(getUpdateExpense);
			obj.setStatusCode(HttpStatus.OK.value());
			
			return obj;
		}
		
		obj.setMessage("No Data Found");
		obj.setStatusCode(HttpStatus.NO_CONTENT.value());
		
		return obj;
	}

	public Optional<Expense> findExpense(String id) {

		Optional<Expense> expense = expenseRepo.findById(id);
		
		return expense;
	}

	public ResponseObject deleteExpense(String id) {
		
		ResponseObject obj = new ResponseObject();

		if (id != null && 
				!findExpense(id).isEmpty()) {
			
			expenseRepo.deleteById(id);
			
			obj.setMessage("Successfully delete expense");
			obj.setStatusCode(HttpStatus.OK.value());
			
			return obj;
		}
		obj.setMessage("Something went Wrong!");
		obj.setStatusCode(HttpStatus.BAD_REQUEST.value());
		
		return obj;
	}

	public ResponseObject listAllExpense(String userId) {
		
		ResponseObject obj = new ResponseObject();
		
		if (userId != null) {

			String emailId = userService.findEmailId(userId);

			if (emailId != null) {

				List<Expense> listExpense = expenseRepo.findExpenseByEmailId(emailId);

				if (!listExpense.isEmpty()) {
					
					obj.setMessage("Fetch List of Expense");
					obj.setResponse(listExpense);
					obj.setStatusCode(HttpStatus.OK.value());
					
					return obj;
				}
			}
		}
		
		obj.setMessage("No Data Found");
		obj.setStatusCode(HttpStatus.NO_CONTENT.value());

		return obj;

	}

	@Override
	public ResponseObject listExpenseBasedOnCategory(String userId, String categoryName) {
		
		ResponseObject obj = new ResponseObject();

		if (categoryName != null && userId != null) {

			String emailId = userService.findEmailId(userId);

			if (emailId != null) {

				List<Expense> listExpense = expenseRepo.findExpenseByCategory(emailId, categoryName);

				if (!listExpense.isEmpty()) {
					
					obj.setMessage("Fetch List of Expense");
					obj.setResponse(listExpense);
					obj.setStatusCode(HttpStatus.OK.value());
					
					return obj;
				}
			}
		}

		obj.setMessage("No Data Found");
		obj.setStatusCode(HttpStatus.NO_CONTENT.value());

		return obj;
	}

	@Override
	public ResponseObject findPercentageOfCategory(String userId) {
		
		ResponseObject obj = new ResponseObject();

		// by default initialized map with zero value
		Map<String, Integer> categoryPercentageTracker = new LinkedHashMap<String, Integer>();
		categoryPercentageTracker.put(CategoryLabel.DINING.toString(), 0);
		categoryPercentageTracker.put(CategoryLabel.ENTERTAINMENT.toString(), 0);
		categoryPercentageTracker.put(CategoryLabel.GROCERIES.toString(), 0);
		categoryPercentageTracker.put(CategoryLabel.HOME_UTILITIES.toString(), 0);
		categoryPercentageTracker.put(CategoryLabel.MISC.toString(), 0);
		categoryPercentageTracker.put(CategoryLabel.SHOPPING.toString(), 0);
		categoryPercentageTracker.put(CategoryLabel.TRAVEL.toString(), 0);

		if (userId != null) {

			List<Expense> listExpense = (List<Expense>) listAllExpense(userId).getResponse();

			if(listExpense != null) {
			int listSize = listExpense.size();

			if (listSize > 0) {

				// find number of each category and add to map
				for (int i = 0; i < listExpense.size(); i++) {

					String category = listExpense.get(i).getCategory();

					if (categoryPercentageTracker.containsKey(category)) {

						int count = categoryPercentageTracker.get(category);
						categoryPercentageTracker.put(category, ++count);
					}
				}

				// calculate percentage of category
				Iterator<Map.Entry<String, Integer>> categoryPercentageIterator = categoryPercentageTracker.entrySet()
						.iterator();

				while (categoryPercentageIterator.hasNext()) {
					Map.Entry<String, Integer> entry = categoryPercentageIterator.next();

					int percentage = (int) Math.ceil((entry.getValue() * 100.0) / listSize);
					entry.setValue(percentage);
				}
					
				//SET IN ASENDING ORDER BASED ON MAP VALUE
				
				 List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(categoryPercentageTracker.entrySet());
				 Collections.sort( list, new Comparator<Map.Entry<String, Integer>>()
			        {
			            public int compare( Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2)
			            {
			                return (o2.getValue()).compareTo( o1.getValue());
			            }
			        });
				 
				 categoryPercentageTracker.clear();				 
				
				    for (Entry<String, Integer> data : list) {
				    	categoryPercentageTracker.put(data.getKey(), data.getValue());
				    }
				//-----------------
				 
				
				obj.setMessage("Category percetage");
				obj.setResponse(categoryPercentageTracker);
				obj.setStatusCode(HttpStatus.OK.value());
				
				return obj;

			}
			}

		}
		obj.setMessage("No Data Found");
		obj.setStatusCode(HttpStatus.NO_CONTENT.value());

		return obj;		
		
	}

	

}
