# Actores y roles

## Actores del sistema

| Actor | Cómo interactúa |
|---|---|
| Recepción | Carga, consulta, reprograma y cancela turnos en su sede. Es el actor principal del primer change. |
| Odontólogo | Consulta sus propias citas de la agenda diaria. No requiere una opción para bloquear horarios. |
| Asistente dental | Consulta la agenda diaria del sillón para preparar instrumental e higienizar. No edita turnos en el MVP. |
| Administrador | Un administrador interno configura los recursos y usuarios de todas las sedes. |
| Paciente | Registro ficticio para la entrega; sin cuenta ni interfaz propia en el MVP. |

## RBAC — Permisos por rol y sede

| Rol | Turnos y agenda | Configuración | Límite |
|---|---|---|---|
| Recepción | Crear, consultar, reprogramar y cancelar | Sin edición de catálogos confirmada | Sede propia. |
| Odontólogo | Solo consultar su agenda | Sin permisos confirmados | Sus propias citas en su sede. |
| Asistente dental | Solo consultar agenda diaria del sillón | Sin permisos confirmados | La asignación a sede, profesional o sillón queda por precisar. |
| Administrador | No gestiona turnos en el MVP | Configurar sedes, sillones, odontólogos, prestaciones y usuarios | Configuración de todas las sedes de Odonclick. |
| Paciente | Ninguno | Ninguna | Sin acceso en el MVP. |

En el MVP, el administrador crea la cuenta de recepción y le asigna una sola sede. Al iniciar sesión, la persona de recepción entra directamente al espacio de esa sede, sin selector para cambiarla. Solo consulta y gestiona los turnos de su sede y el formulario solo ofrece odontólogos y sillones de esa sede.

El servidor determina la sede a partir del acceso de recepción y valida el rol y el ámbito de cada operación. Para C-01 la demo selecciona una recepción ficticia con sede preasignada mediante `APP_DEMO_RECEPCION_ID`, sin implementar el inicio de sesión ni el panel administrativo.

## Rutas públicas

No se prevé agenda, reserva ni portal público. Si el diseño del MVP incorpora una pantalla de ingreso, será la única entrada pública de usuario prevista; no se fija una ruta concreta en la KB.
