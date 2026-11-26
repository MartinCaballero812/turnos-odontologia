import { guardarTurno, obtenerCatalogos, obtenerContexto } from "./api.js";
import type { Catalogos, ContextoRecepcion, Opcion, SolicitudTurno } from "./api.js";

const elemento = <T extends HTMLElement = HTMLElement>(id: string) => document.getElementById(id) as T;
const form = elemento<HTMLFormElement>("turno");
const campos = form.querySelector<HTMLFieldSetElement>("fieldset")!;
const guardar = form.querySelector<HTMLButtonElement>("button[type=submit]")!;
const controles = {
  date: elemento<HTMLInputElement>("date"),
  time: elemento<HTMLInputElement>("time"),
  dentist: elemento<HTMLSelectElement>("dentist"),
  patient: elemento<HTMLSelectElement>("patient"),
  service: elemento<HTMLSelectElement>("service"),
  chair: elemento<HTMLSelectElement>("chair"),
};
type Campo = keyof typeof controles;
const obligatorios: Record<Campo, string> = {
  date: "Ingresá la fecha.",
  time: "Ingresá la hora de inicio.",
  dentist: "Seleccioná un odontólogo.",
  patient: "Seleccioná un paciente.",
  service: "Seleccioná una prestación.",
  chair: "Elegí un sillón para esta prestación.",
};
const FORMATO_HORA = /^([01][0-9]|2[0-3]):[0-5][0-9]$/;
const hintInicial = "Se mostrarán quienes atienden en esa franja.";
let contexto: ContextoRecepcion;
let catalogos: Catalogos;
let formatoFecha: Intl.DateTimeFormat;
let disponible = false;
let enviando = false;
let consultaDentistas: AbortController | undefined;
let odontologoElegido = "";
let estadoDentistas: "incompleto" | "cargando" | "listo" | "vacio" | "error" = "incompleto";

function resultado(estado: string, titulo: string, mensaje: string): void {
  elemento("result").dataset.state = estado;
  elemento("result-title").textContent = titulo;
  elemento("result-message").textContent = mensaje;
  elemento("receipt").hidden = estado !== "guardado";
}

function errorCampo(campo: Campo, mensaje: string): void {
  const error = elemento(`${campo}-error`);
  error.textContent = mensaje;
  error.hidden = !mensaje;
  if (mensaje) controles[campo].setAttribute("aria-invalid", "true");
  else controles[campo].removeAttribute("aria-invalid");
}

function opciones(select: HTMLSelectElement, etiqueta: string, datos: Opcion[], elegida = ""): void {
  const inicial = new Option(etiqueta, "", true);
  inicial.disabled = true;
  select.replaceChildren(inicial, ...datos.map(({ id, nombre }) => new Option(nombre, String(id))));
  select.value = datos.some(({ id }) => String(id) === elegida) ? elegida : "";
}

function rangoFechas(): { desde: string; hasta: string } {
  const fecha = Object.fromEntries(formatoFecha.formatToParts(new Date()).map(({ type, value }) => [type, value]));
  const [year, month, day] = [fecha.year, fecha.month, fecha.day].map(Number);
  const siguiente = new Date(Date.UTC(year, month + 1, 0));
  const mes = String(siguiente.getUTCMonth() + 1).padStart(2, "0");
  const ultimo = String(Math.min(day, siguiente.getUTCDate())).padStart(2, "0");
  return { desde: `${fecha.year}-${fecha.month}-${fecha.day}`, hasta: `${siguiente.getUTCFullYear()}-${mes}-${ultimo}` };
}

function actualizarLimites(): void {
  const { desde, hasta } = rangoFechas();
  controles.date.min = desde;
  controles.date.max = hasta;
}

function fechaVisible(iso: string): string {
  return iso.split("-").reverse().join("/");
}

function validar(campo: Campo): string {
  const control = controles[campo];
  if (campo === "date" && control.validity.badInput) return "Elegí una fecha válida desde el calendario.";
  if (!control.value) return obligatorios[campo];
  if (campo === "date" && (control.value < controles.date.min || control.value > controles.date.max)) {
    return `Elegí una fecha entre el ${fechaVisible(controles.date.min)} y el ${fechaVisible(controles.date.max)}, inclusive.`;
  }
  if (campo === "time" && !FORMATO_HORA.test(control.value)) {
    return "Formato no aceptado. Usá HH:MM en 24 horas (ej.: 10:00).";
  }
  return "";
}

function inicioLocal(): string {
  return `${controles.date.value}T${controles.time.value}`;
}

async function refrescarOdontologos(): Promise<void> {
  consultaDentistas?.abort();
  actualizarLimites();
  errorCampo("dentist", "");
  controles.dentist.disabled = true;
  const hint = elemento("dentist-hint");
  if (validar("date") || validar("time")) {
    odontologoElegido = "";
    estadoDentistas = "incompleto";
    opciones(controles.dentist, "Elegí fecha y hora primero", []);
    hint.textContent = hintInicial;
    return;
  }
  const consulta = new AbortController();
  consultaDentistas = consulta;
  estadoDentistas = "cargando";
  opciones(controles.dentist, "Consultando odontólogos…", []);
  hint.textContent = "Consultando la franja seleccionada.";
  try {
    const { odontologos } = await obtenerCatalogos(inicioLocal(), consulta.signal);
    if (consulta.signal.aborted) return;
    estadoDentistas = odontologos.length ? "listo" : "vacio";
    opciones(controles.dentist, odontologos.length ? "Seleccioná un odontólogo" : "No hay odontólogos en ese horario",
      odontologos, odontologoElegido);
    odontologoElegido = controles.dentist.value;
    controles.dentist.disabled = !odontologos.length;
    hint.textContent = odontologos.length
      ? "Solo se muestran quienes atienden en esa franja. Los solapamientos se verifican al guardar."
      : "No hay odontólogos para esa fecha y hora. Elegí otra fecha u hora.";
  } catch {
    if (consulta.signal.aborted) return;
    estadoDentistas = "error";
    opciones(controles.dentist, "No se pudo consultar", []);
    hint.textContent = "No se pudo consultar el catálogo. Volvé a elegir la fecha o la hora.";
  }
}

function prestacionSeleccionada() {
  return catalogos.prestaciones.find(({ id }) => String(id) === controles.service.value);
}

function refrescarSillon(): void {
  const prestacion = prestacionSeleccionada();
  const requiere = prestacion?.usaSillon ?? false;
  opciones(controles.chair, requiere ? "Seleccioná un sillón" : prestacion ? "Sin sillón" : "Elegí una prestación primero",
    requiere ? catalogos.sillones : [], controles.chair.value);
  controles.chair.required = requiere;
  controles.chair.disabled = !requiere;
  elemento("chair-hint").textContent = requiere
    ? "Esta prestación requiere un sillón de la sede."
    : prestacion ? "Radiografía no utiliza sillón." : "Se habilita si la prestación usa sillón.";
  errorCampo("chair", "");
}

function validarFormulario(): boolean {
  actualizarLimites();
  let primerError: Campo | undefined;
  for (const campo of Object.keys(controles) as Campo[]) {
    if (controles[campo].disabled) continue;
    const error = validar(campo);
    errorCampo(campo, error);
    if (error) primerError ??= campo;
  }
  if (!validar("date") && !validar("time") && estadoDentistas !== "listo") {
    const mensaje = estadoDentistas === "vacio"
      ? "No hay odontólogos para esa fecha y hora. Elegí otra fecha u hora."
      : estadoDentistas === "cargando" ? "Esperá a que se carguen los odontólogos."
        : "No se pudo consultar el catálogo. Volvé a elegir la fecha o la hora.";
    errorCampo("time", mensaje);
    primerError = "time";
  }
  if (!primerError) return true;
  resultado("datos", "Revisá los datos del turno", "Corregí los campos indicados para continuar.");
  controles[primerError].focus();
  return false;
}

function mostrarRechazo(codigo: string, mensaje: string): void {
  if (codigo === "ODONTOLOGO_OCUPADO" || codigo === "SILLON_OCUPADO") {
    resultado("conflicto", codigo === "ODONTOLOGO_OCUPADO"
      ? "El odontólogo no está disponible" : "El sillón no está disponible", mensaje);
    return;
  }
  if (codigo === "FECHA_FUERA_DE_PLAZO" || codigo === "SEDE_CERRADA") {
    errorCampo("date", mensaje);
    controles.date.focus();
  } else if (codigo === "CITA_O_PAUSA_FUERA_DE_FRANJA" || codigo === "ODONTOLOGO_FUERA_DE_FRANJA") {
    errorCampo("time", mensaje);
    controles.time.focus();
  }
  if (codigo === "CITA_O_PAUSA_FUERA_DE_FRANJA") {
    resultado("horario", "La cita y su pausa no entran en la franja", mensaje);
  } else {
    resultado("datos", "No se guardó el turno", mensaje);
  }
}

form.addEventListener("submit", async (event) => {
  event.preventDefault();
  if (!disponible || enviando || !validarFormulario()) return;
  const datos: SolicitudTurno = {
    pacienteId: Number(controles.patient.value),
    odontologoId: Number(controles.dentist.value),
    prestacionId: Number(controles.service.value),
    inicioLocal: inicioLocal(),
    ...(prestacionSeleccionada()?.usaSillon ? { sillonId: Number(controles.chair.value) } : {}),
  };
  const paciente = controles.patient.selectedOptions[0].textContent;
  const odontologo = controles.dentist.selectedOptions[0].textContent;
  const sillon = controles.chair.selectedOptions[0].textContent;
  enviando = true;
  campos.disabled = guardar.disabled = true;
  resultado("inicio", "Guardando turno…", "Esperá la respuesta del servidor.");
  const respuesta = await guardarTurno(datos);
  enviando = false;
  campos.disabled = guardar.disabled = false;
  if (respuesta.estado === "guardado") {
    const { turno } = respuesta;
    elemento("receipt-patient").textContent = paciente;
    elemento("receipt-dentist").textContent = odontologo;
    elemento("receipt-chair").textContent = turno.sillonId === null ? "Sin sillón" : sillon;
    elemento("receipt-time").textContent = `${fechaVisible(turno.inicioLocal.slice(0, 10))} · `
      + `${turno.inicioLocal.slice(11, 16)}–${turno.finLocal.slice(11, 16)}`;
    resultado("guardado", "Turno guardado", `La cita quedó registrada en ${contexto.sede.nombre}.`);
  } else if (respuesta.estado === "rechazo") {
    mostrarRechazo(respuesta.codigo, respuesta.mensaje);
  } else {
    resultado("incierto", "No se pudo confirmar si el turno se guardó.",
      "Antes de volver a cargarlo, verificá si quedó registrado.");
  }
});

async function iniciar(): Promise<void> {
  try {
    [contexto, catalogos] = await Promise.all([obtenerContexto(), obtenerCatalogos()]);
    formatoFecha = new Intl.DateTimeFormat("es-AR", {
      timeZone: contexto.zonaHoraria, calendar: "gregory", numberingSystem: "latn",
      year: "numeric", month: "2-digit", day: "2-digit",
    });
    elemento("sede").textContent = contexto.sede.nombre;
    opciones(controles.patient, "Seleccioná un paciente", catalogos.pacientes);
    opciones(controles.service, "Seleccioná una prestación", catalogos.prestaciones.map((valor) => ({
      id: valor.id, nombre: `${valor.nombre} · ${valor.duracionMinutos} min`,
    })));
    actualizarLimites();
    refrescarSillon();
    for (const campo of Object.keys(controles) as Campo[]) {
      controles[campo].addEventListener("input", () => {
        if (controles[campo].hasAttribute("aria-invalid")) errorCampo(campo, validar(campo));
        if (campo === "date" || campo === "time") void refrescarOdontologos();
        if (campo === "dentist") odontologoElegido = controles.dentist.value;
        if (campo === "service") refrescarSillon();
      });
    }
    controles.date.addEventListener("focus", actualizarLimites);
    campos.disabled = guardar.disabled = false;
    disponible = true;
  } catch {
    elemento("sede").textContent = "Sede no disponible";
    resultado("datos", "No se pudo cargar el formulario", "Verificá que la aplicación esté en marcha y recargá la página.");
  }
}

void iniciar();
