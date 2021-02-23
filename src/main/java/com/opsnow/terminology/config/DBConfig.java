package com.opsnow.terminology.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
@Component
@ConfigurationProperties(prefix="spring.datasource")
public class DBConfig {

    private String driverClassName;
    private String url;
    private String username;
    private String password;
}
