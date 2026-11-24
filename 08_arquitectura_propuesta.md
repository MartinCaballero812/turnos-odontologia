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

**Mecanismo propuesto, no decidido:** en una transacción, bloquear primero la fila del odontólogo y después la del sillón, si existe. Consultar los turnos que ocupan esos recursos **después** de adquirir los bloqueos, y guardar solo si no hay conflicto. Con `READ COMMITTED`, esa consulta posterior puede ver una reserva concurrente que ya se confirmó. Si se adopta esta opción, las operaciones que cambien la ocupación deben compartir el protocolo y usar un orden estable. Bloquear solo turnos existentes no cubre el caso de una agenda todavía vacía.

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

El README de entrega deberá explicar requisitos, preparación de PostgreSQL local, carga de datos ficticios, arranque y ejecución de pruebas. JDK, versiones y herramienta de compilación del frontend quedan pendientes de selección.

## Seguridad

- **Autenticación:** la separación por rol y sede está confirmada; el mecanismo de identidad de la demostración y el del MVP quedan pendientes de diseño. No confiar en un rol o sede enviados libremente por el navegador.
- **Autorización:** verificar en el backend cada operación y no mostrar información de recursos ajenos en los errores.
- **Validación:** cotejar IDs de catálogos, sede, horario y duración con el servidor. La interfaz no decide si un turno puede persistirse.
- **Datos y secretos:** solo datos ficticios. No versionar contraseñas, archivos `.env` con valores reales ni volcados; evitar datos de pacientes en logs.
- **Entorno:** planteado para ejecución local; no se requiere abrir un servicio a Internet.

## Variables de entorno propuestas

| Variable | Propósito | Ejemplo ficticio | Sensible |
|---|---|---|---|
| `SPRING_DATASOURCE_URL` | Conexión a PostgreSQL local | `jdbc:postgresql://localhost:5432/odonclick` | No, si no incluye credenciales. |
| `SPRING_DATASOURCE_USERNAME` | Usuario local de la aplicación | `odonclick_dev` | No es contraseña; configurar fuera del código. |
| `SPRING_DATASOURCE_PASSWORD` | Credencial local | No versionar el valor. | Sí. |

La entrega no exige un proveedor de nube, colas ni servicios externos.
