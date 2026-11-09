INSERT INTO USUARIOS (id, username, password_hash, nombre_completo, email, rol, estado) VALUES
(1, 'admin_root',      '$2y$10$y.sDlOfgaNniu6nK7irmguVDmp86D0bIx7FzA1WSUNFx2xtBowlDK', 'Administrador Auditor', 'admin@medivault.ucr.ac.cr',  'ROLE_ADMIN',    'ACTIVO'),
(2, 'dr_garcia',       '$2y$10$y.sDlOfgaNniu6nK7irmguVDmp86D0bIx7FzA1WSUNFx2xtBowlDK', 'Dr. Luis Garcia',        'garcia@medivault.ucr.ac.cr', 'ROLE_DOCTOR',   'ACTIVO'),
(3, 'dr_lopez',        '$2y$10$y.sDlOfgaNniu6nK7irmguVDmp86D0bIx7FzA1WSUNFx2xtBowlDK', 'Dra. Ana Lopez',         'lopez@medivault.ucr.ac.cr',  'ROLE_DOCTOR',   'ACTIVO'),
(4, 'paciente_carlos', '$2y$10$y.sDlOfgaNniu6nK7irmguVDmp86D0bIx7FzA1WSUNFx2xtBowlDK', 'Carlos Mora Rojas',      'carlos@correo.cr',           'ROLE_PACIENTE', 'ACTIVO'),
(5, 'paciente_maria',  '$2y$10$y.sDlOfgaNniu6nK7irmguVDmp86D0bIx7FzA1WSUNFx2xtBowlDK', 'Maria Solis Vega',       'maria@correo.cr',            'ROLE_PACIENTE', 'ACTIVO');

ALTER TABLE USUARIOS ALTER COLUMN id RESTART WITH 100;

INSERT INTO EXPEDIENTES_MEDICOS
    (id, paciente_id, medico_asignado_id, codigo_expediente, diagnostico_principal, notas_clinicas, nivel_confidencialidad) VALUES
(101, 4, 2, 'EXP-2026-101', 'Hipertension arterial cronica', 'Paciente en tratamiento con enalapril 10mg. Control mensual de presion arterial.', 'ALTO'),
(102, 5, 2, 'EXP-2026-102', 'Diabetes mellitus tipo 2',      'Metformina 850mg cada 12 horas. Dieta y control de glicemia semanal.',          'ALTO'),
(103, 5, 3, 'EXP-2026-103', 'Rinitis alergica estacional',   'Antihistaminico segun necesidad. Sin complicaciones respiratorias.',            'BAJO');

ALTER TABLE EXPEDIENTES_MEDICOS ALTER COLUMN id RESTART WITH 200;