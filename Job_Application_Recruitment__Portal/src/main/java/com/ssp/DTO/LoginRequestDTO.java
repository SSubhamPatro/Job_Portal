package com.ssp.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequestDTO {

	@NotBlank(message = "Email Cannot BE Blank")
	@Email(message = "Invalid Email Format")
	private String email;
	
	@NotBlank(message = "Password Cannot Be Blank")
	private String password;
}
