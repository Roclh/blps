package com.roclh.mainmodule.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:sensitive.properties")
@Getter
public class SensitivePropertiesWatcher {

    @Value("${studopedia.jwt_service.sekret_key:}")
    private String jwtServiceSekretKey;
}
