package com.pwm.springbootecommerce.exception;

import java.util.NoSuchElementException;

public class CategoryNotFoundException extends NoSuchElementException {

	private static final long serialVersionUID = 1L;
	
	public CategoryNotFoundException(String message) {
		super(message);
	}

}
