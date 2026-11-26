# Arquitectura propuesta

La entrega se ejecutará **en local**. La estructura y las técnicas de esta página son propuestas para evaluar en OpenSpec; no son infraestructura existente ni un contrato HTTP aprobado.

## Patrones aplicados

| Propuesta | Uso y motivo |
|---|---|
| Monolito modular | Un backend para agenda, sedes y acceso. Evita coordinar servicios separados para un equipo pequeño. |
| API, lógica de agenda y persistencia separadas | La interfaz y los controladores traducen entradas y salidas. La lógica de agenda decide si se puede reservar y la persistencia guarda los turnos admitidos. |
| Contexto de sede validado en el backend | Filtrar en la interfaz mejora la usabilidad, pero no reemplaza el control de acceso en servidor. |
| Datos ficticios reproducibles | Permiten demostrar los escenarios sin depender de una cuenta externa ni acceder a datos de pacientes. |

## Concurrencia de la agenda

RN-03 exige rechazar conflictos con turnos ya guardados y proteger el primer change contra solicitudes simultáneas incompatibles. Ambos casos consideran el intervalo completo, incluida la pausa, y el odontólogo y el sillón si está asignado. Consultar disponibilidad antes de guardar no alcanza por sí solo: dos peticiones podrían consultar antes de que cualquiera guarde.

**Mecanismo implementado en C-01** ([diseño archivado](../openspec/changes/archive/2026-11-26-crear-turno-sin-solapamientos/design.md#transacción-y-concurrencia)): en una transacción `READ COMMITTED`, bloquear primero la fila del odontólogo y después la del sillón, si la prestación lo usa. Consultar la ocupación **después** de adquirir los bloqueos y guardar solo si no hay conflicto. La consulta posterior puede ver una reserva concurrente que ya se confirmó. Las operaciones futuras que cambien la ocupación deberán compartir el protocolo y el orden. Bloquear solo turnos existentes no cubre una agenda todavía vacía.

**Prueba de integración obligatoria con PostgreSQL:** partir de una agenda libre en el intervalo de prueba y enviar en paralelo al backend dos solicitudes válidas por separado que se superponen y comparten un recurso. Al terminar, verificar una creación exitosa, un rechazo por conflicto y un solo turno guardado. La prueba no depende del mecanismo elegido ni del orden de llegada. Referencias técnicas: [bloqueos de filas](https://www.postgresql.org/docs/current/explicit-locking.html#LOCKING-ROWS) y [aislamiento de transacciones](https://www.postgresql.org/docs/current/transaction-iso.html#XACT-READ-COMMITTED).

## Estructura de directorios

```text
turnos-odontologia/
├── frontend/          # TypeScript, HTML y CSS; ubicación propuesta
├── src/main/          # Backend Spring Boot; organización interna a definir
├── src/test/          # Escenarios automatizados e integración con PostgreSQL
├── docs/discovery/    # Fuentes de producto
├── knowledge-base/    # Contexto y decisiones
└── openspec/          # Especificaciones y cambios
```

El [README de entrega](../README.md) explica requisitos, preparación de PostgreSQL local, carga de datos ficticios, arranque y ejecución de pruebas. Las versiones se resumen en la [descripción general](02_descripcion_general.md#stack-tecnológico). Spring Boot usa PostgreSQL `16.15` en el entorno verificado, con bases y cuentas de demo y de pruebas separadas.

## Seguridad

- **Identidad de la demo:** el servidor selecciona un usuario ficticio de recepción y su sede mediante `APP_DEMO_RECEPCION_ID`. Es un contexto local, no un sistema de autenticación. No confiar en un rol o sede enviados por el navegador. El acceso con cuentas corresponde al MVP posterior.
- **Autorización:** verificar en el backend cada operación y no mostrar información de recursos ajenos en los errores.
- **Validación:** cotejar IDs, pertenencia a sede, plazo, franja asignada, duración y uso de sillón según prestación. El filtro de odontólogos y los avisos del formulario no reemplazan la decisión del servidor.
- **Datos y secretos:** solo datos ficticios. No versionar contraseñas, archivos `.env` con valores reales ni volcados; evitar datos de pacientes en logs.
- **Entorno:** planteado para ejecución local; no se requiere abrir un servicio a Internet.

## Variables de entorno propuestas

| Variable | Propósito | Ejemplo ficticio | Sensible |
|---|---|---|---|
| `APP_DEMO_RECEPCION_ID` | Seleccionar el contexto ficticio de recepción al iniciar la demo | Identificador de la recepción precargada. | No. |
| `SPRING_DATASOURCE_URL` | Conexión a PostgreSQL local | `jdbc:postgresql://localhost:5432/odonclick` | No, si no incluye credenciales. |
| `SPRING_DATASOURCE_USERNAME` | Usuario local de la aplicación | `odonclick_dev` | No es contraseña; configurar fuera del código. |
| `SPRING_DATASOURCE_PASSWORD` | Credencial local | No versionar el valor. | Sí. |

La entrega no exige un proveedor de nube, colas ni servicios externos.
