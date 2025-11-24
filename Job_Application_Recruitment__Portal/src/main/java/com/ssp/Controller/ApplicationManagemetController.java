package com.ssp.Controller;

import java.net.HttpURLConnection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssp.DTO.ApplicationDTO;
import com.ssp.Response.ApiResponse;
import com.ssp.Service.IApplicationService;
import com.ssp.utility.IResponseMessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/Application-api")
@Tag(name = "Application Management", description = "Its Controlling Application Management")
public class ApplicationManagemetController {

	@Autowired
	private IApplicationService service;

	@Operation(summary = "Register The Details", description = "Its Performing To Save The Details In Db")
	@PostMapping("/registerDetails")
	public ResponseEntity<ApiResponse> registerDetails(@Valid @RequestBody ApplicationDTO dto) {

		String applicationsave = service.createApplication(dto);
		return ResponseEntity.ok(new ApiResponse(HttpURLConnection.HTTP_OK, IResponseMessage.SUCCESS, applicationsave));
	}

	@Operation(summary = "Show All The Details", description = "It's Performing To View  The Details")
	@GetMapping("/findAllDetails")
	public ResponseEntity<ApiResponse> viewAllDetails() {

		List<ApplicationDTO> allApplication = service.getAllApplication();

		return ResponseEntity.ok(new ApiResponse(HttpURLConnection.HTTP_OK, IResponseMessage.SUCCESS,
				"Find All The Details", allApplication));
	}

	@Operation(summary = "Show Details By The Id", description = "It's Performing To View The Details By Id")
	@GetMapping("/getDetailsById/{id}")
	public ResponseEntity<ApiResponse> viewDetailsByid(Long id) {
		ApplicationDTO applicationById = service.getApplicationById(id);

		return ResponseEntity.ok(new ApiResponse(HttpURLConnection.HTTP_OK, IResponseMessage.SUCCESS,
				"Show Details By Id", applicationById));
	}

	@Operation(summary = "Modify The Details" ,description = "It's Performing Modify The Details")
	@PutMapping("/updateDetails")
	public ResponseEntity<ApiResponse> modifyDetails(@Valid @RequestBody ApplicationDTO dto) {

		String updateApplication = service.updateApplication(dto);
		return ResponseEntity
				.ok(new ApiResponse(HttpURLConnection.HTTP_OK, IResponseMessage.SUCCESS, updateApplication));
	}
	

	@Operation(summary = "Delete The Details By Id",description = "It's Performing Delete The Detalis By id")
	@DeleteMapping("/deleteById/{id}")
	public ResponseEntity<ApiResponse> vanishDetalisById(@PathVariable Long id) {

		String deleteApplicationById = service.deleteApplicationById(id);

		return ResponseEntity
				.ok(new ApiResponse(HttpURLConnection.HTTP_NO_CONTENT, IResponseMessage.SUCCESS, deleteApplicationById));

	}
}
