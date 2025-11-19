package com.ssp.Repositry;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssp.Entity.UserAccount;

public interface IUserAccountRepositry extends JpaRepository<UserAccount, Long> {

	Optional<UserAccount> findByEmail(String email);
    boolean existsByEmail(String email);

}
