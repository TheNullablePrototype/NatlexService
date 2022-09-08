package com.prototype.natlexservice;

import com.prototype.natlexservice.config.AuthProps;
import com.prototype.natlexservice.config.TaskHandlerProps;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({TaskHandlerProps.class, AuthProps.class})
public class NatlexServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NatlexServiceApplication.class, args);
    }

}
