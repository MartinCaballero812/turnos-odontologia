package ar.edu.utn.odonclick.recepcion;

import ar.edu.utn.odonclick.agenda.PlazoReserva;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("demo")
class CatalogosController {

    record Contexto(CatalogosRepositorio.Opcion sede, CatalogosRepositorio.Opcion recepcion, String zonaHoraria) {}
    record Catalogos(List<CatalogosRepositorio.Opcion> pacientes,
                     List<CatalogosRepositorio.Opcion> odontologos,
                     List<CatalogosRepositorio.Prestacion> prestaciones,
                     List<CatalogosRepositorio.Opcion> sillones) {}
    record ErrorCatalogo(String codigo, String mensaje) {}

    private final CatalogosRepositorio repositorio;
    private final ContextoDemo contexto;
    private final PlazoReserva plazo;

    CatalogosController(CatalogosRepositorio repositorio, ContextoDemo contexto, PlazoReserva plazo) {
        this.repositorio = repositorio;
        this.contexto = contexto;
        this.plazo = plazo;
    }

    @GetMapping("/api/contexto")
    Contexto contexto() {
        var recepcion = contexto.recepcion();
        return new Contexto(recepcion.sede(), recepcion.recepcion(), PlazoReserva.ZONA_HORARIA.getId());
    }

    @GetMapping("/api/catalogos")
    Catalogos catalogos(@RequestParam(name = "inicioLocal", required = false) String inicioLocal) {
        var inicio = inicioLocal == null ? null : LocalDateTime.parse(inicioLocal);
        var sedeId = contexto.recepcion().sede().id();
        var odontologos = inicio != null && plazo.admite(inicio.toLocalDate())
                ? repositorio.odontologos(sedeId, inicio) : List.<CatalogosRepositorio.Opcion>of();
        return new Catalogos(repositorio.pacientes(), odontologos, repositorio.prestaciones(),
                repositorio.sillones(sedeId));
    }

    @ExceptionHandler(DateTimeParseException.class)
    ResponseEntity<ErrorCatalogo> inicioInvalido() {
        return ResponseEntity.badRequest()
                .body(new ErrorCatalogo("DATOS_INVALIDOS", "Ingresá una fecha y hora de inicio válidas, sin zona horaria."));
    }
}
