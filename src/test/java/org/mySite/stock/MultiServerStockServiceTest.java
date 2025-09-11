package org.mySite.stock;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiServerStockServiceTest {

    @Test
    void decrease_with_100_request_to_multi_server() throws InterruptedException {
        // given
        int threadCount = 100;
        RestTemplate restTemplate = new RestTemplate();
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i=0; i<threadCount; i++){
            final int ii = i;
            executorService.submit(() -> {
                try {
                    int port = (ii % 2 == 0) ? 8080 : 8081;
                    ResponseEntity<Void> forEntity = restTemplate.getForEntity(
                            "http://localhost:" + port + "/stocks/11/decrease",
                            void.class);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
    }
}
