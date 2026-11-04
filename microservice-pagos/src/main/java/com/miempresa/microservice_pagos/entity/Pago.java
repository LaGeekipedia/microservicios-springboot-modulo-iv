package com.miempresa.microservice_pagos.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "pagos")
public class Pago {

    @Id
    @Column(name = "pago_id", nullable = false)
    private String pagoId;

    @Column(name = "cliente_id", nullable = false)
    private String clienteId;

    @Column(name = "monto", nullable = false, precision = 19, scale = 2)
    private BigDecimal monto;

    @Column(name = "moneda", nullable = false)
    private String moneda;

    @Column(name = "fecha_creacion", nullable = false)
    private Instant fechaCreacion;

    public Pago() {
    }

    public Pago(String pagoId, String clienteId, BigDecimal monto,
                String moneda, Instant fechaCreacion) {
        this.pagoId = pagoId;
        this.clienteId = clienteId;
        this.monto = monto;
        this.moneda = moneda;
        this.fechaCreacion = fechaCreacion;
    }

    public String getPagoId() {
        return pagoId;
    }

    public void setPagoId(String pagoId) {
        this.pagoId = pagoId;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public Instant getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
