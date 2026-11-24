# Descripción general

## Stack tecnológico

| Capa | Tecnología confirmada | Versión mínima |
|---|---|---|
| Interfaz web | HTML, CSS y TypeScript vanilla, sin framework de interfaz | Por definir. |
| API | Java y Spring Boot, interfaz REST | JDK y Spring Boot por definir. |
| Construcción del backend | Gradle | Por definir junto con el JDK. |
| Persistencia | Hibernate y PostgreSQL | Por definir junto con Spring Boot. |

## Arquitectura general

```text
Navegador local (TypeScript) → API REST de Odonclick → PostgreSQL local
```

Una aplicación para una organización. Las sedes son ámbitos de operación y acceso; no son empresas ni instancias independientes. Se propone un solo backend modular para la entrega, con reglas en servidor y persistencia compartida. Ver [arquitectura propuesta](08_arquitectura_propuesta.md).

## Integraciones externas

No hay integraciones externas confirmadas para el MVP y ninguna es necesaria para el primer change. PostgreSQL local es infraestructura de persistencia, no una integración de negocio. WhatsApp, calendarios externos, Mercado Pago y ARCA son posibilidades posteriores, no requisitos.

## API REST propuesta

Los nombres y contratos son orientativos; la especificación del change debe confirmarlos.

| Operación | Propuesta | Alcance |
|---|---|---|
| Crear turno | `POST /api/turnos` | Primer change. Recibe sede, paciente, profesional, prestación, inicio y sillón opcional; la API calcula el fin. |
| Consultar opciones del formulario | Lectura de catálogos filtrados por sede, por API o datos precargados | Solo lo necesario para la interfaz del change. No exige un CRUD de catálogos. |
| Consultar turnos / agenda | Lectura filtrada por sede, profesional o sillón | MVP. Un listado mínimo para la demostración no implica completar las vistas por rol. |
| Cancelar o reprogramar | Acciones sobre turno existente | Changes posteriores del MVP. |

Si se usa la propuesta REST, devolver un resultado de creación, un error por datos u horario inválidos y un conflicto distinguible por recurso. No prometer rutas, códigos de error ni mecanismo de sesión hasta el diseño del change.
