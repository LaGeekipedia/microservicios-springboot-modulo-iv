package com.miempresa.microservice_demo.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PagosCliente {

    private final RestTemplate restTemplate;

    @Autowired
    public PagosCliente(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Retry(name = "pagosRetry")
    @CircuitBreaker(name = "pagosCB", fallbackMethod = "pagosFallBack")
    public String llamarPagosLento(long ms) {
        String url = "http://microservice-pagos/pagos/lento?ms=" + ms;
        return restTemplate.getForObject(url, String.class);
    }

    private String pagosFallBack(long ms, Throwable ex) {
        return "Estamos en mantenimiento, (fallback). Retries/CB activados. Causa: " + ex.getClass().getSimpleName();
    }

}
