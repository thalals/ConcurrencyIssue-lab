package com.example.practice.facade;

import com.example.practice.service.OptimisticLockStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OptimisticLockStockFacade {

    private OptimisticLockStockService optimisticLockStockService;

    public void decrease(final Long id, final Long quantity) throws InterruptedException {
        while (true) {
            try {
                optimisticLockStockService.decrease(id, quantity);
            } catch (Exception e) {
                Thread.sleep(50);
            }
        }
    }
}
