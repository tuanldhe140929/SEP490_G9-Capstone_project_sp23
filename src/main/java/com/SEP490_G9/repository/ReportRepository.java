package com.SEP490_G9.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.Report;
import com.SEP490_G9.entities.User;
import com.SEP490_G9.entities.embeddable.ReportItemKey;

@Repository
public interface ReportRepository extends JpaRepository<Report, ReportItemKey> {
	
	List<Report> findAll();
	
	List<Report> findByProduct(Product product);
}
