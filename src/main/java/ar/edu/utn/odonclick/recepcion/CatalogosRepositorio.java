package ar.edu.utn.odonclick.recepcion;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@Profile("demo")
class CatalogosRepositorio {

    record Opcion(long id, String nombre) {}
    record Prestacion(long id, String nombre, boolean usaSillon, int duracionMinutos) {}
    record Recepcion(Opcion sede, Opcion recepcion) {}

    private final JdbcClient jdbc;

    CatalogosRepositorio(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    Optional<Recepcion> recepcion(long id) {
        return jdbc.sql("""
                SELECT s.id AS sede_id, s.nombre AS sede_nombre, r.id, r.nombre
                FROM recepcion r JOIN sede s ON s.id = r.sede_id
                WHERE r.id = :id
                """)
                .param("id", id)
                .query((fila, n) -> new Recepcion(
                        new Opcion(fila.getLong("sede_id"), fila.getString("sede_nombre")),
                        new Opcion(fila.getLong("id"), fila.getString("nombre"))))
                .optional();
    }

    List<Opcion> pacientes() {
        return jdbc.sql("SELECT id, nombre FROM paciente ORDER BY id").query(Opcion.class).list();
    }

    List<Prestacion> prestaciones() {
        return jdbc.sql("SELECT id, nombre, usa_sillon, duracion_minutos FROM prestacion ORDER BY id")
                .query(Prestacion.class).list();
    }

    List<Opcion> sillones(long sedeId) {
        return jdbc.sql("SELECT id, nombre FROM sillon WHERE sede_id = :sede ORDER BY id")
                .param("sede", sedeId)
                .query(Opcion.class).list();
    }

    List<Opcion> odontologos(long sedeId, LocalDateTime inicio) {
        return jdbc.sql("""
                SELECT o.id, o.nombre
                FROM odontologo o
                JOIN asignacion_franja a ON a.odontologo_id = o.id AND a.sede_id = o.sede_id
                JOIN franja_atencion f ON f.sede_id = a.sede_id AND f.dia_semana = a.dia_semana
                    AND f.codigo = a.franja_codigo
                WHERE o.sede_id = :sede AND a.dia_semana = :dia
                    AND f.inicio <= :hora AND :hora < f.fin
                    AND NOT EXISTS (SELECT 1 FROM cierre_sede c WHERE c.sede_id = :sede AND c.fecha = :fecha)
                ORDER BY o.id
                """)
                .param("sede", sedeId)
                .param("dia", inicio.getDayOfWeek().getValue())
                .param("hora", inicio.toLocalTime())
                .param("fecha", inicio.toLocalDate())
                .query(Opcion.class).list();
    }
}
