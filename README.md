# ms-pedidos

Microservicio de gestión de pedidos para SmartLogix.

## Responsabilidad

Gestiona el ciclo de vida de los pedidos: creación, seguimiento de estado y eliminación. Soporta tres tipos de pedido con diferentes reglas de entrega.

## Puerto

`8082`

## Tecnologías

- Java 17
- Spring Boot 3.2.5
- Spring Data JPA (Hibernate)
- PostgreSQL

## Patrones de diseño

- **Factory Method**: `PedidoFactory.crear(tipo, fecha)` decide qué clase concreta instanciar (Normal/Urgente/Programado) sin que el servicio conozca los detalles de cada tipo.
- **Repository Pattern**: `PedidoRepository extends JpaRepository` abstrae el acceso a datos.

## Tipos de pedido

| Tipo | Fecha de entrega |
|------|-----------------|
| `NORMAL` | 5 días desde hoy |
| `URGENTE` | 1 día desde hoy |
| `PROGRAMADO` | Fecha específica definida por el cliente |

## Arquitectura en capas

```
Controller → Service → Repository → Entity → Base de datos
                ↓
           PedidoFactory (Factory Method)
```

## Base de datos

```sql
CREATE DATABASE pedidos_db;
```

## Configuración

1. Copiar `application-example.properties` como `application-local.properties`
2. Exportar: `export DB_PASSWORD=tu_password`

## Ejecutar

```bash
mvn spring-boot:run
```

## Endpoints

| Método | URL | Descripción |
|--------|-----|-------------|
| GET | `/api/pedidos` | Lista todos los pedidos |
| GET | `/api/pedidos/{id}` | Busca pedido por ID |
| POST | `/api/pedidos` | Crea un pedido |
| PUT | `/api/pedidos/{id}/estado` | Actualiza el estado |
| DELETE | `/api/pedidos/{id}` | Elimina un pedido |

### Ejemplo de creación de pedido

```json
POST /api/pedidos
{
  "codigo": "ORD-001",
  "tipo": "URGENTE",
  "clienteId": "1"
}
```

## Tests

```bash
mvn test
```

- `PedidoFactoryTest`: prueba el Factory Method (9 casos)
- `PedidoServiceTest`: prueba la lógica del servicio con mocks (8 casos)
