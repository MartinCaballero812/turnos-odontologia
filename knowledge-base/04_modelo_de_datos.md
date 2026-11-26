# Modelo de datos

**Propuesta lógica.** Esta KB no fija nombres de tablas, migraciones ni tipos SQL definitivos. Los campos de horarios reflejan reglas confirmadas.

## Dominios

- **Organización:** sedes, franjas, cierres, profesionales, asignaciones de franja y sillones.
- **Identidades internas:** usuarios, rol y sede operativa si corresponde.
- **Agenda:** prestaciones, pacientes ficticios, turnos e historial.

## ERD

```text
Sede 1 ── N FranjaAtencion
Sede 1 ── N CierreSede
Sede 1 ── N Odontologo
Sede 1 ── N Sillon
Sede 1 ── N Turno
Odontologo 1 ── N Turno (obligatorio)
Odontologo 1 ── N AsignacionFranja
FranjaAtencion 1 ── N AsignacionFranja
Sillon 1 ── N Turno (0..1 sillón por turno)
Prestacion 1 ── N Turno
Paciente 1 ── N Turno
Usuario N ── 0..1 Sede (según el rol; el administrador configura todas las sedes)
Turno 1 ── N EventoTurno (historial de cambios del MVP)
```

## Entidades

| Entidad | Atributos propuestos | Restricciones y acceso |
|---|---|---|
| Sede | ID, nombre | Cada recurso físico y odontólogo pertenece a una sede. |
| FranjaAtencion | ID, sede, día, hora de inicio y fin | Inicio anterior a fin; mañana y tarde son franjas distintas. Las franjas iniciales se aplican de lunes a viernes. |
| CierreSede | Sede, fecha local | No se agendan citas en una fecha registrada de cierre de esa sede. |
| Odontologo | ID, sede, nombre de visualización | Sede obligatoria; asociación a usuario para consulta propia, a diseñar. |
| AsignacionFranja | Odontólogo, franja de la sede y día | Como máximo una franja por odontólogo y día hábil. Se usa tanto para filtrar opciones por fecha/hora como para validar al guardar. |
| Sillon | ID, sede, etiqueta | Sede obligatoria; el sillón elegido debe ser de la sede del turno. |
| Prestacion | ID, nombre, duración en minutos, indica si usa sillón | Duración positiva. No editable por recepción para un turno puntual. Radiografía no usa sillón; las demás prestaciones de C-01 lo requieren (RN-01). |
| Paciente | ID, nombre ficticio | Solo lo necesario para agendar en la demo, sin historial clínico ni datos reales. |
| Usuario | ID, identificador interno, rol, sede si aplica | Recepción limitada a una sede. La asignación del asistente todavía no se define. |
| Turno | ID, sede, paciente, odontólogo, sillón según prestación, prestación, inicio, fin de cita, duración aplicada, estado | El sillón solo queda vacío si la prestación no lo usa. Fin calculado en servidor. Conservar duración aplicada para que editar el catálogo no desplace reservas ya hechas (propuesta). |
| EventoTurno | ID, turno, tipo, fecha/hora, usuario responsable y datos previos necesarios | Propuesta para cancelación/reprogramación; no requiere un módulo de auditoría en el primer change. |

Guardar los vínculos de sede con integridad referencial y validar pertenencia en el backend. Para buscar ocupación, prever índices por odontólogo e inicio y por sillón e inicio cuando exista. Un índice de búsqueda por sí solo **no impide** solapamientos; ver [concurrencia](08_arquitectura_propuesta.md#concurrencia-de-la-agenda).

## Datos iniciales ficticios

- Tres sedes con franjas `08:30–13:00` y `16:00–21:00` y una fecha de cierre precargada.
- En la demo, un odontólogo asignado a mañana y otro a tarde; para la prueba aislada de conflicto de sillón, dos profesionales en la misma franja. Usar dos sillones y recursos de otra sede para verificar pertenencia.
- Prestaciones ficticias de duraciones distintas, incluida una cita de ejemplo sin sillón, pacientes ficticios y contexto local de recepción.
