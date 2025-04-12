package com.saborgourmet.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Pedido")
@Data
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @CreationTimestamp
    @Column(name = "fecha_pedido", nullable = false, updatable = false)
    private LocalDateTime fechaPedido;

    @NotBlank(message = "El estado del pedido es obligatorio")
    @Pattern(regexp = "PENDIENTE|EN PREPARACIÓN|ENTREGADO|CANCELADO",
            message = "Estado del pedido no válido. Valores permitidos: PENDIENTE, EN PREPARACIÓN, ENTREGADO, CANCELADO")
    private String estado;

    @NotBlank(message = "El método de pago es obligatorio")
    @Pattern(regexp = "EFECTIVO|TARJETA|TRANSFERENCIA",
            message = "Método de pago no válido. Valores permitidos: EFECTIVO, TARJETA, TRANSFERENCIA")
    private String metodoPago;

    @NotNull(message = "El total es obligatorio")
    @DecimalMin(value = "0.00", message = "El total no puede ser negativo")
    private BigDecimal total;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoProducto> productos;
}