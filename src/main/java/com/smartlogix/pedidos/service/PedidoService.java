package com.smartlogix.pedidos.service;

import com.smartlogix.pedidos.entity.PedidoEntity;
import com.smartlogix.pedidos.factory.Pedido;
import com.smartlogix.pedidos.factory.PedidoFactory;
import com.smartlogix.pedidos.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// Capa de servicio: contiene la lógica de negocio del microservicio
// Usa PedidoFactory para crear el tipo correcto de pedido (Factory Method)
// Usa PedidoRepository para persistir en la base de datos (Repository Pattern)
// @Service: Spring lo registra como componente y permite inyectarlo en el controller
@Service
public class PedidoService {

    // Spring inyecta automáticamente la implementación de PedidoRepository
    private final PedidoRepository pedidoRepository;

    // Inyección por constructor: recomendada porque hace las dependencias explícitas
    // y facilita las pruebas unitarias (se puede pasar un mock sin Spring)
    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    // Retorna todos los pedidos almacenados en la base de datos
    public List<PedidoEntity> listarTodos() {
        return pedidoRepository.findAll();
    }

    // Busca un pedido por su ID. Retorna Optional para manejar el caso de no encontrarlo
    public Optional<PedidoEntity> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    // Crea un nuevo pedido usando el Factory Method
    // El Factory decide qué clase concreta instanciar según el tipo
    // Luego se guarda la entidad en la base de datos via el Repository
    public PedidoEntity crear(String codigo, String tipo, LocalDate fechaProgramada, Long clienteId) {
        // 1. Factory Method: crea el objeto de dominio según el tipo
        Pedido pedido = PedidoFactory.crear(tipo, fechaProgramada);

        // 2. Construye la entidad JPA con los datos del pedido creado por el factory
        PedidoEntity entidad = new PedidoEntity(
                codigo,
                pedido.getTipo(),           // tipo normalizado ("NORMAL", "URGENTE", "PROGRAMADO")
                "PENDIENTE",                // estado inicial siempre es PENDIENTE
                pedido.calcularFechaEntrega(), // fecha calculada según la lógica del tipo
                clienteId
        );

        // 3. Repository Pattern: persiste la entidad en PostgreSQL
        return pedidoRepository.save(entidad);
    }

    // Actualiza el estado de un pedido existente
    // Retorna null si el pedido no existe
    public PedidoEntity actualizarEstado(Long id, String nuevoEstado) {
        Optional<PedidoEntity> encontrado = pedidoRepository.findById(id);
        if (encontrado.isPresent()) {
            PedidoEntity pedido = encontrado.get();
            pedido.setEstado(nuevoEstado);
            return pedidoRepository.save(pedido);
        }
        return null;
    }

    // Elimina un pedido por su ID
    public void eliminar(Long id) {
        pedidoRepository.deleteById(id);
    }
}
