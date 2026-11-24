# Informe de Discovery — Turnos Odontología

- **Materia:** Metodología I
- **Carrera:** Tecnicatura Universitaria en Programación
- **Integrantes:** Martin Caballero · Juan Simón Ledda · Juan Cruz Reveco · Bruno Santiago Tejada
- **Fecha del informe:** 24 de noviembre de 2026

## Resumen ejecutivo

Se compararon 16 sistemas de gestión odontológica, con prioridad en Argentina y referencias de Latinoamérica e internacionales. El relevamiento examinó las agendas, la automatización, las funciones clínicas, las integraciones, los precios y la evidencia pública disponible. Las fuentes describen ofertas comerciales; no se realizaron pruebas en producción ni se contactó a proveedores.

El hallazgo principal es que **mostrar una agenda por sillón no equivale a impedir una doble reserva**. Codental documenta avisos que permiten guardar y AgendaPro documenta excepciones configurables para reservas internas. No se comprobó en las fuentes revisadas la combinación exacta del proyecto: odontólogo obligatorio, sillón opcional, rechazo de solapamientos y pausa fija. Tampoco se encontró una estimación argentina representativa del ausentismo odontológico, las superposiciones o el tiempo administrativo perdido.

Se recomienda un **MVP de agenda gráfica interna para tres sedes**, con duración por prestación, separación de permisos por rol y sede, prevención de conflictos, pausa de **10 minutos** e historial de cancelaciones y reprogramaciones. Se postergan la autorreserva, WhatsApp, la ficha clínica y la facturación. Las decisiones de la entrevista se conservan en el [checklist de 11 puntos](checklist-discovery.json).

## Metodología y alcance

**Fecha de consulta de todas las URLs citadas: 23 de noviembre de 2026.** Ámbito: Argentina, Latinoamérica y referencias internacionales. Se comparan 16 productos distintos. El orden de la sección A prioriza disponibilidad y adaptación a Argentina, proximidad al caso de tres sedes y utilidad como referencia; no representa cuota de mercado ni coincide necesariamente con la puntuación de la sección B.

**Método.** Primero se buscaron soluciones argentinas y ofertas regionales localizadas; después se incorporaron referentes con documentación útil para comparar recursos de agenda, experiencia del paciente y trazabilidad. Se leyeron las páginas citadas, incluidos manuales, términos y precios cuando estaban disponibles. Los resultados de buscadores se usaron para descubrir productos, no como prueba. Se priorizó tener al menos una fuente primaria consultable por producto y se contrastaron más de cinco dominios de proveedores. No se contrataron planes, no se ingresaron datos de pacientes y no se efectuaron pruebas en cuentas de producción. Es un relevamiento amplio de oferta pública, no un censo de todas las soluciones instaladas.

**Cómo leer la evidencia**

- **D — Documentado:** se consultó un manual, centro de ayuda o documentación técnica que describe pasos o comportamiento concreto. No equivale a una prueba propia.
- **P — Publicado:** el dato figura en una política, contrato o tabla oficial de precios. Prueba qué se publica, no el cumplimiento efectivo ni el precio final de una cotización.
- **C — Comercial:** afirmación, captura, caso de éxito o cifra difundida por el propio proveedor. No se verificó funcionalmente. Una reseña verificada se atribuye a su plataforma y no se usa como prueba de una función actual.
- **No evidenciado:** no hay respaldo suficiente en las fuentes consultadas. No significa que la función no exista. En las fichas, **toda función, integración, métrica o condición comercial no enumerada se considera «No evidenciado»**. Cuando el proveedor declara una exclusión, se indica expresamente.

## A. Tabla comparativa

Los precios son referencias de la consulta, sin conversión de divisa. En productos con planes de entrada, el precio inicial no implica que incluya tres sedes, WhatsApp, ficha clínica o facturación.

| # | Producto, proveedor y origen | Segmento y modalidad | Agenda y diferencial principal | Modelo comercial público | Fuente y fecha |
|---|---|---|---|---|---|
| 1 | [OdonFlow](https://odonflow.com/) — Centro Privado Salud S.R.L., Argentina | Consultorio, clínica y cadenas; SaaS web | **C:** multiodontólogo y multisillón, sobreturnos y espera; ARCA, Mercado Pago y obras sociales | **P:** mensual por clínica; Básico, Pro y Full, a cotizar. | [producto y planes](https://odonflow.com/) · 2026-11-23 |
| 2 | [DentalCore](https://dentalcore.app/) — DentalCore, operado por Alfredo Di Tullio, Argentina | Independiente y clínica; SaaS web | **C:** agenda por profesional, WhatsApp oficial, controles y gestión fiscal local; declara que todavía no ofrece autorreserva | **P:** prueba de 7 días; términos: USD 50/mes, dos usuarios, USD 10 por usuario adicional. Portada argentina: ARS 75.000 y ARS 15.000 adicionales; confirmar moneda contractual. | [producto](https://dentalcore.app/) · 2026-11-23 |
| 3 | [Dentidad](https://www.dentidad.com/) — Solvianweb, Argentina | Independiente, consultorio y cadena; SaaS web | **C:** reserva por profesional, seña con Mercado Pago, factura C y plan multisede | **P:** prueba de 14 días. Precios de lista ARS 50.000 / 85.000 / 300.000 al mes; promoción publicada del 50 % por tres meses. | [producto, flujo y precios](https://www.dentidad.com/) · 2026-11-23 |
| 4 | [Dentalink](https://www.softwaredentalink.com/ar) — Healthatom / Engenis SpA, Chile; oferta argentina | Independiente, clínica y cadena; SaaS web | **C:** agenda de sillones, portafolio clínico, coberturas argentinas y Contact Center IA | **P:** cotización, pagos recurrentes; promoción del Contact Center IA: USD 50/mes, hasta 80 conversaciones, solo un profesional. No es el precio base. | [agenda](https://www.softwaredentalink.com/ar/experiencia-de-pacientes/agenda) · 2026-11-23 |
| 5 | [AgendaPro](https://agendapro.com/ar/dental/software-odontologico) — AgendaPro SpA, Chile; oferta argentina | Independiente, clínica y multisede; SaaS web multisectorial | **D:** horarios por recurso y sobreturnos internos configurables; **C:** odontograma, WhatsApp y Mercado Pago | **P:** ARS 9.900 / 19.900 / 39.900 / 129.900 al mes con IVA, cuentas nuevas; adicionales por profesional, módulo clínico, IA o mensajería según plan. | [configuración de agenda](https://ayuda.agendapro.com/es/articles/5924647-opciones-de-configuracion-para-la-agenda) · 2026-11-23 |
| 6 | [DentiX](https://dentix.com.ar/) — marca DentiX, Argentina como mercado; origen societario y razón social: **No evidenciado** | Independiente y consultorio; SaaS web | **C:** columnas por sillón y profesional con bloqueos, facturación ARCA, gestión de ausencias | **P:** primer mes bonificado; ARS 30.000 / 40.000 / 50.000 mensuales equivalentes con facturación anual; esquema de clínica y cupos de WhatsApp. | [producto, agenda y precios](https://dentix.com.ar/) · 2026-11-23 |
| 7 | [DentalHub](https://dentalhub.com.ar/) — Closure S.A., Argentina | Independiente, consultorio y clínica; SaaS web | **C:** agenda por profesional, permisos, portal y enlaces de Mercado Pago | **P:** prueba de 7 días; oferta de ARS 41.999/mes Profesional y ARS 94.999/mes Clínica. El descuento anual no coincide entre portada y página de precios. | [agenda](https://dentalhub.com.ar/agenda-odontologica) · 2026-11-23 |
| 8 | [Dentatools](https://dentatools.co/ar/) — marca Dentatools; oferta argentina, origen y razón social: **No evidenciado** | Independiente y consultorio con recepción; SaaS web | **C:** enlace de autorreserva con ocupación inmediata, WhatsApp y cobros particulares en pesos | **P:** ARS 76.500/mes con IVA, usuarios ilimitados, inicio gratuito sin tarjeta; duración exacta: **No evidenciado**. | [oferta argentina](https://dentatools.co/ar/) · 2026-11-23 |
| 9 | [OdontoGRAMA](https://odontograma.com.ar/) — SolSoftware / crédito a Julio Santolo, Argentina | Independiente, consultorio, clínica o centro; web en nube | **C:** asistencia por agenda, equipo con asistentes, caja, prepagas y obras sociales | **P:** acceso de prueba gratuito anunciado; tarifa, moneda y condiciones del plan contratado: **No evidenciado**. | [producto y acceso de prueba](https://odontograma.com.ar/) · 2026-11-23 |
| 10 | [DentalFlow](https://www.dentalflow.com.ar/) — HASHEM Publicidad, Argentina | Independiente, consultorio y clínica; SaaS web | **C:** vistas diaria, semanal y mensual; agenda enlazada al arqueo diario y acceso directo a WhatsApp | **P:** demo pública enlazada; precio y prueba de producción: **No evidenciado**. | [producto, pantallas ilustrativas y demo enlazada](https://www.dentalflow.com.ar/) · 2026-11-23 |
| 11 | [Citalo](https://www.citalo.app/ar/software-turnos-odontologos) — marca Citalo; jurisdicción publicada en Uruguay y oferta argentina; razón social y origen societario: **No evidenciado** | Independiente y clínica; SaaS web multisectorial | **C:** agenda de varios profesionales y sillones, duración por servicio y sistema de reserva compartible | **P:** plan gratuito; USD 14,99–119,99/mes en planes pagos, más USD 0,05 por mensaje excedente según tabla. | [vertical dental](https://www.citalo.app/ar/software-turnos-odontologos) · 2026-11-23 |
| 12 | [DenPro](https://www.denpro.ar/) — Elite Digital Services, LLC, operador registrado en EE. UU.; origen de desarrollo: **No evidenciado** | Independiente, clínica y cadenas; SaaS web con oferta argentina | **C:** agenda de equipo, odontograma y migración; alojamiento europeo declarado | **P:** ARS 19.900/mes Basic y ARS 29.900/mes Team en el sitio argentino, prueba de 30 días y 15 % anual; multisede a consultar. | [producto](https://www.denpro.ar/) · 2026-11-23 |
| 13 | [DENTIDESK](https://www.dentidesk.com/) — marca DENTIDESK, Chile | Clínica, centro multidisciplinario e instituciones; web en nube y app | **C:** multisucursal, agenda pública configurable y fichas por especialidad | **P:** prueba de 15 días y demo; monto, moneda y precio argentino: **No evidenciado**. | [producto, prueba y testimonios](https://www.dentidesk.com/) · 2026-11-23 |
| 14 | [Codental](https://www.codental.com.br/) — Codental, Brasil | Independiente, clínica y equipos; SaaS web | **D:** agenda por sillón con aviso que no bloquea; sincronización bidireccional con Google Calendar | **P:** prueba de 7 días; BRL 89,90 / 134,90 / 179,90 por mes de lista, campañas aparte; créditos de WhatsApp y emisión fiscal tienen cargos publicados. | [manual de sillones](https://ajuda.codental.com.br/agenda-por-cadeiras-como-organizar-sua-clinica-por-espaco-de-atendimento) · 2026-11-23 |
| 15 | [Dentally](https://www.dentally.com/en-gb/) — Henry Schein One UK Ltd, Reino Unido | Independiente, clínica y cadena; SaaS web | **D:** portal multisede con duración, señas, cancelación/reagenda e historial del turno | **P:** Reino Unido desde GBP 134/mes en tabla por tamaño y plan; oferta argentina: **No evidenciado**. | [manual de reserva](https://help.dentally.com/en/collections/2414893-online-booking) · 2026-11-23 |
| 16 | [Open Dental](https://www.opendental.com/) — Open Dental Software, EE. UU. | Consultorio, clínica y cadena; instalación local/autohospedada o nube adicional | **D:** agenda por operatorios y reglas por procedimiento; API y auditoría | **P:** licencia y soporte internacional general USD 89/mes por sede hasta tres profesionales; confirmar elegibilidad y extras Cloud/eServices. Hay versión de prueba. | [reglas de agenda](https://www.opendental.com/manual/appointmentrules.html) · 2026-11-23 |

### Fichas y trazabilidad

**1. OdonFlow.** **C:** agenda por profesional, sillón o mes, arrastre para mover turnos, bloqueo de casilleros, sobreturnos y lista de espera. Reserva web 24/7 y asistente que agenda desde WhatsApp, Instagram y Messenger; recordatorios y confirmación con WhatsApp oficial, recuperación de pacientes y seguimientos. Historia clínica con evoluciones firmadas e inmutables declaradas, odontograma, periodontograma, imágenes, ortodoncia, presupuestos y planes. Caja, cuotas, Mercado Pago, ARCA, convenios, nomenclador, autorizaciones, lotes y liquidación; informes Excel. Full anuncia firma digital y multisucursal. **P:** términos con Ley 25.326, respaldos diarios, exportación al terminar y soporte por WhatsApp. El sitio no permite concluir si la combinación profesional/sillón y la pausa rechazan todos los solapamientos o admiten excepciones según rol; tampoco cuantifica adopción verificable. Fuentes: [producto y planes](https://odonflow.com/), [funciones para Argentina](https://odonflow.com/software-odontologico-argentina), [términos](https://odonflow.com/terminos).

**2. DentalCore.** **C:** agenda diaria/semanal/mensual por profesional, arrastre y estados; recordatorios por WhatsApp oficial de Meta, correo o SMS, enlaces para confirmar o cancelar y liberar el turno, controles vencidos y tratamientos sin agendar. La FAQ excluye la autorreserva; la agenda se carga desde la clínica. Ficha clínica, odontograma, periodontograma, radiología, planes, caja, reportes, señas, Mercado Pago, ARCA C/B y liquidación de obras sociales. Declara 2FA, registro ineditable de lecturas clínicas y soporte en español. **P:** el contrato exige CUIT, certificado y punto de venta habilitado para ARCA; la semana gratuita se habilita a solicitud. Doble restricción profesional/sillón, formato de exportación integral y adopción: **No evidenciado**. Fuentes: [producto](https://dentalcore.app/), [agenda y limitación de autorreserva](https://dentalcore.app/agenda-odontologica), [contrato](https://dentalcore.app/terms).

**3. Dentidad.** **C:** agendas de día y semana, bloqueos, enlace público con elección de profesional, recordatorios por WhatsApp y email, respuesta de asistencia, liberación automática al cancelar o vencer la seña, y portal preconsulta. Historia clínica, anamnesis, odontograma, radiografías, presupuestos y seguimiento; caja, Mercado Pago con cuenta propia, factura C electrónica, reportes y datos de obra social. No se equipara registrar la cobertura con liquidarla. Plan multisede, importación Excel/CSV y soporte según plan. **P:** términos y política identifican encargado y responsable bajo Ley 25.326, respaldos y exportación; la ventana posbaja publicada es de 30–90 días. API oficial de WhatsApp, bloqueo por sillón, bitácora de cambios de agenda y adopción: **No evidenciado**. Fuentes: [producto, flujo y precios](https://www.dentidad.com/), [términos](https://www.dentidad.com/terminos), [privacidad](https://www.dentidad.com/privacidad).

**4. Dentalink.** **C:** vistas global, diaria y semanal por sillón y profesional, reserva online y recordatorios. El Contact Center IA anuncia llamadas y WhatsApp para reservar, confirmar, cancelar y reprogramar. Ficha, odontograma y periodontograma, imágenes, receta con firma digital declarada, presupuestos, seguimiento, cobros, caja, aranceles y conciliación de obras sociales; reportes, multisucursal y API como complemento según oferta. Declara ISO 27001, cifrado y respaldos diarios; no se cotejó certificado, alcance ni adecuación legal completa. **P:** términos y planes publican el marco de contratación y soporte; validar condiciones de continuidad, exportación completa y complementos. **Adopción:** el proveedor anuncia más de 15.000 clínicas/clientes globales y publica testimonios, no una cifra argentina. Capterra muestra dos reseñas verificadas de 2022, muestra insuficiente para inferir satisfacción general o funciones vigentes. ARCA y bloqueo duro doble: **No evidenciado** en las fuentes usadas. Fuentes: [oferta argentina](https://www.softwaredentalink.com/ar), [agenda](https://www.softwaredentalink.com/ar/experiencia-de-pacientes/agenda), [planes](https://www.softwaredentalink.com/ar/planes), [términos](https://www.softwaredentalink.com/ar/terminos-de-servicio), [Capterra verificado](https://www.capterra.es/software/196361/dentalink).

**5. AgendaPro.** **D:** se crean recursos con horario semanal y servicios asociados, visibles en la agenda; la ayuda distingue duración de prestación de granularidad visual. Los solapamientos por profesional y recurso son opciones para reservas internas; el paciente no puede reservar sobre un horario ya ocupado. La reserva interna fuera de horario es configurable. **C:** turnos 24/7, WhatsApp y chatbot en redes, recordatorios, campañas y recuperación; anamnesis, odontograma, radiografías, firmas electrónicas, presupuestos, caja, reportes, señas, honorarios por profesional y Mercado Pago. Multisede y ficha cifrada declarados. **P:** ficha clínica adicional en ciertos planes, IA y cargos Meta según configuración. **Adopción C:** más de 30.000 negocios anunciados en la oferta general; no son necesariamente odontológicos ni argentinos. ARCA, obras sociales y exportación clínica integral: **No evidenciado**. Fuentes: [vertical dental](https://agendapro.com/ar/dental/software-odontologico), [recurso con horario](https://ayuda.agendapro.com/es/articles/3326185-como-creo-un-recurso-con-horario), [configuración de agenda](https://ayuda.agendapro.com/es/articles/5924647-opciones-de-configuracion-para-la-agenda), [planes argentinos](https://agendapro.com/ar/planes), [términos](https://agendapro.com/ar/terminos-y-condiciones).

**6. DentiX.** **C:** vistas día/semana/mes con columnas por sillón y profesional, bloqueos, confirmaciones y gestión de ausencias. Recordatorios automáticos por correo y por WhatsApp según plan; ficha por enlace para que el paciente complete sus datos. Historia clínica, odontograma, tratamientos por pieza, presupuestos, pagos, egresos, indicadores y ARCA; registra obra social, sin prueba pública de liquidación. Migración desde Excel, soporte humano y cifrado/respaldos declarados. **P:** la política alude a Ley 25.326 y concede exportación durante 90 días tras la baja, con salvedad legal. Prevención dura, reserva pública 24/7, auditoría por operador y adopción: **No evidenciado**. Fuentes: [producto, agenda y precios](https://dentix.com.ar/), [política de privacidad](https://dentix.com.ar/privacy).

**7. DentalHub.** **C:** agenda semanal y compartida, bloqueos, varios profesionales en Clínica, recordatorios por WhatsApp y prevención de superposiciones anunciada sin precisar si cubre sillones. Portal para consultar turnos, presupuestos, pagos y documentos; reserva autónoma: **No evidenciado** en las fuentes usadas. Ficha, odontograma, plan, presupuestos, cobranzas, Mercado Pago, facturación genérica, permisos, reportes y soporte. **P:** política con cifrado en tránsito, aislamiento lógico, controles y referencia a Ley 25.326; no acredita auditoría integral. **Adopción C:** la portada declara más de 150 clínicas activas; sin corroboración independiente. ARCA, API oficial de WhatsApp y restauración de respaldos: **No evidenciado**. Fuentes: [producto](https://dentalhub.com.ar/), [agenda](https://dentalhub.com.ar/agenda-odontologica), [precios](https://dentalhub.com.ar/precios), [términos](https://dentalhub.com.ar/terminos), [privacidad](https://dentalhub.com.ar/privacidad).

**8. Dentatools.** **C:** agenda compartida y enlace de autorreserva que ocupa el horario inmediatamente; se comparte en web, redes o WhatsApp. Recordatorios automáticos por WhatsApp, sin prueba del canal API oficial. Ficha, anamnesis, odontograma, fotos y radiografías; cobros ARS, presupuestos, cuentas por cobrar, reportes y liquidación de profesionales. El sitio excluye expresamente autorizaciones y liquidación de obras sociales. Factura C anunciada corresponde a **la suscripción**, no a la facturación de prestaciones del consultorio. Roles, importación/migración, entrenamiento y soporte en español. **P:** política declara respaldos y los términos prevén 30 días para exportar tras terminar. La mención comercial de clínicas con varios sillones no prueba capacidad de reservar o bloquear un sillón. Multisede, ARCA clínico, auditoría y adopción: **No evidenciado**. Fuentes: [oferta argentina](https://dentatools.co/ar/), [precios](https://dentatools.co/ar/precios/), [términos](https://dentatools.co/terminos-y-condiciones/), [privacidad](https://dentatools.co/politica-de-privacidad/).

**9. OdontoGRAMA.** **C:** agendas personalizables, varios profesionales, control de asistencia y equipo que incluye asistentes. Ficha, diagnósticos, imágenes, odontograma, tratamientos, caja, cuentas corrientes, facturación a obras sociales/prepagas y estadísticas. El sitio anuncia protección y respaldos gestionados, sin detalle técnico o contractual suficiente. Autorreserva, WhatsApp, reglas de recursos, ARCA electrónica, exportación y adopción: **No evidenciado**. La página de SolSoftware enlaza el mismo producto: se cuenta una sola vez. Fuentes: [producto y acceso de prueba](https://odontograma.com.ar/), [ficha de SolSoftware](https://www.solsoftware.com.ar/odontograma/).

**10. DentalFlow.** **C:** agenda diaria, semanal y mensual vinculada a caja; el acceso a WhatsApp abre un chat, no prueba un recordatorio automático ni una integración API. Ficha con antecedentes y obra social, odontograma, carga de radiografías, caja, arqueo, gastos y contacto de soporte. Disponibilidad web y respaldo gestionado se anuncian sin detalle público de retención o restauración. El sitio dice «500+ Odontólogos en zona oeste», sin definir si son clientes activos; no se cuenta como adopción comprobada. Sillones simultáneos, autorreserva, ARCA, permisos granulares y exportación: **No evidenciado**. Fuente: [producto, pantallas ilustrativas y demo enlazada](https://www.dentalflow.com.ar/).

**11. Citalo.** **C:** agenda con personal, duración por servicio y varios sillones, con prevención anunciada; reserva 24/7 y página para compartir por WhatsApp, Instagram o Google. Publica confirmación/cancelación por WhatsApp, recordatorios, reprogramación, pagos, multiubicación, historial de pacientes, cifrado y permisos. El historial genérico no evidencia ficha odontológica, odontograma ni imágenes clínicas. La referencia al cobro de una suscripción en USD no implica facturación fiscal argentina para la clínica. ARCA, API oficial de WhatsApp, respaldo verificable, auditoría y adopción: **No evidenciado**. Fuentes: [vertical dental](https://www.citalo.app/ar/software-turnos-odontologos), [producto y precios](https://www.citalo.app/ar), [controles declarados](https://www.citalo.app/ar/funcionalidades/seguridad-privacidad), [términos](https://www.citalo.app/terms-conditions).

**12. DenPro.** **C:** agenda, citas online publicitadas, recordatorios SMS, fichas, odontograma, planes de tratamiento y roles de equipo. Publica facturación y exportaciones de seguros sin respaldo para ARCA ni obras sociales argentinas. **P:** contrato identifica operador estadounidense y permite exportar antes de cerrar; el proveedor declara RGPD, cifrado AES-256 y alojamiento en centros europeos ISO 27001. No equivale a conformidad argentina. Multiubicación tiene consulta comercial; bloqueo doble, WhatsApp API, bitácora de agenda y adopción: **No evidenciado**. Fuentes: [producto](https://www.denpro.ar/), [precios](https://www.denpro.ar/precios/), [términos](https://www.denpro.ar/informacion-legal/terminos-de-servicio/).

**13. DENTIDESK.** **C:** agenda con reprogramación, recordatorios, intervalos configurables, feriados bloqueados y reserva embebible por enlace. Los intervalos del calendario no acreditan duración variable por prestación. Fichas de odontograma, periodoncia y otras especialidades, radiografías, consentimientos, presupuestos, administración, stock y reportes. Multisucursal, usuarios, permisos y soporte. Declara facturación chilena SII y dominicana vía Alegra/DGII: no se puntúan como integraciones argentinas. El proveedor menciona WhatsApp automático como novedad futura en la publicación consultada; no se le asigna disponibilidad actual. Testimonios propios nombran clínicas e instituciones; cantidad verificable y exportación integral: **No evidenciado**. Fuentes: [producto, prueba y testimonios](https://www.dentidesk.com/), [funcionalidades](https://www.dentidesk.com/dentidesk/feature), [integraciones](https://www.dentidesk.com/dentidesk/solution).

**14. Codental.** **D:** columnas por sillón, incluso si el espacio se configura como sala de rayos; verifica conflictos con profesional, sillón y bloqueos. El manual dice que **advierte pero deja guardar** y que la comprobación descrita usa el inicio de la cita, no un bloqueo integral de todo el intervalo. Sincronización bidireccional con Google Calendar documentada, con permisos y detalles privados ocultos al equipo. **C:** reserva web, confirmación y recuperación con WhatsApp, anamnesis, historia, cobros y comisiones, logins y permisos; el proveedor distingue el complemento de WhatsApp de la mensajería oficial por créditos. **P:** condiciones y precios brasileños, facturación local y tratamiento de pagos mediante Asaas; no evidencia Mercado Pago, ARCA ni adecuación argentina. **Adopción C:** más de 20.000 dentistas anunciados, sin desglose ni auditoría. Fuentes: [producto y precios](https://www.codental.com.br/), [manual de sillones](https://ajuda.codental.com.br/agenda-por-cadeiras-como-organizar-sua-clinica-por-espaco-de-atendimento), [manual Google Calendar](https://ajuda.codental.com.br/como-conectar-o-codental-ao-google-agenda), [privacidad](https://www.codental.com.br/politica-de-privacidade).

**15. Dentally.** **D:** reserva online para nuevos pacientes y existentes, duración/precio por tipo de cita, enlaces por sede, señas, cancelación con nueva reserva, controles periódicos e historial de cambios de cada turno. Los artículos marcan mercados admitidos y no incluyen Argentina. **C:** odontograma, exámenes periodontales, imágenes, formularios, planes, pagos y reportes; soporte y capacitación publicados y casos propios de consultorios británicos. No evidenciados para Argentina: disponibilidad de planes, ARCA, obras sociales y WhatsApp oficial. La auditoría del turno no acredita exportación integral ni obligaciones legales locales. Fuentes: [producto](https://www.dentally.com/en-gb/), [funciones clínicas](https://www.dentally.com/en-gb/dental-software-features/clinical), [manual de reserva](https://help.dentally.com/en/collections/2414893-online-booking), [auditoría de turno](https://help.dentally.com/en/articles/7854805-how-to-audit-a-patient-s-appointment), [precios británicos](https://www.dentally.com/en-gb/pricing).

**16. Open Dental.** **D:** operadores físicos y profesionales en la API; reglas para no duplicar tiempo del profesional según códigos de procedimiento, con comportamiento configurable. La documentación aclara que la regla deja superponer tiempo no reservado al profesional, por lo que no prueba una prohibición universal de choques por sillón. API publicada, auditoría, manual de Web Sched y servicios de recordatorio, portal, recall y cobros como componentes adicionales. **C:** odontograma gráfico, formularios, reportes, integraciones con radiología por puentes y migraciones desde múltiples sistemas. **P:** soporte, webinars y complementos tarifados; despliegue local o nube con costo adicional. Mercado Pago, ARCA, obras sociales argentinas, WhatsApp oficial y cantidad verificada de clientes: **No evidenciado**. Fuentes: [producto](https://www.opendental.com/), [reglas de agenda](https://www.opendental.com/manual/appointmentrules.html), [API de turnos](https://www.opendental.com/site/apiappointments.html), [auditoría](https://www.opendental.com/manual/audittrail.html), [tarifas y eServices](https://www.opendental.com/site/fees.html).

## B. Matriz de puntuación

Los puntajes miden **amplitud respaldada por evidencia pública y encaje en Argentina**. No son una certificación ni una prueba de aceptación. Escala ordinal: **0** sin evidencia relevante para el criterio; **1** capacidad puntual; **2** básica; **3** capacidad suficiente con limitaciones o respaldo principalmente comercial; **4** cobertura amplia con mecanismos o condiciones concretas; **5** cobertura destacada y documentación operativa para flujos críticos. Una descripción comercial genérica no recibe 5; una capacidad avanzada anunciada puede aportar, pero se penalizan ambigüedades, dependencias y exclusiones. Así, un 0 en integración local no significa que el producto carezca de integraciones en su país.

| Criterio | Peso | Qué se evaluó |
|---|---:|---|
| T — Turnos y automatización | 25 % | Agenda por recursos, duración, conflictos, estados, avisos y seguimiento. |
| O — Clínica odontológica | 20 % | Ficha, odontograma, periodoncia, imágenes, presupuestos y planes. |
| I — Integraciones locales y WhatsApp | 15 % | Conexiones argentinas y profundidad probada o declarada de WhatsApp. |
| A — Administración | 15 % | Caja, cobros, señas, facturación, coberturas, liquidación y reportes. |
| X — Experiencia del paciente | 10 % | Reserva, confirmación, cambio, portal, pagos y formularios. |
| S — Seguridad, exportación y trazabilidad | 10 % | Controles, respaldo, portabilidad y evidencia concreta de historial/auditoría. No mide conformidad jurídica. |
| P — Precio y adopción | 5 % | Transparencia, prueba, cargos, soporte y barreras para implementar. |

**Total /100 = (25T + 20O + 15I + 15A + 10X + 10S + 5P) / 5.**

| Producto | T | O | I | A | X | S | P | Total /100 |
|---|---:|---:|---:|---:|---:|---:|---:|---:|
| OdonFlow | 4 | 4 | 4 | 4 | 4 | 3 | 2 | **76** |
| DentalCore | 3 | 4 | 4 | 4 | 2 | 3 | 3 | **68** |
| Dentidad | 4 | 3 | 4 | 4 | 4 | 3 | 4 | **74** |
| Dentalink | 4 | 4 | 3 | 4 | 4 | 3 | 2 | **73** |
| AgendaPro | 5 | 3 | 4 | 3 | 4 | 2 | 4 | **74** |
| DentiX | 3 | 3 | 3 | 3 | 2 | 3 | 3 | **58** |
| DentalHub | 3 | 3 | 3 | 3 | 2 | 3 | 4 | **59** |
| Dentatools | 3 | 3 | 2 | 3 | 3 | 3 | 4 | **58** |
| OdontoGRAMA | 2 | 3 | 0 | 3 | 0 | 1 | 2 | **35** |
| DentalFlow | 2 | 3 | 1 | 2 | 1 | 1 | 2 | **37** |
| Citalo | 3 | 0 | 2 | 2 | 3 | 2 | 4 | **41** |
| DenPro | 2 | 3 | 0 | 2 | 2 | 3 | 4 | **42** |
| DENTIDESK | 3 | 4 | 0 | 3 | 2 | 2 | 3 | **51** |
| Codental | 4 | 3 | 2 | 3 | 4 | 2 | 4 | **63** |
| Dentally | 4 | 4 | 0 | 3 | 5 | 4 | 3 | **66** |
| Open Dental | 4 | 4 | 0 | 3 | 4 | 4 | 2 | **63** |

OdonFlow lidera por la combinación declarada de agenda y operación argentina, pero su 76 no demuestra que su motor rechace todos los conflictos. AgendaPro obtiene 5 solo en turnos porque su ayuda permite distinguir lo que bloquea de lo que autoriza por configuración. Dentidad y AgendaPro empatan con perfiles distintos: localización fiscal y portal frente a configuración de recursos. Dentally puntúa alto en experiencia por los flujos documentados, no por su disponibilidad argentina. Comparar diferencias pequeñas como hipótesis, no como ventajas estadísticas.

## C. Análisis competitivo

### Qué ya es estándar

- **Agenda, ficha clínica y odontograma** son la oferta base recurrente de las suites odontológicas consultadas. Caja, presupuestos, imágenes y reportes también son habituales en las suites; no son requisitos mínimos de un sistema que solo resuelve agenda.
- **Nube, varios profesionales y recordatorios** son comunes en la oferta relevada. La autorreserva, el portal, la lista de espera y la recuperación de ausentes no son universales; con frecuencia dependen de un plan.
- **WhatsApp no es una sola capacidad:** DentalFlow anuncia un botón para abrir el chat; Dentatools anuncia automatización; DentalCore y OdonFlow identifican la API oficial; AgendaPro publica condiciones para usar un número propio y cargos de Meta. No corresponde puntuar estas variantes como equivalentes.

### Diferenciales con respaldo concreto

- **Recursos y conflictos:** AgendaPro documenta recursos con horario y excepciones de reserva interna. Codental distingue sillones, respeta permisos y muestra avisos que no impiden guardar. Open Dental documenta bloqueo por procedimiento y tiempo del profesional. Los tres mecanismos son distintos: disponer de una vista por sillón no implica que el sistema impida sobreasignarlo.
- **Trazabilidad del turno:** Dentally documenta un historial accesible desde la tarjeta del turno; Open Dental publica manual de auditoría y API. Un log de accesos a fichas clínicas no acredita el historial de modificaciones de una cita.
- **Cobro y fiscalidad local:** OdonFlow, Dentidad y DentalCore anuncian flujos diferenciados de Mercado Pago y ARCA; los términos de DentalCore explicitan requisitos del contribuyente. Los convenios y liquidaciones que anuncian OdonFlow, DentalCore y Dentalink ameritan una demo con un caso argentino realista. Una nota fiscal brasileña, una integración chilena SII y la factura del abono SaaS no sustituyen facturar prestaciones ante ARCA.
- **Experiencia del paciente:** Dentally separa flujos para paciente nuevo y existente y documenta enlaces por sede y nueva reserva. Dentidad muestra una experiencia localizada de seña por prestación, confirmación desde el mensaje y ficha previa por enlace. La documentación de Codental prueba qué sincroniza con Google y qué información privada no muestra a recepción.

### Vacíos frecuentes en la evidencia pública del mercado argentino

1. **Poca transparencia pública sobre el motor de agenda.** No se comprobó en el relevamiento la combinación exacta del proyecto: odontólogo obligatorio, sillón opcional, pausa fija para ambos recursos cuando corresponde, rechazo de colisiones y separación por sede. Es una oportunidad a validar en demo, no una afirmación de que nadie la implemente.
2. **El trabajo del asistente suele quedar implícito.** Vale probar una vista por sillón con permisos de solo lectura y decidir cómo mostrar preparaciones sin sillón. La oportunidad es reducir la coordinación informal sin exponer más información del paciente de la necesaria.
3. **La portabilidad y los precios completos suelen quedar fuera de la ficha comercial.** Preguntar por exportación de adjuntos, auditoría y agenda histórica; plazo de descarga al cancelar; costo total por tres sedes, seis o más usuarios y mensajería. Ofrecer en el futuro una exportación útil y una política de salida clara puede ser un diferencial.
### Oportunidades de innovación

- **Automatizar ausencias requiere primero medirlas.** La [revisión académica sobre inasistencia dental](https://www.redalyc.org/journal/4215/421582841008/html/) identifica causas múltiples y efectos variables de los recordatorios. El [Observatorio de Salud de ISALUD](https://observatoriodesalud.isalud.edu.ar/posts/determinantes-y-estrategias-de-gestin-frente-al-ausentismo-en-turnos-mdicos) discute estrategias ambulatorias, no una tasa representativa de odontología argentina. No se encontró en las fuentes consultadas una estimación representativa argentina de ausencias odontológicas, solapamientos o tiempo de coordinación. No se trasladan al proyecto porcentajes promocionales. En un piloto autorizado, medir minutos por turno creado/reprogramado, colisiones rechazadas, huecos recuperados y ausencias sobre turnos programados que no se cancelaron.
- **Privacidad como requisito verificable, no como eslogan.** La [Ley 25.326](https://www.argentina.gob.ar/normativa/nacional/ley-25326-64790/actualizacion) trata la información de salud como dato sensible y establece seguridad y confidencialidad; la [Ley 26.529](https://www.argentina.gob.ar/normativa/nacional/ley-26529-160432/actualizacion) regula derechos del paciente, reserva e historia clínica. Para un eventual uso real se requiere revisar minimización, acceso por sede/rol, trazabilidad, respaldos y restauración, exportación, conservación, subencargados y transferencias. Citar ISO 27001, HIPAA, RGPD o una ley en una política no prueba cumplimiento argentino. El MVP académico usa exclusivamente datos ficticios.

## D. Recomendación final

### Cinco demos prioritarias

| Prioridad | Competidor | Qué pedir que demuestre, no solo que describa |
|---|---|---|
| 1 | **OdonFlow** | Intentar un choque por odontólogo y otro por sillón; separar rechazo de sobreturno autorizado; ver costo real de tres sedes y módulos ARCA, Mercado Pago y obras sociales. |
| 2 | **AgendaPro** | Configurar un sillón como recurso, asignarlo a una prestación y comprobar la diferencia entre reserva pública e interna, incluidos los límites de horario. |
| 3 | **Dentalink** | Operar una agenda multisede desde recepción, filtrar ocupación por sillón y profesional, reprogramar y solicitar evidencia de exportación íntegra. |
| 4 | **Dentidad** | Reservar con y sin seña, cancelar desde el mensaje y comprobar cuándo vuelve a estar disponible el cupo; pedir precio y permisos del plan multisede. |
| 5 | **DentiX** | Comparar columnas por sillón y odontólogo, guardar turnos con recursos cruzados, medir la carga operativa y verificar qué plan admite tres sedes. |

Usar demos públicas, documentación o videos oficiales, sin contactar a proveedores ni usar datos personales reales. Para una demo pública que permita interacción, aplicar el mismo guion con datos ficticios: crear un turno de 30 minutos, intentar otro al mismo tiempo con el mismo profesional en un sillón distinto, otro con distinto profesional en el mismo sillón, y otro que empiece exactamente cuando termina el primero. Agregar una radiografía sin sillón, dos intentos simultáneos desde recepción y una reprogramación. Comprobar **si el sistema rechaza, advierte o permite una excepción**, qué rol puede autorizarla y qué queda registrado. Si la evidencia pública no permite comprobarlo, consignar «No evidenciado».

### Tres referencias para experiencia de usuario

1. **AgendaPro:** selector de agenda por recurso y separación entre configuración, disponibilidad y permisos de sobrecarga.
2. **Dentally:** tarjeta de turno con historial y portal que distingue paciente nuevo, existente y sede.
3. **Dentidad:** flujos locales de confirmación, seña y ficha previa desde el celular. Son referencias de una etapa posterior, no requisitos de la entrega académica.

### MVP sugerido para Turnos Odontología

- **Imprescindible:** interfaz gráfica para tres sedes ampliables, datos ficticios, configuración de sedes, sillones, odontólogos, prestaciones y usuarios. Recepción solo accede a su sede; odontología consulta su agenda; asistencia consulta la agenda del sillón; administración opera las tres sedes. Crear, cancelar y reprogramar con registro del turno original y liberación del horario. Respetar duración por prestación, odontólogo obligatorio, sillón opcional y una sede por odontólogo y por sillón.
- **Diferenciador prioritario:** rechazar choques por intervalo real, incluso si dos recepcionistas guardan al mismo tiempo. Aplicar **10 minutos fijos** al odontólogo y al sillón cuando lo haya; aplicar la pausa al odontólogo también sin sillón. Explicar el rechazo indicando qué recurso y qué franja están ocupados. Dar prioridad a esta regla sobre una agenda con más vistas o automatizaciones.
- **Resolver antes de especificar:** decidir desde dónde ve el asistente los turnos sin sillón y a qué recurso queda asignado; definir si el turno y su pausa deben entrar completos en la franja de la sede; definir el registro de atención sin alterar por defecto la ocupación programada. Estas son decisiones de producto, no hechos que pueda resolver un competidor.
- **Dejar para después:** autorreserva y portal público, WhatsApp y recordatorios, lista de espera automatizada, historia clínica, imágenes, señas y facturación, obras sociales y analítica avanzada. Si un piloto confirma que las ausencias son el principal costo, priorizar confirmación/cancelación por enlace y recordatorio antes que un chatbot generalista.

**Conclusión.** En Argentina ya hay oferta para odontograma, WhatsApp, cobros y facturación. La oportunidad inmediata no es replicar una suite: es ofrecer una agenda de tres sedes que haga explícito qué bloquea, preserve el historial y permita a recepción y asistencia trabajar sin coordinación paralela. La falta de pruebas públicas sobre la combinación exacta de reglas justifica las demos propuestas; no demuestra exclusividad.

## Verificación de fuentes

**Revisión personal registrada por Juan Cruz Reveco el 24 de noviembre de 2026.** Se revisaron cinco páginas: tres fichas comerciales, un documento contractual y una página de precios. Verificar que un proveedor publica una función no equivale a probar que funcione.

| Fuente y tipo | Qué afirmaba el informe | Qué encontró el integrante | Qué se corrigió |
|---|---|---|---|
| [OdonFlow — producto](https://odonflow.com/) · Sitio oficial · 24/11/2026 | Anuncia agenda para múltiples profesionales y sillones, sobreturnos y lista de espera. | La página anuncia expresamente «Agenda multi-profesional y multi-sillón, con arrastre, sobreturnos y lista de espera». | Sin cambios. Sigue clasificado como oferta del proveedor. |
| [DentalCore — términos y condiciones](https://dentalcore.app/terms) · Términos contractuales · 24/11/2026 | Para facturar ante ARCA se requiere CUIT propio, certificado digital y punto de venta habilitado. | El apartado 5 de los términos enumera esos tres requisitos. | Sin cambios. |
| [Dentalink — agenda](https://www.softwaredentalink.com/ar/experiencia-de-pacientes/agenda) · Sitio oficial · 24/11/2026 | Muestra la ocupación de los sillones; el bloqueo automático de turnos superpuestos no está evidenciado. | Describe vistas diaria, semanal y global de sillones. La página no demuestra que el sistema impida guardar dos turnos superpuestos. | Sin cambios en el análisis. Se evitó interpretar la falta de demostración como prueba de que la función no existe. |
| [DentiX — producto y planes](https://dentix.com.ar/) · Sitio oficial · 24/11/2026 | Anuncia columnas por sillón y profesional, bloqueos, precios equivalentes mensuales con facturación anual y recordatorios según el plan. | La página anuncia las columnas y el bloqueo de horarios, muestra precios con facturación anual e indica correo en todos los planes y WhatsApp en Pro y Max, con un cupo mensual. | Sin cambios. El primer mes bonificado no se da por comprobado en esta revisión: el registro del integrante no incluye esa condición. |
| [AgendaPro — planes Argentina](https://agendapro.com/ar/planes) · Página de precios · 24/11/2026 | Publica cuatro precios mensuales en ARS con IVA para cuentas nuevas y adicionales según el plan. | La página muestra ARS 9.900, 19.900, 39.900 y 129.900, y describe adicionales por profesionales, ficha clínica e IA, y cargos de Meta cuando corresponde. | Sin cambios. |

**Diversidad de fuentes:** para cubrir más tipos de evidencia de la consigna, queda pendiente una revisión personal del [centro de ayuda de Codental](https://ajuda.codental.com.br/agenda-por-cadeiras-como-organizar-sua-clinica-por-espaco-de-atendimento) y de las [reseñas verificadas de Dentalink en Capterra](https://www.capterra.es/software/196361/dentalink#reviews).

## Referencias

Fecha de consulta para las fuentes del relevamiento: **23 de noviembre de 2026**. La comprobación personal del equipo registrada arriba corresponde al **24 de noviembre de 2026**.

- OdonFlow — [producto y planes](https://odonflow.com/).
- OdonFlow — [funciones para Argentina](https://odonflow.com/software-odontologico-argentina).
- OdonFlow — [términos](https://odonflow.com/terminos).
- DentalCore — [producto](https://dentalcore.app/).
- DentalCore — [agenda y limitación de autorreserva](https://dentalcore.app/agenda-odontologica).
- DentalCore — [contrato](https://dentalcore.app/terms).
- Dentidad — [producto, flujo y precios](https://www.dentidad.com/).
- Dentidad — [términos](https://www.dentidad.com/terminos).
- Dentidad — [privacidad](https://www.dentidad.com/privacidad).
- Dentalink — [oferta argentina](https://www.softwaredentalink.com/ar).
- Dentalink — [agenda](https://www.softwaredentalink.com/ar/experiencia-de-pacientes/agenda).
- Dentalink — [planes](https://www.softwaredentalink.com/ar/planes).
- Dentalink — [términos](https://www.softwaredentalink.com/ar/terminos-de-servicio).
- Dentalink — [Capterra verificado](https://www.capterra.es/software/196361/dentalink).
- AgendaPro — [vertical dental](https://agendapro.com/ar/dental/software-odontologico).
- AgendaPro — [recurso con horario](https://ayuda.agendapro.com/es/articles/3326185-como-creo-un-recurso-con-horario).
- AgendaPro — [configuración de agenda](https://ayuda.agendapro.com/es/articles/5924647-opciones-de-configuracion-para-la-agenda).
- AgendaPro — [planes argentinos](https://agendapro.com/ar/planes).
- AgendaPro — [términos](https://agendapro.com/ar/terminos-y-condiciones).
- DentiX — [producto, agenda y precios](https://dentix.com.ar/).
- DentiX — [política de privacidad](https://dentix.com.ar/privacy).
- DentalHub — [producto](https://dentalhub.com.ar/).
- DentalHub — [agenda](https://dentalhub.com.ar/agenda-odontologica).
- DentalHub — [precios](https://dentalhub.com.ar/precios).
- DentalHub — [términos](https://dentalhub.com.ar/terminos).
- DentalHub — [privacidad](https://dentalhub.com.ar/privacidad).
- Dentatools — [oferta argentina](https://dentatools.co/ar/).
- Dentatools — [precios](https://dentatools.co/ar/precios/).
- Dentatools — [términos](https://dentatools.co/terminos-y-condiciones/).
- Dentatools — [privacidad](https://dentatools.co/politica-de-privacidad/).
- OdontoGRAMA — [producto y acceso de prueba](https://odontograma.com.ar/).
- OdontoGRAMA — [ficha de SolSoftware](https://www.solsoftware.com.ar/odontograma/).
- DentalFlow — [producto, pantallas ilustrativas y demo enlazada](https://www.dentalflow.com.ar/).
- Citalo — [vertical dental](https://www.citalo.app/ar/software-turnos-odontologos).
- Citalo — [producto y precios](https://www.citalo.app/ar).
- Citalo — [controles declarados](https://www.citalo.app/ar/funcionalidades/seguridad-privacidad).
- Citalo — [términos](https://www.citalo.app/terms-conditions).
- DenPro — [producto](https://www.denpro.ar/).
- DenPro — [precios](https://www.denpro.ar/precios/).
- DenPro — [términos](https://www.denpro.ar/informacion-legal/terminos-de-servicio/).
- DENTIDESK — [producto, prueba y testimonios](https://www.dentidesk.com/).
- DENTIDESK — [funcionalidades](https://www.dentidesk.com/dentidesk/feature).
- DENTIDESK — [integraciones](https://www.dentidesk.com/dentidesk/solution).
- Codental — [producto y precios](https://www.codental.com.br/).
- Codental — [manual de sillones](https://ajuda.codental.com.br/agenda-por-cadeiras-como-organizar-sua-clinica-por-espaco-de-atendimento).
- Codental — [manual Google Calendar](https://ajuda.codental.com.br/como-conectar-o-codental-ao-google-agenda).
- Codental — [privacidad](https://www.codental.com.br/politica-de-privacidade).
- Dentally — [producto](https://www.dentally.com/en-gb/).
- Dentally — [funciones clínicas](https://www.dentally.com/en-gb/dental-software-features/clinical).
- Dentally — [manual de reserva](https://help.dentally.com/en/collections/2414893-online-booking).
- Dentally — [auditoría de turno](https://help.dentally.com/en/articles/7854805-how-to-audit-a-patient-s-appointment).
- Dentally — [precios británicos](https://www.dentally.com/en-gb/pricing).
- Open Dental — [producto](https://www.opendental.com/).
- Open Dental — [reglas de agenda](https://www.opendental.com/manual/appointmentrules.html).
- Open Dental — [API de turnos](https://www.opendental.com/site/apiappointments.html).
- Open Dental — [auditoría](https://www.opendental.com/manual/audittrail.html).
- Open Dental — [tarifas y eServices](https://www.opendental.com/site/fees.html).
- [revisión académica sobre inasistencia dental](https://www.redalyc.org/journal/4215/421582841008/html/).
- [Observatorio de Salud de ISALUD](https://observatoriodesalud.isalud.edu.ar/posts/determinantes-y-estrategias-de-gestin-frente-al-ausentismo-en-turnos-mdicos).
- [Ley 25.326](https://www.argentina.gob.ar/normativa/nacional/ley-25326-64790/actualizacion).
- [Ley 26.529](https://www.argentina.gob.ar/normativa/nacional/ley-26529-160432/actualizacion).
