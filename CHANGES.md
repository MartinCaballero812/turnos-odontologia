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

- **Estado**: `[ ]` pendiente
- **Scope**:
  - Preparar el arranque local reproducible con Spring Boot REST, Gradle, Hibernate, PostgreSQL y TypeScript vanilla. Crear solo el esquema y los datos ficticios necesarios: sedes, franjas, odontólogos, sillones, prestaciones, pacientes y turnos.
  - Resolver en el diseño la identidad local de recepción. Validar su sede en el servidor y rechazar un odontólogo o sillón de otra sede; no confiar en el valor que manda el formulario.
  - Implementar la creación de turnos (`POST /api/turnos` como ruta propuesta): odontólogo obligatorio, sillón opcional y fin calculado por prestación. La cita y la pausa de **10 minutos** deben caber en la misma franja.
  - Guardar un solo turno si llegan dos solicitudes simultáneas incompatibles que serían válidas por separado. Devolver éxito para una y conflicto para la otra. Definir en OpenSpec el mecanismo, los códigos de error y cómo mostrar recurso y franja sin exponer datos del otro paciente.
  - Incorporar una pantalla mínima de creación con confirmación y mensajes de rechazo. No incluye calendario completo ni administración de catálogos.
  - Probar: creación válida; conflictos separados por odontólogo y sillón; cita sin sillón; inicio justo al terminar la cita o su pausa; cierre con cita hasta 12:50 o 12:55; acceso fuera de sede y concurrencia con PostgreSQL (un éxito, un rechazo y un solo registro).
  - Precisar versiones, día y zona horaria de la demo antes de implementar. Verificar que se pueda ejecutar y probar desde el README. No incluye cancelar, reprogramar, desplegar ni habilitar reserva pública.
- **Dependencias**: ninguna
- **Governance**: CRITICO
- **Leer antes**:
  - [`knowledge-base/03_actores_y_roles.md`](knowledge-base/03_actores_y_roles.md) — permisos de recepción.
  - [`knowledge-base/05_reglas_de_negocio.md`](knowledge-base/05_reglas_de_negocio.md) — RN-01 a RN-05, RN-07 a RN-10, RN-AU-01.
  - [`knowledge-base/06_funcionalidades.md`](knowledge-base/06_funcionalidades.md#us-001--crear-un-turno-sin-solapamientos-primer-change) — US-001.
  - [`knowledge-base/08_arquitectura_propuesta.md`](knowledge-base/08_arquitectura_propuesta.md) — concurrencia y entorno local.
  - [`knowledge-base/10_preguntas_abiertas.md`](knowledge-base/10_preguntas_abiertas.md) — decisiones que faltan para el diseño.

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
  - Agregar acciones administrativas y pantalla para registrar sedes y configurar franjas de atención. Reutilizar los modelos básicos de C-01 y no fijar en código el número de sedes.
  - Probar configuración por una cuenta de administración, franjas válidas y ausencia de permisos en recepción. Precisar en diseño el efecto de cambiar una franja que ya tiene turnos.
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
  - Probar que recepción no puede editar el catálogo ni variar la duración de una cita puntual. No agregar obligatoriedad de sillón por prestación.
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
