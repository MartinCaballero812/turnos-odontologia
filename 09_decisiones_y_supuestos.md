# Decisiones y supuestos

## Decisiones documentadas

### DD-01 — Una aplicación interna para Odonclick

**Decisión confirmada:** una organización, tres sedes ampliables y una interfaz web interna. El acceso se separa por rol y sede, no por empresa ni por instalación. **Contexto:** las tres sedes pertenecen a Odonclick. **Alternativas:** instancias por sede o SaaS para múltiples empresas, no necesarias para este alcance. **Impacto:** conviene modelar las sedes como datos y no fijar su cantidad en el código.

### DD-02 — Stack y entrega local

**Decisión confirmada:** frontend TypeScript vanilla; backend Spring Boot REST, Gradle e Hibernate; PostgreSQL local. No se requiere despliegue remoto. **Contexto:** stack elegido por el equipo y ejecución local confirmada para la entrega. **Alternativas:** no consta una evaluación técnica comparativa; no se atribuye esta elección a una prueba comparativa del Discovery. **Impacto:** la demostración depende de una preparación local reproducible del frontend, backend y base de datos.

### DD-03 — Primer change con interfaz gráfica

**Decisión confirmada:** crear turno sin solapamientos con interfaz gráfica mínima. **Alternativas:** primer change solo API o implementación de toda la agenda; no son el alcance elegido. **Impacto:** la interfaz permite demostrar el flujo completo, pero reprogramar, cancelar y administrar catálogos no entran en esta iteración.

### DD-04 — Pausa dentro de la franja

**Decisión confirmada:** la cita y la pausa de 10 minutos caben dentro de una sola franja. **Contexto:** el equipo quiere terminar la preparación o higienización al cierre de la franja. **Alternativa descartada:** permitir que la pausa se extienda después del cierre aunque la cita haya terminado. **Impacto:** con cierre a las 13:00, la cita finaliza como máximo a las 12:50.

### DD-05 — Diferencial respaldado como oportunidad

**Decisión de alcance:** priorizar el rechazo de conflictos por odontólogo y sillón, con sillón opcional e historial en el MVP, antes que automatización y funciones clínicas. **Evidencia:** el [informe de mercado](../docs/discovery/informe-discovery.md) distingue los avisos que dejan guardar de Codental, la sobrecarga interna configurable de AgendaPro y el historial de turno de Dentally. No comprobó que los competidores apliquen la misma combinación de restricciones. **Impacto:** no es un reclamo de exclusividad; el valor se demuestra con escenarios del producto.

### DD-06 — Proteger la creación ante envíos simultáneos

**Decisión confirmada por el equipo:** incluir la protección y una prueba de integración con PostgreSQL en el primer change. Si dos solicitudes son válidas por separado pero sus horarios se superponen y comparten odontólogo o sillón, una se guarda y la otra se rechaza. **Justificación:** puede haber doble envío o reintento desde recepción, sin que se necesiten dos personas en la misma sede. **Pendiente:** elegir el mecanismo y los códigos de error en el diseño.

## Supuestos inferidos

### SU-01 — Demostración con catálogos precargados

**Propuesta:** usar datos ficticios de sedes, odontólogos, sillones, pacientes y prestaciones; no construir su administración en el primer change. **Origen:** alcance del change y restricción de datos ficticios. **Riesgo:** ampliar la UI si la evaluación pide edición de catálogos. **Validación:** confirmar alcance en la especificación.

### SU-02 — Una referencia temporal coherente

**Propuesta:** interpretar la fecha y hora ingresadas en la hora local de la sede; fijar la zona y la representación de persistencia antes de diseñar la API. **Origen:** las franjas se expresan en hora local. **Riesgo:** discrepancias al mostrar o guardar fechas. **Validación:** caso de prueba con hora de sede y cambio de día si corresponde. No se presupone el código de zona de cada sede.

### SU-03 — Sin integraciones para el primer change

**Propuesta:** la agenda local funciona sin WhatsApp, pagos ni calendarios externos. **Origen:** el checklist no confirma integraciones y el informe las posterga. **Riesgo:** una integración solo entraría con una decisión explícita que cambie el alcance. **Validación:** ejecutar el flujo principal y los tests sin cuentas de terceros.
