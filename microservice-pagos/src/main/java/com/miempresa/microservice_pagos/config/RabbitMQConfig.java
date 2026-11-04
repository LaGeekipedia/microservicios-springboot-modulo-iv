package com.miempresa.microservice_pagos.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${app.rabbitmq.exchange}")
    private String exchangeName;

    @Value("${app.rabbitmq.queue}")
    private String queueName;

    @Value("${app.rabbitmq.routingKey}")
    private String routingKey;

    @Bean
    public TopicExchange pagosExchange() {
        return ExchangeBuilder.topicExchange(exchangeName)
                .durable(true).build();
    }

    @Bean
    public Queue pagosQueue() {
        return QueueBuilder.durable(queueName)
                .build();
    }

    @Bean
    public Binding pagosBinding(Queue pagosQueue, TopicExchange pagosExchange) {
        return BindingBuilder.bind(pagosQueue)
                .to(pagosExchange)
                .with(routingKey);
    }

    @Bean
    public Jackson2JsonMessageConverter jacksonConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf,
                                         Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(cf);
        template.setMessageConverter(converter);

        return template;
    }

}
