package org.mySite.stock;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class StockServiceTest {

    @Autowired
    private StockService stockService;

    @Autowired
    private StockRepository stockRepository;

    private Long stockId;

    @BeforeEach
    void setUp() {
        stockId = stockRepository.saveAndFlush(new Stock(1L, 100))
                .getId();
    }

    @AfterEach
    void tearDown() {
        stockRepository.deleteAll();
    }

    @Test
    @DisplayName("요청 1번")
    void decreaseStock() {
        // given
        stockService.decrease(stockId);

        // when
        Stock stock = stockRepository.getById(stockId);

        // then
        assertThat(stock.getQuantity()).isEqualTo(99);
    }

    @Test
    @DisplayName("동시 요청 100개")
    void decrease_with_100_request() throws InterruptedException {
        //given
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i=0; i< threadCount; i++){
            executorService.submit(() -> {
                try {
                    stockService.decrease(stockId);
                } catch (Exception e) {
                    System.out.println("에러: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();

        // then
        Stock stock = stockRepository.getById(stockId);
        assertThat(stock.getQuantity()).isEqualTo(0);
    }


}
