# CHANGES — Secuencia de Implementación

> Índice de implementación del MVP de Odonclick. Para la entrega académica se seleccionó **un solo change: C-01 — crear turno sin solapamientos**. Los demás figuran para planificar el producto, no son parte de esa entrega.
> **Leer este archivo antes de empezar un item.**

---

## Cómo usar este documento

1. Identificar la ficha del cambio y conservar su ID. Para la entrega, elegir **C-01**.
2. Verificar dependencias y leer las referencias indicadas. Ante una diferencia con un borrador anterior, usar la decisión confirmada en la KB.
3. Acordar alcance, escenarios y verificación en OpenSpec antes de implementar. Mantener abiertas las decisiones técnicas hasta revisar el diseño.
4. Verificar el resultado con los escenarios aprobados; no archivar con pruebas fallidas.
5. Actualizar el estado de la ficha solo después de verificar y archivar el change. Registrar qué se decidió en la revisión y por qué.

## Árbol de dependencias

```text
C-01 Crear turno sin solapamientos [única entrega académica]
└── C-02 Identidad y acceso del MVP
    ├── C-03 Reprogramar turno con historial
    │   └── C-04 Cancelar turno y liberar horario
    ├── C-05 Consultar agenda diaria del odontólogo
    ├── C-07 Configurar sedes y franjas
    │   └── C-08 Configurar sillones y profesionales
    │       ├── C-06 Consultar agenda diaria del sillón
    │       └── C-10 Administrar usuarios internos
    └── C-09 Configurar prestaciones y duraciones
```

C-01 incluye solo la base local, los datos ficticios y el contexto de recepción necesarios para crear un turno. No requiere construir primero un sistema completo de cuentas ni un panel de configuración.

### Paralelismo por fase

```text
GATE 0: Inicio; KB revisada
  → C-01 crear-turno-sin-solapamientos          [Agente A, con revisión de B/C]

GATE 1: C-01 ✓ — fin de la entrega académica
  → C-02 habilitar-identidad-y-acceso-interno   [Agente B; solo MVP posterior]

GATE 2: C-02 ✓ — ramas independientes del MVP  ← FORK
  → C-03 reprogramar-turno-con-historial        [Agente B]
  → C-05 consultar-agenda-diaria-del-odontologo [Agente C]
  → C-07 configurar-sedes-y-franjas            [Agente A]
  → C-09 configurar-prestaciones-y-duraciones  [Agente B, cuando quede disponible]

GATE 3: C-03 ✓
  → C-04 cancelar-turno-y-liberar-horario       [Agente B]

GATE 4: C-07 ✓
  → C-08 configurar-sillones-y-profesionales   [Agente A]

GATE 5: C-08 ✓
  → C-06 consultar-agenda-diaria-del-sillon     [Agente C]
  → C-10 administrar-usuarios-internos          [Agente B]
```

C-04 reutiliza la consulta diaria de recepción y la base de historial de C-03.

### Camino crítico (5 changes — ramas más largas del MVP)

- **Entrega académica:** `C-01` únicamente, de Explore a Archive.
- **MVP local:** `C-01 → C-02 → C-07 → C-08 → C-06*` o `C-01 → C-02 → C-07 → C-08 → C-10*`. Las ramas paralelas de reprogramación, cancelación, agenda del odontólogo y prestaciones también deben quedar completas para cerrar el MVP. El plazo no incluye un despliegue remoto.

### Plan óptimo con 3 agentes

Reparto orientativo, no asignación de integrantes. Para la entrega, la revisión no divide C-01 en varios changes.

| Paso | Agente A — Backend principal | Agente B — Backend y pruebas | Agente C — Frontend |
|---|---|---|---|
| 1 · Entrega | Responsable de C-01 como un único change | Revisar escenarios y concurrencia de C-01 | Revisar la interfaz mínima y los estados de error de C-01 |
| 2 · MVP posterior | — | C-02 | — |
| 3 · Cambios independientes | C-07 | C-03 | C-05 |
| 4 · Operación y catálogos | C-08 | C-04, luego C-09 | — |
| 5 · Cierre del MVP | — | C-10 | C-06 |

## FASE 1 — Entrega académica: crear un turno

### [C-01] `crear-turno-sin-solapamientos`

- **Estado**: `[x]` implementado, verificado y archivado el 26/11/2026.
- **Cierre**: [change archivado](openspec/changes/archive/2026-11-26-crear-turno-sin-solapamientos/proposal.md), [especificación vigente](openspec/specs/reserva-de-turnos/spec.md) y [evidencia de Verify](openspec/changes/archive/2026-11-26-crear-turno-sin-solapamientos/verify.md). Pasaron los 27 escenarios; se aprobó archivar tras sincronizar la especificación.
- **Scope**:
  - Preparar el arranque local reproducible con Spring Boot REST, Gradle, Hibernate, PostgreSQL y TypeScript vanilla. Precargar solo los datos ficticios necesarios, incluidos franjas de sede, asignaciones de odontólogos y una fecha de cierre de ejemplo.
  - Usar un contexto ficticio de recepción con sede preasignada configurada en el servidor, sin selector de sede ni sistema de cuentas. Rechazar odontólogos o sillones ajenos a la sede.
  - Implementar la creación de turnos (`POST /api/turnos`): odontólogo obligatorio y fin calculado por prestación. Radiografía no admite sillón; para las demás prestaciones del catálogo inicial se exige elegir uno. La cita y la pausa de **10 minutos** deben caber en la misma franja de apertura de la sede y de trabajo del odontólogo. No se agenda en fin de semana ni en un cierre precargado; la disponibilidad de asistentes no condiciona la reserva.
  - Validar el plazo de reserva: desde hoy hasta un mes calendario después, inclusive, según `America/Argentina/Buenos_Aires`.
  - Guardar un solo turno si llegan dos solicitudes simultáneas incompatibles que serían válidas por separado. Devolver éxito para una y conflicto para la otra, sin exponer datos del otro paciente.
  - Incorporar una pantalla mínima de escritorio. Se eligen fecha y hora antes del odontólogo; la lista solo muestra quienes trabajan en esa franja y el servidor comprueba los solapamientos al guardar. El sillón depende de la prestación. No incluye consulta de agenda ni administración de catálogos.
  - Probar los bordes del plazo y de las franjas, el filtro de odontólogos, las combinaciones de prestación y sillón, los rechazos por sede y los conflictos por cada recurso. Verificar concurrencia con PostgreSQL: un éxito, un rechazo y un solo registro.
  - Usar el martes 1/12/2026 como fecha de datos ficticios para las pruebas con reloj controlado; la aplicación calcula el plazo con la fecha actual. Seguir las [versiones y decisiones del diseño](openspec/changes/archive/2026-11-26-crear-turno-sin-solapamientos/design.md) y verificar que la demo y la suite se puedan ejecutar desde el README. No incluye cancelar, reprogramar, reasignar, editar horarios, desplegar ni habilitar reserva pública.
- **Dependencias**: ninguna
- **Governance**: CRITICO
- **Leer antes**:
  - [`knowledge-base/03_actores_y_roles.md`](knowledge-base/03_actores_y_roles.md) — permisos de recepción.
  - [`knowledge-base/05_reglas_de_negocio.md`](knowledge-base/05_reglas_de_negocio.md) — RN-01 a RN-05, RN-07 a RN-13, RN-AU-01.
  - [`knowledge-base/06_funcionalidades.md`](knowledge-base/06_funcionalidades.md#us-001--crear-un-turno-sin-solapamientos-primer-change) — US-001.
  - [`knowledge-base/08_arquitectura_propuesta.md`](knowledge-base/08_arquitectura_propuesta.md) — concurrencia y entorno local.
  - [`knowledge-base/10_preguntas_abiertas.md`](knowledge-base/10_preguntas_abiertas.md) — decisiones cerradas y pendientes operativos.

## FASE 2 — Completar la operación del MVP

### [C-02] `habilitar-identidad-y-acceso-interno`

- **Estado**: `[ ]` pendiente
- **Scope**:
  - Completar el mecanismo de ingreso interno definido en OpenSpec, enlazar usuario, rol y ámbito de sede, y consolidar las comprobaciones del servidor sobre la base de C-01. No habilitar acceso público para pacientes.
  - Probar que usuarios de consulta no puedan modificar turnos, que recepción no pueda salir de su sede y que administración solo tenga permisos de configuración. No incluye la interfaz administrativa de usuarios.
- **Dependencias**: C-01
- **Governance**: CRITICO
- **Leer antes**:
  - [`knowledge-base/03_actores_y_roles.md`](knowledge-base/03_actores_y_roles.md)
  - [`knowledge-base/04_modelo_de_datos.md`](knowledge-base/04_modelo_de_datos.md) — Usuario y Sede.
  - [`knowledge-base/08_arquitectura_propuesta.md`](knowledge-base/08_arquitectura_propuesta.md#seguridad)

### [C-03] `reprogramar-turno-con-historial`

- **Estado**: `[ ]` pendiente
- **Scope**:
  - Agregar la consulta diaria de recepción, limitada a su sede, para seleccionar turnos existentes. La vista se reutiliza para cancelar.
  - Agregar la acción de reprogramación en API e interfaz de recepción. Validar la nueva ocupación con las mismas reglas de agenda; conservar el turno anterior si el intento se rechaza.
  - Ante un cambio de franja laboral, permitir que recepción reprograme con el mismo odontólogo o mantenga el horario y reasigne la cita a otro odontólogo disponible y habilitado para esa franja.
  - Guardar evidencia del horario original y liberar la ocupación anterior al completar la operación. Incorporar la persistencia de historial necesaria si aún no existe.
  - Probar reprogramación válida, conflicto sin pérdida del turno original y tentativa fuera de sede.
- **Dependencias**: C-02
- **Governance**: CRITICO
- **Leer antes**:
  - [`knowledge-base/04_modelo_de_datos.md`](knowledge-base/04_modelo_de_datos.md) — Turno y EventoTurno.
  - [`knowledge-base/05_reglas_de_negocio.md`](knowledge-base/05_reglas_de_negocio.md) — RN-03 a RN-06.
  - [`knowledge-base/06_funcionalidades.md`](knowledge-base/06_funcionalidades.md) — US-002.
  - [`knowledge-base/07_flujos_principales.md`](knowledge-base/07_flujos_principales.md) — Flujo 3.

### [C-04] `cancelar-turno-y-liberar-horario`

- **Estado**: `[ ]` pendiente
- **Scope**:
  - Agregar la acción de cancelación en API y en la consulta diaria de recepción. Registrar cancelación, conservar la cita original y dejar de ocupar el horario; reutilizar la base de historial de C-03.
  - Probar que el horario vuelva a estar disponible y que una cancelación ajena a la sede no cambie el turno.
- **Dependencias**: C-03
- **Governance**: CRITICO
- **Leer antes**:
  - [`knowledge-base/04_modelo_de_datos.md`](knowledge-base/04_modelo_de_datos.md) — Turno y EventoTurno.
  - [`knowledge-base/05_reglas_de_negocio.md`](knowledge-base/05_reglas_de_negocio.md) — RN-06 y RN-AU-01.
  - [`knowledge-base/06_funcionalidades.md`](knowledge-base/06_funcionalidades.md) — US-003.
  - [`knowledge-base/07_flujos_principales.md`](knowledge-base/07_flujos_principales.md) — Flujo 3.

### [C-05] `consultar-agenda-diaria-del-odontologo`

- **Estado**: `[ ]` pendiente
- **Scope**:
  - Agregar consulta diaria y vista de solo lectura para el odontólogo identificado. Usar el vínculo con su identidad interna para filtrar por profesional y sede, sin incorporar edición ni bloqueos de horarios.
  - Probar acceso a la agenda propia y rechazo de consulta a otra agenda.
- **Dependencias**: C-02
- **Governance**: ALTO
- **Leer antes**:
  - [`knowledge-base/03_actores_y_roles.md`](knowledge-base/03_actores_y_roles.md)
  - [`knowledge-base/04_modelo_de_datos.md`](knowledge-base/04_modelo_de_datos.md) — Odontólogo y Turno.
  - [`knowledge-base/06_funcionalidades.md`](knowledge-base/06_funcionalidades.md) — US-004.
  - [`knowledge-base/07_flujos_principales.md`](knowledge-base/07_flujos_principales.md) — Flujo 2.

### [C-06] `consultar-agenda-diaria-del-sillon`

- **Estado**: `[ ]` pendiente
- **Scope**:
  - Agregar consulta y vista diaria de sillón para el asistente, de solo lectura. Antes de aprobar el diseño, decidir su asignación y cómo consulta citas sin sillón; no deducir esos permisos.
  - Probar que solo se muestren los sillones y citas autorizados y que la vista no permita editar turnos.
- **Dependencias**: C-08
- **Governance**: ALTO
- **Leer antes**:
  - [`knowledge-base/03_actores_y_roles.md`](knowledge-base/03_actores_y_roles.md)
  - [`knowledge-base/06_funcionalidades.md`](knowledge-base/06_funcionalidades.md) — US-005.
  - [`knowledge-base/07_flujos_principales.md`](knowledge-base/07_flujos_principales.md) — Flujo 2.
  - [`knowledge-base/10_preguntas_abiertas.md`](knowledge-base/10_preguntas_abiertas.md) — asignación y turnos sin sillón.

## FASE 3 — Configuración de Odonclick

> Las fases agrupan funciones; para el orden de ejecución prevalecen las dependencias. C-06 usa el catálogo editable de C-08.

### [C-07] `configurar-sedes-y-franjas`

- **Estado**: `[ ]` pendiente
- **Scope**:
  - Agregar acciones administrativas y pantalla para registrar sedes, configurar franjas de apertura y cargar feriados u otros cierres. Reutilizar los modelos básicos de C-01 y no fijar en código el número de sedes.
  - Permitir modificar el horario de apertura cuando la franja afectada no tenga citas pendientes y no aceptar nuevas reservas en esa franja hasta configurar el nuevo horario. Probar configuración válida y ausencia de permisos en recepción.
- **Dependencias**: C-02
- **Governance**: ALTO
- **Leer antes**:
  - [`knowledge-base/03_actores_y_roles.md`](knowledge-base/03_actores_y_roles.md)
  - [`knowledge-base/04_modelo_de_datos.md`](knowledge-base/04_modelo_de_datos.md) — Sede y FranjaAtencion.
  - [`knowledge-base/05_reglas_de_negocio.md`](knowledge-base/05_reglas_de_negocio.md) — RN-07, RN-09 y RN-AU-02.
  - [`knowledge-base/06_funcionalidades.md`](knowledge-base/06_funcionalidades.md) — US-006.

### [C-08] `configurar-sillones-y-profesionales`

- **Estado**: `[ ]` pendiente
- **Scope**:
  - Habilitar edición administrativa de los catálogos de sillones y odontólogos por sede. Reutilizar los recursos mínimos de C-01 y garantizar que cada registro pertenece a una sola sede.
  - Permitir que administración asigne o cambie la franja de trabajo del odontólogo por día hábil, respetando como máximo una franja por día. El administrador no reasigna ni reprograma citas.
  - Probar la pertenencia a sede, permisos administrativos y conservación de las referencias de citas ya guardadas al editar catálogos. No otorgar al administrador acciones sobre turnos.
- **Dependencias**: C-07
- **Governance**: ALTO
- **Leer antes**:
  - [`knowledge-base/03_actores_y_roles.md`](knowledge-base/03_actores_y_roles.md)
  - [`knowledge-base/04_modelo_de_datos.md`](knowledge-base/04_modelo_de_datos.md) — Odontólogo y Sillón.
  - [`knowledge-base/05_reglas_de_negocio.md`](knowledge-base/05_reglas_de_negocio.md) — RN-02, RN-08, RN-10 y RN-AU-02.
  - [`knowledge-base/06_funcionalidades.md`](knowledge-base/06_funcionalidades.md) — US-006.

### [C-09] `configurar-prestaciones-y-duraciones`

- **Estado**: `[ ]` pendiente
- **Scope**:
  - Habilitar edición administrativa del catálogo de prestaciones y su duración. Validar minutos positivos y confirmar en diseño cómo se preserva la duración de los turnos existentes al modificar una prestación.
  - Probar que recepción no puede editar el catálogo ni variar la duración de una cita puntual. Conservar la regla de uso de sillón por prestación definida para C-01; editar esa regla no forma parte de C-09.
- **Dependencias**: C-02
- **Governance**: ALTO
- **Leer antes**:
  - [`knowledge-base/03_actores_y_roles.md`](knowledge-base/03_actores_y_roles.md)
  - [`knowledge-base/04_modelo_de_datos.md`](knowledge-base/04_modelo_de_datos.md) — Prestación y duración aplicada.
  - [`knowledge-base/05_reglas_de_negocio.md`](knowledge-base/05_reglas_de_negocio.md) — RN-04 y RN-AU-02.
  - [`knowledge-base/06_funcionalidades.md`](knowledge-base/06_funcionalidades.md) — US-006.

### [C-10] `administrar-usuarios-internos`

- **Estado**: `[ ]` pendiente
- **Scope**:
  - Agregar interfaz y acciones administrativas sobre usuarios internos, sus roles y vínculos de sede o profesional conforme a lo definido en C-02. Usar los registros configurables de profesionales de C-08; no ampliar permisos de asistencia por inferencia.
  - Probar que las asignaciones respeten el ámbito de cada rol y que la configuración no habilite al administrador a gestionar turnos.
- **Dependencias**: C-08
- **Governance**: CRITICO
- **Leer antes**:
  - [`knowledge-base/03_actores_y_roles.md`](knowledge-base/03_actores_y_roles.md)
  - [`knowledge-base/04_modelo_de_datos.md`](knowledge-base/04_modelo_de_datos.md) — Usuario y Odontólogo.
  - [`knowledge-base/06_funcionalidades.md`](knowledge-base/06_funcionalidades.md) — US-006.
  - [`knowledge-base/08_arquitectura_propuesta.md`](knowledge-base/08_arquitectura_propuesta.md#seguridad)
