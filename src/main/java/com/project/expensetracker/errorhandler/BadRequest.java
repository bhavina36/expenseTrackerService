package com.project.expensetracker.errorhandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BadRequest extends RuntimeException{

	private static final long serialVersionUID = -1297303758696813897L;
	
	public BadRequest() {
		super();
	}
	
	public BadRequest(String message) {
		super(message);
	}
}
