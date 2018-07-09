-- migrate:up
UPDATE items_definitions SET sprite = 'pedowall' WHERE id = 251;

-- migrate:down
UPDATE items_definitions SET sprite = 'poster' WHERE id = 251;
