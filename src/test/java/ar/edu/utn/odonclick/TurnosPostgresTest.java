package ar.edu.utn.odonclick;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;

import static ar.edu.utn.odonclick.DemoPostgres.conBase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("postgres")
class TurnosPostgresTest {

    private final HttpClient http = HttpClient.newHttpClient();
    private final ObjectMapper json = new ObjectMapper();

    @Test
    void s04aS06YS26aS27RechazanSinGuardar() throws Exception {
        conBase(base -> {
            try (var app = base.iniciar(1)) {
                var valido = turno("2026-12-01T10:00", 11, 1, "11");
                for (var invalido : List.of("{", "[]", valido.replace("\"pacienteId\":1,", ""),
                        valido.replace("\"pacienteId\":1", "\"pacienteId\":\"1\""),
                        valido.replace("T10:00", "T10:00Z"), valido.replace("T10:00", "T25:00"),
                        valido.replace("{", "{\"sedeId\":2,"))) {
                    rechazo(app, base, invalido, 400, "DATOS_INVALIDOS", 0);
                }
                for (var id : List.of("pacienteId", "odontologoId", "prestacionId", "sillonId")) {
                    rechazo(app, base, valido.replaceFirst("\"" + id + "\":\\d+", "\"" + id + "\":999"),
                            404, "IDENTIFICADOR_INEXISTENTE", 0);
                }
                rechazo(app, base, turno("2026-12-01T10:00", 21, 1, "11"), 403, "RECURSO_FUERA_DE_SEDE", 0);
                rechazo(app, base, turno("2026-12-01T10:00", 11, 1, "21"), 403, "RECURSO_FUERA_DE_SEDE", 0);
                rechazo(app, base, turno("2026-12-01T10:00", 11, 1, null), 422, "SILLON_REQUERIDO", 0);
                rechazo(app, base, turno("2026-12-01T10:00", 11, 1, "null"), 422, "SILLON_REQUERIDO", 0);
                rechazo(app, base, turno("2026-12-01T10:00", 11, 2, "11"), 422, "SILLON_NO_APLICA", 0);
            }
        });
    }

    @Test
    void s07aS10CierresYBordesDeLasFranjas() throws Exception {
        conBase(base -> {
            try (var app = base.iniciar(1)) {
                for (var fecha : List.of("2026-12-05", "2026-12-06", "2026-12-08")) {
                    rechazo(app, base, turno(fecha + "T10:00", 11, 1, "11"), 422, "SEDE_CERRADA", 0);
                }
                rechazo(app, base, turno("2026-12-01T16:00", 11, 1, "11"), 422, "ODONTOLOGO_FUERA_DE_FRANJA", 0);
                for (var inicio : List.of("12:25", "12:55", "15:30", "13:00")) {
                    rechazo(app, base, turno("2026-12-01T" + inicio, 11, 1, "11"), 422,
                            "CITA_O_PAUSA_FUERA_DE_FRANJA", 0);
                }
                rechazo(app, base, turno("2026-12-01T20:25", 12, 1, "11"), 422, "CITA_O_PAUSA_FUERA_DE_FRANJA", 0);
                assertFecha("2026-12-01T13:00", crear(app, turno("2026-12-01T12:20", 11, 1, "11")), "finPausaLocal");
                assertFecha("2026-12-01T21:00", crear(app, turno("2026-12-01T20:20", 12, 1, "11")), "finPausaLocal");
                assertEquals(2, turnosGuardados(base));
            }
        });
    }

    @Test
    void s19YS20PlazoIncluyeHoyYLimiteEnBuenosAires() throws Exception {
        conBase(base -> {
            // En UTC ya es 26/11, pero en Buenos Aires todavía es 25/11.
            try (var app = base.iniciar(1, "2026-11-26T02:00:00Z")) {
                for (var fecha : List.of("2026-11-24", "2026-12-26", "4000-01-01")) {
                    var error = rechazo(app, base, turno(fecha + "T10:00", 11, 1, "11"), 422, "FECHA_FUERA_DE_PLAZO", 0);
                    assertTrue(error.get("mensaje").asText().contains("25/11/2026 y el 25/12/2026"));
                }
                crear(app, turno("2026-11-25T10:00", 11, 1, "11"));
                crear(app, turno("2026-12-25T10:00", 11, 1, "11"));
                assertEquals(2, turnosGuardados(base));
            }
        });
    }

    @Test
    void s21FinDeMesIncluyeEl29DeFebreroYRechazaMarzo() throws Exception {
        conBase(base -> {
            try (var app = base.iniciar(1, "2028-01-31T15:00:00Z")) {
                rechazo(app, base, turno("2028-03-01T10:00", 11, 1, "11"), 422, "FECHA_FUERA_DE_PLAZO", 0);
                crear(app, turno("2028-02-29T10:00", 11, 1, "11"));
                assertEquals(1, turnosGuardados(base));
            }
        });
    }

    @Test
    void s02YS03GuardadoConSillonYRadiografiaSinSillon() throws Exception {
        conBase(base -> {
            try (var app = base.iniciar(1)) {
                var creado = crear(app, turno("2026-12-01T10:00", 11, 1, "11"));
                assertTrue(creado.get("id").asLong() > 0);
                assertEquals(1, creado.get("sedeId").asLong());
                assertEquals(11, creado.get("sillonId").asLong());
                assertFecha("2026-12-01T10:00", creado, "inicioLocal");
                assertFecha("2026-12-01T10:30", creado, "finLocal");
                assertFecha("2026-12-01T10:40", creado, "finPausaLocal");
                assertEquals(1, turnosGuardados(base));

                var radiografia = crear(app, turno("2026-12-01T10:40", 11, 2, null));
                assertTrue(radiografia.get("sillonId").isNull());
                assertFecha("2026-12-01T11:05", radiografia, "finPausaLocal");
                rechazo(app, base, turno("2026-12-01T11:00", 11, 1, "12"), 409, "ODONTOLOGO_OCUPADO", 2);
                dosProfesionalesDeManana(base);
                crear(app, turno("2026-12-01T10:45", 12, 1, "11"));
                assertEquals(3, turnosGuardados(base));
                assertEquals(1, contar(base, "SELECT count(*) FROM turno WHERE sillon_id IS NULL"));
            }
        });
    }

    @Test
    void s11aS14ConflictosSeparadosPrioridadYBordeDePausa() throws Exception {
        conBase(base -> {
            try (var app = base.iniciar(1)) {
                dosProfesionalesDeManana(base);
                crear(app, turno("2026-12-01T10:00", 11, 1, "11"));
                var ocupado = rechazo(app, base, turno("2026-12-01T10:20", 11, 1, "12"), 409, "ODONTOLOGO_OCUPADO", 1);
                assertEquals("ODONTOLOGO", ocupado.get("recurso").asText());
                assertEquals(Set.of("codigo", "mensaje", "recurso", "inicioOcupadoLocal", "finOcupadoLocal"), campos(ocupado));
                assertFecha("2026-12-01T10:00", ocupado, "inicioOcupadoLocal");
                assertFecha("2026-12-01T10:40", ocupado, "finOcupadoLocal");
                var sillon = rechazo(app, base, turno("2026-12-01T10:20", 12, 1, "11"), 409, "SILLON_OCUPADO", 1);
                assertEquals("SILLON", sillon.get("recurso").asText());
                crear(app, turno("2026-12-01T10:00", 12, 1, "12"));
                for (var hora : List.of("10:20", "10:30", "10:39")) {
                    rechazo(app, base, turno("2026-12-01T" + hora, 11, 1, "12"), 409, "ODONTOLOGO_OCUPADO", 2);
                }
                crear(app, turno("2026-12-01T10:40", 11, 1, "12"));
                assertEquals(3, turnosGuardados(base));
            }
        });
    }

    @Test
    void s15SimultaneosMismoOdontologo() throws Exception {
        concurrentes(false);
    }

    @Test
    void s16SimultaneosMismoSillon() throws Exception {
        concurrentes(true);
    }

    private void concurrentes(boolean compartenSillon) throws Exception {
        conBase(base -> {
            try (var app = base.iniciar(1); var candado = base.conexion(); var monitoreo = base.conexion()) {
                if (compartenSillon) {
                    dosProfesionalesDeManana(base);
                }
                var tabla = compartenSillon ? "sillon" : "odontologo";
                candado.setAutoCommit(false);
                try (var sql = candado.createStatement()) {
                    sql.executeQuery("SELECT id FROM " + tabla + " WHERE id = 11 FOR UPDATE").close();
                }
                var primera = http.sendAsync(solicitud(app, turno("2026-12-01T10:00", 11, 1, "11")),
                        HttpResponse.BodyHandlers.ofString());
                var segunda = http.sendAsync(solicitud(app,
                                turno("2026-12-01T10:20", compartenSillon ? 12 : 11, 1, compartenSillon ? "11" : "12")),
                        HttpResponse.BodyHandlers.ofString());
                try (var consulta = monitoreo.prepareStatement("""
                        SELECT count(*) FROM pg_stat_activity
                        WHERE application_name = ? AND wait_event_type = 'Lock' AND query LIKE ?
                        """)) {
                    consulta.setString(1, base.nombre());
                    consulta.setString(2, "SELECT id FROM " + tabla + " % FOR UPDATE");
                    long limite = System.nanoTime() + TimeUnit.SECONDS.toNanos(8);
                    int esperando = 0;
                    while (esperando < 2 && System.nanoTime() < limite) {
                        try (var fila = consulta.executeQuery()) {
                            fila.next();
                            esperando = fila.getInt(1);
                        }
                        if (esperando < 2) {
                            Thread.sleep(20);
                        }
                    }
                    assertEquals(2, esperando, "Las dos solicitudes deben esperar en conexiones distintas");
                } finally {
                    candado.rollback();
                }
                var a = primera.get(10, TimeUnit.SECONDS);
                var b = segunda.get(10, TimeUnit.SECONDS);
                assertEquals(List.of(201, 409), List.of(a.statusCode(), b.statusCode()).stream().sorted().toList());
                var conflicto = json.readTree(a.statusCode() == 409 ? a.body() : b.body());
                assertEquals(compartenSillon ? "SILLON_OCUPADO" : "ODONTOLOGO_OCUPADO", conflicto.get("codigo").asText());
                assertEquals(1, turnosGuardados(base));
            }
        });
    }

    private static void dosProfesionalesDeManana(DemoPostgres.Base base) throws SQLException {
        try (var conexion = base.conexion(); var sql = conexion.createStatement()) {
            assertEquals(1, sql.executeUpdate("""
                    UPDATE asignacion_franja SET franja_codigo = 'MANANA'
                    WHERE odontologo_id = 12 AND dia_semana = 2
                    """));
        }
    }

    private static String turno(String inicio, long odontologo, long prestacion, String sillon) {
        return """
                {"pacienteId":1,"odontologoId":%d,"prestacionId":%d,"inicioLocal":"%s"%s}
                """.formatted(odontologo, prestacion, inicio, sillon == null ? "" : ",\"sillonId\":" + sillon);
    }

    private HttpRequest solicitud(ServletWebServerApplicationContext app, String cuerpo) {
        return HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + app.getWebServer().getPort() + "/api/turnos"))
                .timeout(Duration.ofSeconds(15)).header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(cuerpo)).build();
    }

    private JsonNode crear(ServletWebServerApplicationContext app, String cuerpo) throws Exception {
        return enviar(app, cuerpo, 201);
    }

    private JsonNode rechazo(ServletWebServerApplicationContext app, DemoPostgres.Base base, String cuerpo,
                             int estado, String codigo, int cantidad) throws Exception {
        var error = enviar(app, cuerpo, estado);
        assertEquals(codigo, error.get("codigo").asText());
        assertTrue(!error.get("mensaje").asText().isBlank());
        assertEquals(cantidad, turnosGuardados(base));
        return error;
    }

    private JsonNode enviar(ServletWebServerApplicationContext app, String cuerpo, int estado) throws Exception {
        var respuesta = http.send(solicitud(app, cuerpo), HttpResponse.BodyHandlers.ofString());
        assertEquals(estado, respuesta.statusCode(), respuesta.body());
        return json.readTree(respuesta.body());
    }

    private static int turnosGuardados(DemoPostgres.Base base) throws SQLException {
        return contar(base, "SELECT count(*) FROM turno");
    }

    private static int contar(DemoPostgres.Base base, String consulta) throws SQLException {
        try (var conexion = base.conexion(); var sql = conexion.createStatement(); var fila = sql.executeQuery(consulta)) {
            fila.next();
            return fila.getInt(1);
        }
    }

    private static void assertFecha(String esperado, JsonNode cuerpo, String campo) {
        assertEquals(LocalDateTime.parse(esperado), LocalDateTime.parse(cuerpo.get(campo).asText()));
    }

    private static Set<String> campos(JsonNode objeto) {
        var campos = new HashSet<String>();
        objeto.fieldNames().forEachRemaining(campos::add);
        return campos;
    }
}
