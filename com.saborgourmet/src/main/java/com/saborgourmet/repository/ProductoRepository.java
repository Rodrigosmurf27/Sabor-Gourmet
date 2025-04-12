package com.saborgourmet.repository;

import com.saborgourmet.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByDisponibleTrue();
    List<Producto> findByCategoria(String categoria);
    boolean existsByNombre(String nombre);
}