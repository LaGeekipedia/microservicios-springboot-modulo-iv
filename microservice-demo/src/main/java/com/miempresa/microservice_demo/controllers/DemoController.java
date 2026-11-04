package com.miempresa.microservice_demo.controllers;

import com.miempresa.microservice_demo.service.PagosCliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RefreshScope
@RestController
public class DemoController {

    private final RestTemplate restTemplate;
    private final PagosCliente pagosCliente;

    @Autowired
    public DemoController(RestTemplate restTemplate, PagosCliente pagosCliente) {
        this.restTemplate = restTemplate;
        this.pagosCliente = pagosCliente;
    }

    @Value("${mensaje}")
    private String mensaje;

    @GetMapping("/mensaje")
    public String obtenerMensaje() {
        return mensaje;
    }

    @GetMapping("/pagos")
    public String obtenerMensajePagos() {
        String url = "http://microservice-pagos/pagos/mensaje";
        return restTemplate.getForObject(url, String.class);
    }

    @GetMapping("/pagos/lento")
    public String pagosLento(@RequestParam(defaultValue = "0") long ms) {
        return pagosCliente.llamarPagosLento(ms);
    }

}
