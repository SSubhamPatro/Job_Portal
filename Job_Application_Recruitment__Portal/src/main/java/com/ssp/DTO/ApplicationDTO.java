	package com.ssp.DTO;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssp.Entity.Status;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//Hybrid DTO (IDs + Selected fields)
//send only IDs for relations and selected summary fields.
public class ApplicationDTO {

	private Long id;

	@NotNull(message = "Applied Date Is Required")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(description = "Date And Time When The Aplication Was Submitted", example = "2025-10-05 14:30:00")
	private LocalDateTime appliedDate;

	@NotNull(message = "Status Cannot Be Null")
	@Schema(description = "Current Status Of The Application ", example = "PENDING")
	private Status status;

	// ---------From Job-----------
	@NotNull(message = "Job Id Is Required")
	@Schema(description = "Unique Identifier Of The Job To Which The Applicant Applied")
	private Long jobID;

//	@NotBlank(message = "Job ")
	@Schema(description = "Title of the job (read-only summary field)", example = "Java Backend Developer", accessMode = Schema.AccessMode.READ_ONLY)
	private String jobTitle;

	// From Applicant
	@NotNull(message = "Applicant ID is required")
	@Schema(description = "Unique identifier of the applicant")
	private Long applicantID;

	@Schema(description = "Applicant's name (read-only summary field)", example = "Subham Patra", accessMode = Schema.AccessMode.READ_ONLY)
	private String applicantName;

	// From InterviewSchedule
//     private Long interviewId;
//     private LocalDateTime interviewDate;

}
//What's The Benifit to use Hybrid Dto	
//✅ No recursion

//✅ More informative than just IDs

//✅ Good balance between lightweight and rich