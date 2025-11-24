package com.ssp.Controller;

import java.net.HttpURLConnection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssp.DTO.JobDTO;
import com.ssp.Response.ApiResponse;
import com.ssp.Service.IJobServiceManagement;
import com.ssp.utility.IResponseMessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/jobs")
@Tag(name = "JobManagement", description = "APIs for managing job")
public class JobManagementController {

	@Autowired
	private IJobServiceManagement service;

	@PostMapping("/addJobDetails")
	@Operation(summary = "Add a new Job", description = "Add Details About Job")
	public ResponseEntity<ApiResponse> addJobDetails(@Valid @RequestBody JobDTO jobDto) {

		String details = service.registerJobDetails(jobDto);

		return ResponseEntity.ok(new ApiResponse(HttpURLConnection.HTTP_OK, IResponseMessage.SUCCESS,
				"Job Details Added SuccessFully", details));
	}

	@GetMapping("/viewAllDetails")
	@Operation(summary = "View All Details", description = "It's Performing To View All The Details")
	public ResponseEntity<ApiResponse> getAllDetails() {

		List<JobDTO> searchAllDetails = service.searchAllDetails();

		return ResponseEntity.ok(new ApiResponse(HttpURLConnection.HTTP_OK, IResponseMessage.SUCCESS,
				"Show All Details", searchAllDetails));
	}

	@GetMapping("/viewById/{id}")
	@Operation(summary = "View By Id", description = "It's Performing To View By Id")
	public ResponseEntity<ApiResponse> showDetailsById(@PathVariable Long id) {

		String detailsById = service.searchDetailsById(id);

		return ResponseEntity.ok(new ApiResponse(HttpURLConnection.HTTP_OK, IResponseMessage.SUCCESS, detailsById));
	}

	@PutMapping("/updateDetails/{id}")
	@Operation(summary = "Update Details", description = "It's Performing Update The Details")
	public ResponseEntity<ApiResponse> updateDetails(@PathVariable Long id, @Valid @RequestBody JobDTO dto) {

		String updateJobs = service.updateJobs(id, dto);

		return ResponseEntity.ok(new ApiResponse(HttpURLConnection.HTTP_OK, IResponseMessage.SUCCESS, updateJobs));
	}

	@GetMapping("/viewByTitleAndLocation/{title}/{location}")
	@Operation(summary = "Get Jobs By Title And Location", description = "It's Performing View Jobs By Title And Location")
	public ResponseEntity<ApiResponse> getJobsByTitleAndLocation(@PathVariable String title,
			@PathVariable String location) {

		List<JobDTO> searchJobList = service.searchJobs(title, location);
		searchJobList.forEach(System.out::println);
		return ResponseEntity.ok(new ApiResponse(HttpURLConnection.HTTP_OK, IResponseMessage.SUCCESS,
				"View Jobs By Title And Location", searchJobList));
	}

	@GetMapping("/searchFilterJobs")
	public ResponseEntity<ApiResponse>searchJobs( @RequestParam(required = false) String keyword,
	        @RequestParam(required = false) String location,
	        @RequestParam(required = false) Integer experience){
		
		List<JobDTO> searchFilterAllJobs = service.searchFilterAllJobs(keyword, location, experience);
		searchFilterAllJobs.forEach(System.out::println);
	return ResponseEntity.ok(new ApiResponse(HttpURLConnection.HTTP_OK,IResponseMessage.SUCCESS,"Search results fetched successfully",searchFilterAllJobs));
	   
	}
	  
	@DeleteMapping("/deleteById/{id}")
	@Operation(summary = "Delete Details By Id ", description = "It's Performing Delete The Details By Id")
	public ResponseEntity<ApiResponse> vanishDetailsById(@PathVariable Long id) {

		String removeById = service.removeById(id);
//		RETURN
		return ResponseEntity                            //204-status code of HTTP_NO_CONTENT
				.ok(new ApiResponse(HttpURLConnection.HTTP_NO_CONTENT, "This Id Is Deleted From The DB", removeById));
	}

}