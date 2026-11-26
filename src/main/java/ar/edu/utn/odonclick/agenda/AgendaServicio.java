package ar.edu.utn.odonclick.agenda;

import ar.edu.utn.odonclick.recepcion.ContextoDemo;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Profile("demo")
class AgendaServicio {

    private static final int PAUSA_MINUTOS = 10;
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final AgendaRepositorio repositorio;
    private final ContextoDemo contexto;
    private final PlazoReserva plazo;

    AgendaServicio(AgendaRepositorio repositorio, ContextoDemo contexto, PlazoReserva plazo) {
        this.repositorio = repositorio;
        this.contexto = contexto;
        this.plazo = plazo;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Turno.Creado crear(SolicitudTurno solicitud) {
        var sede = contexto.sedeId();
        var prestacion = validarReferencias(sede, solicitud);
        var inicio = solicitud.inicio();
        validarFecha(sede, inicio);
        var fin = inicio.plusMinutes(prestacion.duracionMinutos());
        var finPausa = fin.plusMinutes(PAUSA_MINUTOS);
        validarFranja(sede, solicitud.odontologoId(), inicio, finPausa);

        repositorio.bloquearOdontologo(solicitud.odontologoId());
        if (solicitud.sillonId() != null) {
            repositorio.bloquearSillon(solicitud.sillonId());
        }
        validarOcupacion(solicitud, finPausa);

        var turno = new Turno(sede, solicitud, fin, finPausa, prestacion.duracionMinutos());
        repositorio.guardar(turno);
        return turno.creado();
    }

    private AgendaRepositorio.Prestacion validarReferencias(long sede, SolicitudTurno solicitud) {
        if (!repositorio.existePaciente(solicitud.pacienteId())) {
            throw RechazoTurno.noExiste("paciente");
        }
        var prestacion = repositorio.prestacion(solicitud.prestacionId())
                .orElseThrow(() -> RechazoTurno.noExiste("prestación"));
        var sedeOdontologo = repositorio.sedeOdontologo(solicitud.odontologoId())
                .orElseThrow(() -> RechazoTurno.noExiste("odontólogo"));
        validarSede(sede, sedeOdontologo);
        if (prestacion.usaSillon() && solicitud.sillonId() == null) {
            throw new RechazoTurno(422, "SILLON_REQUERIDO", "Elegí un sillón para esta prestación.");
        }
        if (!prestacion.usaSillon() && solicitud.sillonId() != null) {
            throw new RechazoTurno(422, "SILLON_NO_APLICA", "Esta prestación no usa sillón.");
        }
        if (solicitud.sillonId() != null) {
            var sedeSillon = repositorio.sedeSillon(solicitud.sillonId())
                    .orElseThrow(() -> RechazoTurno.noExiste("sillón"));
            validarSede(sede, sedeSillon);
        }
        return prestacion;
    }

    private static void validarSede(long sede, long sedeRecurso) {
        if (sede != sedeRecurso) {
            throw new RechazoTurno(403, "RECURSO_FUERA_DE_SEDE", "El recurso elegido no pertenece a tu sede.");
        }
    }

    private void validarFecha(long sede, LocalDateTime inicio) {
        var fecha = inicio.toLocalDate();
        var rango = plazo.rango();
        if (!rango.admite(fecha)) {
            throw new RechazoTurno(422, "FECHA_FUERA_DE_PLAZO",
                    "Elegí una fecha entre el " + rango.desde().format(FECHA) + " y el "
                            + rango.hasta().format(FECHA) + ", inclusive.");
        }
        if (fecha.getDayOfWeek().getValue() > 5 || repositorio.cierreRegistrado(sede, fecha)) {
            throw new RechazoTurno(422, "SEDE_CERRADA", "La sede está cerrada en la fecha elegida.");
        }
    }

    private void validarFranja(long sede, long odontologo, LocalDateTime inicio, LocalDateTime finPausa) {
        var franja = repositorio.franja(sede, odontologo, inicio).orElseThrow(AgendaServicio::fueraDeFranja);
        if (!franja.asignada()) {
            throw new RechazoTurno(422, "ODONTOLOGO_FUERA_DE_FRANJA", "El odontólogo no trabaja en esa franja.");
        }
        if (finPausa.isAfter(inicio.toLocalDate().atTime(franja.fin()))) {
            throw fueraDeFranja();
        }
    }

    private static RechazoTurno fueraDeFranja() {
        return new RechazoTurno(422, "CITA_O_PAUSA_FUERA_DE_FRANJA",
                "La cita y los 10 minutos de pausa deben quedar dentro de una misma franja de atención.");
    }

    private void validarOcupacion(SolicitudTurno solicitud, LocalDateTime finPausa) {
        repositorio.ocupacionOdontologo(solicitud.odontologoId(), solicitud.inicio(), finPausa)
                .ifPresent(ocupacion -> { throw RechazoTurno.ocupado("ODONTOLOGO", "odontólogo", ocupacion); });
        if (solicitud.sillonId() != null) {
            repositorio.ocupacionSillon(solicitud.sillonId(), solicitud.inicio(), finPausa)
                    .ifPresent(ocupacion -> { throw RechazoTurno.ocupado("SILLON", "sillón", ocupacion); });
        }
    }
}
