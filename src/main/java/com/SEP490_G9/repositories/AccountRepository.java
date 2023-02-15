package com.SEP490_G9.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SEP490_G9.models.Entities.Account;
import com.SEP490_G9.models.Entities.RefreshToken;

public interface AccountRepository extends JpaRepository<Account, Long> {

	boolean existsByEmail(String email);

	Account findByRefreshToken(RefreshToken findByToken);

	Account findByEmail(String name);

}
