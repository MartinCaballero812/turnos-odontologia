export interface Opcion {
  id: number;
  nombre: string;
}

export interface ContextoRecepcion {
  sede: Opcion;
  recepcion: Opcion;
  zonaHoraria: string;
}

export type Prestacion = Opcion & { usaSillon: boolean; duracionMinutos: number };

export interface Catalogos {
  pacientes: Opcion[];
  odontologos: Opcion[];
  prestaciones: Prestacion[];
  sillones: Opcion[];
}

export interface SolicitudTurno {
  pacienteId: number;
  odontologoId: number;
  prestacionId: number;
  inicioLocal: string;
  sillonId?: number;
}

export interface TurnoCreado {
  id: number;
  sillonId: number | null;
  inicioLocal: string;
  finLocal: string;
}

export type ResultadoTurno =
  | { estado: "guardado"; turno: TurnoCreado }
  | { estado: "rechazo"; codigo: string; mensaje: string }
  | { estado: "incierto" };

async function leerJson<T>(ruta: string, signal?: AbortSignal): Promise<T> {
  const respuesta = await fetch(ruta, { signal });
  if (!respuesta.ok) throw new Error(`La API respondió HTTP ${respuesta.status}.`);
  return respuesta.json();
}

export function obtenerContexto(signal?: AbortSignal): Promise<ContextoRecepcion> {
  return leerJson("/api/contexto", signal);
}

export function obtenerCatalogos(inicioLocal?: string, signal?: AbortSignal): Promise<Catalogos> {
  const consulta = inicioLocal ? `?${new URLSearchParams({ inicioLocal })}` : "";
  return leerJson(`/api/catalogos${consulta}`, signal);
}

export async function guardarTurno(solicitud: SolicitudTurno): Promise<ResultadoTurno> {
  try {
    const respuesta = await fetch("/api/turnos", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(solicitud),
      signal: AbortSignal.timeout(15_000),
    });
    if (respuesta.status === 201) {
      const turno = await respuesta.json() as TurnoCreado;
      if (Number.isSafeInteger(turno.id) && typeof turno.inicioLocal === "string" && typeof turno.finLocal === "string") {
        return { estado: "guardado", turno };
      }
    } else if ([400, 403, 404, 409, 422].includes(respuesta.status)) {
      const error = await respuesta.json();
      if (typeof error.codigo === "string" && typeof error.mensaje === "string") {
        return { estado: "rechazo", codigo: error.codigo, mensaje: error.mensaje };
      }
    }
  } catch {
    // Perder la respuesta no prueba que el turno no se haya guardado.
  }
  return { estado: "incierto" };
}
