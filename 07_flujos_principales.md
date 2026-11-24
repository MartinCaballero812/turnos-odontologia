# Flujos principales

## Flujo 1 — Crear turno (primer change)

**Actor:** recepción. **Disparador:** solicitud de una cita para un paciente ficticio en la sede autorizada.

1. La persona completa el formulario con fecha/hora, odontólogo, prestación, paciente y sillón si aplica.
2. La API valida los datos, ámbito de sede y pertenencia de los recursos. La API obtiene la duración de la prestación; no confía en un fin calculado por la interfaz.
3. Se calcula el fin de la cita y de su pausa. Ambos deben encajar en la misma franja de sede.
4. El backend comprueba la ocupación de cada recurso y guarda el nuevo turno solo si es admisible, incluso ante solicitudes simultáneas incompatibles.
5. La interfaz confirma la creación o muestra un rechazo por horario, permiso, dato o recurso. Puede refrescar un listado mínimo para la demostración.

| Caso | Resultado |
|---|---|
| Odontólogo y sillón libres, cita y pausa dentro de franja | Se guarda y confirma. |
| Mismo odontólogo, distinto sillón, horarios incompatibles | Rechazo por odontólogo. |
| Distinto odontólogo, mismo sillón, horarios incompatibles | Rechazo por sillón. |
| Cita sin sillón y odontólogo libre | Se guarda sin reservar un sillón. No puede solaparse con otro turno de ese odontólogo. |
| Inicio durante la pausa | Rechazo. Al terminar la pausa exacta, no hay conflicto por el turno previo. |
| Fin de cita 12:55, pausa hasta 13:05 | Rechazo por límite de la franja. Fin a las 12:50 y pausa hasta las 13:00 sí encajan. |
| Recepción intenta operar fuera de su sede o selecciona recursos de otra | Rechazo sin cambios. |

**Escenario técnico obligatorio del primer change:** si dos solicitudes simultáneas son válidas por separado, pero se superponen y comparten odontólogo o sillón, una se guarda y la otra se rechaza por conflicto. No se fija cuál gana. Este caso se suma a la prueba normal, donde el primer turno ya está guardado cuando llega el segundo.

## Flujo 2 — Consultar agenda (MVP)

El backend filtra por sede y rol; recepción consulta su sede, odontología su propia agenda y asistencia la agenda diaria del sillón. La consulta de turnos sin sillón por parte del asistente queda por definir.

## Flujo 3 — Reprogramar o cancelar (MVP)

1. Recepción selecciona un turno de su sede.
2. Al reprogramar, se valida la nueva franja y ocupación antes de liberar la anterior; si falla, el turno original sigue vigente.
3. Al cancelar, el turno queda identificado como cancelado y deja de ocupar recursos.
4. El sistema conserva el registro original para ambas operaciones.

El mecanismo de ingreso del MVP, las transiciones exactas de finalización y la interfaz de estos flujos se precisarán en sus cambios, no durante el primero.
