# Funcionalidades

Las siguientes historias son insumo del roadmap y no sustituyen la futura especificación del change.

## Épica 1 — Gestión de turnos

### US-001 — Crear un turno sin solapamientos (primer change)

**Como** recepción, **quiero** crear un turno desde una interfaz gráfica, **para** que la agenda no asigne dos citas al mismo recurso ni quede fuera de horario.

**Criterios de aceptación:**

- [ ] La interfaz muestra la sede preasignada sin selector y los campos del turno sin preselección. Recepción elige la fecha y la hora antes del odontólogo. Solo se ofrecen odontólogos de esa sede asignados a la franja elegida; si la asignación deja de ser válida al cambiar el inicio, se limpia la selección. El filtro no comprueba solapamientos.
- [ ] **RN-01:** el selector de sillón se bloquea hasta elegir la prestación. Para radiografía muestra «Sin sillón» sin permitir editarlo; para las demás prestaciones exige elegir un sillón. El servidor también rechaza combinaciones incompatibles.
- [ ] El servidor calcula el fin por prestación y guarda una cita admisible; la interfaz confirma el resultado.
- [ ] **RN-03:** los conflictos con turnos ya guardados se comprueban por separado para el odontólogo y el sillón. Los rechazos no guardan un turno.
- [ ] **RN-03:** con dos solicitudes simultáneas válidas por separado que se superponen y comparten odontólogo o sillón, una se guarda y la otra se rechaza por conflicto. Al final hay un solo turno guardado para ese par de solicitudes.
- [ ] Se rechaza un comienzo inmediato o durante la pausa de un recurso compartido; el final exacto de la pausa puede usarse.
- [ ] Una radiografía sin sillón sigue reservando al odontólogo y su pausa.
- [ ] **RN-07 y RN-11:** se rechaza un turno en sábado, domingo o una fecha de cierre precargada, y también si el odontólogo no trabaja en esa franja aunque la sede esté abierta.
- [ ] **RN-09:** se rechazan las citas que no dejan la pausa completa dentro de la franja. Con cierre a las 13:00, una cita que termina a las 12:50 cabe con la pausa y una que termina a las 12:55 se rechaza. Con cierre a las 21:00, una cita que termina a las 20:50 cabe con la pausa y una que termina a las 20:55 se rechaza.
- [ ] **RN-13:** se rechazan las fechas anteriores a hoy o posteriores a un mes calendario desde hoy, en hora de Buenos Aires; el aviso indica el rango permitido. La fecha límite se ajusta al fin del mes siguiente si no tiene ese día.
- [ ] **RN-10:** se rechaza un odontólogo o un sillón que pertenece a una sede distinta de la del turno.
- [ ] **Motivo del rechazo:** si hay conflicto de agenda, el mensaje identifica el recurso ocupado y la franja, sin mostrar datos del otro paciente. El mensaje distingue los rechazos por datos, sede, fecha fuera de plazo, franja y uso de sillón. Si se pierde la respuesta, se informa que no se pudo confirmar el guardado, sin reenviar automáticamente.
- [ ] **RN-AU-01:** el servidor fija la sede del turno a partir del contexto de recepción; no acepta que la solicitud elija o cambie esa sede.
- [ ] **RN-12:** se puede guardar un turno válido sin asignar un asistente.

**Reglas:** RN-01 a RN-05, RN-07 a RN-13, RN-AU-01 y RN-DAT-01.

### US-002 — Reprogramar (MVP, posterior al primer change)

**Como** recepción, **quiero** mover una cita, **para** liberar su horario sin perder el registro original.

- [ ] Se valida la nueva ocupación; si hay conflicto se conserva la reserva anterior.
- [ ] Al completar el cambio, el horario anterior se libera y el registro original sigue disponible.

**Reglas:** RN-03 a RN-06 y RN-AU-01.

### US-003 — Cancelar (MVP, posterior al primer change)

**Como** recepción, **quiero** cancelar un turno, **para** volver a ofrecer el horario sin borrar lo ocurrido.

- [ ] Una cancelación deja el historial y no ocupa agenda.

**Reglas:** RN-06 y RN-AU-01.

## Épica 2 — Consulta y configuración

### US-004 — Consultar agenda propia (MVP)

**Como** odontólogo, **quiero** ver mi agenda diaria, **para** conocer mis próximas citas.

- [ ] No habilita edición ni consulta de agendas ajenas.

**Reglas:** RN-08 y RN-AU-01.

### US-005 — Consultar agenda de sillón (MVP)

**Como** asistente, **quiero** consultar la agenda diaria del sillón, **para** preparar instrumental y espacio.

- [ ] Es de solo lectura. El alcance de los turnos sin sillón queda por decidir.

**Reglas:** RN-02, RN-05 y RN-AU-01.

### US-006 — Configurar la operación (MVP)

**Como** administrador, **quiero** configurar sedes, sillones, profesionales, prestaciones y usuarios, **para** que la agenda refleje el funcionamiento de los consultorios.

- [ ] Gestiona catálogos para las tres sedes sin duplicar la aplicación.

**Reglas:** RN-02, RN-04, RN-07, RN-08 y RN-AU-02.

## Etapas posteriores

Reserva y portal del paciente, WhatsApp y recordatorios, lista de espera automatizada, datos clínicos e imágenes, cobros y señas, facturación, convenios y obras sociales, y analítica avanzada quedan fuera del MVP. Ver el [informe de mercado, sección D](../docs/discovery/informe-discovery.md#d-recomendación-final).
