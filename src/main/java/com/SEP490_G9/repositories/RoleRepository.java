package com.SEP490_G9.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SEP490_G9.models.Entities.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	Role findById(int id);
}
