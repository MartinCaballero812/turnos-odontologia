package ar.edu.utn.odonclick;

import java.sql.DriverManager;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class DemoPostgres {

    private static final String URL_BASE = "jdbc:postgresql://127.0.0.1:5432/odonclick_test";
    private static final String USUARIO = "odonclick_test_app";
    private static final String AHORA = "2026-11-25T15:00:00Z";

    private DemoPostgres() {}

    @FunctionalInterface
    interface Prueba {
        void ejecutar(DemoPostgres.Base base) throws Exception;
    }

    record Base(String nombre, String password) {
        private String url() {
            return URL_BASE + "?currentSchema=" + nombre;
        }

        ServletWebServerApplicationContext iniciar(long recepcionId) {
            return iniciar(recepcionId, AHORA);
        }

        ServletWebServerApplicationContext iniciar(long recepcionId, String ahora) {
            return (ServletWebServerApplicationContext) new SpringApplicationBuilder(OdonclickApplication.class,
                    RelojPrueba.class)
                    .initializers(app -> TestPropertyValues.of("spring.datasource.password=" + password).applyTo(app))
                    .run("--spring.profiles.active=demo", "--server.port=0",
                            "--spring.datasource.url=" + url() + "&ApplicationName=" + nombre,
                            "--spring.datasource.username=" + USUARIO, "--APP_DEMO_RECEPCION_ID=" + recepcionId,
                            "--odonclick.prueba.instante=" + ahora);
        }

        java.sql.Connection conexion() throws java.sql.SQLException {
            return DriverManager.getConnection(url(), USUARIO, password);
        }
    }

    static void conBase(Prueba prueba) throws Exception {
        var password = System.getenv("ODONCLICK_TEST_DB_PASSWORD");
        assertTrue(password != null && !password.isEmpty(), "Configurá ODONCLICK_TEST_DB_PASSWORD según el README");
        try (var conexion = DriverManager.getConnection(URL_BASE, USUARIO, password);
             var sql = conexion.createStatement()) {
            assertEquals(16, conexion.getMetaData().getDatabaseMajorVersion());
            try (var datos = sql.executeQuery("SELECT current_database(), current_user")) {
                assertTrue(datos.next());
                assertEquals("odonclick_test", datos.getString(1));
                assertEquals(USUARIO, datos.getString(2));
            }
            var esquema = "prueba_" + UUID.randomUUID().toString().replace("-", "");
            sql.execute("CREATE SCHEMA " + esquema);
            try {
                prueba.ejecutar(new Base(esquema, password));
            } finally {
                sql.execute("DROP SCHEMA " + esquema + " CASCADE");
            }
        }
    }

    @TestConfiguration(proxyBeanMethods = false)
    static class RelojPrueba {
        @Bean
        @Primary
        Clock relojFijo(@Value("${odonclick.prueba.instante}") String instante) {
            return Clock.fixed(Instant.parse(instante), ZoneOffset.UTC);
        }
    }
}
