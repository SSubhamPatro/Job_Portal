package com.ssp.DTO;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
//Hybrid DTO
//show job title + applicant name along with the interview.

/**
 * DTO used for creating or updating interview schedules. Hybrid type: includes
 * applicationId (for linkage) + interview details.
 */
@Schema(description = "DTO used to schedule or update an interview for a specific application")
@Data
public class InterviewScheduleRequestDTO {

	@NotNull(message = "Interview date and time is required")
	@Future(message = "Interview date must be in the future")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@Schema(description = "Scheduled date and time for the interview", example = "2025-10-10T14:30:00")
	private LocalDateTime interviewDateTime;
	
	@NotBlank(message = "Meeting link is required")
	@Schema(description = "Online meeting link (e.g., Zoom/Google Meet URL)", example = "https://meet.google.com/xyz-abcd-pqr")
	private String meetingLink;
	
	@NotBlank(message = "Interviewer name is required")
	@Schema(description = "Name of the interviewer", example = "Mr. Rajesh Sharma")
	private String interviewerName;
	
	@NotNull(message = "Application ID is required")
	@Schema(description = "Unique ID of the related application", example = "101")
	// hybrid fields from Application
	private Long applicationId; // client provides this
    
	/*
     * The following can be included in response DTOs if needed:
     * private String jobTitle;      // from Application → Job
     * private String applicantName; // from Application → Applicant
     */

}
