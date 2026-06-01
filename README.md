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

## Pruebas y cobertura

Pruebas unitarias con **JUnit 5 + Mockito**. Usan **H2 en memoria** (perfil de test).

```bash
mvn test      # ejecuta las pruebas
mvn verify    # pruebas + reporte de cobertura + validacion del minimo (>=60%)
```

- `PedidoFactoryTest`: prueba el Factory Method (9 casos).
- `PedidoServiceTest`: lógica del servicio con mocks del repositorio (8 casos).
- `PedidoControllerTest`: capa REST y códigos HTTP / 404 (8 casos).
- `OpenApiExportTest`: genera la especificación Swagger en `api-docs/openapi.json`.

Reporte de cobertura (JaCoCo): `target/site/jacoco/index.html`. La regla `jacoco:check`
falla el build si la cobertura baja del 60%. Cobertura actual: **87.7%**.

## API REST (Swagger)

Con el servicio corriendo: UI en `/swagger-ui.html` y especificación JSON en
`/v3/api-docs` (copia versionada en `api-docs/openapi.json`).
