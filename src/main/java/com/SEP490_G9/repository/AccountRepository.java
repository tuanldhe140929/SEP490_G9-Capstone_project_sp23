package com.SEP490_G9.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.RefreshToken;
import com.SEP490_G9.entities.Role;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

	boolean existsByEmail(String email);

	Account findByRefreshToken(RefreshToken findByToken);

	Account findByEmail(String name);

	List<Account> findAll();

	Optional<Account> findById(Long id);

	List<Account> findByRolesIn(List<Role> roles);
}
