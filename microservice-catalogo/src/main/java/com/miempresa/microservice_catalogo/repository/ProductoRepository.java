package com.miempresa.microservice_catalogo.repository;

import com.miempresa.microservice_catalogo.entity.Producto;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductoRepository
        extends MongoRepository<Producto, String> {

}
