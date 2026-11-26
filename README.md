# Turnos Odontología

Sistema de gestión de turnos y agenda para consultorios odontológicos (Argentina).

## Arranque local del backend

Usá **Java 17**, **Node `24.19.0`** y **npm `11.17.0`**. En macOS, si tenés varias versiones de Java instaladas, elegí Java 17 en esta terminal:

```sh
export JAVA_HOME="$(/usr/libexec/java_home -F -v 17)"
```

Desde la raíz del proyecto:

```sh
./gradlew --version
./gradlew build
```

El Wrapper descarga Gradle `8.14.8`; no hace falta instalar Gradle globalmente.
Gradle instala las dependencias del frontend con `npm ci`, compila TypeScript `7.0.2` y empaqueta los recursos para servirlos desde Spring Boot. No hace falta un servidor separado para el frontend.
La compilación incluye una prueba de arranque que no necesita base de datos y comprueba que los recursos se sirven por HTTP.

### Demo con PostgreSQL 16.x

El perfil `demo` usa `odonclick_demo` en `127.0.0.1:5432` y el rol local no administrador `odonclick_app`.
Si todavía no existen, conectate por socket con un usuario administrador (`psql -d postgres`) y ejecutá:

```sql
CREATE ROLE odonclick_app LOGIN NOSUPERUSER NOCREATEROLE NOCREATEDB;
\password odonclick_app
CREATE DATABASE odonclick_demo OWNER odonclick_app;
\q
```

Con PostgreSQL en marcha, ejecutá este bloque en la Terminal de macOS (`zsh`).
Ingresá la contraseña de `odonclick_app` cuando se pida; no se muestra ni queda escrita en el comando:

```sh
(
  read -rs 'SPRING_DATASOURCE_PASSWORD?Contraseña de odonclick_app: ' || exit 1
  printf '\n'
  export SPRING_DATASOURCE_PASSWORD
  export APP_DEMO_RECEPCION_ID=1
  ./gradlew --no-daemon bootRun --args='--spring.profiles.active=demo'
)
```

Spring Boot escucha en `127.0.0.1:8080`. Para detenerlo, usá **Ctrl+C**.
Al arrancar, el perfil `demo` crea las tablas si no existen y carga los datos ficticios de las tres sedes sin duplicarlos ni borrar turnos. Usá este perfil solo con la base local de demo.
Los IDs ficticios de recepción son `1`, `2` y `3`, uno por sede. Para cambiar de sede, detené el servidor y cambiá `APP_DEMO_RECEPCION_ID` antes de reiniciar. Si falta el ID o no corresponde a una recepción, el servidor no arranca. La Sede 1 tiene un cierre de ejemplo el `2026-12-08`.

Para consultar el contexto y, por ejemplo, los catálogos de una fecha y hora, abrí otra Terminal mientras corre la demo:

```sh
curl -fsS 'http://127.0.0.1:8080/api/contexto'
curl -fsS 'http://127.0.0.1:8080/api/catalogos?inicioLocal=2026-12-01T10:00'
```

El formulario está en **<http://127.0.0.1:8080/>**. El contexto se muestra como texto, sin selector de sede. Elegí una fecha hábil dentro del plazo y una hora para cargar los odontólogos. La duración junto a cada prestación es informativa; el servidor calcula el fin, agrega la pausa y valida el turno al guardar. Usá solo datos ficticios.

### Consultar los turnos guardados desde la Terminal

Después de guardar un turno, abrí otra Terminal y ejecutá esta consulta de solo lectura. Muestra los últimos diez turnos de la base de demo. Ingresá la contraseña de `odonclick_app` solo cuando la Terminal te la pida.

```sh
psql -h 127.0.0.1 -p 5432 -U odonclick_app -d odonclick_demo -c "
  SELECT
    t.id AS turno,
    s.nombre AS sede,
    p.nombre AS paciente,
    o.nombre AS odontologo,
    pr.nombre AS prestacion,
    COALESCE(si.nombre, 'Sin sillón') AS sillon,
    t.inicio,
    t.fin,
    t.fin_pausa AS ocupado_hasta
  FROM turno t
  JOIN sede s ON s.id = t.sede_id
  JOIN paciente p ON p.id = t.paciente_id
  JOIN odontologo o ON o.id = t.odontologo_id
  JOIN prestacion pr ON pr.id = t.prestacion_id
  LEFT JOIN sillon si ON si.id = t.sillon_id
  ORDER BY t.id DESC
  LIMIT 10;
"
```

Si la Terminal no encuentra `psql`, reemplazalo por la ruta del cliente instalado. Por ejemplo, con Homebrew en una Mac Intel y el cliente 17 instalado: `/usr/local/opt/postgresql@17/bin/psql`. La versión del cliente no cambia la del servidor PostgreSQL 16.

`fin` es el fin de la cita; `ocupado_hasta` incluye la pausa de 10 minutos. Una radiografía aparece como **Sin sillón**.

### Suite de verificación con PostgreSQL real y Chromium

#### Dónde están las pruebas

Las pruebas se organizan según el stack, en lugar de usar una carpeta `tests/` en la raíz:

- [`src/test/java/`](src/test/java/): pruebas Java del arranque, las reglas de negocio, la API y la concurrencia con PostgreSQL real.
- [`frontend/tests/`](frontend/tests/): pruebas del formulario en Chromium con Playwright.

El [registro de Verify](openspec/changes/archive/2026-11-26-crear-turno-sin-solapamientos/verify.md#cobertura-de-la-especificación) indica cómo se verificó cada uno de los 27 escenarios de C-01.

#### Cómo ejecutar la suite

La prueba usa **`odonclick_test`** con su propio rol **`odonclick_test_app`**, nunca la base ni el rol de la demo. Si todavía no existen, crealos desde `psql -d postgres` con un usuario administrador:

```sql
CREATE ROLE odonclick_test_app LOGIN NOSUPERUSER NOCREATEROLE NOCREATEDB;
CREATE DATABASE odonclick_test OWNER odonclick_test_app;
REVOKE CONNECT, TEMPORARY ON DATABASE odonclick_test FROM PUBLIC;
```

En esa misma sesión de `psql`, asigná una contraseña local si el rol todavía no tiene una y salí:

```text
\password odonclick_test_app
\q
```

Desde la raíz del proyecto, instalá una vez el navegador de pruebas:

```sh
(cd frontend && npm ci && npm exec -- playwright install chromium)
```

Luego, en la Terminal de macOS (`zsh`), corré la suite completa:

```sh
(
  read -rs 'ODONCLICK_TEST_DB_PASSWORD?Contraseña de odonclick_test_app: ' || exit 1
  printf '\n'
  export ODONCLICK_TEST_DB_PASSWORD
  ./gradlew --no-daemon clean build postgresTest browserTest
)
```

Las pruebas usan esquemas temporales en `odonclick_test`. Cubren la inicialización, los catálogos por sede y franja, la validación de los turnos y el guardado sin solapamientos. La suite de navegador levanta su propio servidor en un puerto libre, con reloj fijo en **25/11/2026** en Buenos Aires y casos de ejemplo del **1/12/2026**: no se cambia la fecha del equipo ni hace falta dejar `bootRun` en marcha. En la demo normal se usa la fecha actual.

Para ejecutar solo las pruebas del alta y concurrencia, reemplazá el comando Gradle del bloque anterior por `./gradlew --no-daemon postgresTest --tests '*TurnosPostgresTest'`. Los dos casos simultáneos usan conexiones distintas en PostgreSQL 16 y comprueban, por separado, que compartir odontólogo o sillón produce un `201`, un `409` y un solo turno guardado.
