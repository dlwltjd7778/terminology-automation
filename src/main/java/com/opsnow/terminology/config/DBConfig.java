package com.opsnow.terminology.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.inject.Named;

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
