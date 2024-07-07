package com.assignment.repository;

import com.assignment.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Transaction, Long> {
}
