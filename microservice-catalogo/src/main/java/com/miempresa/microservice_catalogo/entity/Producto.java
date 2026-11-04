package com.miempresa.microservice_catalogo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.util.Map;

@Document(collection = "productos")
public class Producto {

    @Id
    private String id;

    private String nombre;

    private String categoria;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal precio;

    private Map<String, Object> atributos;

    public Producto() {

    }

    public Producto(String nombre, String categoria, BigDecimal precio, Map<String, Object> atributos) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.atributos = atributos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Map<String, Object> getAtributos() {
        return atributos;
    }

    public void setAtributos(Map<String, Object> atributos) {
        this.atributos = atributos;
    }
}
