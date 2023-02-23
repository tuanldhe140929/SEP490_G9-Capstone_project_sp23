package com.SEP490_G9.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.SEP490_G9.entity.Account;
import com.SEP490_G9.entity.RefreshToken;
import com.SEP490_G9.entity.Role;


@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
	
	boolean existsByEmail(String email);

	Account findByRefreshToken(RefreshToken findByToken);

	Account findByEmail(String name);
	
	List<Account> findAll();
	
	Optional<Account> findById(Long id);

	List<Account> findByRolesIn(List<Role> roles);
}
