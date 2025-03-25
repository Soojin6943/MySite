package org.mySite;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServerHealthCheck {

    @GetMapping("/health")
    public String healthCheck() {
        return "server health check ok";
    }
}
