-- Insert users (base users for livreurs and clients)
INSERT INTO app_user (id, nom, email, password, role, locked, enabled) VALUES
(1, 'Alice Livreur', 'alice.livreur@example.com', 'passwordHash1', 'LIVREUR', FALSE, TRUE),
(2, 'Bob Client', 'bob.client@example.com', 'passwordHash2', 'CLIENT', FALSE, TRUE),
(3, 'Charlie Livreur', 'charlie.livreur@example.com', 'passwordHash3', 'LIVREUR', FALSE, TRUE),
(4, 'Diana Client', 'diana.client@example.com', 'passwordHash4', 'CLIENT', FALSE, TRUE);

-- Insert livreurs (inherits from app_user)
INSERT INTO livreurs (id, phone_number, is_active) VALUES
(1, '+212600000001', TRUE),
(3, '+212600000003', TRUE);

-- Insert clients (inherits from app_user)
INSERT INTO clients (id, address) VALUES
(2, '123 Main Street, Casablanca'),
(4, '456 Rue de Fes, Rabat');

-- Insert produits
INSERT INTO produits (id, nom, prix_unitaire, stock) VALUES
(1, 'Gaz Cylinder 5kg', 50.0, 100),
(2, 'Gaz Cylinder 12kg', 100.0, 50),
(3, 'Gaz Cylinder 20kg', 150.0, 20);

-- Insert orders
INSERT INTO orders (id, client_id, created_at, status, delivery_address, scheduled_for, delivered_at, note, total_amount) VALUES
(1, 2, CURRENT_TIMESTAMP, 'PENDING', '123 Main Street, Casablanca', CURRENT_TIMESTAMP + INTERVAL '1' DAY, NULL, 'Urgent delivery', 150.0),
(2, 4, CURRENT_TIMESTAMP, 'DELIVERED', '456 Rue de Fes, Rabat', CURRENT_TIMESTAMP - INTERVAL '3' DAY, CURRENT_TIMESTAMP - INTERVAL '1' DAY, NULL, 100.0);

-- Insert order_items
INSERT INTO order_item (id, order_id, produit_id, quantity, unit_price, subtotal, note) VALUES
(1, 1, 1, 2, 50.0, 100.0, 'Handle with care'),
(2, 1, 3, 1, 150.0, 150.0, ''),
(3, 2, 2, 1, 100.0, 100.0, 'Fragile item');
