package com.ssp.Exception;

@SuppressWarnings("serial")
public class DuplicateEmailException extends RuntimeException {

	public DuplicateEmailException() {
		super();
	}

	public DuplicateEmailException(String msg) {
		super(msg);
	}

}
