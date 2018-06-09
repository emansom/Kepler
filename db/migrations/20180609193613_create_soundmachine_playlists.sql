-- migrate:up
CREATE TABLE `soundmachine_playlists` (
  `machineid` int(10) NOT NULL,
  `songid` int(10) NOT NULL,
  `pos` int(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- migrate:down
DROP TABLE `soundmachine_playlists`;
