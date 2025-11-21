package com.ssp.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

//RecruiterUpdateDTORecruiterUpdateDTO → for updating profile info (no password/email validation)
@Data
public class RecruiterUpdateDTO {

	private Long rid;

	@NotBlank(message = "Recruiter Name Is Required")
	@Size(min = 2, max = 50, message = "Name Must Be Between 2 and 50 Characters")
	private String name;

	@NotBlank(message = "Company Name Is Required")
	private String companyName;

	@NotBlank(message = "Department Name Is Required")
	@Size(min = 2, max = 50, message = "Department Must Be Between 2 And 50 Characters")
	private String department;

	@NotBlank(message = "Designation Is Required")
	private String designation;

	@NotBlank(message = "Location Is Reequired")
	@Size(min = 2, max = 100, message = "Location must be between 2 and 100 characters")
	private String location;
}
