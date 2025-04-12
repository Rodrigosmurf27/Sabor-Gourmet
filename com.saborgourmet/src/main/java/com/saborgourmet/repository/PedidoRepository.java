package com.saborgourmet.repository;

import com.saborgourmet.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByClienteId(Long clienteId);
    List<Pedido> findByEstado(String estado);

    @Query("SELECT p FROM Pedido p WHERE p.fechaPedido BETWEEN :startDate AND :endDate")
    List<Pedido> findByFechaPedidoBetween(LocalDateTime startDate, LocalDateTime endDate);
}