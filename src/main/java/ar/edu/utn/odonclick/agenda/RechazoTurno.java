package ar.edu.utn.odonclick.agenda;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

final class RechazoTurno extends RuntimeException {

    private static final DateTimeFormatter HORARIO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    final int estado;
    final String codigo;
    final Conflicto conflicto;

    record Conflicto(String recurso, LocalDateTime inicio, LocalDateTime fin) {}

    RechazoTurno(int estado, String codigo, String mensaje) {
        this(estado, codigo, mensaje, null);
    }

    private RechazoTurno(int estado, String codigo, String mensaje, Conflicto conflicto) {
        super(mensaje);
        this.estado = estado;
        this.codigo = codigo;
        this.conflicto = conflicto;
    }

    static RechazoTurno datos(String mensaje) {
        return new RechazoTurno(400, "DATOS_INVALIDOS", mensaje);
    }

    static RechazoTurno noExiste(String recurso) {
        return new RechazoTurno(404, "IDENTIFICADOR_INEXISTENTE", "No se encontró el recurso seleccionado: " + recurso + ".");
    }

    static RechazoTurno ocupado(String recurso, String nombre, AgendaRepositorio.Ocupacion ocupacion) {
        return new RechazoTurno(409, recurso + "_OCUPADO", "El " + nombre + " está ocupado desde "
                + ocupacion.inicio().format(HORARIO) + " hasta " + ocupacion.finPausa().format(HORARIO) + ".",
                new Conflicto(recurso, ocupacion.inicio(), ocupacion.finPausa()));
    }
}
