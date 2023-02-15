package com.SEP490_G9.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.SEP490_G9.helpers.Constant;
import com.SEP490_G9.models.Entities.Account;
import com.SEP490_G9.models.Entities.RefreshToken;
import com.SEP490_G9.models.Entities.Role;

public interface AccountRepository extends JpaRepository<Account, Long> {
	
	boolean existsByEmail(String email);

	Account findByRefreshToken(RefreshToken findByToken);

	Account findByEmail(String name);
	
	List<Account> findAll();
	
	Optional<Account> findById(Long id);

	List<Account> findByRolesIn(List<Role> roles);
}
