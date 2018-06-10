-- migrate:up
UPDATE `items_definitions` SET `behaviour` = 'solid,custom_data_on_off' WHERE `id` = 83;

-- migrate:down

