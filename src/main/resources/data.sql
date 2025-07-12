-- Insert users (base users for livreurs and clients)
INSERT INTO app_user (id, nom, email, password, role, locked, enabled) VALUES
(1, 'Alice Livreur', 'alice.livreur@example.com', 'passwordHash1', 'LIVREUR', FALSE, TRUE),
(2, 'Bob Client', 'bob.client@example.com', 'passwordHash2', 'CLIENT', FALSE, TRUE),
(3, 'Charlie Livreur', 'charlie.livreur@example.com', 'passwordHash3', 'LIVREUR', FALSE, TRUE),
(4, 'Diana Client', 'diana.client@example.com', 'passwordHash4', 'CLIENT', FALSE, TRUE),
(5, 'Emma Admin', 'emma.admin@example.com', 'passwordHash5', 'ADMIN', FALSE, TRUE),
(6, 'Frank Admin', 'frank.admin@example.com', 'passwordHash6', 'ADMIN', FALSE, TRUE);


-- Insert livreurs (inherits from app_user)
INSERT INTO livreurs (id, phone_number, is_active) VALUES
(1, '+212600000001', TRUE),
(3, '+212600000003', TRUE);

-- Insert clients (inherits from app_user)
INSERT INTO clients (id, address) VALUES
(2, '123 Main Street, Casablanca'),
(4, '456 Rue de Fes, Rabat');

-- Insert produits
INSERT INTO produits (id, nom, prix_unitaire, stock,description,type,image) VALUES
(1, 'Bouteille de Propane', 50.0, 100 ,  'Bouteille de propane de haute qualité pour usage industriel','PROPANE','https://images.unsplash.com/photo-1581091226825-a6a2a5aee158?w=300&h=200&fit=crop'''),
(2, 'Gaz Cylinder 12kg', 100.0, 50, 'Bouteille de butane standard pour applications commerciales', 'PROPANE','https://images.unsplash.com/photo-1581091226825-a6a2a5aee158?w=300&h=200&fit=crop'''),
(3, 'Gaz Cylinder 20kg', 150.0, 20,'fref','BUTANE','https://images.unsplash.com/photo-1581091226825-a6a2a5aee158?w=300&h=200&fit=crop''');

-- Insert Admin
INSERT INTO admin (id, telephone) VALUES
(5, '+1234567890'),
(6, '+0987654321');
-- Insert orders
INSERT INTO orders ( client_id, created_at, status, delivery_address, scheduled_for, delivered_at, note, total_amount) VALUES
 (2, CURRENT_TIMESTAMP, 'EN_ATTENTE', '123 Main Street, Casablanca', CURRENT_TIMESTAMP + INTERVAL '1' DAY, NULL, 'Urgent delivery', 150.0),
(4, CURRENT_TIMESTAMP, 'EN_ATTENTE', '150 Main Street, Settat', CURRENT_TIMESTAMP + INTERVAL '3' DAY, NULL, 'URGENT delivery',  100.0);

-- Insert order_items
INSERT INTO order_item ( order_id, produit_id, quantity, unit_price, subtotal, note) VALUES
( 1, 1, 2, 50.0, 100.0, 'Handle with care'),
( 1, 3, 1, 150.0, 150.0, ''),
( 2, 2, 1, 100.0, 100.0, 'Fragile item');

--insert reclamation
INSERT INTO reclamation(id,description,type, status, date) VALUES
(1,'Le produit reçu est endommagé à louverture','Produit défectueux','PENDING','2025-06-06'),
(2, 'Livraison retardée de 5 jours par rapport à la date prévue', 'Délai de livraison','IN_PROGRESS','2025-06-02');

-- Insérer des livraisons
INSERT INTO livraisons (order_id, livreur_id, scheduled_time, status, delivery_address, notes, tracking_code, latitude, longitude, is_urgent) VALUES
 (1, 1, '2025-07-06 10:00:00', 'SCHEDULED', '123 Rue Exemple, Paris', 'Livrer avant midi', 'TRK-ABC12345', 48.8566, 2.3522, TRUE),
 (2, 3, '2025-07-06 12:00:00', 'IN_PROGRESS', '456 Avenue Test, Lyon', NULL, 'TRK-XYZ67890', 45.7640, 4.8357, FALSE);
