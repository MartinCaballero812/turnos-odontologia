package ar.edu.utn.odonclick;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration"
)
class OdonclickApplicationTest {

    @LocalServerPort
    private int puerto;

    @Test
    void iniciaElServidorLocalYSirveLosRecursosCompilados() throws IOException, InterruptedException {
        var cliente = HttpClient.newHttpClient();
        var js = obtener(cliente, "/assets/api.js");
        assertEquals(200, js.statusCode());
        assertTrue(js.body().contains("export function obtenerContexto("));
        var css = obtener(cliente, "/assets/tokens.css");
        assertEquals(200, css.statusCode());
        assertTrue(css.body().contains(":root"));
    }

    private HttpResponse<String> obtener(HttpClient cliente, String ruta) throws IOException, InterruptedException {
        var url = URI.create("http://127.0.0.1:" + puerto + ruta);
        return cliente.send(HttpRequest.newBuilder(url).GET().build(), HttpResponse.BodyHandlers.ofString());
    }
}
