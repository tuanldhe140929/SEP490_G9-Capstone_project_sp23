package com.SEP490_G9.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SEP490_G9.entities.Payout;

public interface PayoutRepository extends JpaRepository<Payout,Long> {

	List<Payout> findByTransactionId(Long transactionId);

}
