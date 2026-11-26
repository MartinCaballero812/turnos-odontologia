package ar.edu.utn.odonclick.agenda;

import com.fasterxml.jackson.databind.JsonNode;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("demo")
class TurnosController {

    private static final Logger LOG = LoggerFactory.getLogger(TurnosController.class);
    private final AgendaServicio servicio;

    TurnosController(AgendaServicio servicio) {
        this.servicio = servicio;
    }

    @PostMapping(path = "/api/turnos", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Turno.Creado> crear(@RequestBody JsonNode cuerpo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicio.crear(SolicitudTurno.leer(cuerpo)));
    }

    record Error(String codigo, String mensaje) {}
    record ErrorConflicto(String codigo, String mensaje, String recurso,
                          LocalDateTime inicioOcupadoLocal, LocalDateTime finOcupadoLocal) {}

    @ExceptionHandler(RechazoTurno.class)
    ResponseEntity<?> rechazo(RechazoTurno error) {
        if (error.conflicto != null) {
            var conflicto = error.conflicto;
            return ResponseEntity.status(error.estado).body(new ErrorConflicto(error.codigo, error.getMessage(),
                    conflicto.recurso(), conflicto.inicio(), conflicto.fin()));
        }
        return ResponseEntity.status(error.estado).body(new Error(error.codigo, error.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<?> cuerpoInvalido() {
        return rechazo(RechazoTurno.datos("Enviá un cuerpo JSON válido con los datos del turno."));
    }

    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<Error> errorTecnico(RuntimeException error) {
        LOG.error("Error al procesar un turno ({})", error.getClass().getSimpleName());
        return ResponseEntity.internalServerError().body(new Error("ERROR_TECNICO",
                "No se pudo confirmar si el turno se guardó."));
    }
}
