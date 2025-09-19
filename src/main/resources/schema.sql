-- Banking Statistics Database Schema
-- MySQL script to create tables for banking transactions and categories

-- Create categories table
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    color VARCHAR(7) NOT NULL,
    
    CONSTRAINT uk_category_name UNIQUE (name)
);

-- Create transactions table
CREATE TABLE IF NOT EXISTS transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha_operacion DATE NOT NULL,
    fecha_valor DATE NOT NULL,
    concepto VARCHAR(255),
    pagos DECIMAL(15,2) DEFAULT 0.00,
    ingresos DECIMAL(15,2) DEFAULT 0.00,
    saldo DECIMAL(15,2) NOT NULL,
    category_id BIGINT,
    notes TEXT,
    
    CONSTRAINT fk_transaction_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL,
    INDEX idx_fecha_operacion (fecha_operacion),
    INDEX idx_fecha_valor (fecha_valor),
    INDEX idx_concepto (concepto),
    INDEX idx_category_id (category_id)
);

-- Insert default categories
INSERT INTO categories (name, color) VALUES
('Vivienda', '#FF6384'),
('Coche', '#36A2EB'),
('Super', '#FFCE56'),
('Ocio', '#4BC0C0'),
('Telefonía e Internet', '#9966FF'),
('Tecnología', '#FF9F40'),
('Otros', '#FF6384'),
('Suplementación', '#C9CBCF'),
('Regalos', '#FF99CC'),
('Suscripciónes', '#66CCFF'),
('Ropa', '#99FF99'),
('Peluquería', '#FFCC99'),
('Pelo', '#CC99FF'),
('Nómina', '#99CCCC'),
('Gasoil', '#FFCCCC'),
('Videojuegos', '#CCFFCC'),
('Gym', '#CCCCFF'),
('Efectivo', '#FFFFCC'),
('Luz', '#FFCCFF'),
('Gas', '#CCFFFF'),
('Moto', '#FFE6CC')
ON DUPLICATE KEY UPDATE color = VALUES(color);