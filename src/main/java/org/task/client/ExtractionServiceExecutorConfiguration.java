package org.task.client;

import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.task.client.common.ExecutorFactory;

@Configuration
public class ExtractionServiceExecutorConfiguration {

    private final ExecutorFactory executorFactory;

    public ExtractionServiceExecutorConfiguration(ExecutorFactory executorFactory) {
        this.executorFactory = executorFactory;
    }

    @Bean(name = "extractionServiceExecutor")
    public ExecutorService getServiceExecutor(
        @Value("${integration.extraction-service.corePoolSize}") int corePoolSize,
        @Value("${integration.extraction-service.maxPoolSize}") int maxPoolSize,
        @Value("${integration.extraction-service.keepAliveTime}") int keepAliveTime) {
        return executorFactory.newFixedThreadPoolExecutor(
            corePoolSize,
            maxPoolSize,
            keepAliveTime);
    }
}
