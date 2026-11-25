# Skill Registry — Odonclick

Registro versionable de la Etapa 5. No se instalaron skills adicionales. Las rutas con `~/` reflejan la instalación local del cliente indicado; en otra máquina, resolver por nombre en el cliente activo. Las rutas del repositorio se resuelven desde su raíz. Los bloques compactos son para quien coordina el trabajo; no reemplazan las instrucciones de la skill ni autorizan delegación por sí mismos.

## Skills disponibles

| Activación | Skill | Ruta |
|---|---|---|
| Iniciar o retomar fundación | `active-orchestrator` | `~/.config/opencode/skills/active-orchestrator/SKILL.md` |
| Crear o revisar instrucciones de agentes | `agent-instruction` | `~/.config/opencode/skills/agent-instruction/SKILL.md` |
| Definir o auditar el sistema visual | `design-system` | `~/.config/opencode/skills/design-system/SKILL.md` |
| Conservar una estética v1 ya adoptada | `design-taste-frontend-v1` | `~/.config/opencode/skills/design-taste-frontend-v1/SKILL.md` |
| Hacer Discovery de producto | `discovery-research` | `~/.config/opencode/skills/discovery-research/SKILL.md` |
| Revisar interacción, movimiento y acabado | `emil-design-eng` | `~/.config/opencode/skills/emil-design-eng/SKILL.md` |
| Buscar skills | `find-skills` | `~/.config/opencode/skills/find-skill/SKILL.md` |
| Crear base de conocimiento | `kb-creator` | `~/.config/opencode/skills/kb-creator/SKILL.md` |
| Implementar tareas de un change | `openspec-apply-change` | `.agents/skills/openspec-apply-change/SKILL.md` |
| Archivar un change completo | `openspec-archive-change` | `.agents/skills/openspec-archive-change/SKILL.md` |
| Explorar una idea o un problema | `openspec-explore` | `.agents/skills/openspec-explore/SKILL.md` |
| Preparar artefactos de un nuevo change | `openspec-propose` | `.agents/skills/openspec-propose/SKILL.md` |
| Sincronizar deltas sin archivar | `openspec-sync-specs` | `.agents/skills/openspec-sync-specs/SKILL.md` |
| Revisar artefactos de un change existente | `openspec-update-change` | `.agents/skills/openspec-update-change/SKILL.md` |
| Generar o actualizar el roadmap | `roadmap-generator` | `~/.config/opencode/skills/roadmap-generator/SKILL.md` |
| Crear, mejorar o evaluar una skill | `skill-creator` | `~/.config/opencode/skills/skill-creator/SKILL.md` |
| Investigar un competidor desde una URL | `web-scraper` | `~/.config/opencode/skills/web-scraper/SKILL.md` |

Las seis skills OPSX también están en `.claude/skills/`. Claude Code tiene copias globales de las skills de fundación, búsqueda, creación de skills e investigación de competidores. Las skills integradas `opencode` y `report` pertenecen al cliente OpenCode, no al repositorio.

## Reglas compactas

### active-orchestrator
- Leé estado y artefactos antes de proponer una ruta.
- Reutilizá fases terminadas y verificá sus salidas.
- Confirmá variantes y escrituras antes de ejecutar un plan nuevo.
- Preservá campos de estado desconocidos y la historia de pausas.
- No instales ni publiques sin aprobación explícita.

### agent-instruction
- Derivá el stack y el alcance de la KB y el roadmap.
- No dupliques reglas heredadas del cliente ni bloques de otros arneses.
- Presentá y confirmá las reglas duras antes de escribir.
- Usá marcadores administrados y preservá texto preexistente.
- Validá los enlaces y las reglas en los dos destinos.

### design-system
- Leé la política visual guardada antes de preguntar o diseñar.
- No resuelvas silenciosamente discrepancias entre estado y README.
- Auditá lo existente; marcá lo no definido como provisional.
- Mantené tokens y activos locales, accesibles y reutilizables.
- Mostrá vistas móvil/escritorio y esperá aprobación cuando corresponda.

### design-taste-frontend-v1
- Usá esta versión solo si hace falta mantener la compatibilidad visual v1.
- Adaptá densidad y composición al tipo de pantalla.
- Diseñá estados vacíos, de carga y error.
- Reducí efectos costosos y respetá movimiento reducido.
- Verificá legibilidad, responsividad y estados antes de entregar.

### discovery-research
- Reusá fuentes existentes y separá evidencia de hipótesis.
- Cubrí los once puntos del checklist con el usuario.
- Conservá restricciones, integraciones sin confirmar y preguntas abiertas.
- Confirmá el resumen íntegro antes de escribir.
- Guardá artefactos y estado de Discovery sin generar la KB.

### emil-design-eng
- Animá solo cuando ayude a entender la interacción.
- Mantené transiciones cortas, interrumpibles y coherentes.
- Preferí `transform` y `opacity` para el movimiento.
- Respetá `prefers-reduced-motion` y no dependas del hover táctil.
- Revisá estados, tiempos y accesibilidad en el componente real.

### find-skills
- Buscá por la tarea concreta que se quiere resolver.
- Consultá el catálogo antes de recomendar.
- Verificá fuente, documentación y adopción de cada opción.
- Explicá utilidad y limitaciones para el stack real.
- Instalá únicamente con autorización explícita.

### kb-creator
- Elegí importación o entrevista según las fuentes disponibles.
- Generá los diez archivos canónicos y un índice coherente.
- Separá decisiones confirmadas, propuestas y dudas.
- Conservá trazabilidad y enlaces a las fuentes.
- No inventes decisiones faltantes ni sobrescribas material sin autorización.

### openspec-apply-change
- Seleccioná el change explícito o resolvé la ambigüedad primero.
- Leé el estado y las instrucciones de apply antes de editar.
- Leé todos los artefactos que indique el comando.
- Implementá una tarea por vez y marcala solo al completarla.
- Pausá si aparecen bloqueos o falta una decisión de diseño.

### openspec-archive-change
- Confirmá qué change se archiva si no es inequívoco.
- Revisá artefactos y tareas antes de cerrar.
- Compará los deltas con los specs vigentes y decidí su sincronización.
- No archives un change académico con verificación fallida.
- Comprobá el destino de archivo y reportá el resultado.

### openspec-explore
- Investigá el repositorio y entendé el problema antes de proponer.
- Contrastá alternativas, restricciones y escenarios.
- Usá diagramas cuando aclaren relaciones o flujos.
- No implementes ni edites código durante Explore.
- Capturá el relevamiento si se necesita como evidencia.

### openspec-propose
- Confirmá el nombre del change y su objetivo.
- Seguí el orden y las dependencias del esquema activo.
- Usá las instrucciones y plantillas devueltas por OpenSpec.
- Prepará propuesta, diseño, specs y tareas antes de apply.
- Validá el estado de los artefactos; no implementes en esta fase.

### openspec-sync-specs
- Identificá los deltas del change y la spec principal correspondiente.
- Aplicá cambios puntuales sin sobrescribir requisitos no afectados.
- Integrá requisitos agregados, modificados, renombrados o eliminados según el delta.
- Mantené la operación idempotente.
- No archives el change como efecto de la sincronización.

### openspec-update-change
- Seleccioná el change existente y revisá su estado.
- Leé los artefactos vigentes y sus dependencias.
- Propagá la decisión nueva a propuesta, diseño, specs y tareas afectados.
- Conservá decisiones y trabajo no afectados.
- No edites código de implementación durante esta revisión.

### roadmap-generator
- Derivá el alcance de la KB y respetá los cambios ya acordados.
- Dale a cada ficha un ID estable, dependencias y referencias.
- Verificá árbol, puertas de paralelismo y camino crítico.
- Indicá gobernanza y criterios concretos por ficha.
- No regeneres un roadmap aprobado sin pedido explícito.

### skill-creator
- Definí el resultado, los disparadores y las restricciones de la skill.
- Escribí reglas accionables y guardá el detalle largo en referencias.
- Probá ejemplos realistas con criterios verificables.
- Compará resultados con una referencia sin skill cuando se evalúe desempeño.
- Iterá con evidencia y comentarios del usuario.

### web-scraper
- Validá la URL y limitá el análisis a las páginas necesarias.
- Priorizá fuentes oficiales y registrá los enlaces usados.
- Separá descripción observada de afirmaciones comerciales.
- Marcá lo no disponible sin deducir prestaciones ni precios.
- No hagas scraping masivo ni intentes sortear bloqueos.

## Convenciones del proyecto

| Archivo | Uso |
|---|---|
| [`AGENTS.md`](../AGENTS.md) | Instrucciones y reglas aprobadas para los agentes. |
| [`CLAUDE.md`](../CLAUDE.md) | Copia de las instrucciones para Claude Code. |
| [`knowledge-base/README.md`](../knowledge-base/README.md) | Índice de la base de conocimiento y sus fuentes. |
| [`CHANGES.md`](../CHANGES.md) | Roadmap, dependencias y alcance de C-01. |
| [`openspec/config.yaml`](../openspec/config.yaml) | Configuración OpenSpec del repositorio. |
| [`knowledge-base/05_reglas_de_negocio.md`](../knowledge-base/05_reglas_de_negocio.md) | Reglas del dominio. |
| [`knowledge-base/06_funcionalidades.md`](../knowledge-base/06_funcionalidades.md) | Criterios de aceptación. |
| [`knowledge-base/07_flujos_principales.md`](../knowledge-base/07_flujos_principales.md) | Flujos de agenda. |
| [`knowledge-base/08_arquitectura_propuesta.md`](../knowledge-base/08_arquitectura_propuesta.md) | Arquitectura y concurrencia. |
| [`knowledge-base/10_preguntas_abiertas.md`](../knowledge-base/10_preguntas_abiertas.md) | Decisiones pendientes antes de aprobar el diseño. |
