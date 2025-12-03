package com.ssp.Service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssp.Repositry.IVerificationTokenRepositry;

@Service
@EnableScheduling
@Transactional
public class TokenCleanupService {

	@Autowired
	private IVerificationTokenRepositry tokenRepo;

	@Scheduled(cron = "0 */5 * * * *")
	public void cleanupExpiryTokens() {

		tokenRepo.deleteByExpiryDateBefore(LocalDateTime.now());

	}
}
