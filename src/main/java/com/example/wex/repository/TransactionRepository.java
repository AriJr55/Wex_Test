package com.example.wex.repository;

import com.example.wex.entity.Transactions;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public interface TransactionRepository extends JpaRepository<Transactions, Long> {

    List<Transactions> findByTransactionDateBetweenOrderByIdDesc(LocalDate initDate, LocalDate endDate);

    List<Transactions> findByDestinyKeyOrderByTransactionDateDesc(Long destinyKey, PageRequest page);
}
