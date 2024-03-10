package com.roclh.mainmodule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.stream.Stream;

@SpringBootApplication
@Slf4j
public class EntryPointApplication {
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
