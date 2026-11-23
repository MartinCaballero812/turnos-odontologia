# Informe de Discovery — Turnos Odontología

Fuente: entrevista de producto confirmada por el solicitante. No se realizó investigación de mercado ni se consultaron fuentes externas. El [checklist estructurado](checklist-discovery.json) registra los mismos once puntos y distingue decisiones, hipótesis y pendientes.

## 1. Problema

Se busca reducir el trabajo manual de coordinar turnos en consultorios odontológicos. La hipótesis es que el uso de papel, planillas y mensajes favorece las superposiciones por odontólogo o sillón, las ausencias sin aviso y el tiempo administrativo perdido. Recepción carga y corrige turnos; odontología se ve afectada por huecos o superposiciones en la agenda. No se conoce la frecuencia de esos problemas ni el tiempo que insume coordinarlos.

## 2. Usuarios y roles del MVP

| Rol | Alcance confirmado |
|---|---|
| Recepción | Crea, reprograma, cancela y consulta turnos exclusivamente en su sede. |
| Odontólogo | Consulta su propia agenda diaria. No se requiere que pueda bloquear horarios. |
| Asistente dental | Solo consulta la agenda diaria del sillón para preparar el instrumental y la higienización. |
| Administrador | Un administrador configura sedes, sillones, profesionales, prestaciones y usuarios para las tres sedes. |

El paciente no usa el sistema en el MVP. La edición por parte del asistente no está habilitada en esta versión; su eventual alcance queda abierto.

## 3. Casos de uso

- Como recepcionista, quiero crear un turno sin conflictos por profesional ni por sillón, cuando corresponda, para evitar dobles reservas.
- Como recepcionista, quiero reprogramar un turno para liberar el horario anterior sin perder su registro.
- Como recepcionista, quiero cancelar un turno para liberar el horario y conservar el registro de la cancelación.
- Como odontólogo, quiero consultar mi agenda diaria para saber qué pacientes atiendo y cuándo.
- Como asistente dental, quiero consultar la agenda diaria del sillón para preparar el instrumental y el espacio antes de que pase el próximo paciente.
- Como administrador, quiero configurar sedes, sillones, profesionales, prestaciones con duración propia y usuarios para que la agenda refleje la operación del consultorio.

## 4. Alternativas y competidores

Papel, planillas y mensajes son referencias sobre el proceso actual, no observaciones verificadas. No se identificaron ni evaluaron competidores en esta entrevista. El análisis queda para la investigación de mercado por separado.

## 5. Funcionalidades necesarias

Interfaz gráfica para gestionar turnos en las tres sedes, prevenir solapamientos, respetar la duración de cada prestación y la pausa entre turnos, y consultar agendas diarias por odontólogo y por sillón. El administrador debe poder configurar los recursos y usuarios de las sedes. El alcance del MVP sigue sujeto al contraste con la investigación de mercado.

## 6. Funcionalidades opcionales

Reserva en línea para pacientes, WhatsApp y recordatorios, ficha clínica, cobros y facturación quedan fuera del MVP.

## 7. Reglas de negocio

- Todo turno requiere un odontólogo responsable. El sillón es opcional, por ejemplo en una radiografía en sala de rayos. No se admite un turno con sillón y sin odontólogo. Cada sillón pertenece a una sola sede.
- No pueden superponerse turnos que comparten odontólogo o sillón asignado.
- La prestación determina la duración del turno. Recepción no puede modificar esa duración para un turno puntual.
- Entre turnos consecutivos se reserva una pausa de **5 a 10 minutos** para acondicionamiento, higienización o descanso. El asistente dental se encarga de preparar el espacio y el equipo. La pausa bloquea al odontólogo y al sillón cuando está asignado. También aplica al odontólogo en turnos sin sillón, donde sigue haciendo falta preparar el instrumental.
- **Caso borde para la futura especificación:** dado un turno existente, cuando se intenta agendar otro que comparte odontólogo o sillón y empieza justo al finalizar el anterior, se rechaza el nuevo turno porque no respeta la pausa obligatoria.
- Cancelar libera el horario y conserva el registro del turno cancelado. Reprogramar libera el horario anterior y conserva el registro original.
- Los horarios de atención se definen por sede, no por profesional.

## 8. Integraciones

Pendientes de definir. No se confirmó ninguna integración obligatoria para el MVP.

## 9. Restricciones

- Tres sedes en el MVP, con capacidad de sumar más. Por sede: de uno a tres odontólogos, al menos un asistente por odontólogo y un recepcionista.
- Horarios por sede: **08:30–13:00** y **16:00–21:00**.
- Interfaz gráfica obligatoria. Para la entrega se usan exclusivamente datos ficticios.
- Entrega: **jueves 26 de noviembre de 2026 a las 12:00**, sin prórroga.

## 10. Riesgos

- Sin validar la frecuencia de las superposiciones y las ausencias ni medir la carga administrativa, no se sabe si el MVP prioriza los problemas más relevantes.
- Las reglas de agenda y permisos que sigan abiertos pueden producir retrabajo frente a un plazo fijo.
- No se verificaron los requisitos legales para usar datos de pacientes reales ni las prácticas operativas de los consultorios. La entrega con datos ficticios no valida un uso con datos reales.
- Para evitar afirmaciones no sustentadas sobre competidores, la investigación posterior debe verificar manualmente **al menos cinco fuentes**. Esa verificación no forma parte de esta entrevista.

## 11. Preguntas abiertas

- ¿Cómo se determina la duración exacta de la pausa dentro del rango de 5 a 10 minutos?
- ¿Qué permisos de edición podrían habilitarse para el asistente después del MVP?
- ¿Qué integraciones se necesitan, si alguna?
- ¿Con qué frecuencia ocurren superposiciones y ausencias, cuánto tiempo demanda la coordinación y qué implican esos datos para las prioridades del MVP?
- ¿Cómo trabajan en la práctica los consultorios y qué requisitos legales aplicarían si se usaran datos de pacientes reales?

**Punto de parada:** la entrevista termina acá. La investigación de mercado se hará por separado, solo cuando se solicite. La base de conocimiento queda pendiente hasta después de esa investigación y usará `docs/discovery/` como fuente.
