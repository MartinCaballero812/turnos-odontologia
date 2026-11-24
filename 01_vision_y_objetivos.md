# Visión y objetivos

## Propósito del sistema

Odonclick necesita coordinar la agenda de sus consultorios odontológicos sin dobles reservas por profesional o sillón. El producto es una aplicación web interna para una única empresa, con un equipo pequeño distribuido en tres sedes ampliables.

Recepción y odontología hoy podrían perder tiempo con la coordinación manual, pero el [Discovery](../docs/discovery/informe-discovery.md) no estableció una magnitud argentina representativa de ausencias, conflictos ni tiempo perdido. No se prometen porcentajes de mejora.

## Objetivos por actor

| Actor | Objetivo |
|---|---|
| Recepción | Registrar y gestionar turnos en su propia sede, con conflictos explicados. |
| Odontólogo | Consultar su agenda diaria. |
| Asistente dental | Consultar la agenda del sillón y preparar instrumental y espacio. |
| Administrador | Configurar sedes, sillones, profesionales, prestaciones y usuarios de la empresa. |
| Paciente | Es la persona asociada a la cita; no usa la aplicación en el MVP. |

## Alcance del MVP

- Interfaz gráfica interna; tres sedes, con capacidad de agregar más sin duplicar la aplicación.
- Crear, reprogramar y cancelar turnos, preservar el historial y liberar horarios cuando corresponda.
- Consultar agendas por profesional y sillón; gestionar accesos por sede y rol.
- Asignar un odontólogo obligatorio y, cuando corresponda, un sillón. Respetar duración por prestación, pausas fijas y horarios de sede.

## Primer change de la entrega académica

**Crear un turno sin solapamientos**, con una interfaz gráfica mínima y ejecución local. Usar datos ficticios precargados; la recepción registra la cita, ve si se guardó y recibe un motivo claro de rechazo si no se puede reservar. No se requiere un calendario completo para este change.

## Fuera de alcance

- **Del primer change, no del MVP:** cancelar, reprogramar, implementar agendas completas por rol y administrar catálogos desde la interfaz.
- **Del MVP:** cuentas de pacientes, reserva pública, recordatorios y WhatsApp, lista de espera automatizada, ficha clínica e imágenes, cobros y señas, facturación, convenios y obras sociales, y analítica avanzada. Sin despliegue en nube, integración externa o aislamiento entre distintas empresas para la entrega local.

## Métricas de éxito propuestas

- Se rechaza una nueva cita si un turno ya guardado ocupa el mismo odontólogo o sillón durante el intervalo solicitado, incluida la pausa. Ante dos solicitudes simultáneas válidas por separado pero incompatibles entre sí, solo una se guarda.
- El primer change se demuestra localmente con escenarios aceptado, rechazado y de borde, además de pruebas automatizadas.
- El tiempo para cargar un turno y la frecuencia inicial de conflictos podrían medirse en un piloto posterior; hoy no hay una línea de base validada.
