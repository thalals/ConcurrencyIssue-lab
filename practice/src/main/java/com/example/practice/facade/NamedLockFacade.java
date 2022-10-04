package com.example.practice.facade;

import com.example.practice.repository.LockRepository;
import com.example.practice.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class NamedLockFacade {

    private final LockRepository lockRepository;
    private final StockService stockService;

    //부모의 트랜잭션과 별도로 실행되어야 함
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void decrease(final Long id, final Long quantity) {
        try {
            lockRepository.getLock(id.toString());
            stockService.decrease(id, quantity);
        }finally {
            //락의 해제
            lockRepository.releaseLock(id.toString());
        }
    }
}
