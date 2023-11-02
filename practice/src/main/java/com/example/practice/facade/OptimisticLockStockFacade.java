package com.example.practice.facade;

import com.example.practice.service.OptimisticLockStockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OptimisticLockStockFacade {

    private OptimisticLockStockService optimisticLockStockService;

    public void decrease(final Long id, final Long quantity) throws InterruptedException {
        while (true) {
            try {
                optimisticLockStockService.decrease(id, quantity);
                break;

            } catch (Exception e) {
                log.error("error : ", e);
                Thread.sleep(50);
            }
        }
    }
}
