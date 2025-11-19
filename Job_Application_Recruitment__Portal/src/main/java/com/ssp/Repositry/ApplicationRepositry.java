package com.ssp.Repositry;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssp.Entity.Application;
//ApplicantService → register applicants
public interface ApplicationRepositry extends JpaRepository<Application, Long> {

}
