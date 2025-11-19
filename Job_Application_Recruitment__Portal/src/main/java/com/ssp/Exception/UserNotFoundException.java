package com.ssp.Exception;

@SuppressWarnings("serial")
public class UserNotFoundException extends RuntimeException {

	public UserNotFoundException() {
	}
	
	public UserNotFoundException(String msg) {
		super(msg);
	}
}
