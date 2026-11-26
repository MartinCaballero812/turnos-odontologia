package ar.edu.utn.odonclick.recepcion;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("demo")
@DependsOnDatabaseInitialization
public class ContextoDemo {

    private final CatalogosRepositorio.Recepcion recepcion;

    ContextoDemo(CatalogosRepositorio repositorio, @Value("${APP_DEMO_RECEPCION_ID:}") String id) {
        try {
            recepcion = repositorio.recepcion(Long.parseLong(id))
                    .orElseThrow(() -> new IllegalStateException("APP_DEMO_RECEPCION_ID no identifica una recepción de demo."));
        } catch (NumberFormatException error) {
            throw new IllegalStateException("APP_DEMO_RECEPCION_ID debe indicar un ID de recepción de demo.", error);
        }
    }

    CatalogosRepositorio.Recepcion recepcion() {
        return recepcion;
    }

    public long sedeId() {
        return recepcion.sede().id();
    }
}
