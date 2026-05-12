package com.smartlogix.pedidos.controller;

import com.smartlogix.pedidos.entity.PedidoEntity;
import com.smartlogix.pedidos.service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// Capa de presentación: expone los endpoints HTTP del microservicio de pedidos
// @RestController: combina @Controller + @ResponseBody (todas las respuestas son JSON)
// @RequestMapping: todos los endpoints de esta clase empiezan con /api/pedidos
@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    // El service contiene la lógica de negocio — el controller solo delega
    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    // GET /api/pedidos → lista todos los pedidos
    @GetMapping
    public List<PedidoEntity> listarTodos() {
        return pedidoService.listarTodos();
    }

    // GET /api/pedidos/{id} → busca un pedido por ID
    // Retorna 404 si no existe
    @GetMapping("/{id}")
    public ResponseEntity<PedidoEntity> buscarPorId(@PathVariable Long id) {
        Optional<PedidoEntity> pedido = pedidoService.buscarPorId(id);
        return pedido.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/pedidos → crea un nuevo pedido
    // El body JSON debe tener: codigo, tipo, clienteId y opcionalmente fechaProgramada
    // Retorna 201 Created con el pedido guardado
    @PostMapping
    public ResponseEntity<PedidoEntity> crear(@RequestBody Map<String, String> body) {
        String codigo = body.get("codigo");
        String tipo = body.get("tipo");
        Long clienteId = Long.parseLong(body.get("clienteId"));

        // fechaProgramada solo se usa para pedidos de tipo PROGRAMADO
        LocalDate fechaProgramada = null;
        if (body.containsKey("fechaProgramada")) {
            fechaProgramada = LocalDate.parse(body.get("fechaProgramada"));
        }

        PedidoEntity creado = pedidoService.crear(codigo, tipo, fechaProgramada, clienteId);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // PUT /api/pedidos/{id}/estado → actualiza el estado de un pedido
    // Body JSON: { "estado": "EN_PROCESO" }
    @PutMapping("/{id}/estado")
    public ResponseEntity<PedidoEntity> actualizarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String nuevoEstado = body.get("estado");
        PedidoEntity actualizado = pedidoService.actualizarEstado(id, nuevoEstado);
        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizado);
    }

    // DELETE /api/pedidos/{id} → elimina un pedido
    // Retorna 204 No Content (operación exitosa sin cuerpo de respuesta)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pedidoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
