package com.ssp.Controller;

import java.net.HttpURLConnection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssp.DTO.RecruiterDTO;
import com.ssp.DTO.RecruiterResponseDTO;
import com.ssp.DTO.RecruiterUpdateDTO;
import com.ssp.Response.ApiResponse;
import com.ssp.Service.IRecruiterServiceManagement;
import com.ssp.utility.IResponseMessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@RestController
@Tag(name = "Recruiter Api's", description = "Api's Are Performing Recruiter Management")
@RequestMapping("/Recruiter-Api")

public class RecruiterManagementController {

	@Autowired
	private IRecruiterServiceManagement service;

	@PostMapping("/addRecruiterDetails")
	@Operation(summary = "ADD ALL DETAILS", description = "Its Adding Recruiter Details")
	@PreAuthorize("permitAll()")// 
	public ResponseEntity<ApiResponse> addAllDetails(@Valid @RequestBody RecruiterDTO recruiterDto) {

		String details = service.registerRecruiterDetails(recruiterDto);
		System.out.println(details);
		return ResponseEntity.ok(new ApiResponse(HttpURLConnection.HTTP_CREATED, IResponseMessage.SUCCESS,
				"Recruiter Details Added Successfully", details));
	}

	@Operation(summary = "SHOW ALL DETAILS",description = "It's Showing All Recruiter Details")
	@GetMapping("/showAllDetails")
	@PreAuthorize("hasAnyRole('ADMIN','RECRUITER')") // Admin & Recruiter can view all
	public ResponseEntity<ApiResponse> findAllRecrutierDetails() {

		List<RecruiterResponseDTO> allRecruiterDetails = service.viewAllRecruiterDetails();
//		System.out.println(allRecruiterDetails);
		return ResponseEntity.ok(new ApiResponse(HttpURLConnection.HTTP_OK, IResponseMessage.SUCCESS,
				"All Recruiter Details", allRecruiterDetails));
	}
	

	@GetMapping("/show/{id}")
	@Operation(summary = "SHOW BY ID DETAILS",description = "It's Showing By The Id Details ")
	public ResponseEntity<ApiResponse> viewByIdRecrutierDetails(@PathVariable Long id) {

		 RecruiterResponseDTO byIdRecruiterDetails = service.viewByIdRecruiterDetails(id);

		return ResponseEntity.ok(new ApiResponse(HttpURLConnection.HTTP_OK, IResponseMessage.SUCCESS,byIdRecruiterDetails));
	}

	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasRole('ADMIN')") // Only admin can delete
	@Operation(summary = "Delete By Id Details",description = "It's Delete The Details By Id")
	public ResponseEntity<ApiResponse>vanishById(@PathVariable Long id){
		
		String deleteById = service.removeById(id);
	  return ResponseEntity.ok(new ApiResponse(HttpURLConnection.HTTP_NO_CONTENT, IResponseMessage.SUCCESS, deleteById));
	}
	
	@PutMapping("/updateDetails")
	@Operation(summary = "UPDATE DETAILS",description = "It'S Api Is Used TO Update Details")
	public ResponseEntity<ApiResponse> updateTheRecruiterDetails(@Valid @RequestBody RecruiterUpdateDTO recruiterDTO) {
		
		String updateDetails = service.updateDetails(recruiterDTO);
		
		return ResponseEntity.ok(new ApiResponse(HttpURLConnection.HTTP_OK, IResponseMessage.SUCCESS, updateDetails));
	}
}
