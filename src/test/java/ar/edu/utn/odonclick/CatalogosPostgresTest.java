package ar.edu.utn.odonclick;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;

import static ar.edu.utn.odonclick.DemoPostgres.conBase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("postgres")
class CatalogosPostgresTest {

    private final HttpClient cliente = HttpClient.newHttpClient();
    private final ObjectMapper json = new ObjectMapper();

    @Test
    void s01ContextoFijoYCatalogosPorSede() throws Exception {
        conBase(base -> {
            try (var app = base.iniciar(1)) {
                var contexto = obtener(app, "/api/contexto?sedeId=2", 200);
                assertEquals(1, contexto.at("/sede/id").asLong());
                assertEquals("SEDE 1", contexto.at("/sede/nombre").asText());
                assertEquals(1, contexto.at("/recepcion/id").asLong());
                assertEquals("America/Argentina/Buenos_Aires", contexto.get("zonaHoraria").asText());
                var catalogos = obtener(app, "/api/catalogos?inicioLocal=2026-12-01T10:00&sedeId=2", 200);
                assertEquals(1, catalogos.get("odontologos").size());
                assertEquals(11, catalogos.at("/odontologos/0/id").asLong());
                assertEquals("Lucía Ferrer", catalogos.at("/odontologos/0/nombre").asText());
                verificarSillones(catalogos, 11, 12);
                assertEquals(5, catalogos.get("pacientes").size());
                assertEquals("Radiografía", catalogos.at("/prestaciones/1/nombre").asText());
                assertTrue(catalogos.at("/prestaciones/1/usaSillon").isBoolean());
                assertFalse(catalogos.at("/prestaciones/1/usaSillon").asBoolean());
                assertEquals(15, catalogos.at("/prestaciones/1/duracionMinutos").asInt());
            }
            try (var app = base.iniciar(2)) {
                assertEquals(2, obtener(app, "/api/contexto", 200).at("/sede/id").asLong());
                var catalogos = obtener(app, "/api/catalogos?inicioLocal=2026-12-01T10:00", 200);
                assertEquals(1, catalogos.get("odontologos").size());
                assertEquals(21, catalogos.at("/odontologos/0/id").asLong());
                verificarSillones(catalogos, 21, 22);
            }
        });
    }

    @Test
    void s22YS23FiltranPorFechaHorarioYCierre() throws Exception {
        conBase(base -> {
            try (var app = base.iniciar(1)) {
                verificarOdontologo(app, "2026-12-01T10:00", "Lucía Ferrer");
                verificarOdontologo(app, "2026-12-01T17:00", "Julián Soria");
                verificarOdontologo(app, "2026-12-01T08:30", "Lucía Ferrer");
                verificarOdontologo(app, "2026-12-01T16:00", "Julián Soria");
                for (var inicio : new String[]{"2026-12-01T13:00", "2026-12-01T15:30", "2026-12-08T10:00",
                        "2026-12-05T10:00", "2026-11-24T10:00", "2026-12-28T10:00"}) {
                    assertTrue(obtener(app, "/api/catalogos?inicioLocal=" + inicio, 200).get("odontologos").isEmpty(), inicio);
                }
                assertTrue(obtener(app, "/api/catalogos", 200).get("odontologos").isEmpty());
                assertEquals("DATOS_INVALIDOS",
                        obtener(app, "/api/catalogos?inicioLocal=2026-12-01T10:00Z", 400).get("codigo").asText());
            }
        });
    }

    private void verificarOdontologo(ServletWebServerApplicationContext app, String inicio, String nombre)
            throws Exception {
        var dato = obtener(app, "/api/catalogos?inicioLocal=" + inicio, 200).get("odontologos");
        assertEquals(1, dato.size());
        assertEquals(nombre, dato.get(0).get("nombre").asText());
    }

    private static void verificarSillones(JsonNode catalogos, long primero, long segundo) {
        assertEquals(2, catalogos.get("sillones").size());
        assertEquals(primero, catalogos.at("/sillones/0/id").asLong());
        assertEquals(segundo, catalogos.at("/sillones/1/id").asLong());
    }

    private JsonNode obtener(ServletWebServerApplicationContext app, String ruta, int codigo) throws Exception {
        var url = URI.create("http://127.0.0.1:" + app.getWebServer().getPort() + ruta);
        var respuesta = cliente.send(HttpRequest.newBuilder(url).GET().build(), HttpResponse.BodyHandlers.ofString());
        assertEquals(codigo, respuesta.statusCode(), respuesta.body());
        return json.readTree(respuesta.body());
    }

}
