package com.miempresa.microservice_clientes.controller;

import com.miempresa.microservice_clientes.entity.Cliente;
import com.miempresa.microservice_clientes.service.ClienteService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
public class ClientesController {

    private final ClienteService clienteService;

    public ClientesController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping("/crear")
    public String crearCliente(@RequestParam String clienteId,
                               @RequestParam String nombre,
                               @RequestParam String email) {
        Cliente cliente = new Cliente(clienteId, nombre, email);
        clienteService.guardarCliente(cliente);
        return "Cliente creado: " + clienteId;
    }

    @GetMapping("/{clienteId}")
    public Cliente buscarClientePorId(@PathVariable String clienteId) {
        return clienteService.buscarClientePorId(clienteId);
    }
}
