package com.miempresa.microservice_catalogo.controller;

import com.miempresa.microservice_catalogo.dto.ProductoCommandDTO;
import com.miempresa.microservice_catalogo.dto.ProductoConsultaDTO;
import com.miempresa.microservice_catalogo.dto.ProductoPatchDTO;
import com.miempresa.microservice_catalogo.entity.Producto;
import com.miempresa.microservice_catalogo.service.command.ProductoCommandService;
import com.miempresa.microservice_catalogo.service.query.ProductoQueryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductosController {

    private final ProductoCommandService commandService;
    private final ProductoQueryService queryService;

    public ProductosController(ProductoQueryService queryService,
                               ProductoCommandService commandService) {
        this.queryService = queryService;
        this.commandService = commandService;
    }

    @PostMapping("/crear")
    @ResponseStatus(HttpStatus.CREATED)
    public Producto crearProducto(@RequestBody ProductoCommandDTO producto) {
        return commandService.creaProducto(producto);
    }

    @GetMapping
    public List<ProductoConsultaDTO> obtenerProductos() {
        return queryService.obtenerProductos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoConsultaDTO> obtenerProducto(@PathVariable String id) {

        return queryService.obtenerProductoPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(
            @PathVariable String id,
            @RequestBody ProductoCommandDTO producto) {

        Producto actualizado = commandService.actualizarProducto(id, producto);
        return ResponseEntity.ok(actualizado);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Producto> actualizarParcialmente(
            @PathVariable String id,
            @RequestBody ProductoPatchDTO producto) {

        Producto actualizado = commandService.actualizarParcialmente(id, producto);

        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable String id) {
        commandService.eliminarProducto(id);

        return ResponseEntity.noContent().build();
    }
}