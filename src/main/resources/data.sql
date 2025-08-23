-- 1. Insert into app_user
INSERT INTO app_user (id, nom, email, password, role, locked, enabled)
VALUES
(1, 'Fahd', 'fahd@gmail.com', '$2a$10$2YZjkWk4rJh0sDRM0Lif7eNofuDeh6QIGvyJ0qnx5DE.uYFtkhDKy', 'ROLE_ADMIN', false, true),
(2, 'Zyad', 'zyad@gmail.com', '$2a$10$2YZjkWk4rJh0sDRM0Lif7eNofuDeh6QIGvyJ0qnx5DE.uYFtkhDKy', 'ROLE_CLIENT', false, true),
(3, 'Jalal', 'jalal@gmail.com', '$2a$10$2YZjkWk4rJh0sDRM0Lif7eNofuDeh6QIGvyJ0qnx5DE.uYFtkhDKy', 'ROLE_LIVREUR', false, true);

-- 2. Insert into corresponding subtype tables:
-- Fahd is an Admin
INSERT INTO admin (id, telephone)
VALUES (1, '0612345678');

-- Zyad is a Client
INSERT INTO clients (id, address)
VALUES (2, 'Casablanca');

-- Jalal is a Livreur
INSERT INTO livreurs (id, phone_number, is_active)
VALUES (3, '0701020304', TRUE);



-- Insert produits
INSERT INTO produits (id, nom, prix_unitaire, stock,description,type,image) VALUES
(1, 'Bouteille de Propane', 50.0, 100 ,  'Bouteille de propane de haute qualité pour usage industriel','PROPANE','https://images.unsplash.com/photo-1581091226825-a6a2a5aee158?w=300&h=200&fit=crop'''),
(2, 'Gaz Cylinder 12kg', 100.0, 50, 'Bouteille de butane standard pour applications commerciales', 'PROPANE','https://images.unsplash.com/photo-1581091226825-a6a2a5aee158?w=300&h=200&fit=crop'''),
(3, 'Gaz Cylinder 20kg', 150.0, 20,'fref','BUTANE','https://images.unsplash.com/photo-1581091226825-a6a2a5aee158?w=300&h=200&fit=crop''');


-- Insert orders
INSERT INTO orders ( id, client_id, created_at, status, delivery_address, scheduled_for, delivered_at, note, total_amount) VALUES
 ( 'a5467f6a-fa47-441c-a7b6-cf5985780089', 2, CURRENT_TIMESTAMP, 'EN_ATTENTE', '123 Main Street, Casablanca', CURRENT_TIMESTAMP + INTERVAL '1' DAY, NULL, 'Urgent delivery', 150.0),
 ( 'a5467f6a-fa47-441c-a7b6-cf5985780088', 2, CURRENT_TIMESTAMP, 'EN_ATTENTE', '150 Main Street, Settat', CURRENT_TIMESTAMP + INTERVAL '3' DAY, NULL, 'URGENT delivery',  100.0);

-- Insert order_items
INSERT INTO order_item ( id , order_id, produit_id, quantity, unit_price, subtotal, note) VALUES
( 'b5467f6a-fa47-441c-a7b6-cf5985780089', 'a5467f6a-fa47-441c-a7b6-cf5985780089', 1, 2, 50.0, 100.0, 'Handle with care'),
( 'b5467f6a-fa47-441c-a7b6-cf5985780087', 'a5467f6a-fa47-441c-a7b6-cf5985780089', 3, 1, 150.0, 150.0, ''),
( 'b5467f6a-fa47-441c-a7b6-cf5985780088', 'a5467f6a-fa47-441c-a7b6-cf5985780088', 2, 1, 100.0, 100.0, 'Fragile item');

--insert reclamation
INSERT INTO reclamation(id,description,type, status, date, client_id, order_id) VALUES
(1,'Le produit reçu est endommagé à louverture','Produit défectueux','EN_ATTENTE','2025-06-06', 2, 'a5467f6a-fa47-441c-a7b6-cf5985780089'),
(2, 'Livraison retardée de 5 jours par rapport à la date prévue', 'Délai de livraison','EN_ATTENTE','2025-06-02', 2, 'a5467f6a-fa47-441c-a7b6-cf5985780088');

-- Insérer des livraisons
INSERT INTO livraisons (order_id, livreur_id, scheduled_time, status, delivery_address, notes, tracking_code, latitude, longitude, is_urgent) VALUES
 ('a5467f6a-fa47-441c-a7b6-cf5985780089', 3, '2025-07-06 10:00:00', 'SCHEDULED', '123 Rue Exemple, Paris', 'Livrer avant midi', 'TRK-ABC12345', 48.8566, 2.3522, TRUE),
 ('a5467f6a-fa47-441c-a7b6-cf5985780088', 3, '2025-07-06 12:00:00', 'IN_PROGRESS', '456 Avenue Test, Lyon', NULL, 'TRK-XYZ67890', 45.7640, 4.8357, FALSE);
