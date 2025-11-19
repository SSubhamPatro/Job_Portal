package com.ssp.Entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Application {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NonNull
	@ManyToOne
	@JoinColumn(name = "job_id")
	private Job job;

	@NonNull
	@ManyToOne
	@JoinColumn(name = "application_id")
	private Applicant applicant;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@NonNull
	private Status status;

	@NonNull
	private LocalDateTime appliedDate;

	@OneToOne(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference  // Parent side: Application → InterviewSchedule
	private InterviewSchedule schedule;
}
