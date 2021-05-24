package com.ucm.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ucm.lib.services.MyService;

/**
 * Mainly exists for integration testing the package structure. Will be repurposed later.
 */
@SpringBootApplication(scanBasePackages = "com.ucm")
@RestController
public class DemoApplication {
    private final MyService myService;

    @Autowired
    public DemoApplication(MyService myService) {
        this.myService = myService;
    }

    @GetMapping("/")
    public String home() {
        return myService.message();
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
