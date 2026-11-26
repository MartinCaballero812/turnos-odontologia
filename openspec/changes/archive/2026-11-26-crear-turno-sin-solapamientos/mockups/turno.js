(() => {
  const field = (id) => document.getElementById(id);
  const stateControl = field("preview-state");
  const form = field("turno");
  const result = field("result");
  const controls = [...form.querySelectorAll("select, input")];
  const openingHours = [
    { start: 8 * 60 + 30, end: 13 * 60, dentist: "Lucía Ferrer" },
    { start: 16 * 60, end: 21 * 60, dentist: "Julián Soria" },
  ];
  const breakMinutes = 10;
  const chairs = ["Sillón 1", "Sillón 2"];
  const buenosAiresDate = new Intl.DateTimeFormat("es-AR", {
    timeZone: "America/Argentina/Buenos_Aires",
    calendar: "gregory",
    numberingSystem: "latn",
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
  });
  const missing = {
    patient: "Seleccioná un paciente.",
    dentist: "Seleccioná un odontólogo.",
    service: "Seleccioná una prestación.",
    date: "Ingresá la fecha.",
    time: "Ingresá la hora de inicio.",
    chair: "Elegí un sillón para esta prestación.",
  };
  const messages = {
    guardado: ["Turno guardado", "La cita quedó registrada en SEDE 1."],
    conflicto: ["El odontólogo no está disponible", "Está ocupado de 10:00 a 10:40, incluida la pausa. Elegí otro horario."],
    horario: ["La cita y su pausa no entran en la franja", "El último fin de cita válido es 12:50 por la mañana o 20:50 por la tarde."],
    incierto: ["No se pudo confirmar si el turno se guardó.", "Antes de volver a cargarlo, verificá si quedó registrado."],
  };

  function daysInMonth(year, month) {
    const leap = year % 4 === 0 && (year % 100 !== 0 || year % 400 === 0);
    return [31, leap ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31][month - 1];
  }

  function bookingDates() {
    const parts = Object.fromEntries(buenosAiresDate.formatToParts(new Date()).map(({ type, value }) => [type, value]));
    const [year, month, day] = [parts.year, parts.month, parts.day].map(Number);
    const nextYear = month === 12 ? year + 1 : year;
    const nextMonth = month === 12 ? 1 : month + 1;
    const lastDay = Math.min(day, daysInMonth(nextYear, nextMonth));
    const format = (d, m, y) => `${String(y).padStart(4, "0")}-${String(m).padStart(2, "0")}-${String(d).padStart(2, "0")}`;
    return { from: format(day, month, year), to: format(lastDay, nextMonth, nextYear) };
  }

  function displayDate(value) {
    return value.split("-").reverse().join("/");
  }

  function syncDateLimits() {
    const { from, to } = bookingDates();
    const date = field("date");
    date.min = from;
    date.max = to;
  }

  function showError(control, message) {
    const error = field(`${control.id}-error`);
    error.textContent = message;
    error.hidden = !message;
    if (message) control.setAttribute("aria-invalid", "true");
    else control.removeAttribute("aria-invalid");
  }

  function validationMessage(control) {
    if (control.id === "date" && control.validity.badInput) return "Elegí una fecha válida desde el calendario.";
    if (!control.value) return missing[control.id];
    if (control.id === "date") {
      const { from, to } = bookingDates();
      if (control.validity.rangeUnderflow || control.validity.rangeOverflow || control.value < from || control.value > to) {
        return `Elegí una fecha entre el ${displayDate(from)} y el ${displayDate(to)}. Podés reservar hasta con un mes de anticipación.`;
      }
    }
    if (control.id === "time" && control.validity.patternMismatch) {
      return "Formato no aceptado. Usá HH:MM en 24 horas (ej.: 10:00).";
    }
    return "";
  }

  function selectedSlot() {
    const date = field("date");
    const time = field("time");
    if (validationMessage(date) || validationMessage(time)) return null;
    const day = new Date(`${date.value}T00:00:00Z`).getUTCDay();
    const [hour, minute] = time.value.split(":").map(Number);
    const start = hour * 60 + minute;
    const weekend = day === 0 || day === 6;
    const opening = weekend ? null : openingHours.find((range) => range.start <= start && start < range.end);
    return { start, opening, weekend };
  }

  function refreshDentists() {
    const slot = selectedSlot();
    const select = field("dentist");
    const hint = field("dentist-hint");
    const chosen = select.value;
    const available = slot?.opening?.dentist;
    const placeholder = document.createElement("option");
    placeholder.value = "";
    placeholder.disabled = true;
    placeholder.textContent = available ? "Seleccioná un odontólogo" : slot ? "No hay odontólogos en ese horario" : "Elegí fecha y hora primero";
    select.replaceChildren(placeholder);
    select.disabled = !available;
    if (available) {
      const option = document.createElement("option");
      option.textContent = available;
      select.append(option);
    }
    select.value = available && chosen === available ? chosen : "";
    hint.textContent = available
      ? "Solo se muestran quienes atienden en esa franja. Los solapamientos se verifican al guardar."
      : slot?.weekend
        ? "La sede no atiende los sábados ni los domingos."
        : slot ? "Elegí una hora dentro de la franja de mañana o tarde." : "Se mostrarán quienes atienden en esa franja.";
    showError(select, "");
    return slot;
  }

  function refreshChair() {
    const service = field("service");
    const select = field("chair");
    const hint = field("chair-hint");
    const chosen = select.value;
    const usesChair = service.selectedOptions[0].dataset.usesChair === "true";
    const label = usesChair ? "Seleccioná un sillón" : service.value ? "Sin sillón" : "Elegí una prestación primero";
    const placeholder = new Option(label, "");
    placeholder.disabled = true;
    select.replaceChildren(placeholder);
    select.disabled = !usesChair;
    select.required = usesChair;
    if (usesChair) select.append(...chairs.map((chair) => new Option(chair, chair)));
    select.value = usesChair && chairs.includes(chosen) ? chosen : "";
    hint.textContent = usesChair
      ? "Esta prestación requiere un sillón de la sede."
      : service.value ? "Radiografía no utiliza sillón." : "Se habilita si la prestación usa sillón.";
    showError(select, "");
  }

  function showResult(state, title, message) {
    result.dataset.state = state;
    field("result-title").textContent = title;
    field("result-message").textContent = message;
    field("receipt").hidden = state !== "guardado";
  }

  form.noValidate = true;
  syncDateLimits();
  refreshDentists();
  refreshChair();
  field("date").addEventListener("focus", syncDateLimits);
  for (const control of controls) {
    control.addEventListener("input", () => {
      if (control.hasAttribute("aria-invalid")) showError(control, validationMessage(control));
      if (control.id === "date" || control.id === "time") refreshDentists();
      if (control.id === "service") refreshChair();
    });
  }

  form.addEventListener("submit", (event) => {
    event.preventDefault();
    syncDateLimits();
    const slot = refreshDentists();
    refreshChair();
    let firstInvalid = null;
    for (const control of controls) {
      if (control.disabled) continue;
      const message = validationMessage(control);
      showError(control, message);
      if (message && !firstInvalid) firstInvalid = control;
    }
    if (firstInvalid) {
      showResult("datos", "Revisá los datos del turno", "Corregí los campos indicados para continuar.");
      firstInvalid.focus();
      return;
    }
    const time = field("time");
    const { opening, start, weekend } = slot;
    const end = start + Number(field("service").selectedOptions[0].dataset.minutes);

    if (!opening || end + breakMinutes > opening.end) {
      const [title, message] = opening
        ? messages.horario
        : weekend
          ? ["La sede está cerrada", "No se atiende los sábados ni los domingos."]
          : ["La sede no atiende a esa hora", "Elegí un inicio entre las 08:30 y las 13:00, o entre las 16:00 y las 21:00."];
      const invalid = weekend ? field("date") : time;
      showError(invalid, title + ".");
      showResult("horario", title, message);
      invalid.focus();
      return;
    }
    const state = stateControl.value;
    const [title, message] = messages[state];
    showResult(state, title, message);

    if (state === "guardado") {
      const endTime = `${String(Math.floor(end / 60)).padStart(2, "0")}:${String(end % 60).padStart(2, "0")}`;
      field("receipt-patient").textContent = field("patient").value;
      field("receipt-dentist").textContent = field("dentist").value;
      field("receipt-time").textContent = `${displayDate(field("date").value)} · ${time.value}–${endTime}`;
      field("receipt-chair").textContent = field("chair").selectedOptions[0].textContent;
    }
  });
})();
