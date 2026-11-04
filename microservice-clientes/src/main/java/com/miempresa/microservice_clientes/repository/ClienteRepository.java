package com.miempresa.microservice_clientes.repository;

import com.miempresa.microservice_clientes.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository
        extends JpaRepository<Cliente, String> {
}
