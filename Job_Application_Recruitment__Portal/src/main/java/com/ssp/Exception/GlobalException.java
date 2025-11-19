package com.ssp.Exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ssp.Response.ApiResponse;
import com.ssp.utility.IResponseMessage;

import io.swagger.v3.oas.annotations.Hidden;

@RestControllerAdvice
@Hidden
public class GlobalException {

	@ExceptionHandler(JobIdNotFoundException.class)
	public ResponseEntity<ApiResponse> jobIdNotFound(JobIdNotFoundException ex) {

		System.out.println("GlobalException.jobIdNotFound()");
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ApiResponse(HttpStatus.NOT_FOUND.value(), IResponseMessage.FAILED, ex.getMessage()));
	}

	@ExceptionHandler(RecruiterIdNotFoundException.class)
	public ResponseEntity<ApiResponse> recruiterIdNotfound(RecruiterIdNotFoundException rinf) {

		System.out.println("GlobalException.recruiterIdNotfound()");
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ApiResponse(HttpStatus.NOT_FOUND.value(), IResponseMessage.FAILED, rinf.getMessage()));
	}

	@ExceptionHandler(ApplicaionNotFoundException.class)
	public ResponseEntity<ApiResponse> applicationNotFound(ApplicaionNotFoundException anfe) {

		System.out.println("GlobalException.applicationNotFound()");
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ApiResponse(HttpStatus.NOT_FOUND.value(), IResponseMessage.FAILED, anfe.getMessage()));
	}

	@ExceptionHandler(InterviewIdNotFoundException.class)
	public ResponseEntity<ApiResponse> interviewNotFound(InterviewIdNotFoundException iin) {

		System.out.println("GlobalException.interviewNotFound()");

		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ApiResponse(HttpStatus.NOT_FOUND.value(), IResponseMessage.FAILED, iin.getMessage()));
	}

	@ExceptionHandler(ApplicantIdNotFoundException.class)
	public ResponseEntity<ApiResponse> applicantIdNotFound(ApplicantIdNotFoundException ainf) {

		System.out.println("GlobalException.applicantIdNotFound()");
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ApiResponse(HttpStatus.NOT_FOUND.value(), IResponseMessage.FAILED, ainf.getMessage()));
	}

	@ExceptionHandler(ResumeUploadFailedException.class)
    public ResponseEntity<ApiResponse> resumeNotFound(ResumeUploadFailedException rue) {

		System.out.println("GlobalException.resumeNotFound()");
		rue.printStackTrace();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
				body(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),IResponseMessage.FAILED,rue.getMessage()));
	}

	@ExceptionHandler(DuplicateEmailException.class)
	public ResponseEntity<ApiResponse>duplicateEmail(DuplicateEmailException dee){
		System.out.println("GlobalException.duplicateEmail()");
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),IResponseMessage.FAILED,dee.getMessage()));
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse>handleValidationErrors(MethodArgumentNotValidException ex){
		
		String errorMsg=ex.getBindingResult().getFieldErrors()
				 .stream()
				 .map(err->err.getField()+" : "+err.getDefaultMessage())
				 .collect(Collectors.joining(","));
		return ResponseEntity.badRequest().body(new ApiResponse(HttpStatus.BAD_REQUEST.value(),"VALIDATION_FAILED",errorMsg));
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ApiResponse>handleUserNotFound(UserNotFoundException e){
		
		System.out.println("GlobalException.handleUserNotFound()");
	
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
			   .body(new ApiResponse(HttpStatus.NOT_FOUND.value(),IResponseMessage.FAILED,e.getMessage()));
		
	}
	
	@ExceptionHandler(OtpNotFoundException.class)
	public ResponseEntity<ApiResponse>handleOtpNotFound(OtpNotFoundException e){
		
		System.out.println("GlobalException.handleOtpNotFound()");
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ApiResponse(HttpStatus.NOT_FOUND.value(),IResponseMessage.OTP_NOT_FOUND,"OTP not found for this email",e));
	}
	
	@ExceptionHandler(OtpExpiredException.class)
	public ResponseEntity<ApiResponse>handleOtpExipry(OtpExpiredException e){
		
		System.out.println("GlobalException.handleOtpExipry()");
		return ResponseEntity.status(HttpStatus.GONE)//410 error code
			   .body(new ApiResponse(HttpStatus.GONE.value(),IResponseMessage.OTP_EXPIRED,"OTP has expired",e));	
	}
	
	@ExceptionHandler(InvalidOtpException.class)
	public ResponseEntity<ApiResponse>handleInvalidOtp(InvalidOtpException e){
		
		System.out.println("GlobalException.handleInvalidOtp()");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)//400
			   .body(new ApiResponse(HttpStatus.BAD_REQUEST.value(),IResponseMessage.INVALID_OTP,"OTP is invalid",e));
	}
	
	@ExceptionHandler(PasswordMismatchException.class)
	public ResponseEntity<ApiResponse>handlePasswordMismatch(PasswordMismatchException e){
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)//400
			   .body(new ApiResponse(HttpStatus.BAD_REQUEST.value(),IResponseMessage.PASSWORD_MISMATCH,"Password and Confirm Password Do Not Match",e));	
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse> handleAllExceptions(Exception ex) {

		System.out.println("GlobalException.handleAllExceptions()");
		// Convert stack trace to String
		StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw); 
		 
		ex.printStackTrace(pw);
		String fullStackTrace = sw.toString();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
				new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), IResponseMessage.FAILED, fullStackTrace));
	}

}
