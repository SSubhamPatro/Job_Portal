package com.ssp.Exception;

@SuppressWarnings("serial")
public class InvalidTokenException extends RuntimeException {

	public InvalidTokenException() {
       super();
	}
	
	public InvalidTokenException(String msg) {
          super(msg);
	}
}
