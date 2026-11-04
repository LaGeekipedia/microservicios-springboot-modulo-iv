package com.miempresa.microservice_catalogo.service.command;

import com.miempresa.microservice_catalogo.dto.ProductoCommandDTO;
import com.miempresa.microservice_catalogo.dto.ProductoPatchDTO;
import com.miempresa.microservice_catalogo.entity.Producto;
import com.miempresa.microservice_catalogo.repository.ProductoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
public class ProductoCommandService {

    private final ProductoRepository productoRepository;

    public ProductoCommandService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // Crear un producto
    public Producto creaProducto(ProductoCommandDTO command) {

        validarDatos(command.nombre(), command.categoria(), command.precio());

        Producto producto = new Producto(
                command.nombre(),
                command.categoria(),
                command.precio(),
                command.atributos()
        );

        return productoRepository.save(producto);
    }

    // Modificación completa de un producto
    public Producto actualizarProducto(String id, ProductoCommandDTO command) {

        Producto producto = buscarProducto(id);

        validarDatos(command.nombre(), command.categoria(), command.precio());

        producto.setNombre(command.nombre());
        producto.setCategoria(command.categoria());
        producto.setPrecio(command.precio());
        producto.setAtributos(command.atributos());

        return productoRepository.save(producto);
    }

    // Modificación parcial de un producto
    public Producto actualizarParcialmente(String id, ProductoPatchDTO command) {

        Producto producto = buscarProducto(id);

        if (command.nombre() == null
                && command.categoria() == null
                && command.precio() == null
                && command.atributos() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Debe ingresar al menos un campo para actualizar");
        }

        if (command.nombre() != null){
            if (command.nombre().isBlank()){
                throw new ResponseStatusException(
                  HttpStatus.BAD_REQUEST,
                  "El nombre no puede estar vacío"
                );
            }

            producto.setNombre(command.nombre());
        }

        if (command.categoria() != null){
            if (command.categoria().isBlank()){
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "La categoría no puede estar vacía"
                );
            }

            producto.setCategoria(command.categoria());
        }

        if (command.precio() != null){
            if (command.precio().compareTo(BigDecimal.ZERO) < 0){
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "El precio debe ser mayor o igual a cero"
                );
            }

            producto.setPrecio(command.precio());
        }

        if (command.atributos() != null){
            producto.setAtributos(command.atributos());
        }

        return productoRepository.save(producto);
    }

    // Eliminar producto
    public void eliminarProducto(String id) {

        Producto producto = buscarProducto(id);

        productoRepository.delete(producto);
    }


    // Métodos auxiliares
    // Métodoo para buscar un producto.
    private Producto buscarProducto(String id) {

        return productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe el producto con el id: " + id
                ));
    }

    // Validación de datos (Básica)
    private void validarDatos(String nombre, String categoria, BigDecimal precio) {

        if (nombre == null || nombre.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El nombre es obligatorio"
            );
        }

        if (categoria == null || categoria.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La categoría es obligatoria"
            );
        }

        if (precio == null || precio.compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El precio debe ser mayor o igual a 0"
            );
        }
    }
}