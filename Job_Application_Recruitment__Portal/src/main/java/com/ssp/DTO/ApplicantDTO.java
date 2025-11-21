package com.ssp.DTO;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssp.utility.ValidPhoneNumber;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ApplicantDTO {

	private Long id;

	@NotBlank(message = "Name Is Required")
	@Size(min = 2, max = 50, message = "Name Must Be 2-50 Character Long")
	private String name;

	@NotBlank(message = "Email Is Required")
	@Email(message = "Invalid Email Format")
	private String email;


	@ValidPhoneNumber
	private String phoneNumber;

	@Schema(description = "Resume file URL, automatically generated after upload", accessMode = Schema.AccessMode.READ_ONLY)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String resumeLink;

	@NotBlank(message = "Skills Cannot Be Empty")
    @Size(min = 2,max = 100,message = "Skills Must Be 2-100 Character Long")
	private String skills;

	@NotNull(message = "Experience Is Required")
	@Min(value = 0,message = "Experience Must Be 0 Or More Years")
	private Integer experienceYears;

	@NotNull(message = "Registered Date Is Required")
	@PastOrPresent(message = "Registered date cannot be in the future")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate registeredDate;
}
//accessMode = READ_ONLY tells Swagger not to expect this field in request input.
