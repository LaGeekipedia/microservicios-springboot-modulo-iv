package com.miempresa.microservice_pagos.messaging;

import com.miempresa.microservice_pagos.events.PagoCreadoEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PagosPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.exchange}")
    private String exchange;

    @Value("${app.rabbitmq.routingKey}")
    private String routingKey;

    @Autowired
    public PagosPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publicarPagoCreado(PagoCreadoEvent event) {
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
    }
}
