package ar.edu.utn.odonclick.agenda;

import com.fasterxml.jackson.databind.JsonNode;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Set;

record SolicitudTurno(long pacienteId, long odontologoId, long prestacionId, Long sillonId, LocalDateTime inicio) {

    private static final Set<String> CAMPOS = Set.of("pacienteId", "odontologoId", "prestacionId",
            "sillonId", "inicioLocal");

    static SolicitudTurno leer(JsonNode cuerpo) {
        if (cuerpo == null || !cuerpo.isObject()) {
            throw RechazoTurno.datos("Enviá los datos del turno como un objeto JSON.");
        }
        for (var nombres = cuerpo.fieldNames(); nombres.hasNext();) {
            if (!CAMPOS.contains(nombres.next())) {
                throw RechazoTurno.datos("La solicitud contiene campos que no corresponden al turno.");
            }
        }
        var inicio = cuerpo.path("inicioLocal");
        if (!inicio.isTextual()) {
            throw RechazoTurno.datos("Ingresá una fecha y hora de inicio válidas, sin zona horaria.");
        }
        try {
            return new SolicitudTurno(id(cuerpo.path("pacienteId"), "paciente"),
                    id(cuerpo.path("odontologoId"), "odontólogo"),
                    id(cuerpo.path("prestacionId"), "prestación"),
                    cuerpo.path("sillonId").isMissingNode() || cuerpo.path("sillonId").isNull()
                            ? null : id(cuerpo.path("sillonId"), "sillón"),
                    LocalDateTime.parse(inicio.textValue()).truncatedTo(ChronoUnit.MICROS));
        } catch (DateTimeParseException error) {
            throw RechazoTurno.datos("Ingresá una fecha y hora de inicio válidas, sin zona horaria.");
        }
    }

    private static long id(JsonNode valor, String campo) {
        if (!valor.isIntegralNumber() || !valor.canConvertToLong() || valor.longValue() <= 0) {
            throw RechazoTurno.datos("Ingresá un identificador válido para " + campo + ".");
        }
        return valor.longValue();
    }
}
