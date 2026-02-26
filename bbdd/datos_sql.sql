USE `ruta66market`;

-- 1. LIMPIEZA PREVIA (Opcional, para evitar duplicados si ya ejecutaste algo antes)
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE `transactions`;
TRUNCATE TABLE `loyalty_cards`;
TRUNCATE TABLE `rewards`;
TRUNCATE TABLE `stores`;
TRUNCATE TABLE `companies`;
TRUNCATE TABLE `users`;
SET FOREIGN_KEY_CHECKS = 1;

-- 2. INSERTAR USUARIOS (Total: 20)
-- Roles: 1 ADMIN_PLATAFORMA, 4 ADMIN_NEGOCIO, 15 CLIENTES
INSERT INTO `users` (`id`, `email`, `password`, `nickname`, `role`, `created_at`) VALUES
(1, 'admin@ruta66.com', '$2a$10$wH7Z6...', 'SuperAdmin', 'ADMIN_PLATAFORMA', '2026-01-01 10:00:00'),
(2, 'gerente.cafe@email.com', '$2a$10$wH7Z6...', 'GerenteCafe', 'ADMIN_NEGOCIO', '2026-01-02 11:00:00'),
(3, 'owner.moda@email.com', '$2a$10$wH7Z6...', 'ModaBoss', 'ADMIN_NEGOCIO', '2026-01-02 12:00:00'),
(4, 'ceo.tech@email.com', '$2a$10$wH7Z6...', 'TechCEO', 'ADMIN_NEGOCIO', '2026-01-03 09:00:00'),
(5, 'admin.gym@email.com', '$2a$10$wH7Z6...', 'GymManager', 'ADMIN_NEGOCIO', '2026-01-03 10:00:00'),
(6, 'cliente1@email.com', '$2a$10$wH7Z6...', 'Carlos', 'CLIENTE', '2026-01-05 15:00:00'),
(7, 'cliente2@email.com', '$2a$10$wH7Z6...', 'Laura', 'CLIENTE', '2026-01-05 16:00:00'),
(8, 'ana.garcia@email.com', '$2a$10$wH7Z6...', 'AnaG', 'CLIENTE', '2026-01-06 10:00:00'),
(9, 'pedro.perez@email.com', '$2a$10$wH7Z6...', 'Peter', 'CLIENTE', '2026-01-06 11:00:00'),
(10, 'sofia.m@email.com', '$2a$10$wH7Z6...', 'Sofi', 'CLIENTE', '2026-01-07 09:30:00'),
(11, 'lucas.trend@email.com', '$2a$10$wH7Z6...', 'Lucas', 'CLIENTE', '2026-01-08 14:20:00'),
(12, 'marta.shop@email.com', '$2a$10$wH7Z6...', 'MartaS', 'CLIENTE', '2026-01-08 15:10:00'),
(13, 'javier.sol@email.com', '$2a$10$wH7Z6...', 'Javi', 'CLIENTE', '2026-01-09 18:00:00'),
(14, 'elena.vlc@email.com', '$2a$10$wH7Z6...', 'Ele', 'CLIENTE', '2026-01-10 12:00:00'),
(15, 'marcos.ruiz@email.com', '$2a$10$wH7Z6...', 'Marcos', 'CLIENTE', '2026-01-11 11:00:00'),
(16, 'sergio.t@email.com', '$2a$10$wH7Z6...', 'Serch', 'CLIENTE', '2026-01-12 10:00:00'),
(17, 'nuria.v@email.com', '$2a$10$wH7Z6...', 'Nuria', 'CLIENTE', '2026-01-13 09:00:00'),
(18, 'pablo.d@email.com', '$2a$10$wH7Z6...', 'Pablito', 'CLIENTE', '2026-01-14 17:00:00'),
(19, 'carla.f@email.com', '$2a$10$wH7Z6...', 'Carla', 'CLIENTE', '2026-01-15 13:00:00'),
(20, 'diego.s@email.com', '$2a$10$wH7Z6...', 'Diego', 'CLIENTE', '2026-01-16 16:00:00');

-- 3. INSERTAR COMPAÑÍAS (Total: 4 principales, repetidas para llegar a 20 entradas o planes)
-- Nota: En un SaaS suele haber una compañía por Admin, aquí creamos las 4 base con diferentes planes.
INSERT INTO `companies` (`id`, `owner_id`, `plan_id`, `legal_name`, `tax_id`, `subscription_status`, `next_billing_date`) VALUES
(1, 2, 2, 'Café Central S.L.', 'B12345678', 'ACTIVE', '2026-04-22 18:32:22'),
(2, 3, 2, 'Moda Urbana S.L.', 'B87654321', 'ACTIVE', '2026-05-15 10:00:00'),
(3, 4, 3, 'Tech World S.A.', 'A99887766', 'ACTIVE', '2026-06-01 09:00:00'),
(4, 5, 1, 'Fit Life Gym Group', 'B55443322', 'ACTIVE', '2026-03-20 12:00:00');

-- 4. INSERTAR TIENDAS (Total: 20)
-- Distribuidas entre las 4 compañías
INSERT INTO `stores` (`id`, `company_id`, `name`, `category`, `address`, `image_url`, `points_ratio`, `is_visible`) VALUES
(1, 1, 'Café Central Mayor', 'Restaurante', 'Calle Mayor 10', 'cafe_central.png', 10, 1),
(2, 1, 'Café Central Express', 'Restaurante', 'Estación Norte', 'cafe_express.png', 5, 1),
(3, 1, 'Café Central Playa', 'Restaurante', 'Paseo Marítimo 5', 'cafe_playa.png', 10, 1),
(4, 1, 'Café Central Aeropuerto', 'Restaurante', 'Terminal T2', 'cafe_aero.png', 8, 1),
(5, 2, 'Moda Urbana Centro', 'Ropa', 'Gran Vía 45', 'moda_centro.png', 15, 1),
(6, 2, 'Moda Urbana Outlet', 'Ropa', 'Polígono Ind 4', 'moda_outlet.png', 20, 1),
(7, 2, 'Moda Urbana Kids', 'Ropa', 'CC El Saler', 'moda_kids.png', 15, 1),
(8, 2, 'Moda Urbana Sport', 'Ropa', 'Calle Colon 12', 'moda_sport.png', 15, 1),
(9, 3, 'Tech World - Principal', 'Electrónica', 'Av. Francia 20', 'tech_main.png', 5, 1),
(10, 3, 'Tech World - Mobile', 'Electrónica', 'CC Bonaire', 'tech_mobile.png', 5, 1),
(11, 3, 'Tech World - Gaming', 'Electrónica', 'Calle Sueca 5', 'tech_gaming.png', 7, 1),
(12, 3, 'Tech World - Repair', 'Electrónica', 'Calle Cadiz 2', 'tech_repair.png', 10, 1),
(13, 4, 'Fit Life Central', 'Gimnasio', 'Av. Baleares 12', 'gym_central.png', 12, 1),
(14, 4, 'Fit Life Ruzafa', 'Gimnasio', 'Calle Literato 8', 'gym_ruzafa.png', 12, 1),
(15, 4, 'Fit Life Cross', 'Gimnasio', 'Nave 5 Poligono', 'gym_cross.png', 15, 1),
(16, 1, 'Café Central Corner', 'Restaurante', 'Biblioteca Municipal', 'cafe_corner.png', 10, 1),
(17, 2, 'Moda Urbana Shoes', 'Ropa', 'Calle Mayor 5', 'moda_shoes.png', 15, 1),
(18, 3, 'Tech World Outlet', 'Electrónica', 'Online Only', 'tech_online.png', 10, 1),
(19, 4, 'Fit Life Yoga', 'Gimnasio', 'Calle Paz 1', 'gym_yoga.png', 12, 1),
(20, 4, 'Fit Life Pool', 'Gimnasio', 'Polideportivo Sur', 'gym_pool.png', 10, 1);

-- 5. INSERTAR RECOMPENSAS (Total: 20)
-- Siguiendo formato: company_{id}_{name}_{file}
INSERT INTO `rewards` (`id`, `store_id`, `name`, `description`, `cost_points`, `image_url`, `active`) VALUES
(1, 1, 'Café Gratis', 'Espresso o con leche', 100, 'company_1_cafe_central_cafe.png', 1),
(2, 1, 'Croissant', 'Mantequilla artesana', 150, 'company_1_cafe_central_croissant.png', 1),
(3, 5, 'Camiseta Básica', '100% Algodón', 500, 'company_2_moda_urbana_camiseta.png', 1),
(4, 5, 'Calcetines Style', 'Pack de 2 pares', 300, 'company_2_moda_urbana_calcetines.png', 1),
(5, 9, 'Protector Pantalla', 'Cristal templado', 400, 'company_3_tech_world_protector.png', 1),
(6, 9, 'Limpieza PC', 'Mantenimiento básico', 1200, 'company_3_tech_world_limpieza.png', 1),
(7, 13, 'Batido Proteína', 'Sabor a elegir', 200, 'company_4_fit_life_batido.png', 1),
(8, 13, 'Toalla Gym', 'Microfibra logo', 600, 'company_4_fit_life_toalla.png', 1),
(9, 2, 'Donut Chocolate', 'Glaseado especial', 120, 'company_1_cafe_central_donut.png', 1),
(10, 6, 'Vale 10€', 'Descuento directo', 1000, 'company_2_moda_urbana_vale10.png', 1),
(11, 10, 'Funda Móvil', 'Silicona varios colores', 800, 'company_3_tech_world_funda.png', 1),
(12, 14, 'Pase Día Amigo', 'Trae a alguien gratis', 1500, 'company_4_fit_life_pasedia.png', 1),
(13, 3, 'Zumo Natural', 'Naranja exprimida', 200, 'company_1_cafe_central_zumo.png', 1),
(14, 7, 'Gorra Logo', 'Talla única', 700, 'company_2_moda_urbana_gorra.png', 1),
(15, 11, 'Alfombrilla Gaming', 'Superficie XL', 1500, 'company_3_tech_world_mousepad.png', 1),
(16, 15, 'Sesión Personal', '30 min entrenador', 3000, 'company_4_fit_life_entrenador.png', 1),
(17, 4, 'Muffin XL', 'Arándanos o Choco', 180, 'company_1_cafe_central_muffin.png', 1),
(18, 8, 'Bolsa Deporte', 'Impermeable', 2500, 'company_2_moda_urbana_bolsa.png', 1),
(19, 12, 'Cable USB-C', 'Carga rápida 2m', 600, 'company_3_tech_world_cable.png', 1),
(20, 16, 'Cookie Artesana', 'Recién horneada', 90, 'company_1_cafe_central_cookie.png', 1);

-- 6. INSERTAR TARJETAS DE FIDELIDAD (Total: 20)
-- Vinculando clientes con diferentes tiendas
INSERT INTO `loyalty_cards` (`id`, `user_id`, `store_id`, `current_balance`, `total_accumulated`, `last_visit`) VALUES
(1, 6, 1, 120, 300, '2026-02-20 18:00:00'),
(2, 7, 1, 50, 150, '2026-02-21 09:00:00'),
(3, 6, 5, 450, 1200, '2026-02-22 14:00:00'),
(4, 8, 9, 100, 500, '2026-02-23 11:00:00'),
(5, 9, 13, 200, 200, '2026-02-24 19:00:00'),
(6, 10, 1, 80, 80, '2026-02-25 08:30:00'),
(7, 11, 5, 1500, 3000, '2026-02-20 17:00:00'),
(8, 12, 10, 25, 1000, '2026-02-21 12:00:00'),
(9, 13, 13, 600, 600, '2026-02-22 20:00:00'),
(10, 14, 2, 45, 45, '2026-02-23 09:00:00'),
(11, 15, 6, 0, 2000, '2026-02-24 18:30:00'),
(12, 16, 11, 75, 75, '2026-02-25 10:00:00'),
(13, 17, 14, 300, 300, '2026-02-26 07:00:00'),
(14, 18, 3, 210, 500, '2026-02-20 13:00:00'),
(15, 19, 7, 1200, 1200, '2026-02-21 16:00:00'),
(16, 20, 12, 50, 50, '2026-02-22 11:00:00'),
(17, 6, 2, 30, 30, '2026-02-23 15:00:00'),
(18, 7, 6, 100, 800, '2026-02-24 12:00:00'),
(19, 8, 13, 400, 400, '2026-02-25 19:00:00'),
(20, 9, 1, 15, 15, '2026-02-26 08:00:00');

-- 7. INSERTAR TRANSACCIONES (Total: 20)
INSERT INTO `transactions` (`id`, `card_id`, `amount`, `type`, `created_at`) VALUES
(1, 1, 100, 'EARN', '2026-02-20 18:00:00'),
(2, 3, 500, 'EARN', '2026-02-22 14:00:00'),
(3, 3, -100, 'REDEEM', '2026-02-22 14:05:00'),
(4, 7, 1500, 'EARN', '2026-02-20 17:00:00'),
(5, 5, 200, 'EARN', '2026-02-24 19:00:00'),
(6, 11, 1000, 'EARN', '2026-02-24 18:30:00'),
(7, 11, -1000, 'REDEEM', '2026-02-24 18:35:00'),
(8, 15, 1200, 'EARN', '2026-02-21 16:00:00'),
(9, 13, 300, 'EARN', '2026-02-26 07:00:00'),
(10, 4, 500, 'EARN', '2026-02-23 11:00:00'),
(11, 8, 1000, 'EARN', '2026-02-21 12:00:00'),
(12, 19, 400, 'EARN', '2026-02-25 19:00:00'),
(13, 2, 150, 'EARN', '2026-02-21 09:00:00'),
(14, 1, 200, 'EARN', '2026-02-20 18:30:00'),
(15, 6, 80, 'EARN', '2026-02-25 08:30:00'),
(16, 9, 600, 'EARN', '2026-02-22 20:00:00'),
(17, 14, 500, 'EARN', '2026-02-20 13:00:00'),
(18, 14, -200, 'REDEEM', '2026-02-20 13:10:00'),
(19, 18, 800, 'EARN', '2026-02-24 12:00:00'),
(20, 15, -300, 'REDEEM', '2026-02-21 16:30:00');