package com.example.demo.model;

import org.springframework.stereotype.Component;

@Component
public class ResponseModel {

	private int statusCode;
	private String message;

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ResponseModel(int statusCode, String message) {
		super();
		this.statusCode = statusCode;
		this.message = message;
	}

	public ResponseModel() {
		super();
	}
	
	

}
