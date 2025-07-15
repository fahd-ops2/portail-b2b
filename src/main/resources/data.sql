-- Insert users (base users for livreurs and clients)
INSERT INTO app_user (id, nom, email, password, role, locked, enabled) VALUES
         (1, 'Alice Livreur', 'alice.livreur@example.com', '$2a$10$zW5gYc3z3z3z3z3z3z3z3u', 'LIVREUR', FALSE, TRUE),
         (2, 'Bob Client', 'bob.client@example.com', '$2a$10$zW5gYc3z3z3z3z3z3z3z3u', 'CLIENT', FALSE, TRUE),
         (3, 'Charlie Livreur', 'charlie.livreur@example.com', '$2a$10$zW5gYc3z3z3z3z3z3z3z3u', 'LIVREUR', FALSE, TRUE),
         (4, 'Diana Client', 'diana.client@example.com', '$2a$10$zW5gYc3z3z3z3z3z3z3z3u', 'CLIENT', FALSE, TRUE),
         (5, 'Emma Admin', 'emma.admin@example.com', '$2a$10$zW5gYc3z3z3z3z3z3z3z3u', 'ADMIN', FALSE, TRUE),
         (6, 'Frank Admin', 'frank.admin@example.com', '$2a$10$zW5gYc3z3z3z3z3z3z3z3u', 'ADMIN', FALSE, TRUE);

-- Insert livreurs (inherits from app_user)
INSERT INTO livreurs (id, phone_number, is_active) VALUES
                (1, '+212600000001', TRUE),
                (3, '+212600000003', TRUE);

-- Insert clients (inherits from app_user)
INSERT INTO clients (id, address) VALUES
            (2, '123 Main Street, Casablanca'),
            (4, '456 Rue de Fes, Rabat');

-- Insert produits
INSERT INTO produits (id, nom, prix_unitaire, stock, description, type, image) VALUES
                    ('PROD-1', 'Bouteille de Propane', 50.0, 100, 'Bouteille de propane de haute qualité pour usage industriel', 'PROPANE', 'https://images.unsplash.com/photo-1581091226825-a6a2a5aee158?w=300&h=200&fit=crop'),
                    ('PROD-2', 'Gaz Cylinder 12kg', 100.0, 50, 'Bouteille de butane standard pour applications commerciales', 'PROPANE', 'https://images.unsplash.com/photo-1581091226825-a6a2a5aee158?w=300&h=200&fit=crop'),
                    ('PROD-3', 'Gaz Cylinder 20kg', 150.0, 20, 'Bouteille de gaz pour usage intensif', 'BUTANE', 'https://images.unsplash.com/photo-1581091226825-a6a2a5aee158?w=300&h=200&fit=crop');

-- Insert Admin
INSERT INTO admin (id, telephone) VALUES
             (5, '+1234567890'),
             (6, '+0987654321');

-- Insert orders
INSERT INTO orders (id, client_id, created_at, status, delivery_address, scheduled_for, delivered_at, note, total_amount) VALUES
                    ('CMD-12345678', 2, CURRENT_TIMESTAMP, 'EN_ATTENTE', '123 Main Street, Casablanca', CURRENT_TIMESTAMP + INTERVAL '1' DAY, NULL, 'Urgent delivery', 250.0),
                    ('CMD-87654321', 4, CURRENT_TIMESTAMP, 'EN_ATTENTE', '150 Main Street, Settat', CURRENT_TIMESTAMP + INTERVAL '3' DAY, NULL, 'URGENT delivery', 100.0);

-- Insert order_items
INSERT INTO order_item (id, order_id, produit_id, quantity, unit_price, subtotal, note) VALUES
                        ('ITEM-1', 'CMD-12345678', 'PROD-1', 2, 50.0, 100.0, 'Handle with care'),
                        ('ITEM-2', 'CMD-12345678', 'PROD-3', 1, 150.0, 150.0, ''),
                        ('ITEM-3', 'CMD-87654321', 'PROD-2', 1, 100.0, 100.0, 'Fragile item');

-- Insert reclamations
INSERT INTO reclamations (id, client_id, order_id, description, type, status, created_at, updated_at, resolution_notes, file_path) VALUES
             (1, 2, 'CMD-12345678', 'Problème avec la livraison, article endommagé', 'GENERAL', 'EN_ATTENTE', CURRENT_TIMESTAMP, NULL, NULL, NULL),
             (2, 4, 'CMD-87654321', 'Commande incomplète', 'PRODUIT', 'RESOLUE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Remplacement envoyé', '/uploads/reclamation2.pdf');

-- Insérer des livraisons
INSERT INTO livraisons (ID,order_id, livreur_id, scheduled_time, status, delivery_address, notes, tracking_code, latitude, longitude, is_urgent) VALUES
                        (1,'CMD-12345678', 1, '2025-07-06 10:00:00', 'SCHEDULED', '123 Rue Exemple, Paris', 'Livrer avant midi', 'TRK-ABC12345', 48.8566, 2.3522, TRUE),
                        (2,'CMD-87654321', 3, '2025-07-06 12:00:00', 'IN_PROGRESS', '456 Avenue Test, Lyon', NULL, 'TRK-XYZ67890', 45.7640, 4.8357, FALSE);