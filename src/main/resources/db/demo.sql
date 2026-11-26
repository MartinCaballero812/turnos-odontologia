INSERT INTO sede (id, nombre) VALUES
    (1, 'SEDE 1'), (2, 'SEDE 2'), (3, 'SEDE 3')
ON CONFLICT (id) DO NOTHING;

INSERT INTO recepcion (id, sede_id, nombre) VALUES
    (1, 1, 'Recepción de ejemplo 1'),
    (2, 2, 'Recepción de ejemplo 2'),
    (3, 3, 'Recepción de ejemplo 3')
ON CONFLICT (id) DO NOTHING;

INSERT INTO odontologo (id, sede_id, nombre) VALUES
    (11, 1, 'Lucía Ferrer'), (12, 1, 'Julián Soria'),
    (21, 2, 'Elena Vidal'), (22, 2, 'Rafael Mora'),
    (31, 3, 'Inés Roldán'), (32, 3, 'Bruno Silva')
ON CONFLICT (id) DO NOTHING;

INSERT INTO sillon (id, sede_id, nombre)
SELECT sede.id * 10 + numero, sede.id, 'Sillón ' || numero
FROM sede CROSS JOIN generate_series(1, 2) AS numeros(numero)
WHERE sede.id IN (1, 2, 3)
ON CONFLICT (id) DO NOTHING;

INSERT INTO franja_atencion (sede_id, dia_semana, codigo, inicio, fin)
SELECT sede.id, dia, franja.codigo, franja.inicio, franja.fin
FROM sede
CROSS JOIN generate_series(1, 5) AS dias(dia)
CROSS JOIN (VALUES
    ('MANANA', time '08:30', time '13:00'),
    ('TARDE',  time '16:00', time '21:00')
) AS franja(codigo, inicio, fin)
WHERE sede.id IN (1, 2, 3)
ON CONFLICT (sede_id, dia_semana, codigo) DO NOTHING;

INSERT INTO asignacion_franja (odontologo_id, sede_id, dia_semana, franja_codigo)
SELECT odontologo.id, odontologo.sede_id, franja.dia_semana, franja.codigo
FROM odontologo
JOIN franja_atencion AS franja ON franja.sede_id = odontologo.sede_id
WHERE (odontologo.id IN (11, 21, 31) AND franja.codigo = 'MANANA')
   OR (odontologo.id IN (12, 22, 32) AND franja.codigo = 'TARDE')
ON CONFLICT (odontologo_id, dia_semana) DO NOTHING;

INSERT INTO cierre_sede (sede_id, fecha) VALUES (1, date '2026-12-08')
ON CONFLICT (sede_id, fecha) DO NOTHING;

INSERT INTO paciente (id, nombre) VALUES
    (1, 'Camila Ríos'), (2, 'Facundo Ledesma'), (3, 'Malena Acosta'),
    (4, 'Tomás Benítez'), (5, 'Agustina Molina')
ON CONFLICT (id) DO NOTHING;

INSERT INTO prestacion (id, nombre, duracion_minutos, usa_sillon) VALUES
    (1, 'Control odontológico', 30, true),
    (2, 'Radiografía', 15, false),
    (3, 'Limpieza dental', 45, true),
    (4, 'Restauración simple', 40, true),
    (5, 'Consulta de urgencia', 20, true)
ON CONFLICT (id) DO NOTHING;
