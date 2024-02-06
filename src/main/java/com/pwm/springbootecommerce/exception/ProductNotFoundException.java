package com.pwm.springbootecommerce.exception;

import java.util.NoSuchElementException;

public class ProductNotFoundException extends NoSuchElementException {
	private static final long serialVersionUID = 1L;

	public ProductNotFoundException(String message) {
		super(message);
	}
}
