package org.mySite.stock;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StockService {

    private final StockRepository stockRepository;

    public synchronized void decrease(Long id) {
        Stock stock = stockRepository.getById(id);
        stock.decrease();
        stockRepository.saveAndFlush(stock);
    }
}
