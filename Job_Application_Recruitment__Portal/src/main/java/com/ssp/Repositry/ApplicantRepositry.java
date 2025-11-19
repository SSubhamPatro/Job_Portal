package com.ssp.Repositry;


import org.springframework.data.jpa.repository.JpaRepository;

import com.ssp.Entity.Applicant;

public interface ApplicantRepositry extends JpaRepository<Applicant, Long> {
//
//	Optional<Applicant>findByEmail(String email);
//	boolean existsByEmail(String email); 
}
