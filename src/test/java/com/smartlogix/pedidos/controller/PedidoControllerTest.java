package com.smartlogix.pedidos.controller;

import com.smartlogix.pedidos.entity.PedidoEntity;
import com.smartlogix.pedidos.service.PedidoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

// Pruebas unitarias de la capa de presentacion (controller).
// Se usa Mockito para simular el service: se prueba SOLO la logica del controller
// (mapeo de peticiones, codigos HTTP y ramas 404) sin levantar el contexto web.
@ExtendWith(MockitoExtension.class)
class PedidoControllerTest {

    @Mock
    private PedidoService pedidoService;

    @InjectMocks
    private PedidoController pedidoController;

    @Test
    void listarTodos_debeRetornarListaDelService() {
        List<PedidoEntity> pedidos = List.of(
                new PedidoEntity("PED-001", "NORMAL", "PENDIENTE", LocalDate.now(), 1L));
        when(pedidoService.listarTodos()).thenReturn(pedidos);

        List<PedidoEntity> resultado = pedidoController.listarTodos();

        assertEquals(1, resultado.size());
        verify(pedidoService).listarTodos();
    }

    @Test
    void buscarPorId_cuandoExiste_debeRetornar200ConElPedido() {
        PedidoEntity pedido = new PedidoEntity("PED-002", "URGENTE", "PENDIENTE", LocalDate.now(), 2L);
        when(pedidoService.buscarPorId(2L)).thenReturn(Optional.of(pedido));

        ResponseEntity<PedidoEntity> resp = pedidoController.buscarPorId(2L);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals("PED-002", resp.getBody().getCodigo());
    }

    @Test
    void buscarPorId_cuandoNoExiste_debeRetornar404() {
        when(pedidoService.buscarPorId(99L)).thenReturn(Optional.empty());

        ResponseEntity<PedidoEntity> resp = pedidoController.buscarPorId(99L);

        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
    }

    @Test
    void crear_pedidoNormal_debeRetornar201() {
        Map<String, String> body = new HashMap<>();
        body.put("codigo", "PED-003");
        body.put("tipo", "NORMAL");
        body.put("clienteId", "3");

        PedidoEntity creado = new PedidoEntity("PED-003", "NORMAL", "PENDIENTE", LocalDate.now(), 3L);
        when(pedidoService.crear(eq("PED-003"), eq("NORMAL"), isNull(), eq(3L))).thenReturn(creado);

        ResponseEntity<PedidoEntity> resp = pedidoController.crear(body);

        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        assertEquals("PED-003", resp.getBody().getCodigo());
    }

    @Test
    void crear_pedidoProgramado_debeParsearFechaYRetornar201() {
        Map<String, String> body = new HashMap<>();
        body.put("codigo", "PED-004");
        body.put("tipo", "PROGRAMADO");
        body.put("clienteId", "4");
        body.put("fechaProgramada", "2026-06-15");

        PedidoEntity creado = new PedidoEntity("PED-004", "PROGRAMADO", "PENDIENTE",
                LocalDate.parse("2026-06-15"), 4L);
        when(pedidoService.crear(eq("PED-004"), eq("PROGRAMADO"),
                eq(LocalDate.parse("2026-06-15")), eq(4L))).thenReturn(creado);

        ResponseEntity<PedidoEntity> resp = pedidoController.crear(body);

        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        assertEquals("PROGRAMADO", resp.getBody().getTipo());
    }

    @Test
    void actualizarEstado_cuandoExiste_debeRetornar200() {
        Map<String, String> body = Map.of("estado", "EN_PROCESO");
        PedidoEntity actualizado = new PedidoEntity("PED-005", "NORMAL", "EN_PROCESO", LocalDate.now(), 5L);
        when(pedidoService.actualizarEstado(5L, "EN_PROCESO")).thenReturn(actualizado);

        ResponseEntity<PedidoEntity> resp = pedidoController.actualizarEstado(5L, body);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals("EN_PROCESO", resp.getBody().getEstado());
    }

    @Test
    void actualizarEstado_cuandoNoExiste_debeRetornar404() {
        Map<String, String> body = Map.of("estado", "ENTREGADO");
        when(pedidoService.actualizarEstado(99L, "ENTREGADO")).thenReturn(null);

        ResponseEntity<PedidoEntity> resp = pedidoController.actualizarEstado(99L, body);

        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
    }

    @Test
    void eliminar_debeRetornar204() {
        ResponseEntity<Void> resp = pedidoController.eliminar(7L);

        assertEquals(HttpStatus.NO_CONTENT, resp.getStatusCode());
        verify(pedidoService).eliminar(7L);
    }
}
