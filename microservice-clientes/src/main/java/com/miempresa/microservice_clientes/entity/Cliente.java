package com.miempresa.microservice_clientes.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @Column(name = "cliente_id", nullable = false)
    private String clienteId;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "email", nullable = false)
    private String email;

    public Cliente() {
    }

    public Cliente(String clienteId, String nombre, String email) {
        this.clienteId = clienteId;
        this.nombre = nombre;
        this.email = email;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
