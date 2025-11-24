package com.ssp.Controller;

import java.net.HttpURLConnection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssp.DTO.InterviewScheduleRequestDTO;
import com.ssp.DTO.InterviewScheduleResponseDto;
import com.ssp.Response.ApiResponse;
import com.ssp.Service.IInterviewScheduleService;
import com.ssp.utility.IResponseMessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/Interview-api")
@Tag(name = "Interview Management",description = "It's Handling Interview Management Api's")
public class InterviewManagementController {

	@Autowired
	private IInterviewScheduleService service;

	@Operation(summary = "Create Interview Api's",description = "It's Creaing The Interviews")
	@PostMapping("/registerInterview")
	public ResponseEntity<ApiResponse> createInterview(@Valid @RequestBody InterviewScheduleRequestDTO dto) {
		System.out.println("InterviewManagementController.createInterview()");
		InterviewScheduleResponseDto interview = service.createInterview(dto);
		return ResponseEntity.ok(new ApiResponse(HttpURLConnection.HTTP_OK, IResponseMessage.SUCCESS,
				"Interview Created Sussessfully", interview));
	}
      
	@Operation(summary = "View Details By id",description = "It's Performing To View By Id ")
	@GetMapping("/findInterviewById/{id}")
	public ResponseEntity<ApiResponse> searchInterviewById(@PathVariable Long id) {

		InterviewScheduleResponseDto interviewById = service.getInterviewById(id);

		return ResponseEntity.ok(new ApiResponse(HttpURLConnection.HTTP_OK, IResponseMessage.SUCCESS,
				"Id Found Sucessfully", interviewById));
	}

	
	@GetMapping("/findAllInterviews")
	@Operation(summary = "View All Details",description = "It's Used To Show The All Details")
	public ResponseEntity<ApiResponse> showAllInterviews() {
		
		System.out.println("InterviewManagementController.getAllInterviews()");

		List<InterviewScheduleResponseDto> allInterviews = service.getAllInterviews();

		return ResponseEntity.ok(new ApiResponse(HttpURLConnection.HTTP_OK, IResponseMessage.SUCCESS,
				"All The Interview Details Found Sucessfully", allInterviews));
	}
	
	@DeleteMapping("/deleteById/{id}")
	@Operation(summary = "Delete The Details By Id",description = "It's Performing Delete Operation")
	public ResponseEntity<ApiResponse>vanishById(@PathVariable Long id){
		
		System.out.println("InterviewManagementController.vanishById()");
		String deleteInterviewById = service.deleteInterviewById(id);
		
		return ResponseEntity.ok(new ApiResponse(HttpURLConnection.HTTP_NO_CONTENT, IResponseMessage.SUCCESS, deleteInterviewById));
	}

	@PutMapping("/updateDetails/{id}")
	@Operation(summary = "Update The Details",description = "It's Performing Update Operation")
	public ResponseEntity<ApiResponse> updateDetails(@PathVariable Long id, @RequestBody InterviewScheduleRequestDTO dto) {
        System.out.println("InterviewManagementController.updateDetails()");
        
		InterviewScheduleResponseDto updateInterviewDetails = service.updateInterviewDetails(id, dto);
		return ResponseEntity.ok(new ApiResponse(HttpURLConnection.HTTP_OK, IResponseMessage.SUCCESS, "Interview Details Updated Successfully", updateInterviewDetails));
	}
}
