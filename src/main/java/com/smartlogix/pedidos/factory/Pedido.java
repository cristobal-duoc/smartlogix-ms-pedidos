package com.smartlogix.pedidos.factory;

import java.time.LocalDate;

// Interfaz del producto en el patrón Factory Method
// Define el contrato que todos los tipos de pedido deben cumplir
// PedidoNormal, PedidoUrgente y PedidoProgramado implementan esta interfaz
public interface Pedido {

    // Retorna el tipo de pedido como String: "NORMAL", "URGENTE" o "PROGRAMADO"
    String getTipo();

    // Calcula y retorna la fecha de entrega según las reglas de negocio del tipo
    LocalDate calcularFechaEntrega();
}
