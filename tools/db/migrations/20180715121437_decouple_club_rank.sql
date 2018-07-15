-- migrate:up
UPDATE users SET rank = 1, credits = credits + 25 WHERE rank = 2;

-- migrate:down

