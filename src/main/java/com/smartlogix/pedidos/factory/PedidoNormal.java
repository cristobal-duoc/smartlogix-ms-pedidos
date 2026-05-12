package com.smartlogix.pedidos.factory;

import java.time.LocalDate;

// Producto concreto #1 del patrón Factory Method
// Un pedido normal se entrega en 5 días hábiles
public class PedidoNormal implements Pedido {

    @Override
    public String getTipo() {
        return "NORMAL";
    }

    // Regla de negocio: pedido normal se entrega en 5 días desde hoy
    @Override
    public LocalDate calcularFechaEntrega() {
        return LocalDate.now().plusDays(5);
    }
}
