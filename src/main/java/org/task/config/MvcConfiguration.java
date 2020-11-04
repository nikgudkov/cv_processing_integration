package org.task.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.task.controller.interceptor.FileUploadInterceptor;

@Configuration
public class MvcConfiguration implements WebMvcConfigurer {

    @Value("${max-file-upload-size}")
    private int maxFileUploadSize;

    @Bean
    public RestTemplate restTemplate(@Value("${http-client.readTimeout}") int readTimeout, @Value("${http-client.connectTimeout}") int connectTimeout) {
        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(readTimeout);
        factory.setReadTimeout(connectTimeout);
        return new RestTemplate(factory);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new FileUploadInterceptor(maxFileUploadSize)).addPathPatterns("/submit");
    }

}

