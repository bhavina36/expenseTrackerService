package com.project.expensetracker.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "expenses")
public class Expense {
	
	@Id
	private String id;
	private String email;
	private double amount;
	private String transactionDate;
	private Merchant merchant;
	private CategoryLabel categoryLabel;
	private String category;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	public Merchant getMerchant() {
		return merchant;
	}
	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public CategoryLabel getCategoryLabel() {
		return categoryLabel;
	}
	public void setCategoryLabel(CategoryLabel categoryLabel) {
		this.categoryLabel = categoryLabel;
	}
	
	
	

}
