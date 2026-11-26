# Reserva interna de turnos

## Purpose

Permitir que recepción registre citas en su sede sin solapamientos de odontólogos ni de sillones asignados, respetando las franjas y los cierres de atención.

## ADDED Requirements

### Requirement: Contexto y catálogos de la sede de recepción

El sistema MUST determinar la recepción y su sede en el servidor. `GET /api/contexto` expone ese contexto y `GET /api/catalogos` ofrece las opciones de la sede activa para la demo. El formulario de escritorio MUST mostrar la sede sin selector y permitir operar con teclado, con foco visible.

#### Scenario: S01 — Formulario en la sede preasignada

- **GIVEN** una recepción de `SEDE 1` y odontólogos y sillones precargados en más de una sede
- **WHEN** recepción abre el formulario, elige una fecha y hora válidas y recorre los campos con el teclado
- **THEN** ve `SEDE 1`, sin selector, con foco visible y sin odontólogos ni sillones de otra sede; cambiar el contexto del servidor a otra recepción cambia la sede y sus opciones.

### Requirement: Creación de un turno

`POST /api/turnos` MUST recibir paciente ficticio, odontólogo, prestación e inicio local. El sillón depende de la prestación. El servidor MUST calcular el fin según la prestación y aplicar una pausa fija de 10 minutos. No requiere asistente ni permite elegir la sede al guardar.

El catálogo MUST informar la duración de cada prestación en minutos. El formulario MUST mostrar ese dato junto al nombre, sin permitir editarlo ni enviarlo al guardar.

#### Scenario: S02 — Creación válida con sillón

- **GIVEN** una prestación de 30 minutos y un odontólogo y un sillón de la sede disponibles a las 10:00
- **WHEN** recepción envía un turno válido para las 10:00
- **THEN** recibe `201`, se guarda un solo turno de 10:00 a 10:30 en su sede, con ocupación hasta las 10:40, y el formulario confirma la creación. La opción de prestación muestra los 30 minutos como dato de solo lectura.

#### Scenario: S03 — Radiografía sin sillón ni asistente

- **GIVEN** una prestación ficticia de radiografía y un odontólogo disponible y asignado a la franja
- **WHEN** recepción registra la cita sin sillón ni asistente
- **THEN** recibe `201`, se guarda la cita y la pausa ocupa solo al odontólogo.

### Requirement: Rechazo de entradas y recursos inválidos

La API MUST rechazar sin guardar datos inválidos, identificadores inexistentes y odontólogos o sillones ajenos a la sede activa. El error MUST incluir `codigo` y `mensaje` sin revelar datos de otros pacientes.

#### Scenario: S04 — Solicitud mal formada o incompleta

- **WHEN** falta un campo obligatorio, `inicioLocal` no cumple el formato, o se intenta enviar una sede en la solicitud
- **THEN** devuelve `400` con `DATOS_INVALIDOS` y no guarda un turno.

#### Scenario: S05 — Identificador inexistente

- **WHEN** la solicitud está bien formada pero refiere a un identificador obligatorio o a un sillón inexistente
- **THEN** devuelve `404` con `IDENTIFICADOR_INEXISTENTE` y no guarda un turno.

#### Scenario: S06 — Odontólogo o sillón de otra sede

- **GIVEN** una recepción de `SEDE 1` y recursos existentes en `SEDE 2`
- **WHEN** la solicitud contiene un odontólogo o un sillón de `SEDE 2`
- **THEN** devuelve `403` con `RECURSO_FUERA_DE_SEDE` y no guarda un turno.

### Requirement: Franjas de apertura y asignación profesional

El servidor MUST aceptar solo citas en días de apertura y en la franja de la sede asignada al odontólogo para ese día. Cada odontólogo puede tener como máximo una franja por día. La cita y su pausa MUST caber por completo en esa franja. La fecha y hora se interpretan en `America/Argentina/Buenos_Aires`.

#### Scenario: S07 — Sede cerrada

- **WHEN** recepción solicita una cita en sábado, domingo o una fecha de cierre precargada
- **THEN** devuelve `422` con `SEDE_CERRADA` y no guarda un turno.

#### Scenario: S08 — Odontólogo fuera de su franja asignada

- **GIVEN** una sede abierta por la mañana y por la tarde y un odontólogo asignado solo a la mañana ese día
- **WHEN** recepción intenta agendarlo a la tarde
- **THEN** devuelve `422` con `ODONTOLOGO_FUERA_DE_FRANJA` y no guarda un turno.

#### Scenario: S09 — Fin de pausa justo al cierre

- **GIVEN** recursos libres y un odontólogo asignado a la franja correspondiente
- **WHEN** una cita válida acaba a las 12:50 con cierre a las 13:00, o a las 20:50 con cierre a las 21:00
- **THEN** recibe `201` porque la pausa termina justo al cierre.

#### Scenario: S10 — Cita o pausa rebasa la franja

- **WHEN** la cita acaba a las 12:55 o a las 20:55, cruza el cierre, o empieza en el intervalo cerrado del mediodía
- **THEN** devuelve `422` con `CITA_O_PAUSA_FUERA_DE_FRANJA` y no guarda un turno.

### Requirement: Rechazo de solapamientos

El servidor MUST rechazar una cita si su intervalo de ocupación se cruza con el de otra cita del mismo odontólogo o del mismo sillón asignado. El intervalo incluye la pausa, pero no su extremo final. Si ambos recursos están ocupados, el servidor MUST informar primero el conflicto del odontólogo.

#### Scenario: S11 — Conflicto por odontólogo

- **GIVEN** un turno guardado con un odontólogo ocupado de 10:00 a 10:40 incluida su pausa
- **WHEN** recepción solicita con ese odontólogo una cita que empieza a las 10:20, aunque use otro sillón
- **THEN** devuelve `409` con `ODONTOLOGO_OCUPADO`, informa la franja sin datos del otro paciente y no guarda otro turno.

#### Scenario: S12 — Conflicto por sillón

- **GIVEN** dos odontólogos de la misma sede y un sillón ocupado hasta las 10:40 incluida la pausa
- **WHEN** el segundo odontólogo intenta usar ese sillón para una cita que empieza a las 10:20
- **THEN** devuelve `409` con `SILLON_OCUPADO` y no guarda otro turno.

#### Scenario: S13 — Conflicto en ambos recursos

- **WHEN** una cita se superpone con ocupaciones tanto del odontólogo como del sillón solicitado
- **THEN** devuelve `409` con `ODONTOLOGO_OCUPADO` y el formulario muestra el conflicto del odontólogo.

#### Scenario: S14 — Borde exacto de la pausa

- **GIVEN** un turno de 10:00 a 10:30 con pausa hasta las 10:40
- **WHEN** se pide el mismo recurso para las 10:30 o las 10:39, o se pide para las 10:40 en una prueba separada
- **THEN** se rechazan los dos primeros inicios y se acepta el último si cumple las demás reglas.

### Requirement: Creación concurrente sin doble guardado

Si dos solicitudes válidas por separado son incompatibles, el sistema MUST guardar exactamente una y rechazar la otra por conflicto, sin garantizar cuál gana. La verificación MUST ejecutarse contra PostgreSQL real.

#### Scenario: S15 — Solicitudes paralelas por el mismo odontólogo

- **GIVEN** un intervalo libre y dos solicitudes que comparten odontólogo, pero no sillón
- **WHEN** las solicitudes se envían en paralelo con intervalos de ocupación que se cruzan
- **THEN** una devuelve `201`, la otra devuelve `409` con `ODONTOLOGO_OCUPADO` y queda un solo turno guardado.

#### Scenario: S16 — Solicitudes paralelas por el mismo sillón

- **GIVEN** un intervalo libre y dos solicitudes con odontólogos distintos pero el mismo sillón
- **WHEN** las solicitudes se envían en paralelo con intervalos de ocupación que se cruzan
- **THEN** una devuelve `201`, la otra devuelve `409` con `SILLON_OCUPADO` y queda un solo turno guardado.

### Requirement: Resultado informado a recepción

El formulario MUST mostrar confirmación al recibir `201` o el motivo legible ante un rechazo conocido. Si no obtiene una respuesta concluyente, MUST informar que no pudo confirmar el guardado, sin mostrarlo como conflicto ni reintentar automáticamente.

#### Scenario: S17 — Rechazo con mensaje legible

- **WHEN** la API devuelve un rechazo de negocio con un código y mensaje
- **THEN** el formulario muestra el motivo sin indicar que la cita fue creada y sin exponer datos de otro paciente.

#### Scenario: S18 — Fallo técnico sin resultado confirmado

- **WHEN** el formulario no recibe respuesta o recibe un fallo técnico al solicitar la creación
- **THEN** avisa «No se pudo confirmar si el turno se guardó», no muestra un conflicto de agenda ni reenvía la solicitud automáticamente.

### Requirement: Anticipación máxima de un mes calendario

El servidor MUST admitir fechas desde el día actual hasta un mes calendario después, ambos inclusive, en `America/Argentina/Buenos_Aires`. Si el mes siguiente no tiene el mismo número de día, el límite es su último día. Las reglas de cierre, asignación, pausa y solapamiento siguen aplicando.

#### Scenario: S19 — Bordes inclusivos del plazo

- **GIVEN** una fecha actual conocida en Buenos Aires y solicitudes que cumplen las demás reglas
- **WHEN** se solicita un turno para hoy o justo para la fecha límite del mes siguiente
- **THEN** el plazo no impide crear ninguno de los dos turnos.

#### Scenario: S20 — Fecha anterior o posterior al plazo

- **WHEN** se solicita una cita para ayer, para el día posterior a la fecha límite o para un año lejano como 4000
- **THEN** el servidor responde `422` con `FECHA_FUERA_DE_PLAZO`, no guarda un turno y el formulario indica en español el rango permitido.

#### Scenario: S21 — Mes siguiente con menos días

- **GIVEN** que hoy es el 31 de enero en Buenos Aires
- **WHEN** el sistema calcula el límite para crear turnos
- **THEN** el último día de febrero (28 o 29, según el año) es el último permitido por el plazo; una fecha de marzo se rechaza por excederlo.

### Requirement: Odontólogos filtrados por fecha y hora

El formulario MUST pedir fecha y hora antes de habilitar Odontólogo. El catálogo MUST mostrar solo los profesionales de la sede asignados a la franja elegida en esa fecha, sin seleccionar ninguno automáticamente. Este filtro no confirma disponibilidad contra citas guardadas; el servidor la comprueba al crear el turno.

#### Scenario: S22 — Elegir franja de mañana o de tarde

- **GIVEN** una fecha hábil sin cierre y dos odontólogos de la sede, uno asignado a la mañana y el otro a la tarde
- **WHEN** recepción elige esa fecha y las 10:00 o, en una prueba separada, las 17:00
- **THEN** ve solo al odontólogo de la franja correspondiente y debe seleccionarlo manualmente.

#### Scenario: S23 — No hay odontólogos para el horario elegido

- **WHEN** la fecha es de cierre o la hora es 15:30, entre las franjas de apertura
- **THEN** el catálogo no ofrece odontólogos para ese inicio, el selector queda deshabilitado y el formulario explica que hay que elegir otra fecha u hora.

#### Scenario: S24 — Cambiar la fecha o la hora

- **GIVEN** un odontólogo ya seleccionado para una fecha y franja
- **WHEN** recepción cambia la fecha o la hora y la asignación deja de ser válida
- **THEN** se limpia el odontólogo seleccionado y se muestran solo las opciones correspondientes al nuevo inicio.

### Requirement: Uso de sillón según la prestación

El formulario MUST mantener Sillón bloqueado hasta elegir la prestación. Radiografía MUST registrarse sin sillón; las demás prestaciones del catálogo inicial MUST usar un sillón de la sede elegido por recepción. El servidor MUST validar ambas condiciones sin confiar en el formulario.

#### Scenario: S25 — El selector depende de la prestación

- **GIVEN** que no hay una prestación seleccionada y el selector de sillón está bloqueado
- **WHEN** recepción elige una prestación que usa sillón
- **THEN** puede elegir un sillón de su sede, ninguno queda preseleccionado y no puede guardar dejando el campo vacío.
- **WHEN** selecciona un sillón y luego cambia a Radiografía
- **THEN** se descarta el sillón anterior y se muestra «Sin sillón», bloqueado. Volver a una prestación que usa sillón requiere elegir uno nuevamente.

#### Scenario: S26 — Prestación que requiere sillón sin asignación

- **WHEN** la API recibe una solicitud por una prestación que requiere sillón, pero no trae un `sillonId` o lo trae como `null`
- **THEN** responde `422` con `SILLON_REQUERIDO` y no guarda un turno.

#### Scenario: S27 — Radiografía con sillón enviado

- **WHEN** la API recibe una solicitud de radiografía con un `sillonId`
- **THEN** responde `422` con `SILLON_NO_APLICA` y no guarda un turno.
