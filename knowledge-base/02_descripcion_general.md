# Descripción general

## Stack tecnológico

| Capa | Tecnología confirmada | Versión mínima |
|---|---|---|
| Interfaz web | HTML, CSS y TypeScript vanilla, sin framework de interfaz | Por definir. |
| API | Java y Spring Boot, interfaz REST | JDK y Spring Boot por definir. |
| Construcción del backend | Gradle | Por definir junto con el JDK. |
| Persistencia | Hibernate y PostgreSQL | Por definir junto con Spring Boot. |

La tabla registra mínimos generales del producto, no las versiones fijadas para la demo. **C-01** tiene como objetivo Java 17, Spring Boot `3.5.16`, Gradle Wrapper `8.14.8`, PostgreSQL `16.x` y TypeScript `7.0.2` con Node `24.19.0` y npm `11.17.0`. Hibernate y el controlador JDBC los gestiona Spring Boot. Se verificó el acceso por TCP a PostgreSQL `16.15` en el entorno local.

## Arquitectura general

```text
Navegador local (TypeScript) → API REST de Odonclick → PostgreSQL local
```

Una aplicación para una organización. Las sedes son ámbitos de operación y acceso; no son empresas ni instancias independientes. Se propone un solo backend modular para la entrega, con reglas en servidor y persistencia compartida. Ver [arquitectura propuesta](08_arquitectura_propuesta.md).

## Integraciones externas

No hay integraciones externas confirmadas para el MVP y ninguna es necesaria para el primer change. PostgreSQL local es infraestructura de persistencia, no una integración de negocio. WhatsApp, calendarios externos, Mercado Pago y ARCA son posibilidades posteriores, no requisitos.

## API REST de C-01 y del MVP posterior

El [diseño archivado de C-01](../openspec/changes/archive/2026-11-26-crear-turno-sin-solapamientos/design.md#contrato-http) contiene el contrato HTTP detallado. La [especificación vigente](../openspec/specs/reserva-de-turnos/spec.md) describe el comportamiento implementado. Las operaciones posteriores no se implementan en C-01.

| Operación | Propuesta | Alcance |
|---|---|---|
| Crear turno | `POST /api/turnos` | Primer change. Recibe paciente, profesional, prestación e inicio; toma la sede del contexto de recepción y calcula el fin. Radiografía no admite sillón; las demás prestaciones del catálogo inicial lo requieren. |
| Obtener recepción y sede | `GET /api/contexto` | Contexto ficticio preasignado por el servidor; el formulario no cambia de sede. |
| Consultar opciones del formulario | `GET /api/catalogos` | Catálogos de la sede; con `inicioLocal` se filtran odontólogos por la franja laboral de ese día. Las prestaciones indican si usan sillón. |
| Consultar turnos / agenda | Lectura filtrada por sede, profesional o sillón | MVP posterior; no se incluye un listado de turnos en C-01. |
| Cancelar o reprogramar | Acciones sobre turno existente | Changes posteriores del MVP. |

La API distingue creación, errores de datos o reglas de agenda y conflictos por recurso. Una falla técnica sin resultado concluyente no se muestra como conflicto.
