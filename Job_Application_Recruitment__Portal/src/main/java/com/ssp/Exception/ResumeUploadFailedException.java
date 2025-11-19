package com.ssp.Exception;

@SuppressWarnings("serial")
public class ResumeUploadFailedException extends RuntimeException {

	public ResumeUploadFailedException() {
		super();
	}

	public ResumeUploadFailedException(String msg) {
		super(msg);
	}

	public ResumeUploadFailedException(String msg, Throwable cause) {
		super(msg, cause);

	}
}
