package com.SEP490_G9.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SEP490_G9.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	Role findById(int id);

	Role findByName(String roleName);
}
