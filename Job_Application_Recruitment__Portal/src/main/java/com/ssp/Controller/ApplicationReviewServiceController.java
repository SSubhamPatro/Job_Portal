package com.ssp.Controller;
import java.net.HttpURLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssp.Response.ApiResponse;
import com.ssp.Service.IApplicationReviewService;
import com.ssp.utility.IResponseMessage;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/applications")
@Tag(name = "Application Review Service", description = "Handles recruiter review of applications")
public class ApplicationReviewServiceController {

    @Autowired
    private IApplicationReviewService service;

    // Example: POST /api/applications/101/recruiter/501/decision/true
    @PostMapping("/{applicationId}/recruiter/{recruiterId}/decision/{shortListed}")
    public ResponseEntity<ApiResponse> reviewApplication(
            @PathVariable Long applicationId,
            @PathVariable Long recruiterId,
            @PathVariable boolean shortListed) {

        service.reviewApplication(applicationId, recruiterId, shortListed);

        return ResponseEntity.ok(
            new ApiResponse(
                HttpURLConnection.HTTP_OK,
                IResponseMessage.SUCCESS,
                shortListed ? "Application Shortlisted Successfully!" : "Application Rejected Successfully!"
            )
        );
    }
}
