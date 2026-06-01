package com.smartlogix.pedidos;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

// Genera y exporta la especificacion OpenAPI (Swagger) de ms-pedidos.
// Levanta el contexto con H2, consulta /v3/api-docs (springdoc) y guarda el JSON
// en api-docs/openapi.json como evidencia del entregable.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OpenApiExportTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void exportarEspecificacionOpenApi() throws Exception {
        String json = restTemplate.getForObject(
                "http://localhost:" + port + "/v3/api-docs", String.class);

        assertNotNull(json);
        assertTrue(json.contains("/api/pedidos"),
                "La especificacion debe incluir los endpoints del controlador");

        Path dir = Path.of("api-docs");
        Files.createDirectories(dir);
        Files.writeString(dir.resolve("openapi.json"), json);
    }
}
