package com.opsnow.terminology;

import com.opsnow.terminology.config.DBConfig;
import com.opsnow.terminology.service.Facade;
import com.opsnow.terminology.service.GoogleAPIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = {"com.opsnow.terminology.config"})
@Import({DBConfig.class})
public class TerminologyApplication extends SpringBootServletInitializer {

    private static Logger logger = LoggerFactory.getLogger(TerminologyApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(TerminologyApplication.class, args);
        System.out.println("build success");
    }

}
