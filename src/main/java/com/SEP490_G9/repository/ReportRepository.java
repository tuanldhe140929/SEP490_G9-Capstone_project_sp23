package com.SEP490_G9.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SEP490_G9.entity.Report;
import com.SEP490_G9.entity.embeddable.ReportItemKey;


@Repository
public interface ReportRepository extends JpaRepository<Report,ReportItemKey> {

}
