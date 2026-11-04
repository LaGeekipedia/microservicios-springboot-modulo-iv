package com.miempresa.microservice_catalogo.dto;

import java.math.BigDecimal;
import java.util.Map;

public record ProductoCommandDTO(
        String nombre,
        String categoria,
        BigDecimal precio,
        Map<String, Object> atributos
) {
}