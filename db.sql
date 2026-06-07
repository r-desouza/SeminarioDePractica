-- Crear la base de datos y usarla
CREATE DATABASE IF NOT EXISTS reposteriabd;

USE reposteriabd;

-- Crear tabla Cliente
CREATE TABLE Cliente (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(20)
);

-- Crear tabla Producto
CREATE TABLE Producto (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    precio_vigente DECIMAL(10, 2) NOT NULL,
    estado VARCHAR(20) DEFAULT 'Activo'
);

-- Crear tabla Pedido
CREATE TABLE Pedido (
    id_pedido INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT,
    fecha DATE NOT NULL,
    hora TIME,
    estado VARCHAR(30) DEFAULT 'Pendiente',
    observaciones TEXT,
    total_presupuesto DECIMAL(10, 2),
    FOREIGN KEY (id_cliente) REFERENCES Cliente (id_cliente) ON DELETE SET NULL
);

-- Crear tabla Detalle_Pedido
CREATE TABLE Detalle_Pedido (
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT,
    id_producto INT,
    cantidad INT NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (id_pedido) REFERENCES Pedido (id_pedido) ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES Producto (id_producto) ON DELETE RESTRICT
);

--INSERTAR DATOS DE PRUEBA
-- Registrar un nuevo cliente
INSERT INTO
    Cliente (nombre, telefono)
VALUES (
        'María Gomez',
        '3541-15223344'
    );

-- Dar de alta nuevos productos en el catálogo
INSERT INTO
    Producto (
        nombre,
        descripcion,
        precio_vigente,
        estado
    )
VALUES (
        'Tarta Cabsha',
        'Masa sablée con dulce de leche y baño de chocolate',
        15000.00,
        'Activo'
    ),
    (
        'Torta Selva Negra',
        'Bizcochuelo de chocolate, crema y cerezas',
        22000.00,
        'Activo'
    );

-- Registrar la "cabecera" de un pedido (Ej: María Gomez encarga algo para el viernes)
INSERT INTO
    Pedido (
        id_cliente,
        fecha,
        hora,
        observaciones,
        total_presupuesto
    )
VALUES (
        1,
        '2026-05-22',
        '17:00:00',
        'Escribir "Feliz Cumple" en la torta',
        37000.00
    );

-- Registrar los detalles de ese pedido (las tortas específicas que encargó)
INSERT INTO
    Detalle_Pedido (
        id_pedido,
        id_producto,
        cantidad,
        subtotal
    )
VALUES (1, 1, 1, 15000.00), -- 1 Tarta Cabsha
    (1, 2, 1, 22000.00);
-- 1 Selva Negra

-- CONSULTAS DE PRUEBA
-- Consultar el catálogo web
SELECT
    id_producto,
    nombre,
    precio_vigente
FROM Producto
WHERE
    estado = 'Activo';

-- Consultar la "Agenda de Entregas"
SELECT
    id_pedido,
    fecha,
    hora,
    observaciones,
    total_presupuesto
FROM Pedido
WHERE
    estado = 'Pendiente'
ORDER BY fecha ASC, hora ASC;

-- BORRADO DE PRUEBA
-- Borrar un producto del catálogo web
UPDATE Producto SET estado = 'Pausado' WHERE id_producto = 1;