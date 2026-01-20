-- --------------------------------------- --
-- --------------------------------------- --
-- Datos iniciales de la tabla de usuarios --
-- --------------------------------------- --
-- --------------------------------------- --

INSERT INTO users (id, email, password, nickname, role, created_at) VALUES
(1, 'admin@empresa.com', '$2a$10$wH7Z6F1fY8eF3cJXjz8b5uF7tJ2Lw9W3Xn6Z6h0B0P8R8q7GZb1kK', 'AdminEmpresa', 'ADMIN_NEGOCIO', NOW()),
(2, 'cliente1@email.com', '$2a$10$wH7Z6F1fY8eF3cJXjz8b5uF7tJ2Lw9W3Xn6Z6h0B0P8R8q7GZb1kK', 'Carlos', 'CLIENTE', NOW()),
(3, 'cliente2@email.com', '$2a$10$wH7Z6F1fY8eF3cJXjz8b5uF7tJ2Lw9W3Xn6Z6h0B0P8R8q7GZb1kK', 'Laura', 'CLIENTE', NOW());


-- -------------------------------------------- --
-- -------------------------------------------- --
-- Datos iniciales de la tabla de planes (SAAS) --
-- -------------------------------------------- --
-- -------------------------------------------- --

INSERT INTO plans (id, name, price, max_stores, max_users, active) VALUES
(1, 'Free', 0.00, 1, 100, true),
(2, 'Pro', 29.99, 5, 1000, true),
(3, 'Enterprise', 99.99, 50, 10000, true);


-- ----------------------------------------- --
-- ----------------------------------------- --
-- Datos iniciales de la tabla de companyias --	EMPRESA DEL USUARIO ADMINISTRADOR DE LA PLATAFORMA
-- ----------------------------------------- --
-- ----------------------------------------- --

INSERT INTO companies (
  id, owner_id, plan_id, legal_name, tax_id, subscription_status, next_billing_date
) VALUES
(1, 1, 2, 'Café Central S.L.', 'B12345678', 'ACTIVE', DATE_ADD(NOW(), INTERVAL 1 MONTH));


-- -------------------------------------- --
-- -------------------------------------- --		TIENDAS QUE VERÁN LOS USUARIOS DE LA PLATAFORMA
-- Datos iniciales de la tabla de tiendas --		2 TIENDAS CORRESPONDIENTES A LA COMPANYIA (Café Central) DEL 
-- -------------------------------------- -- 	USUARIO ADMIN DE LA PLATAFORMA
-- -------------------------------------- --

-- NOTAS:	company_id 	 -> relación con la tabla companyias
--				category 	 -> Cadena de texto que describe tipo de comercio
-- 			image_url	 -> Imagen principal de la tienda
--				points_ratio -> Cuantos puntos otorga la tienda x € euro gastado. (Ej. 10 puntos x € euro gastado)
--				is_visible	 -> Es o no, la tienda visible, para ocultarla temporalmente
-- -----------------------------------------------------------------------------------------------------------

INSERT INTO stores (
  id, company_id, name, category, address, image_url, points_ratio, is_visible
) VALUES
(1, 1, 'Café Central', 'Restaurante', 'Calle Mayor 10', 'cafe_central.png', 10, true),
(2, 1, 'Café Central Express', 'Restaurante', 'Estación Central', 'cafe_central_express.png', 5, true);


-- ------------------------------------------ --
-- ------------------------------------------ --	RECOMPENSAS RECLAMABLES POR LOS USUARIOS
-- Datos iniciales de la tabla de recompensas --	DE SUS TIENDAS FAVORITAS.
-- ------------------------------------------ -- 	Ver notas:
-- ------------------------------------------ --

INSERT INTO rewards (id, store_id, name, cost_points, image_url, active) VALUES
(1, 1, 'Café Gratis', 100, '1_cafe_central_cafe_gratis.png', true),
(2, 1, 'Croissant Gratis', 150, '1_cafe_central_croissant.png', true),
(3, 2, 'Descuento 2€', 80, '2_cafe_central_express_descuento.png', true);

-- NOTAS:	image_url 	 -> Quiero formar el nombre de la imagen, para que sea única
--								 -> con los campos "id_company + _ + company_name + _ + nombre_del_archivo_origial
-- 							 -> (Ej. 1_cafe_central_cafe_gratis.png)
-- -------------------------------------------------------------------------------------------------------


-- ---------------------------------------------------- --
-- ---------------------------------------------------- --	DIFERENTES CARTAS DE FIDELIDAD CON LAS TIENDAS QUE
-- Datos iniciales de la tabla de tarjetas de fidelidad --	QUE TIENEN LOS USUARIOS
-- ---------------------------------------------------- -- 	
-- ---------------------------------------------------- --

INSERT INTO loyalty_cards (
  id, user_id, store_id, current_balance, total_accumulated, last_visit
) VALUES
(NEWID(), 2, 1, 120, 300, NOW()),
(NEWID(), 3, 1, 50, 150, NOW()),
(NEWID(), 2, 2, 20, 20, NOW());


-- -------------------------------------------- --
-- -------------------------------------------- --	
-- Datos iniciales de la tabla de transacciones --	
-- -------------------------------------------- -- 	
-- -------------------------------------------- --

INSERT INTO transactions (id, card_id, amount, type, created_at) VALUES
(1, 1, 100, 'EARN', NOW()),
(2, 1, -50, 'REDEEM', NOW()),
(3, 2, 150, 'EARN', NOW()),
(4, 3, 20, 'EARN', NOW());








