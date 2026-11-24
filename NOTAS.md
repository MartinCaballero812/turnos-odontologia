

# ETAPA 2: Discovery

# 23 \- 11- 2026 — ENTREVISTA

## Discovery — Primera ronda

1\. Problema. ¿Qué falla hoy al gestionar turnos y a quién le afecta más? Por ejemplo: turnos superpuestos, ausencias, demoras para coordinar o algo distinto. Esto define qué debería mejorar el sistema.

2\. Usuarios. ¿Quiénes van a usar el sistema en la primera versión y qué hará cada uno? Por ejemplo: recepción agenda, odontología consulta su agenda, pacientes reservan. Esto define qué pantallas y permisos hacen falta.

3\. Casos de uso. ¿Cuáles son las 3 a 5 tareas principales que el sistema tiene que permitir de punta a punta? Por ejemplo: «Como recepcionista, quiero reprogramar un turno para liberar el horario anterior sin perder el registro». Esto nos ayuda a definir los flujos.

4\. Alcance de la primera versión. ¿Qué es indispensable para que se pueda usar en un consultorio y qué dejarías para después? Esto separa lo necesario de lo opcional.  
Podés responder numerando del 1 al 4\. Si algo todavía no está decidido, lo dejamos como pregunta abierta.

### 

### RESPUESTAS POR CLAUDE  Revisado por Juan Cruz

1\. Problema  
Los consultorios odontológicos coordinan los turnos de forma manual (papel, planillas, mensajes). Hipótesis de fallas: turnos superpuestos por profesional o sillón, ausencias sin aviso y tiempo administrativo perdido en coordinar. Afecta más a recepción (carga y corrige todo a mano) y al odontólogo (huecos y superposiciones en su agenda). 

Pendiente: magnitud real (porcentaje de ausentismo, tiempo perdido); no lo sabemos y lo debe aclarar la investigación de mercado.

2\. Usuarios (primera versión)  
\- Recepción: crea, reprograma y cancela turnos, y consulta la agenda.  
\- Odontólogo: consulta su propia agenda.  
\- Administrador: configura profesionales, sillones y prestaciones.  
\- Paciente: pendiente; queda fuera de la primera versión.

Pendiente: permisos exactos de cada rol.

3\. Casos de uso  
\- Como recepcionista, quiero crear un turno sin solapamientos por profesional y por sillón, para evitar dobles reservas.  
\- Como recepcionista, quiero reprogramar un turno, para liberar el horario anterior sin perder el registro.  
\- Como recepcionista, quiero cancelar un turno, para que el horario quede disponible.  
\- Como odontólogo, quiero ver mi agenda por día, para saber qué pacientes atiendo y cuándo.  
\- Como administrador, quiero configurar profesionales, sillones y prestaciones (con duración variable), para que la agenda respete la realidad del consultorio.

4\. Alcance de la primera versión (provisional)  
Imprescindible: gestión de turnos con prevención de solapamientos, duración variable por prestación y agenda por profesional.  
Para después: reserva online del paciente, WhatsApp y recordatorios, ficha clínica, cobros y facturación.  
Pendiente: confirmar este alcance con la investigación de mercado.

## Discovery — Segunda ronda

1\. Proceso actual. Mencionaste papel, planillas y mensajes. ¿Cómo se coordina un turno de principio a fin en los consultorios que conocés? Si no tenés un caso observado, lo dejamos como hipótesis. Esto permite comparar la propuesta con el flujo real, sin investigar competidores todavía.

2\. Reglas de agenda. ¿Qué reglas ya tenés definidas sobre disponibilidad de profesionales y sillones, cambios de duración o cancelaciones? Por ejemplo: ¿recepción puede ajustar la duración sugerida por la prestación? Esto define qué debe impedir o permitir el sistema.

3\. Integraciones. Con WhatsApp, recordatorios y facturación fuera del MVP, ¿la primera versión puede funcionar sola o necesita importar datos de una planilla o conectarse a un calendario existente? Esto separa dependencias necesarias de mejoras futuras.

4\. Restricciones. ¿La primera versión está pensada para un consultorio piloto, varias sedes o todavía no está decidido? ¿Hay alguna exigencia fija de entrega, tecnología o manejo de datos de pacientes? Esto delimita qué tiene que soportar la primera versión.

### RESPUESTAS POR CLAUDE 

1. hipótesis  
2.  Reglas de agenda  
    Acá sí pueden decidir ustedes, porque son reglas del producto, no hechos del mercado. Les dejo las preguntas para pensar:  
* ¿Un turno ocupa a la vez un profesional y un sillón? ¿Puede haber un turno en un sillón sin profesional asignado, o al revés?  
* ¿La duración sale de la prestación? ¿Recepción puede cambiarla en un turno puntual?  
* ¿Qué pasa si un turno empieza justo cuando termina otro? (El PDF lo menciona como caso borde del change.)  
* Al cancelar, ¿el horario se libera y queda registro del turno cancelado? ¿Y al reprogramar, queda registro del original?  
* ¿Hay horarios de atención por profesional (días y franjas)?

Lo que no decidan hoy: **"pendiente"**.

3.  **Integraciones**  
    Esta se desprende de lo que ya definieron: WhatsApp, recordatorios y facturación están fuera del MVP. La pregunta es si la primera versión necesita importar una planilla o un calendario. Por el alcance del change (API o módulo sin interfaz gráfica), lo razonable es que **funcione sola**, y que importación y calendarios queden como "pendiente / etapas posteriores". Pero confirmenlo ustedes.  
     
4.  **Restricciones**  
    Acá sí hay datos firmes, y salen de los documentos que tenés:  
* **Entrega:** jueves 26/11, 12:00 hs, sin entregas fuera de término.  
* **Repo:** GitHub público, con .gitignore, sin secretos.  
* **Datos:** nada de datos reales de pacientes, solo ficticios.  
* **Implementación:** el PDF dice que puede ser una API o un módulo con lógica de dominio, sin interfaz gráfica, y que el stack lo definen ustedes.  
* **Piloto o varias sedes:** no está decidido. Sugerencia: un solo consultorio, y sucursales como "pendiente". Es decisión del grupo.  
* **Stack:** pendiente, se define en la knowledge-base.

### RESPUESTAS POR CLAUDE (2da iteracion)

**4\. Restricciones Alcance: 3 sedes.** La primera versión está pensada para una organización con 3 sedes, no para un consultorio piloto.

* Por sede: de 1 a 3 odontólogos, de 1 a 2 asistentes y 1 recepcionista.  
* 1 administrador a cargo de todas las sedes.  
* Cada odontólogo atiende en una sola sede.  
* Horarios de atención por sede: turno mañana de 8:30 a 13:00 y turno tarde de 16:00 a 21:00.

**Exigencias de entrega y proceso (firmes):**

* Entrega el jueves 26/11 a las 12:00 hs, sin prórroga.  
* Repositorio público en GitHub con `.gitignore` correcto y sin secretos.  
* Se prioriza la velocidad de entrega para validar el flujo de turnos antes de una etapa productiva.

**Datos de pacientes:**

* La primera versión usa **solo datos ficticios**: ningún dato real de personas en el repo, en los ejemplos ni en las pruebas.  
* El cliente no declaró exigencias de infraestructura ni normativas. Eso queda como **pendiente de verificar en la investigación**, porque los datos de salud suelen tener tratamiento legal específico en Argentina. Si el sistema llegara a producción, habría que analizarlo antes.

**Tecnología:**

* Lo definido hasta ahora es que la implementación puede ser una API o un módulo con lógica de dominio.  
* Stack: **pendiente**, se define en la knowledge-base.  
* Interfaz gráfica: **pendiente**. Hay que confirmar con el docente si hace falta front, porque el PDF dice que no y el formato de entrega habla de front y backend en el mismo repo.

**Pendientes que afectan este punto:**

* Si un sillón pertenece a una sola sede.  
* Qué puede hacer el asistente con la agenda.  
* Si recepción de una sede puede ver o modificar otras sedes.  
* Si el change cubre las 3 sedes o una sola.

---

**Por qué cambié lo de Juan Cruz.** Él escribió "no hay normativas legales complejas". Yo no lo dejaría así, porque el agente lo toma como un hecho firme y la knowledge-base quedaría con una suposición sin respaldo. Es muy probable que existan obligaciones sobre datos personales de salud (por ejemplo, la Ley 25.326 de protección de datos personales, de la que me acuerdo sin haberla verificado ahora). Con datos ficticios el riesgo hoy es mínimo, pero conviene dejarlo como pregunta abierta para que el Discovery lo investigue. Así, la sección "Seguridad y cumplimiento" del informe tiene un punto concreto para verificar.

**Antes de mandarlo:** avisá a los otros integrantes y anotá en `NOTAS.md` la fecha y quién decidió "3 sedes" y "solo datos ficticios". Cuando el agente conteste, fijate que no vuelva a hablar de "consultorio piloto".

### Corrección a la ronda 1:

2: Usuarios (segunda versión) 

Agregamos nuevo usuario:  

Asistente dental: consulta la agenda del sillon  
Pendiente: permisos exactos de cada rol incluido asistente dental.

3\. Casos de uso (agregado): *Como asistente dental, quiero consultar la agenda del sillón para el día, para preparar el instrumental adecuado antes de que pase el próximo paciente.*

### RESPUESTAS POR JUAN CRUZ

1. hipótesis  
2.  Reglas de agenda:	  
* ¿Un turno ocupa a la vez un profesional y un sillón? No, no necesariamente, puede abarcar solo al profesional y no necesitar el uso de un sillón en caso de por ejemplo una radiografía. ¿Puede haber un turno en un sillón sin profesional asignado, o al revés? No puede haber un turno  en un sillón sin un profesional asignado; todos los turnos requieren obligatoriamente un odontólogo responsable, incluso en prácticas como las radiografías. Sin embargo, sí puede existir un turno con profesional pero sin sillón asignado, por ejemplo, cuando el estudio se realiza en una sala de rayos.  
* ¿La duración sale de la prestación? ¿La recepción puede cambiar la duración en un turno puntual? Si, la duración sale de la prestación, según el tipo de consulta que el cliente solicite. No, la recepción no puede cambiar la duración de un turno puntual.  
* ¿Qué pasa si un turno empieza justo cuando termina otro? (El PDF lo menciona como caso borde del change.) No es posible agendar una cita de forma consecutiva e inmediata. Es indispensable reservar una franja intermedia (aproximadamente de 5 a 10 minutos) dedicada a la higienización del espacio y al acondicionamiento del equipo encargado por parte del asistente dental.  
* Al cancelar, ¿el horario se libera y queda registro del turno cancelado? ¿Y al reprogramar, queda registro del original? Si, se libera el horario y queda registro del turno. Al reprogramar también queda el registro del original y se hace la modificación.   
* ¿Hay horarios de atención por profesional (días y franjas)? No trabajamos con franjas y días por cada profesional sino que por cada sede. 


3.  **Integraciones** (pendiente)  
4. **Restricciones**  
   ¿La primera versión está pensada para un consultorio piloto, varias sedes o todavía no está decidido? ¿Hay alguna exigencia fija de entrega, tecnología o manejo de datos de pacientes? Esto delimita qué tiene que soportar la primera versión. El sistema abarca 3 sedes, Por sede: de 1 a 3 odontólogos, de 1 asistente al menos por odontólogo y 1 recepcionista, con horarios por sede de 8.30 a 13 mañana y tarde de 16 a 21\. La entrega es el jueves 26/11 a las 12 hs. Los datos de los pacientes son datos ficticios No hay exigencias normativas declaradas, pero por tratarse de datos de salud queda como pendiente de verificar en la investigación 

## Discovery — Última ronda

1\. Asistente dental. Cuando decís «segunda versión», ¿te referís a la lista corregida de usuarios del MVP o a una versión posterior? Esto define cuándo entra la consulta de la agenda diaria del sillón para preparar el instrumental.

2\. Pausa de higienización. ¿La pausa bloquea el sillón, al odontólogo o a ambos? ¿También se aplica a turnos sin sillón, como radiografías? Podemos dejar los 5–10 minutos como rango pendiente si todavía no hay una duración definida.

3\. Alternativas conocidas. Además de papel, planillas y mensajes, ¿hay algún sistema que ya conozcas y quieras anotar como referencia para la investigación posterior? Si no, lo dejamos pendiente; no voy a buscarlo ahora.

4\. Riesgos y preguntas abiertas. De lo que falta resolver —validar el MVP, precisar permisos, definir integraciones o verificar requisitos para datos reales—, ¿qué te preocupa más de cara a la entrega? ¿Falta algún riesgo importante?

### RESPUESTAS POR CLAUDE 

## 

**1\. Asistente dental.** "Segunda versión" se refiere a la lista corregida de usuarios del MVP, no a una versión posterior del producto. Por lo tanto, el asistente dental entra en el MVP, pero solo con consulta: ve la agenda diaria del sillón para preparar el instrumental y la higienización. Qué más puede hacer (crear, reprogramar, cancelar) queda **pendiente**, junto con los permisos exactos de todos los roles.

**2\. Pausa de higienización.** Entre dos turnos consecutivos se reserva una pausa de 5 a 10 minutos para limpiar el espacio y acondicionar el equipo, a cargo del asistente dental.

* **Sillón:** la pausa bloquea el sillón, porque se limpia el espacio y el equipo.  
* **Odontólogo:** si la pausa también lo bloquea queda **pendiente**.  
* **Turnos sin sillón (por ejemplo, radiografías en sala de rayos):** si la pausa aplica queda **pendiente**.  
* **Duración:** el rango de 5 a 10 minutos queda **pendiente**, sin un valor fijo todavía.

**3\. Alternativas conocidas.** Además de papel, planillas y mensajes, el grupo no conoce ningún sistema que quiera anotar como referencia. Queda **pendiente** y lo resuelve la investigación de mercado.

**4\. Riesgos y preguntas abiertas.**

* **Reglas de agenda sin cerrar.** Falta definir los permisos de cada rol, a quién bloquea la pausa y si el sillón pertenece a una sola sede. Si no se resuelven antes del change, la especificación puede quedar con reglas inventadas.  
* **Datos reales y normativa.** Se trabaja solo con datos ficticios, pero por tratarse de datos de salud hay que verificar los requisitos legales en la investigación.  
* **Alcance de la entrega.** Falta confirmar con el docente si hace falta interfaz gráfica y si el change cubre las 3 sedes o una sola.  
* **Plazo.** Entrega el jueves 26/11 a las 12:00, sin prórroga, con 6 integrantes que coordinar.  
* **Confiabilidad del Discovery.** El agente puede inventar datos de competidores, por eso hay que verificar a mano al menos 5 fuentes.

---

Antes de pegarlo, revisen estas dos cosas:

1. **Cantidad de asistentes.** La corrección dice "1 a 2 asistentes por sede", pero Juan escribió "al menos 1 asistente por odontólogo", que con 3 odontólogos serían 3 asistentes o más por sede. Hay que unificarlo o dejarlo **pendiente**, para que la knowledge-base no quede con dos versiones.  
2. **Caso borde del change.** El PDF pide un escenario de un turno que empieza justo cuando termina otro. Con la regla de la pausa, ese turno **no se puede agendar**, así que el escenario pasa a ser de rechazo por falta de pausa. Conviene aclararlo en las notas para que la spec lo refleje.

La pregunta 4 es sobre qué les preocupa a ustedes. Si el grupo prioriza otro riesgo, cámbienlo antes de mandar.

### RESPUESTAS POR JUAN CRUZ

**1\. Asistente dental.** "Segunda versión" se refiere a la lista corregida de usuarios del MVP, no a una versión posterior del producto. Por lo tanto, el asistente dental entra en el MVP, pero solo con consulta: ve la agenda diaria del sillón para preparar el instrumental y la higienización. 

**2\. Pausa de higienización.** Entre dos turnos consecutivos se reserva una pausa de 5 a 10 minutos para limpiar el espacio y acondicionar el equipo, a cargo del asistente dental.

* **Sillón:** la pausa bloquea el sillón, porque se limpia el espacio y el equipo.  
* **Odontólogo:** La pausa lo bloquea hasta que el asistente dental termine.   
* **Turnos sin sillón (por ejemplo, radiografías en sala de rayos):** La pausa aplica, porque aun así el asistente dental debería de preparar los utensilios necesarios para el turno.  
* **Duración:** el rango de 5 a 10 minutos queda confirmado.

**3\. Alternativas conocidas:** Queda **pendiente** y lo resuelve la investigación de mercado.

**4\. Riesgos y preguntas abiertas.**

* **Reglas de agenda sin cerrar.** **Roles:** recepción crea, reprograma, cancela y consulta turnos; odontólogo y asistente solo consultan; administrador configura (Sedes, Sillones, Profesionales, Prestaciones, Usuarios) se encarga de todas las sedes, si en el futuro se amplía la cantidad de sedes se necesitara de más administradores pero por ahora solo de uno. Pendiente: permisos de edición del asistente, acceso de recepción a otras sedes, y si el odontólogo puede bloquear horarios.   
* **Datos reales y normativa.** Se trabaja solo con datos ficticios, pero por tratarse de datos de salud hay que verificar los requisitos legales en la investigación y también de cómo se trabaja en una odontología.  
* **Alcance de la entrega:** Interfaz gráfica es necesaria, el change cubre las 3 sedes (actuales)  
* **Plazo.** Entrega del proyecto es  el a las  jueves 26/11 a las 12:00, sin prórroga.  
* **Confiabilidad del Discovery.** El agente puede inventar datos de competidores, por eso hay que verificar a mano al menos 5 fuentes

## Discovery — Resumen para confirmar

**La primera versión será una agenda interna con interfaz gráfica para tres sedes. El asistente dental entra en el MVP con acceso de solo consulta; el paciente queda fuera.**

**\#    Punto    Resultado de la entrevista**

**1    Problema    La hipótesis es que coordinar turnos con papel, planillas y mensajes genera superposiciones por odontólogo o sillón, ausencias sin aviso y trabajo manual para recepción. No se conoce la frecuencia de estos problemas ni el tiempo perdido.**

**2    Usuarios    Recepción crea, reprograma, cancela y consulta turnos. Odontología consulta su propia agenda. Asistencia dental consulta la agenda diaria del sillón para preparar el instrumental y la higienización. Un administrador configura sedes, sillones, profesionales, prestaciones y usuarios para las tres sedes.**

**3    Casos de uso    Crear un turno sin solapamientos; reprogramarlo sin perder el registro original; cancelarlo y liberar el horario; consultar la agenda diaria propia del odontólogo; consultar la agenda diaria del sillón; configurar la operación de las sedes.**

**4    Alternativas    Pendientes para la investigación de mercado. No se investigaron competidores en esta etapa.**

**5    Imprescindibles    Interfaz gráfica, gestión de turnos, prevención de solapamientos, duración según prestación y agendas por odontólogo y sillón. El alcance se contrastará con la investigación de mercado.**

**6    Post-MVP    Reserva en línea para pacientes, WhatsApp y recordatorios, ficha clínica, cobros y facturación.**

**7    Reglas de agenda    Todo turno requiere odontólogo; el sillón es opcional. Recepción no puede modificar la duración fijada por la prestación. Entre turnos se reserva una pausa de 5–10 minutos, a cargo del asistente, que bloquea al odontólogo y, si está asignado, al sillón. También aplica a turnos sin sillón. Cancelar o reprogramar libera el horario anterior y conserva el registro. La agenda usa horarios por sede, no por profesional.**

**8    Integraciones    Pendientes de definir. No se da por requerida ninguna en el MVP.**

**9    Restricciones    Tres sedes, con posibilidad de sumar más. Por sede: 1–3 odontólogos, al menos un asistente por odontólogo y un recepcionista. Horarios: 08:30–13:00 y 16:00–21:00. Solo datos ficticios. Entrega: jueves 26/11/2026 a las 12:00, sin prórroga.**

**10    Riesgos    Reglas de agenda y permisos aún abiertos frente a un plazo fijo. La investigación posterior deberá verificar manualmente al menos cinco fuentes para evitar afirmaciones no sustentadas sobre competidores.**

**11    Preguntas abiertas    Cómo se elige el valor exacto de la pausa dentro del rango; si recepción puede acceder a otras sedes; si el odontólogo puede bloquear horarios; qué edición podría habilitarse al asistente más adelante, sin darle edición en el MVP; qué integraciones se necesitan; cómo operan los consultorios y qué requisitos legales aplicarían con datos reales.**

###  Respuestas a preguntas abiertas:  Juan Cruz

**Si recepción puede acceder a otras sedes; No, recepción solo tiene acceso a su propia sede.** 

**si el odontólogo puede bloquear horarios:** 

**El odontólogo trabaja por turnos, no es necesario esta opción**

**Si un sillón pertenece a una sola sede: Si, un sillón pertenece a una sola sede**

Cómo queda el caso borde del PDF: con la pausa obligatoria, un turno que empieza justo cuando termina otro se rechaza. Conviene registrarlo, porque define un escenario de la spec: Ya dijimos que entre turnos habrá un lapso de 5 a 10 minutos de acondicionamiento, higienización o hasta descanso entre turno y turno.

### Corrección por Juan Cruz: 

 **falta fijar la pausa del MVP dentro de los 5–10 minutos**

**Duración:** el rango de 10 minutos queda confirmado. (**Debe de estar fijo)**

**decidir cómo el asistente ve los turnos sin sillón**

	El asistente en los turnos sin sillón también queda a cargo de ayudar al Odontólogo a preparar la maquinaria. 

  **qué pasa en los límites del horario de cada sede.** 

Cada sede trabaja de la misma forma: Turno tarde, turno noche con los horarios establecidos con anterioridad 

**También conviene confirmar con el docente la exigencia de interfaz gráfica para la entrega.**

La interfaz gráfica es obligatoria 

 

—--------—---------------—-------- FIN ENTREVISTA \-----—--------—--------—--------

# \#\# 24 \- 11 \- 2026 

\- **Revisó**: Martin Caballero

\- **Decisión**: Ajustar

\- **Hallazgo**: El informe mezclaba la entrevista con la investigación de mercado.

\-**Cambio**: Borramos la entrevista \`docs/discovery/informe-discovery.md\`

esta solo estará en el archivo   \`docs/discovery/checklist-discovery.json\`

  y el contenido de mercado estará en  \`docs/discovery/[informe-discovery.md](http://informe-discovery.md)\`

# 24 \- 11 \- 2026 Verificación  de la fuente

Verificación: Juan Cruz

| Fuente (urly fecha) | Qué afirmaba el informe    | Qué encontramos en la fuente  | Qué corregimos |
| :---- | :---- | :---- | :---- |
| [https\://odonflow.com/](https://odonflow.com/) ·  · 24/11/2026  | Multiodontólogo y multisillón, sobreturnos y espera | La página anuncia de manera directa: “**Agenda** multi-profesional y multi-sillón, con arrastre, sobreturnos y lista de espera.” | Sin cambios  |
| [DentalCore — Términos y Condiciones](https://dentalcore.app/terms) · 24/11/2026  | Para facturar con ARCA, el profesional necesita su propio CUIT, certificado digital y punto de venta habilitado | En el punto 5 de los términos aparecen estos tres requisitos. La página respalda bien lo que dice en el informe | Sin cambios  |
| [Dentalink — página de agenda](https://www.softwaredentalink.com/ar/experiencia-de-pacientes/agenda) · 24/11/2026  | Muestra la ocupación de los sillones, pero no hay una demostración de que se impida guardar dos turnos superpuestos  | La página muestra distintas vistas (diaria, semanal y general) de los sillones ayudando a evitar sobre agendamientos, pero también menciona una agenda paralela para agendar. No aplica una regla que bloquee los turnos superpuestos   | Sin cambios |
| [https\://dentix.com.ar/](https://dentix.com.ar/) · 24/11/2026 | Anuncia columnas por sillón y profesional, y permite bloquear horarios.  Primer mes bonificado; ARS 30.000 / 40.000 / 50.000 mensuales equivalentes con facturación anual.  Recordatorios por correo y WhatsApp según plan. | La página de manera explícita afirma: \-Agenda inteligente. \-Una columna por sillón y por profesional.  \-Bloquea horarios. \-Se observan las bonificaciones mensuales al pago del servicio anualmente. \-Recordatorios de turno: Se envían solos, sin que tengas que acordarte. Por email se envían en todos los planes; por WhatsApp solo en el plan Pro y Max, con un cupo mensual incluido en el precio. | Sin cambios |
| [https\://agendapro.com/ar/planes](https://agendapro.com/ar/planes) · 24/11/26 | Publica precios en ARS (9.900 / 19.900 / 39.900 / 129.900 con IVA para cuentas nuevas) y adicionales según el plan.  | Confirma esos cuatro valores mensuales en ARS con IVA incluido para cuentas nuevas y los adicionales por profesional extra, ficha clínica (+\$10.000 en Individual), Recepcionista IA y paquetes extra de WhatsApp (con cargos de Meta aparte al usar número propio). Respalda lo que dice el informe  | sin cambios  |

—--------—--------—--------—--------FIN ETAPA 2-----—--------—--------—--------—--------

# ETAPA 3: