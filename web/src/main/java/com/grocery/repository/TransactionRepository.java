package com.grocery.repository;

import com.grocery.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByCustomerUsernameOrderByTransactionDateDesc(String username);
}
