package ar.edu.utn.odonclick.agenda;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
class Turno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long sedeId;
    private long pacienteId;
    private long odontologoId;
    private long prestacionId;
    private Long sillonId;
    private LocalDateTime inicio;
    private LocalDateTime fin;
    private LocalDateTime finPausa;
    private int duracionMinutos;

    protected Turno() {}

    Turno(long sedeId, SolicitudTurno solicitud, LocalDateTime fin, LocalDateTime finPausa, int duracionMinutos) {
        this.sedeId = sedeId;
        pacienteId = solicitud.pacienteId();
        odontologoId = solicitud.odontologoId();
        prestacionId = solicitud.prestacionId();
        sillonId = solicitud.sillonId();
        inicio = solicitud.inicio();
        this.fin = fin;
        this.finPausa = finPausa;
        this.duracionMinutos = duracionMinutos;
    }

    Creado creado() {
        return new Creado(id, sedeId, pacienteId, odontologoId, prestacionId, sillonId, inicio, fin, finPausa);
    }

    record Creado(long id, long sedeId, long pacienteId, long odontologoId, long prestacionId, Long sillonId,
                  LocalDateTime inicioLocal, LocalDateTime finLocal, LocalDateTime finPausaLocal) {}
}
