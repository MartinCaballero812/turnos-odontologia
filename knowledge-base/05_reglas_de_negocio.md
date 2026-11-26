# Reglas de negocio

RN-01 a RN-08 conservan los identificadores del [checklist](../docs/discovery/checklist-discovery.json). La KB incorpora las decisiones operativas confirmadas después del Discovery.

## Agenda y recursos

| ID | Regla |
|---|---|
| RN-01 | Todo turno tiene un odontólogo responsable. En el catálogo de C-01, radiografía no admite sillón y las demás prestaciones requieren un sillón de la sede. El servidor valida la regla según la prestación; no puede haber sillón sin odontólogo. |
| RN-02 | Cada sillón pertenece a una sola sede. |
| RN-03 | No se admite superposición por odontólogo ni por sillón asignado. Si un turno ya guardado ocupa uno de esos recursos en el intervalo solicitado, se rechaza la nueva cita. Si dos solicitudes simultáneas son válidas por separado pero sus intervalos se superponen y comparten un recurso, una se guarda y la otra se rechaza por conflicto. |
| RN-04 | La prestación fija la duración de la cita. Recepción no indica ni modifica una duración distinta para un turno puntual. |
| RN-05 | Se aplica una pausa fija de **10 minutos** después de cada cita. Bloquea al odontólogo y al sillón si fue asignado; sin sillón bloquea al odontólogo. La asistencia dental realiza la preparación o higienización. |
| RN-06 | Cancelar o reprogramar libera la ocupación correspondiente y conserva registro del turno original. Al reprogramar, la nueva ubicación debe satisfacer las reglas de agenda. |
| RN-07 | La apertura se define por sede. Las franjas iniciales son 08:30–13:00 y 16:00–21:00, de lunes a viernes. No se atiende sábados, domingos, feriados ni otros cierres excepcionales registrados. C-01 usa una fecha de cierre precargada, sin calendario automático. |
| RN-08 | Cada odontólogo atiende en una única sede. |
| RN-09 | La cita y sus 10 minutos de pausa deben caber completas en la franja de la sede asignada al odontólogo para ese día. Un fin de cita a las 12:50 o 20:50 cabe justo con la pausa; a las 12:55 o 20:55 se rechaza. No se pueden cruzar franjas ni usar el intervalo cerrado del mediodía. |
| RN-10 | **Derivación de RN-02 y RN-08:** el odontólogo y el sillón, si hay, deben pertenecer a la sede del turno. |
| RN-11 | El odontólogo puede tener como máximo una franja laboral por día hábil, de mañana o de tarde, y puede alternarlas entre días. Es una política de Odonclick. Aunque la sede esté abierta, no se puede agendar fuera de la franja asignada al odontólogo. C-01 valida asignaciones precargadas. |
| RN-12 | La disponibilidad de un asistente no condiciona la reserva. La asignación opcional de un asistente disponible es una decisión de recepción en el MVP posterior; no se administran horarios laborales de asistentes en C-01. |
| RN-13 | C-01 permite agendar desde hoy hasta un mes calendario después, inclusive, según la fecha de `America/Argentina/Buenos_Aires`. Si el mes siguiente no tiene el mismo número de día, el límite es su último día. Estar dentro del plazo no reemplaza las reglas de apertura, asignación, pausa ni solapamiento. |

## Cambios de configuración posteriores a C-01

- El administrador asigna o cambia franjas de odontólogos y registra feriados o cierres excepcionales. Ante un cambio de asignación que afecta citas existentes, recepción puede reprogramarlas con el mismo odontólogo o mantener el horario y reasignarlas a otro disponible que tenga asignada esa franja. La asignación laboral sigue limitada a una franja por día.
- El horario de apertura de una franja solo se cambia cuando la franja afectada no tiene citas pendientes. No se permiten nuevas reservas en esa franja hasta configurar el nuevo horario. El horario `08:00–14:00` es solo un ejemplo de un posible cambio, no un nuevo horario aprobado.

## Acceso y datos

| ID | Regla |
|---|---|
| RN-AU-01 | Recepción solo consulta y opera turnos de su propia sede. Odontólogo y asistente son roles de consulta con los límites de [Actores y roles](03_actores_y_roles.md). |
| RN-AU-02 | El administrador configura sedes, sillones, profesionales, prestaciones y usuarios de todas las sedes. No gestiona turnos en el MVP. |
| RN-DAT-01 | La demostración, los datos iniciales y las pruebas usan exclusivamente datos ficticios. |

## Interpretación verificable de los intervalos

La ocupación incluye el intervalo desde `inicio` hasta `fin de cita + 10 min`. Para un recurso compartido, dos ocupaciones chocan si cada una comienza antes del fin de la otra. El extremo final no está ocupado: iniciar **justo al terminar la cita** se rechaza; iniciar **justo al terminar la pausa** se admite si la nueva cita también cumple las demás reglas.

Ejemplo: una cita de 10:00 a 10:30 ocupa sus recursos hasta las 10:40. Con el mismo odontólogo o sillón, 10:30 o 10:39 se rechazan; 10:40 no se rechaza por esa cita. Si no se comparte ningún recurso, esta regla no impide dos citas simultáneas. Una cita sin sillón nunca bloquea un sillón.

La protección contra solicitudes simultáneas incompatibles forma parte del primer change. No se garantiza cuál de las dos se guarda.
