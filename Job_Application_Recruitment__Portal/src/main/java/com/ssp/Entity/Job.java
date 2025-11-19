package com.ssp.Entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Job {

    @Id
    @SequenceGenerator(name = "gen1", sequenceName = "job_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(generator = "gen1", strategy = GenerationType.SEQUENCE)
    private Long id;

    @NonNull
    private String title;
    @NonNull
    private String description;
    @NonNull
    private String location;
    @NonNull
    private String employementType;
    @NonNull
    private Double salaryRange;
    @NonNull
    private String companyName;
    @NonNull
    private LocalDate postedDate;
    
    
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NonNull
    private Status status;

    @ManyToOne
    @JoinColumn(name = "recruiter_id", referencedColumnName = "rid")
  @JsonBackReference   // Child side: Job → Recruiter
//    @JsonIgnore
    private Recruiter recruiter;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference  // Parent side: Job → Applications
    private List<Application> applications = new ArrayList<>();
}
