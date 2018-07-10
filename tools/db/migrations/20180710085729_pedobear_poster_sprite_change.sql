-- migrate:up
INSERT INTO items_definitions (id, sprite, colour, `length`, width, top_height, behaviour) VALUES (415, 'pedowall', '', 0, 0, 0, 'wall_item');
UPDATE catalogue_items SET definition_id = 415 WHERE sale_code = 'poster_pedobear';

-- migrate:down
DELETE FROM items_definitions WHERE sprite = 'pedowall';
UPDATE catalogue_items SET definition_id = 251 WHERE sale_code = 'poster_pedobear';
