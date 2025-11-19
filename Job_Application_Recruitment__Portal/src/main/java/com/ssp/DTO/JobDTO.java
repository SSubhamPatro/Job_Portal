package com.ssp.DTO;

import java.time.LocalDate;

import com.ssp.Entity.Status;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class JobDTO {

    private Long id;

    @NotBlank(message = "Job title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @NotBlank(message = "Job description cannot be empty")
    @Size(min = 10, max = 1000, message = "Description must be between 10 and 1000 characters")
    private String description;

    @NotBlank(message = "Location is required")
    @Size(min = 2, max = 100, message = "Location must be between 2 and 100 characters")
    private String location;

    @NotBlank(message = "Employment type is required (e.g., Full-time, Part-time)")
    private String employementType;

    @NotNull(message = "Salary range is required")
    @Positive(message = "Salary must be a positive number")
    private Double salaryRange;

    @NotBlank(message = "Company Name Is Required")
    @Size(min = 2, max = 100, message = "Company name must be between 2 and 100 characters")
    private String companyName;

    @PastOrPresent(message = "Posted date cannot be in the future")
    private LocalDate postedDate;

    @NotNull(message = "Status is required")
    private Status status;

    @NotNull(message = "Recruiter ID is required")
    private Long recruiterId;
}
