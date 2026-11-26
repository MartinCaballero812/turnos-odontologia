# Tareas — C-01

Referencias aprobadas: [diseño](design.md) y [especificación con escenarios S01–S27](specs/reserva-de-turnos/spec.md). La aprobación de la especificación y del mockup no sustituye la autorización para iniciar Apply.

## 1. Arranque y datos de demo

- [x] 1.1 Preparar Spring Boot, Java 17 y Gradle Wrapper; verificar que `./gradlew --version` y la compilación usen las versiones del diseño.
- [x] 1.2 Preparar HTML/CSS/TypeScript sin framework, con versiones y dependencias reproducibles; verificar que el frontend compila y que el artefacto se puede servir desde la aplicación local.
- [x] 1.3 Usar PostgreSQL 16.x y la conexión de demo ya verificada por TCP. Configurar la conexión desde Spring Boot, preparar el esquema y los datos ficticios, incluidas asignaciones por día/franja y una fecha de cierre, y preparar una base de pruebas aislada. Documentar el arranque, la carga inicial y la conexión en el README; verificar que se puede repetir la carga y que no se aceptan dos franjas para un odontólogo el mismo día.

## 2. Contexto y validación de la API

- [x] 2.1 Resolver el contexto mediante `APP_DEMO_RECEPCION_ID` y crear `GET /api/contexto` y `GET /api/catalogos` con filtro por inicio local. Automatizar la parte de API de S01, S22 y S23, incluido un cambio de contexto entre sedes, y documentar cómo seleccionar la recepción de la demo.
- [x] 2.2 Implementar la entrada de `POST /api/turnos`, validación de datos, referencias, uso de sillón por prestación y pertenencia a sede. Automatizar S04–S06 y S26–S27 y verificar que no se guarden turnos rechazados.
- [x] 2.3 Calcular duración y pausa y validar cierres, franjas y el plazo de un mes. Automatizar S07–S10 y S19–S21 con un reloj controlado, incluidos los bordes de mañana y tarde, hoy, la fecha límite, el día posterior y fin de mes.

## 3. Guardado y concurrencia

- [x] 3.1 Implementar el guardado transaccional con bloqueos por odontólogo y por sillón cuando corresponda, consulta posterior de ocupación y respuesta `201` tras confirmar la transacción. Automatizar el guardado con y sin sillón (S02–S03), conflictos independientes y prioridad del odontólogo (S11–S13) y bordes de pausa (S14).
- [x] 3.2 Automatizar S15 y S16 con solicitudes paralelas coordinadas y PostgreSQL real, en una base de prueba aislada. Verificar en cada caso `201`, `409` y un solo registro. Documentar el comando de estas pruebas y sus requisitos en el README.

## 4. Formulario de escritorio

- [x] 4.1 Implementar la pantalla de escritorio según el [mockup aprobado](mockups/turno.html) y conectar sede y catálogos. Automatizar sede fija y foco visible (S01), filtrado por fecha y hora (S22–S24) y comportamiento del selector de sillón según prestación (S25). Los controles de simulación no forman parte de la aplicación.
- [x] 4.2 Enviar el formulario y representar confirmación, rechazo y fallo técnico sin reenvío automático. Automatizar la confirmación y los mensajes para S02, S13, S17, S18 y S20 (rango permitido). Documentar cómo abrir la pantalla y cómo reproducir el ejemplo del 1/12/2026 con el reloj de pruebas.

## 5. Verificación integral

- [x] 5.1 Ejecutar la suite completa desde el README en un entorno local limpio, incluido PostgreSQL real y los casos de navegador. Registrar resultados por escenario, confirmar que la demo guarda solo un turno en cada prueba concurrente y verificar que las respuestas no exponen datos ajenos. Comparar la pantalla con el [mockup aprobado](mockups/turno.html) y [sus capturas](mockups/README.md#capturas-inspeccionadas--25112026); la vista angosta solo comprueba el diseño, no añade alcance móvil. Ver [evidencia de Verify](verify.md).

## Workflow follow-up

- Archivar solo después de que pasen todas las verificaciones y se cumplan las revisiones del proyecto. Verificar el archivo, actualizar la ficha de C-01 y redirigir los enlaces del roadmap, la base de conocimiento y la base visual que apuntaban al change activo.
- **Cierre:** el 26/11/2026 se archivó este change tras Verify y se sincronizó la [especificación vigente](../../../specs/reserva-de-turnos/spec.md). El [roadmap](../../../../CHANGES.md#c-01-crear-turno-sin-solapamientos) y el [estado del proyecto](../../../../.active-orchestrator-state.json) registran el cierre de la Etapa 6.
