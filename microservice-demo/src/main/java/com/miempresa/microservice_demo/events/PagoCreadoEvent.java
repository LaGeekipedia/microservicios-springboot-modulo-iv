package com.miempresa.microservice_demo.events;

import java.math.BigDecimal;
import java.time.Instant;

public class PagoCreadoEvent {

    private String pagoId;
    private String clienteId;
    private BigDecimal monto;
    private String moneda;
    private Instant fechaCreacion;

    public PagoCreadoEvent() {
    }

    public PagoCreadoEvent(String pagoId, String clienteId, BigDecimal precio, String moneda, Instant fechaCreacion) {
        this.pagoId = pagoId;
        this.clienteId = clienteId;
        this.monto = precio;
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
