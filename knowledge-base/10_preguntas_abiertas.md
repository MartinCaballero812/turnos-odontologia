# Preguntas abiertas

La KB registra dudas en lugar de resolverlas por deducción. No todas bloquean el primer change.

## Cierre de C-01

La conexión desde Spring Boot, la base de pruebas aislada y la autorización de Apply quedaron resueltas. C-01 está archivado; los [resultados de Verify](../openspec/changes/archive/2026-11-26-crear-turno-sin-solapamientos/verify.md) documentan los 27 escenarios comprobados.

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

Incluye las preguntas de prioridad alta relevadas al inicio de C-01. Su diseño ya documenta las decisiones; no hace falta resolverlas otra vez.

- Una empresa, tres sedes iniciales y entrega local.
- Pausa fija de 10 minutos; cita y pausa en la misma franja.
- Primer change con interfaz gráfica; no incluye reprogramación ni cancelación.
- **Concurrencia:** la protección ante solicitudes simultáneas incompatibles y su prueba de integración se incluyen en el primer change (DD-06). La justificación es el doble envío o reintento desde recepción; el [checklist](../docs/discovery/checklist-discovery.json) indica una persona de recepción por sede.
- **Administrador:** el equipo confirmó que en el MVP configura sedes, sillones, profesionales, prestaciones y usuarios de todas las sedes, pero no gestiona turnos. El alcance se basa en el checklist y coincide con la recomendación actualizada del informe.
- **Sillón:** en C-01, radiografía no admite sillón y las demás prestaciones del catálogo inicial lo requieren (RN-01, DD-07). No se configura esta regla desde el formulario.
- **Recepción para la demo:** contexto local ficticio, seleccionado por `APP_DEMO_RECEPCION_ID`, con sede validada por el servidor; no incluye login.
- **Calendario de la demo:** martes 1 de diciembre de 2026 como fecha de datos de ejemplo, zona `America/Argentina/Buenos_Aires`, franjas por sede de lunes a viernes, cierres precargados y odontólogos con hasta una franja laboral por día (RN-07, RN-09 y RN-11).
- **Plazo y filtro:** hoy a un mes calendario, inclusive (RN-13); fecha y hora primero, luego odontólogos asignados a esa franja (DD-08).
- **Asistencia:** no condiciona la reserva (RN-12).
- **Mecanismo de concurrencia de C-01:** transacción PostgreSQL `READ COMMITTED`, bloqueo del odontólogo y después del sillón asignado, con consulta de ocupación posterior a los bloqueos.
- **Versiones objetivo y API:** se detallan en el [diseño archivado de C-01](../openspec/changes/archive/2026-11-26-crear-turno-sin-solapamientos/design.md), incluidos los códigos diferenciados y los avisos ante fallos técnicos.
- **PostgreSQL local:** se usa `16.x` para la demo y las pruebas. Se verificó una consulta SQL a `16.15` por TCP con el rol de demo.
- **Política visual:** sistema visual y mockups. La [base visual revisión 1](../design-system/README.md) y el [mockup de C-01](../openspec/changes/archive/2026-11-26-crear-turno-sin-solapamientos/mockups/README.md) fueron aprobados.
