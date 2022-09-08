package com.prototype.natlexservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "task.handler.thread.pool")
@ConstructorBinding
public record TaskHandlerProps(int queueMaxCapacity, int threadsSize, int threadsSizeMax, long keepAliveTime,
                               long shutdownTimeout, int scheduledThreadsSize, int exportAliveTime) {

}
