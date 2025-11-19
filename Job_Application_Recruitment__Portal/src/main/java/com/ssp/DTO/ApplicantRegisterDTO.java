package com.ssp.DTO;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ApplicantRegisterDTO {

	private Long id;

	@NotBlank(message = "Name Is Required")
	@Size(min = 2, max = 50, message = "Name Must Be 2-50 Character Long")
	private String name;

	@Email(message = "Invalid Email Format")
	@NotBlank(message = "Email Is Required")
	private String email;

	@NotBlank(message = "Password Is Required")
	@Size(min = 6, max = 20, message = "Password Must Be Between 6 And 20 Characters")
	private String password;

	@NotNull(message = "Phone Number Is Required")
	@Digits(integer = 10, fraction = 0, message = "Phone Number Must Be 10 Digits")
	private Long phoneNumber;

	@Schema(description = "Resume file URL, Automatically Generated After Upload", accessMode = Schema.AccessMode.READ_ONLY)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String resumeLink;

	@NotBlank(message = "Skills Are Required")
	@Size(min = 2,max = 100,message ="Skills Description Must Be 2-100 Character Long" )
	private String skills;

	@NotNull(message = "Experience Years Are Required")
	@Min(value = 0,message = "Experience Years Cannot Be Negative")
	@Max(value = 50,message = "Experience Years Cannot Exceed 50")
	private Integer experienceYears;

	@NotNull(message = "Registered Date Is Required")
	@PastOrPresent(message = "Registered Date Cannot Be In Future")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate registeredDate;

}
