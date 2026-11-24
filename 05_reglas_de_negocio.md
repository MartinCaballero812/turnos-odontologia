# Reglas de negocio

RN-01 a RN-08 conservan los identificadores del [checklist](../docs/discovery/checklist-discovery.json). Las reglas nuevas documentan confirmaciones de la Etapa 3 o precisiones operativas derivadas, identificadas abajo.

## Agenda y recursos

| ID | Regla |
|---|---|
| RN-01 | Todo turno tiene un odontólogo responsable. Un turno puede no tener sillón (por ejemplo, radiografía); no puede tener sillón sin odontólogo. |
| RN-02 | Cada sillón pertenece a una sola sede. |
| RN-03 | No se admite superposición por odontólogo ni por sillón asignado. Si un turno ya guardado ocupa uno de esos recursos en el intervalo solicitado, se rechaza la nueva cita. Si dos solicitudes simultáneas son válidas por separado pero sus intervalos se superponen y comparten un recurso, una se guarda y la otra se rechaza por conflicto. |
| RN-04 | La prestación fija la duración de la cita. Recepción no indica ni modifica una duración distinta para un turno puntual. |
| RN-05 | Se aplica una pausa fija de **10 minutos** después de cada cita. Bloquea al odontólogo y al sillón si fue asignado; sin sillón bloquea al odontólogo. La asistencia dental realiza la preparación o higienización. |
| RN-06 | Cancelar o reprogramar libera la ocupación correspondiente y conserva registro del turno original. Al reprogramar, la nueva ubicación debe satisfacer las reglas de agenda. |
| RN-07 | Las franjas se definen por sede. En el MVP son 08:30–13:00 y 16:00–21:00 en los días de atención que se definan. |
| RN-08 | Cada odontólogo atiende en una única sede. |
| RN-09 | **Confirmado en Etapa 3:** la cita y sus 10 minutos de pausa deben entrar en una sola franja de atención. Si cierra a las 13:00, una cita que termina a las 12:50 cabe; una que termina a las 12:55 se rechaza. No se pueden cruzar dos franjas ni usar el intervalo cerrado del mediodía. Es una regla operativa, no una prescripción legal de agenda. |
| RN-10 | **Derivación de RN-02 y RN-08:** el odontólogo y el sillón, si hay, deben pertenecer a la sede del turno. |

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
