package com.SEP490_G9.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SEP490_G9.entities.Violation;

public interface ViolationRepository extends JpaRepository<Violation, Long>{
	List<Violation> findAll();
	Optional<Violation> findById(Long id);
	Violation findByName(String name);
}
