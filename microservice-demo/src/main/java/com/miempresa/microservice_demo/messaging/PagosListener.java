package com.miempresa.microservice_demo.messaging;

import com.miempresa.microservice_demo.events.PagoCreadoEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class PagosListener {

    @Value("${app.rabbitmq.queue}")
    private String queueName;

    @RabbitListener(queues = "${app.rabbitmq.queue}")
    public void onPagoCreado(@Payload PagoCreadoEvent event) {
        System.out.println("[microservice-demo] Evento recibido: " + event.getPagoId() +
                " monto=" + event.getMonto() + " " + event.getMoneda());
    }

}
