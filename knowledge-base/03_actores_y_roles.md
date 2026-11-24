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

La sede elegida en la interfaz no concede permisos. El backend debe validar el rol y ámbito de cada operación. El mecanismo exacto para representar la identidad de recepción en una demo local está [pendiente](10_preguntas_abiertas.md); datos ficticios no significa acceso público.

## Rutas públicas

No se prevé agenda, reserva ni portal público. Si el diseño del MVP incorpora una pantalla de ingreso, será la única entrada pública de usuario prevista; no se fija una ruta concreta en la KB.
