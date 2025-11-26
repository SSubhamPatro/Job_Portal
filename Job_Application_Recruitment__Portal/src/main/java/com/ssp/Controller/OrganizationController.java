package com.ssp.Controller;

import java.net.HttpURLConnection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssp.DTO.OrganizationDto;
import com.ssp.Response.ApiResponse;
import com.ssp.Service.IOrganizationService;
import com.ssp.utility.IResponseMessage;


@RestController
@RequestMapping("/organizations")
public class OrganizationController {

	@Autowired
	private IOrganizationService service;
	
	@GetMapping("/search")
	public ResponseEntity<ApiResponse>search(@RequestParam String name){
		
		List<String> searchOrganization = service.searchOrganization(name);
		
		return ResponseEntity.ok(new ApiResponse(HttpURLConnection.HTTP_OK, IResponseMessage.SUCCESS, "All lists", searchOrganization));
		
	}
	
	@PostMapping("/add")
	public ResponseEntity<ApiResponse> add(@RequestParam String name) {
		
		OrganizationDto dto = service.addOrganization(name);
		
		return ResponseEntity.ok(new ApiResponse(HttpURLConnection.HTTP_OK, IResponseMessage.SUCCESS, "Organization added", dto));
	}
	
}
