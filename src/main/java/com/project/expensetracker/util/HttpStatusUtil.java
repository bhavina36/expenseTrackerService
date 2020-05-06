package com.project.expensetracker.util;

import org.springframework.http.HttpStatus;

public class HttpStatusUtil {
	
	public static HttpStatus getStatus(int statusCode) {
		
		HttpStatus httpStatus = null;
		
		switch (statusCode) {
		case 200:
			httpStatus = HttpStatus.OK;
			break;
		case 204:
			httpStatus = HttpStatus.NO_CONTENT;
			break;
		case 400:
			httpStatus = HttpStatus.BAD_REQUEST;
			break;
		case 401:
			httpStatus = HttpStatus.UNAUTHORIZED;
			break;
		case 404:
			httpStatus = HttpStatus.NOT_FOUND;
			break;
		case 500:
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			break;
		
		default:
			httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
			break;
		}
		
		return httpStatus;
	}

}
