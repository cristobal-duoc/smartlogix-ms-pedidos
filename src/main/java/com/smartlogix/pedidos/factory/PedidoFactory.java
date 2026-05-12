package com.smartlogix.pedidos.factory;

import java.time.LocalDate;

// Fábrica del patrón Factory Method
// Centraliza la lógica de creación de pedidos
// El cliente (PedidoService) llama a PedidoFactory.crear() sin saber qué clase concreta se instancia
public class PedidoFactory {

    // Método estático de fábrica: decide qué tipo de Pedido instanciar según el tipo recibido
    // @param tipo     String que indica el tipo: "NORMAL", "URGENTE" o "PROGRAMADO"
    // @param fecha    Fecha programada (solo se usa cuando tipo es "PROGRAMADO")
    // @return         Instancia concreta de Pedido según el tipo
    public static Pedido crear(String tipo, LocalDate fecha) {
        switch (tipo.toUpperCase()) {
            case "URGENTE":
                return new PedidoUrgente();
            case "PROGRAMADO":
                // Para pedidos programados, la fecha es obligatoria
                if (fecha == null) {
                    throw new IllegalArgumentException("Un pedido PROGRAMADO requiere una fecha de entrega");
                }
                return new PedidoProgramado(fecha);
            case "NORMAL":
            default:
                // Si el tipo no es reconocido, se crea un pedido normal por defecto
                return new PedidoNormal();
        }
    }
}
