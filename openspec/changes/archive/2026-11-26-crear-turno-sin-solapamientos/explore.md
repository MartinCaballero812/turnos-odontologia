# Explore — C-01: Crear turno sin solapamientos

**Fecha:** 25/11/2026

> **Registro histórico de Explore.** Después de este relevamiento se definió el plazo de un mes, el orden fecha/hora → odontólogo y el uso de sillón según prestación. La opcionalidad general del sillón quedó reemplazada: radiografía no usa sillón y las demás prestaciones iniciales requieren uno. Para implementar, usá la [especificación actual](specs/reserva-de-turnos/spec.md) y el [diseño](design.md), no el contrato inicial de esta nota.

## Problema y estado del repositorio

Recepción registra la cita con un odontólogo y, si corresponde, le asigna un sillón. El servidor verifica la disponibilidad del odontólogo y, si se asignó un sillón, también la de ese recurso. La verificación incluye la cita y su pausa, incluso si llegan dos solicitudes incompatibles al mismo tiempo.

Al iniciar Explore, el repositorio tenía la fundación, el informe de Discovery, la base de conocimiento y el roadmap. OpenSpec estaba inicializado y no tenía changes activos ni especificaciones vigentes. No se encontró código ni pruebas del producto que hubiera que adaptar.

## Alcance al cerrar Explore

- C-01 es el único change de la entrega académica y no depende de C-02 a C-10.
- Recepción carga el turno en un formulario mínimo con una sede preasignada para la demo. No hay selector de sede y solo se ofrecen odontólogos y sillones de esa sede. El servidor determina la sede, valida la pertenencia de los recursos y calcula la duración por prestación. El formulario confirma la creación o explica el rechazo sin exponer datos de otro paciente.
- El odontólogo es obligatorio; el sillón es opcional y la disponibilidad de asistentes no condiciona la reserva. La cita y la pausa de 10 minutos ocupan al odontólogo y al sillón si se asignó, y deben caber en la misma franja de apertura de la sede y de trabajo del odontólogo.
- La sede abre de lunes a viernes en dos franjas: 08:30–13:00 y 16:00–21:00, salvo feriados y otros cierres registrados. Cada odontólogo tiene como máximo una franja laboral por día y puede alternarla según el día. En C-01, estas asignaciones y una fecha de cierre son datos precargados.
- Si llegan dos solicitudes simultáneas válidas por separado pero incompatibles entre sí, se guarda solo una. El resultado se debe verificar con PostgreSQL real.
- La demostración es local y usa datos ficticios precargados. No incluye cuentas completas, edición de horarios o catálogos, calendario automático de feriados, planificación de asistentes, cancelación, reprogramación, reasignación ni reserva pública.

## Escenarios a llevar a Propose

| Caso | Resultado esperado |
|---|---|
| Turno válido dentro de la franja | Se guarda y el formulario lo confirma. |
| Solapamiento con el mismo odontólogo o el mismo sillón | Se rechaza; probar por separado cada recurso. |
| Turno sin sillón | Es válido si el odontólogo está disponible y se cumplen las demás reglas. |
| Turno sin asistente | No se rechaza por falta de asistente. |
| Odontólogo no asignado a la franja solicitada | Se rechaza aunque la sede esté abierta. |
| Turno en sábado, domingo o fecha de cierre precargada | Se rechaza porque la sede está cerrada. |
| Inicio al terminar la cita o al terminar su pausa | Durante la pausa se rechaza; al terminar la pausa se permite si se cumplen las demás reglas. |
| Cierre de franja a las 13:00 | Una cita que termina a las 12:50 cabe con la pausa; una que termina a las 12:55 no. |
| Cierre de franja a las 21:00 | Una cita que termina a las 20:50 cabe con la pausa hasta las 21:00; una que termina a las 20:55 se rechaza porque la pausa llegaría hasta las 21:05. |
| Recepción opera en su única sede | El formulario solo muestra odontólogos y sillones de esa sede, sin opción para cambiarla. El turno válido se guarda en la sede preasignada. |
| Validación interna de pertenencia de recursos | El servidor no guarda un turno con un odontólogo o sillón de otra sede. No es una opción del formulario. |
| Dos solicitudes simultáneas incompatibles | Una tiene éxito, la otra recibe un conflicto y queda un solo turno guardado. |

## Diseño acordado para llevar a Propose

- **Demo:** martes 1 de diciembre de 2026 como fecha fija de los datos ficticios, en `America/Argentina/Buenos_Aires`, no como fecha de entrega. Las tres sedes usan las mismas franjas iniciales de apertura. La fecha fija permite repetir el escenario y se complementa con fechas de prueba de fines de semana y un cierre precargado.
- **Identidad de recepción:** al arrancar, el servidor selecciona una recepción ficticia mediante `APP_DEMO_RECEPCION_ID`. La sede se obtiene de ese contexto, nunca del formulario. La demo se ejecuta solo en la computadora local y no incluye autenticación ni administración de cuentas.
- **Concurrencia:** usar una transacción PostgreSQL con aislamiento `READ COMMITTED`; bloquear primero la fila del odontólogo y después la del sillón asignado, si existe. Ejecutar la consulta de ocupación después de adquirir los bloqueos y guardar en la misma transacción. La ocupación va desde el inicio hasta el fin de la cita más 10 minutos, sin incluir el extremo final. Con PostgreSQL real, enviar dos solicitudes paralelas válidas por separado pero incompatibles y verificar una creación, un conflicto y un solo registro, sin exigir que gane la que se envió primero. Ver [arquitectura](../../../../knowledge-base/08_arquitectura_propuesta.md#concurrencia-de-la-agenda).
- **Pantalla de escritorio:** no se diseñan vistas para celular ni tablet. El formulario muestra la sede asignada (`SEDE 1`, `SEDE 2` o `SEDE 3`) y permite cargar paciente ficticio, odontólogo, prestación, fecha, hora y sillón opcional. Fondo claro, texto oscuro, acento verde petróleo (`#0F766E`), errores legibles y foco visible por teclado. La identidad visual definitiva queda para más adelante.

### Contrato inicial de la API

| Operación | Comportamiento |
|---|---|
| `GET /api/contexto` | Devuelve la sede y la recepción activas para mostrar en la pantalla. |
| `GET /api/catalogos` | Devuelve las opciones válidas para la sede activa. |
| `POST /api/turnos` | Recibe `pacienteId`, `odontologoId`, `prestacionId`, `inicioLocal` y `sillonId` opcional. No recibe la sede. El servidor obtiene la duración por prestación y calcula el fin y la pausa. |

Respuestas de creación: `201` turno creado; `400` datos incompletos o mal formados; `403` recurso ajeno a la sede; `404` identificador inexistente; `409` odontólogo o sillón ocupado; `422` regla de horario incumplida (sede cerrada, odontólogo fuera de la franja laboral o cita/pausa fuera de la franja). Los rechazos llevan un código estable y un mensaje claro sin exponer datos de otro paciente. Si coinciden ambos conflictos de ocupación, se informa primero el del odontólogo. Una pérdida de conexión o respuesta no se presenta como un `409` ni como prueba de que el turno no se guardó.

## Decisiones que pasaron de Explore a Propose

- **Java de referencia:** usar Java 17, ya instalado en la Mac (Temurin `17.0.20.1`), sin requerir una actualización del JDK. En Propose se fijarán versiones de Spring Boot y Gradle Wrapper que funcionen con Java 17. La versión del servidor PostgreSQL se confirmará por separado y deberá ser compatible con el controlador JDBC del proyecto. El entorno local también tiene Node `24.19.0`, TypeScript `7.0.2` y npm `11.17.0`.
- Completar en Propose los cuerpos de respuesta, los nombres exactos de los códigos estables, la distinción entre motivos de `422` y el tratamiento de fallos técnicos.
- Resolver la política visual del proyecto antes de implementar la pantalla: la apariencia inicial y el alcance de escritorio están definidos, pero la política de sistema visual y mockups sigue diferida.

**Referencias:** [C-01 en el roadmap](../../../../CHANGES.md#c-01-crear-turno-sin-solapamientos), [reglas de agenda](../../../../knowledge-base/05_reglas_de_negocio.md#agenda-y-recursos), [US-001](../../../../knowledge-base/06_funcionalidades.md#us-001--crear-un-turno-sin-solapamientos-primer-change) y [preguntas abiertas](../../../../knowledge-base/10_preguntas_abiertas.md).
