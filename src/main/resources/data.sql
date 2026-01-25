-- Script de inicialización de usuarios para PostgreSQL
-- Este script crea usuarios de ejemplo para el sistema de autenticación JWT

-- Tabla de usuarios (se creará automáticamente por JPA/Hibernate)
-- Si necesitas crearla manualmente, descomenta las siguientes líneas:
/*
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT true
);
*/

-- Insertar usuario administrador
-- Username: admin
-- Password: admin123
-- BCrypt hash generado con strength 10
INSERT INTO usuarios (username, password, role, enabled) 
VALUES ('admin', '$2a$12$uo2LQ4GNqOZh0t9p5Clgre4Lw9/aHq/utSE.nQpHeo9uBwjlkZN5.', 'ADMIN', true)
ON CONFLICT (username) DO NOTHING;

-- Insertar usuario peluquero de ejemplo
-- Username: peluquero1
-- Password: peluquero123
-- BCrypt hash generado con strength 10
INSERT INTO usuarios (username, password, role, enabled) 
VALUES ('peluquero1', '$2a$12$Nhd7wkLsOA3BwHLeB/Pb9OdOshz1nzfmk0HLvh7Nxl6BnRR.0Hi4m', 'USER', true)
ON CONFLICT (username) DO NOTHING;
