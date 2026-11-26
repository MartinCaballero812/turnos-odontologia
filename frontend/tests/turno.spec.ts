import { expect, test } from "@playwright/test";
import type { Page } from "@playwright/test";
import { mkdir } from "node:fs/promises";
import { join } from "node:path";

const fecha = (page: Page) => page.getByLabel("Fecha", { exact: true });
const hora = (page: Page) => page.getByLabel("Hora de inicio", { exact: false });
const dentista = (page: Page) => page.getByLabel("Odontólogo", { exact: true });
const silla = (page: Page) => page.getByLabel("Sillón", { exact: true });
const prestacion = (page: Page) => page.getByLabel("Prestación", { exact: true });
const guardar = (page: Page) => page.getByRole("button", { name: "Guardar turno" });
const estado = (page: Page) => page.locator("#result");

test.beforeEach(async ({ page }) => {
  await page.clock.setFixedTime(new Date("2026-11-25T15:00:00Z"));
  await page.goto("/");
  await expect(page.locator("#sede")).toHaveText("SEDE 1");
  await expect(guardar(page)).toBeEnabled();
});

test("S01: Sede fija, campos vacíos y foco visible por teclado", async ({ page }) => {
  const fallos: string[] = [];
  page.on("pageerror", (error) => fallos.push(error.message));
  page.on("console", (mensaje) => {
    if (mensaje.type() === "error") fallos.push(mensaje.text());
  });
  page.on("requestfailed", (pedido) => fallos.push(pedido.url()));
  page.on("response", (respuesta) => {
    if (respuesta.status() >= 400) fallos.push(`${respuesta.status()} ${respuesta.url()}`);
  });
  await page.reload({ waitUntil: "networkidle" });
  await expect(guardar(page)).toBeEnabled();
  await expect(page.getByRole("heading", { name: "Crear turno" })).toBeVisible();
  await expect(page.getByRole("combobox")).toHaveCount(4);
  await expect(page.getByText("Solo para revisar")).toHaveCount(0);
  await expect(fecha(page)).toHaveValue("");
  await expect(hora(page)).toHaveValue("");
  await expect(dentista(page)).toBeDisabled();
  await expect(silla(page)).toBeDisabled();
  await expect(prestacion(page)).toHaveValue("");
  await capturar(page, "inicio");
  await page.keyboard.press("Tab");
  await expect(fecha(page)).toBeFocused();
  await expect(fecha(page)).toHaveCSS("outline-style", "solid");
  await expect(fecha(page)).not.toHaveCSS("outline-width", "0px");
  expect(fallos).toEqual([]);
});

test("S22–S24: filtra por franja, no preselecciona y conserva solo selecciones válidas", async ({ page }) => {
  await fecha(page).fill("2026-12-01");
  await expect(dentista(page)).toBeDisabled();
  await hora(page).fill("10:00");
  await expect(dentista(page).locator("option")).toHaveText(["Seleccioná un odontólogo", "Lucía Ferrer"]);
  await expect(dentista(page)).toHaveValue("");
  await dentista(page).selectOption("11");
  await hora(page).fill("11:15");
  await expect(dentista(page)).toHaveValue("11");
  await hora(page).fill("17:00");
  await expect(dentista(page).locator("option")).toHaveText(["Seleccioná un odontólogo", "Julián Soria"]);
  await expect(dentista(page)).toHaveValue("");
  await capturar(page, "tarde");
  await dentista(page).selectOption("12");
  await hora(page).fill("15:30");
  await expect(dentista(page)).toBeDisabled();
  await expect(page.locator("#dentist-hint")).toContainText("Elegí otra fecha u hora");
  await hora(page).fill("10:00");
  await fecha(page).fill("2026-12-08");
  await expect(dentista(page)).toBeDisabled();
  await expect(page.locator("#dentist-hint")).toContainText("No hay odontólogos");
});

test("S25: radiografía no usa sillón y las demás prestaciones requieren el de la sede", async ({ page }) => {
  await fecha(page).fill("2026-12-03");
  await completar(page, "2026-12-03", "10:00");
  await expect(silla(page)).toHaveValue("");
  await expect(silla(page).locator("option")).toHaveText(["Seleccioná un sillón", "Sillón 1", "Sillón 2"]);
  await guardar(page).click();
  await expect(page.locator("#chair-error")).toHaveText("Elegí un sillón para esta prestación.");
  await expect(silla(page)).toBeFocused();
  await silla(page).selectOption("12");
  await prestacion(page).selectOption("2");
  await expect(silla(page)).toBeDisabled();
  await expect(silla(page).locator("option")).toHaveText(["Sin sillón"]);
  await prestacion(page).selectOption("3");
  await expect(silla(page)).toHaveValue("");
  await prestacion(page).selectOption("2");
  const pedido = page.waitForRequest((r) => r.url().endsWith("/api/turnos") && r.method() === "POST");
  await guardar(page).click();
  expect((await pedido).postDataJSON()).not.toHaveProperty("sillonId");
  await expect(estado(page)).toHaveAttribute("data-state", "guardado");
  await expect(page.locator("#receipt-chair")).toHaveText("Sin sillón");
  await capturar(page, "radiografia");
});

test("S02: lee la duración del catálogo y confirma una creación real", async ({ page }) => {
  await completar(page, "2026-12-01", "10:00", "11");
  await expect(prestacion(page).locator("option:checked")).toHaveText("Control odontológico · 30 min");
  const pedido = page.waitForRequest((r) => r.url().endsWith("/api/turnos") && r.method() === "POST");
  const respuesta = page.waitForResponse((r) => r.url().endsWith("/api/turnos"));
  await guardar(page).click();
  expect((await pedido).postDataJSON()).toEqual({
    pacienteId: 1, odontologoId: 11, prestacionId: 1, sillonId: 11, inicioLocal: "2026-12-01T10:00",
  });
  expect((await respuesta).status()).toBe(201);
  await expect(estado(page)).toContainText("Turno guardado");
  await expect(page.locator("#receipt-time")).toHaveText("01/12/2026 · 10:00–10:30");
  await expect(page.locator("#receipt-chair")).toHaveText("Sillón 1");
  await capturar(page, "guardado");
});

test("S13: muestra el conflicto prioritario sin revelar al paciente del turno previo", async ({ page, request }) => {
  const previo = await request.post("/api/turnos", {
    data: { pacienteId: 2, odontologoId: 11, prestacionId: 1, inicioLocal: "2026-12-02T10:00", sillonId: 11 },
  });
  expect(previo.status()).toBe(201);
  await completar(page, "2026-12-02", "10:20", "11");
  await guardar(page).click();
  await expect(estado(page)).toContainText("El odontólogo no está disponible");
  await expect(estado(page)).toContainText("10:40");
  await expect(estado(page)).not.toContainText("Facundo Ledesma");
  await expect(page.locator("#receipt")).toBeHidden();
  await capturar(page, "conflicto");
});

test("S17: muestra el motivo de un rechazo sin simular éxito", async ({ page }) => {
  await completar(page, "2026-12-01", "12:25", "11");
  await guardar(page).click();
  await expect(estado(page)).toContainText("La cita y su pausa no entran en la franja");
  await expect(estado(page)).toContainText("10 minutos");
  await expect(hora(page)).toBeFocused();
  await expect(page.locator("#receipt")).toBeHidden();
  await capturar(page, "horario");
});

for (const fallo of ["red", "servidor"] as const) {
  test(`S18: ${fallo} no confirma el turno ni reintenta`, async ({ page }) => {
    let pedidos = 0;
    await page.route("**/api/turnos", async (ruta) => {
      pedidos++;
      if (fallo === "red") await ruta.abort("connectionreset");
      else await ruta.fulfill({ status: 500, json: { codigo: "ERROR_TECNICO" } });
    });
    await completar(page, "2026-12-01", "11:30", "11");
    await guardar(page).click();
    await expect(estado(page)).toHaveAttribute("data-state", "incierto");
    await expect(estado(page)).toContainText("No se pudo confirmar si el turno se guardó.");
    await expect(page.locator("#receipt")).toBeHidden();
    expect(pedidos).toBe(1);
    if (fallo === "red") await capturar(page, "incierto");
  });
}

test("S20: limita el calendario y muestra el rango permitido en español", async ({ page }) => {
  await expect(fecha(page)).toHaveAttribute("min", "2026-11-25");
  await expect(fecha(page)).toHaveAttribute("max", "2026-12-25");
  await fecha(page).fill("4000-01-01");
  await hora(page).fill("25:99");
  await guardar(page).click();
  await expect(page.locator("#date-error")).toContainText("25/11/2026 y el 25/12/2026");
  await expect(page.locator("#time-error")).toContainText("HH:MM");
  await expect(fecha(page)).toBeFocused();
  await expect(estado(page)).not.toContainText("Turno guardado");
  await capturar(page, "datos");
});

async function completar(page: Page, dia: string, inicio: string, sillon?: string): Promise<void> {
  await fecha(page).fill(dia);
  await hora(page).fill(inicio);
  await dentista(page).selectOption("11");
  await page.getByLabel("Paciente", { exact: true }).selectOption("1");
  await prestacion(page).selectOption("1");
  if (sillon) await silla(page).selectOption(sillon);
}

async function capturar(page: Page, nombre: string): Promise<void> {
  const carpeta = process.env.ODONCLICK_VERIFY_DIR;
  if (!carpeta) return;
  await mkdir(carpeta, { recursive: true });
  for (const [width, height] of [[1440, 900], [390, 844]]) {
    await page.setViewportSize({ width, height });
    expect(await page.evaluate(() => document.documentElement.scrollWidth <= innerWidth)).toBe(true);
    await page.screenshot({ path: join(carpeta, `${nombre}-${width}.png`), fullPage: true, animations: "disabled" });
  }
  await page.setViewportSize({ width: 1440, height: 900 });
}
