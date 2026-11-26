package ar.edu.utn.odonclick.agenda;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlazoReservaTest {

    @Test
    void s21AjustaElDiaAlFinDeFebreroNoBisiesto() {
        var plazo = new PlazoReserva(Clock.fixed(Instant.parse("2027-01-31T15:00:00Z"), ZoneOffset.UTC));
        var finDeFebrero = LocalDate.of(2027, 2, 28);
        assertEquals(finDeFebrero, plazo.rango().hasta());
        assertTrue(plazo.admite(finDeFebrero));
        assertFalse(plazo.admite(finDeFebrero.plusDays(1)));
    }
}
