package org.example.fakeceit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling

public class FakeceitApplication {

    public static void main(String[] args) {
        SpringApplication.run(FakeceitApplication.class, args);
    }

}
