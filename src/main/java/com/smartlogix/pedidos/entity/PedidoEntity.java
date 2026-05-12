package com.smartlogix.pedidos.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

// @Entity: esta clase se mapea a una tabla en la base de datos (pedidos_db)
// @Table: nombre explícito de la tabla en PostgreSQL
@Entity
@Table(name = "pedidos")
public class PedidoEntity {

    // @Id: clave primaria de la tabla
    // @GeneratedValue: PostgreSQL genera el ID automáticamente (autoincremental)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Código único del pedido (ej: "ORD-2024-001")
    @Column(nullable = false)
    private String codigo;

    // Tipo del pedido: NORMAL, URGENTE o PROGRAMADO
    // Corresponde a los tipos definidos en el Factory Method
    @Column(nullable = false)
    private String tipo;

    // Estado del pedido: PENDIENTE, EN_PROCESO, ENTREGADO, CANCELADO
    @Column(nullable = false)
    private String estado;

    // Fecha en que se debe entregar el pedido
    private LocalDate fechaEntrega;

    // ID del cliente que realizó el pedido (referencia externa, no FK)
    private Long clienteId;

    // Constructor vacío requerido por JPA/Hibernate para instanciar entidades
    public PedidoEntity() {}

    // Constructor completo para crear pedidos desde el factory
    public PedidoEntity(String codigo, String tipo, String estado,
                        LocalDate fechaEntrega, Long clienteId) {
        this.codigo = codigo;
        this.tipo = tipo;
        this.estado = estado;
        this.fechaEntrega = fechaEntrega;
        this.clienteId = clienteId;
    }

    // --- Getters y Setters ---
    // JPA necesita acceso a los campos para leer/escribir en la base de datos

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDate getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(LocalDate fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
}
