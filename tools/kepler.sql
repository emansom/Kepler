-- phpMyAdmin SQL Dump
-- version 4.8.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Aug 04, 2018 at 07:55 AM
-- Server version: 10.3.7-MariaDB
-- PHP Version: 7.2.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `kepler`
--

-- --------------------------------------------------------

--
-- Table structure for table `catalogue_items`
--

CREATE TABLE `catalogue_items` (
  `id` int(11) NOT NULL,
  `sale_code` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `page_id` int(11) DEFAULT NULL,
  `order_id` int(11) NOT NULL DEFAULT 0,
  `price` int(11) NOT NULL DEFAULT 3,
  `definition_id` int(11) DEFAULT NULL,
  `item_specialspriteid` int(11) NOT NULL DEFAULT 0,
  `package_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `package_description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_package` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `catalogue_items`
--

INSERT INTO `catalogue_items` (`id`, `sale_code`, `page_id`, `order_id`, `price`, `definition_id`, `item_specialspriteid`, `package_name`, `package_description`, `is_package`) VALUES
(1, 'floor', 3, 1, 2, 249, 0, NULL, NULL, 0),
(2, 'CF_50_goldbar', 4, 5, 50, 212, 0, NULL, NULL, 0),
(3, 'CF_20_moneybag', 4, 4, 20, 211, 0, NULL, NULL, 0),
(4, 'CF_10_coin_gold', 4, 3, 10, 209, 0, NULL, NULL, 0),
(5, 'CF_5_coin_silver', 4, 2, 5, 213, 0, NULL, NULL, 0),
(6, 'CF_1_coin_bronze', 4, 1, 1, 210, 0, NULL, NULL, 0),
(7, 'deal_rollers_red_5', 5, 1, 25, 0, 0, 'Red Roller 5Pack', '5 Red Rollers in a convenient pack', 1),
(8, 'deal_rollers_red_3', 5, 2, 15, 0, 0, 'Red Roller 3Pack', '3 Red Rollers in a convenient pack', 1),
(9, 'roller_red', 5, 3, 7, 184, 0, NULL, NULL, 0),
(10, 'deal_rollers_blue_5', 5, 4, 25, 0, 0, 'Blue Roller 5Pack', '5 Blue Rollers in a convenient pack', 1),
(11, 'deal_rollers_blue_3', 5, 5, 15, 0, 0, 'Blue Roller 3Pack', '3 Blue Rollers in a convenient pack', 1),
(12, 'roller_blue', 5, 6, 7, 180, 0, NULL, NULL, 0),
(13, 'deal_rollers_green_5', 5, 7, 25, 0, 0, 'Green Roller 5Pack', '5 Green Rollers in a convenient pack', 1),
(14, 'deal_rollers_green_3', 5, 8, 15, 0, 0, 'Green Roller 3Pack', '3 Green Rollers in a convenient pack', 1),
(15, 'roller_green', 5, 9, 7, 181, 0, NULL, NULL, 0),
(16, 'deal_rollers_navy_5', 5, 10, 25, 0, 0, 'Navy Roller 5Pack', '5 Navy Rollers in a convenient pack', 1),
(17, 'deal_rollers_navy_3', 5, 11, 15, 0, 0, 'Navy Roller 3Pack', '3 Navy Rollers in a convenient pack', 1),
(18, 'roller_navy', 5, 12, 7, 182, 0, NULL, NULL, 0),
(19, 'deal_rollers_purple_5', 5, 14, 25, 0, 0, 'Purple Roller 5Pack', '5 Purple Rollers in a convenient pack', 1),
(20, 'roller_purple', 5, 16, 7, 183, 0, NULL, NULL, 0),
(21, 'deal_rollers_purple_3', 5, 15, 15, 0, 0, 'Purple Roller 3Pack', '3 Purple Rollers in a convenient pack', 1),
(22, 'tele_london', 6, 1, 5, 100, 0, NULL, NULL, 0),
(23, 'tele_portaloo', 6, 2, 4, 102, 0, NULL, NULL, 0),
(24, 'tele_wardrobe', 6, 3, 3, 101, 0, NULL, NULL, 0),
(25, 'pet0', 7, 1, 20, 154, 0, NULL, NULL, 0),
(26, 'pet1', 7, 2, 20, 154, 0, NULL, NULL, 0),
(27, 'pet2', 7, 3, 20, 154, 0, NULL, NULL, 0),
(28, 'deal_dogfood', 8, 1, 2, 0, 0, 'Doggy Bones', 'Natural nutrition for the barking one', 1),
(29, 'deal_catfood', 8, 2, 2, 0, 0, 'Sardines', 'Fresh catch of the day', 1),
(30, 'deal_crocfood', 8, 3, 2, 0, 0, 'T-Bones', 'For the croc!', 1),
(31, 'deal_cabbage', 8, 4, 2, 0, 0, 'Cabbage', 'Health food for pets', 1),
(32, 'dogfood', 8, 5, 1, 155, 0, NULL, NULL, 0),
(33, 'catfood', 8, 6, 1, 156, 0, NULL, NULL, 0),
(34, 'crocfood', 8, 7, 1, 236, 0, NULL, NULL, 0),
(35, 'cabbage', 8, 8, 1, 157, 0, NULL, NULL, 0),
(36, 'waterbowlblue', 8, 9, 2, 158, 0, NULL, NULL, 0),
(37, 'waterbowlbrown', 8, 10, 2, 159, 0, NULL, NULL, 0),
(38, 'waterbowlgreen', 8, 11, 2, 160, 0, NULL, NULL, 0),
(39, 'waterbowlred', 8, 12, 2, 161, 0, NULL, NULL, 0),
(40, 'waterbowlyellow', 8, 13, 2, 162, 0, NULL, NULL, 0),
(41, 'chocmouse', 8, 14, 1, 169, 0, NULL, NULL, 0),
(42, 'marzipanman', 8, 15, 1, 168, 0, NULL, NULL, 0),
(43, 'petball1', 8, 16, 2, 163, 0, NULL, NULL, 0),
(44, 'petball2', 8, 17, 2, 164, 0, NULL, NULL, 0),
(45, 'petball3', 8, 18, 2, 165, 0, NULL, NULL, 0),
(46, 'petball4', 8, 19, 2, 166, 0, NULL, NULL, 0),
(47, 'petball5', 8, 20, 2, 167, 0, NULL, NULL, 0),
(48, 'bed_silo_two', 9, 1, 3, 20, 0, NULL, NULL, 0),
(49, 'bed_silo_one', 9, 2, 3, 19, 0, NULL, NULL, 0),
(50, 'shelves_silo', 9, 3, 3, 3, 0, NULL, NULL, 0),
(51, 'sofa_silo', 9, 4, 3, 9, 0, NULL, NULL, 0),
(52, 'sofachair_silo', 9, 5, 3, 16, 0, NULL, NULL, 0),
(53, 'kuttafel', 9, 6, 1, 21, 0, NULL, NULL, 0),
(54, 'silo_gate', 9, 7, 6, 122, 0, NULL, NULL, 0),
(55, 'silo_screen', 9, 8, 3, 120, 0, NULL, NULL, 0),
(56, 'silo_corner', 9, 9, 3, 118, 0, NULL, NULL, 0),
(57, 'chair_silo', 9, 10, 3, 8, 0, NULL, NULL, 0),
(58, 'safe_silo', 9, 11, 3, 203, 0, NULL, NULL, 0),
(59, 'barchair_silo', 9, 12, 3, 197, 0, NULL, NULL, 0),
(60, 'table_silo_med', 9, 13, 3, 6, 0, NULL, NULL, 0),
(61, 'gothicchair', 10, 1, 10, 229, 0, NULL, NULL, 0),
(62, 'gothicsofa', 10, 2, 7, 230, 0, NULL, NULL, 0),
(63, 'gothicstool', 10, 3, 5, 231, 0, NULL, NULL, 0),
(64, 'gothic_carpet1', 10, 4, 5, 237, 0, NULL, NULL, 0),
(65, 'gothic_carpet2', 10, 5, 5, 238, 0, NULL, NULL, 0),
(66, 'goth_table', 10, 6, 15, 218, 0, NULL, NULL, 0),
(67, 'gothrailing', 10, 7, 8, 217, 0, NULL, NULL, 0),
(68, 'gothgate', 10, 8, 10, 215, 0, NULL, NULL, 0),
(69, 'goth_torch', 10, 9, 7, 248, 0, NULL, NULL, 0),
(70, 'goth_fountain', 10, 10, 7, 245, 0, NULL, NULL, 0),
(71, 'gothiccandle', 10, 11, 8, 216, 0, NULL, NULL, 0),
(72, 'industrialfan', 10, 12, 5, 247, 0, NULL, NULL, 0),
(73, 'deal_soundmachine1', 11, 1, 10, 0, 0, 'Soundmachine starterset', 'Gray soundmachine with Duck Funk sampleset', 1),
(74, 'soundset1', 11, 2, 3, 239, 0, NULL, NULL, 0),
(75, 'soundset2', 11, 9, 3, 358, 0, '', '', 0),
(76, 'soundset4', 11, 9, 3, 359, 0, '', '', 0),
(77, 'soundset5', 11, 9, 3, 360, 0, '', '', 0),
(78, 'soundset3', 11, 4, 3, 240, 0, '', '', 0),
(79, 'soundset6', 11, 9, 3, 241, 0, '', '', 0),
(80, 'soundset7', 11, 9, 3, 242, 0, '', '', 0),
(81, 'soundset9', 11, 9, 3, 356, 0, '', '', 0),
(82, 'soundset8', 11, 9, 3, 243, 0, '', '', 0),
(83, 'candy_bar', 12, 1, 3, 198, 0, NULL, NULL, 0),
(84, 'candy_corner', 12, 2, 3, 199, 0, NULL, NULL, 0),
(85, 'candy_hatch', 12, 3, 6, 200, 0, NULL, NULL, 0),
(86, 'candy_sofachair', 12, 4, 3, 64, 0, NULL, NULL, 0),
(87, 'candy_sofa', 12, 5, 4, 67, 0, NULL, NULL, 0),
(88, 'candy_rug', 12, 6, 0, 62, 0, NULL, NULL, 0),
(89, 'candy_bed_one', 12, 7, 3, 65, 0, NULL, NULL, 0),
(90, 'candy_bed_two', 12, 8, 4, 66, 0, NULL, NULL, 0),
(91, 'wall_china', 13, 1, 8, 207, 0, NULL, NULL, 0),
(92, 'corner_china', 13, 2, 8, 208, 0, NULL, NULL, 0),
(93, 'shelve_china', 13, 3, 8, 204, 0, NULL, NULL, 0),
(94, 'chair_china', 13, 4, 5, 201, 0, NULL, NULL, 0),
(95, 'table_china', 13, 5, 6, 202, 0, NULL, NULL, 0),
(96, 'sofa_china', 13, 6, 9, 186, 0, NULL, NULL, 0),
(97, 'poster_china', 13, 7, 3, 251, 57, NULL, NULL, 0),
(98, 'knots_china', 13, 8, 5, 251, 58, NULL, NULL, 0),
(99, 'lantern_china', 13, 9, 9, 185, 0, NULL, NULL, 0),
(100, 'chair_norja', 14, 1, 3, 11, 0, NULL, NULL, 0),
(101, 'couch_norja', 14, 2, 3, 10, 0, NULL, NULL, 0),
(102, 'table_norja_med', 14, 3, 3, 252, 0, NULL, NULL, 0),
(103, 'shelves_norja', 14, 4, 3, 1, 0, NULL, NULL, 0),
(104, 'soft_sofachair_norja', 14, 5, 3, 95, 0, NULL, NULL, 0),
(105, 'soft_sofa_norja', 14, 6, 4, 96, 0, NULL, NULL, 0),
(106, 'norja_bardesk', 14, 7, 3, 119, 0, NULL, NULL, 0),
(107, 'norja_barcorner', 14, 8, 3, 117, 0, NULL, NULL, 0),
(108, 'norja_hatch', 14, 9, 6, 121, 0, NULL, NULL, 0),
(109, 'norja_shutter', 14, 10, 5, 206, 0, NULL, NULL, 0),
(110, 'norja_shutter_corner', 14, 11, 4, 205, 0, NULL, NULL, 0),
(111, 'bed_armas_two', 15, 1, 3, 22, 0, NULL, NULL, 0),
(112, 'bed_armas_one', 15, 2, 3, 30, 0, NULL, NULL, 0),
(113, 'fireplace_armas', 15, 3, 4, 28, 0, NULL, NULL, 0),
(114, 'bartable_armas', 15, 4, 3, 51, 0, NULL, NULL, 0),
(115, 'table_armas', 15, 5, 3, 25, 0, NULL, NULL, 0),
(116, 'bench_armas', 15, 6, 3, 24, 0, NULL, NULL, 0),
(117, 'divider_armas', 15, 7, 3, 115, 0, NULL, NULL, 0),
(118, 'corner_armas', 15, 8, 3, 114, 0, NULL, NULL, 0),
(119, 'gate_armas', 15, 9, 6, 116, 0, NULL, NULL, 0),
(120, 'shelves_armas', 15, 10, 3, 23, 0, NULL, NULL, 0),
(121, 'bar_armas', 15, 11, 4, 50, 0, NULL, NULL, 0),
(122, 'bar_chair_armas', 15, 12, 1, 52, 0, NULL, NULL, 0),
(123, 'lamp_armas', 15, 13, 3, 29, 0, NULL, NULL, 0),
(124, 'candle_armas', 15, 14, 3, 98, 0, NULL, NULL, 0),
(125, 'small_table_armas', 15, 15, 2, 26, 0, NULL, NULL, 0),
(126, 'small_chair_armas', 15, 16, 1, 27, 0, NULL, NULL, 0),
(127, 'bed_budgetb_one', 17, 1, 3, 68, 0, NULL, NULL, 0),
(128, 'bed_budgetb_two', 17, 2, 3, 69, 0, NULL, NULL, 0),
(129, 'shelves_basic', 17, 3, 3, 94, 0, NULL, NULL, 0),
(130, 'bar_basic', 17, 4, 4, 93, 0, NULL, NULL, 0),
(131, 'fridge', 17, 5, 6, 99, 0, NULL, NULL, 0),
(132, 'lamp_basic', 17, 6, 3, 97, 0, NULL, NULL, 0),
(133, 'bed_polyfon_two', 18, 1, 4, 17, 0, NULL, NULL, 0),
(134, 'bed_polyfon_one', 18, 2, 3, 18, 0, NULL, NULL, 0),
(135, 'fireplace_polyfon', 18, 3, 5, 34, 0, NULL, NULL, 0),
(136, 'sofachair_polyfon', 18, 5, 3, 15, 0, NULL, NULL, 0),
(137, 'bar_polyfon', 18, 6, 5, 45, 0, NULL, NULL, 0),
(138, 'mode_bardesk', 18, 7, 3, 48, 0, NULL, NULL, 0),
(139, 'mode_barcorner', 18, 8, 3, 49, 0, NULL, NULL, 0),
(140, 'mode_hatch', 18, 9, 6, 113, 0, NULL, NULL, 0),
(141, 'dinerchair_polyfon', 18, 10, 3, 5, 0, NULL, NULL, 0),
(142, 'dinertable_polyfon', 18, 12, 4, 63, 0, NULL, NULL, 0),
(143, 'coffeetable_polyfon2', 18, 13, 3, 12, 0, NULL, NULL, 0),
(144, 'table_polyfon_small', 18, 14, 1, 4, 0, NULL, NULL, 0),
(145, 'shelves_polyfon', 18, 15, 4, 2, 0, NULL, NULL, 0),
(146, 'polyfon_zshelf', 18, 16, 1, 7, 0, NULL, NULL, 0),
(147, 'tv_luxus', 19, 1, 6, 83, 0, NULL, NULL, 0),
(148, 'wood_tv', 19, 2, 4, 61, 0, NULL, NULL, 0),
(149, 'red_tv', 19, 3, 3, 60, 0, NULL, NULL, 0),
(150, 'stickies', 19, 4, 3, 244, 0, NULL, NULL, 0),
(151, 'pizzabox', 19, 5, 3, 43, 0, NULL, NULL, 0),
(152, 'drinks', 19, 6, 3, 44, 0, NULL, NULL, 0),
(153, 'bottle', 19, 7, 3, 47, 0, NULL, NULL, 0),
(154, 'holodice', 19, 8, 6, 111, 0, NULL, NULL, 0),
(155, 'cakewasalie', 19, 9, 4, 109, 0, NULL, NULL, 0),
(156, 'menorah', 19, 10, 3, 103, 0, NULL, NULL, 0),
(157, 'bunny', 19, 11, 3, 110, 0, NULL, NULL, 0),
(158, 'mummy', 19, 12, 3, 251, 44, NULL, NULL, 0),
(159, 'wcandleset', 19, 13, 3, 105, 0, NULL, NULL, 0),
(160, 'rcandleset', 19, 14, 3, 106, 0, NULL, NULL, 0),
(161, 'ham', 19, 15, 3, 104, 0, NULL, NULL, 0),
(162, 'alert', 19, 16, 5, 131, 0, NULL, NULL, 0),
(163, 'bath', 20, 1, 6, 84, 0, NULL, NULL, 0),
(164, 'sink', 20, 2, 3, 85, 0, NULL, NULL, 0),
(165, 'duck', 20, 3, 1, 87, 0, NULL, NULL, 0),
(166, 'toilet_blue', 20, 4, 4, 86, 0, NULL, NULL, 0),
(167, 'toilet_red', 20, 5, 4, 89, 0, NULL, NULL, 0),
(168, 'toilet_yellow', 20, 6, 4, 90, 0, NULL, NULL, 0),
(169, 'tile_blue', 20, 7, 3, 88, 0, NULL, NULL, 0),
(170, 'tile_red', 20, 8, 3, 91, 0, NULL, NULL, 0),
(171, 'tile_yellow', 20, 9, 3, 92, 0, NULL, NULL, 0),
(172, 'plant_flowers', 21, 1, 4, 108, 0, NULL, NULL, 0),
(173, 'plant_rose', 21, 2, 3, 82, 0, NULL, NULL, 0),
(174, 'plant_sunflower', 21, 3, 3, 81, 0, NULL, NULL, 0),
(175, 'plant_yukka', 21, 4, 3, 75, 0, NULL, NULL, 0),
(176, 'plant_ananas', 21, 5, 3, 70, 0, NULL, NULL, 0),
(177, 'plant_bonsai', 21, 6, 3, 73, 0, NULL, NULL, 0),
(178, 'plant_big_cactus', 21, 7, 3, 74, 0, NULL, NULL, 0),
(179, 'plant_small_cactus', 21, 8, 1, 72, 0, NULL, NULL, 0),
(180, 'plant_fruittree', 21, 9, 3, 71, 0, NULL, NULL, 0),
(181, 'plant_bulrush', 21, 10, 3, 235, 0, NULL, NULL, 0),
(182, 'plant_aloevera', 21, 11, 6, 46, 0, NULL, NULL, 0),
(183, 'plant_mazehedge', 21, 12, 5, 234, 0, NULL, NULL, 0),
(184, 'plant_mazegate', 21, 13, 6, 233, 0, NULL, NULL, 0),
(185, 'poster_pedobear', 2, 1, 25, 251, 1338, NULL, NULL, 0),
(186, 'hockeystick1', 22, 1, 3, 251, 52, NULL, NULL, 0),
(187, 'hockeystick2', 22, 2, 3, 251, 53, NULL, NULL, 0),
(188, 'hockeystick3', 22, 3, 3, 251, 54, NULL, NULL, 0),
(189, 'scoreboard', 22, 4, 6, 130, 0, NULL, NULL, 0),
(190, 'bballthrophy', 22, 5, 6, 126, 0, NULL, NULL, 0),
(191, 'bballhoop', 22, 6, 3, 251, 51, NULL, NULL, 0),
(192, 'bballcourt', 22, 7, 3, 124, 0, NULL, NULL, 0),
(193, 'sportbench', 22, 8, 6, 125, 0, NULL, NULL, 0),
(194, 'track_straight_tartan', 22, 9, 3, 187, 0, NULL, NULL, 0),
(195, 'track_straight_asphalt', 22, 12, 3, 189, 0, NULL, NULL, 0),
(196, 'track_straight_grass', 22, 15, 3, 188, 0, NULL, NULL, 0),
(197, 'track_corner_tartan', 22, 10, 3, 190, 0, NULL, NULL, 0),
(198, 'track_corner_asphalt', 22, 13, 3, 191, 0, NULL, NULL, 0),
(199, 'track_corner_grass', 22, 16, 3, 192, 0, NULL, NULL, 0),
(200, 'track_goal_tartan', 22, 11, 3, 193, 0, NULL, NULL, 0),
(201, 'track_goal_asphalt', 22, 14, 3, 194, 0, NULL, NULL, 0),
(202, 'track_goal_grass', 22, 17, 3, 195, 0, NULL, NULL, 0),
(203, 'soccerlamp', 22, 18, 4, 196, 0, NULL, NULL, 0),
(204, 'carpet_standard4', 23, 1, 3, 39, 0, NULL, NULL, 0),
(205, 'carpet_standard5', 23, 2, 3, 41, 0, NULL, NULL, 0),
(206, 'carpet_standard', 23, 3, 3, 31, 0, NULL, NULL, 0),
(207, 'carpet_standardb', 23, 4, 3, 80, 0, NULL, NULL, 0),
(208, 'carpet_standarda', 23, 5, 3, 79, 0, NULL, NULL, 0),
(209, 'carpet_standard3', 23, 6, 3, 38, 0, NULL, NULL, 0),
(210, 'carpet_standard2', 23, 7, 3, 37, 0, NULL, NULL, 0),
(211, 'carpet_standard8', 23, 8, 3, 77, 0, NULL, NULL, 0),
(212, 'carpet_standard7', 23, 9, 3, 76, 0, NULL, NULL, 0),
(213, 'carpet_standard1', 23, 10, 3, 35, 0, NULL, NULL, 0),
(214, 'carpet_standard6', 23, 11, 3, 42, 0, NULL, NULL, 0),
(215, 'carpet_standard9', 23, 12, 3, 78, 0, NULL, NULL, 0),
(216, 'carpet_soft', 23, 13, 3, 53, 0, NULL, NULL, 0),
(217, 'carpet_soft1', 23, 14, 3, 54, 0, NULL, NULL, 0),
(218, 'carpet_soft2', 23, 15, 3, 55, 0, NULL, NULL, 0),
(219, 'carpet_soft3', 23, 16, 3, 56, 0, NULL, NULL, 0),
(220, 'carpet_soft4', 23, 17, 3, 57, 0, NULL, NULL, 0),
(221, 'carpet_soft5', 23, 18, 3, 58, 0, NULL, NULL, 0),
(222, 'carpet_soft6', 23, 19, 3, 59, 0, NULL, NULL, 0),
(223, 'doormat_love', 23, 20, 1, 13, 0, NULL, NULL, 0),
(224, 'doormat0', 23, 21, 1, 14, 0, NULL, NULL, 0),
(225, 'doormat1', 23, 22, 1, 36, 0, NULL, NULL, 0),
(226, 'doormat2', 23, 23, 1, 254, 0, NULL, NULL, 0),
(227, 'doormat3', 23, 24, 1, 255, 0, NULL, NULL, 0),
(228, 'doormat4', 23, 25, 1, 253, 0, NULL, NULL, 0),
(229, 'doormat5', 23, 26, 1, 256, 0, NULL, NULL, 0),
(230, 'doormat6', 23, 27, 1, 40, 0, NULL, NULL, 0),
(231, 'carpet_handwoven', 23, 28, 3, 32, 0, NULL, NULL, 0),
(232, 'carpet_polarbear', 23, 29, 4, 33, 0, NULL, NULL, 0),
(233, 'trophy_duck_bronze', 26, 1, 8, 133, 0, NULL, NULL, 0),
(234, 'trophy_globe_bronze', 26, 2, 8, 134, 0, NULL, NULL, 0),
(235, 'trophy_fish_bronze', 26, 3, 8, 135, 0, NULL, NULL, 0),
(236, 'trophy_duo_bronze', 26, 4, 8, 136, 0, NULL, NULL, 0),
(237, 'trophy_champion_bronze', 26, 5, 8, 137, 0, NULL, NULL, 0),
(238, 'trophy_classic_bronze', 26, 6, 8, 149, 0, NULL, NULL, 0),
(239, 'trophy_duck_silver', 26, 7, 10, 144, 0, NULL, NULL, 0),
(240, 'trophy_globe_silver', 26, 8, 10, 145, 0, NULL, NULL, 0),
(241, 'trophy_fish_silver', 26, 9, 10, 146, 0, NULL, NULL, 0),
(242, 'trophy_duo_silver', 26, 10, 10, 147, 0, NULL, NULL, 0),
(243, 'trophy_champion_silver', 26, 11, 10, 148, 0, NULL, NULL, 0),
(244, 'trophy_classic_silver', 26, 12, 10, 143, 0, NULL, NULL, 0),
(245, 'trophy_duck_gold', 26, 13, 12, 138, 0, NULL, NULL, 0),
(246, 'trophy_globe_gold', 26, 14, 12, 139, 0, NULL, NULL, 0),
(247, 'trophy_fish_gold', 26, 15, 12, 140, 0, NULL, NULL, 0),
(248, 'trophy_duo_gold', 26, 16, 12, 141, 0, NULL, NULL, 0),
(249, 'trophy_champion_gold', 26, 17, 12, 137, 0, NULL, NULL, 0),
(250, 'trophy_classic_gold', 26, 18, 12, 142, 0, NULL, NULL, 0),
(251, 'wallpaper1', 3, 2, 2, 250, 1, NULL, NULL, 0),
(252, 'wallpaper2', 3, 3, 2, 250, 2, NULL, NULL, 0),
(253, 'wallpaper3', 3, 4, 2, 250, 3, NULL, NULL, 0),
(254, 'wallpaper4', 3, 5, 2, 250, 4, NULL, NULL, 0),
(255, 'wallpaper5', 3, 6, 2, 250, 5, NULL, NULL, 0),
(256, 'wallpaper6', 3, 7, 2, 250, 6, NULL, NULL, 0),
(257, 'wallpaper7', 3, 8, 2, 250, 7, NULL, NULL, 0),
(258, 'wallpaper8', 3, 9, 2, 250, 8, NULL, NULL, 0),
(259, 'wallpaper9', 3, 10, 2, 250, 9, NULL, NULL, 0),
(260, 'wallpaper10', 3, 11, 2, 250, 10, NULL, NULL, 0),
(261, 'wallpaper11', 3, 12, 3, 250, 11, NULL, NULL, 0),
(262, 'wallpaper12', 3, 13, 3, 250, 12, NULL, NULL, 0),
(263, 'wallpaper13', 3, 14, 3, 250, 13, NULL, NULL, 0),
(264, 'wallpaper14', 3, 15, 3, 250, 14, NULL, NULL, 0),
(265, 'wallpaper15', 3, 16, 3, 250, 15, NULL, NULL, 0),
(266, 'wallpaper16', 3, 17, 3, 250, 16, NULL, NULL, 0),
(267, 'wallpaper17', 3, 18, 3, 250, 17, NULL, NULL, 0),
(268, 'wallpaper18', 3, 19, 3, 250, 18, NULL, NULL, 0),
(269, 'wallpaper19', 3, 20, 3, 250, 19, NULL, NULL, 0),
(270, 'wallpaper20', 3, 21, 3, 250, 20, NULL, NULL, 0),
(271, 'wallpaper21', 3, 22, 3, 250, 21, NULL, NULL, 0),
(272, 'wallpaper22', 3, 23, 3, 250, 22, NULL, NULL, 0),
(273, 'wallpaper23', 3, 24, 3, 250, 23, NULL, NULL, 0),
(274, 'wallpaper24', 3, 25, 3, 250, 24, NULL, NULL, 0),
(275, 'wallpaper25', 3, 26, 3, 250, 25, NULL, NULL, 0),
(276, 'wallpaper26', 3, 27, 3, 250, 26, NULL, NULL, 0),
(277, 'wallpaper27', 3, 28, 3, 250, 27, NULL, NULL, 0),
(278, 'wallpaper28', 3, 29, 3, 250, 28, NULL, NULL, 0),
(279, 'wallpaper29', 3, 30, 3, 250, 29, NULL, NULL, 0),
(280, 'wallpaper30', 3, 31, 3, 250, 30, NULL, NULL, 0),
(281, 'wallpaper31', 3, 32, 3, 250, 31, NULL, NULL, 0),
(282, 'poster_hammer', 24, 1, 3, 251, 7, NULL, NULL, 0),
(283, 'poster_bflies_blue', 24, 2, 3, 251, 18, NULL, NULL, 0),
(284, 'poster_bflies', 24, 3, 3, 251, 17, NULL, NULL, 0),
(285, 'poster_bear', 24, 4, 3, 251, 4, NULL, NULL, 0),
(286, 'poster_fish', 24, 5, 3, 251, 3, NULL, NULL, 0),
(287, 'poster_carrot', 24, 6, 3, 251, 2, NULL, NULL, 0),
(288, 'poster_lapland', 24, 7, 3, 251, 10, NULL, NULL, 0),
(289, 'poster_himalaya', 24, 8, 3, 251, 15, NULL, NULL, 0),
(290, 'poster_rainforest', 24, 9, 3, 251, 9, NULL, NULL, 0),
(291, 'poster_colors', 24, 10, 3, 251, 8, NULL, NULL, 0),
(292, 'poster_hole', 24, 11, 3, 251, 19, NULL, NULL, 0),
(293, 'poster_bars', 24, 12, 3, 251, 16, NULL, NULL, 0),
(294, 'poster_duck', 24, 13, 3, 251, 5, NULL, NULL, 0),
(295, 'poster_abstract', 24, 14, 3, 251, 6, NULL, NULL, 0),
(296, 'poster_shiva', 24, 15, 3, 251, 32, NULL, NULL, 0),
(297, 'poster_granny', 24, 16, 3, 251, 1, NULL, NULL, 0),
(298, 'poster_cunningfox', 24, 17, 3, 251, 14, NULL, NULL, 0),
(299, 'poster_givingtree', 24, 18, 3, 251, 55, NULL, NULL, 0),
(300, 'poster_panda', 24, 19, 3, 251, 33, NULL, NULL, 0),
(301, 'poster_cert', 24, 20, 3, 251, 11, NULL, NULL, 0),
(302, 'flag_nl', 25, 1, 3, 251, 511, NULL, NULL, 0),
(303, 'flag_us', 25, 2, 3, 251, 502, NULL, NULL, 0),
(304, 'flag_au', 25, 3, 3, 251, 513, NULL, NULL, 0),
(305, 'flag_br', 25, 4, 3, 251, 521, NULL, NULL, 0),
(306, 'flag_ca', 25, 5, 3, 251, 505, NULL, NULL, 0),
(307, 'flag_de', 25, 6, 3, 251, 504, NULL, NULL, 0),
(308, 'flag_en', 25, 7, 3, 251, 516, NULL, NULL, 0),
(309, 'flag_fr', 25, 8, 3, 251, 507, NULL, NULL, 0),
(310, 'flag_ire', 25, 9, 3, 251, 512, NULL, NULL, 0),
(311, 'flag_ind', 25, 10, 3, 251, 523, NULL, NULL, 0),
(312, 'flag_jam', 25, 11, 3, 251, 509, NULL, NULL, 0),
(313, 'flag_jp', 25, 12, 3, 251, 522, NULL, NULL, 0),
(314, 'flag_rainbow', 25, 13, 3, 251, 520, NULL, NULL, 0),
(315, 'flag_scot', 25, 14, 3, 251, 517, NULL, NULL, 0),
(316, 'flag_es', 25, 15, 3, 251, 508, NULL, NULL, 0),
(317, 'flag_eu', 25, 16, 3, 251, 514, NULL, NULL, 0),
(318, 'flag_fi', 25, 17, 3, 251, 506, NULL, NULL, 0),
(319, 'flag_it', 25, 18, 3, 251, 510, NULL, NULL, 0),
(320, 'flag_wales', 25, 19, 3, 251, 518, NULL, NULL, 0),
(321, 'flag_se', 25, 20, 3, 251, 515, NULL, NULL, 0),
(322, 'flag_ch', 25, 21, 3, 251, 503, NULL, NULL, 0),
(323, 'flag_uk', 25, 22, 3, 251, 500, NULL, NULL, 0),
(324, 'flag_pirate', 25, 23, 3, 251, 501, NULL, NULL, 0),
(325, 'hc_sofa', 27, 1, 5, 112, 0, NULL, NULL, 0),
(326, 'hc_mocca', 27, 2, 5, 123, 0, NULL, NULL, 0),
(327, 'hc_dice', 27, 3, 5, 127, 0, NULL, NULL, 0),
(328, 'hc_tub', 27, 4, 5, 129, 0, NULL, NULL, 0),
(329, 'hc_tele', 27, 5, 5, 132, 0, NULL, NULL, 0),
(330, 'hc_tsofa', 27, 6, 5, 128, 0, NULL, NULL, 0),
(331, 'hc_lmp', 27, 7, 5, 150, 0, NULL, NULL, 0),
(332, 'hc_tbl', 27, 8, 5, 151, 0, NULL, NULL, 0),
(333, 'hc_chair', 27, 9, 5, 152, 0, NULL, NULL, 0),
(334, 'hc_dsk', 27, 10, 5, 153, 0, NULL, NULL, 0),
(335, 'hc_trolly', 27, 11, 5, 228, 0, NULL, NULL, 0),
(336, 'hc_strtlmp', 27, 12, 5, 224, 0, NULL, NULL, 0),
(337, 'hc_curtain', 27, 13, 5, 221, 0, NULL, NULL, 0),
(338, 'hc_tv', 27, 14, 5, 214, 0, NULL, NULL, 0),
(339, 'hc_butler', 27, 15, 5, 220, 0, NULL, NULL, 0),
(340, 'hc_bkshlf', 27, 16, 5, 219, 0, NULL, NULL, 0),
(341, 'hc_rontgen', 27, 17, 5, 227, 0, NULL, NULL, 0),
(342, 'hc_fireplace', 27, 18, 5, 223, 0, NULL, NULL, 0),
(343, 'hc_dj', 27, 19, 5, 222, 0, NULL, NULL, 0),
(344, 'hc_wallamp', 27, 20, 5, 246, 0, NULL, NULL, 0),
(345, 'hc_machine', 27, 21, 5, 225, 0, NULL, NULL, 0),
(346, 'deal_hcrollers', 27, 22, 5, 0, 0, 'HC rollers set', 'Five of those uber rollers in one pack!', 1),
(347, 'deal_throne', 27, 23, 5, 0, 0, 'Throne', 'Ten for the price of one!', 1),
(348, 'rare_dragonlamp_4', 28, 1, 5, 170, 0, NULL, NULL, 0),
(349, 'rare_dragonlamp_0', 28, 2, 5, 171, 0, NULL, NULL, 0),
(350, '', 0, 0, 0, 172, 0, NULL, NULL, 0),
(351, 'rare_dragonlamp_2', 28, 4, 5, 173, 0, NULL, NULL, 0),
(352, 'rare_dragonlamp_8', 28, 5, 5, 174, 0, NULL, NULL, 0),
(353, 'rare_dragonlamp_9', 28, 6, 5, 175, 0, NULL, NULL, 0),
(354, 'rare_dragonlamp_7', 28, 7, 5, 176, 0, NULL, NULL, 0),
(355, 'rare_dragonlamp_6', 28, 8, 5, 177, 0, NULL, NULL, 0),
(356, 'rare_dragonlamp_1', 28, 9, 5, 178, 0, NULL, NULL, 0),
(357, 'rare_dragonlamp_3', 28, 10, 5, 179, 0, NULL, NULL, 0),
(358, 'scifidoor*1', 29, 0, 5, 270, 0, NULL, NULL, 0),
(359, 'scifidoor*10', 29, 0, 5, 261, 0, NULL, NULL, 0),
(360, 'scifidoor*2', 29, 0, 5, 269, 0, NULL, NULL, 0),
(361, 'scifidoor*3', 29, 0, 5, 268, 0, NULL, NULL, 0),
(362, 'scifidoor*4', 29, 0, 5, 267, 0, NULL, NULL, 0),
(363, 'scifidoor*5', 29, 0, 5, 266, 0, NULL, NULL, 0),
(364, 'scifidoor*6', 29, 0, 5, 265, 0, NULL, NULL, 0),
(365, 'scifidoor*7', 29, 0, 5, 264, 0, NULL, NULL, 0),
(366, 'scifidoor*8', 29, 0, 5, 263, 0, NULL, NULL, 0),
(367, 'scifidoor*9', 29, 0, 5, 262, 0, NULL, NULL, 0),
(368, 'rare_parasol*0', 30, 0, 5, 260, 0, NULL, NULL, 0),
(369, 'rare_parasol*1', 30, 0, 5, 257, 0, NULL, NULL, 0),
(370, 'rare_parasol*2', 30, 0, 5, 258, 0, NULL, NULL, 0),
(371, 'rare_parasol*3', 30, 0, 5, 259, 0, NULL, NULL, 0),
(372, 'wooden_screen*0', 31, 0, 5, 317, 0, NULL, NULL, 0),
(373, 'wooden_screen*1', 31, 0, 5, 314, 0, NULL, NULL, 0),
(374, 'wooden_screen*2', 31, 0, 5, 315, 0, NULL, NULL, 0),
(375, 'wooden_screen*3', 31, 0, 5, 323, 0, NULL, NULL, 0),
(376, 'wooden_screen*4', 31, 0, 5, 321, 0, NULL, NULL, 0),
(377, 'wooden_screen*5', 31, 0, 5, 319, 0, NULL, NULL, 0),
(378, 'wooden_screen*6', 31, 0, 5, 322, 0, NULL, NULL, 0),
(379, 'wooden_screen*7', 31, 0, 5, 316, 0, NULL, NULL, 0),
(380, 'wooden_screen*8', 31, 0, 5, 318, 0, NULL, NULL, 0),
(381, 'wooden_screen*9', 31, 0, 5, 320, 0, NULL, NULL, 0),
(382, 'marquee*1', 32, 0, 5, 304, 0, NULL, NULL, 0),
(383, 'marquee*2', 32, 0, 5, 305, 0, NULL, NULL, 0),
(384, 'marquee*3', 32, 0, 5, 313, 0, NULL, NULL, 0),
(385, 'marquee*4', 32, 0, 5, 311, 0, NULL, NULL, 0),
(386, 'marquee*5', 32, 0, 5, 310, 0, NULL, NULL, 0),
(387, 'marquee*6', 32, 0, 5, 312, 0, NULL, NULL, 0),
(388, 'marquee*7', 32, 0, 5, 306, 0, NULL, NULL, 0),
(389, 'marquee*8', 32, 0, 5, 308, 0, NULL, NULL, 0),
(390, 'marquee*9', 32, 0, 5, 309, 0, NULL, NULL, 0),
(391, 'marquee*a', 32, 0, 5, 307, 0, NULL, NULL, 0),
(392, 'pillow*5', 33, 0, 5, 294, 0, NULL, NULL, 0),
(393, 'pillow*8', 33, 0, 5, 295, 0, NULL, NULL, 0),
(394, 'pillow*0', 33, 0, 5, 296, 0, NULL, NULL, 0),
(395, 'pillow*1', 33, 0, 5, 297, 0, NULL, NULL, 0),
(396, 'pillow*2', 33, 0, 5, 298, 0, NULL, NULL, 0),
(397, 'pillow*7', 33, 0, 5, 299, 0, NULL, NULL, 0),
(398, 'pillow*9', 33, 0, 5, 300, 0, NULL, NULL, 0),
(399, 'pillow*4', 33, 0, 5, 301, 0, NULL, NULL, 0),
(400, 'pillow*6', 33, 0, 5, 302, 0, NULL, NULL, 0),
(401, 'pillow*3', 33, 0, 5, 303, 0, NULL, NULL, 0),
(402, 'rare_icecream*0', 34, 0, 5, 331, 0, NULL, NULL, 0),
(403, 'rare_icecream*1', 34, 0, 5, 324, 0, NULL, NULL, 0),
(404, 'rare_icecream*2', 34, 0, 5, 327, 0, NULL, NULL, 0),
(405, 'rare_icecream*3', 34, 0, 5, 330, 0, NULL, NULL, 0),
(406, 'rare_icecream*4', 34, 0, 5, 332, 0, NULL, NULL, 0),
(407, 'rare_icecream*5', 34, 0, 5, 333, 0, NULL, NULL, 0),
(408, 'rare_icecream*6', 34, 0, 5, 328, 0, NULL, NULL, 0),
(409, 'rare_icecream*7', 34, 0, 5, 325, 0, NULL, NULL, 0),
(410, 'rare_icecream*8', 34, 0, 5, 326, 0, NULL, NULL, 0),
(411, 'rare_icecream*9', 34, 0, 5, 329, 0, NULL, NULL, 0),
(412, 'scifirocket*0', 35, 0, 5, 293, 0, NULL, NULL, 0),
(413, 'scifirocket*1', 35, 0, 5, 292, 0, NULL, NULL, 0),
(414, 'scifirocket*2', 35, 0, 5, 291, 0, NULL, NULL, 0),
(415, 'scifirocket*3', 35, 0, 5, 290, 0, NULL, NULL, 0),
(416, 'scifirocket*4', 35, 0, 5, 289, 0, NULL, NULL, 0),
(417, 'scifirocket*5', 35, 0, 5, 288, 0, NULL, NULL, 0),
(418, 'scifirocket*6', 35, 0, 5, 287, 0, NULL, NULL, 0),
(419, 'scifirocket*7', 35, 0, 5, 286, 0, NULL, NULL, 0),
(420, 'scifirocket*8', 35, 0, 5, 285, 0, NULL, NULL, 0),
(421, 'scifirocket*9', 35, 0, 5, 284, 0, NULL, NULL, 0),
(422, 'scifiport*0', 36, 0, 5, 274, 0, NULL, NULL, 0),
(423, 'scifiport*1', 36, 0, 5, 283, 0, NULL, NULL, 0),
(424, 'scifiport*2', 36, 0, 5, 282, 0, NULL, NULL, 0),
(425, 'scifiport*3', 36, 0, 5, 281, 0, NULL, NULL, 0),
(426, 'scifiport*4', 36, 0, 5, 280, 0, NULL, NULL, 0),
(427, 'scifiport*5', 36, 0, 5, 279, 0, NULL, NULL, 0),
(428, 'scifiport*6', 36, 0, 5, 278, 0, NULL, NULL, 0),
(429, 'scifiport*7', 36, 0, 5, 277, 0, NULL, NULL, 0),
(430, 'scifiport*8', 36, 0, 5, 276, 0, NULL, NULL, 0),
(431, 'scifiport*9', 36, 0, 5, 275, 0, NULL, NULL, 0),
(432, 'rare_beehive_bulb', 37, 53, 5, 271, 0, NULL, NULL, 0),
(433, 'rare_beehive_bulb*1', 37, 53, 5, 272, 0, NULL, NULL, 0),
(434, 'rare_beehive_bulb*2', 37, 53, 5, 273, 0, NULL, NULL, 0),
(435, 'rare_fountain', 38, 54, 5, 334, 0, NULL, NULL, 0),
(436, 'rare_fountain*1', 38, 54, 5, 335, 0, NULL, NULL, 0),
(437, 'rare_fountain*2', 38, 54, 5, 336, 0, NULL, NULL, 0),
(438, 'rare_fountain*3', 38, 54, 5, 337, 0, NULL, NULL, 0),
(439, 'rare_elephant_statue', 39, 55, 5, 338, 0, NULL, NULL, 0),
(440, 'rare_elephant_statue*1', 39, 0, 5, 339, 0, NULL, NULL, 0),
(441, 'rare_elephant_statue*2', 39, 0, 5, 340, 0, NULL, NULL, 0),
(442, 'rare_fan*0', 40, 0, 5, 345, 0, NULL, NULL, 0),
(443, 'rare_fan*1', 40, 0, 5, 348, 0, NULL, NULL, 0),
(444, 'rare_fan*2', 40, 0, 5, 350, 0, NULL, NULL, 0),
(445, 'rare_fan*3', 40, 0, 5, 344, 0, NULL, NULL, 0),
(446, 'rare_fan*4', 40, 0, 5, 346, 0, NULL, NULL, 0),
(447, 'rare_fan*5', 40, 0, 5, 347, 0, NULL, NULL, 0),
(448, 'rare_fan*6', 40, 0, 5, 342, 0, NULL, NULL, 0),
(449, 'rare_fan*7', 40, 0, 5, 341, 0, NULL, NULL, 0),
(450, 'rare_fan*8', 40, 0, 5, 349, 0, NULL, NULL, 0),
(451, 'rare_fan*9', 40, 0, 5, 343, 0, NULL, NULL, 0),
(452, 'habbowheel_fan', 19, 0, 8, 351, 0, NULL, NULL, 0),
(453, 'roomdimmer', 19, 0, 12, 352, 0, NULL, NULL, 0),
(454, 'jukebox', 11, 0, 3, 353, 0, NULL, NULL, 0),
(455, 'jukebox_ptv', 11, 0, 8, 354, 0, NULL, NULL, 0),
(456, 'carpet_soft_tut', 23, 0, 1, 355, 0, NULL, NULL, 0),
(458, 'soundset10', 11, 9, 3, 357, 0, '', '', 0),
(459, 'soundset11', 11, 9, 3, 361, 0, '', '', 0),
(460, 'soundset12', 11, 9, 3, 362, 0, '', '', 0),
(461, 'soundset13', 11, 9, 3, 363, 0, '', '', 0),
(462, 'soundset14', 11, 9, 3, 364, 0, '', '', 0),
(463, 'soundset15', 11, 9, 3, 365, 0, '', '', 0),
(464, 'soundset16', 11, 9, 3, 366, 0, '', '', 0),
(465, 'soundset17', 11, 9, 3, 367, 0, '', '', 0),
(466, 'soundset18', 11, 9, 3, 368, 0, '', '', 0),
(467, 'soundset19', 11, 9, 3, 369, 0, '', '', 0),
(468, 'soundset20', 11, 9, 3, 370, 0, '', '', 0),
(469, 'soundset21', 11, 9, 3, 371, 0, '', '', 0),
(470, 'soundset21', 11, 9, 3, 372, 0, '', '', 0),
(471, 'soundset23', 11, 9, 3, 373, 0, '', '', 0),
(472, 'soundset24', 11, 9, 3, 374, 0, '', '', 0),
(473, 'soundset25', 11, 9, 3, 375, 0, '', '', 0),
(474, 'soundset26', 11, 9, 3, 376, 0, '', '', 0),
(475, 'soundset27', 11, 9, 3, 377, 0, '', '', 0),
(476, 'soundset28', 11, 9, 3, 378, 0, '', '', 0),
(477, 'soundset29', 11, 9, 3, 379, 0, '', '', 0),
(478, 'soundset30', 11, 9, 3, 380, 0, '', '', 0),
(479, 'soundset31', 11, 9, 3, 381, 0, '', '', 0),
(480, 'soundset32', 11, 9, 3, 382, 0, '', '', 0),
(481, 'soundset33', 11, 9, 3, 383, 0, '', '', 0),
(482, 'soundset34', 11, 9, 3, 384, 0, '', '', 0),
(483, 'soundset35', 11, 9, 3, 385, 0, '', '', 0),
(484, 'soundset36', 11, 9, 3, 386, 0, '', '', 0),
(485, 'soundset37', 11, 9, 3, 387, 0, '', '', 0),
(486, 'soundset38', 11, 9, 3, 388, 0, '', '', 0),
(487, 'soundset39', 11, 9, 3, 389, 0, '', '', 0),
(488, 'soundset40', 11, 9, 3, 390, 0, '', '', 0),
(489, 'soundset41', 11, 9, 3, 391, 0, '', '', 0),
(490, 'soundset42', 11, 9, 3, 392, 0, '', '', 0),
(491, 'soundset43', 11, 9, 3, 393, 0, '', '', 0),
(492, 'soundset44', 11, 9, 3, 394, 0, '', '', 0),
(493, 'soundset45', 11, 9, 3, 395, 0, '', '', 0),
(494, 'soundset46', 11, 9, 3, 396, 0, '', '', 0),
(495, 'soundset47', 11, 9, 3, 397, 0, '', '', 0),
(496, 'soundset48', 11, 9, 3, 398, 0, '', '', 0),
(497, 'soundset49', 11, 9, 3, 399, 0, '', '', 0),
(498, 'soundset50', 11, 9, 3, 400, 0, '', '', 0),
(499, 'soundset51', 11, 9, 3, 401, 0, '', '', 0),
(500, 'soundset52', 11, 9, 3, 402, 0, '', '', 0),
(501, 'soundset53', 11, 9, 3, 403, 0, '', '', 0),
(502, 'soundset54', 11, 9, 3, 404, 0, '', '', 0),
(503, 'soundset55', 11, 9, 3, 405, 0, '', '', 0),
(504, 'soundset56', 11, 9, 3, 406, 0, '', '', 0),
(505, 'soundset57', 11, 9, 3, 407, 0, '', '', 0),
(506, 'soundset58', 11, 9, 3, 408, 0, '', '', 0),
(507, 'soundset59', 11, 9, 3, 409, 0, '', '', 0),
(508, 'soundset60', 11, 9, 3, 410, 0, '', '', 0),
(509, 'soundset61', 11, 9, 3, 411, 0, '', '', 0),
(510, 'soundset62', 11, 9, 3, 412, 0, '', '', 0),
(511, 'soundset63', 11, 9, 3, 413, 0, '', '', 0),
(512, 'soundset64', 11, 9, 3, 414, 0, '', '', 0),
(513, 'camera', 41, 0, 10, 421, 0, NULL, NULL, 0),
(514, 'film', 41, 1, 6, 423, 0, NULL, NULL, 0);

-- --------------------------------------------------------

--
-- Table structure for table `catalogue_packages`
--

CREATE TABLE `catalogue_packages` (
  `salecode` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `definition_id` int(11) DEFAULT NULL,
  `special_sprite_id` int(11) DEFAULT NULL,
  `amount` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `catalogue_packages`
--

INSERT INTO `catalogue_packages` (`salecode`, `definition_id`, `special_sprite_id`, `amount`) VALUES
('deal_rollers_red_5', 184, 0, 5),
('deal_rollers_red_3', 184, 0, 3),
('deal_rollers_blue_5', 180, 0, 5),
('deal_rollers_blue_3', 180, 0, 3),
('deal_rollers_green_5', 181, 0, 5),
('deal_rollers_green_3', 181, 0, 3),
('deal_rollers_navy_5', 182, 0, 5),
('deal_rollers_navy_3', 182, 0, 3),
('deal_rollers_purple_5', 183, 0, 5),
('deal_rollers_purple_3', 183, 0, 3),
('deal_dogfood', 155, 0, 6),
('deal_catfood', 156, 0, 6),
('deal_crocfood', 236, 0, 6),
('deal_cabbage', 157, 0, 6),
('deal_soundmachine1', 232, 0, 1),
('deal_soundmachine1', 239, 0, 1),
('deal_hcrollers', 226, 0, 5),
('deal_throne', 107, 0, 10);

-- --------------------------------------------------------

--
-- Table structure for table `catalogue_pages`
--

CREATE TABLE `catalogue_pages` (
  `id` int(11) NOT NULL,
  `order_id` int(11) DEFAULT NULL,
  `min_role` int(11) DEFAULT NULL,
  `index_visible` tinyint(1) NOT NULL DEFAULT 1,
  `name_index` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `link_list` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `layout` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `image_headline` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `image_teasers` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `body` text COLLATE utf8mb4_unicode_ci DEFAULT '',
  `label_pick` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `label_extra_s` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `label_extra_t` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `catalogue_pages`
--

INSERT INTO `catalogue_pages` (`id`, `order_id`, `min_role`, `index_visible`, `name_index`, `link_list`, `name`, `layout`, `image_headline`, `image_teasers`, `body`, `label_pick`, `label_extra_s`, `label_extra_t`) VALUES
(1, 1, 1, 1, 'Frontpage', '', 'Frontpage', 'ctlg_frontpage2', 'catal_fp_header', 'catal_fp_pic4,catal_fp_pic5,', 'Welcome to the Hotel Catalogue. It\'s packed full of fab things for your room - there\'s something for everyone! Browse the ranges by clicking the tabs on the right.<br><br>Some ranges are seasonal, so check back regularly for new items.<br><br>We regularly', NULL, 'Home sweet home!', '1:You need Credits to buy Furni for your room, click the Purse at the bottom of your screen for more information about Credits.'),
(2, 2, 1, 1, 'Rare', '', 'Rare', 'ctlg_productpage1', 'catalog_rares_headline1', '', 'Okay this thing is fucking epic!\rEnjoy it while it lasts!', NULL, NULL, NULL),
(3, 3, 1, 1, 'Spaces', '', 'Spaces', 'ctlg_spaces', 'catalog_spaces_headline1', '', 'Floors, wallpapers, landscapes - get a groovy combination to your room. Use our virtual room preview below to test out the combinations before you buy. Select the design and color you like and click Buy.', NULL, NULL, '1:Wall\r\n2:Floor\r\n3:Pattern\r\n4:Colour\r\n5:Pattern\r\n6:Colour\r\n7:Preview'),
(4, 4, 1, 1, 'Bank', '', 'Exchange', 'ctlg_layout2', 'catalog_bank_headline1', 'catalog_bank_teaser,', 'The Habbo Exchange is where you can convert your Habbo Credits into a tradable currency. You can use this tradable currency to exchange Habbo Credits for Furni!', 'Click on the item you want for more information', 'EPIC YAY!', NULL),
(5, 5, 1, 1, 'Rollers', '', 'Rollers', 'ctlg_layout2', 'catalog_roller_headline1', '', 'Move your imagination, while you move your Habbo!  Perfect for mazes, games, for keeping your queue moving or making your pet go round in circles for hours.  Available in multi-packs ? the more you buy the cheaper the Roller! Pink Rollers out now!', 'Click on a Roller to see more information!', 'You can fit 30 Rollers in a user flat!', NULL),
(6, 6, 1, 1, 'Teleporters', '', 'Teleporters', 'ctlg_productpage3', 'catalog_doors_headline1', 'catalog_door_a,catalog_door_c,catalog_door_b,', 'Beam your user from one room to another with one of our cunningly disguised, space age teleports. Now you can link any two rooms together! Teleports are sold in pairs, so if you trade for them, check you\'re getting a linked pair.', 'Click on the item you want for more information', 'Beam!', NULL),
(7, 7, 1, 1, 'Pets', '', 'Pets', 'ctlg_pets', 'catalog_pet_headline1', 'catalog_pets_teaser1,', 'Fluff and whiskers, meows and woofs! You\'\'re about to enter the world of small creatures with furry features. Find a new friend from our ever-changing selection. From faithful servants to playful playmates - here\'s where you\'\'ll find them all.', 'Find your own pet!', NULL, '1:Give name:'),
(8, 8, 1, 1, 'Petstuff', '', 'Pet accessories', 'ctlg_layout2', 'catalog_pet_headline2', 'ctlg_pet_teaser1,', 'You\'\'ll need to take care of your pet to keep it happy and healthy. This section of the Catalogue has EVERYTHING you\'ll need to satisfy your pet\'s needs.', 'Click on the item you want for more information', 'You\'ll have to share it!', NULL),
(9, 9, 1, 1, 'Area', '', 'Area', 'Area', 'catalog_area_headline1', 'catalog_area_teaser1,', 'Introducing the Area Collection...  Clean, chunky lines set this collection apart as a preserve of the down-to-earth person. It\'s beautiful in its simplicity, and welcoming to everyone.', 'Click on the item you want for more information', '2: Beautiful in it\'s simplicity!', NULL),
(10, 10, 1, 1, 'Gothic', '', 'Gothic', 'ctlg_layout2', 'catalog_gothic_headline1', 'catalog_gothic_teaser1,', 'The Gothic section is full of medieval looking items. Create your own Gothic castle!', 'Click on the item you want for more information', NULL, NULL),
(11, 11, 1, 1, 'Soundmachines', '', 'Trax', 'ctlg_soundmachine', 'catalog_djshop_headline1', 'catalog_djshop_teaser1,', 'Bring sound to your room! Purchase a sound machine plus some sample packs and create your own songs to play in your flat!<br>Moar soundsets coming soon!', 'Click on the item you want for more information', NULL, NULL),
(12, 12, 1, 1, 'Candy', '', 'Candy', 'ctlg_layout2', 'catalog_candy_headline1', 'catalog_candy_teaser1,', 'Candy combines the cool, clean lines of the Mode collection with a softer, more soothing style. It\'\'s urban sharpness with a hint of the feminine.', 'Click on the item you want for more information', '2: WTF SCREW YOU LOL', NULL),
(13, 13, 1, 1, 'Asian', '', 'Asian', 'ctlg_layout2', 'catalog_asian_headline1', 'catalog_asian_teaser1,', 'Introducing the Asian collection... These handcrafted items are the result of years of child slavery, some mixture of Ying and Yang and a mass-shipping from China. These authentic items fit in every oriental themed user flat. Made in China: fo\' real nigga', 'Click on the item you want for more information', NULL, NULL),
(14, 14, 1, 1, 'Iced', '', 'Iced', 'ctlg_layout2', 'catalog_iced_headline1', 'catalog_iced_teaser1,', 'Introducing the Iced Collection...  For the person who needs no introduction. It\'s so chic, it says everything and nothing. It\'s a blank canvas, let your imagination to run wild!', 'Click on the item you want for more information', '2: These chairs are so filthy', NULL),
(15, 15, 1, 1, 'Lodge', '', 'Lodge', 'ctlg_layout2', 'catalog_lodge_headline1', 'catalog_lodge_teaser1,', 'Introducing the Lodge Collection...  Do you appreciate the beauty of wood?  For that ski lodge effect, or to match that open fire... Lodge is the Furni of choice for people with that no frills approach to decorating.', 'Click on the item you want for more information', '2: I luv wood!', NULL),
(16, 16, 1, 1, 'Plasto', '', 'Plasto', 'ctlg_plasto', 'catalog_plasto_headline1', '', 'Introducing The Plasto Collection...  Can you feel that 1970s vibe?  Decorate with Plasto and add some colour to your life. Choose a colour that reflect your mood, or just pick your favourite shade.', 'Select an item and a colour and buy!', 'New colors!', '1:Choose an item\r\n2:Select the color\r\n3:Preview'),
(17, 17, 1, 1, 'Pura', '', 'Pura', 'ctlg_layout2', 'catalog_pura_headline1', 'catalog_pura_teaser1,', 'Introducing the Pura Collection...  This collection breathes fresh, clean air and cool tranquillity. Use it to create a special haven away from the hullabaloo of life outside the Hotel.', 'Click on the item you want for more information', NULL, NULL),
(18, 18, 1, 1, 'Mode', '', 'Mode', 'ctlg_layout2', 'catalog_mode_headline1', 'catalog_mode_teaser1,', 'Introducing the Mode Collection...  Steely grey functionality combined with sleek designer upholstery. The person that chooses this furniture is a cool urban cat - streetwise, sassy and so slightly untouchable.', 'Click on the item you want for more information', '2: So shiny and new...', NULL),
(19, 19, 1, 1, 'Accessories', '', 'Accessories', 'ctlg_layout2', 'catalog_extra_headline1', 'catalog_extra_teaser1,', 'Is your room missing something?  Well, now you can add the finishing touches that express your true personality. And don\'t forget, like everything else, these accessories can be moved about to suit your mood.', 'Click on the item you want for more information', '2: I herd u liek mudkips?', NULL),
(20, 20, 1, 1, 'Bathroom', '', 'Bathroom', 'ctlg_layout2', 'catalog_bath_headline1', 'catalog_bath_teaser1,', 'Introducing the Bathroom Collection...  Have some fun with the cheerful bathroom collection. Give yourself and your guests somewhere to freshen up - vital if you want to avoid nasty niffs. Put your loo in a corner though...', 'Click on the item you want for more information', NULL, NULL),
(21, 21, 1, 1, 'Plants', '', 'Plants', 'ctlg_layout2', 'catalog_plants_headline1', 'catalog_plants_teaser1,', 'Introducing the Plant Collection...  Every room needs a plant! Not only do they bring a bit of the outside inside, they also enhance the air quality! Do we give a fuck? Up to you!', 'Click on the item you want for more information', NULL, NULL),
(22, 22, 1, 1, 'Sports', '', 'Sport', 'ctlg_layout2', 'catalog_sports_headline1', 'catalog_sports_teaser1,', 'For the sporty people, here is the Sports section! Create your own hockey stadium!', 'Click on the item you want for more information', '2:Yay!', NULL),
(23, 23, 1, 1, 'Rugs', '', 'Rugs', 'ctlg_layout2', 'catalog_rugs_headline1', 'catalog_rugs_teaser1,', 'We have rugs for all occasions. All rugs are non-slip and washable.', 'Click on the item you want for more information', '2:We have rugs for ANY room!', NULL),
(24, 24, 1, 1, 'Gallery', '', 'Gallery', 'ctlg_layout2', 'catalog_gallery_headline1', 'catalog_gallery_teaser1,', 'Adorn your walls with wondrous works of art, posters, plaques and wall hangings. We have items to suit all tastes, from kitsch to cool, traditional to modern.', 'Click on the item you want for more information', '2: Brighten up your walls!', NULL),
(25, 25, 1, 1, 'Flags', '', 'Flags', 'ctlg_layout2', 'catalog_flags_headline1', 'catalog_flags_teaser1,', 'If you\'re feeling patriotic, get a flag to prove it. Our finest cloth flags will brighten up the dullest walls.', 'Click on the item you want for more information', '2:Hail Nillus!', NULL),
(26, 26, 1, 1, 'Trophies', '', 'Trophies', 'ctlg_trophies', 'catalog_trophies_headline1', '', 'Reward your friends, or yourself with one of our fabulous glittering array of bronze, silver and gold trophies.<br><br>First choose the trophy model (click on the arrows to see all the different styles) and then the metal (click on the seal below the trop', NULL, NULL, '1:Type your inscription CAREFULLY, it\'s permanent!'),
(27, 40, 5, 1, 'staffHC', '', 'Club Shop', 'ctlg_layout2', 'catalog_club_headline1', 'catalog_hc_teaser,', 'Welcome to the Club Shop! All \'Habbo Club membership gifts\' are available here, use them wisely you greedy cunt! We have sofas, butlers and all the happy stuff.', 'Click on the item you want for more information', NULL, NULL),
(28, 41, 5, 1, 'Dragons', '', 'Dragons', 'ctlg_layout2', 'catalog_rares_headline1', '', 'The Dragon page contains all of the Dragon Lamps.', 'Click on the item you want for more information', NULL, NULL),
(29, 43, 5, 1, 'Sci-fi Doors', '', 'Sci-fi Doors', 'ctlg_layout2', 'catalog_rares_headline1', '', 'Yet another rares page.', 'Click on the item you want for more information', NULL, NULL),
(30, 43, 5, 1, 'Parasols', '', 'Parasols', 'ctlg_layout2', 'catalog_rares_headline1', '', 'Yet another rares page.', 'Click on the item you want for more information', NULL, NULL),
(31, 43, 5, 1, 'Screens', '', 'Screens', 'ctlg_layout2', 'catalog_rares_headline1', '', 'Yet another rares page.', 'Click on the item you want for more information', NULL, NULL),
(32, 32, 5, 1, 'Marquee', '', 'Marquee', 'ctlg_layout2', 'catalog_rares_headline1', '', 'Yet another rares page.', 'Click on the item you want for more information', NULL, NULL),
(33, 33, 5, 1, 'Pillows', '', 'Pillows', 'ctlg_layout2', 'catalog_rares_headline1', '', 'Yet another rares page.', 'Click on the item you want for more information', NULL, NULL),
(34, 34, 5, 1, 'Icecream', '', 'Icecream', 'ctlg_layout2', 'catalog_rares_headline1', '', 'Yet another rares page.', 'Click on the item you want for more information', NULL, NULL),
(35, 35, 5, 1, 'Smoke machines', '', 'Smoke machines', 'ctlg_layout2', 'catalog_rares_headline1', '', 'Yet another rares page.', 'Click on the item you want for more information', NULL, NULL),
(36, 36, 5, 1, 'Laser Ports', '', 'Laser Ports', 'ctlg_layout2', 'catalog_rares_headline1', '', 'Yet another rares page.', 'Click on the item you want for more information', NULL, NULL),
(37, 37, 5, 1, 'Amber Lamp', '', 'Amber Lamp', 'ctlg_layout2', 'catalog_rares_headline1', '', 'Yet another rares page.', 'Click on the item you want for more information', NULL, NULL),
(38, 38, 5, 1, 'Fountains', '', 'Fountains', 'ctlg_layout2', 'catalog_rares_headline1', '', 'Yet another rares page.', 'Click on the item you want for more information', NULL, NULL),
(39, 39, 5, 1, 'Elephants', '', 'Elephants', 'ctlg_layout2', 'catalog_rares_headline1', '', 'Yet another rares page.', 'Click on the item you want for more information', NULL, NULL),
(40, 40, 5, 1, 'Fans', '', 'Fans', 'ctlg_layout2', 'catalog_rares_headline1', '', 'Yet another rares page.', 'Click on the item you want for more information', NULL, NULL),
(41, 3, 1, 1, 'Camera', 'Camera2', 'Camera', 'ctlg_camera1', 'catalog_camera_headline1', 'campic_cam,campic_film,', 'With your Camera you can take pictures of just about anything in the hotel - your friend on the loo (hehe), your best dive in the Lido, or your room when you\'ve got it just right!<br><br>A camera costs 10 Credits (two free photos included).', NULL, NULL, '1:When you\'ve used your free photos, you\'ll need to buy more. Each roll of film takes five photos. Your Camera will show how much film you have left and loads the next roll automatically.<br><br>Each Film (5 photos) costs:'),
(42, 3, 1, 0, 'Camera2', '', 'Camera', 'ctlg_camera2', 'catalog_camera_headline1', 'campic_help,', 'CAMERA FUNCTIONS<br><br>1. Press this button to take a photo.<br>2. Photo cancel - for when you\'ve chopped off your friend\'s head!<br>3. Zoom in and out.<br>4. Photo counter - shows how much film you have left<br>5. Caption Box - write your caption before saving the photo.<br>6. Save - this moves the photo to your giant.<br>You can give photos to your friends, or put them on the wall like posters.', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `items`
--

CREATE TABLE `items` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `room_id` int(11) DEFAULT 0,
  `definition_id` int(11) NOT NULL,
  `x` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT '0',
  `y` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT '0',
  `z` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT '0',
  `wall_position` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `rotation` int(11) DEFAULT 0,
  `custom_data` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `items_definitions`
--

CREATE TABLE `items_definitions` (
  `id` int(11) NOT NULL,
  `sprite` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `colour` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `length` int(11) DEFAULT NULL,
  `width` int(11) DEFAULT NULL,
  `top_height` double DEFAULT NULL,
  `behaviour` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `items_definitions`
--

INSERT INTO `items_definitions` (`id`, `sprite`, `colour`, `length`, `width`, `top_height`, `behaviour`) VALUES
(1, 'shelves_norja', '#ffffff,#F7EBBC', 1, 1, 2, 'solid'),
(2, 'shelves_polyfon', '0,0,0', 2, 1, 1, 'solid'),
(3, 'shelves_silo', '0,0,0', 2, 1, 0, 'solid'),
(4, 'table_polyfon_small', '0,0,0', 2, 2, 1, 'solid,can_stack_on_top'),
(5, 'chair_polyfon', '0,0,0', 1, 1, 1, 'can_sit_on_top'),
(6, 'table_silo_med', '#ffffff,#ABD0D2', 2, 2, 1, 'solid,can_stack_on_top'),
(7, 'stand_polyfon_z', '0,0,0', 1, 1, 0.5, 'solid,can_stack_on_top'),
(8, 'chair_silo', '#ffffff,#ffffff,#ABD0D2,#ABD0D2', 1, 1, 1, 'can_sit_on_top'),
(9, 'sofa_silo', '#ffffff,#ffffff,#ffffff,#ffffff,#ABD0D2,#ABD0D2,#ABD0D2,#ABD0D2', 2, 1, 1, 'can_sit_on_top'),
(10, 'couch_norja', '#ffffff,#ffffff,#ffffff,#ffffff,#F7EBBC,#F7EBBC,#F7EBBC,#F7EBBC', 2, 1, 1, 'can_sit_on_top'),
(11, 'chair_norja', '#ffffff,#ffffff,#F7EBBC,#F7EBBC', 1, 1, 1, 'can_sit_on_top'),
(12, 'table_polyfon_med', '0,0,0', 2, 2, 1, 'solid,can_stack_on_top'),
(13, 'doormat_love', '0,0,0', 1, 1, 0, 'can_stand_on_top,can_stack_on_top'),
(14, 'doormat_plain', '0,0,0', 1, 1, 0.2, 'can_stand_on_top,can_stack_on_top'),
(15, 'sofachair_polyfon', '#ffffff,#ffffff,#ABD0D2,#ABD0D2', 1, 1, 1, 'can_sit_on_top'),
(16, 'sofachair_silo', '#ffffff,#ffffff,#ABD0D2,#ABD0D2', 1, 1, 1, 'can_sit_on_top'),
(17, 'bed_polyfon', '#ffffff,#ffffff,#ABD0D2,#ABD0D2', 2, 3, 1, 'can_lay_on_top'),
(18, 'bed_polyfon_one', '#ffffff,#ffffff,#ffffff,#ABD0D2,#ABD0D2', 1, 3, 1, 'can_lay_on_top'),
(19, 'bed_silo_one', '0,0,0', 1, 3, 1.7, 'can_lay_on_top'),
(20, 'bed_silo_two', '0,0,0', 2, 3, 1.7, 'can_lay_on_top'),
(21, 'table_silo_small', '#ffffff,#ABD0D2', 1, 1, 1, 'solid,can_stack_on_top'),
(22, 'bed_armas_two', '0,0,0', 2, 3, 1.7, 'can_lay_on_top'),
(23, 'shelves_armas', '0,0,0', 2, 1, 2, 'solid'),
(24, 'bench_armas', '0,0,0', 2, 1, 1, 'can_sit_on_top'),
(25, 'table_armas', '0,0,0', 2, 2, 1, 'solid,can_stack_on_top'),
(26, 'small_table_armas', '0,0,0', 1, 1, 1, 'solid,can_stack_on_top'),
(27, 'small_chair_armas', '0,0,0', 1, 1, 1, 'can_sit_on_top'),
(28, 'fireplace_armas', '0,0,0', 2, 1, 2, 'solid,custom_data_numeric_on_off'),
(29, 'lamp_armas', '0,0,0', 1, 1, 2, 'solid,custom_data_numeric_on_off'),
(30, 'bed_armas_one', '0,0,0', 1, 3, 1.7, 'can_lay_on_top'),
(31, 'carpet_standard', '0,0,0', 3, 5, 0, 'can_stand_on_top,can_stack_on_top'),
(32, 'carpet_armas', '0,0,0', 2, 4, 0, 'can_stand_on_top,can_stack_on_top'),
(33, 'carpet_polar', '#ffffff,#ffffff,#ffffff', 2, 3, 0, 'can_stand_on_top,can_stack_on_top'),
(34, 'fireplace_polyfon', '0,0,0', 2, 1, 2, 'solid,custom_data_numeric_on_off'),
(35, 'carpet_standard*1', '#ff1f08', 3, 5, 0, 'can_stand_on_top,can_stack_on_top'),
(36, 'doormat_plain*1', '#ff1f08', 1, 1, 0.2, 'can_stand_on_top,can_stack_on_top'),
(37, 'carpet_standard*2', '#99DCCC', 3, 5, 0, 'can_stand_on_top,can_stack_on_top'),
(38, 'carpet_standard*3', '#ffee00', 3, 5, 0, 'can_stand_on_top,can_stack_on_top'),
(39, 'carpet_standard*4', '#ccddff', 3, 5, 0, 'can_stand_on_top,can_stack_on_top'),
(40, 'doormat_plain*6', '#777777', 1, 1, 0.2, 'can_stand_on_top,can_stack_on_top'),
(41, 'carpet_standard*5', '#ddccff', 3, 5, 0, 'can_stand_on_top,can_stack_on_top'),
(42, 'carpet_standard*6', '#777777', 3, 5, 0, 'can_stand_on_top,can_stack_on_top'),
(43, 'pizza', '', 1, 1, 0.1, 'solid,can_stack_on_top'),
(44, 'drinks', '', 1, 1, 0.2, 'solid'),
(45, 'bar_polyfon', '', 1, 1, 1, 'solid,requires_touching_for_interaction,custom_data_true_false'),
(46, 'plant_cruddy', '', 1, 1, 1, 'solid'),
(47, 'bottle', '', 1, 1, 0.1, 'solid,requires_touching_for_interaction,dice'),
(48, 'bardesk_polyfon', '#ffffff,#ffffff,#ABD0D2,#ABD0D2', 2, 1, 1, 'solid,can_stack_on_top'),
(49, 'bardeskcorner_polyfon', '#ffffff,#ABD0D2', 1, 1, 1, 'solid,can_stack_on_top'),
(50, 'bar_armas', '', 1, 1, 1, 'solid,requires_touching_for_interaction,custom_data_true_false'),
(51, 'bartable_armas', '', 1, 3, 1, 'solid,can_stack_on_top'),
(52, 'bar_chair_armas', '', 1, 1, 1, 'can_sit_on_top'),
(53, 'carpet_soft', '', 2, 4, 0, 'can_stand_on_top,can_stack_on_top'),
(54, 'carpet_soft*1', '#CC3333', 2, 4, 0, 'can_stand_on_top,can_stack_on_top'),
(55, 'carpet_soft*2', '#66FFFF', 2, 4, 0, 'can_stand_on_top,can_stack_on_top'),
(56, 'carpet_soft*3', '#FFCC99', 2, 4, 0, 'can_stand_on_top,can_stack_on_top'),
(57, 'carpet_soft*4', '#FFCCFF', 2, 4, 0, 'can_stand_on_top,can_stack_on_top'),
(58, 'carpet_soft*5', '#FFFF66', 2, 4, 0, 'can_stand_on_top,can_stack_on_top'),
(59, 'carpet_soft*6', '#669933', 2, 4, 0, 'can_stand_on_top,can_stack_on_top'),
(60, 'red_tv', '', 1, 1, 1, 'solid,custom_data_on_off'),
(61, 'wood_tv', '', 1, 2, 2, 'solid,custom_data_numeric_state'),
(62, 'carpet_polar*1', '#ffbbcf,#ffbbcf,#ffddef', 2, 3, 0, 'can_stand_on_top,can_stack_on_top'),
(63, 'smooth_table_polyfon', '', 2, 2, 1, 'solid,can_stack_on_top'),
(64, 'sofachair_polyfon_girl', '#ffffff,#ffffff,#EE7EA4,#EE7EA4', 1, 1, 1, 'can_sit_on_top'),
(65, 'bed_polyfon_girl_one', '#ffffff,#ffffff,#ffffff,#EE7EA4,#EE7EA4', 1, 3, 1.6, 'can_lay_on_top'),
(66, 'bed_polyfon_girl', '#ffffff,#ffffff,#EE7EA4,#EE7EA4', 2, 3, 1.6, 'can_lay_on_top'),
(67, 'sofa_polyfon_girl', '#ffffff,#ffffff,#ffffff,#ffffff,#EE7EA4,#EE7EA4,#EE7EA4,#EE7EA4', 2, 1, 1, 'can_sit_on_top'),
(68, 'bed_budgetb_one', '#FFFFFF,#FFFFFF,#FFFFFF,#FFFFFF,#FFFFFF,#FFFFFF', 1, 3, 1.6, 'can_lay_on_top'),
(69, 'bed_budgetb', '#FFFFFF,#FFFFFF,#FFFFFF,#FFFFFF,#FFFFFF,#FFFFFF', 2, 3, 1.6, 'can_lay_on_top'),
(70, 'plant_pineapple', '', 1, 1, 1, 'solid'),
(71, 'plant_fruittree', '', 1, 1, 1, 'solid'),
(72, 'plant_small_cactus', '', 1, 1, 1, 'solid'),
(73, 'plant_bonsai', '', 1, 1, 1, 'solid'),
(74, 'plant_big_cactus', '', 1, 1, 1, 'solid'),
(75, 'plant_yukka', '', 1, 1, 1, 'solid'),
(76, 'carpet_standard*7', '#99CCFF,#99CCFF,#99CCFF', 3, 5, 0, 'can_stand_on_top,can_stack_on_top'),
(77, 'carpet_standard*8', '#999966,#999966,#999966', 3, 5, 0, 'can_stand_on_top,can_stack_on_top'),
(78, 'carpet_standard*9', '#FFDD00,#FFDD00,#FFDD00', 3, 5, 0, 'can_stand_on_top,can_stack_on_top'),
(79, 'carpet_standard*a', '#55AC00,#55AC00,#55AC00', 3, 5, 0, 'can_stand_on_top,can_stack_on_top'),
(80, 'carpet_standard*b', '#336666,#336666,#336666', 3, 5, 0, 'can_stand_on_top,can_stack_on_top'),
(81, 'plant_sunflower', '', 1, 1, 1, 'solid'),
(82, 'plant_rose', '', 1, 1, 1, 'solid'),
(83, 'tv_luxus', '', 1, 3, 1, 'solid,custom_data_on_off'),
(84, 'bath', '', 1, 2, 1, 'can_sit_on_top,custom_data_numeric_on_off'),
(85, 'sink', '', 1, 1, 1, 'solid,requires_touching_for_interaction,custom_data_true_false'),
(86, 'toilet', '', 1, 1, 1.1, 'can_sit_on_top,custom_data_on_off'),
(87, 'duck', '', 1, 1, 0, 'solid'),
(88, 'tile', '', 4, 4, 0, 'can_stand_on_top,can_stack_on_top'),
(89, 'toilet_red', '', 1, 1, 1.1, 'can_sit_on_top,custom_data_on_off'),
(90, 'toilet_yell', '', 1, 1, 1.1, 'can_sit_on_top,custom_data_on_off'),
(91, 'tile_red', '', 4, 4, 0, 'can_stand_on_top,can_stack_on_top'),
(92, 'tile_yell', '', 4, 4, 0, 'can_stand_on_top,can_stack_on_top'),
(93, 'bar_basic', '', 1, 1, 1, 'solid,requires_touching_for_interaction,custom_data_true_false'),
(94, 'shelves_basic', '0,0,0', 2, 1, 2, 'solid'),
(95, 'soft_sofachair_norja', '#ffffff,#F7EBBC,#F7EBBC', 1, 1, 1, 'can_sit_on_top'),
(96, 'soft_sofa_norja', '#ffffff,#F7EBBC,#ffffff,#F7EBBC,#F7EBBC,#F7EBBC', 2, 1, 1, 'can_sit_on_top'),
(97, 'lamp_basic', '0,0,0', 1, 1, 2, 'solid,custom_data_numeric_on_off'),
(98, 'lamp2_armas', '0,0,0', 1, 1, 2, 'solid,custom_data_numeric_on_off'),
(99, 'fridge', '0,0,0', 1, 1, 2, 'solid,requires_touching_for_interaction,custom_data_true_false'),
(100, 'door', '', 1, 1, 2, 'solid,requires_touching_for_interaction,custom_data_true_false,teleporter'),
(101, 'doorB', '', 1, 1, 2, 'solid,requires_touching_for_interaction,custom_data_true_false,teleporter'),
(102, 'doorC', '', 1, 1, 2, 'solid,requires_touching_for_interaction,custom_data_true_false,teleporter'),
(103, 'menorah', '0,0,0', 1, 1, 1, 'solid,custom_data_numeric_state'),
(104, 'ham', '0,0,0', 1, 1, 0.1, 'solid'),
(105, 'wcandleset', '0,0,0', 1, 1, 0.2, 'solid,custom_data_numeric_on_off'),
(106, 'rcandleset', '0,0,0', 1, 1, 0.2, 'solid,custom_data_numeric_on_off'),
(107, 'throne', '0,0,0', 1, 1, 1, 'can_sit_on_top'),
(108, 'giftflowers', '0,0,0', 1, 1, 1, 'solid'),
(109, 'habbocake', '0,0,0', 1, 1, 2, 'solid,custom_data_numeric_on_off'),
(110, 'bunny', '0,0,0', 1, 1, 1, 'solid'),
(111, 'edice', '0,0,0', 1, 1, 1, 'solid,requires_touching_for_interaction,dice'),
(112, 'club_sofa', '0,0,0', 2, 1, 1, 'can_sit_on_top'),
(113, 'divider_poly3', '#ffffff,#ffffff,#ffffff,#ABD0D2,#ABD0D2', 1, 1, 1, 'solid,requires_rights_for_interaction,door'),
(114, 'divider_arm1', '0,0,0', 1, 1, 1, 'solid'),
(115, 'divider_arm2', '0,0,0', 2, 1, 1, 'solid,can_stack_on_top'),
(116, 'divider_arm3', '0,0,0', 1, 1, 1, 'solid,requires_rights_for_interaction,door'),
(117, 'divider_nor1', '#ffffff,#F7EBBC', 1, 1, 1, 'solid'),
(118, 'divider_silo1', '#ffffff,#ABD0D2', 1, 1, 1, 'solid,can_stack_on_top'),
(119, 'divider_nor2', '#ffffff,#ffffff,#F7EBBC,#F7EBBC', 2, 1, 1, 'solid'),
(120, 'divider_silo2', '0,0,0', 2, 1, 1, 'solid,can_stack_on_top'),
(121, 'divider_nor3', '#ffffff,#ffffff,#F7EBBC,#F7EBBC', 1, 1, 1, 'solid,requires_rights_for_interaction,door'),
(122, 'divider_silo3', '#ffffff,#ffffff,#ffffff,#ABD0D2', 1, 1, 1, 'solid,requires_rights_for_interaction,door'),
(123, 'mocchamaster', '0,0,0', 1, 1, 0, 'solid,requires_touching_for_interaction,custom_data_on_off'),
(124, 'carpet_legocourt', '0,0,0', 3, 3, 0, 'can_stand_on_top,can_stack_on_top'),
(125, 'bench_lego', '0,0,0', 4, 1, 1, 'can_sit_on_top'),
(126, 'legotrophy', '0,0,0', 1, 1, 1, 'solid,custom_data_numeric_on_off'),
(127, 'edicehc', '0,0,0', 1, 1, 1, 'solid,requires_touching_for_interaction,dice'),
(128, 'hcsohva', '0,0,0', 2, 1, 1, 'can_sit_on_top'),
(129, 'hcamme', '0,0,0', 1, 2, 0.9, 'can_sit_on_top,custom_data_numeric_on_off'),
(130, 'hockey_score', '0,0,0', 1, 1, 2, 'solid,requires_touching_for_interaction,custom_data_numeric_state'),
(131, 'hockey_light', '0,0,0', 1, 1, 1, 'solid,requires_touching_for_interaction,custom_data_true_false'),
(132, 'doorD', '', 1, 1, 2, 'solid,requires_touching_for_interaction,custom_data_true_false,teleporter'),
(133, 'prizetrophy2*3', '#ffffff,#ffffff,#996600', 1, 1, 2, 'solid,prize_trophy'),
(134, 'prizetrophy3*3', '#ffffff,#ffffff,#996600', 1, 1, 2, 'solid,prize_trophy'),
(135, 'prizetrophy4*3', '#ffffff,#ffffff,#996600', 1, 1, 2, 'solid,prize_trophy'),
(136, 'prizetrophy5*3', '#ffffff,#ffffff,#996600', 1, 1, 2, 'solid,prize_trophy'),
(137, 'prizetrophy6*3', '#ffffff,#ffffff,#996600', 1, 1, 2, 'solid,prize_trophy'),
(138, 'prizetrophy2*1', '#ffffff,#ffffff,#FFDD3F', 1, 1, 2, 'solid,prize_trophy'),
(139, 'prizetrophy3*1', '#ffffff,#ffffff,#FFDD3F', 1, 1, 2, 'solid,prize_trophy'),
(140, 'prizetrophy4*1', '#ffffff,#ffffff,#FFDD3F', 1, 1, 2, 'solid,prize_trophy'),
(141, 'prizetrophy5*1', '#ffffff,#ffffff,#FFDD3F', 1, 1, 2, 'solid,prize_trophy'),
(142, 'prizetrophy6*1', '#ffffff,#ffffff,#FFDD3F', 1, 1, 2, 'solid,prize_trophy'),
(143, 'prizetrophy*2', '#ffffff,#ffffff,#ffffff', 1, 1, 2, 'solid,prize_trophy'),
(144, 'prizetrophy2*2', '#ffffff,#ffffff,#ffffff', 1, 1, 2, 'solid,prize_trophy'),
(145, 'prizetrophy3*2', '#ffffff,#ffffff,#ffffff', 1, 1, 2, 'solid,prize_trophy'),
(146, 'prizetrophy4*2', '#ffffff,#ffffff,#ffffff', 1, 1, 2, 'solid,prize_trophy'),
(147, 'prizetrophy5*2', '#ffffff,#ffffff,#ffffff', 1, 1, 2, 'solid,prize_trophy'),
(148, 'prizetrophy6*2', '#ffffff,#ffffff,#ffffff', 1, 1, 2, 'solid,prize_trophy'),
(149, 'prizetrophy*3', '#ffffff,#ffffff,#996600', 1, 1, 2, 'solid,prize_trophy'),
(150, 'hc_lmp', '0,0,0', 1, 1, 1, 'solid,custom_data_numeric_on_off'),
(151, 'hc_tbl', '0,0,0', 1, 3, 1, 'solid,can_stack_on_top'),
(152, 'hc_chr', '0,0,0', 1, 1, 1, 'can_sit_on_top'),
(153, 'hc_dsk', '0,0,0', 1, 2, 1.5, 'solid,custom_data_numeric_on_off'),
(154, 'nest', '0,0,0', 1, 1, 0.1, 'can_stand_on_top'),
(155, 'petfood1', '0,0', 1, 1, 0, 'can_stand_on_top'),
(156, 'petfood2', '0', 1, 1, 0, 'can_stand_on_top'),
(157, 'petfood3', '0,0,0', 1, 1, 0, 'can_stand_on_top'),
(158, 'waterbowl*4', '#0099ff,#ffffff,#ffffff', 1, 1, 0, 'can_stand_on_top'),
(159, 'waterbowl*5', '#bf7f00,#ffffff,#ffffff', 1, 1, 0, 'can_stand_on_top'),
(160, 'waterbowl*2', '#3fff3f,#ffffff,#ffffff', 1, 1, 0, 'can_stand_on_top'),
(161, 'waterbowl*1', '#ff3f3f,#ffffff,#ffffff', 1, 1, 0, 'can_stand_on_top'),
(162, 'waterbowl*3', '#ffff00,#ffffff,#ffffff', 1, 1, 0, 'can_stand_on_top'),
(163, 'toy1', '#ff0000,#ffff00,#ffffff,#ffffff', 1, 1, 0, 'can_stand_on_top'),
(164, 'toy1*1', '#ff7f00,#007f00,#ffffff,#ffffff', 1, 1, 0, 'can_stand_on_top'),
(165, 'toy1*2', '#003f7f,#ff00bf,#ffffff,#ffffff', 1, 1, 0, 'can_stand_on_top'),
(166, 'toy1*3', '#bf1900,#00bfbf,#ffffff,#ffffff', 1, 1, 0, 'can_stand_on_top'),
(167, 'toy1*4', '#000000,#ffffff,#ffffff,#ffffff', 1, 1, 0, 'can_stand_on_top'),
(168, 'goodie1', '#ff4cbf,#ffffff', 1, 1, 0, 'can_stand_on_top'),
(169, 'goodie2', '0', 1, 1, 0, 'can_stand_on_top'),
(170, 'rare_dragonlamp*4', '#ffffff,#3e3d40,#3e3d40,0,0,0,0', 1, 1, 1, 'solid,custom_data_numeric_on_off'),
(171, 'rare_dragonlamp*0', '#ffffff,#fa2d00,#fa2d00,0,0,0,0', 1, 1, 1, 'solid,custom_data_numeric_on_off'),
(172, 'rare_dragonlamp*5', '#ffffff,#589a03,#589a03,0,0,0,0', 1, 1, 1, 'solid,custom_data_numeric_on_off'),
(173, 'rare_dragonlamp*2', '#ffffff,#02bb70,#02bb70,0,0,0,0', 1, 1, 1, 'solid,custom_data_numeric_on_off'),
(174, 'rare_dragonlamp*8', '#ffffff,#ff5f01,#ff5f01,0,0,0,0', 1, 1, 1, 'solid,custom_data_numeric_on_off'),
(175, 'rare_dragonlamp*9', '#FFFFFF,#B357FF,#B357FF,0,0,0,0', 1, 1, 1, 'solid,custom_data_numeric_on_off'),
(176, 'rare_dragonlamp*7', '#ffffff,#83aeff,#83aeff,0,0,0,0', 1, 1, 1, 'solid,custom_data_numeric_on_off'),
(177, 'rare_dragonlamp*6', '#ffffff,#ffbc00,#ffbc00,0,0,0,0', 1, 1, 1, 'solid,custom_data_numeric_on_off'),
(178, 'rare_dragonlamp*1', '#ffffff,#3470ff,#3470ff,0,0,0,0', 1, 1, 1, 'solid,custom_data_numeric_on_off'),
(179, 'rare_dragonlamp*3', '#ffffff,#ffffff,#ffffff,0,0,0,0', 1, 1, 1, 'solid,custom_data_numeric_on_off'),
(180, 'queue_tile1*6', '#ffffff,#A2E8FA,#ffffff,#ffffff', 1, 1, 0.45, 'can_stand_on_top,can_stack_on_top,roller'),
(181, 'queue_tile1*9', '#ffffff,#9AFF60,#ffffff,#ffffff', 1, 1, 0.45, 'can_stand_on_top,can_stack_on_top,roller'),
(182, 'queue_tile1*8', '#ffffff,#1E8AA5,#ffffff,#ffffff', 1, 1, 0.45, 'can_stand_on_top,can_stack_on_top,roller'),
(183, 'queue_tile1*7', '#ffffff,#FC5AFF,#ffffff,#ffffff', 1, 1, 0.45, 'can_stand_on_top,can_stack_on_top,roller'),
(184, 'queue_tile1*2', '#ffffff,#FF3333,#ffffff,#ffffff', 1, 1, 0.45, 'can_stand_on_top,can_stack_on_top,roller'),
(185, 'cn_lamp', '0,0,0', 1, 1, 1, 'solid,custom_data_numeric_on_off'),
(186, 'cn_sofa', '0,0,0', 3, 1, 1, 'can_sit_on_top'),
(187, 'sporttrack1*1', '#ffffff,#e4592d,#ffffff', 2, 2, 0, 'can_stand_on_top,can_stack_on_top'),
(188, 'sporttrack1*3', '#ffffff,#5cb800,#ffffff', 2, 2, 0, 'can_stand_on_top,can_stack_on_top'),
(189, 'sporttrack1*2', '#ffffff,#62818b,#ffffff', 2, 2, 0, 'can_stand_on_top,can_stack_on_top'),
(190, 'sporttrack2*1', '#ffffff,#e4592d,#ffffff', 2, 2, 0, 'can_stand_on_top,can_stack_on_top'),
(191, 'sporttrack2*2', '#ffffff,#62818b,#ffffff', 2, 2, 0, 'can_stand_on_top,can_stack_on_top'),
(192, 'sporttrack2*3', '#ffffff,#5cb800,#ffffff', 2, 2, 0, 'can_stand_on_top,can_stack_on_top'),
(193, 'sporttrack3*1', '#ffffff,#e4592d,#ffffff', 2, 2, 0, 'can_stand_on_top,can_stack_on_top'),
(194, 'sporttrack3*2', '#ffffff,#62818b,#ffffff', 2, 2, 0, 'can_stand_on_top,can_stack_on_top'),
(195, 'sporttrack3*3', '#ffffff,#5cb800,#ffffff', 2, 2, 0, 'can_stand_on_top,can_stack_on_top'),
(196, 'footylamp', '0,0,0', 1, 1, 1, 'solid,custom_data_numeric_on_off'),
(197, 'barchair_silo', '#ffffff,#ABD0D2', 1, 1, 1.5, 'can_sit_on_top'),
(198, 'bardesk_polyfon*5', '#ffffff,#ffffff,#FF9BBD,#FF9BBD', 2, 1, 1, 'solid,can_stack_on_top'),
(199, 'bardeskcorner_polyfon*5', '#ffffff,#FF9BBD', 1, 1, 1, 'solid,can_stack_on_top'),
(200, 'divider_poly3*5', '#ffffff,#ffffff,#ffffff,#EE7EA4,#EE7EA4', 1, 1, 1, 'solid,requires_rights_for_interaction,door'),
(201, 'chair_china', '0,0,0', 1, 1, 1, 'can_sit_on_top'),
(202, 'china_table', '0,0,0', 1, 1, 0.9, 'solid,can_stack_on_top'),
(203, 'safe_silo', '#ffffff,#ABD0D2,#ABD0D2,#ffffff', 1, 1, 1, 'solid,custom_data_true_false'),
(204, 'china_shelve', '0,0,0', 2, 1, 2, 'solid'),
(205, 'divider_nor5', '#ffffff,#F7EBBC,#F7EBBC', 1, 1, 2, 'solid,requires_rights_for_interaction,door'),
(206, 'divider_nor4', '#ffffff,#ffffff,#F7EBBC,#F7EBBC,#F7EBBC,#F7EBBC', 2, 1, 2, 'solid,requires_rights_for_interaction,door'),
(207, 'wall_china', '0,0,0', 1, 1, 1, 'solid'),
(208, 'corner_china', '0,0,0', 1, 1, 1, 'solid'),
(209, 'CF_10_coin_gold', '0,0,0', 1, 1, 0.1, 'solid,can_stack_on_top,redeemable'),
(210, 'CF_1_coin_bronze', '0,0,0', 1, 1, 0.1, 'solid,can_stack_on_top,redeemable'),
(211, 'CF_20_moneybag', '0,0,0', 1, 1, 1, 'solid,can_stack_on_top,redeemable'),
(212, 'CF_50_goldbar', '0,0,0', 1, 1, 0.4, 'solid,can_stack_on_top,redeemable'),
(213, 'CF_5_coin_silver', '0,0,0', 1, 1, 0.1, 'solid,can_stack_on_top,redeemable'),
(214, 'hc_tv', '', 2, 1, 2, 'solid,custom_data_numeric_on_off'),
(215, 'gothgate', '0,0,0', 2, 1, 2.5, 'solid,requires_rights_for_interaction,door'),
(216, 'gothiccandelabra', '0,0,0', 1, 1, 1, 'solid,custom_data_numeric_on_off'),
(217, 'gothrailing', '0,0,0', 2, 1, 1, 'solid,can_stack_on_top'),
(218, 'goth_table', '0,0,0', 1, 5, 1, 'solid,can_stack_on_top'),
(219, 'hc_bkshlf', '0,0,0', 1, 4, 3, 'solid'),
(220, 'hc_btlr', '0,0,0', 1, 1, 1, 'solid,requires_touching_for_interaction,custom_data_true_false'),
(221, 'hc_crtn', '0,0,0', 2, 1, 1, 'solid,requires_rights_for_interaction,door'),
(222, 'hc_djset', '0,0,0', 3, 1, 1.5, 'solid'),
(223, 'hc_frplc', '0,0,0', 1, 3, 3, 'solid,custom_data_numeric_on_off'),
(224, 'hc_lmpst', '0,0,0', 1, 1, 3, 'solid,custom_data_numeric_on_off'),
(225, 'hc_machine', '0,0,0', 1, 3, 3, 'solid,custom_data_numeric_on_off'),
(226, 'hc_rllr', '0,0,0', 1, 1, 0.45, 'can_stand_on_top,can_stack_on_top,roller'),
(227, 'hc_rntgn', '0,0,0', 2, 1, 2, 'solid'),
(228, 'hc_trll', '0,0,0', 1, 2, 1.5, 'solid'),
(229, 'gothic_chair*3', '#ffffff,#dd0000,#ffffff,#dd0000', 1, 1, 1.2, 'can_sit_on_top'),
(230, 'gothic_sofa*3', '#ffffff,#dd0000,#ffffff,#ffffff,#dd0000,#ffffff', 2, 1, 1.2, 'can_sit_on_top'),
(231, 'gothic_stool*3', '#ffffff,#dd0000,#ffffff', 1, 1, 1.2, 'can_sit_on_top'),
(232, 'sound_machine', '', 1, 1, 1, 'solid,requires_rights_for_interaction,custom_data_numeric_on_off,sound_machine'),
(233, 'plant_mazegate', '', 2, 1, 1, 'solid,requires_rights_for_interaction,door'),
(234, 'plant_maze', '', 2, 1, 1, 'solid'),
(235, 'plant_bulrush', '', 1, 1, 1, 'solid'),
(236, 'petfood4', '0,0', 1, 1, 0, 'can_stand_on_top'),
(237, 'gothic_carpet', '', 2, 4, 0, 'can_stand_on_top,can_stack_on_top'),
(238, 'gothic_carpet2', '', 2, 4, 0, 'can_stand_on_top,can_stack_on_top'),
(239, 'sound_set_1', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(240, 'sound_set_3', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(241, 'sound_set_6', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(242, 'sound_set_7', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(243, 'sound_set_8', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(244, 'post.it', '', 0, 0, 0, 'wall_item,requires_rights_for_interaction,post_it'),
(245, 'gothicfountain', '', 0, 0, 0, 'wall_item,custom_data_numeric_on_off'),
(246, 'hc_wall_lamp', '', 0, 0, 0, 'wall_item,custom_data_numeric_on_off'),
(247, 'industrialfan', '', 0, 0, 0, 'wall_item,custom_data_numeric_on_off'),
(248, 'torch', '', 0, 0, 0, 'wall_item,custom_data_numeric_on_off'),
(249, 'floor', '', 0, 0, 0, 'wall_item,decoration'),
(250, 'wallpaper', '', 0, 0, 0, 'wall_item,decoration'),
(251, 'poster', '', 0, 0, 0, 'wall_item'),
(252, 'table_norja_med', '0,#E2DAAC', 2, 2, 1, 'solid,can_stack_on_top'),
(253, 'doormat_plain*4', '#ccddff', 1, 1, 0.2, 'can_stand_on_top,can_stack_on_top'),
(254, 'doormat_plain*2', '#99DCCC', 1, 1, 0.2, 'can_stand_on_top,can_stack_on_top'),
(255, 'doormat_plain*3', '#ffee00', 1, 1, 0.2, 'can_stand_on_top,can_stack_on_top'),
(256, 'doormat_plain*5', '#ddccff', 1, 1, 0.2, 'can_stand_on_top,can_stack_on_top'),
(257, 'rare_parasol*1', '#ffffff,#ffffff,#ffffff,#ffff11', 1, 1, 1, 'solid,custom_data_numeric_on_off'),
(258, 'rare_parasol*2', '#ffffff,#ffffff,#ffffff,#ff8f61', 1, 1, 1, 'solid,custom_data_numeric_on_off'),
(259, 'rare_parasol*3', '#ffffff,#ffffff,#ffffff,#d47fff', 1, 1, 1, 'solid,custom_data_numeric_on_off'),
(260, 'rare_parasol*0', '#ffffff,#ffffff,#ffffff,#9eff1c', 1, 1, 1, 'solid,custom_data_numeric_on_off'),
(261, 'scifidoor*10', '#ffffff,#bb99ff,#bb99ff,#bb99ff,#ffffff', 1, 1, 1.5, 'solid,requires_rights_for_interaction,door'),
(262, 'scifidoor*9', '#ffffff,#4488ff,#4488ff,#4488ff,#ffffff', 1, 1, 1.5, 'solid,requires_rights_for_interaction,door'),
(263, 'scifidoor*8', '#ffffff,#ff99cc,#ff99cc,#ff99cc,#ffffff', 1, 1, 1.5, 'solid,requires_rights_for_interaction,door'),
(264, 'scifidoor*7', '#ffffff,#aaffff,#aaffff,#aaffff,#ffffff', 1, 1, 1.5, 'solid,requires_rights_for_interaction,door'),
(265, 'scifidoor*6', '#ffffff,#333333,#333333,#333333,#ffffff', 1, 1, 1.5, 'solid,requires_rights_for_interaction,door'),
(266, 'scifidoor*5', '#ffffff,#ffffff,#ffffff,#ffffff,#ffffff', 1, 1, 1.5, 'solid,requires_rights_for_interaction,door'),
(267, 'scifidoor*4', '#ffffff,#99dd77,#99dd77,#99dd77,#ffffff', 1, 1, 1.5, 'solid,requires_rights_for_interaction,door'),
(268, 'scifidoor*3', '#ffffff,#aaccff,#aaccff,#aaccff,#ffffff', 1, 1, 1.5, 'solid,requires_rights_for_interaction,door'),
(269, 'scifidoor*2', '#ffffff,#ffee66,#ffee66,#ffee66,#ffffff', 1, 1, 1.5, 'solid,requires_rights_for_interaction,door'),
(270, 'scifidoor*1', '#ffffff,#ffaaaa,#ffaaaa,#ffaaaa,#ffffff', 1, 1, 1.5, 'solid,requires_rights_for_interaction,door'),
(271, 'rare_beehive_bulb', '#ffffff,#ffffff,#ffffff,#ffffff,#55c4de,#ffffff', 1, 1, 1, 'solid,can_stack_on_top,custom_data_numeric_on_off'),
(272, 'rare_beehive_bulb*1', '#ffffff,#ffffff,#ffffff,#ffffff,#ff4c47,#ffffff', 1, 1, 1, 'solid,can_stack_on_top,custom_data_numeric_on_off'),
(273, 'rare_beehive_bulb*2', '#ffffff,#ffffff,#ffffff,#ffffff,#ffc307,#ffffff', 1, 1, 1, 'solid,can_stack_on_top,custom_data_numeric_on_off'),
(274, 'scifiport*0', '#ffffff,#dd2d22,#ffffff,#ffffff,#ffffff,#dd2d22', 1, 1, 1, 'solid,requires_rights_for_interaction,door'),
(275, 'scifiport*9', '#ffffff,#7733ff,#ffffff,#ffffff,#ffffff,#7733ff', 1, 1, 1, 'solid,requires_rights_for_interaction,door'),
(276, 'scifiport*8', '#ffffff,#bb55cc,#ffffff,#ffffff,#ffffff,#bb55cc', 1, 1, 1, 'solid,requires_rights_for_interaction,door'),
(277, 'scifiport*7', '#ffffff,#00cccc,#ffffff,#ffffff,#ffffff,#00cccc', 1, 1, 1, 'solid,requires_rights_for_interaction,door'),
(278, 'scifiport*6', '#ffffff,#ffffff,#ffffff,#ffffff,#ffffff,#ffffff', 1, 1, 1, 'solid,requires_rights_for_interaction,door'),
(279, 'scifiport*5', '#ffffff,#555555,#ffffff,#ffffff,#ffffff,#555555', 1, 1, 1, 'solid,requires_rights_for_interaction,door'),
(280, 'scifiport*4', '#ffffff,#ff5577,#ffffff,#ffffff,#ffffff,#ff5577', 1, 1, 1, 'solid,requires_rights_for_interaction,door'),
(281, 'scifiport*3', '#ffffff,#02bb70,#ffffff,#ffffff,#ffffff,#02bb70', 1, 1, 1, 'solid,requires_rights_for_interaction,door'),
(282, 'scifiport*2', '#ffffff,#5599ff,#ffffff,#ffffff,#ffffff,#5599ff', 1, 1, 1, 'solid,requires_rights_for_interaction,door'),
(283, 'scifiport*1', '#ffffff,#ffbc00,#ffffff,#ffffff,#ffffff,#ffbc00', 1, 1, 1, 'solid,requires_rights_for_interaction,door'),
(284, 'scifirocket*9', '#ffffff,#ffffff,#7733ff,#ffffff', 1, 1, 1, 'solid,custom_data_on_off'),
(285, 'scifirocket*8', '#ffffff,#ffffff,#bb55cc,#ffffff', 1, 1, 1, 'solid,custom_data_on_off'),
(286, 'scifirocket*7', '#ffffff,#ffffff,#00cccc,#ffffff', 1, 1, 1, 'solid,custom_data_on_off'),
(287, 'scifirocket*6', '#ffffff,#ffffff,#ffffff,#ffffff', 1, 1, 1, 'solid,custom_data_on_off'),
(288, 'scifirocket*5', '#ffffff,#ffffff,#555555,#ffffff', 1, 1, 1, 'solid,custom_data_on_off'),
(289, 'scifirocket*4', '#ffffff,#ffffff,#ff5577,#ffffff', 1, 1, 1, 'solid,custom_data_on_off'),
(290, 'scifirocket*3', '#ffffff,#ffffff,#02bb70,#ffffff', 1, 1, 1, 'solid,custom_data_on_off'),
(291, 'scifirocket*2', '#ffffff,#ffffff,#5599ff,#ffffff', 1, 1, 1, 'solid,custom_data_on_off'),
(292, 'scifirocket*1', '#ffffff,#ffffff,#ffbc00,#ffffff', 1, 1, 1, 'solid,custom_data_on_off'),
(293, 'scifirocket*0', '#ffffff,#ffffff,#dd2d22,#ffffff', 1, 1, 1, 'solid,custom_data_on_off'),
(294, 'pillow*5', '#494D29,#494D29,#ffffff,#ffffff', 1, 1, 1, 'can_sit_on_top'),
(295, 'pillow*8', '#214E68,#214E68,#ffffff,#ffffff', 1, 1, 1, 'can_sit_on_top'),
(296, 'pillow*0', '#ffffff,#ffffff,#ffffff,#ffffff', 1, 1, 1, 'can_sit_on_top'),
(297, 'pillow*1', '#FF8888,#FF8888,#ffffff,#ffffff', 1, 1, 1, 'can_sit_on_top'),
(298, 'pillow*2', '#F00000,#F00000,#ffffff,#ffffff', 1, 1, 1, 'can_sit_on_top'),
(299, 'pillow*7', '#E532CA,#E532CA,#ffffff,#ffffff', 1, 1, 1, 'can_sit_on_top'),
(300, 'pillow*9', '#B9FF4B,#B9FF4B,#ffffff,#ffffff', 1, 1, 1, 'can_sit_on_top'),
(301, 'pillow*4', '#FFBD18,#FFBD18,#ffffff,#ffffff', 1, 1, 1, 'can_sit_on_top'),
(302, 'pillow*6', '#5DAAC9,#5DAAC9,#ffffff,#ffffff', 1, 1, 1, 'can_sit_on_top'),
(303, 'pillow*3', '#63C9A0,#63C9A0,#ffffff,#ffffff', 1, 1, 1, 'can_sit_on_top'),
(304, 'marquee*1', '#ffffff,#ffffff,#ffffff,#FF798F', 1, 1, 0.7, 'solid,requires_rights_for_interaction,door'),
(305, 'marquee*2', '#ffffff,#ffffff,#ffffff,#C60000', 1, 1, 0.7, 'solid,requires_rights_for_interaction,door'),
(306, 'marquee*7', '#ffffff,#ffffff,#ffffff,#D600E2', 1, 1, 0.7, 'solid,requires_rights_for_interaction,door'),
(307, 'marquee*a', '#ffffff,#ffffff,#ffffff,#ffffff', 1, 1, 0.7, 'solid,requires_rights_for_interaction,door'),
(308, 'marquee*8', '#ffffff,#ffffff,#ffffff,#004AA0', 1, 1, 0.7, 'solid,requires_rights_for_interaction,door'),
(309, 'marquee*9', '#ffffff,#ffffff,#ffffff,#8FD94A', 1, 1, 0.7, 'solid,requires_rights_for_interaction,door'),
(310, 'marquee*5', '#ffffff,#ffffff,#ffffff,#707070', 1, 1, 0.7, 'solid,requires_rights_for_interaction,door'),
(311, 'marquee*4', '#ffffff,#ffffff,#ffffff,#F8CD00', 1, 1, 0.7, 'solid,requires_rights_for_interaction,door'),
(312, 'marquee*6', '#ffffff,#ffffff,#ffffff,#719EFD', 1, 1, 0.7, 'solid,requires_rights_for_interaction,door'),
(313, 'marquee*3', '#ffffff,#ffffff,#ffffff,#68DADA', 1, 1, 0.7, 'solid,requires_rights_for_interaction,door'),
(314, 'wooden_screen*1', '#ffffff,#ffffff,#FFA795,#FFA795,#ffffff,#ffffff', 1, 2, 1, 'solid'),
(315, 'wooden_screen*2', '#ffffff,#ffffff,#BD0000,#BD0000,#ffffff,#ffffff', 1, 2, 1, 'solid'),
(316, 'wooden_screen*7', '#ffffff,#ffffff,#DA2591,#DA2591,#ffffff,#ffffff', 1, 2, 1, 'solid'),
(317, 'wooden_screen*0', '#ffffff,#ffffff,#ffffff,#ffffff,#ffffff,#ffffff', 1, 2, 1, 'solid'),
(318, 'wooden_screen*8', '#ffffff,#ffffff,#004B5E,#004B5E,#ffffff,#ffffff', 1, 2, 1, 'solid'),
(319, 'wooden_screen*5', '#ffffff,#ffffff,#778B61,#778B61,#ffffff,#ffffff', 1, 2, 1, 'solid'),
(320, 'wooden_screen*9', '#ffffff,#ffffff,#A0BE1F,#A0BE1F,#ffffff,#ffffff', 1, 2, 1, 'solid'),
(321, 'wooden_screen*4', '#ffffff,#ffffff,#F7B800,#F7B800,#ffffff,#ffffff', 1, 2, 1, 'solid'),
(322, 'wooden_screen*6', '#ffffff,#ffffff,#72B6D1,#72B6D1,#ffffff,#ffffff', 1, 2, 1, 'solid'),
(323, 'wooden_screen*3', '#ffffff,#ffffff,#79E4B3,#79E4B3,#ffffff,#ffffff', 1, 2, 1, 'solid'),
(324, 'rare_icecream*1', '#FFFFFF,#3C75FF,0,0,0', 1, 1, 1, 'solid,requires_touching_for_interaction,custom_data_true_false'),
(325, 'rare_icecream*7', '#FFFFFF,#97420C,0,0,0', 1, 1, 1, 'solid,requires_touching_for_interaction,custom_data_true_false'),
(326, 'rare_icecream*8', '#FFFFFF,#00E5E2,0,0,0', 1, 1, 1, 'solid,requires_touching_for_interaction,custom_data_true_false'),
(327, 'rare_icecream*2', '#FFFFFF,#55CD01,0,0,0', 1, 1, 1, 'solid,requires_touching_for_interaction,custom_data_true_false'),
(328, 'rare_icecream*6', '#FFFFFF,#FF8000,0,0,0', 1, 1, 1, 'solid,requires_touching_for_interaction,custom_data_true_false'),
(329, 'rare_icecream*9', '#FFFFFF,#FF60B0,0,0,0', 1, 1, 1, 'solid,requires_touching_for_interaction,custom_data_true_false'),
(330, 'rare_icecream*3', '#FFFFFF,#C05DFF,0,0,0', 1, 1, 1, 'solid,requires_touching_for_interaction,custom_data_true_false'),
(331, 'rare_icecream*0', '#FFFFFF,#F43100,0,0,0', 1, 1, 1, 'solid,requires_touching_for_interaction,custom_data_true_false'),
(332, 'rare_icecream*4', '#FFFFFF,#FF9898,0,0,0', 1, 1, 1, 'solid,requires_touching_for_interaction,custom_data_true_false'),
(333, 'rare_icecream*5', '#FFFFFF,#E1CC00,0,0,0', 1, 1, 1, 'solid,requires_touching_for_interaction,custom_data_true_false'),
(334, 'rare_fountain', '#ffffff,#ffffff,#ff6666', 1, 1, 1, 'solid,can_stack_on_top,custom_data_numeric_on_off'),
(335, 'rare_fountain*1', '#ffffff,#ffffff,#ffffff', 1, 1, 1, 'solid,can_stack_on_top,custom_data_numeric_on_off'),
(336, 'rare_fountain*2', '#ffffff,#ffffff,#b8cf00', 1, 1, 1, 'solid,can_stack_on_top,custom_data_numeric_on_off'),
(337, 'rare_fountain*3', '#ffffff,#ffffff,#59cccc', 1, 1, 1, 'solid,can_stack_on_top,custom_data_numeric_on_off'),
(338, 'rare_elephant_statue', '#ffffff,#ffcf1c', 1, 1, 1, 'solid,can_stack_on_top'),
(339, 'rare_elephant_statue*1', '#ffffff,#bfbfbf', 1, 1, 1, 'solid,can_stack_on_top'),
(340, 'rare_elephant_statue*2', '#ffffff,#d46b00', 1, 1, 1, 'solid,can_stack_on_top'),
(341, 'rare_fan*7', '#97420C,0,0,0', 1, 1, 1, 'solid,custom_data_numeric_on_off'),
(342, 'rare_fan*6', '#FF8000,0,0,0', 1, 1, 1, 'solid,custom_data_numeric_on_off'),
(343, 'rare_fan*9', '#FF60B0,0,0,0', 1, 1, 1, 'solid,custom_data_numeric_on_off'),
(344, 'rare_fan*3', '#C05DFF,0,0,0', 1, 1, 1, 'solid,custom_data_numeric_on_off'),
(345, 'rare_fan*0', '#F43100,0,0,0', 1, 1, 1, 'solid,custom_data_numeric_on_off'),
(346, 'rare_fan*4', '#FF9898,0,0,0', 1, 1, 1, 'solid,custom_data_numeric_on_off'),
(347, 'rare_fan*5', '#E1CC00,0,0,0', 1, 1, 1, 'solid,custom_data_numeric_on_off'),
(348, 'rare_fan*1', '#3C75FF,0,0,0', 1, 1, 1, 'solid,custom_data_numeric_on_off'),
(349, 'rare_fan*8', '#00E5E2,0,0,0', 1, 1, 1, 'solid,custom_data_numeric_on_off'),
(350, 'rare_fan*2', '#55CD01,0,0,0', 1, 1, 1, 'solid,custom_data_numeric_on_off'),
(351, 'habbowheel', '', 1, 1, 1, 'wall_item,wheel_of_fortune'),
(352, 'roomdimmer', '', 1, 1, 1, 'wall_item,roomdimmer'),
(353, 'jukebox*1', '', 1, 1, 1, 'solid,jukebox'),
(354, 'jukebox_ptv*1', '', 1, 1, 1, 'solid,jukebox'),
(355, 'carpet_soft_tut', '', 1, 1, 0.2, 'can_stand_on_top,can_stack_on_top'),
(356, 'sound_set_9', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(357, 'sound_set_10', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(358, 'sound_set_2', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(359, 'sound_set_4', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(360, 'sound_set_5', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(361, 'sound_set_11', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(362, 'sound_set_12', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(363, 'sound_set_13', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(364, 'sound_set_14', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(365, 'sound_set_15', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(366, 'sound_set_16', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(367, 'sound_set_17', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(368, 'sound_set_18', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(369, 'sound_set_19', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(370, 'sound_set_20', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(371, 'sound_set_21', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(372, 'sound_set_22', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(373, 'sound_set_23', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(374, 'sound_set_24', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(375, 'sound_set_25', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(376, 'sound_set_26', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(377, 'sound_set_27', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(378, 'sound_set_28', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(379, 'sound_set_29', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(380, 'sound_set_30', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(381, 'sound_set_31', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(382, 'sound_set_32', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(383, 'sound_set_33', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(384, 'sound_set_34', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(385, 'sound_set_35', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(386, 'sound_set_36', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(387, 'sound_set_37', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(388, 'sound_set_38', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(389, 'sound_set_39', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(390, 'sound_set_40', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(391, 'sound_set_41', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(392, 'sound_set_42', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(393, 'sound_set_43', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(394, 'sound_set_44', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(395, 'sound_set_45', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(396, 'sound_set_46', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(397, 'sound_set_47', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(398, 'sound_set_48', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(399, 'sound_set_49', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(400, 'sound_set_50', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(401, 'sound_set_51', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(402, 'sound_set_52', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(403, 'sound_set_53', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(404, 'sound_set_54', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(405, 'sound_set_55', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(406, 'sound_set_56', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(407, 'sound_set_57', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(408, 'sound_set_58', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(409, 'sound_set_59', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(410, 'sound_set_60', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(411, 'sound_set_61', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(412, 'sound_set_62', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(413, 'sound_set_63', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(414, 'sound_set_64', '', 1, 1, 0.2, 'solid,can_stack_on_top,sound_machine_sample_set'),
(415, 'present_gen1', '', 1, 1, 1, 'solid,present,can_stack_on_top'),
(416, 'present_gen2', '', 1, 1, 1, 'solid,present,can_stack_on_top'),
(417, 'present_gen3', '', 1, 1, 1, 'solid,present,can_stack_on_top'),
(418, 'present_gen4', '', 1, 1, 1, 'solid,present,can_stack_on_top'),
(419, 'present_gen5', '', 1, 1, 1, 'solid,present,can_stack_on_top'),
(420, 'present_gen6', '', 1, 1, 1, 'solid,present,can_stack_on_top'),
(421, 'camera', '', 1, 1, 0, 'solid'),
(422, 'photo', '', 0, 0, 0, 'photo,wall_item'),
(423, 'film', '', 0, 0, 0, '');

-- --------------------------------------------------------

--
-- Table structure for table `items_moodlight_presets`
--

CREATE TABLE `items_moodlight_presets` (
  `item_id` int(11) NOT NULL,
  `current_preset` int(11) NOT NULL DEFAULT 1,
  `preset_1` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '1,#000000,255',
  `preset_2` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '1,#000000,255',
  `preset_3` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '1,#000000,255'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `items_photos`
--

CREATE TABLE `items_photos` (
  `photo_id` int(11) NOT NULL,
  `photo_user_id` bigint(11) NOT NULL,
  `timestamp` bigint(11) NOT NULL,
  `photo_data` blob NOT NULL,
  `photo_checksum` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `items_teleporter_links`
--

CREATE TABLE `items_teleporter_links` (
  `item_id` int(11) NOT NULL,
  `linked_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `messenger_friends`
--

CREATE TABLE `messenger_friends` (
  `from_id` int(11) NOT NULL,
  `to_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `messenger_messages`
--

CREATE TABLE `messenger_messages` (
  `id` int(11) NOT NULL,
  `receiver_id` int(11) DEFAULT NULL,
  `sender_id` int(11) DEFAULT NULL,
  `unread` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `body` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `date` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `messenger_requests`
--

CREATE TABLE `messenger_requests` (
  `from_id` int(11) DEFAULT NULL,
  `to_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `rank_badges`
--

CREATE TABLE `rank_badges` (
  `rank` tinyint(1) UNSIGNED NOT NULL DEFAULT 1,
  `badge` char(3) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `rank_fuserights`
--

CREATE TABLE `rank_fuserights` (
  `min_rank` int(11) NOT NULL,
  `fuseright` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `rank_fuserights`
--

INSERT INTO `rank_fuserights` (`min_rank`, `fuseright`) VALUES
(1, 'default'),
(1, 'fuse_login'),
(1, 'fuse_buy_credits'),
(1, 'fuse_trade'),
(1, 'fuse_room_queue_default'),
(2, 'fuse_enter_full_rooms'),
(3, 'fuse_enter_locked_rooms'),
(3, 'fuse_kick'),
(3, 'fuse_mute'),
(4, 'fuse_ban'),
(4, 'fuse_room_mute'),
(4, 'fuse_room_kick'),
(4, 'fuse_receive_calls_for_help'),
(4, 'fuse_remove_stickies'),
(5, 'fuse_mod'),
(5, 'fuse_superban'),
(5, 'fuse_pick_up_any_furni'),
(5, 'fuse_ignore_room_owner'),
(5, 'fuse_any_room_controller'),
(2, 'fuse_room_alert'),
(5, 'fuse_moderator_access'),
(6, 'fuse_administrator_access'),
(6, 'fuse_see_flat_ids'),
(5, 'fuse_credits');

-- --------------------------------------------------------

--
-- Table structure for table `rooms`
--

CREATE TABLE `rooms` (
  `id` int(11) NOT NULL,
  `owner_id` varchar(11) COLLATE utf8mb4_unicode_ci NOT NULL,
  `category` int(11) DEFAULT 2,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `model` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ccts` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `wallpaper` int(4) DEFAULT 0,
  `floor` int(4) DEFAULT 0,
  `showname` tinyint(1) DEFAULT 1,
  `superusers` tinyint(1) DEFAULT 0,
  `accesstype` tinyint(3) DEFAULT 0,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT '',
  `visitors_now` int(11) DEFAULT 0,
  `visitors_max` int(11) DEFAULT 25
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `rooms`
--

INSERT INTO `rooms` (`id`, `owner_id`, `category`, `name`, `description`, `model`, `ccts`, `wallpaper`, `floor`, `showname`, `superusers`, `accesstype`, `password`, `visitors_now`, `visitors_max`) VALUES
(1, '0', 3, 'Welcome Lounge', 'welcome_lounge', 'newbie_lobby', 'hh_room_nlobby', 0, 0, 0, 0, 0, '', 0, 40),
(2, '0', 3, 'Theatredome', 'theatredrome', 'theater', 'hh_room_theater', 0, 0, 0, 0, 0, '', 0, 100),
(3, '0', 3, 'Library', 'library', 'library', 'hh_room_library', 0, 0, 0, 0, 0, '', 0, 30),
(4, '0', 5, 'TV Studio', 'tv_studio', 'tv_studio', 'hh_room_tv_studio_general', 0, 0, 0, 0, 0, '', 0, 20),
(5, '0', 5, 'Cinema', 'habbo_cinema', 'cinema_a', 'hh_room_cinema', 0, 0, 0, 0, 0, '', 0, 50),
(6, '0', 5, 'Power Gym', 'sport', 'sport', 'hh_room_sport', 0, 0, 0, 0, 0, '', 0, 35),
(7, '0', 5, 'Olympic Stadium', 'ballroom', 'ballroom', 'hh_room_ballroom', 0, 0, 0, 0, 0, '', 0, 50),
(8, '0', 5, 'Habbo Kitchen', 'hotel_kitchen', 'cr_kitchen', 'hh_room_kitchen', 0, 0, 0, 0, 0, '', 0, 20),
(9, '0', 6, 'The Dirty Duck Pub', 'the_dirty_duck_pub', 'pub_a', 'hh_room_pub', 0, 0, 0, 0, 0, '', 0, 40),
(10, '0', 6, 'Cafe Ole', 'cafe_ole', 'cafe_ole', 'hh_room_cafe', 0, 0, 0, 0, 0, '', 0, 35),
(11, '0', 6, 'Gallery Cafe', 'eric\'s_eaterie', 'cr_cafe', 'hh_room_erics', 0, 0, 0, 0, 0, '', 0, 35),
(12, '0', 6, 'Space Cafe', 'space_cafe', 'space_cafe', 'hh_room_space_cafe', 0, 0, 0, 0, 0, '', 0, 35),
(13, '0', 7, 'Rooftop Terrace', 'rooftop', 'rooftop', 'hh_room_rooftop', 0, 0, 0, 0, 0, '', 0, 30),
(14, '0', 7, 'Rooftop Cafe', 'rooftop', 'rooftop_2', 'hh_room_rooftop', 0, 0, 0, 0, 0, '', 0, 20),
(15, '0', 6, 'Palazzo Pizza', 'pizza', 'pizza', 'hh_room_pizza', 0, 0, 0, 0, 0, '', 0, 40),
(16, '0', 6, 'Habburgers', 'habburger\'s', 'habburger', 'hh_room_habburger', 0, 0, 0, 0, 0, '', 0, 40),
(17, '0', 8, 'Grandfathers Lounge', 'dusty_lounge', 'dusty_lounge', 'hh_room_dustylounge', 0, 0, 0, 0, 0, '', 0, 45),
(18, '0', 7, 'Oriental Tearoom', 'tearoom', 'tearoom', 'hh_room_tearoom', 0, 0, 0, 0, 0, '', 0, 40),
(19, '0', 7, 'Oldskool Lounge', 'old_skool', 'old_skool0', 'hh_room_old_skool', 0, 0, 0, 0, 0, '', 0, 45),
(20, '0', 7, 'Oldskool Dancefloor', 'old_skool', 'old_skool1', 'hh_room_old_skool', 0, 0, 0, 0, 0, '', 0, 45),
(21, '0', 7, 'The Chromide Club', 'the_chromide_club', 'malja_bar_a', 'hh_room_disco', 0, 0, 0, 0, 0, '', 0, 45),
(22, '0', 7, 'The Chromide Club II', 'the_chromide_club', 'malja_bar_b', 'hh_room_disco', 0, 0, 0, 0, 0, '', 0, 50),
(23, '0', 7, 'Club Massiva', 'club_massiva', 'bar_a', 'hh_room_bar', 0, 0, 0, 0, 0, '', 0, 45),
(24, '0', 7, 'Club Massiva II', 'club_massiva2', 'bar_b', 'hh_room_bar', 0, 0, 0, 0, 0, '', 0, 70),
(25, '0', 6, 'Sunset Cafe', 'sunset_cafe', 'sunset_cafe', 'hh_room_sunsetcafe', 0, 0, 0, 0, 0, '', 0, 35),
(26, '0', 7, 'Oasis Spa', 'cafe_gold', 'cafe_gold0', 'hh_room_gold', 0, 0, 0, 0, 0, '', 0, 50),
(27, '0', 9, 'Treehugger Garden', 'chill', 'chill', 'hh_room_chill', 0, 0, 0, 0, 0, '', 0, 30),
(28, '0', 8, 'Club Mammoth', 'club_mammoth', 'club_mammoth', 'hh_room_clubmammoth', 0, 0, 0, 0, 0, '', 0, 45),
(29, '0', 9, 'Floating Garden', 'floatinggarden', 'floatinggarden', 'hh_room_floatinggarden', 0, 0, 0, 0, 0, '', 0, 80),
(30, '0', 9, 'Picnic Fields', 'picnic', 'picnic', 'hh_room_picnic', 0, 0, 0, 0, 0, '', 0, 55),
(31, '0', 9, 'Sun Terrace', 'sun_terrace', 'sun_terrace', 'hh_room_sun_terrace', 0, 0, 0, 0, 0, '', 0, 50),
(32, '0', 20, 'The Laughing Lions - Gate', 'gate_park', 'gate_park', 'hh_room_gate_park', 0, 0, 0, 0, 0, '', 0, 50),
(33, '0', 20, 'The Laughing Lions - Picnic', 'gate_park', 'gate_park_2', 'hh_room_gate_park', 0, 0, 0, 0, 0, '', 0, 50),
(34, '0', 21, 'The Park', 'park', 'park_a', 'hh_room_park_general,hh_room_park', 0, 0, 0, 0, 0, '', 0, 45),
(35, '0', 21, 'The Infobus', 'park', 'park_b', 'hh_room_park_general,hh_room_park', 0, 0, 0, 0, 0, '', 0, 20),
(36, '0', 10, 'Habbo Lido', 'habbo_lido', 'pool_a', 'hh_room_pool,hh_people_pool', 0, 0, 0, 0, 0, '', 0, 60),
(37, '0', 10, 'Lido B', 'habbo_lido_ii', 'pool_b', 'hh_room_pool,hh_people_pool', 0, 0, 0, 0, 0, '', 0, 60),
(38, '0', 10, 'Rooftop Rumble', 'rooftop_rumble', 'md_a', 'hh_room_terrace,hh_paalu,hh_people_pool,hh_people_paalu', 0, 0, 0, 0, 0, '', 0, 50),
(39, '0', 11, 'Main Lobby', 'main_lobby', 'lobby_a', 'hh_room_lobby', 0, 0, 0, 0, 0, '', 0, 100),
(40, '0', 11, 'Basement Lobby', 'basement_lobby', 'floorlobby_a', 'hh_room_floorlobbies', 0, 0, 0, 0, 0, '', 0, 50),
(41, '0', 11, 'Median Lobby', 'median_lobby', 'floorlobby_b', 'hh_room_floorlobbies', 0, 0, 0, 0, 0, '', 0, 50),
(42, '0', 11, 'Skylight Lobby', 'skylight_lobby', 'floorlobby_c', 'hh_room_floorlobbies', 0, 0, 0, 0, 0, '', 0, 50),
(43, '0', 6, 'Ice Cafe', 'ice_cafe', 'ice_cafe', 'hh_room_icecafe', 0, 0, 0, 0, 0, '', 0, 25),
(44, '0', 6, 'Net Cafe', 'netcafe', 'netcafe', 'hh_room_netcafe', 0, 0, 0, 0, 0, '', 0, 25),
(45, '0', 5, 'Beauty Salon', 'beauty_salon_loreal', 'beauty_salon0', 'hh_room_beauty_salon_general', 0, 0, 0, 0, 0, '', 0, 25),
(46, '0', 5, 'The Den', 'the_den', 'cr_staff', 'hh_room_den', 0, 0, 0, 0, 0, '', 0, 100),
(47, '0', 12, 'Lower Hallways', 'hallway', 'hallway2', 'hh_room_hallway', 0, 0, 1, 0, 0, '', 0, 25),
(48, '0', 12, 'Lower Hallways I', 'hallway', 'hallway0', 'hh_room_hallway', 0, 0, 1, 0, 0, '', 0, 25),
(49, '0', 12, 'Lower Hallways II', 'hallway', 'hallway1', 'hh_room_hallway', 0, 0, 1, 0, 0, '', 0, 25),
(50, '0', 12, 'Lower Hallways III', 'hallway', 'hallway3', 'hh_room_hallway', 0, 0, 1, 0, 0, '', 0, 25),
(51, '0', 12, 'Lower Hallways IV', 'hallway', 'hallway5', 'hh_room_hallway', 0, 0, 1, 0, 0, '', 0, 25),
(52, '0', 12, 'Lower Hallways V', 'hallway', 'hallway4', 'hh_room_hallway', 0, 0, 1, 0, 0, '', 0, 25),
(53, '0', 12, 'Upper Hallways', 'hallway_ii', 'hallway9', 'hh_room_hallway', 0, 0, 1, 0, 0, '', 0, 25),
(54, '0', 12, 'Upper Hallways I', 'hallway_ii', 'hallway8', 'hh_room_hallway', 0, 0, 1, 0, 0, '', 0, 25),
(55, '0', 12, 'Upper Hallways II', 'hallway_ii', 'hallway7', 'hh_room_hallway', 0, 0, 1, 0, 0, '', 0, 25),
(56, '0', 12, 'Upper Hallways III', 'hallway_ii', 'hallway6', 'hh_room_hallway', 0, 0, 1, 0, 0, '', 0, 25),
(57, '0', 12, 'Upper Hallways IV', 'hallway_ii', 'hallway10', 'hh_room_hallway', 0, 0, 1, 0, 0, '', 0, 25),
(58, '0', 12, 'Upper Hallways V', 'hallway_ii', 'hallway11', 'hh_room_hallway', 0, 0, 1, 0, 0, '', 1, 25),
(59, '0', 7, 'Star Lounge', 'star_lounge', 'star_lounge', 'hh_room_starlounge', 0, 0, 1, 0, 0, '', 0, 35),
(60, '0', 8, 'Club Orient', 'orient', 'orient', 'hh_room_orient', 0, 0, 1, 0, 0, '', 1, 35);

-- --------------------------------------------------------

--
-- Table structure for table `rooms_categories`
--

CREATE TABLE `rooms_categories` (
  `id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `parent_id` int(11) NOT NULL,
  `isnode` int(11) DEFAULT 0,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `public_spaces` int(11) DEFAULT 0,
  `allow_trading` int(11) DEFAULT 0,
  `minrole_access` int(11) DEFAULT 1,
  `minrole_setflatcat` int(11) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `rooms_categories`
--

INSERT INTO `rooms_categories` (`id`, `order_id`, `parent_id`, `isnode`, `name`, `public_spaces`, `allow_trading`, `minrole_access`, `minrole_setflatcat`) VALUES
(2, 0, 0, 0, 'No category', 0, 0, 1, 1),
(3, 0, 0, 1, 'Public Rooms', 1, 0, 1, 6),
(4, 0, 0, 1, 'Guest Rooms', 0, 0, 1, 6),
(5, 0, 3, 0, 'Entertainment', 1, 0, 1, 6),
(6, 0, 3, 0, 'Restaurants and Cafes', 1, 0, 1, 6),
(7, 0, 3, 0, 'Lounges and Clubs', 1, 0, 1, 6),
(8, 0, 3, 0, 'Club-only Spaces', 1, 0, 1, 6),
(9, 0, 3, 0, 'Parks and Gardens', 1, 0, 1, 6),
(10, 0, 3, 0, 'Swimming Pools', 1, 0, 1, 6),
(11, 0, 3, 0, 'The Lobbies', 1, 0, 1, 6),
(12, -1, 3, 0, 'The Hallways', 1, 0, 1, 6),
(20, 0, 9, 0, 'The Laughing Lions Park', 1, 0, 1, 6),
(21, 0, 9, 0, 'The Green Heart', 1, 0, 1, 6),
(101, 0, 4, 0, 'Staff HQ', 0, 1, 4, 5),
(112, 0, 4, 0, 'Restaurant, Bar & Night Club Rooms', 0, 0, 1, 1),
(113, 0, 4, 0, 'Trade floor', 0, 1, 1, 1),
(114, 0, 4, 0, 'Chill, Chat & Discussion Rooms', 0, 0, 1, 1),
(115, 0, 4, 0, 'Hair Salons & Modelling Rooms', 0, 0, 1, 1),
(116, 0, 4, 0, 'Maze & Theme Park Rooms', 0, 0, 1, 1),
(117, 0, 4, 0, 'Gaming & Race Rooms', 0, 0, 1, 1),
(118, 0, 4, 0, 'Help Centre Rooms', 0, 0, 1, 1),
(120, 0, 4, 0, 'Miscellaneous', 0, 0, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `rooms_models`
--

CREATE TABLE `rooms_models` (
  `id` int(11) NOT NULL,
  `model_id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `model_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `door_x` int(11) NOT NULL DEFAULT 0,
  `door_y` int(11) NOT NULL DEFAULT 0,
  `door_z` double NOT NULL,
  `door_dir` int(11) DEFAULT 2,
  `heightmap` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `usertype` int(11) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `rooms_models`
--

INSERT INTO `rooms_models` (`id`, `model_id`, `model_name`, `door_x`, `door_y`, `door_z`, `door_dir`, `heightmap`, `usertype`) VALUES
(1, 'model_a', 'model_a', 3, 5, 0, 2, 'xxxxxxxxxxxx|xxxx00000000|xxxx00000000|xxxx00000000|xxxx00000000|xxxx00000000|xxxx00000000|xxxx00000000|xxxx00000000|xxxx00000000|xxxx00000000|xxxx00000000|xxxx00000000|xxxx00000000|xxxxxxxxxxxx|xxxxxxxxxxxx', 1),
(2, 'model_b', 'model_b', 0, 5, 0, 2, 'xxxxxxxxxxxx|xxxxx0000000|xxxxx0000000|xxxxx0000000|xxxxx0000000|x00000000000|x00000000000|x00000000000|x00000000000|x00000000000|x00000000000|xxxxxxxxxxxx|xxxxxxxxxxxx|xxxxxxxxxxxx|xxxxxxxxxxxx|xxxxxxxxxxxx', 1),
(3, 'model_c', 'model_c', 4, 7, 0, 2, 'xxxxxxxxxxxx|xxxxxxxxxxxx|xxxxxxxxxxxx|xxxxxxxxxxxx|xxxxxxxxxxxx|xxxxx000000x|xxxxx000000x|xxxxx000000x|xxxxx000000x|xxxxx000000x|xxxxx000000x|xxxxxxxxxxxx|xxxxxxxxxxxx|xxxxxxxxxxxx|xxxxxxxxxxxx|xxxxxxxxxxxx', 1),
(4, 'model_d', 'model_d', 4, 7, 0, 2, 'xxxxxxxxxxxx|xxxxx000000x|xxxxx000000x|xxxxx000000x|xxxxx000000x|xxxxx000000x|xxxxx000000x|xxxxx000000x|xxxxx000000x|xxxxx000000x|xxxxx000000x|xxxxx000000x|xxxxx000000x|xxxxx000000x|xxxxx000000x|xxxxxxxxxxxx', 1),
(5, 'model_e', 'model_e', 1, 5, 0, 2, 'xxxxxxxxxxxx|xxxxxxxxxxxx|xxxxxxxxxxxx|xx0000000000|xx0000000000|xx0000000000|xx0000000000|xx0000000000|xx0000000000|xx0000000000|xx0000000000|xxxxxxxxxxxx|xxxxxxxxxxxx|xxxxxxxxxxxx|xxxxxxxxxxxx|xxxxxxxxxxxx', 1),
(6, 'model_f', 'model_f', 2, 5, 0, 2, 'xxxxxxxxxxxx|xxxxxxx0000x|xxxxxxx0000x|xxx00000000x|xxx00000000x|xxx00000000x|xxx00000000x|x0000000000x|x0000000000x|x0000000000x|x0000000000x|xxxxxxxxxxxx|xxxxxxxxxxxx|xxxxxxxxxxxx|xxxxxxxxxxxx|xxxxxxxxxxxx', 1),
(7, 'model_g', 'model_g', 1, 7, 1, 2, 'xxxxxxxxxxxxx|xxxxxxxxxxxxx|xxxxxxx00000x|xxxxxxx00000x|xxxxxxx00000x|xx1111000000x|xx1111000000x|xx1111000000x|xx1111000000x|xx1111000000x|xxxxxxx00000x|xxxxxxx00000x|xxxxxxx00000x|xxxxxxxxxxxxx|xxxxxxxxxxxxx|xxxxxxxxxxxxx|xxxxxxxxxxxxx', 2),
(8, 'model_h', 'model_h', 4, 4, 1, 2, 'xxxxxxxxxxxx|xxxxxxxxxxxx|xxxxx111111x|xxxxx111111x|xxxxx111111x|xxxxx111111x|xxxxx111111x|xxxxx000000x|xxxxx000000x|xxx00000000x|xxx00000000x|xxx00000000x|xxx00000000x|xxxxxxxxxxxx|xxxxxxxxxxxx|xxxxxxxxxxxx', 2),
(9, 'model_i', 'model_i', 0, 10, 0, 2, 'xxxxxxxxxxxxxxxxx|x0000000000000000|x0000000000000000|x0000000000000000|x0000000000000000|x0000000000000000|x0000000000000000|x0000000000000000|x0000000000000000|x0000000000000000|x0000000000000000|x0000000000000000|x0000000000000000|x0000000000000000|x0000000000000000|x0000000000000000|x0000000000000000|x0000000000000000|x0000000000000000|x0000000000000000|x0000000000000000|x0000000000000000|x0000000000000000|x0000000000000000|x0000000000000000|x0000000000000000|x0000000000000000|xxxxxxxxxxxxxxxxx', 1),
(10, 'model_j', 'model_j', 0, 10, 0, 2, 'xxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxx0000000000|xxxxxxxxxxx0000000000|xxxxxxxxxxx0000000000|xxxxxxxxxxx0000000000|xxxxxxxxxxx0000000000|xxxxxxxxxxx0000000000|x00000000000000000000|x00000000000000000000|x00000000000000000000|x00000000000000000000|x00000000000000000000|x00000000000000000000|x00000000000000000000|x00000000000000000000|x00000000000000000000|x00000000000000000000|x0000000000xxxxxxxxxx|x0000000000xxxxxxxxxx|x0000000000xxxxxxxxxx|x0000000000xxxxxxxxxx|x0000000000xxxxxxxxxx|x0000000000xxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxx', 1),
(11, 'model_k', 'model_k', 0, 13, 0, 2, 'xxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxx00000000|xxxxxxxxxxxxxxxxx00000000|xxxxxxxxxxxxxxxxx00000000|xxxxxxxxxxxxxxxxx00000000|xxxxxxxxx0000000000000000|xxxxxxxxx0000000000000000|xxxxxxxxx0000000000000000|xxxxxxxxx0000000000000000|x000000000000000000000000|x000000000000000000000000|x000000000000000000000000|x000000000000000000000000|x000000000000000000000000|x000000000000000000000000|x000000000000000000000000|x000000000000000000000000|xxxxxxxxx0000000000000000|xxxxxxxxx0000000000000000|xxxxxxxxx0000000000000000|xxxxxxxxx0000000000000000|xxxxxxxxx0000000000000000|xxxxxxxxx0000000000000000|xxxxxxxxx0000000000000000|xxxxxxxxx0000000000000000|xxxxxxxxx0000000000000000|xxxxxxxxx0000000000000000|xxxxxxxxxxxxxxxxxxxxxxxxx', 1),
(12, 'model_l', 'model_l', 0, 16, 0, 2, 'xxxxxxxxxxxxxxxxxxxxx|x00000000000000000000|x00000000000000000000|x00000000000000000000|x00000000000000000000|x00000000000000000000|x00000000000000000000|x00000000000000000000|x00000000000000000000|x00000000xxxx00000000|x00000000xxxx00000000|x00000000xxxx00000000|x00000000xxxx00000000|x00000000xxxx00000000|x00000000xxxx00000000|x00000000xxxx00000000|x00000000xxxx00000000|x00000000xxxx00000000|x00000000xxxx00000000|x00000000xxxx00000000|x00000000xxxx00000000|xxxxxxxxxxxxxxxxxxxxx', 1),
(13, 'model_m', 'model_m', 0, 15, 0, 2, 'xxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxx00000000xxxxxxxxxx|xxxxxxxxxxx00000000xxxxxxxxxx|xxxxxxxxxxx00000000xxxxxxxxxx|xxxxxxxxxxx00000000xxxxxxxxxx|xxxxxxxxxxx00000000xxxxxxxxxx|xxxxxxxxxxx00000000xxxxxxxxxx|xxxxxxxxxxx00000000xxxxxxxxxx|xxxxxxxxxxx00000000xxxxxxxxxx|xxxxxxxxxxx00000000xxxxxxxxxx|xxxxxxxxxxx00000000xxxxxxxxxx|x0000000000000000000000000000|x0000000000000000000000000000|x0000000000000000000000000000|x0000000000000000000000000000|x0000000000000000000000000000|x0000000000000000000000000000|x0000000000000000000000000000|x0000000000000000000000000000|xxxxxxxxxxx00000000xxxxxxxxxx|xxxxxxxxxxx00000000xxxxxxxxxx|xxxxxxxxxxx00000000xxxxxxxxxx|xxxxxxxxxxx00000000xxxxxxxxxx|xxxxxxxxxxx00000000xxxxxxxxxx|xxxxxxxxxxx00000000xxxxxxxxxx|xxxxxxxxxxx00000000xxxxxxxxxx|xxxxxxxxxxx00000000xxxxxxxxxx|xxxxxxxxxxx00000000xxxxxxxxxx|xxxxxxxxxxx00000000xxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 1),
(14, 'model_n', 'model_n', 0, 16, 0, 2, 'xxxxxxxxxxxxxxxxxxxxx|x00000000000000000000|x00000000000000000000|x00000000000000000000|x00000000000000000000|x00000000000000000000|x00000000000000000000|x000000xxxxxxxx000000|x000000x000000x000000|x000000x000000x000000|x000000x000000x000000|x000000x000000x000000|x000000x000000x000000|x000000x000000x000000|x000000xxxxxxxx000000|x00000000000000000000|x00000000000000000000|x00000000000000000000|x00000000000000000000|x00000000000000000000|x00000000000000000000|xxxxxxxxxxxxxxxxxxxxx', 1),
(15, 'model_o', 'model_o', 0, 18, 1, 2, 'xxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxx11111111xxxx|xxxxxxxxxxxxx11111111xxxx|xxxxxxxxxxxxx11111111xxxx|xxxxxxxxxxxxx11111111xxxx|xxxxxxxxxxxxx11111111xxxx|xxxxxxxxxxxxx11111111xxxx|xxxxxxxxxxxxx11111111xxxx|xxxxxxxxxxxxx00000000xxxx|xxxxxxxxx0000000000000000|xxxxxxxxx0000000000000000|xxxxxxxxx0000000000000000|xxxxxxxxx0000000000000000|xxxxxxxxx0000000000000000|xxxxxxxxx0000000000000000|x111111100000000000000000|x111111100000000000000000|x111111100000000000000000|x111111100000000000000000|x111111100000000000000000|x111111100000000000000000|x111111100000000000000000|x111111100000000000000000|xxxxxxxxx0000000000000000|xxxxxxxxx0000000000000000|xxxxxxxxx0000000000000000|xxxxxxxxx0000000000000000|xxxxxxxxxxxxxxxxxxxxxxxxx', 2),
(16, 'model_p', 'model_p', 0, 23, 2, 2, 'xxxxxxxxxxxxxxxxxxx|xxxxxxx222222222222|xxxxxxx222222222222|xxxxxxx222222222222|xxxxxxx222222222222|xxxxxxx222222222222|xxxxxxx222222222222|xxxxxxx22222222xxxx|xxxxxxx11111111xxxx|x222221111111111111|x222221111111111111|x222221111111111111|x222221111111111111|x222221111111111111|x222221111111111111|x222221111111111111|x222221111111111111|x2222xx11111111xxxx|x2222xx00000000xxxx|x2222xx000000000000|x2222xx000000000000|x2222xx000000000000|x2222xx000000000000|x2222xx000000000000|x2222xx000000000000|xxxxxxxxxxxxxxxxxxx', 2),
(17, 'model_q', 'model_q', 10, 4, 2, 2, 'xxxxxxxxxxxxxxxxxxx|xxxxxxxxxxx22222222|xxxxxxxxxxx22222222|xxxxxxxxxxx22222222|xxxxxxxxxxx22222222|xxxxxxxxxxx22222222|xxxxxxxxxxx22222222|x222222222222222222|x222222222222222222|x222222222222222222|x222222222222222222|x222222222222222222|x222222222222222222|x2222xxxxxxxxxxxxxx|x2222xxxxxxxxxxxxxx|x2222211111xx000000|x222221111110000000|x222221111110000000|x2222211111xx000000|xx22xxx1111xxxxxxxx|xx11xxx1111xxxxxxxx|x1111xx1111xx000000|x1111xx111110000000|x1111xx111110000000|x1111xx1111xx000000|xxxxxxxxxxxxxxxxxxx', 2),
(18, 'model_r', 'model_r', 10, 4, 3, 2, 'xxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxx33333333333333|xxxxxxxxxxx33333333333333|xxxxxxxxxxx33333333333333|xxxxxxxxxxx33333333333333|xxxxxxxxxxx33333333333333|xxxxxxxxxxx33333333333333|xxxxxxx333333333333333333|xxxxxxx333333333333333333|xxxxxxx333333333333333333|xxxxxxx333333333333333333|xxxxxxx333333333333333333|xxxxxxx333333333333333333|x4444433333xxxxxxxxxxxxxx|x4444433333xxxxxxxxxxxxxx|x44444333333222xx000000xx|x44444333333222xx000000xx|xxx44xxxxxxxx22xx000000xx|xxx33xxxxxxxx11xx000000xx|xxx33322222211110000000xx|xxx33322222211110000000xx|xxxxxxxxxxxxxxxxx000000xx|xxxxxxxxxxxxxxxxx000000xx|xxxxxxxxxxxxxxxxx000000xx|xxxxxxxxxxxxxxxxx000000xx|xxxxxxxxxxxxxxxxxxxxxxxxx', 2),
(19, 'newbie_lobby', 'newbie_lobby', 2, 11, 0, 2, 'xxxxxxxxxxxxxxxx000000|xxxxx0xxxxxxxxxx000000|xxxxx00000000xxx000000|xxxxx000000000xx000000|0000000000000000000000|0000000000000000000000|0000000000000000000000|0000000000000000000000|0000000000000000000000|xxxxx000000000000000xx|xxxxx000000000000000xx|x0000000000000000000xx|x0000000000000000xxxxx|xxxxxx00000000000xxxxx|xxxxxxx0000000000xxxxx|xxxxxxxx000000000xxxxx|xxxxxxxx000000000xxxxx|xxxxxxxx000000000xxxxx|xxxxxxxx000000000xxxxx|xxxxxxxx000000000xxxxx|xxxxxxxx000000000xxxxx|xxxxxx00000000000xxxxx|xxxxxx00000000000xxxxx|xxxxxx00000000000xxxxx|xxxxxx00000000000xxxxx|xxxxxx00000000000xxxxx|xxxxx000000000000xxxxx|xxxxx000000000000xxxxx', 0),
(20, 'theater', 'theater', 20, 27, 0, 0, 'XXXXXXXXXXXXXXXXXXXXXXX|XXXXXXXXXXXXXXXXXXXXXXX|XXXXXXXXXXXXXXXXXXXXXXX|XXXXXXXXXXXXXXXXXXXXXXX|XXXXXXXXXXXXXXXXXXXXXXX|XXXXXXXXXXXXXXXXXXXXXXX|XXXXXXX111111111XXXXXXX|XXXXXXX11111111100000XX|XXXX00X11111111100000XX|XXXX00x11111111100000XX|4XXX00X11111111100000XX|4440000XXXXXXXXX00000XX|444000000000000000000XX|4XX000000000000000000XX|4XX0000000000000000000X|44400000000000000000000|44400000000000000000000|44X0000000000000000O000|44X11111111111111111000|44X11111111111111111000|33X11111111111111111000|22X11111111111111111000|22X11111111111111111000|22X11111111111111111000|22X11111111111111111000|22X11111111111111111000|22211111111111111111000|22211111111111111111000|XXXXXXXXXXXXXXXXXXXX00X|XXXXXXXXXXXXXXXXXXXX00X', 0),
(21, 'library', 'library', 20, 3, 1, 4, 'xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxx11111xx1xx1x111111x|xxxxxxxxxxxx111111xx1xx11111111x|xx111xxxxxxx111111xx1xx11111111x|xx111xxxxxxx1111111111111111111x|xx111xxxxxxx1111111111111111111x|xx111xxxxxxx1111111111111111111x|xx111xxxxxxx1111111111111xxxxxxx|xx111xxxxxx11111111111111111111x|xx111xxxxxx11111111111111111111x|xx111xxxxxx11111111111111111111x|xx111xxxxxx11111111111111xxxxxxx|xx111xxxxxxxx111111111111111111x|xx111xx11111x111111111111111111x|xx111xx11111x111111111111111111x|xx111xxxxx11x11111111x111xxxxxxx|xx111xxxxxxxx11111111xx11111111x|xx111xxx1111111111111xxx1111111x|xx111xxx1111111111111xxxx111111x|xxx111xx1111111111x11xxxx000000x|xxxxx1111xx1111111x11xxxx000000x|xxxxxxxxxxxx111111x11xxxx000000x|xxxxxxxxxxxx11xx11x11xxxx000000x|xxxxxxxxxxxx11xx11x11xxxx000000x|xxxxxxxxxxxx11xx11x11xxxx000000x|xxxxxxxxxxxx11xx11x11xxxx000000x|xxxxxxxxxxxx11xx11x11xxxx000000x|xxxxxxxxxxxx11xx11x111xxx000000x|xxxxxxxxxxxxxxxxxxxx11xxx000000x|xxxxxxxxxxxxxxxxxxxx11xxxxxxxxxx|xxxxxxxxxxxxxxxxxxxx22222xxxxxxx|xxxxxxxxxxxxxxxxxxxx22222xxxxxxx|xxxxxxxxxxxxxxxxxxxx22222xxxxxxx|xxxxxxxxxxxxxxxxxxxx22222xxxxxxx|xxxxxxxxxxxxxxxxxxxx22222xxxxxxx|xxxxxxxxxxxxxxxxxxxx22222xxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 0),
(22, 'floatinggarden', 'floatinggarden', 2, 21, 5, 4, 'xxxxxxxxxxxxxxxx333333xxxxxxxxx|xxxxxxxxxxxxxxxx3xxxx3xxxxxxxxx|xxxxxxxxxxxxxxxx3xxxx3xxxxxxxxx|xxxxxxxxxxxxxxxx3xxxx3xxxxxxxxx|xxxxxxxxxxxxxxx223xxx33xxxxxxxx|xxxxxxxxxxxxxxx11xxx33333xxxxxx|xxxxxxxxxxxxxxxx11xx3333333xxxx|xxxxxxxxxxxxxxxx11xx33333333xxx|xxxxxxxxxxxxxxxxx11xxxxxxxx3xxx|xxxxxxxxxxxxxxxxxx11xxxx3333xxx|xxxxxxxxxxxxxxxxxxx1xxxx33333xx|xxxxxxxxxxxxxxxxxxx1xxx3333333x|555xxxxxxxxxxx1111111x333333333|555xxxxxxxxxxx21111111xxxxxx333|555xxxxxxxxxxx22111111111xxxxxx|555xxxxxxxxxxx222xxxxxxx111xxxx|555xxxxxxxxxxx22xxxxxxxxxx1xxxx|555xxxxxxxxxxx23333333333x111xx|555xxxxxxxx33333333333333x111xx|555xxxxxxxx333333x3333333x111xx|555xxxxxxxx33333333333333x111xx|555xxxxxxxx33x33333333333x111xx|555xxxxxxxx33x33x33333333x111xx|555xxxxxxxx33x33x33333333x111xx|5554333333333x333x3333333x111xx|x554333333xxxx33xxxxxxxxxx111xx|xxxxxxxxx3xxxx333221111111111xx|xxxxxxxxx3xxxx333221111111111xx|xxxxxxxxx33333333xx1111x11x11xx|xxxxxxxxx33333333111xxx11xxxxxx|xxxxxxxxxxxxxx33311xxxx11xxxxxx|xxxxxxxxxxxxxx33311xxxx11xxxxxx|xxxxxxxxxxxxxx333x1xxxx11xxxxxx|xxxxxxxxxxxxxx333x1xx111111xxxx|xxxxxxxxxxxxxx33311xx111111xxxx|xxxxxxxxxx333333311xx111111xxxx|xxxxxxxxxxx33333311xx111111xxxx|xxxxxxxxxxxxxxxx111xxxxxxxxxxxx|xxxxxxxxxxxxxxx111xxxxxxxxxxxxx', 0),
(23, 'sunset_cafe', 'sunset_cafe', 34, 40, 0, 0, 'xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx000000xxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0000000xxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxx00000xx00000000xxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxx000000000000000000xxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxx000000000000000000xxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxx000000000000000000xxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxx000000000000000000xxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxx00000000xxx0000000xxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxx00000000xxxx000000xxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxx00000000xxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxx0000000000000000xxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxx00000000000000000xxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxx00000000000000000xxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxx00000000000000000xxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxx00000000000000000xxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxx00000000000000000xxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx00000000xxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0000000xxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0000xxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0000xxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0000xxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 0),
(24, 'pool_a', 'pool_a', 2, 25, 7, 2, 'xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxx7xxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxx777xxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxx7777777xxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxx77777777xxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxx77777777xxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxx777777777xxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxx7xxx777777xxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxx7x777777777xxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxx7xxx77777777xxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxx7x777777777x7xxxxxxxxxxxxxxx|xxxxxxxxxxxxxxx7xxx7777777777xxxxxxxxxxxxxx|xxxxxxxxxxxxxxx777777777777xxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxx77777777777x2111xxxxxxxxxxxx|xxxxxxxxxxxxxxx7777777777x221111xxxxxxxxxxx|xxxxxxxxx7777777777777777x2211111xxxxxxxxxx|xxxxxxxxx7777777777777777x22211111xxxxxxxxx|xxxxxxxxx7777777777777777x222211111xxxxxxxx|xxxxxx77777777777777777777x222211111xxxxxxx|xxxxxx7777777xx777777777777x222211111xxxxxx|xxxxxx7777777xx77777777777772222111111xxxxx|xxxxxx777777777777777777777x22221111111xxxx|xx7777777777777777777777x322222211111111xxx|77777777777777777777777x33222222111111111xx|7777777777777777777777x333222222211111111xx|xx7777777777777777777x333322222222111111xxx|xx7777777777777777777333332222222221111xxxx|xx777xxx777777777777733333222222222211xxxxx|xx777x7x77777777777773333322222222222xxxxxx|xx777x7x7777777777777x33332222222222xxxxxxx|xxx77x7x7777777777777xx333222222222xxxxxxxx|xxxx77777777777777777xxx3222222222xxxxxxxxx|xxxxx777777777777777777xx22222222xxxxxxxxxx|xxxxxx777777777777777777x2222222xxxxxxxxxxx|xxxxxxx777777777777777777222222xxxxxxxxxxxx|xxxxxxxx7777777777777777722222xxxxxxxxxxxxx|xxxxxxxxx77777777777777772222xxxxxxxxxxxxxx|xxxxxxxxxx777777777777777222xxxxxxxxxxxxxxx|xxxxxxxxxxx77777777777777x2xxxxxxxxxxxxxxxx|xxxxxxxxxxxx77777777777777xxxxxxxxxxxxxxxxx|xxxxxxxxxxxxx777777777777xxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxx7777777777xxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxx77777777xxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxx777777xxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxx7777xxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxx77xxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 0),
(25, 'pub_a', 'pub_a', 15, 25, 0, 0, 'xxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxx2222222211111xxx|xxxxxxxxx2222222211111xxx|xxxxxxxxx2222222211111xxx|xxxxxxxxx2222222211111xxx|xxxxxxxxx2222222222111xxx|xxxxxxxxx2222222222111xxx|xxxxxxxxx2222222222000xxx|xxxxxxxxx2222222222000xxx|xxxxxxxxx2222222222000xxx|xxxxxxxxx2222222222000xxx|x333333332222222222000xxx|x333333332222222222000xxx|x333333332222222222000xxx|x333333332222222222000xxx|x333333332222222222000xxx|x333332222222222222000xxx|x333332222222222222000xxx|x333332222222222222000xxx|x333332222222222222000xxx|x333333332222222222000xxx|xxxxx31111112222222000xxx|xxxxx31111111000000000xxx|xxxxx31111111000000000xxx|xxxxx31111111000000000xxx|xxxxx31111111000000000xxx|xxxxxxxxxxxxxxx00xxxxxxxx|xxxxxxxxxxxxxxx00xxxxxxxx|xxxxxxxxxxxxxxx00xxxxxxxx|xxxxxxxxxxxxxxx00xxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxx', 0),
(26, 'md_a', 'md_a', 3, 4, 7, 2, 'xxxxxxxx77xxxxxxxxxxxxxxxx|xxxxxxxx77xxxxxxxxxxxxxxxx|xxxxxx77777x77xxxxxxxxxxxx|xxx77777777777xxx44xxxxxxx|77777777777777xx444444444x|777777777777777xx44444444x|xxx777777777777xx44444444x|xxxx7777777777xxx44444444x|7777777777777777744448444x|7777777777777x4x744448444x|777777777777x444444448444x|7777777777774444444448444x|7777777777774444444448444x|777777777777x444444448444x|7777777777777x44444448444x|xxx777777777777x444448444x|xxx7777777777777444448444x|xxx7777777777777444448444x|xxx777777777777x444448444x|xxx77777777777x4444444444x|xxxx777777777444444444444x|xxxxxxxxxxxxxxxxxxxxxxxxxx', 0),
(27, 'picnic', 'picnic', 16, 5, 2, 4, 'xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xx22222xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|22xxxxxxxxxxxxx22xxxxxxxxxxxxxxxxxxxxx|2222222222222222222x222222222222222222|22222222222222222222222222222222222222|22222222222222222222222222222222222222|22222222222222222222222222222222222222|22222222222xxx222222222222222222222222|2222222222xx33x22222222222222222222222|222222222xx3333x2222222222222222222222|222222222x333333x222222222222222222222|222222222x333333x222222222222222222222|2222222222x3332x2222222222222222222222|22222222222x33x22222222222222222222222|22222222222222222222222222222222222222|22222222222222222222222222222222222222|22222222222222222222222222222222222222|22222222222222222222222222222222222222|22222222222222222222222222222222222222|22222222222222222222222222222222222222|22222222x22222xxxx22222222222222222222|22222222222222xxxx22222222222222222222|22222222222222xxx22222222X222222222222|22222222222222222222222222222222222222|22222222222222222222222222222222222222|22222222222222222222222222222222222222|22222222222222222222222222222222222222|22222222222222222222222222222222222222|22222222222222222222222222222222222222|22222222222222222222222222222222222222|22222222222222222222222222222222222222|22222222222222222222222222222222222222|22222222222222222222222222222222222222|22222222222222222222222222222222222222|22222222222222222222222222222222222222|22222222222222222222222222222222222222|22222222222222222222222222222222222222|22222222222222222222222222222222222222|22222222222222222222222222222222222222|22222222222222222222222222222222222222|22222222222222222222222222222222222222', 0),
(28, 'park_a', 'park_a', 2, 15, 0, 0, 'xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx00xxxxxxxxxxxx|xxxxxxxxxxxxx0x00xxxxxxxxxxx0x000xxxxxxxxxxx|xxxxxxxxxxxx0000000000000000000000xxxxxxxxxx|xxxxxxxxxxx000000000000000000000000xxxxxxxxx|xxxxxxxxxxx0000000000000000000000000xxxxxxxx|xxxxxxxxxxx00000000000000000000000000xxxxxxx|xxxxxxxx000000000000000000000000000000xxxxxx|xxxxxxx00000000000000000000000000000000xxxxx|xxxxxxx000000000000000000000000000000000xxxx|xxxxxxx0000000000000000000000000000000000xxx|xxxxxxxxx000000000000000000000000000000000xx|00000000000000000000xx00000000000000000000xx|0000000000000000000xxxx00000000000xxxxxxx0xx|0000000000000000000xxxx00000000000x00000xxxx|xxxxx00x0000000000xxxxx0xxxxxx0000x0000000xx|xxxxx0000000000000xxxxx0xx000x0000x000000xxx|xxxxx0000000000000xxxxx0x000000000x00000xxxx|xxxxx000000x0000000xxxx0x000000000xxx00xxxxx|xxxxxxxx000x0000000xxx00xxx000000x0000xxxxxx|xxxxxxxx000x000000xxxx0x0000000000000xxxxxxx|xxxxxxxx000x000000011100000000000000xxxxxxxx|xxxxxxxx000x00000001110000000000000xxxxxxxxx|xxxxxxxxx00x0000000111x00000000x00xxxxxxxxxx|xxxxxxxxxx0x0000000xxx0000000xxxxxxxxxxxxxxx|xxxxxxxxxxxx000000xxxx0000000xxxxxxxxxxxxxxx|xxxxxxxxxxxx000000xxx00xxxxx00xxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxx0xxx0xx000x00xxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxx0xxx0x000000xxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxx0xxx0x00000xxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxx0xxxxx00xxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxx0xxxxx0xxxxxxxxxxxxxxxxxxxx', 0),
(29, 'park_b', 'park_b', 11, 2, 0, 6, '0000x0000000|0000xx000000|000000000000|00000000000x|000000000000|00x0000x0000', 0),
(30, 'pool_b', 'pool_b', 9, 21, 7, 1, 'xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx7xxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx777xxxxxxxxxxx|xxxxxxxxxxxxxxxxxx8888888x7xxx77777xxxxxxxxxx|xxxxxxxxxxxxxxxxxx8888888x7xxx777777xxxxxxxxx|xxxxxxxxxxxxxxxx88xxxxx77x7x777777777xxxxxxxx|xxxxxxxxxxxxxxxx88x7777777777777777777xxxxxxx|xxxxxxxxxxxxxxxx88x77777777777777777777xxxxxx|xxxxxxxxxxxxxx9988x77777777777777777777xxxxxx|xxxxxxxxxxxxxx9988x7777777777777777777x00xxxx|xxxxxxxxxxxxxx9988x777777777777777777x0000xxx|xxxxxxxxxxxxxx9988x7777777x0000000000000000xx|xxxxxxxxxxxxxx9988x777777x000000000000000000x|7777777777xxxx9988777777x0x0000000000000000xx|x7777777777xxx998877777x000x00000000000000xxx|xx7777777777xx99887777x00000x000000000000xxxx|xxx7777777777x9988777x0000000x0000000000xxxxx|xxxx777777777x777777x00000000x000000000xxxxxx|xxxxx777777777777777000000000x00000000xxxxxxx|xxxxxx77777777777777000000000x0000000xxxxxxxx|xxxxxxx777777x7777770000000000xxxx00xxxxxxxxx|xxxxxxxx77777777777xx0000000000000xxxxxxxxxxx|xxxxxxxxx777777110000x000000000000xxxxxxxxxxx|xxxxxxxxxx7x77x1100000x0000000000xxxxxxxxxxxx|xxxxxxxxxxx777x11000000x00000000xxxxxxxxxxxxx|xxxxxxxxxxxx771110000000x000000xxxxxxxxxxxxxx|xxxxxxxxxxxxx111100000000x0000xxxxxxxxxxxxxxx|xxxxxxxxxxxxxx11100000000x000xxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxx1100000000x00xxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxx110000000x0xxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxx110000000xxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxx1100000xxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxx11000xxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxx110xxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxx1xxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 0),
(31, 'ballroom', 'ballroom', 13, 6, 0, 4, 'xxxx4444444444444444444|xxxx4444444444444444444|xxxx4444444444444444444|xxxx33x2222444442222x33|xxxx2222222x00xx2222222|xxxx22222220000x2222222|xxxx11x0000x000x0000x11|xxxx0000000000000000000|11100000000000000000000|11100000000000000000000|11100000000000000000000|xxxx0000000000000000000|22210000000000000000000|22210000000000000000000|22210000000000000000000|xxxx0000000000000000000|11100000000000000000000|11100000000000000000000|11100000000000000000000|xxxxx000x11111111x0000x|xxxxxx00x1111x111x000xx|xxxxxxx0x11111111x00xxx|xxxxxxxxx11111111x0xxxx|xxxxxxxxx11111111xxxxxx', 0),
(32, 'cafe_gold0', 'cafe_gold0', 9, 29, 0, 0, 'xxxxxxxxxx1111xxxxxxx|xxxxxxxxxx11111xxxxxx|xxxxxxxxxx111111xxxxx|xx111111111111111xxxx|x11111111111111111xxx|1111111111111111111xx|11111111111111111111x|111111111111111111111|111111111111111111111|1111111111111x1111111|1111111000000x1111111|1111111000000x1111111|1111111000000x1111111|1111111000000x1111111|1111111000000x1111111|1111111000000x1111111|1111111000000x1111111|1111111000000x1111111|1111111000000x1111111|1111111000000xxx00000|111111100000000000000|111111100000000000000|111111100000000000000|111111100000000xxxxx0|11111110000000xxxxxx0|11111110000000xxxxxx0|11111110000000xxxxxxx|x1111110000000xxxxxxx|xxxxxxxx0000000000xxx|xxxxxxxx000xxxxxxxxxx|xxxxxxxx000xxxxxxxxxx|xxxxxxxx000xxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxx', 0),
(33, 'cafe', 'cafe', 30, 40, 0, 0, 'xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx000000xxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0000000xxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxx00000xx00000000xxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxx000000000000000000xxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxx000000000000000000xxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxx000000000000000000xxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxx000000000000000000xxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxx00000000xxx0000000xxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxx00000000xxxx000000xxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxx00000000xxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxx0000000000000000xxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxx00000000000000000xxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxx00000000000000000xxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxx00000000000000000xxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxx00000000000000000xxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxx00000000000000000xxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx00000000xxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0000000xxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0000xxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0000xxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0000xxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 0),
(34, 'den', 'den', 3, 22, 0, 0, '00000000xxxxxxxx|0000000000000000|0000000000000000|000000000000xx00|0000000000000000|0000000000000000|0000000000000000|x000000000000000|0000000000000000|0000000000000000|0000000000000000|0000000000000000|x000000000000000|x000000000000000|x000000000000000|x000000000000000|x000000000000000|x000000000000000|x000000000000000|x000000000000000|xxx00xxxxxxxxxxx|xxx00xxxxxxxxxxx|xxx00xxxxxxxxxxx', 0),
(35, 'gardens', 'gardens', 2, 15, 0, 0, 'xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0xxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx00xxxxxxxxxxxx|xxxxxxxxxxxxx0x00xxxxxxxxxxx0x000xxxxxxxxxxx|xxxxxxxxxxxx0000000000000000000000xxxxxxxxxx|xxxxxxxxxxx000000000000000000000000xxxxxxxxx|xxxxxxxxxxx0000000000000000000000000xxxxxxxx|xxxxxxxxxxx00000000000000000000000000xxxxxxx|xxxxxxxx000000000000000000000000000000xxxxxx|xxxxxxx00000000000000000000000000000000xxxxx|xxxxxxx000000000000000000000000000000000xxxx|xxxxxxx0000000000000000000000000000000000xxx|xxxxxxxxx000000000000000000000000000000000xx|00000000000000000000xx00000000000000000000xx|0000000000000000000xxxx00000000000xxxxxxx0xx|0000000000000000000xxxx00000000000x00000xxxx|xxxxx00x0000000000xxxxx0xxxxxx0000x0000000xx|xxxxx0000000000000xxxxx0xx000x0000x000000xxx|xxxxx0000000000000xxxxx0x000000000x00000xxxx|xxxxx000000x0000000xxxx0x000000000xxx00xxxxx|xxxxxxxx000x0000000xxx00xxx000000x0000xxxxxx|xxxxxxxx000x000000xxxx0x0000000000000xxxxxxx|xxxxxxxx000x000000011100000000000000xxxxxxxx|xxxxxxxx000x00000001110000000000000xxxxxxxxx|xxxxxxxxx00x0000000111x00000000x00xxxxxxxxxx|xxxxxxxxxx0x0000000xxx0000000xxxxxxxxxxxxxxx|xxxxxxxxxxxx000000xxxx0000000xxxxxxxxxxxxxxx|xxxxxxxxxxxx000000xxx00xxxxx00xxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxx0xxx0xx000x00xxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxx0xxx0x000000xxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxx0xxx0x00000xxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxx0xxxxx00xxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxx0xxxxx0xxxxxxxxxxxxxxxxxxxx', 0),
(36, 'gate_park', 'gate_park', 17, 26, 0, 0, 'xxxxxxxxx222xx222222xxx222xxxxxx|xxxxxxxxx222xx2222222xx222xxxxxx|xxxxxxxxx22222222222222222xxxxxx|xxxxxxxxx22222222222222222xxxxxx|xxxxxxxxx22222222222222222xxxxxx|xxxxxxxxx22222x22222x22222xxxxxx|xxxxxxxxx11111x22222x11111xxxxxx|0000000xx00000x22222x00000xxxxxx|0000000xx00000000000000000xxxxxx|000000000000000000000000000xx00x|000000000000xxx00000xxx00000000x|000000000000xxx00000xxx00000000x|000000000000xxx00000xxx000000000|00000000000000000000000000000000|x0000000000000000000000000000000|xxx00000000000000000000000000000|xxxxx000000000000000000000000000|xxxxx000000000000000000000000000|xxxxx000000000000000000000000xxx|xxxxxx00000000000000000000000xxx|xxxxxxx000000000000000000000xxxx|xxxxxxxxxx00000000000000000xxxxx|xxxxxxxxxx0000000000000000xxxxxx|xxxxxxxxxx000000000000000xxxxxxx|xxxxxxxxxxxxx00000000000xxxxxxxx|xxxxxxxxxxxxxx000000000xxxxxxxxx|xxxxxxxxxxxxxxxx000xxxxxxxxxxxxx|xxxxxxxxxxxxxxxx000xxxxxxxxxxxxx|xxxxxxxxxxxxxxxx000xxxxxxxxxxxxx', 0),
(37, 'gate_park_2', 'gate_park_2', 17, 23, 0, 0, 'xxxxxxxxxxx111111111111xxxxxxxxxxxx|xxxxxxxxxx11111111111111xxxxxxxxxxx|xxxxxxxxxx11111111111111xxxxxxxxxxx|xxxxxxxxxx11xxxx11xxxx11xxxxxxxxxxx|xxxxxxxxxx00xxxx00xxxx00xxxxxxxxxxx|xxxxxxx0000000000000000000xxxxxxxxx|xxxxxx000000000000000000000xxxxxxxx|xxxxx0000000000000000000x000xxxxxxx|xxxx00000000000000000000xx000xxxxxx|xxxx00000000000000000000xxx000xxxxx|xxxx00000000000000000000xxxx00xxxxx|000000000000000000000000000000xxxxx|0000000000000000000000000000000xxxx|000000000000000000000000000000xxxxx|000000000000000000000000000000xxxxx|000000000000000000000000000000xxxxx|xx00000000000000000000000000000000x|xxx000xxxx00000000000000xxxx0000000|xxx0000xxx00000000000000xxx00000000|xxxx0000xx00000000000000xx000000000|xxxxx0000x00000000000000x00000000xx|xxxxxx00000000000000000000000xxxxxx|xxxxxxx00000x000000000000000x0xxxxx|xxxxxxxx0000xxx0000xxx000000xxxxxxx|xxxxxxxxx000xxx0000xxx000000xxxxxxx|xxxxxxxxxxxxxxx0000xxx000000xxxxxxx|xxxxxxxxxxxxxxxx000xxx000000xxxxxxx|xxxxxxxxxxxxxxxxxxxxxx00000xxxxxxxx', 0),
(38, 'sun_terrace', 'sun_terrace', 9, 17, 0, 2, 'xxxxxx21000000000xxxxxxxx|xxxxxx3xxx000xx000xxxxxxx|xxxxxx4xxx000xxx000xxxxxx|xxxxxx44xx000x00x000xxxxx|xxxxxx44xx0000xx00000xxxx|xxxxxx44xx000000000000xxx|xxxxxx44xx0000000000000xx|xxxxxxx4xxxxxxx00000000xx|xxxxxxx4xxxxxxx0000000xxx|xxxxxx444432222xxx00xxxxx|xxxxxx444432222x0000000xx|xxxxxx444432222x0000000xx|xxxxxx44400x222x0000000xx|xxxxxx444000x11x0000000xx|xxxxxx444000000x0000000xx|xxxxxx444000000x0000000xx|xxxxxx440000000000000000x|xxxxxx4400000000000000000|x8876x444000000x000000000|x8xx6x444000000x000000000|x9xx6x444000000x000000000|999x65444000000x000000000|999xxx444xxxxxxxxxx000000|999xxx444xxxxxxxxxxx00000|999xxx333xxxxxxxxxxxx0000|999xxx222222222222222x000|xxxxxx222222222222222xx00|xxxxxx222222222222222xxx0|xxxxxx222222222222222xxxx|xxxxxxx22222222222222xxxx|xxxxxxxx2222222222222xxxx', 0),
(39, 'space_cafe', 'space_cafe', 21, 17, 1, 0, 'x3333x333211111xxxxxxxxx|x3333x333211111xx3333333|xxxxxx333211111xx3333333|33333333xx11111xx3333333|33333333xx11111xx3333333|33x333xxxx11111xx3333333|xxx222xxx111111xx3333333|22222222xx11111xx3333333|22222222xxx1111xx3333333|22222222xxxx1111x2222222|22222222xxxx1111x1111111|22222222xxxx111111111111|22222222xxxx111111111111|xxx222xxxxx1111111111111|xxxx33xxxx11111111111111|xxx333321111111111111111|xxx333321111111111111111|xxx333321111111111111111|xxxxxxxxxxxxx1111xxxx11x|xxxxxxxxxxxxx0000xxxx11x|xxxxxxxxxx0000000xxxx11x|xxxxxxxxxx0000000xxxxxxx|xxxxxxxxxx0000000xxxxxxx|xxxxxxxxxx0000000xxxxxxx|xxxxxxxxxx0000000xxxxxxx|xxxxxxxxxx0000000xxxxxxx', 0),
(40, 'beauty_salon0', 'beauty_salon0', 4, 3, 0, 0, 'xxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxx|xxx0000000000000xxxxxxxx|xxx00000000000000xxxxxxx|xxxxxx000000000000xxxxxx|xxxxxxxxxxxxx0000xxxxxxx|xxxx0x0000x00000000xxxxx|xxx00xxxxxx0000000xxxxxx|xx000x000xx0000000xxxxxx|xx000xxxxx00000000xxxxxx|xx000x000000000000xxxxxx|xx000x000000000000xxxxxx|xx00xx000000000000xxxxxx|xx00xx00x0000000000xxxxx|xx00xx00x0000000000xxxxx|xx00xx0000000000000xxxxx|xx00xx0000000000000xxxxx|xx000x0xx0000000000xxxxx|xx000x0x00000000000xxxxx|xx000x0x00x00000000xxxxx|xx000x0x00000000000xxxxx|xx000x0x00x00000000xxxxx|xx000xx000000000000xxxxx|xx00000000000000000000xx|xx00000000000000000000xx|xxxxxx0000000000000000xx', 0),
(41, 'chill', 'chill', 22, 22, 0, 6, 'xxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxx00xxxxxxxx|xxxxxxxxxxx000000xxxxxxxx|xxxxxxxxxxx000000xxxxxxxx|xxxxxxxxxxxxxx000xxxxxxxx|xxxxxxxxxxxxxxxx0xxxxxxxx|xxxxxxxxx00000000xxxxxxxx|xxxxxxxx000000xxxxxxxxxxx|xxxxxxxx000000xxxxxxxxxxx|xxxxxxxx000000xxxxxxxxxxx|xxxxxxxx000000xxxxxxxxxxx|xxxxxxxx000000xxxxxxxxxxx|xxxxxxxx0000000xxxxxxxxxx|xxxxxxxx00000000xxxxxxxxx|xxxxxxxx0000000000xxxxxxx|xxxxxxxx0000000000xxxxxxx|xxxxxxxx0000000000xxxxxxx|xxxxxxxx0000000000xxxxxxx|xx000000000000xxxxxxxxxxx|xx000000000000000000000xx|xx000000000000000000000xx|xx000000000xx0000000000xx|xx000000000xxxxx000000000|xx000000000xxx0x000000000|xx00000x000xxx0x000000000|xx000000000xxxxx0000000xx|xx000000000000000000000xx|xx000000000000000000000xx|xx000000000000000000000xx|xx000000000000xxxxxxxxxxx', 0),
(42, 'dusty_lounge', 'dusty_lounge', 14, 1, 2, 4, 'xxxxxxxxxxxxxx22xxxxxxxxxxxxx|xxxxxxxxxx222x222x2xxxxxxxxxx|xxxxxxx33322222222223xxxxxxx3|xxxxxxx33322222222223xxxxxxx3|xxxxxxx33322222222223x33333x3|xxxxxxx33322222222223x33333x3|xx111xx33322222222223xxxxxxx3|xx111xxx332222222222333333333|xx111xxxx32222222222333333333|xx111xxxxxx222222222333333333|xx111xxxxxxx1111111x333333333|xx111xxxxxxx1111111x222222222|xx111xxxxxx111111111111111111|xx111xxxxxx111111111111111111|11111xxxxxx111111111111111111|11111xxxxxx111111111111111111|11x11xxxxxx111111111111111111|11xxxxxxxxx11111111111111111x|x11xxxxxxxxx1111111x1111111xx|xx11xxxxxxx111111111111111xxx|xxx11xxxxxx11111111111111xxxx|xxxx11111111111111111111xxxxx|xxxxx11111111111111xxxxxxxxxx|xxxxxxxxxxx11111111xxxxxxxxxx|xxxxxxxxxxx11111111xxxxxxxxxx', 0),
(43, 'cr_staff', 'cr_staff', 3, 22, 0, 0, '00000000xxxxxxxx|0000000000000000|0000000000000000|000000000000xx00|0000000000000000|0000000000000000|0000000000000000|x000000000000000|0000000000000000|0000000000000000|0000000000000000|0000000000000000|x000000000000000|x000000000000000|x000000000000000|x000000000000000|x000000000000000|x000000000000000|x000000000000000|x000000000000000|xxx00xxxxxxxxxxx|xxx00xxxxxxxxxxx|xxx00xxxxxxxxxxx', 0),
(44, 'rooftop', 'rooftop', 17, 12, 4, 0, '44xxxxxxxxxxxxxxxxxx|444xxxxxxxxxxx444444|4444xxxxxxxxxx444444|44444xxxx4xxxx444444|444444xxx44xxx444444|44444444444444444444|44444444444444444444|44444444444444444444|44444444xx44xx44xx44|44444444xx44xx44xx44|44444444444444444444|44444444444444444444|44444444444444444444|x444444x444444xx4444|x444444x444444xx333x|x444444x444444xx222x|x444444x444444xx11xx|x444444x444444xxxxxx', 0),
(45, 'rooftop_2', 'rooftop_2', 4, 9, 0, 0, 'x0000x000|xxxxxx000|000000000|000000000|000000000|000000000|000000000|000000000|000000000|000000000|xxx000xxx|xxx000xxx', 0),
(46, 'tearoom', 'tearoom', 21, 19, 1, 6, 'xxxxxxxxxxxxxxxxxxxxxx|xxxxxxxx3333x33333333x|333333xx3333x33333333x|3333333x3333x33333333x|3333333x3333x33333333x|3333333xxxxxx33333333x|333333333333333333333x|333333333333333333333x|333333333333333333333x|333333333333333333333x|33333333222x333333333x|33333333222x333333333x|33333333222x333333333x|33333333222x333333333x|33333333111x333333333x|33333333111x333333333x|33333333111x333333333x|xxxxxxxx111xxxxxxxxxxx|11111111111111111111xx|1111111111111111111111|1111111111111111111111|11111111111111111111xx', 0),
(47, 'cafe_ole', 'taivas_cafe', 14, 29, 0, 0, 'XXXXXXXXXXXXX111111X|XXXXXXXXXXXXX1111111|XXXXXXXXXXXXX1111111|XXXXXXXXXXXXX1111111|XXXXXXXXXXXXX1111111|XXX11111111111111111|XXX11111111111111111|XX111111111111111111|XX111111111111111111|XX111111111111111111|XXX11111111111111111|111111111XXXXXXX1111|111111111X0000000000|111111111X0000000000|111111111X0000000000|111111111X0000000000|111111111X0000000000|111111111X0000000000|111111111X0000000000|111111111X0000000000|111111111X0000000000|X11111111X0000000000|XX1111111X0000000000|XXX111111X0000000000|XXXX11111X0000000000|XXXXX111110000000000|XXXXXX11110000000000|XXXXXXX1110000000000|XXXXXXXX11000000000X|XXXXXXXXXX00000000XX|XXXXXXXXXXXXXX00XXXX|XXXXXXXXXXXXXX00XXXX', 0),
(48, 'cr_cafe', 'cr_cafe', 20, 10, 0, 6, '0000000000000000000xx|x000000000000000000xx|xx00000000000000000xx|xx00000000000000000xx|xx00000000000000000xx|xxxx000000000000000xx|0000000000000000000xx|0000000000000000000xx|x000000000000000000xx|xx00000000000000000xx|xxxx00000000000000000|xxx000000000000000000|xxx0000000000000000xx|xxx0000000000000000xx|xx00000000000000000xx|xx00000000000000000xx|xx00000000000000000xx|xx00000000000000000xx|xx00000000000000000xx|xx00000000000000000xx', 0),
(49, 'lobby_a', 'lobby_a', 12, 27, 1, 0, 'XXXXXXXXX77777777777XXXXX|XXXXXXXXX777777777777XXXX|XXXXXXXXX777777777766XXXX|XXXXXXXXX777777777755XXXX|XX333333333333333334433XX|XX333333333333333333333XX|XX333333333333333333333XX|33333333333333333333333XX|333333XXXXXXX3333333333XX|333333XXXXXXX2222222222XX|333333XXXXXXX2222222222XX|XX3333XXXXXXX2222222222XX|XX3333XXXXXXX222222221111|XX3333XXXXXXX111111111111|333333XXXXXXX111111111111|3333333222211111111111111|3333333222211111111111111|3333333222211111111111111|XX33333222211111111111111|XX33333222211111111111111|XX3333322221111111XXXXXXX|XXXXXXX22221111111XXXXXXX|XXXXXXX22221111111XXXXXXX|XXXXXXX22221111111XXXXXXX|XXXXXXX22221111111XXXXXXX|XXXXXXX222X1111111XXXXXXX|XXXXXXX222X1111111XXXXXXX|XXXXXXXXXXXX11XXXXXXXXXXX|XXXXXXXXXXXX11XXXXXXXXXXX|XXXXXXXXXXXX11XXXXXXXXXXX|XXXXXXXXXXXX11XXXXXXXXXXX', 0),
(50, 'floorlobby_c', 'floorlobby_c', 9, 21, 0, 0, 'XXXXXXXXXXXXXXXXXXXXXXXXXXX|XXXXXXXXXXXXXXXXXXXXXXXXXXX|XXX0000000000000000XXXXXXX0|XXX000000000000000000XXXX00|X00000000000000000000000000|X00000000000000000000000000|XXX000000000000000000000000|XXXXXXX00000000000000000000|XXX111100000011111111111111|XXX111100000011111111111111|XXX111100000011111111111111|XXX111100000011111111111111|XXX111100000011111111111111|XXX111100000011111111111111|XXX1XX100000011111111111111|XXX1XX100000011111111111111|XXX111100000011111111111111|XXX111100000011111111111111|XXX111100000011111111111111|XXXXXXXX0000XXXXXXXXXXXXXXX|XXXXXXXX0000XXXXXXXXXXXXXXX|XXXXXXXX0000XXXXXXXXXXXXXXX', 0),
(51, 'floorlobby_b', 'floorlobby_b', 9, 21, 0, 0, 'XXXXXXXXXXXXXXXXXXXXXXXXXXX|XXXXXXXXXXXXXXXXXXXXXXXXXXX|XXX0000000000000000XXXXXXX0|XXX000000000000000000XXXX00|X00000000000000000000000000|X00000000000000000000000000|XXX000000000000000000000000|XXXXXXX00000000000000000000|XXX111100000011111111111111|XXX111100000011111111111111|XXX111100000011111111111111|XXX111100000011111111111111|XXX111100000011111111111111|XXX111100000011111111111111|XXX1XX100000011111111111111|XXX1XX100000011111111111111|XXX111100000011111111111111|XXX111100000011111111111111|XXX111100000011111111111111|XXXXXXXX0000XXXXXXXXXXXXXXX|XXXXXXXX0000XXXXXXXXXXXXXXX|XXXXXXXX0000XXXXXXXXXXXXXXX', 0),
(52, 'floorlobby_a', 'floorlobby_a', 9, 21, 0, 0, 'XXXXXXXXXXXXXXXXXXXXXXXXXXX|XXXXXXXXXXXXXXXXXXXXXXXXXXX|XXX0000000000000000XXXXXXX0|XXX000000000000000000XXXX00|X00000000000000000000000000|X00000000000000000000000000|XXX000000000000000000000000|XXXXXXX00000000000000000000|XXX111100000011111111111111|XXX111100000011111111111111|XXX111100000011111111111111|XXX111100000011111111111111|XXX111100000011111111111111|XXX111100000011111111111111|XXX1XX100000011111111111111|XXX1XX100000011111111111111|XXX111100000011111111111111|XXX111100000011111111111111|XXX111100000011111111111111|XXXXXXXX0000XXXXXXXXXXXXXXX|XXXXXXXX0000XXXXXXXXXXXXXXX|XXXXXXXX0000XXXXXXXXXXXXXXX', 0),
(53, 'cinema_a', 'cinema_a', 20, 27, 1, 0, 'xxxxxxx1xx11111111xxxxxx|xxx1111111111111111xxxxx|xxx111xxxx1111111111xxxx|xxxx2xxxxxxxxxxxxxxxxxxx|xx3x3x333311xxxxxxxxxx11|xx3333333311111111111111|xx3333333311111111111111|xx3333333311111111122111|xx3333333311x22222222111|xx3333333311x22222222111|xx3333333311xxxxxxxxx111|xx3333333311111111111111|xx3333333311111111111111|xx3333333311111111111111|xx3333333311111xxxx11111|xx3333333311111xxxx11111|xx3333333311111xxxx11111|xx3333333311111xxxx11111|xx3333333311111xxxx11111|xx3333333311111xxxx11111|xx3333333311111xxxx11111|333333332111111xxxx11111|333333332111111111111111|333333332111111111111111|333333332111111111111111|xx3333332111111111111111|xxxxxxxxxxxxxxxxxxx11111|xxxxxxxxxxxxxxxxxxx11111|xxxxxxxxxxxxxxxxxxx11111', 0),
(54, 'sport', 'sport', 0, 0, 1, 2, '111222222222222222x2222|x11222222222222222x2222|x11222222222222222x2222|xx1x2222222222222211111|x11xx222222222222211111|x11xx222222x22222211111|x11xx222222x222222x1111|x11xx222222x222222x1111|x11xx2222222222222x1111|x11xxxxxxxxxxxxxxxx1111|x1111111111111111111111|x1111111111111111111111|x1111111111111111111111|x1111111111111111111111|xxxx1111111111111x1111x|111x1111111111111x1111x|111x1111111111111x1111x|111x1111111111111x1111x|111x11111xxxxxxxxxxxxxx|x11x1111xxxxxxxxxxxxxxx|xxxx111xxxxxxxxxxxxxxxx', 0),
(55, 'old_skool0', 'old_skool0', 2, 1, 0, 4, 'xx0xxxxxxxxxxxxxx|0000000xxx00000xx|0000000x0000000xx|0000000xxxxxxxxxx|0000000000000000x|0000000000000000x|0000000000000000x|0000000000000000x|0000000000000000x|0000000000000000x|0000000000000000x|0000000000000000x|0000000000000000x|0000000000000000x|0000000000000000x|0000000000000000x|0000000000000000x|0000000000000000x|00000000000000000|0000000000000000x|0000000000000000x|0000000000000000x|0000000000000000x|0000000000000000x|0000000000000000x', 0),
(56, 'old_skool1', 'old_skool1', 1, 7, 6, 2, 'x6666666665432100|x6666666665432100|x6600000000000x00|x6600000000000000|x6600000000000000|x6600000000000000|x660000000000x000|666000000000x1111|x66000000000xx111|x66000000000x1111|x66000000000x1111|x55000000000x1111|x44000000000x1111|x33000000000x1111|x22000000000xx111|x11x00000000x1111|x00000000000x1111|x00000000000xx111', 0),
(57, 'malja_bar_a', 'malja_bar_a', 4, 24, 1, 0, 'xxxxxxxxxxx44444|xxxx444444444444|xxxx444444444444|xxxx444444444444|xxxx444444444444|xxxx444444444444|xxxxxxxxxxxxx333|1111111111111222|1111111111111111|1111111111111111|1111111111111111|1111111111111111|1111111111111111|1111111111111111|1111111111111111|1111111111111111|1111111111111111|1111111111111111|1111111111111111|111111111xxxxxxx|xxx11111xxxxxxxx|11111111xxxxxxxx|11111111xxxxxxxx|11111111xxxxxxxx|11111111xxxxxxxx', 0),
(58, 'malja_bar_b', 'malja_bar_b', 4, 24, 3, 0, '6666333333333333|6666333333333333|6666333333333333|7766333333333333|7755333333333333|8855333333332223|8844333311111111|9944333311111111|9933333311111111|9933333311111111|9933333311111111|9933333311111111|9933333311111111|9933333311111111|9933333211111111|9933333211111111|9933333211111111|9933333311111111|99333333xxxxxxxx|99333333xxxxxxxx|xxx33333xxxxxxxx|xxx33333xxxxxxxx|3xx33333xxxxxxxx|xxx33333xxxxxxxx|xxx33333xxxxxxxx|xxx33333xxxxxxxx', 0),
(59, 'bar_a', 'bar_a', 5, 1, 7, 4, 'xxxx8888xxxxxxxxxxx|xxxx7777xxxxxxxxxxx|xxxx6666xxxxxxxxxxx|xxx6666666555555555|xxx6666666555555555|xxx6666666555555555|xxx6666666555555555|xxx6666666555555555|xxx6666666555555555|xxx6666666555555555|xxx6666666555xxxxxx|xxx6666666555555555|xxx5555555555555555|xxx5555555555555555|xxx5555555555555555|xxx5555555555555555|xxx5555555555xxxxxx|xxx5555555555555555|xxx5555555555555555|xxx5555555555555555|xxx5555555555555555|xxx5555555555555555|xxx5555555555xxxxxx|xxxx555555555555555|55xx555555555555555|55xx555555555555555|5555555555555555555|5555555555555555555|xxxxxxxx55555xxxxxx|xxxxxxxxx5555xxxxxx|xxxxxxxxx5555xxxxxx|xxxxxxxxx5555xxxxxx|xxxxxxxxx4444xxxxxx|xxxxxxxxx3333xxxxxx', 0),
(60, 'bar_b', 'bar_b', 2, 12, 4, 2, 'xxxxx4xxxxxxxxxxxx|xxxx4444444xxxxxxx|xxxx4444444xxxxxxx|xxx444444444444444|xxx444444444444444|xxx444444444444444|xxx444444444444444|xxx444444444444444|xxx444444444444444|xxx444444444444444|654444444444444444|654444444444444444|654444444444444444|654444444444444444|xxx444444444444444|xxx444444444444444|xxx444444444444444|xxx444444444444444|xxx444444444444444|xxxx44444444444444|xxxx33444444444444|xxxx22444444444444|xxxx2222222222xx44|xxxx2222222222xx44|xxxxx222222222xxxx|xxxxxx22222222xxxx|xxxxxx22222222xxxx|xxxxxx22222222xxxx|xxxxxx22222222xxxx|xxxxxx22222222xxxx|xxxxxx22222222xxxx', 0),
(61, 'habburger', 'habburger', 22, 10, 0, 6, '22222222222222222222xxx|22222222222222222222xxx|22222222222222222222xxx|22222222222222222222xxx|xxxxxxxxxxxxxxxx1111xxx|xxxxxxxxxxxxxxxx0000xxx|xxx00000000000000000xxx|00000000000000000000xxx|00000000000000000000xxx|00000000000000000000000|00000000000000000000000|00000000000000000000xxx|00000000000000000000xxx|00000000000000000000xxx|xxx00000000000000000xxx', 0),
(62, 'pizza', 'pizza', 5, 27, 1, 0, 'xxxxxxxxx0000000|x11111x1xx000000|11xxxxx111x00000|11x1111111xx0000|11x1111111100000|xxx1111111100000|1111111111100000|1111111111100000|1111111111100000|1111111111100000|1111111111100000|1111111111100000|1111111111100000|1111111111100000|1111111111100000|1111111111100000|1111111111100000|11111111111xxxxx|1111111111xxxxxx|1111111111111111|1111111111111111|1111111111111111|1111111111111111|1111111111111111|1111111111111111|11xx11xx11111111|xxxx11xxxxxxxxxx|xxxx11xxxxxxxxxx', 0),
(63, 'bb_lobby_1', 'bb_lobby_1', 14, 19, 0, 0, 'xxx2222222222222222x|xxx2222222222222222x|xxx2222222222222222x|xxx2222222222222222x|xxx11111111111111111|11x11111111111111111|11x11111111111111111|11x11111111111111111|x1x11111111111111111|xxx11111111111111111|xxx11111111111111111|xxx11111111111111111|xxx11111111111111111|xxx11111111111111111|xxxxxxxxx00000000000|xxxxxxxxx00000000000|xxxxxxxxx00000000000|xxxxxxxxx00000000000|xxxxxxxxx00000000000|xxxxxxxxx00000000000|xxxxxxxxxxxxx000xxxx|xxxxxxxxxxxxx000xxxx|xxxxxxxxxxxxx000xxxx', 0);
INSERT INTO `rooms_models` (`id`, `model_id`, `model_name`, `door_x`, `door_y`, `door_z`, `door_dir`, `heightmap`, `usertype`) VALUES
(64, 'snowwar_lobby_1', 'snowwar_lobby_1', 41, 36, 1, 0, 'xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx11111xx1xxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx11111xx1111xxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxx111111xxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxx1111111111111111xxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxx1111111111111111xxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxx1111111111111111xxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxx1111111111111111xxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxx1111111111111111xxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxx1111111111111111xxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxx1111111111111111xxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxx1111111111111111xxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxx1111111111111111xxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxx1111111111111111xxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxx1111111111111111xxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxx111111111111111xxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxx1111111111111111xxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxx1111111111111111xxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxx1111111111111111xxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxx111111111111111xxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxx1111x1111111111xxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 0),
(65, 'tv_studio', 'tv_studio', 16, 27, 4, 6, 'xxxxxxxxxxxxxxxxxxxx|xxxxx0x00000xxxxxxxx|xxxxx0x000000xxxxxxx|xxxxx0x000000xxxxxxx|xxxxx0x0xxx00000xx0x|xx0000x000000000xx00|xx00xxx000000000xx00|xxxxx00000000000xx00|x000000000000x000000|x0000000000000000000|x0000000000000000x00|00000000000000000x00|x000x000000000000x00|x0000000000000000x00|x0000000000000000x00|000x0000000000000000|000x0000000000000000|00000000000000xxx000|x0000000000000000000|xx00000000000000000x|xxx000000000000000xx|xxxxxxxxx1111xxxx0xx|xxxxxxxxx2222xxxxxxx|xxxxxxxxx3333xxxxxxx|xxxxxx44x4444x444xxx|xxxxx444444444444xxx|xxxxxx44444444444444|xxxxx4444x4444444444|xxxxx4444x4444444444|xxxxx4444x4444444xxx|xxxxx444444444444xxx|xxxxx444444444444xxx|xxxxxx44444444444xxx', 1),
(66, 'cr_kitchen', 'cr_kitchen', 7, 21, 0, 0, 'X0XXXX000XXXX000X0X|X000000000000000000|X000000000000000000|X000000000000000XXX|X00XXXX00XXXX000XXX|X00XXXX00XXXX00XXXX|X00000000000000XXXX|X00000000000000XXXX|X00000000000000XXXX|X00XXXXXXXXXX00XXXX|X00XXXXXXXXXX00XXXX|X00XXXXXXXXXX000XXX|0000000000000000XXX|000000000000000XXXX|000000000000000XXXX|000XXX0000XXX00XXXX|000XXX0000XXX00XXXX|000000000000000XXXX|000000000000000XXXX|000000000000000XXXX|XXXXXXX00XXXXXXXXXX|XXXXXXX00XXXXXXXXXX|XXXXXXX00XXXXXXXXXX', 1),
(67, 'club_mammoth', 'club_mammoth', 6, 16, 4, 2, 'xxxxxx4444444x4444xxxxxxxxxxxxx|xxxxxx4444444x444322xxxxxxxxxxx|xxxxxxxxxxxxxx444322xxxxxxxxxxx|x444444444444444442222xxxxxxxxx|4444444444444444442222xxxxxxxxx|4xxxxxxxxxxxxxxxxx2222xxxxxxxxx|4xxxxxxxxxx22222xx2222xxxxxxxxx|44xxxxxxxxx22222x2xxxxxxxxxxxxx|x4444444xxx22222x22xxxxxxxxxxxx|xx4444444xx22x22x222xxxxxxxxxxx|xxxxxxx444x22222xxxxxxxxxxxxxxx|xxxxxx444322222222211111111xxxx|xxxxxx444322222222211111111xxxx|xxxxxx444442222222211111111xxxx|xxx444444442222222211111111xxxx|xxx444444442222222211111111xxxx|xxx444444442222222211111111xxxx|xxx444444442222222211111111xxxx|xxxxxx4444422222222x1111111xxxx|xxxxxx4444422222222xxxxxxxxxxxx|xxxxxx4443222222222222222222xxx|xxxxxx4443xxxxxxx2xxxxx222xxxxx|xxxxxx444xxxxxxxxxxxxxxx22xxxxx|xxxxxx4xxxxxxxxxxxxx444422xxxxx|xxx4444xxxxxxxxxxxxx4444x2xxxxx|xxx566666666666666664444xxxxxxx|xxxx66666666666666664444xxxxxxx|xxxxxxx666666666666544xxxxxxxxx|xxxxxxx666666666666544xxxxxxxxx|xxxxxxx6666666666666xxxxxxxxxxx|xxxxxxx6666666666666xxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 1),
(68, 'ice_cafe', 'taivas_cafe', 17, 23, 0, 0, 'xx111111x000000000|xx111111x000000000|xx111111x000000000|xx111111x000000000|xx111111x000000000|xx111111x000000000|xx111111x000000000|xx1111111000000000|xx1111111000000000|111111111000000000|111111111000000000|111111111000000000|111111111000000000|111111111000000000|11111111x000000000|11111111x000000000|xx111111x000000000|xx111111x000000000|xx111111x000000000|xx111111x000000000|xx111111x000000000|xx111111x000000000|xx1111110000000000|xx1111111000000000|xx1111111000000000|xx1111111000000000|', 0),
(69, 'netcafe', 'netcafe', 22, 12, 0, 6, 'xxxxx1111xxxxxxxxxxx1xxxx|xxxxx1111111111111111xxxx|xxxxx1111111111111111xxxx|xxxxx1111111111111111xxxx|xxxxxxxx0000000000000xxxx|111111100000000000000xxxx|111111100000000000000xxxx|111111100000000000000xxxx|xxxx11100000000000000xxxx|x1xx11100000000000000xxxx|x1xx11100000000000000xxxx|x1xx111000000000000000000|x1xx111000000000000000000|xxxx111000000000000000000|xxxx11100000000000000xxxx|xxxx1110000000xx11111xxxx|xxxxx111110000x111111xxxx|xxxxxx111100001111111xxxx|xxxxxx111100001111111xxxx|xxxxxx111100001111111xxxx|xxxxxx111100001111111xxxx|xxxxxx111100001111111xxxx|xxxxxx111100001111111xxxx|xxxxx1111100001111111xxxx|', 1),
(70, 'hallway0', 'hallway0', 2, 2, 0, 2, 'xxxx000000001111111111111111xxxx|xxxx000000001111111111111111xxxx|00000000000011111111111111111111|00000000000011111111111111111111|00000000000011111111111111111111|00000000000011111111111111111111|xxxx000000001111111111111111xxxx|xxxx0000000x1111111111111111xxxx|xxxxxxxxxxxxx1111111xxxxxxxxxxxx|xxxxxxxxxxxx11111111xxxxxxxxxxxx|xxxxxxxxxxxx11111111xxxxxxxxxxxx|xxxxxxxxxxxx11111111xxxxxxxxxxxx|xxxxxxxxxxxx11111111xxxxxxxxxxxx|xxxxxxxxxxxx11111111xxxxxxxxxxxx|xxxxxxxxxxxx11111111xxxxxxxxxxxx|xxxxxxxxxxxx11111111xxxxxxxxxxxx|xxxxxxxxxxxxxx1111xxxxxxxxxxxxxx|xxxxxxxxxxxxxx1111xxxxxxxxxxxxxx|xxxxxxxxxxxxxx1111xxxxxxxxxxxxxx|xxxxxxxxxxxxxx1111xxxxxxxxxxxxxx', 1),
(71, 'hallway9', 'hallway9', 21, 23, 0, 7, 'xxxxxxxxxxxxxx0000xxxxxxxxxxxxxx|xxxxxxxxxxxxxx0000xxxxxxxxxxxxxx|xxxxxxxxxxxxxx0000xxxxxxxxxxxxxx|xxxxxxxxxxxxxx0000xxxxxxxxxxxxxx|xxxxxxxxxxxx00000000xxxxxxxxxxxx|xxxxxxxxxxxx00000000xxxxxxxxxxxx|xxxxxxxxxxxx00000000xxxxxxxxxxxx|xxxxxxxxxxxx00000000xxxxxxxxxxxx|xxxxxxxxxxxx00000000xxxxxxxxxxxx|xxxxxxxxxxxx00000000xxxxxxxxxxxx|xxxxxxxxxxxx00000000xxxxxxxxxxxx|xxxxxxxxxxxx00000000xxxxxxxxxxxx|xxxx000000000000000000000000xxxx|xxxx000000000000000000000000xxxx|00000000000000000000000000000000|00000000000000000000000000000000|00000000000000000000000000000000|00000000000000000000000000000000|xxxx000000000000000000000000xxxx|xxxx000000000000000000000000xxxx|xxxxxxxxxxxx00000000xxxxxxxxxxxx|xxxxxxxxxxxx00000000xxxxxxxxxxxx|xxxxxxxxxxxx000000000000xxxxxxxx|xxxxxxxxxxxx000000000000xxxxxxxx|xxxxxxxxxxxx000000000000xxxxxxxx|xxxxxxxxxxxx000000000000xxxxxxxx|xxxxxxxxxxxx00000000xxxxxxxxxxxx|xxxxxxxxxxxx00000000xxxxxxxxxxxx|xxxxxxxxxxxxxx0000xxxxxxxxxxxxxx|xxxxxxxxxxxxxx0000xxxxxxxxxxxxxx|xxxxxxxxxxxxxx0000xxxxxxxxxxxxxx|xxxxxxxxxxxxxx0000xxxxxxxxxxxxxx', 1),
(72, 'hallway2', 'hallway2', 15, 2, 0, 4, 'xxxxxxxxxxxxxx0000xxxxxxxxxx|xxxxxxxxxxxxxx0000xxxxxxxxxx|xxxxxxxxxxxxxx0000xxxxxxxxxx|xxxxxxxxxxxxxx0000xxxxxxxxxx|xxxx11111111000000000000xxxx|xxxx11111111000000000000xxxx|1111111111110000000000000000|1111111111110000000000000000|1111111111110000000000000000|1111111111110000000000000000|xxxx11111111000000000000xxxx|xxxx11111111000000000000xxxx|xxxx11111111xxxx00000000xxxx|xxxx11111111xxxx00000000xxxx|xxxx11111111xxxx00000000xxxx|xxxx11111111xxxx00000000xxxx|xxxx11111111xxxx00000000xxxx|xxxx11111111xxxx00000000xxxx|xxxx11111111xxxx00000000xxxx|xxxx11111111xxxx00000000xxxx|xxxxxx1111xxxxxxxxxxxxxxxxxx|xxxxxx1111xxxxxxxxxxxxxxxxxx|xxxxxx1111xxxxxxxxxxxxxxxxxx|xxxxxx1111xxxxxxxxxxxxxxxxxx', 1),
(73, 'hallway1', 'hallway1', 2, 14, 0, 2, 'xxxxxxxxxxxxxx0000xxxxxxxxxxxxxx|xxxxxxxxxxxxxx0000xxxxxxxxxxxxxx|xxxxxxxxxxxxxx0000xxxxxxxxxxxxxx|xxxxxxxxxxxxxx0000xxxxxxxxxxxxxx|xxxxxxxxxxxx0000000000000000xxxx|xxxxxxxxxxxx0000000000000000xxxx|xxxxxxxxxxxx00000000000000000000|xxxxxxxxxxxx00000000000000000000|xxxxxxxxxxxx00000000000000000000|xxxxxxxxxxxx00000000000000000000|xxxxxxxxxxxx0000000000000000xxxx|xxxxxxxxxxxx0000000000000000xxxx|xxxx1111111100000000xxxxxxxxxxxx|xxxx1111111100000000xxxxxxxxxxxx|11111111111100000000xxxxxxxxxxxx|11111111111100000000xxxxxxxxxxxx|11111111111100000000xxxxxxxxxxxx|11111111111100000000xxxxxxxxxxxx|xxxx1111111100000000xxxxxxxxxxxx|xxxx1111111100000000xxxxxxxxxxxx', 1),
(74, 'hallway3', 'hallway3', 14, 21, 1, 0, 'xxxxxx1111xxxxxxxxxxxxxxxxxxxxxx|xxxxxx1111xxxxxxxxxxxxxxxxxxxxxx|xxxxxx1111xxxxxxxxxxxxxxxxxxxxxx|xxxxxx1111xxxxxxxxxxxxxxxxxxxxxx|xxxx111111111111111100000000xxxx|xxxx111111111111111100000000xxxx|11111111111111111111000000000000|11111111111111111111000000000000|11111111111111111111000000000000|11111111111111111111000000000000|xxxx111111111111111100000000xxxx|xxxx111111111111111100000000xxxx|xxxxxxxxxxxx11111111xxxxxxxxxxxx|xxxxxxxxxxxx11111111xxxxxxxxxxxx|xxxxxxxxxxxx11111111xxxxxxxxxxxx|xxxxxxxxxxxx11111111xxxxxxxxxxxx|xxxxxxxxxxxx11111111xxxxxxxxxxxx|xxxxxxxxxxxx11111111xxxxxxxxxxxx|xxxxxxxxxxxx11111111xxxxxxxxxxxx|xxxxxxxxxxxx11111111xxxxxxxxxxxx|xxxxxxxxxxxxxx1111xxxxxxxxxxxxxx|xxxxxxxxxxxxxx1111xxxxxxxxxxxxxx|xxxxxxxxxxxxxx1111xxxxxxxxxxxxxx|xxxxxxxxxxxxxx1111xxxxxxxxxxxxxx', 1),
(75, 'hallway4', 'hallway4', 29, 3, 1, 6, 'xxxx000000001111111111111111xxxx|xxxx000000001111111111111111xxxx|00000000000011111111111111111111|00000000000011111111111111111111|00000000000011111111111111111111|00000000000011111111111111111111|xxxx000000001111111111111111xxxx|xxxx000000001111111111111111xxxx|xxxxxxxxxxxxxxxxxxxx11111111xxxx|xxxxxxxxxxxxxxxxxxxx11111111xxxx|xxxxxxxxxxxxxxxxxxxx11111111xxxx|xxxxxxxxxxxxxxxxxxxx11111111xxxx|xxxxxxxxxxxxxxxxxxxx11111111xxxx|xxxxxxxxxxxxxxxxxxxx11111111xxxx|xxxxxxxxxxxxxxxxxxxx11111111xxxx|xxxxxxxxxxxxxxxxxxxx11111111xxxx|xxxxxxxxxxxxxxxxxxxxxx1111xxxxxx|xxxxxxxxxxxxxxxxxxxxxx1111xxxxxx|xxxxxxxxxxxxxxxxxxxxxx1111xxxxxx|xxxxxxxxxxxxxxxxxxxxxx1111xxxxxx', 1),
(76, 'hallway5', 'hallway5', 14, 2, 1, 4, 'xxxxxxxxxxxxxx11xxxxxx1111xx|xxxxxxxxxxxxxx111xxxxx1111xx|xxxxxxxxxxxxxx1111xxxx1111xx|xxxxxxxxxxxxxx1111xxxx1111xx|xxxxxxxxxxxx1111111111111111|xxxxxxxxxxxx1111111111111111|xxxxxxxxxxxx1111111111111111|xxxxxxxxxxxx1111111111111111|xxxxxxxxxxxx1111111111111111|xxxxxxxxxxxx1111111111111111|xxxxxxxxxxxx1111111111111111|xxxxxxxxxxxx1111111111111111|xxxx000000001111111111111111|xxxx000000001111111111111111|0000000000001111111111111111|0000000000001111111111111111|0000000000001111111111111111|0000000000001111111111111111|xxxx000000001111111111111111|xxxx0000000x1111111111111111|xxxxxxxxxxxx11111111xxxxxxxx|xxxxxxxxxxxx11111111xxxxxxxx|xxxxxxxxxxxx11111111xxxxxxxx|xxxxxxxxxxxx11111111xxxxxxxx|xxxxxxxxxxxx11111111xxxxxxxx|xxxxxxxxxxxx11111111xxxxxxxx|xxxxxxxxxxxx11111111xxxxxxxx|xxxxxxxxxxxx11111111xxxxxxxx', 1),
(77, 'hallway8', 'hallway8', 15, 3, 0, 4, 'xxxxxxxxxxxxxx00xxxx0000|xxxxxxxxxxxxxx000xxx0000|xxxxxxxxxxxxxx0000xx0000|xxxxxxxxxxxxxx0000xx0000|xxxx11111111000000000000|xxxx11111111000000000000|xxxx11111111000000000000|xxxx11111111000000000000|xxxx11111111000000000000|xxxx11111111000000000000|xxxx11111111000000000000|xxxx11111111000000000000|xxxx11111111xxxx00000000|xxxx11111111xxxx00000000|111111111111xxxx00000000|111111111111xxxx00000000|111111111111xxxx00000000|111111111111xxxx00000000|xxxx11111111xxxx00000000|xxxx11111111xxxx00000000|xxxxxxxxxxxxxxxxxx0000xx|xxxxxxxxxxxxxxxxxx0000xx|xxxxxxxxxxxxxxxxxx0000xx|xxxxxxxxxxxxxxxxxx0000xx', 1),
(78, 'hallway7', 'hallway7', 7, 2, 1, 4, 'xxxxxx11xxxxxxxxxxxx|xxxxxx111xxxxxxxxxxx|xxxxxx1111xxxxxxxxxx|xxxxxx1111xxxxxxxxxx|xxxx11111111xxxxxxxx|xxxx11111111xxxxxxxx|xxxx111111111111xxxx|xxxx111111111111xxxx|xxxx111111111111xxxx|xxxx111111111111xxxx|xxxx11111111xxxxxxxx|xxxx11111111xxxxxxxx|xxxx11111111xxxxxxxx|xxxx11111111xxxxxxxx|xxxx11111111xxxxxxxx|xxxx11111111xxxxxxxx|xxxx11111111xxxxxxxx|xxxx11111111xxxxxxxx|xxxx11111111xxxxxxxx|xxxx11111111xxxxxxxx|xxxx000000000000xxxx|xxxx000000000000xxxx|00000000000000000000|00000000000000000000|00000000000000000000|00000000000000000000|xxxx000000000000xxxx|xxxx000000000000xxxx', 1),
(79, 'hallway6', 'hallway6', 1, 10, 1, 2, 'xxxx1111111111111111xxxx|xxxx1111111111111111xxxx|xxxx1111111111111111xxxx|xxxx1111111111111111xxxx|xxxx1111xxxxxxxxxxxxxxxx|xxxx1111xxxxxxxxxxxxxxxx|xxxx1111xxxxxxxxxxxxxxxx|xxxx1111xxxxxxxxxxxxxxxx|xxxx1111111100000000xxxx|xxxx1111111100000000xxxx|111111111111000000000000|111111111111000000000000|111111111111000000000000|111111111111000000000000|xxxx1111111100000000xxxx|xxxx1111111100000000xxxx|xxxxxxxx1111xxxxxxxxxxxx|xxxxxxxx1111xxxxxxxxxxxx|xxxxxxxx1111xxxxxxxxxxxx|xxxxxxxx1111xxxxxxxxxxxx|xxxxxxxx111111111111xxxx|xxxxxxxx111111111111xxxx|xxxxxxxx111111111111xxxx|xxxxxxxx111111111111xxxx', 1),
(80, 'hallway10', 'hallway10', 3, 23, 1, 1, 'xxxxxxxxxx00000000xxxx|xxxxxxxxxx00000000xxxx|xxxxxxxxxx00000000xxxx|xxxxxxxxxx00000000xxxx|xx1111xxxx0000xxxxxxxx|xx1111xxxx0000xxxxxxxx|xx1111xxxx0000xxxxxxxx|xx1111xxxx0000xxxxxxxx|11111111xx0000000000xx|11111111xx0000000000xx|11111111xx0000000000xx|11111111xx0000000000xx|11111111xxxxxxxx0000xx|11111111xxxxxxxx0000xx|11111111xxxxxxxx0000xx|11111111xxxxxxxx0000xx|1111111111111111000000|1111111111111111000000|1111111111111111000000|1111111111111111000000|1111111111111111000000|1111111111111111000000|1111111111111111000000|1111111111111111000000|xx1111xxxxxxxxxxxxxxxx|xx1111xxxxxxxxxxxxxxxx|xx1111xxxxxxxxxxxxxxxx|xx1111xxxxxxxxxxxxxxxx', 1),
(81, 'hallway11', 'hallway11', 20, 3, 0, 6, 'xxxx1111111100000000xxxx|xxxx1111111100000000xxxx|111111111111000000000000|111111111111000000000000|111111111111000000000000|111111111111000000000000|xxxx1111111100000000xxxx|xxxx1111111100000000xxxx|xxxxxxxxxxxx000000000000|xxxxxxxxxxxx000000000000|xxxxxxxxxx00000000000000|xxxxxxxxxx00000000000000|xxxxxxxxxx00000000000000|xxxxxxxxxx00000000000000|xxxxxxxxxxxx000000000000|xxxxxxxxxxxx000000000000|xxxxxxxxxxxx000000000000|xxxxxxxxxxxx000000000000|xxxxxxxx000000000000xxxx|xxxxxxxx000000000000xxxx|xxxxxxxx000000000000xxxx|xxxxxxxx000000000000xxxx', 1),
(82, 'star_lounge', 'star_lounge', 36, 35, 0, 6, 'xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxx2222x4444442222xxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxx22222x444x32222xxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxx22222xx4xx22222xxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxx222222222222222xxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxx222222222222222xxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxx222222222222222xxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxx222222222222222xxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxx222222222222222xxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxx222222222222222xxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxx22222222222222211111xxxxxxxxx|xxxxxxxxxxxxxxxxxxxxx22222222222222211111xxxxxxxxx|xxxxxxxxxxxxxxxxxxxxx22222222222222211111xxxxxxxxx|xxxxxxxxxxxxxxxxxxxxx22222222222222211111xxxxxxxxx|xxxxxxxxxxxxxxxxxxxxx22222222222222222111xxxxxxxxx|xxxxxxxxxxxxxxxxxxxxx22222222222222222111xxxxxxxxx|xxxxxxxxxxxxxxxx3333x22222222222222xxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxx3333x22222222222222xxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxx3333x22222222221111xxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxx3333xx2x22222220000xxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxx333333332222222000000xxxxxxxxxxxxx|xxxxxxxxxxxxxxxx333333332222222x0000000xxxxxxxxxxx|xxxxxxxxxxxxxxxxx33333332222222x0000000xxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxx222222000000xxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 1),
(83, 'orient', 'orient', 32, 20, 1, 6, 'xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxxxx00000000xxxxxxxxxxxx|xxxxxxxxxxxxxx1000000000xxxxxxxxxxxx|xxxxxxxxxxxxxx1xxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxx1xxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxx1xxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxx1xxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxx1xxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxx1xxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxx1xxxxxxxxxxxxxxxxxxxxx|xxxxxxxxxxxxxx1xx000x000xx111x111xxx|xxxxxxxxxxxxxx1xx000x000xxxxxx1111xx|xxxxxxxxxxxxxx1xx000x000x111111111xx|xxxxxxxxxxxxxx1xx000x000x111111111xx|xxx111111111111xx000x000x111111111xx|xxx1xxxxxxxxxxxxx000x000x111111111xx|xxx1x1111111111000000000x111111111xx|xxx1x1111111111000000000xx1111111xxx|xxx1x11xxxxxx11000000000xx1111111100|xxx111xxxxxxx11000000000011111111100|xxx111xxxxxxx11000000000011111111100|xxxxx1xxxxxxx11000000000011111111100|xxxxx11xxxxxx11000000000xx1111111100|xxxxx1111111111000000000xx1111111xxx|xxxxx1111111111xx000x000x111111111xx|xxxxxxxxxxxxxxxxx000x000x111111111xx|xxxxxxxxxxxxxxxxx000x000x111111111xx|xxxxxxxxxxxxxxxxx000x000x111111111xx|xxxxxxxxxxxxxxxxx000x000x111111111xx|xxxxxxxxxxxxxxxxx000x00xx11xxxx111xx|xxxxxxxxxxxxxxxxxxxxxxxxxx11111111xx|', 1);

-- --------------------------------------------------------

--
-- Table structure for table `rooms_rights`
--

CREATE TABLE `rooms_rights` (
  `user_id` int(11) NOT NULL,
  `room_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `schema_migrations`
--

CREATE TABLE `schema_migrations` (
  `version` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `schema_migrations`
--

INSERT INTO `schema_migrations` (`version`) VALUES
('20180605202455'),
('20180605202822'),
('20180605203204'),
('20180605204755'),
('20180605205011'),
('20180605205211'),
('20180605205405'),
('20180605205543'),
('20180605205848'),
('20180605210028'),
('20180605210128'),
('20180605210255'),
('20180605210408'),
('20180605210538'),
('20180605210815'),
('20180605210848'),
('20180605210954'),
('20180605211041'),
('20180605211154'),
('20180605211259'),
('20180605211518'),
('20180605211722'),
('20180606130528'),
('20180606163724'),
('20180606171138'),
('20180609193613'),
('20180609193620'),
('20180609193627'),
('20180610045026'),
('20180610071051'),
('20180610084935'),
('20180610105026'),
('20180616170631'),
('20180622105649'),
('20180715121437'),
('20180715152740'),
('20180724140234'),
('20180729161108'),
('20180730120357'),
('20180802105259'),
('20180804021505');

-- --------------------------------------------------------

--
-- Table structure for table `settings`
--

CREATE TABLE `settings` (
  `setting` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `value` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `soundmachine_playlists`
--

CREATE TABLE `soundmachine_playlists` (
  `item_id` int(11) NOT NULL,
  `song_id` int(11) NOT NULL,
  `slot_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `soundmachine_songs`
--

CREATE TABLE `soundmachine_songs` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL DEFAULT 0,
  `title` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `item_id` int(11) NOT NULL,
  `length` int(3) NOT NULL DEFAULT 0,
  `data` text COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `burnt` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `soundmachine_tracks`
--

CREATE TABLE `soundmachine_tracks` (
  `soundmachine_id` int(11) NOT NULL,
  `track_id` int(11) NOT NULL,
  `slot_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `figure` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `pool_figure` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `sex` char(1) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'M',
  `motto` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'de kepler whey',
  `credits` int(11) NOT NULL DEFAULT 200,
  `tickets` int(11) NOT NULL DEFAULT 0,
  `film` int(11) NOT NULL DEFAULT 0,
  `rank` tinyint(1) UNSIGNED NOT NULL DEFAULT 1,
  `console_motto` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'I''m a new user!',
  `last_online` int(11) NOT NULL DEFAULT 0,
  `sso_ticket` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `club_subscribed` bigint(11) NOT NULL DEFAULT 0,
  `club_expiration` bigint(11) NOT NULL DEFAULT 0,
  `badge` char(3) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `badge_active` tinyint(1) NOT NULL DEFAULT 1,
  `allow_stalking` tinyint(1) NOT NULL DEFAULT 1,
  `sound_enabled` tinyint(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users_badges`
--

CREATE TABLE `users_badges` (
  `user_id` int(11) NOT NULL,
  `badge` char(3) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users_room_favourites`
--

CREATE TABLE `users_room_favourites` (
  `room_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users_room_votes`
--

CREATE TABLE `users_room_votes` (
  `user_id` int(11) NOT NULL,
  `room_id` int(11) NOT NULL,
  `vote` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `catalogue_items`
--
ALTER TABLE `catalogue_items`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `items`
--
ALTER TABLE `items`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`);

--
-- Indexes for table `items_definitions`
--
ALTER TABLE `items_definitions`
  ADD UNIQUE KEY `id` (`id`);

--
-- Indexes for table `items_moodlight_presets`
--
ALTER TABLE `items_moodlight_presets`
  ADD PRIMARY KEY (`item_id`);

--
-- Indexes for table `items_photos`
--
ALTER TABLE `items_photos`
  ADD PRIMARY KEY (`photo_id`),
  ADD UNIQUE KEY `photo_id` (`photo_id`);

--
-- Indexes for table `items_teleporter_links`
--
ALTER TABLE `items_teleporter_links`
  ADD UNIQUE KEY `item_id` (`item_id`);

--
-- Indexes for table `messenger_messages`
--
ALTER TABLE `messenger_messages`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`);

--
-- Indexes for table `rooms`
--
ALTER TABLE `rooms`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`);

--
-- Indexes for table `rooms_categories`
--
ALTER TABLE `rooms_categories`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`);

--
-- Indexes for table `rooms_models`
--
ALTER TABLE `rooms_models`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id` (`id`);

--
-- Indexes for table `schema_migrations`
--
ALTER TABLE `schema_migrations`
  ADD PRIMARY KEY (`version`);

--
-- Indexes for table `settings`
--
ALTER TABLE `settings`
  ADD PRIMARY KEY (`setting`);

--
-- Indexes for table `soundmachine_playlists`
--
ALTER TABLE `soundmachine_playlists`
  ADD KEY `machineid` (`item_id`),
  ADD KEY `songid` (`song_id`);

--
-- Indexes for table `soundmachine_songs`
--
ALTER TABLE `soundmachine_songs`
  ADD UNIQUE KEY `id` (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`);

--
-- Indexes for table `users_badges`
--
ALTER TABLE `users_badges`
  ADD KEY `users_badges_users_FK` (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `catalogue_items`
--
ALTER TABLE `catalogue_items`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=515;

--
-- AUTO_INCREMENT for table `items`
--
ALTER TABLE `items`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `items_definitions`
--
ALTER TABLE `items_definitions`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=424;

--
-- AUTO_INCREMENT for table `messenger_messages`
--
ALTER TABLE `messenger_messages`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `rooms`
--
ALTER TABLE `rooms`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1000;

--
-- AUTO_INCREMENT for table `rooms_categories`
--
ALTER TABLE `rooms_categories`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=121;

--
-- AUTO_INCREMENT for table `rooms_models`
--
ALTER TABLE `rooms_models`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=84;

--
-- AUTO_INCREMENT for table `soundmachine_songs`
--
ALTER TABLE `soundmachine_songs`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `users_badges`
--
ALTER TABLE `users_badges`
  ADD CONSTRAINT `users_badges_users_FK` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
