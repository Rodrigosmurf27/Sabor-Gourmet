package com.saborgourmet.service;

import com.saborgourmet.model.*;
import com.saborgourmet.repository.PedidoRepository;
import com.saborgourmet.repository.ProductoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ProductoService productoService;

    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> findById(Long id) {
        return pedidoRepository.findById(id);
    }

    public List<Pedido> findByClienteId(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }

    public List<Pedido> findByEstado(String estado) {
        return pedidoRepository.findByEstado(estado);
    }

    public List<Pedido> findByFechaBetween(LocalDateTime start, LocalDateTime end) {
        return pedidoRepository.findByFechaPedidoBetween(start, end);
    }

    @Transactional
    public Pedido createPedido(Pedido pedido) {
        // Validar cliente
        Cliente cliente = clienteService.findById(pedido.getCliente().getId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

        if (!cliente.getActivo()) {
            throw new IllegalStateException("El cliente no está activo");
        }

        // Validar y procesar productos
        List<PedidoProducto> productos = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (PedidoProducto pp : pedido.getProductos()) {
            Producto producto = productoService.findById(pp.getProducto().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + pp.getProducto().getId()));

            if (!producto.getDisponible() || producto.getStock() < pp.getCantidad()) {
                throw new IllegalStateException("Producto no disponible o sin stock suficiente: " + producto.getNombre());
            }

            // Actualizar stock
            productoService.updateStock(producto.getId(), pp.getCantidad());

            // Calcular subtotal
            BigDecimal subtotal = producto.getPrecio().multiply(BigDecimal.valueOf(pp.getCantidad()));

            // Crear relación pedido-producto
            PedidoProducto pedidoProducto = new PedidoProducto();
            pedidoProducto.setPedido(pedido);
            pedidoProducto.setProducto(producto);
            pedidoProducto.setCantidad(pp.getCantidad());
            pedidoProducto.setPrecioUnitario(producto.getPrecio());
            pedidoProducto.setSubtotal(subtotal);

            productos.add(pedidoProducto);
            total = total.add(subtotal);
        }

        // Configurar pedido
        pedido.setCliente(cliente);
        pedido.setProductos(productos);
        pedido.setTotal(total);
        pedido.setEstado("PENDIENTE");
        pedido.setFechaPedido(LocalDateTime.now());

        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido updateEstado(Long id, String nuevoEstado) {
        return pedidoRepository.findById(id)
                .map(pedido -> {
                    if (!List.of("PENDIENTE", "EN PREPARACIÓN", "ENTREGADO", "CANCELADO").contains(nuevoEstado)) {
                        throw new IllegalArgumentException("Estado no válido");
                    }
                    pedido.setEstado(nuevoEstado);
                    return pedidoRepository.save(pedido);
                })
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado con id: " + id));
    }

    @Transactional
    public void cancelarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado con id: " + id));

        if ("CANCELADO".equals(pedido.getEstado())) {
            return;
        }

        // Devolver stock si el pedido no estaba cancelado
        if (!"CANCELADO".equals(pedido.getEstado())) {
            for (PedidoProducto pp : pedido.getProductos()) {
                Producto producto = pp.getProducto();
                producto.setStock(producto.getStock() + pp.getCantidad());
                producto.setDisponible(true);
                productoRepository.save(producto);
            }
        }

        pedido.setEstado("CANCELADO");
        pedidoRepository.save(pedido);
    }
}