package com.miempresa.microservice_catalogo.service.query;

import com.miempresa.microservice_catalogo.dto.ProductoConsultaDTO;
import com.miempresa.microservice_catalogo.entity.Producto;
import com.miempresa.microservice_catalogo.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoQueryService {

    private final ProductoRepository productoRepository;

    public ProductoQueryService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // Obtener todos los productos
    public List<ProductoConsultaDTO> obtenerProductos() {

        return productoRepository.findAll()
                .stream()
                .map(ProductoConsultaDTO::desdeEntidad)
                .toList();
    }

    // Obtener producto por ID
    public Optional<ProductoConsultaDTO> obtenerProductoPorId(String id) {

        return productoRepository.findById(id)
                .map(ProductoConsultaDTO::desdeEntidad);
    }
}
