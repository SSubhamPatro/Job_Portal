package com.ssp.DTO;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Response DTO returned after scheduling or fetching interview details.
 * Combines interview info + hybrid data (job title & applicant name).
 */
@Data
@Schema(description = "Response DTO containing interview details along with job and applicant summary info")
public class InterviewScheduleResponseDto {

    @Schema(description = "Unique ID of the interview record", example = "501")
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Scheduled date and time for the interview", example = "2025-10-10T15:00:00")
    private LocalDateTime interviewDateTime;

    @Schema(description = "Online meeting link for the interview", example = "https://meet.google.com/xyz-abcd-pqr")
    private String meetingLink;

    @Schema(description = "Name of the interviewer", example = "Mr. Rajesh Sharma")
    private String interviewerName;

    // ---- Hybrid fields from Application ----
    @Schema(description = "ID of the associated application", example = "101")
    private Long applicationId;

    @Schema(description = "Title of the job for which the interview is scheduled", example = "Java Backend Developer")
    private String jobTitle;

    @Schema(description = "Name of the applicant being interviewed", example = "Subham Patra")
    private String applicantName;
}
