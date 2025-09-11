package org.mySite.stock;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;
    private final StockRepository stockRepository;

    @GetMapping("/stocks")
    public void createStock() {
        stockRepository.save(new Stock(1L, 100));
    }

    @GetMapping("/stocks/{id}/decrease")
    public void decreaseStock (@PathVariable Long id){
        stockService.decrease(id);
    }

}
