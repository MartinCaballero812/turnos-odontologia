package ar.edu.utn.odonclick.agenda;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import org.springframework.stereotype.Component;

@Component
public class PlazoReserva {

    public static final ZoneId ZONA_HORARIA = ZoneId.of("America/Argentina/Buenos_Aires");

    private final Clock reloj;

    public PlazoReserva(Clock reloj) {
        this.reloj = reloj.withZone(ZONA_HORARIA);
    }

    public boolean admite(LocalDate fecha) {
        return rango().admite(fecha);
    }

    public Rango rango() {
        var hoy = LocalDate.now(reloj);
        return new Rango(hoy, hoy.plusMonths(1));
    }

    public record Rango(LocalDate desde, LocalDate hasta) {
        public boolean admite(LocalDate fecha) {
            return !fecha.isBefore(desde) && !fecha.isAfter(hasta);
        }
    }
}
