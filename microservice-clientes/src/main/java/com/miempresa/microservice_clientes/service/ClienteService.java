package com.miempresa.microservice_clientes.service;

import com.miempresa.microservice_clientes.entity.Cliente;
import com.miempresa.microservice_clientes.repository.ClienteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente guardarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Cliente buscarClientePorId(String clienteId) {
        return clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cliente no encontrado: " + clienteId));
    }
}
