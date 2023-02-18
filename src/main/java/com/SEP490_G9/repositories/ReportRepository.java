package com.SEP490_G9.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SEP490_G9.models.Entities.Report;
import com.SEP490_G9.models.embeddables.ReportItemKey;

@Repository
public interface ReportRepository extends JpaRepository<Report,ReportItemKey> {

}
