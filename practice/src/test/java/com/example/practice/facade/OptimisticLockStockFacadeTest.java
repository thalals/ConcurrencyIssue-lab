package com.example.practice.facade;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.practice.domain.Stock;
import com.example.practice.repository.StockRepository;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OptimisticLockStockFacadeTest {
    @Autowired
    private OptimisticLockStockFacade stockFacade;

    @Autowired
    private StockRepository stockRepository;

    @BeforeEach
    public void before() {
        Stock stock = new Stock(1L, 100L);
        stockRepository.saveAndFlush(stock);
    }

    @AfterEach
    public void after() {
        stockRepository.deleteAll();
    }


    @Test
    @DisplayName("Pessimistic LOCK 동시에_100개의_요청")
    public void Pessimistic_requests_100_AtTheSameTime() throws InterruptedException {
        int threadCount = 100;
        //멀티스레드 이용 ExecutorService : 비동기를 단순하게 처리할 수 있또록 해주는 java api
        ExecutorService executorService = Executors.newFixedThreadPool(32);

        //다른 스레드에서 수행이 완료될 때 까지 대기할 수 있도록 도와주는 API - 요청이 끝날때 까지 기다림
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                    try {
                        stockFacade.decrease(1L, 1L);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } finally {
                        latch.countDown();
                    }
                }
            );
        }

        latch.await();

        Stock stock = stockRepository.findById(1L).orElseThrow();

        //100 - (1*100) = 0
        // 레이스 컨디션 발생 fail
        assertThat(stock.getQuantity()).isZero();

    }
}