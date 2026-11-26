# C-01 — Crear turno sin solapamientos

##  Por qué elegimos C-01?

La [recomendación de Discovery](../../../../docs/discovery/informe-discovery.md#d-recomendación-final) prioriza evitar que dos citas ocupen al mismo odontólogo o sillón. Elegimos C-01 porque permite demostrar ese diferencial con una sola funcionalidad verificable y sin depender de otros changes del MVP.

##  Qué incluye y qué queda fuera.

- Recepción registra una cita desde un formulario de escritorio con sede preasignada. Primero elige fecha y hora y, después, un odontólogo que trabaje en esa franja. Radiografía no usa sillón y las otras prestaciones iniciales requieren elegir uno.
- El servidor admite fechas desde hoy hasta un mes calendario después, inclusive. Calcula la duración por prestación y comprueba que la cita y la pausa de 10 minutos caben en la franja asignada al odontólogo. Rechaza cierres, recursos ajenos a la sede y solapamientos sin exponer datos de otros pacientes.
- Si llegan dos solicitudes incompatibles que serían válidas por separado, solo una se guarda. El resultado se verifica con PostgreSQL real.
- Se entrega una demo local reproducible con datos ficticios, confirmación de creación y motivos de rechazo legibles.
- **Fuera de alcance:** login completo, gestión de asistentes, edición de horarios o catálogos, calendario automático de feriados, consulta de agenda completa, reprogramación, reasignación, cancelación, reserva pública y despliegue.

## Qué funcionalidad incorpora el change.

### Nuevas capacidades

- `reserva-de-turnos`: creación interna de citas por recepción, con validación de sede, franja asignada y ocupación de recursos.

### Capacidades modificadas

## Impacto

Introduce el primer flujo web de Odonclick: formulario HTML/CSS/TypeScript, API Spring Boot y persistencia con Hibernate y PostgreSQL. Incluye pruebas automatizadas y documentación para ejecutar la demo y la suite en local. No hay código, APIs ni especificaciones vigentes del producto que migrar. Ver [Explore](explore.md) y [C-01 en el roadmap](../../../../CHANGES.md#c-01-crear-turno-sin-solapamientos).
