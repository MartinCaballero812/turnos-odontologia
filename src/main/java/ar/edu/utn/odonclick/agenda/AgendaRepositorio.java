package ar.edu.utn.odonclick.agenda;

import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@Profile("demo")
class AgendaRepositorio {

    record Prestacion(int duracionMinutos, boolean usaSillon) {}
    record Franja(LocalTime fin, boolean asignada) {}
    record Ocupacion(LocalDateTime inicio, LocalDateTime finPausa) {}

    private final JdbcClient jdbc;
    private final EntityManager entidades;

    AgendaRepositorio(JdbcClient jdbc, EntityManager entidades) {
        this.jdbc = jdbc;
        this.entidades = entidades;
    }

    boolean existePaciente(long id) {
        return jdbc.sql("SELECT EXISTS (SELECT 1 FROM paciente WHERE id = :id)")
                .param("id", id).query(Boolean.class).single();
    }

    Optional<Prestacion> prestacion(long id) {
        return jdbc.sql("SELECT duracion_minutos, usa_sillon FROM prestacion WHERE id = :id")
                .param("id", id).query(Prestacion.class).optional();
    }

    Optional<Long> sedeOdontologo(long id) {
        return sedeRecurso("odontologo", id);
    }

    Optional<Long> sedeSillon(long id) {
        return sedeRecurso("sillon", id);
    }

    private Optional<Long> sedeRecurso(String tabla, long id) {
        return jdbc.sql("SELECT sede_id FROM " + tabla + " WHERE id = :id")
                .param("id", id).query(Long.class).optional();
    }

    boolean cierreRegistrado(long sedeId, LocalDate fecha) {
        return jdbc.sql("SELECT EXISTS (SELECT 1 FROM cierre_sede WHERE sede_id = :sede AND fecha = :fecha)")
                .param("sede", sedeId).param("fecha", fecha).query(Boolean.class).single();
    }

    Optional<Franja> franja(long sedeId, long odontologoId, LocalDateTime inicio) {
        return jdbc.sql("""
                SELECT f.fin, a.odontologo_id IS NOT NULL AS asignada
                FROM franja_atencion f
                LEFT JOIN asignacion_franja a ON a.sede_id = f.sede_id AND a.dia_semana = f.dia_semana
                    AND a.franja_codigo = f.codigo AND a.odontologo_id = :odontologo
                WHERE f.sede_id = :sede AND f.dia_semana = :dia AND f.inicio <= :hora AND :hora < f.fin
                """)
                .param("odontologo", odontologoId).param("sede", sedeId)
                .param("dia", inicio.getDayOfWeek().getValue()).param("hora", inicio.toLocalTime())
                .query(Franja.class).optional();
    }

    void bloquearOdontologo(long id) {
        bloquear("odontologo", id);
    }

    void bloquearSillon(long id) {
        bloquear("sillon", id);
    }

    private void bloquear(String tabla, long id) {
        jdbc.sql("SELECT id FROM " + tabla + " WHERE id = :id FOR UPDATE")
                .param("id", id).query(Long.class).single();
    }

    Optional<Ocupacion> ocupacionOdontologo(long id, LocalDateTime inicio, LocalDateTime finPausa) {
        return ocupacion("odontologo_id", id, inicio, finPausa);
    }

    Optional<Ocupacion> ocupacionSillon(long id, LocalDateTime inicio, LocalDateTime finPausa) {
        return ocupacion("sillon_id", id, inicio, finPausa);
    }

    private Optional<Ocupacion> ocupacion(String columna, long id, LocalDateTime inicio, LocalDateTime finPausa) {
        return jdbc.sql("""
                SELECT inicio, fin_pausa FROM turno
                WHERE %s = :id AND inicio < :finPausa AND :inicio < fin_pausa
                ORDER BY inicio, id LIMIT 1
                """.formatted(columna))
                .param("id", id).param("inicio", inicio).param("finPausa", finPausa)
                .query(Ocupacion.class).optional();
    }

    void guardar(Turno turno) {
        entidades.persist(turno);
    }
}
