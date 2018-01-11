BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS `users` (
	`id`	int ( 11 ) NOT NULL,
	`username`	varchar ( 150 ) DEFAULT NULL,
	`password`	varchar ( 150 ) DEFAULT NULL,
	`figure`	varchar ( 200 ) NOT NULL,
	`sex`	varchar ( 1 ) NOT NULL,
	`motto`	varchar ( 50 ) NOT NULL,
	`credits`	int ( 11 ) NOT NULL DEFAULT '0',
	`tickets`	int ( 11 ) NOT NULL DEFAULT '0',
	`film`	int ( 11 ) NOT NULL DEFAULT '0'
);
INSERT INTO `users` VALUES (1,'alex','123','lulz','M','',1337,0,0);
COMMIT;
