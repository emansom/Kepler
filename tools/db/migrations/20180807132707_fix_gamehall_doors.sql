-- migrate:up
UPDATE `rooms_models` SET `door_x` = '2', `door_y` = '1' WHERE `rooms_models`.`model_id` = 'hallA';
UPDATE `rooms_models` SET `door_x` = '2', `door_y` = '1' WHERE `rooms_models`.`model_id` = 'hallB';
UPDATE `rooms_models` SET `door_x` = '2', `door_y` = '1' WHERE `rooms_models`.`model_id` = 'hallC';
UPDATE `rooms_models` SET `door_x` = '2', `door_y` = '1' WHERE `rooms_models`.`model_id` = 'hallD';

-- migrate:down

