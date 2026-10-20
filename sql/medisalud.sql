-- Base de datos del proyecto final MediSalud (Curso de Java)
-- Crea la base de datos y las tablas que usan PacienteDAO, MedicoDAO y CitaDAO.
-- Se puede ejecutar más de una vez: no borra datos existentes.

CREATE DATABASE IF NOT EXISTS medisalud
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE medisalud;

CREATE TABLE IF NOT EXISTS pacientes (
    codigo VARCHAR(20) PRIMARY KEY,
    nombre_completo VARCHAR(100) NOT NULL,
    edad INT NOT NULL
);

CREATE TABLE IF NOT EXISTS medicos (
    codigo VARCHAR(20) PRIMARY KEY,
    nombre_completo VARCHAR(100) NOT NULL,
    especialidad VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS citas (
    codigo VARCHAR(20) PRIMARY KEY,
    paciente_codigo VARCHAR(20) NOT NULL,
    medico_codigo VARCHAR(20) NOT NULL,
    fecha DATE NOT NULL,
    estado VARCHAR(20) NOT NULL,
    CONSTRAINT fk_citas_paciente FOREIGN KEY (paciente_codigo) REFERENCES pacientes (codigo),
    CONSTRAINT fk_citas_medico FOREIGN KEY (medico_codigo) REFERENCES medicos (codigo)
);

-- Datos de ejemplo: 10 pacientes, 10 medicos y 30 citas.
-- INSERT IGNORE: se puede repetir sin duplicar ni fallar.

INSERT IGNORE INTO pacientes (codigo, nombre_completo, edad) VALUES
    ('P001', 'Ana María Gómez', 34),
    ('P002', 'Luis Fernando Pérez', 52),
    ('P003', 'Carolina Ruiz', 28),
    ('P004', 'Jorge Andrés Molina', 67),
    ('P005', 'Valentina Torres', 19),
    ('P006', 'Camilo Rodríguez', 47),
    ('P007', 'Sofía Herrera', 41),
    ('P008', 'Mateo Castaño', 8),
    ('P009', 'Laura Jiménez', 59),
    ('P010', 'Daniel Ospina', 73);

INSERT IGNORE INTO medicos (codigo, nombre_completo, especialidad) VALUES
    ('M001', 'Dra. Patricia Londoño', 'Medicina General'),
    ('M002', 'Dr. Ricardo Vélez', 'Cardiología'),
    ('M003', 'Dra. Marcela Arango', 'Pediatría'),
    ('M004', 'Dr. Héctor Salazar', 'Dermatología'),
    ('M005', 'Dra. Isabel Cárdenas', 'Ginecología'),
    ('M006', 'Dr. Felipe Mejía', 'Ortopedia'),
    ('M007', 'Dra. Natalia Restrepo', 'Neurología'),
    ('M008', 'Dr. Sergio Duque', 'Oftalmología'),
    ('M009', 'Dra. Claudia Ortiz', 'Medicina General'),
    ('M010', 'Dr. Andrés Giraldo', 'Cardiología');

INSERT IGNORE INTO citas (codigo, paciente_codigo, medico_codigo, fecha, estado) VALUES
    ('C001', 'P001', 'M001', '2026-10-01', 'PENDIENTE'),
    ('C002', 'P002', 'M004', '2026-10-04', 'CONFIRMADA'),
    ('C003', 'P003', 'M007', '2026-10-07', 'ATENDIDA'),
    ('C004', 'P004', 'M010', '2026-10-10', 'CANCELADA'),
    ('C005', 'P005', 'M003', '2026-10-13', 'ATENDIDA'),
    ('C006', 'P006', 'M006', '2026-10-16', 'CONFIRMADA'),
    ('C007', 'P007', 'M009', '2026-10-19', 'PENDIENTE'),
    ('C008', 'P008', 'M002', '2026-10-22', 'CONFIRMADA'),
    ('C009', 'P009', 'M005', '2026-10-25', 'ATENDIDA'),
    ('C010', 'P010', 'M008', '2026-10-28', 'CANCELADA'),
    ('C011', 'P001', 'M002', '2026-10-03', 'ATENDIDA'),
    ('C012', 'P002', 'M005', '2026-10-06', 'CONFIRMADA'),
    ('C013', 'P003', 'M008', '2026-10-09', 'PENDIENTE'),
    ('C014', 'P004', 'M001', '2026-10-12', 'CONFIRMADA'),
    ('C015', 'P005', 'M004', '2026-10-15', 'ATENDIDA'),
    ('C016', 'P006', 'M007', '2026-10-18', 'CANCELADA'),
    ('C017', 'P007', 'M010', '2026-10-21', 'ATENDIDA'),
    ('C018', 'P008', 'M003', '2026-10-24', 'CONFIRMADA'),
    ('C019', 'P009', 'M006', '2026-10-27', 'PENDIENTE'),
    ('C020', 'P010', 'M009', '2026-10-02', 'CONFIRMADA'),
    ('C021', 'P001', 'M003', '2026-11-05', 'ATENDIDA'),
    ('C022', 'P002', 'M006', '2026-11-08', 'CANCELADA'),
    ('C023', 'P003', 'M009', '2026-11-11', 'ATENDIDA'),
    ('C024', 'P004', 'M002', '2026-11-14', 'CONFIRMADA'),
    ('C025', 'P005', 'M005', '2026-11-17', 'PENDIENTE'),
    ('C026', 'P006', 'M008', '2026-11-20', 'CONFIRMADA'),
    ('C027', 'P007', 'M001', '2026-11-23', 'ATENDIDA'),
    ('C028', 'P008', 'M004', '2026-11-26', 'CANCELADA'),
    ('C029', 'P009', 'M007', '2026-11-01', 'ATENDIDA'),
    ('C030', 'P010', 'M010', '2026-11-04', 'CONFIRMADA');
