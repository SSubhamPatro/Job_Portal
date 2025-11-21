package com.ssp.Entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class Applicant {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	// name ,email,phone,resumeLink,skills,experience Years,registeredDate

	@NonNull
	private String name;
	
	@NonNull
	private String phoneNumber;

//	@NonNull
	private String resumeLink;

	@NonNull
	private String skills;

	@NonNull
	private Integer experienceYears;

	@NonNull
	private LocalDate registeredDate;
    
	@OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference // Parent side: Applicant → Applications
	private List<Application> application = new ArrayList<Application>();

	@OneToOne
	@JoinColumn(name = "user_id")
	private UserAccount userAccount;
}
