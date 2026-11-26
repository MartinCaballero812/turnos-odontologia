# Odonclick — Base visual

Mode: system-and-mockups
Revision: 1
Approval: approved (2026-11-25, by user)

**Decisión de política:** confirmación del usuario, 25/11/2026. El sistema visual se aprueba antes de preparar el mockup de C-01. Aprobar esta base no equivale a aprobar el mockup ni a autorizar Apply.

Esta base está pensada para recepción: identificar la sede, leer controles y distinguir confirmaciones, rechazos e incertidumbre. La aplicación de C-01 apunta a escritorio; la página de referencia se adapta a ventanas estrechas para revisar la legibilidad de los componentes, no para ampliar el alcance del producto.

**Fuentes:** [visión](../knowledge-base/01_vision_y_objetivos.md), [roles](../knowledge-base/03_actores_y_roles.md), [Explore de C-01](../openspec/changes/archive/2026-11-26-crear-turno-sin-solapamientos/explore.md) y [diseño de C-01](../openspec/changes/archive/2026-11-26-crear-turno-sin-solapamientos/design.md).

## Vista previa local

Abrir [components.html](components.html) en un navegador. No requiere dependencias, conexión externa, Spring Boot ni PostgreSQL. Los controles y estados son ejemplos: no se guardan turnos.

**Capturas de la revisión 1 (25/11/2026):** [escritorio 1440 × 900](previews/escritorio-1440.png) y [ventana angosta 390 × 844, página completa](previews/angosto-390.png).

## Qué se puede reemplazar

- **Nombre mostrado:** editar la constante `BRAND_NAME` en `brand.js`; el nombre vigente es Odonclick.
- **Logo definitivo (TBD):** reemplazar `assets/logo.svg` manteniendo la ruta y retirar el rótulo provisional solo después de aprobar el reemplazo.
- **Imaginería (TBD):** no se usa en C-01. Si se aprueba, agregarla en `assets/` y actualizar [brand.md](brand.md), sin usar fotos de pacientes para la demo.
- **Colores, tipografía, espaciado, foco o movimiento:** cambiar el token correspondiente en `tokens.css`. Si cambia una decisión visual compartida, subir la revisión, volver a generar las capturas y pedir aprobación de las vistas afectadas.
