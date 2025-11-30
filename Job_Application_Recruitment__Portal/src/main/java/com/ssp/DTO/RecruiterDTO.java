package com.ssp.DTO;

import com.ssp.Entity.CompanyType;
import com.ssp.utility.ValidPhoneNumber;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
//RecruiterDTO → for registration (requires password, email)
@Data
public class RecruiterDTO {
  
    private Long rdid;

    @NotBlank(message = "Recruiter name is Required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @NotBlank(message = "Email is Required")
    @Email(message = "Invalid email format")
    private String email;
//    
//    @NotBlank(message = "Company name is Required")
//    private String companyName;
    
    @NotBlank(message = "Department is Required")
    @Size(min = 2, max = 50, message = "Department must be between 2 and 50 characters")
    private String department;
    
    @NotBlank(message = "Designation Is Required")
    private String designation;
    
    @NotBlank(message = "Location Is Required")
    @Size(min = 2, max = 100, message = "Location must be between 2 and 100 characters")
    private String location;
    
    @ValidPhoneNumber
    private String phone;
    
    
    @NotNull(message = "Company Type Is Required")
    private CompanyType companyType;
    
    @NotBlank(message = "Organization Is Required")
    private String organization;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
}
