package com.roclh.blps;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EntryPointApplication {

    private static final Logger log = LogManager.getLogger(EntryPointApplication.class);

    public static void main(String[] args) {
        log.info("Pussy tight pussy clean pussy fresh");
        SpringApplication.run(EntryPointApplication.class, args);
    }

}