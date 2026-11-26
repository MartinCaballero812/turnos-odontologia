package ar.edu.utn.odonclick;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("postgres")
class EsquemaPostgresTest {

    @Test
    void cargaTresSedesSinDuplicarDatosYRechazaUnaSegundaFranja() throws SQLException {
        var password = System.getenv("ODONCLICK_TEST_DB_PASSWORD");
        assertTrue(password != null && !password.isEmpty(), "Configurá ODONCLICK_TEST_DB_PASSWORD según el README");

        try (var conexion = DriverManager.getConnection(
                "jdbc:postgresql://127.0.0.1:5432/odonclick_test", "odonclick_test_app", password)) {
            assertEquals("odonclick_test", valor(conexion, "SELECT current_database()"));
            assertEquals("odonclick_test_app", valor(conexion, "SELECT current_user"));
            assertEquals(16, conexion.getMetaData().getDatabaseMajorVersion());
            conexion.setAutoCommit(false);
            try (var sql = conexion.createStatement()) {
                var esquema = "prueba_" + UUID.randomUUID().toString().replace("-", "");
                sql.execute("CREATE SCHEMA " + esquema);
                sql.execute("SET LOCAL search_path TO " + esquema);

                cargar(conexion);
                verificarDatos(conexion);
                assertEquals("0", valor(conexion, "SELECT count(*) FROM turno"));
                sql.executeUpdate("""
                        INSERT INTO turno (sede_id, paciente_id, odontologo_id, sillon_id,
                                           prestacion_id, inicio, fin, fin_pausa, duracion_minutos)
                        VALUES (1, 1, 11, 11, 1, '2026-12-01 10:00', '2026-12-01 10:30', '2026-12-01 10:40', 30)
                        """);

                cargar(conexion);
                verificarDatos(conexion);
                assertEquals("1", valor(conexion, "SELECT count(*) FROM turno"));

                var error = assertThrows(SQLException.class, () -> sql.executeUpdate("""
                        INSERT INTO asignacion_franja (odontologo_id, sede_id, dia_semana, franja_codigo)
                        VALUES (11, 1, 2, 'TARDE')
                        """));
                assertEquals("23505", error.getSQLState());
            } finally {
                conexion.rollback();
            }
        }
    }

    private static void cargar(Connection conexion) {
        for (var ruta : List.of("db/schema.sql", "db/demo.sql")) {
            ScriptUtils.executeSqlScript(conexion,
                    new EncodedResource(new ClassPathResource(ruta), StandardCharsets.UTF_8));
        }
    }

    private static void verificarDatos(Connection conexion) throws SQLException {
        assertEquals("SEDE 1, SEDE 2, SEDE 3", valor(conexion,
                "SELECT string_agg(nombre, ', ' ORDER BY id) FROM sede"));
        assertEquals("1,2,3", valor(conexion, "SELECT string_agg(sede_id::text, ',' ORDER BY id) FROM recepcion"));
        assertEquals("0", valor(conexion, """
                SELECT count(*) FROM sede
                WHERE (SELECT count(*) FROM odontologo WHERE sede_id = sede.id) <> 2
                   OR (SELECT count(*) FROM sillon WHERE sede_id = sede.id) <> 2
                   OR (SELECT count(*) FROM franja_atencion WHERE sede_id = sede.id) <> 10
                """));
        assertEquals("30", valor(conexion, "SELECT count(*) FROM asignacion_franja"));
        assertEquals("1", valor(conexion, "SELECT count(*) FROM cierre_sede WHERE sede_id = 1 AND fecha = '2026-12-08'"));
        assertEquals("Lucía Ferrer:MANANA,Julián Soria:TARDE", valor(conexion, """
                SELECT string_agg(o.nombre || ':' || a.franja_codigo, ',' ORDER BY o.id)
                FROM odontologo o JOIN asignacion_franja a ON o.id = a.odontologo_id
                WHERE a.sede_id = 1 AND a.dia_semana = 2
                """));
        for (var tabla : List.of("paciente", "prestacion")) {
            assertEquals("5", valor(conexion, "SELECT count(*) FROM " + tabla));
        }
        assertEquals("Radiografía", valor(conexion, "SELECT nombre FROM prestacion WHERE NOT usa_sillon"));
    }

    private static String valor(Connection conexion, String consulta) throws SQLException {
        try (var sql = conexion.createStatement(); var filas = sql.executeQuery(consulta)) {
            assertTrue(filas.next(), "Se esperaba una fila");
            return filas.getString(1);
        }
    }
}
