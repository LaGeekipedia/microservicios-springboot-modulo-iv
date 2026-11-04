package com.miempresa.microservice_demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
public class AppConfig {

    @Bean
    // Balanceo de cargas
    @LoadBalanced
    // Timeouts (Modernos, no deprecados)
    public RestTemplate restTemplate(RestTemplateBuilder builder,
                                     @Value("${app.rest.connectTimeout:1s}")Duration connectTimeout,
                                     @Value("${app.rest.readTimeout:1s}") Duration readTimeout) {

        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(connectTimeout)
                .build();

        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(readTimeout);


        return builder
                .requestFactory(() -> requestFactory)
                .build();
    }

}
