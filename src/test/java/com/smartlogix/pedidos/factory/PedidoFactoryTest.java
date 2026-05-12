package com.smartlogix.pedidos.factory;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

// Pruebas unitarias del Factory Method
// No requieren Spring ni base de datos — prueban solo la lógica de creación
class PedidoFactoryTest {

    @Test
    void cuandoTipoNormal_debeRetornarPedidoNormal() {
        // Arrange & Act: crear un pedido de tipo NORMAL
        Pedido pedido = PedidoFactory.crear("NORMAL", null);

        // Assert: debe ser de tipo NORMAL
        assertEquals("NORMAL", pedido.getTipo());
    }

    @Test
    void cuandoTipoNormal_fechaEntregaDebeSerEn5Dias() {
        Pedido pedido = PedidoFactory.crear("NORMAL", null);

        // La fecha de entrega debe ser 5 días desde hoy
        LocalDate esperado = LocalDate.now().plusDays(5);
        assertEquals(esperado, pedido.calcularFechaEntrega());
    }

    @Test
    void cuandoTipoUrgente_debeRetornarPedidoUrgente() {
        Pedido pedido = PedidoFactory.crear("URGENTE", null);

        assertEquals("URGENTE", pedido.getTipo());
    }

    @Test
    void cuandoTipoUrgente_fechaEntregaDebeSerManana() {
        Pedido pedido = PedidoFactory.crear("URGENTE", null);

        // La fecha de entrega debe ser mañana
        LocalDate esperado = LocalDate.now().plusDays(1);
        assertEquals(esperado, pedido.calcularFechaEntrega());
    }

    @Test
    void cuandoTipoProgramado_debeRetornarPedidoProgramado() {
        LocalDate fechaEspecifica = LocalDate.of(2025, 12, 31);
        Pedido pedido = PedidoFactory.crear("PROGRAMADO", fechaEspecifica);

        assertEquals("PROGRAMADO", pedido.getTipo());
    }

    @Test
    void cuandoTipoProgramado_fechaEntregaDebeSerLaFechaIndicada() {
        LocalDate fechaEspecifica = LocalDate.of(2025, 12, 31);
        Pedido pedido = PedidoFactory.crear("PROGRAMADO", fechaEspecifica);

        // La fecha de entrega debe ser exactamente la fecha programada
        assertEquals(fechaEspecifica, pedido.calcularFechaEntrega());
    }

    @Test
    void cuandoTipoProgramadoSinFecha_debeLanzarExcepcion() {
        // Si se pide tipo PROGRAMADO sin fecha, debe lanzar IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            PedidoFactory.crear("PROGRAMADO", null);
        });
    }

    @Test
    void cuandoTipoDesconocido_debeCrearPedidoNormalPorDefecto() {
        // Tipo no reconocido → crea NORMAL por defecto (caso default del switch)
        Pedido pedido = PedidoFactory.crear("DESCONOCIDO", null);

        assertEquals("NORMAL", pedido.getTipo());
    }

    @Test
    void cuandoTipoEnMinusculas_debeFuncionar() {
        // El factory usa toUpperCase() internamente, así que "urgente" == "URGENTE"
        Pedido pedido = PedidoFactory.crear("urgente", null);

        assertEquals("URGENTE", pedido.getTipo());
    }
}
