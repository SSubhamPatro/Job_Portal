package com.ssp.Controller;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssp.DTO.ApplicantDTO;
import com.ssp.DTO.ApplicantRegisterDTO;
import com.ssp.DTO.ApplicantResponse;
import com.ssp.Response.ApiResponse;
import com.ssp.Service.IApplicantServiceManagement;
import com.ssp.utility.IResponseMessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/Applicant-Api")
@Tag(name = "Applicant Management ", description = "Perform Applicant Operation")
public class ApplicantManagementController {

	@Autowired
	private IApplicantServiceManagement service;

	/*
		@Operation(summary = "Register a new applicant with resume", description = "Uploads resume file to S3 and saves applicant details with the S3 URL")
		@PostMapping(value = "/registerDetails", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
		public ResponseEntity<ApiResponse> registerApplicationDetails(
				@Parameter(description = "Applicant details as JSON", required = true,
	        content = @Content(mediaType = "application/json", 
	        
	        schema = @Schema(implementation = ApplicantDTO.class)))
	@RequestPart("applicant") ApplicantDTO applicantdto,
	
	@Parameter(description = "Resume file to upload", required = true,
	        content = @Content(mediaType = "multipart/form-data", 
	        schema = @Schema(type = "string", format = "binary")))
	@RequestPart("resumefile") MultipartFile resumefile)
	
				throws Exception {
	
			System.out.println("ApplicantManagementController.registerApplicationDetails()");
	
			String createDetails = service.cretateApplicantDetails(applicantdto, resumefile);
	
			return ResponseEntity.ok(new ApiResponse(HttpURLConnection.HTTP_OK, IResponseMessage.SUCCESS, createDetails));
		}
	*/
	@Operation(summary = "Register a new applicant with resume", description = "Uploads resume file to S3 and saves applicant details with the S3 URL")
	@PostMapping(value = "/registerDetails", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//	@PreAuthorize("hasRole('APPLICANT') or hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> registerApplicationDetails(

			@Parameter(description = "Applicant details as JSON (stringified JSON)", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicantRegisterDTO.class))) @RequestPart("applicant") String applicantJson, // <--
																																																																			// changed
			
			@Parameter(description = "Resume file to upload", required = true, content = @Content(mediaType = "multipart/form-data", schema = @Schema(type = "string", format = "binary"))) @RequestPart("resumefile") MultipartFile resumefile)

			throws Exception {

		System.out.println("ApplicantManagementController.registerApplicationDetails()");

		// Convert JSON string to DTO
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		ApplicantRegisterDTO registerDTO = mapper.readValue(applicantJson, ApplicantRegisterDTO.class);

	    // ✅ Manual validation
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<ApplicantRegisterDTO>>violations=validator.validate(registerDTO);
		if (!violations.isEmpty()) {
			String errorMsg=violations.stream().map(v->v.getPropertyPath()+":"+v.getMessage()).reduce((a,b)->a+","+b).orElse("Validation Failed");
			throw new IllegalArgumentException(errorMsg);
		}
		
		ApplicantResponse response = service.cretateApplicantDetails(registerDTO, resumefile);

		return ResponseEntity.ok(new ApiResponse(HttpURLConnection.HTTP_OK, IResponseMessage.SUCCESS, response));
	}

	@Operation(summary = "Fetch All Details", description = "It's Showing All The Details")
	@PreAuthorize("hasAnyRole('RECRUITER','APPLICANT')") // Only admin can view all
	@GetMapping("/showAllDetails")
	public ResponseEntity<ApiResponse> fetchAllDetails() {
		System.out.println("ApplicantManagementController.fetchAllDetails()");

		List<ApplicantDTO> allDetails = service.getAllDetails();
		System.out.println(allDetails);
		return ResponseEntity.ok(new ApiResponse(HttpURLConnection.HTTP_OK, IResponseMessage.SUCCESS,
				"All Details Are Found", allDetails));

	}

	@Operation(summary = "View By Id", description = "It's Performing View By Id")
	@GetMapping("/viewById/{id}")
	@PreAuthorize("hasAnyRole('RECRUITER','APPLICANT')")//only Recruiter and applicant only access this
	public ResponseEntity<ApiResponse> showById(@PathVariable Long id) {
		System.out.println(id);

		System.out.println("ApplicantManagementController.showById()");
		ApplicantDTO detailsById = service.getDetailsById(id);

		return ResponseEntity.ok(new ApiResponse(HttpURLConnection.HTTP_OK, IResponseMessage.SUCCESS, detailsById));
	}

	@Operation(summary = "Delete Application By Id", description = "It's Performing Delete Operation")
	 @PreAuthorize("hasRole('ADMIN')") // Only admin can delete applicant
	@DeleteMapping("/deleteById/{id}")
	public ResponseEntity<ApiResponse> vanishById(@PathVariable Long id) {

		System.out.println("ApplicantManagementController.vanishById()");

		String deleteApplicantDetailsById = service.deleteApplicantDetailsById(id);

		return ResponseEntity
				.ok(new ApiResponse(HttpURLConnection.HTTP_NO_CONTENT, IResponseMessage.SUCCESS, deleteApplicantDetailsById));

	}

//	@Operation(summary = "Authenticate an applicant login", 
//			description = "Verifies applicant email and password. "
//					+ "Returns applicant details if authentication succeeds.")
//
//	@PostMapping("/login")
//	public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequestDTO logindto) {
//
//		ApplicantDTO login = service.loginApplicant(logindto.getEmail(), logindto.getPassword());
//		return ResponseEntity
//				.ok(new ApiResponse(HttpURLConnection.HTTP_OK, IResponseMessage.SUCCESS, "Login Successfull", login));
//	}
}
