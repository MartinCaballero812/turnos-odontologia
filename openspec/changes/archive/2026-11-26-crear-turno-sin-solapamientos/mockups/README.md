# Mockup de C-01 — Crear turno

Design system revision: 1
Approval: approved (2026-11-25, by user)

**Audiencia y tarea:** recepción registra un turno en su sede asignada, sin elegir otra sede. La pantalla es de escritorio. La vista angosta comprueba que los controles y mensajes no se cortan; no incorpora un flujo móvil al producto.

**Fuentes:** [especificación S01–S27](../specs/reserva-de-turnos/spec.md), [diseño de C-01](../design.md) y [base visual aprobada](../../../../../design-system/README.md). Se reutilizan sus colores, tipografía, foco y logo provisional `(TBD)`.

## Cómo revisarlo

1. Abrí [turno.html](turno.html) en un navegador. No se necesita servidor, instalación ni PostgreSQL.
2. Verificá que la sede esté visible y que ningún campo del turno esté seleccionado. Elegí una fecha hábil dentro del plazo y la hora: **10:00** ofrece solo **Lucía Ferrer**, **17:00** ofrece solo **Julián Soria** y **15:30** no ofrece odontólogos. Ninguno queda seleccionado automáticamente; si cambiás de franja, se limpia la selección anterior. El filtro indica quién trabaja, no quién tiene el horario libre.
3. Completá los demás campos. Podés usar **Camila Ríos** y **Control odontológico**. Usá **Tab** para revisar el foco. Los pacientes y las prestaciones de esta vista son ejemplos; en C-01 las opciones se consultarán de PostgreSQL. DNI, teléfono y alta de pacientes no forman parte de la entrega.
4. Probá **Radiografía**: el campo queda en **Sin sillón**, bloqueado. Para **Control odontológico** u otra prestación de ejemplo, elegí **Sillón 1** o **Sillón 2**. Si volvés a Radiografía después de elegir un sillón, la selección anterior se borra.
5. En la barra **Solo para revisar**, elegí el resultado de ejemplo. Pulsá **Guardar turno** para verlo. El selector de resultado solo existe en el mockup: ninguna opción llama a la API ni guarda datos.

Para elegir la fecha, abrí el calendario desde el icono del campo **Fecha**. Para probar la validación, dejá la fecha vacía o ingresá `25:99` en **Hora de inicio**. Al intentar guardar, el formulario muestra en español cómo corregir cada campo y pone el foco en el primero que tiene un problema.

Al guardar, el mockup comprueba que la fecha esté entre **hoy y un mes calendario después**, inclusive, según la fecha actual en Buenos Aires. Si el mes siguiente tiene menos días, usa el último día de ese mes. El campo sigue vacío al abrir. La aplicación real hará la validación definitiva en el servidor además de controlar cierres, franjas y conflictos.

La vista también comprueba las franjas de apertura y que la prestación y su pausa de 10 minutos entren completas antes de mostrar un guardado simulado. Por ejemplo, **15:30** se rechaza aunque esté seleccionado «Turno guardado». No consulta la disponibilidad real de los odontólogos ni de los sillones.

Las capturas cubren el formulario inicial, selección de odontólogo habilitada, datos inválidos, turno guardado con sillón, radiografía guardada sin sillón, recurso ocupado, horario inválido y guardado sin confirmar. Los rechazos de la API por sillón, sede o datos usan el mismo patrón de resultado con su motivo específico. La vista no añade consulta de agenda ni reenvío automático.

## Capturas inspeccionadas — 25/11/2026

| Estado | Escritorio · 1440 × 900 | Ventana angosta · 390 × 844 |
|---|---|---|
| Formulario inicial | [Ver](previews/inicio-1440.png) | [Ver](previews/inicio-390.png) |
| Selector habilitado para la tarde | [Ver](previews/tarde-1440.png) | [Ver](previews/tarde-390.png) |
| Datos inválidos | [Ver](previews/datos-1440.png) | [Ver](previews/datos-390.png) |
| Turno guardado | [Ver](previews/guardado-1440.png) | [Ver](previews/guardado-390.png) |
| Radiografía guardada sin sillón | [Ver](previews/radiografia-1440.png) | [Ver](previews/radiografia-390.png) |
| Odontólogo ocupado | [Ver](previews/conflicto-1440.png) | [Ver](previews/conflicto-390.png) |
| Horario fuera de franja | [Ver](previews/horario-1440.png) | [Ver](previews/horario-390.png) |
| Guardado sin confirmar | [Ver](previews/incierto-1440.png) | [Ver](previews/incierto-390.png) |

Las capturas son de página completa. Se verificaron los estados simulados, el foco visible, el cambio de prestación que limpia el sillón y la ausencia de desborde horizontal en ambas resoluciones. No se verificó guardado real ni comportamiento de la API.
