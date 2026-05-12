package com.smartlogix.pedidos.service;

import com.smartlogix.pedidos.entity.PedidoEntity;
import com.smartlogix.pedidos.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class): activa Mockito para esta clase de test
// No levanta el contexto de Spring — las pruebas son unitarias y rápidas
@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    // @Mock: crea un doble de PedidoRepository que no toca la base de datos real
    @Mock
    private PedidoRepository pedidoRepository;

    // @InjectMocks: crea PedidoService e inyecta el mock de PedidoRepository
    @InjectMocks
    private PedidoService pedidoService;

    // Pedido de prueba reutilizado en varios tests
    private PedidoEntity pedidoEjemplo;

    @BeforeEach
    void setUp() {
        // Se ejecuta antes de cada test para preparar datos de prueba
        pedidoEjemplo = new PedidoEntity(
                "ORD-001",
                "NORMAL",
                "PENDIENTE",
                LocalDate.now().plusDays(5),
                1L
        );
        pedidoEjemplo.setId(1L);
    }

    @Test
    void listarTodos_debeRetornarListaDePedidos() {
        // Arrange: el mock retorna una lista con un pedido cuando se llame findAll()
        when(pedidoRepository.findAll()).thenReturn(Arrays.asList(pedidoEjemplo));

        // Act: llamar al servicio
        List<PedidoEntity> resultado = pedidoService.listarTodos();

        // Assert: la lista tiene exactamente 1 elemento
        assertEquals(1, resultado.size());
        assertEquals("ORD-001", resultado.get(0).getCodigo());

        // Verify: confirmar que el repository fue llamado exactamente 1 vez
        verify(pedidoRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_cuandoExiste_debeRetornarPedido() {
        // Arrange: el mock retorna el pedido de ejemplo cuando se busca por id=1
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedidoEjemplo));

        // Act
        Optional<PedidoEntity> resultado = pedidoService.buscarPorId(1L);

        // Assert: el Optional tiene valor y el código es correcto
        assertTrue(resultado.isPresent());
        assertEquals("ORD-001", resultado.get().getCodigo());
    }

    @Test
    void buscarPorId_cuandoNoExiste_debeRetornarOptionalVacio() {
        // Arrange: el mock retorna Optional.empty() para id=99
        when(pedidoRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<PedidoEntity> resultado = pedidoService.buscarPorId(99L);

        // Assert: el Optional está vacío
        assertFalse(resultado.isPresent());
    }

    @Test
    void crear_conTipoNormal_debeGuardarConEstadoPendiente() {
        // Arrange: cuando se llame save() con cualquier PedidoEntity, retornar pedidoEjemplo
        when(pedidoRepository.save(any(PedidoEntity.class))).thenReturn(pedidoEjemplo);

        // Act: crear un pedido NORMAL usando el servicio
        PedidoEntity resultado = pedidoService.crear("ORD-001", "NORMAL", null, 1L);

        // Assert: el pedido guardado tiene los datos correctos
        assertNotNull(resultado);
        assertEquals("PENDIENTE", resultado.getEstado());
        assertEquals("ORD-001", resultado.getCodigo());

        // Verify: el repository fue llamado para guardar
        verify(pedidoRepository, times(1)).save(any(PedidoEntity.class));
    }

    @Test
    void crear_conTipoUrgente_debeGuardarConTipoUrgente() {
        // Arrange: preparar un pedido urgente para que el mock lo retorne
        PedidoEntity pedidoUrgente = new PedidoEntity(
                "ORD-002", "URGENTE", "PENDIENTE",
                LocalDate.now().plusDays(1), 2L
        );
        when(pedidoRepository.save(any(PedidoEntity.class))).thenReturn(pedidoUrgente);

        // Act
        PedidoEntity resultado = pedidoService.crear("ORD-002", "URGENTE", null, 2L);

        // Assert: tipo es URGENTE
        assertEquals("URGENTE", resultado.getTipo());
    }

    @Test
    void actualizarEstado_cuandoExiste_debeActualizarEstado() {
        // Arrange
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedidoEjemplo));
        when(pedidoRepository.save(any(PedidoEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act: cambiar estado a EN_PROCESO
        PedidoEntity actualizado = pedidoService.actualizarEstado(1L, "EN_PROCESO");

        // Assert: el estado fue actualizado
        assertNotNull(actualizado);
        assertEquals("EN_PROCESO", actualizado.getEstado());
    }

    @Test
    void actualizarEstado_cuandoNoExiste_debeRetornarNull() {
        // Arrange: el pedido no existe
        when(pedidoRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        PedidoEntity resultado = pedidoService.actualizarEstado(99L, "EN_PROCESO");

        // Assert: retorna null porque el pedido no existe
        assertNull(resultado);

        // Verify: no se llamó a save porque no había pedido
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void eliminar_debeInvocarDeleteById() {
        // Arrange: simular que deleteById no hace nada (void method)
        doNothing().when(pedidoRepository).deleteById(1L);

        // Act
        pedidoService.eliminar(1L);

        // Verify: deleteById fue llamado con el id correcto
        verify(pedidoRepository, times(1)).deleteById(1L);
    }
}
