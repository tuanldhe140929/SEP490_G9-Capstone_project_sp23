package com.SEP490_G9.repository;

import com.SEP490_G9.entities.ViolationType;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ViolationTypeRepository extends JpaRepository<ViolationType, Long> { 
	public ViolationType findById(long id);
}
