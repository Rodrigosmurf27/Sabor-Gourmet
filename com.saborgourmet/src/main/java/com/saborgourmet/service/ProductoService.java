package com.saborgourmet.service;

import com.saborgourmet.model.Producto;
import com.saborgourmet.repository.ProductoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    public List<Producto> findAvailable() {
        return productoRepository.findByDisponibleTrue();
    }

    public List<Producto> findByCategory(String categoria) {
        return productoRepository.findByCategoria(categoria);
    }

    public Optional<Producto> findById(Long id) {
        return productoRepository.findById(id);
    }

    @Transactional
    public Producto save(Producto producto) {
        if (productoRepository.existsByNombre(producto.getNombre())) {
            throw new IllegalArgumentException("Ya existe un producto con ese nombre");
        }
        return productoRepository.save(producto);
    }

    @Transactional
    public Producto update(Long id, Producto productoDetails) {
        return productoRepository.findById(id)
                .map(producto -> {
                    producto.setNombre(productoDetails.getNombre());
                    producto.setDescripcion(productoDetails.getDescripcion());
                    producto.setPrecio(productoDetails.getPrecio());
                    producto.setCategoria(productoDetails.getCategoria());
                    producto.setStock(productoDetails.getStock());
                    producto.setDisponible(productoDetails.getDisponible());
                    return productoRepository.save(producto);
                })
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con id: " + id));
    }

    @Transactional
    public void delete(Long id) {
        productoRepository.deleteById(id);
    }

    @Transactional
    public void updateStock(Long id, Integer cantidad) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con id: " + id));

        int nuevoStock = producto.getStock() - cantidad;
        if (nuevoStock < 0) {
            throw new IllegalStateException("No hay suficiente stock para el producto: " + producto.getNombre());
        }

        producto.setStock(nuevoStock);
        producto.setDisponible(nuevoStock > 0);
        productoRepository.save(producto);
    }
}