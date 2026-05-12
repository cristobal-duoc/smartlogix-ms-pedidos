package com.smartlogix.pedidos.repository;

import com.smartlogix.pedidos.entity.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Patrón Repository: JpaRepository abstrae todas las operaciones de base de datos
// Spring Data JPA genera la implementación automáticamente en tiempo de ejecución
// No es necesario escribir SQL ni implementar los métodos CRUD manualmente
// @Repository: marca esta interfaz como componente de acceso a datos
@Repository
public interface PedidoRepository extends JpaRepository<PedidoEntity, Long> {

    // Spring Data JPA interpreta el nombre del método y genera la consulta SQL:
    // SELECT * FROM pedidos WHERE tipo = ?
    List<PedidoEntity> findByTipo(String tipo);

    // SELECT * FROM pedidos WHERE estado = ?
    List<PedidoEntity> findByEstado(String estado);

    // SELECT * FROM pedidos WHERE cliente_id = ?
    List<PedidoEntity> findByClienteId(Long clienteId);
}
