package com.roclh.blps;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.stream.Stream;

@SpringBootApplication
public class EntryPointApplication {

    private static final Logger log = LogManager.getLogger(EntryPointApplication.class);
    private static ApplicationContext applicationContext;

    public static void main(String[] args) {
        applicationContext = SpringApplication.run(EntryPointApplication.class, args);
    }

    public static void displayAllBeans(String filter) {
        String[] allBeanNames = applicationContext.getBeanDefinitionNames();
        Stream.of(allBeanNames).filter(
                (val) -> val.contains(filter)
        ).forEach(log::info);
    }
}
