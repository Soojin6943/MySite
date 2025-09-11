package org.mySite.stock;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RequiredArgsConstructor
@Service
public class LockStockFacade {
    private ConcurrentHashMap<String, Lock> locks = new ConcurrentHashMap<>();

    private final StockService stockService;

    public void decrease(Long id) throws InterruptedException {
        Lock lock = locks.computeIfAbsent(String.valueOf(id), key -> new ReentrantLock());
        boolean acquiredLock = lock.tryLock(3, TimeUnit.SECONDS);
        if (!acquiredLock) {
            throw new RuntimeException("Lock 획득 실패");
        }
        try {
            stockService.decrease(id);
        } finally {
            lock.unlock();
        }
    }
}
