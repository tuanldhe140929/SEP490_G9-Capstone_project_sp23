package com.SEP490_G9.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SEP490_G9.entity.Transaction;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findByCartId(Long cartId);
}
