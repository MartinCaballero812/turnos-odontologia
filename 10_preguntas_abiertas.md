# Preguntas abiertas

La KB registra dudas en lugar de resolverlas por deducción. No todas bloquean el primer change.

## Prioridad alta — Antes de aprobar el diseño del primer change

| Pregunta | Por qué importa | Cómo resolverla |
|---|---|---|
| ¿Qué versiones compatibles de JDK, Spring Boot, Gradle, PostgreSQL y herramientas TypeScript se usarán? | Instalación y tests repetibles. | Seleccionar versiones con el equipo y anotarlas en el README al implementar. |
| ¿Cómo se acredita al usuario de recepción de la demo? | Evita aceptar cualquier sede impuesta por el navegador. | Definir inicio de sesión mínimo o un contexto local preconfigurado que valide el servidor. |
| ¿Qué días atienden las sedes, hay feriados y qué zona horaria se usa? | Completa la interpretación de franjas. Los horarios dentro de un día ya están definidos. | Confirmar los días de la demo y documentar las excepciones fuera de alcance si corresponde. |
| ¿Qué mecanismo usará la API para proteger la agenda y qué códigos de error devolverá? | Ya se decidió rechazar los conflictos y explicar qué recurso está ocupado y en qué franja, sin exponer datos del otro paciente. También está confirmada la protección contra solicitudes simultáneas incompatibles. | Definir el mecanismo y los códigos en la especificación. Verificar el resultado con la prueba de integración con PostgreSQL. |

## Prioridad media — Para los demás cambios del MVP

| Pregunta | Impacto |
|---|---|
| ¿Cómo ve el asistente un turno sin sillón y a qué se lo asigna: sede, odontólogo o sillón? | Afecta su vista y alcance de acceso; no bloquea la creación de un turno sin sillón. |
| ¿Cómo se registra que la cita terminó o fue atendida? | Puede afectar estados e historial. Para el MVP la ocupación sigue el horario programado; no se modifica automáticamente por una atención más larga o corta. |
| ¿Debería tener edición el asistente en una versión posterior? | Hoy es solo consulta; cualquier ampliación requiere una decisión. |
| ¿Qué integraciones se necesitan, si alguna? | No hay ninguna confirmada ni necesaria para el primer change. |

## Investigación pendiente o datos «No evidenciados»

- El informe no confirma una tasa argentina representativa de ausencias, conflictos de agenda ni carga administrativa. Validar en un piloto antes de estimar beneficios.
- La combinación exacta de bloqueo por odontólogo, sillón opcional y pausa no quedó comprobada en los competidores relevados. Si se retoma la comparación, usar evidencia pública o demos públicas con datos ficticios; no inferir que carecen de la función.
- Disponibilidad por plan de integraciones, costos reales de tres sedes y portabilidad integral de datos no son uniformemente públicos. Solo reevaluarlos si alguna integración pasa a ser candidata posterior al MVP.
- Antes de producción con pacientes reales, revisar prácticas de trabajo, privacidad y requisitos legales aplicables. No es requisito para la demostración con datos ficticios.

## Decisiones ya resueltas

- Una empresa, tres sedes iniciales y entrega local.
- Pausa fija de 10 minutos; cita y pausa en la misma franja.
- Primer change con interfaz gráfica; no incluye reprogramación ni cancelación.
- **Concurrencia:** la protección ante solicitudes simultáneas incompatibles y su prueba de integración se incluyen en el primer change (DD-06). La justificación es el doble envío o reintento desde recepción; el [checklist](../docs/discovery/checklist-discovery.json) indica una persona de recepción por sede.
- **Administrador:** el equipo confirmó que en el MVP configura sedes, sillones, profesionales, prestaciones y usuarios de todas las sedes, pero no gestiona turnos. El alcance se basa en el checklist y coincide con la recomendación actualizada del informe.
- **Sillón:** siempre es opcional en el MVP (RN-01). Si se quiere exigir para determinadas prestaciones, se decide después del MVP.
