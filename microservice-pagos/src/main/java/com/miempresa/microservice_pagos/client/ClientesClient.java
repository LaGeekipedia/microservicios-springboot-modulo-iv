package com.miempresa.microservice_pagos.client;

import com.miempresa.microservice_pagos.dto.ClienteDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ClientesClient {

    private final RestClient restClient;

    public ClientesClient(
            @Value("${app.clientes.url}") String clientesUrl) {

        this.restClient = RestClient.builder()
                .baseUrl(clientesUrl)
                .build();
    }

    public ClienteDTO buscarCliente(String clienteId) {

        return restClient.get()
                .uri("/clientes/{clienteId}", clienteId)
                .retrieve()
                .body(ClienteDTO.class);
    }
}
