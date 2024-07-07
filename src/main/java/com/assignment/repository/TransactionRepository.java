package com.assignment.repository;

import com.assignment.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Transaction findFirstByCurrencyPairOrderByTimestampDesc(String currencyPair);

    List<Transaction> findByUserId(Long userId);

    List<Transaction> findByUserIdOrderByTimestampDesc(Long userId);
}
