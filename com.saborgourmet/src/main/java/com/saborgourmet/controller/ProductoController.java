package com.saborgourmet.controller;

import com.saborgourmet.model.Producto;
import com.saborgourmet.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "Operaciones relacionadas con los productos del menú")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    @Operation(summary = "Obtener todos los productos")
    public List<Producto> getAllProductos() {
        return productoService.findAll();
    }

    @GetMapping("/disponibles")
    @Operation(summary = "Obtener productos disponibles")
    public List<Producto> getProductosDisponibles() {
        return productoService.findAvailable();
    }

    @GetMapping("/categoria/{categoria}")
    @Operation(summary = "Obtener productos por categoría")
    public List<Producto> getProductosByCategoria(@PathVariable String categoria) {
        return productoService.findByCategory(categoria);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un producto por ID")
    public ResponseEntity<Producto> getProductoById(@PathVariable Long id) {
        return productoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo producto")
    public Producto createProducto(@RequestBody Producto producto) {
        return productoService.save(producto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un producto existente")
    public ResponseEntity<Producto> updateProducto(@PathVariable Long id, @RequestBody Producto productoDetails) {
        return ResponseEntity.ok(productoService.update(id, productoDetails));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un producto")
    public ResponseEntity<?> deleteProducto(@PathVariable Long id) {
        productoService.delete(id);
        return ResponseEntity.ok().build();
    }
}