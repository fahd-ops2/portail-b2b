-- Drop tables first
DROP TABLE IF EXISTS livraisons;
DROP TABLE IF EXISTS reclamation;
DROP TABLE IF EXISTS order_item;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS produits;
DROP TABLE IF EXISTS admin;
DROP TABLE IF EXISTS clients;
DROP TABLE IF EXISTS livreurs;
DROP TABLE IF EXISTS app_user;

-- Table app_user
CREATE TABLE app_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    role VARCHAR(50),
    locked BOOLEAN,
    enabled BOOLEAN
);

-- Table livreurs (FK vers app_user.id)
CREATE TABLE livreurs (
    id BIGINT PRIMARY KEY,
    phone_number VARCHAR(20),
    is_active BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_livreur_user FOREIGN KEY (id) REFERENCES app_user(id) ON DELETE CASCADE
);

-- Table clients (FK vers app_user.id)
CREATE TABLE clients (
    id BIGINT PRIMARY KEY,
    address VARCHAR(255),
    CONSTRAINT fk_client_user FOREIGN KEY (id) REFERENCES app_user(id) ON DELETE CASCADE
);

-- Table admin (FK vers app_user.id)
CREATE TABLE admin (
    id BIGINT PRIMARY KEY,
    telephone VARCHAR(20),
    CONSTRAINT fk_admin_user FOREIGN KEY (id) REFERENCES app_user(id) ON DELETE CASCADE
);

-- Table produits
CREATE TABLE produits (
    id VARCHAR(36) PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    prix_unitaire DOUBLE NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    description VARCHAR(255),
    type VARCHAR(255),
    image VARCHAR(255)
);

-- Table orders
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    client_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) NOT NULL DEFAULT 'EN_ATTENTE',
    delivery_address TEXT,
    scheduled_for TIMESTAMP NULL,
    delivered_at TIMESTAMP NULL,
    note TEXT,
    total_amount DOUBLE NOT NULL DEFAULT 0.0,
    CONSTRAINT fk_orders_client FOREIGN KEY (client_id) REFERENCES clients(id),
    CHECK (status IN ('EN_ATTENTE', 'EN_COURS', 'LIVREE', 'ANNULEE'))
);

-- Table order_item
CREATE TABLE order_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    produit_id VARCHAR(36) NOT NULL,
    quantity DOUBLE NOT NULL,
    unit_price DOUBLE NOT NULL,
    subtotal DOUBLE NOT NULL,
    note TEXT,
    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES orders(id),
    CONSTRAINT fk_order_item_produit FOREIGN KEY (produit_id) REFERENCES produits(id) ON DELETE CASCADE
);

-- Table reclamation
CREATE TABLE reclamation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(255),
    type VARCHAR(255),
    status VARCHAR(255),
    date DATE
);

-- Table livraisons
CREATE TABLE livraisons (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    livreur_id BIGINT NOT NULL,
    scheduled_time TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'SCHEDULED',
    delivery_address VARCHAR(255) NOT NULL,
    notes TEXT,
    delivered_at TIMESTAMP NULL,
    tracking_code VARCHAR(255) UNIQUE,
    latitude DOUBLE,
    longitude DOUBLE,
    is_urgent BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_livraisons_order FOREIGN KEY (order_id) REFERENCES orders(id),
    CONSTRAINT fk_livraisons_livreur FOREIGN KEY (livreur_id) REFERENCES livreurs(id),
    CHECK (status IN ('SCHEDULED', 'IN_PROGRESS', 'DELIVERED', 'CANCELLED'))
);
