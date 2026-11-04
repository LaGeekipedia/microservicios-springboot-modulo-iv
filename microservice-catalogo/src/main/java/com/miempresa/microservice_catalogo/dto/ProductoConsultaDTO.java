package com.miempresa.microservice_catalogo.dto;

import com.miempresa.microservice_catalogo.entity.Producto;

import java.math.BigDecimal;
import java.util.Map;

public record ProductoConsultaDTO(
        String id,
        String nombre,
        String categoria,
        BigDecimal precio,
        Map<String, Object> atributos
) {

    public static ProductoConsultaDTO desdeEntidad(Producto producto) {
        return new ProductoConsultaDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getCategoria(),
                producto.getPrecio(),
                producto.getAtributos()
        );
    }
}