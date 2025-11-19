package com.ssp.Exception;

@SuppressWarnings("serial")
public class InvalidCredentialsException extends RuntimeException {

	public InvalidCredentialsException() {
     super();
	}
	
	public InvalidCredentialsException(String msg) {
	  super(msg);
	}
}

