-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Temps de generació: 14-05-2026 a les 20:17:40
-- Versió del servidor: 10.4.32-MariaDB
-- Versió de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de dades: `parking_db`
--

-- --------------------------------------------------------

--
-- Estructura de la taula `cancelled_notification`
--

CREATE TABLE `cancelled_notification` (
  `notification_id` int(11) NOT NULL,
  `old_space` varchar(20) NOT NULL,
  `new_space` varchar(20) NOT NULL,
  `notified` tinyint(1) NOT NULL,
  `reservation_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Bolcament de dades per a la taula `cancelled_notification`
--

INSERT INTO `cancelled_notification` (`notification_id`, `old_space`, `new_space`, `notified`, `reservation_id`) VALUES
(1, 'P2-C06', 'P1-C05', 0, 9);

-- --------------------------------------------------------

--
-- Estructura de la taula `parking_space`
--

CREATE TABLE `parking_space` (
  `space_code` varchar(20) NOT NULL,
  `floor` int(11) NOT NULL,
  `vehicle_type` varchar(20) NOT NULL,
  `is_occupied` tinyint(1) NOT NULL,
  `parked_vehicle_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Bolcament de dades per a la taula `parking_space`
--

INSERT INTO `parking_space` (`space_code`, `floor`, `vehicle_type`, `is_occupied`, `parked_vehicle_id`) VALUES
('P1-B01', 1, 'big_car', 1, 160),
('P1-B02', 1, 'big_car', 1, 175),
('P1-C01', 1, 'car', 1, 171),
('P1-C02', 1, 'car', 0, NULL),
('P1-C03', 1, 'car', 1, 174),
('P1-C04', 1, 'car', 0, NULL),
('P1-C05', 1, 'car', 0, NULL),
('P1-C06', 1, 'car', 0, NULL),
('P1-M01', 1, 'motorbike', 1, 172),
('P1-M02', 1, 'motorbike', 0, NULL),
('P1-M03', 1, 'motorbike', 1, 155),
('P1-M04', 1, 'motorbike', 0, NULL),
('P2-B01', 2, 'big_car', 1, 8),
('P2-B02', 2, 'big_car', 1, 169),
('P2-C01', 2, 'car', 0, NULL),
('P2-C02', 2, 'car', 1, 6),
('P2-C03', 2, 'car', 0, NULL),
('P2-C04', 2, 'car', 1, 9),
('P2-C05', 2, 'car', 0, NULL),
('P2-C06', 2, 'car', 1, 156),
('P2-M01', 2, 'motorbike', 1, 158),
('P2-M02', 2, 'motorbike', 1, 7),
('P2-M03', 2, 'motorbike', 0, NULL),
('P2-M04', 2, 'motorbike', 1, 11);

-- --------------------------------------------------------

--
-- Estructura de la taula `reservation`
--

CREATE TABLE `reservation` (
  `reservation_id` int(11) NOT NULL,
  `reservation_date` datetime NOT NULL,
  `is_active` tinyint(1) NOT NULL,
  `vehicle_id` int(11) NOT NULL,
  `space_code` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Bolcament de dades per a la taula `reservation`
--

INSERT INTO `reservation` (`reservation_id`, `reservation_date`, `is_active`, `vehicle_id`, `space_code`) VALUES
(5, '2026-04-18 08:20:00', 1, 7, 'P2-M02'),
(6, '2026-04-18 08:25:00', 1, 8, 'P2-B01'),
(7, '2026-04-18 08:30:00', 1, 9, 'P2-C04'),
(8, '2026-04-18 08:35:00', 1, 11, 'P2-M04'),
(9, '2026-04-18 07:45:00', 0, 10, 'P2-C06'),
(10, '2026-04-18 08:00:00', 1, 171, 'P1-C01'),
(11, '2026-04-18 08:05:00', 1, 172, 'P1-M01'),
(12, '2026-04-18 08:10:00', 1, 174, 'P1-C03'),
(13, '2026-04-18 08:15:00', 1, 175, 'P1-B02');

-- --------------------------------------------------------

--
-- Estructura de la taula `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Bolcament de dades per a la taula `users`
--

INSERT INTO `users` (`user_id`, `username`, `email`, `password`) VALUES
(3, 'marc', 'marc@test.com', 'Marc1234!'),
(4, 'laia', 'laia@test.com', 'Laia1234!'),
(5, 'jordi', 'jordi@test.com', 'Jordi1234!'),
(6, 'pau', 'pau@test.com', 'Pau1234!'),
(7, 'anna', 'anna@test.com', 'Anna1234!');

-- --------------------------------------------------------

--
-- Estructura de la taula `vehicle`
--

CREATE TABLE `vehicle` (
  `vehicle_id` int(11) NOT NULL,
  `license_plate` varchar(15) NOT NULL,
  `vehicle_type` varchar(20) NOT NULL,
  `is_simulated` tinyint(1) NOT NULL,
  `user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Bolcament de dades per a la taula `vehicle`
--

INSERT INTO `vehicle` (`vehicle_id`, `license_plate`, `vehicle_type`, `is_simulated`, `user_id`) VALUES
(6, '3333DDD', 'car', 0, 3),
(7, '4444EEE', 'motorbike', 0, 3),
(8, '5555FFF', 'big_car', 0, 4),
(9, '6666GGG', 'car', 0, 4),
(10, '7777HHH', 'car', 0, 5),
(11, '8888III', 'motorbike', 0, 5),
(12, '9999JJJ', 'car', 0, 5),
(155, '9772CYL', 'MOTORBIKE', 1, NULL),
(156, '9894ZZV', 'CAR', 1, NULL),
(158, '1593ECN', 'MOTORBIKE', 1, NULL),
(160, '0365UWH', 'BIG_CAR', 1, NULL),
(164, '0917OXN', 'CAR', 1, NULL),
(167, '4915WJU', 'CAR', 1, NULL),
(168, '3617ZKC', 'MOTORBIKE', 1, NULL),
(169, '7668VEU', 'BIG_CAR', 1, NULL),
(171, '1234ABC', 'car', 0, 6),
(172, '5678DEF', 'motorbike', 0, 6),
(173, '9012GHI', 'car', 0, 6),
(174, '1111BBB', 'car', 0, 7),
(175, '2222CCC', 'big_car', 0, 7);

--
-- Índexs per a les taules bolcades
--

--
-- Índexs per a la taula `cancelled_notification`
--
ALTER TABLE `cancelled_notification`
  ADD PRIMARY KEY (`notification_id`),
  ADD UNIQUE KEY `uq_cancelled_notification_reservation` (`reservation_id`);

--
-- Índexs per a la taula `parking_space`
--
ALTER TABLE `parking_space`
  ADD PRIMARY KEY (`space_code`),
  ADD UNIQUE KEY `uq_parking_space_parked_vehicle` (`parked_vehicle_id`);

--
-- Índexs per a la taula `reservation`
--
ALTER TABLE `reservation`
  ADD PRIMARY KEY (`reservation_id`),
  ADD UNIQUE KEY `uq_reservation_vehicle` (`vehicle_id`),
  ADD UNIQUE KEY `uq_reservation_space` (`space_code`);

--
-- Índexs per a la taula `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `uq_users_username` (`username`),
  ADD UNIQUE KEY `uq_users_email` (`email`);

--
-- Índexs per a la taula `vehicle`
--
ALTER TABLE `vehicle`
  ADD PRIMARY KEY (`vehicle_id`),
  ADD UNIQUE KEY `uq_vehicle_license_plate` (`license_plate`),
  ADD KEY `fk_vehicle_user` (`user_id`);

--
-- AUTO_INCREMENT per les taules bolcades
--

--
-- AUTO_INCREMENT per la taula `cancelled_notification`
--
ALTER TABLE `cancelled_notification`
  MODIFY `notification_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT per la taula `reservation`
--
ALTER TABLE `reservation`
  MODIFY `reservation_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT per la taula `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT per la taula `vehicle`
--
ALTER TABLE `vehicle`
  MODIFY `vehicle_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=176;

--
-- Restriccions per a les taules bolcades
--

--
-- Restriccions per a la taula `cancelled_notification`
--
ALTER TABLE `cancelled_notification`
  ADD CONSTRAINT `fk_cancelled_notification_reservation` FOREIGN KEY (`reservation_id`) REFERENCES `reservation` (`reservation_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Restriccions per a la taula `parking_space`
--
ALTER TABLE `parking_space`
  ADD CONSTRAINT `fk_parking_space_vehicle` FOREIGN KEY (`parked_vehicle_id`) REFERENCES `vehicle` (`vehicle_id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Restriccions per a la taula `reservation`
--
ALTER TABLE `reservation`
  ADD CONSTRAINT `fk_reservation_space` FOREIGN KEY (`space_code`) REFERENCES `parking_space` (`space_code`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_reservation_vehicle` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicle` (`vehicle_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Restriccions per a la taula `vehicle`
--
ALTER TABLE `vehicle`
  ADD CONSTRAINT `fk_vehicle_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
