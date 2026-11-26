# Diseño — C-01

## Context

El repositorio no tiene implementación previa. Los datos de demostración son ficticios y el único punto de entrada de negocio es el formulario local de recepción. Ver [propuesta](proposal.md), [Explore](explore.md) y [especificación](specs/reserva-de-turnos/spec.md).

## Goals / Non-Goals

- **Objetivos:** arranque local reproducible, validación centralizada en servidor y un guardado atómico que preserve la agenda aun con solicitudes simultáneas.
- **Fuera del diseño:** sincronización de calendarios, login real, configuración de horarios desde la UI e infraestructura remota. Tampoco se implementa reconciliación automatizada para solicitudes cuyo resultado se desconoce.

## Decisions

### Entorno local y versiones

| Componente | Versión propuesta en este diseño |
|---|---|
| JDK | Java 17 (Temurin `17.0.20.1` verificado como entorno local) |
| Spring Boot | `3.5.16`; usar su gestión de dependencias para Hibernate y el driver JDBC |
| Gradle | Wrapper `8.14.8`, sin instalación global |
| PostgreSQL | Servidor **16.x** para la demo y las pruebas; conexión a **16.15** verificada en el entorno local |
| Frontend | HTML/CSS y TypeScript `7.0.2` fijado en el proyecto; Node `24.19.0` y npm `11.17.0` como entorno reproducible |

Mantener Java 17 evita cambiar el entorno del equipo. [Spring Boot 3.5 admite Java 17 y Gradle 8.x](https://github.com/spring-projects/spring-boot/blob/v3.5.16/spring-boot-project/spring-boot-docs/src/docs/antora/modules/ROOT/pages/system-requirements.adoc). El frontend compilado se sirve desde la misma aplicación y origen local que la API, en lugar de mantener dos servidores para la demo. El README especificará los comandos reproducibles de frontend, backend y base de pruebas.

Para la demo ya se verificó una consulta SQL por TCP a `127.0.0.1:5432` con el rol no administrador `odonclick_app` y la base `odonclick_demo`. La conexión desde Spring Boot y la base aislada para las pruebas quedan para Apply.

### Contexto de recepción y agenda

- Usar un perfil de demo en loopback, con un registro ficticio de recepción y su sede seleccionados en el arranque mediante `APP_DEMO_RECEPCION_ID`. Fallar al iniciar la demo si el ID está ausente o no corresponde a una recepción válida. No aceptar una sede del cliente ni desplegar este contexto como autenticación real.
- Precargar tres sedes, recursos ficticios, las franjas de apertura y las asignaciones de odontólogos por día de la semana. Una asignación referencia una franja de su propia sede y se permite como máximo una por odontólogo y día de semana. No se duplican horas de apertura por odontólogo. Usar el martes **1 de diciembre de 2026** como fecha de datos de demo, en `America/Argentina/Buenos_Aires`; incluir además una fecha hábil de cierre y fechas de fin de semana para pruebas.
- Mantener en servidor la fecha de cierre aplicable, la pertenencia a sede, la duración por prestación y los límites de franja. Obtener la fecha actual con un reloj del servidor en `America/Argentina/Buenos_Aires` y calcular el extremo incluyente con un mes calendario (por ejemplo, `LocalDate.plusMonths(1)`, que ajusta un día inexistente al fin de mes). Usar un reloj controlado en pruebas, no la fecha de ejecución, para repetir los escenarios con datos ficticios del 1/12/2026. En la aplicación en ejecución se usa la fecha actual, no la fecha fija de la demo.

### Transacción y concurrencia

Dentro de una transacción `READ COMMITTED`, adquirir un bloqueo exclusivo de fila de agenda sobre el odontólogo y luego el del sillón cuando exista (`SELECT ... FOR UPDATE` o equivalente). No bloquear reservas existentes como único mecanismo: puede no haber ninguna todavía. Usar siempre el mismo orden y mantener los bloqueos hasta confirmar o revertir.

Consultar ocupación **en una sentencia posterior a los bloqueos**, dentro de la misma transacción. Dos intervalos `[inicio, finPausa)` se superponen si `inicioNuevo < finPausaExistente` e `inicioExistente < finPausaNuevo`. Consultar primero el odontólogo y después el sillón, solo si está asignado; si no hay conflicto, insertar y confirmar antes de responder `201`. Una solicitud que esperó un bloqueo consulta tras obtenerlo, con una vista que ya incluye el turno confirmado por la otra solicitud.

Una lectura de disponibilidad sin bloqueos dejaría una ventana entre validar y guardar. Otra alternativa serían restricciones de exclusión de PostgreSQL; no se eligen para C-01 porque el orden de bloqueos ya acordado permite identificar el recurso ocupado y comprobarlo por separado. Ver [bloqueos de filas](https://www.postgresql.org/docs/16/explicit-locking.html) y [aislamiento READ COMMITTED](https://www.postgresql.org/docs/16/transaction-iso.html#XACT-READ-COMMITTED).

### Contrato HTTP

Todos los cuerpos de aplicación son JSON. `inicioLocal`, `finLocal` y `finPausaLocal` se expresan como fecha y hora ISO local sin offset (p. ej., `2026-12-01T10:00:00`); el servidor aplica la zona de la demo. Los identificadores son opacos para la interfaz y se devuelven en el mismo formato que los catálogos. Los tres endpoints solo se usan en el perfil local.

| Operación | Cuerpo |
|---|---|
| `GET /api/contexto` (`200`) | `sede: { id, nombre }`, `recepcion: { id, nombre }`, `zonaHoraria`. |
| `GET /api/catalogos` (`200`) | `pacientes`, `odontologos`, `prestaciones`, `sillones`: listas de opciones `{ id, nombre }` de la sede activa; las prestaciones incluyen `usaSillon` y `duracionMinutos` (solo informativa, para mostrarla junto al nombre). Con `inicioLocal`, `odontologos` contiene solo quienes tienen asignada la franja de ese día y hora. Sin `inicioLocal`, o cuando no hay franja abierta, asignaciones o una fecha dentro del plazo, `odontologos` queda vacío. Un parámetro mal formado recibe `400`. Los pacientes son opciones ficticias, no un modelo multi-sede de pacientes. |
| `POST /api/turnos` (entrada) | `pacienteId`, `odontologoId`, `prestacionId`, `inicioLocal` y `sillonId` según la prestación. Radiografía acepta ausencia o `null`; las demás prestaciones iniciales exigen un identificador de sillón. No recibe `sedeId`, asistente, duración ni fin. |
| `POST /api/turnos` (`201`) | `id`, `sedeId`, `pacienteId`, `odontologoId`, `prestacionId`, `sillonId` (`null` si no hay), `inicioLocal`, `finLocal`, `finPausaLocal`. |

Los rechazos devuelven `{ codigo, mensaje }`. Los conflictos agregan `recurso` (`ODONTOLOGO` o `SILLON`), `inicioOcupadoLocal` y `finOcupadoLocal`, sin datos de pacientes de otros turnos.

| HTTP | Código | Cuándo se usa |
|---|---|---|
| `400` | `DATOS_INVALIDOS` | Faltan datos obligatorios, tienen formato incorrecto o se envía una sede. |
| `404` | `IDENTIFICADOR_INEXISTENTE` | No existe la referencia solicitada. |
| `403` | `RECURSO_FUERA_DE_SEDE` | El odontólogo o sillón existe pero es ajeno a la sede activa. |
| `422` | `SILLON_REQUERIDO` | Falta sillón para una prestación que lo requiere. |
| `422` | `SILLON_NO_APLICA` | Se envía un sillón para una radiografía. |
| `422` | `FECHA_FUERA_DE_PLAZO` | La fecha es anterior a hoy o posterior a un mes calendario desde hoy, en hora de Buenos Aires. El mensaje indica las fechas permitidas. |
| `422` | `SEDE_CERRADA` | Sábado, domingo o fecha registrada como cierre. |
| `422` | `ODONTOLOGO_FUERA_DE_FRANJA` | El inicio cae en una franja abierta no asignada al odontólogo ese día. |
| `422` | `CITA_O_PAUSA_FUERA_DE_FRANJA` | No hay franja de apertura en ese horario, o cita y pausa no caben completas. |
| `409` | `ODONTOLOGO_OCUPADO` / `SILLON_OCUPADO` | Conflicto de ocupación. Se prioriza el odontólogo si se dan ambos. |
| `500` | `ERROR_TECNICO` | El servidor puede devolver un error inesperado, sin detalles internos. |

Validar cuerpo y referencias antes de la agenda. Aplicar `usaSillon` leyendo la prestación en el servidor, no un valor enviado por el cliente. Verificar el plazo de reserva antes de los cierres, la franja y la ocupación. Si hay pérdida de respuesta o un fallo técnico, el formulario avisa que no se pudo confirmar el guardado y no reenvía automáticamente: una respuesta perdida no prueba que la transacción se haya revertido.

### Pantalla de escritorio

Formulario único con sede visible (`SEDE 1`, `SEDE 2` o `SEDE 3`), campos acordados y confirmación o error legible. La sede se muestra desde el contexto; los campos del turno arrancan vacíos y las opciones precargadas no se preseleccionan. Sillón permanece bloqueado hasta elegir la prestación. Radiografía muestra «Sin sillón» y no envía un identificador; si había una selección, se borra. Las demás prestaciones requieren elegir un sillón del catálogo de la sede (Sillón 1 o 2 en el ejemplo). Esta regla reemplaza la opcionalidad general del planteo inicial de [Explore](explore.md). La referencia es fondo claro, texto oscuro, acento `#0F766E` y foco visible; sin variantes para celular o tablet. Los mensajes no dependen solo del color.

La fecha se elige con un calendario limitado al plazo de reserva; sigue vacía al abrir el formulario. La hora se ingresa aparte en formato `HH:MM` (24 horas). Los errores de entrada se muestran en español debajo del campo. Si la fecha queda fuera de plazo, se informa el rango permitido. Al intentar guardar, el foco pasa al primer campo que hay que corregir. Esto no reemplaza la validación del servidor.

Mostrar fecha y hora antes de Odontólogo. Al tener un inicio válido, actualizar sus opciones con el catálogo de esa franja y conservar una selección previa solo si sigue siendo válida. No preseleccionar aunque quede una sola opción. Ignorar respuestas del catálogo para un inicio que ya cambió. Para la demo visual del 1/12/2026, Lucía Ferrer está asignada únicamente a la mañana y Julián Soria únicamente a la tarde. Las pruebas de conflicto por sillón usan datos aislados con dos profesionales en una misma franja. La lista no promete que no haya un solapamiento: eso se verifica al guardar.

## Risks / Trade-offs

- **Falsos positivos en la prueba de carrera** → ejecutar las dos solicitudes contra PostgreSQL real, en conexiones de servidor distintas y con inicio coordinado, y comprobar el resultado persistido. Hacer un caso por odontólogo y otro por sillón.
- **Desajuste de entorno** → fijar Wrapper y dependencias del frontend, usar PostgreSQL 16.x en la demo y las pruebas, y documentar una base de prueba aislada con datos ficticios. No guardar credenciales en el repositorio.
- **Incertidumbre tras perder la respuesta** → distinguir fallos técnicos de rechazos de negocio; sin reenvío automático.

## Migration Plan

No hay datos reales ni código anterior que migrar. Crear el esquema local y cargar los datos ficticios de manera reproducible; la base de pruebas se prepara y limpia de forma aislada sin reutilizar una base con datos del usuario.

## UI Mockups

- **Revisión de Propose:** aprobada inicialmente el 25/11/2026. El usuario también aprobó el 25/11/2026 los ajustes S19–S27: plazo de un mes, filtro de odontólogos y sillón según prestación. Esta aprobación no autoriza Apply.
- **Ajuste de integración aprobado:** incluir `duracionMinutos` en el catálogo para reproducir el selector del mockup. No se envía ni se edita la duración al guardar; no cambia el cálculo del servidor ni la referencia visual aprobada.
- **Política:** `system-and-mockups`, confirmada el 25/11/2026.
- **Design system revision:** 1, [aprobada por el usuario el 25/11/2026](../../../../design-system/README.md).
- **Decision:** yes (25/11/2026).
- **Mockups:** [`mockups/`](mockups/README.md), [formulario de escritorio](mockups/turno.html). Fuentes: [especificación](specs/reserva-de-turnos/spec.md) y base visual aprobada.
- **Approval:** approved (2026-11-25, by user).
- **Preview:** rendered (25/11/2026, [capturas de los ocho estados](mockups/README.md#capturas-inspeccionadas--25112026)).
- **Visual verify:** passed (25/11/2026, [comparación y capturas](verify.md#comparación-visual)).
