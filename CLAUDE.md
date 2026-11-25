# Odonclick — Instrucciones para Agentes

<!-- active-stack:agent-instruction -->
## Stack tecnológico

| Capa | Tecnología confirmada | Versión mínima |
|---|---|---|
| Interfaz web | HTML, CSS y TypeScript vanilla, sin framework de interfaz | Por definir. |
| API | Java y Spring Boot, interfaz REST | JDK y Spring Boot por definir. |
| Construcción del backend | Gradle | Por definir junto con el JDK. |
| Persistencia | Hibernate y PostgreSQL | Por definir junto con Spring Boot. |

## Base de conocimiento

Odonclick es una aplicación interna de turnos odontológicos para una organización con tres sedes ampliables. Usá el [índice de la base de conocimiento](knowledge-base/README.md) para ubicar las referencias del dominio. Para C-01, consultá las [reglas de negocio](knowledge-base/05_reglas_de_negocio.md), los [criterios de aceptación](knowledge-base/06_funcionalidades.md), los [flujos](knowledge-base/07_flujos_principales.md) y la [arquitectura propuesta](knowledge-base/08_arquitectura_propuesta.md).

Antes de aprobar el diseño, resolvé las [preguntas de prioridad alta](knowledge-base/10_preguntas_abiertas.md): versiones del stack, identidad local de recepción, calendario y zona horaria, y mecanismo de concurrencia y errores.

## Skills disponibles

| Rol | Skills según la tarea |
|---|---|
| Backend y pruebas | `openspec-explore`, `openspec-propose`, `openspec-update-change`, `openspec-apply-change`, `openspec-archive-change` |
| Frontend | `design-system` según la política visual; `emil-design-eng` para revisión de interacción |
| Fundación | `active-orchestrator`, `kb-creator`, `roadmap-generator`, `agent-instruction` |

Consultá las rutas y reglas compactas en el [registro de skills](.atl/skill-registry.md). No se instalaron skills adicionales para la entrega.

## Roadmap de changes

El [roadmap](CHANGES.md) organiza diez changes en tres fases. **La entrega académica solo implementa C-01**, que no tiene dependencias. C-02 a C-10 son parte del MVP posterior; sus cadenas más largas son `C-01 → C-02 → C-07 → C-08 → C-06` y `C-01 → C-02 → C-07 → C-08 → C-10`.

## Reglas duras del proyecto

Las reglas globales del cliente activo se heredan; no se duplican aquí.

1. **Datos ficticios.** No usar datos reales de pacientes ni credenciales reales en el repositorio, las pruebas, las capturas o los ejemplos.
2. **Carga y validación del turno.** Recepción carga el turno del paciente desde el formulario. El servidor valida la sede autorizada, la pertenencia de los recursos, la duración por prestación, la franja y los solapamientos por odontólogo o sillón antes de guardar. El formulario confirma la creación o muestra el motivo del rechazo.
3. **Pruebas obligatorias.** Automatizar cada escenario de la especificación. Probar los envíos simultáneos con PostgreSQL real. No archivar si falla alguna prueba.
4. **Alcance.** Implementar solo C-01 para la entrega. Si una decisión o comportamiento no está en la especificación aprobada, volver a Propose antes de agregarlo.
5. **Git.** No hacer commits ni push sin autorización. Cada integrante registra su aporte con commits propios.

## Flujo de trabajo

Leé la KB y las referencias de C-01 en el roadmap. Seguí Explore → Propose → Apply → Verify → Archive. Revisá la especificación antes de aplicar y registrá la evidencia de verificación y archivado.
<!-- /active-stack:agent-instruction -->
