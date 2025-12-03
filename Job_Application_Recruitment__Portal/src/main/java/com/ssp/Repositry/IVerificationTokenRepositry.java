package com.ssp.Repositry;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssp.Entity.UserAccount;
import com.ssp.Entity.VerificationToken;

public interface IVerificationTokenRepositry extends JpaRepository<VerificationToken, Long> {

	Optional<VerificationToken> findByToken(String token);
	void deleteByExpiryDateBefore(LocalDateTime time);
	void deleteByAccount(UserAccount account); 
}
