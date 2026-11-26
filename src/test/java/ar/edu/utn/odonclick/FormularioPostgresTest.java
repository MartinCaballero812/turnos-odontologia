package ar.edu.utn.odonclick;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("browser")
class FormularioPostgresTest {

    @Test
    void ejecutaLosEscenariosDeFormularioEnElNavegador() throws Exception {
        DemoPostgres.conBase(base -> {
            try (var app = base.iniciar(1)) {
                var npm = System.getProperty("os.name").startsWith("Windows")
                        ? new ProcessBuilder("cmd", "/c", "npm.cmd", "run", "test:browser")
                        : new ProcessBuilder("npm", "run", "test:browser");
                npm.directory(Path.of("frontend").toAbsolutePath().toFile());
                npm.environment().put("ODONCLICK_BASE_URL", "http://127.0.0.1:" + app.getWebServer().getPort());
                npm.environment().remove("ODONCLICK_TEST_DB_PASSWORD");
                var proceso = npm.inheritIO().start();
                try {
                    assertTrue(proceso.waitFor(120, TimeUnit.SECONDS), "La suite de navegador excedió 120 segundos");
                    assertEquals(0, proceso.exitValue(), "Fallaron los escenarios de navegador");
                } finally {
                    if (proceso.isAlive()) proceso.destroyForcibly();
                }
            }
        });
    }
}
