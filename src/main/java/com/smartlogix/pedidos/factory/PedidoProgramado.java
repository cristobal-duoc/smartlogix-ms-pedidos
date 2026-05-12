package com.smartlogix.pedidos.factory;

import java.time.LocalDate;

// Producto concreto #3 del patrón Factory Method
// Un pedido programado tiene una fecha de entrega fija definida por el cliente
public class PedidoProgramado implements Pedido {

    // Fecha específica que el cliente eligió al crear el pedido
    private final LocalDate fechaProgramada;

    // Constructor recibe la fecha programada como parámetro
    public PedidoProgramado(LocalDate fechaProgramada) {
        this.fechaProgramada = fechaProgramada;
    }

    @Override
    public String getTipo() {
        return "PROGRAMADO";
    }

    // Retorna la fecha que el cliente programó (no se calcula, es fija)
    @Override
    public LocalDate calcularFechaEntrega() {
        return this.fechaProgramada;
    }
}
