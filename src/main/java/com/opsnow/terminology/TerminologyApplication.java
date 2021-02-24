package com.opsnow.terminology;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class TerminologyApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(TerminologyApplication.class, args);
    }

}
