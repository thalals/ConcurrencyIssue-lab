package com.example.practice.facade;

import com.example.practice.repository.RedisLockRepository;
import com.example.practice.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LettuceLockStockFacade  {

    private final RedisLockRepository redisLockRepository;
    private final StockService stockService;

    public void decrease(final Long key, final Long quantity) throws InterruptedException {
        // Lock 획득 시도
        while (!redisLockRepository.lock(key)) {
            //SpinLock 방식이 redis 에게 주는 부하를 줄여주기위한 sleep
            Thread.sleep(100);
        }

        //lock 획득 성공시
        try{
            stockService.decrease(key,quantity);
        }finally {
            //락 해제
            redisLockRepository.unlock(key);
        }
    }
}
