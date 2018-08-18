-- migrate:up
UPDATE `public_items` SET rotation = 7 WHERE id = 'e705';
UPDATE `public_items` SET rotation = 1 WHERE id = 'z704';
UPDATE `public_items` SET rotation = 3 WHERE id = 'y934';
UPDATE `public_items` SET rotation = 5 WHERE id = 'f589';

UPDATE rooms_models SET trigger_class = 'flat_trigger' WHERE trigger_class = 'FlatTrigger';
UPDATE rooms_models SET trigger_class = 'battleball_lobby_trigger' WHERE trigger_class = 'BattleballLobbyTrigger';
UPDATE rooms_models SET trigger_class = 'snowstorm_lobby_trigger' WHERE trigger_class = 'SnowstormLobbyTrigger';
UPDATE rooms_models SET trigger_class = 'space_cafe_trigger' WHERE trigger_class = 'SpaceCafeTrigger';
UPDATE rooms_models SET trigger_class = 'habbo_lido_trigger' WHERE trigger_class = 'HabboLidoTrigger';
UPDATE rooms_models SET trigger_class = 'rooftop_rumble_trigger' WHERE trigger_class = 'RooftopRumbleTrigger';
UPDATE rooms_models SET trigger_class = 'diving_deck_trigger' WHERE trigger_class = 'DivingDeckTrigger';
UPDATE rooms_models SET trigger_class = 'none' WHERE trigger_class = '';

ALTER TABLE `rooms_models` CHANGE `trigger_class` `trigger_class` ENUM('flat_trigger','battleball_lobby_trigger','snowstorm_lobby_trigger','space_cafe_trigger','habbo_lido_trigger','rooftop_rumble_trigger','diving_deck_trigger','none') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL;

INSERT INTO `public_items` (`id`, `room_model`, `sprite`, `x`, `y`, `z`, `rotation`, `top_height`, `length`, `width`, `behaviour`, `current_program`) VALUES
('g243', 'md_a', 'poolEnter', '11', '11', '7', '2', '0.001', '1', '1', 'can_stand_on_top', ''), 
('n678', 'md_a', 'poolExit', '12', '11', '4', '6', '0.001', '1', '1', 'can_stand_on_top', ''),
('h317', 'md_a', 'poolExit', '12', '12', '4', '6', '0.001', '1', '1', 'can_stand_on_top', '');

INSERT INTO `public_items` (`id`, `room_model`, `sprite`, `x`, `y`, `z`, `rotation`, `top_height`, `length`, `width`, `behaviour`, `current_program`) VALUES
('t743', 'hallD', 'streetlight', '0', '9', '1', '0', '0.001', '1', '1', 'solid', ''),
('j332', 'hallD', 'streetlight', '0', '15', '1', '0', '0.001', '1', '1', 'solid', ''),
('w621', 'hallD', 'streetlight', '8', '1', '1', '0', '0.001', '1', '1', 'solid', ''),
('o742', 'hallD', 'streetlight', '14', '1', '1', '0', '0.001', '1', '1', 'solid', '');

INSERT INTO `public_items` (`id`, `room_model`, `sprite`, `x`, `y`, `z`, `rotation`, `top_height`, `length`, `width`, `behaviour`, `current_program`) VALUES ('t546', 'pub_a', 'bar_gate', '9', '9', '2', '0', '0.001', '1', '1', 'solid,invisible', '');

UPDATE public_items SET top_height = '1.5' WHERE sprite = 'pub_chair' AND room_model = 'pub_a';

UPDATE public_items SET behaviour = 'can_stand_on_top,invisible' WHERE sprite IN ('poolEnter','poolExit','poolBooth','poolLift');

UPDATE public_items SET behaviour = 'can_sit_on_top',top_height = '1.0' WHERE sprite = 'table2' AND room_model = 'beauty_salon0';

UPDATE public_items SET behaviour = 'can_sit_on_top',top_height = '1.0' WHERE sprite IN ('elephantcouch1','elephantcouch2','elephantcouch3','elephantcouch4') AND room_model = 'club_mammoth';

UPDATE public_items SET behaviour = 'can_sit_on_top',top_height = '1.0' WHERE sprite IN ('gate_table','gate_table1','gate_table2','gate_table3');

INSERT INTO `public_items` (`id`, `room_model`, `sprite`, `x`, `y`, `z`, `rotation`, `top_height`, `length`, `width`, `behaviour`, `current_program`) VALUES
('b523', 'library', 'invisible_table', 28, 28, 1, 0, 0.001, 2, 2, 'solid,invisible', '');

INSERT INTO `public_items` (`id`, `room_model`, `sprite`, `x`, `y`, `z`, `rotation`, `top_height`, `length`, `width`, `behaviour`, `current_program`) VALUES
('j635', 'bar_b', 'djtable', 16, 22, 4, 0, 0.001, 1, 2, 'solid,invisible', '');

-- migrate:down

