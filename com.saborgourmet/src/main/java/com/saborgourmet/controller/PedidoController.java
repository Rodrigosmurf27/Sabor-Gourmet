package com.saborgourmet.controller;

import com.saborgourmet.model.Pedido;
import com.saborgourmet.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@Tag(name = "Pedidos", description = "Operaciones relacionadas con los pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    @Operation(summary = "Obtener todos los pedidos")
    public List<Pedido> getAllPedidos() {
        return pedidoService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un pedido por ID")
    public ResponseEntity<Pedido> getPedidoById(@PathVariable Long id) {
        return pedidoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Obtener pedidos por cliente")
    public List<Pedido> getPedidosByCliente(@PathVariable Long clienteId) {
        return pedidoService.findByClienteId(clienteId);
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Obtener pedidos por estado")
    public List<Pedido> getPedidosByEstado(@PathVariable String estado) {
        return pedidoService.findByEstado(estado);
    }

    @GetMapping("/fecha")
    @Operation(summary = "Obtener pedidos por rango de fechas")
    public List<Pedido> getPedidosByFecha(
            @RequestParam LocalDateTime inicio,
            @RequestParam LocalDateTime fin) {
        return pedidoService.findByFechaBetween(inicio, fin);
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo pedido")
    public Pedido createPedido(@RequestBody Pedido pedido) {
        return pedidoService.createPedido(pedido);
    }

    @PutMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado de un pedido")
    public ResponseEntity<Pedido> updateEstado(
            @PathVariable Long id,
            @RequestParam String nuevoEstado) {
        return ResponseEntity.ok(pedidoService.updateEstado(id, nuevoEstado));
    }

    @PutMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar un pedido")
    public ResponseEntity<Pedido> cancelarPedido(@PathVariable Long id) {
        pedidoService.cancelarPedido(id);
        return ResponseEntity.ok().build();
    }
}