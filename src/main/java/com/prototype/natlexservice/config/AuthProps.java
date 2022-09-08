package com.prototype.natlexservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "authorization")
@ConstructorBinding
public record AuthProps(String token) {
}
