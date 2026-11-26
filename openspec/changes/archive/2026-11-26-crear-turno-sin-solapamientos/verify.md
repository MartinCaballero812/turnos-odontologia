# Verify — C-01

**Resultado:** aprobado el 25/11/2026. Se ejecutó `./gradlew --no-daemon clean build postgresTest browserTest` con Java 17, PostgreSQL 16.15 y Chromium de Playwright. Pasaron 2 pruebas de la suite base, 11 de PostgreSQL y 9 de navegador. La configuración inicial y los comandos están en el [README](../../../../README.md#suite-de-verificación-con-postgresql-real-y-chromium).

Los casos HTTP y de navegador levantaron Spring Boot en puertos locales libres, con el perfil `demo` apuntado a esquemas temporales en `odonclick_test`. No se escribió en `odonclick_demo`. Los casos del 1/12/2026 usaron un reloj fijo para evitar depender de la fecha de ejecución.

## Cobertura de la especificación

**Tipos de escenario que pide la consigna (pág. 6):**

- **Caso exitoso — S02:** el odontólogo y el sillón están libres; se guarda el turno.
- **Error de negocio — S11:** el odontólogo está ocupado; se rechaza el turno aunque se elija otro sillón.
- **Caso de borde — S14:** un turno a las 10:39 se rechaza; a las 10:40, justo al terminar la pausa, se admite si cumple las demás reglas.

La columna **Tipo** indica el objetivo principal de cada escenario. Un mismo caso puede verificar más de un comportamiento.

| Escenario | Tipo | Evidencia automatizada | Resultado |
|---|---|---|---|
| S01 — Sede fija | Interacción del formulario | HTTP: contexto y catálogos de dos sedes. Chromium: formulario sin selector y foco visible con Tab. | Pasa |
| S02 — Turno con sillón | Caso exitoso | HTTP: `201` y fila persistida de 10:00 a 10:30, ocupada hasta 10:40. Chromium: duración informativa, envío sin duración y comprobante. | Pasa |
| S03 — Radiografía | Caso exitoso | HTTP: se guarda sin sillón, bloquea el odontólogo pero no el sillón. | Pasa |
| S04 — Datos inválidos | Error de entrada | HTTP: JSON, campos, tipos, hora y sede inválidos se rechazan sin insertar. | Pasa |
| S05 — Identificador inexistente | Error de referencia | HTTP: paciente, odontólogo, prestación o sillón inexistentes devuelven `404`. | Pasa |
| S06 — Otra sede | Error de autorización | HTTP: odontólogo y sillón de otra sede probados por separado (`403`). | Pasa |
| S07 — Cierres | Error de negocio | HTTP: sábado, domingo y cierre registrado rechazados. | Pasa |
| S08 — Fuera de asignación | Error de negocio | HTTP: odontólogo de mañana rechazado en franja de tarde. | Pasa |
| S09 — Fin de pausa exacto | Caso de borde | HTTP: la pausa termina exactamente a las 13:00 o a las 21:00 y se admite. | Pasa |
| S10 — Fuera de franja | Error de negocio | HTTP: corte de mediodía y cita o pausa fuera de franja rechazados. | Pasa |
| S11 — Odontólogo ocupado | Error de negocio | HTTP: `409`, franja ocupada y sin campos de paciente ajeno. | Pasa |
| S12 — Sillón ocupado | Error de negocio | HTTP: dos odontólogos distintos, mismo sillón; `409` por sillón. | Pasa |
| S13 — Ambos ocupados | Error de negocio | HTTP: el odontólogo tiene prioridad. Chromium: mensaje de conflicto sin datos del paciente que ya tenía turno. | Pasa |
| S14 — Borde de pausa | Caso de borde | HTTP: rechaza 10:30 y 10:39; admite 10:40. | Pasa |
| S15 — Envíos simultáneos por odontólogo | Concurrencia | PostgreSQL: se observaron dos conexiones esperando el bloqueo; un `201`, un `409` por odontólogo, una fila. | Pasa |
| S16 — Envíos simultáneos por sillón | Concurrencia | PostgreSQL: se observaron dos conexiones esperando el bloqueo; un `201`, un `409` por sillón, una fila. | Pasa |
| S17 — Rechazo legible | Comunicación de un error de negocio | Chromium: aviso de franja/pausa y foco en Hora, sin comprobante de guardado. | Pasa |
| S18 — Resultado incierto | Fallo técnico | Chromium: pérdida de respuesta y `500` probados por separado; no se afirma guardado ni se reenvía. | Pasa |
| S19 — Plazo inclusivo | Caso de borde | HTTP: acepta hoy y la fecha límite con reloj de Buenos Aires. | Pasa |
| S20 — Fuera del plazo | Error de negocio | HTTP: `422`, rango legible y cero registros. Chromium: límites del calendario y error de campo en español. | Pasa |
| S21 — Fin de mes | Caso de borde | HTTP: 29 de febrero válido y marzo fuera del plazo. Prueba unitaria: 28 de febrero en año no bisiesto. | Pasa |
| S22 — Mañana y tarde | Interacción del formulario | HTTP y Chromium: cada franja devuelve solo su profesional; no hay preselección. | Pasa |
| S23 — Sin odontólogos | Interacción del formulario | HTTP y Chromium: cierre y 15:30 no ofrecen opciones; la interfaz pide otra fecha u hora. | Pasa |
| S24 — Cambio de fecha/hora | Interacción del formulario | Chromium: conserva una selección que sigue vigente y limpia la que deja de corresponder. | Pasa |
| S25 — Sillón según prestación | Interacción del formulario | Chromium: selector bloqueado al abrir, elección obligatoria cuando aplica y radiografía sin sillón. | Pasa |
| S26 — Sillón requerido | Error de negocio | HTTP: silla ausente o `null` rechazada y cero registros. | Pasa |
| S27 — Sillón no aplica | Error de negocio | HTTP: identificador de sillón para radiografía rechazado y cero registros. | Pasa |

El arranque y la carga repetible del esquema también pasaron. La comprobación de salud del formulario abrió la página y cargó los recursos y catálogos por HTTP sin errores de consola ni peticiones fallidas en el recorrido normal. Las pruebas de fallo técnico sí provocaron errores de red/HTTP deliberadamente.

## Comparación visual

Referencia: [mockup aprobado](mockups/README.md), sistema visual revisión 1. Se inspeccionaron los ocho estados de la única pantalla en escritorio y ventana angosta; sin desborde horizontal. La ventana angosta no agrega soporte móvil al alcance. Otros navegadores y dispositivos no se probaron.

| Estado | Escritorio 1440 × 900 | Ventana 390 × 844 |
|---|---|---|
| Inicial | [Ver](mockups/verify/inicio-1440.png) | [Ver](mockups/verify/inicio-390.png) |
| Tarde | [Ver](mockups/verify/tarde-1440.png) | [Ver](mockups/verify/tarde-390.png) |
| Datos inválidos | [Ver](mockups/verify/datos-1440.png) | [Ver](mockups/verify/datos-390.png) |
| Turno guardado | [Ver](mockups/verify/guardado-1440.png) | [Ver](mockups/verify/guardado-390.png) |
| Radiografía guardada | [Ver](mockups/verify/radiografia-1440.png) | [Ver](mockups/verify/radiografia-390.png) |
| Conflicto | [Ver](mockups/verify/conflicto-1440.png) | [Ver](mockups/verify/conflicto-390.png) |
| Horario inválido | [Ver](mockups/verify/horario-1440.png) | [Ver](mockups/verify/horario-390.png) |
| Resultado incierto | [Ver](mockups/verify/incierto-1440.png) | [Ver](mockups/verify/incierto-390.png) |

**Sin diferencias bloqueantes o mayores.** Las diferencias observadas fueron intencionales o menores:

| Prioridad y estado | Antes: mockup | Después: aplicación | Cómo comprobarlo |
|---|---|---|---|
| Esperada · todos · ambas vistas | Barra de simulación y pie de revisión. | Sin controles de simulación. El pie identifica la demo local. | Abrir el formulario por primera vez. |
| Menor · datos inválidos · ambas | Paciente y prestación completos en la captura. | En el caso capturado, también están vacíos y se resaltan. | Ingresar la fecha y hora inválidas y dejar los otros campos vacíos. |
| Menor · conflicto y horario · ambas | Fechas y mensajes simulados; la captura de horario usa 15:30. | Mensajes reales y datos de prueba; a las 12:25 hay profesional asignado, pero una cita de 30 minutos y su pausa no caben. A las 15:30 el selector queda bloqueado, como se comprobó en S23. | Crear un turno y enviar otro incompatible; o probar un control odontológico a las 12:25. |

Las capturas se pueden regenerar con `ODONCLICK_VERIFY_DIR` apuntando a una carpeta de salida absoluta antes de ejecutar `browserTest`.
