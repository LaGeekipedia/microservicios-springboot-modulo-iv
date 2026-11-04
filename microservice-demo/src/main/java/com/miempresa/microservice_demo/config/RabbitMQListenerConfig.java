package com.miempresa.microservice_demo.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQListenerConfig {

    @Bean
    public Queue pagosQueue(@Value("${app.rabbitmq.queue}") String queueName) {
        return new Queue(queueName, true);
    }

}