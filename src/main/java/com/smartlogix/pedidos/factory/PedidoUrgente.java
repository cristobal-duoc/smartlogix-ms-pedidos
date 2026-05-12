package com.smartlogix.pedidos.factory;

import java.time.LocalDate;

// Producto concreto #2 del patrón Factory Method
// Un pedido urgente se entrega en 1 día (servicio express)
public class PedidoUrgente implements Pedido {

    @Override
    public String getTipo() {
        return "URGENTE";
    }

    // Regla de negocio: pedido urgente se entrega al día siguiente
    @Override
    public LocalDate calcularFechaEntrega() {
        return LocalDate.now().plusDays(1);
    }
}
