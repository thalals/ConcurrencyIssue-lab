package com.example.practice.repository;

import com.example.practice.domain.Stock;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StockRepository extends JpaRepository<Stock, Long> {

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Stock s where s.id = :id")
    Stock findByWithPessimisticLock(final Long id);

    @Lock(value = LockModeType.OPTIMISTIC)
    @Query("select s from Stock s where s.id = :id")
    Stock findByWithOptimisticLock(final Long id);


}
