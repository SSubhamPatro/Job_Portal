package com.ssp.Exception;

@SuppressWarnings("serial")
public class PasswordMismatchException extends RuntimeException {
   
	public PasswordMismatchException() {
	}
	
	public PasswordMismatchException(String msg) {
	   
		super(msg);
	}
	
	
}
