-- migrate:up
ALTER TABLE `catalogue_items`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `items`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`);

ALTER TABLE `items_definitions`
  ADD UNIQUE KEY `id` (`id`);

ALTER TABLE `items_teleporter_links`
  ADD UNIQUE KEY `item_id` (`item_id`);

ALTER TABLE `messenger_messages`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`);

ALTER TABLE `rooms`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`);

ALTER TABLE `rooms_categories`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`);

ALTER TABLE `rooms_models`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id` (`id`);

ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`);

ALTER TABLE `users_badges`
  ADD KEY `users_badges_users_FK` (`user_id`);

ALTER TABLE `soundmachine_songs`
  ADD UNIQUE KEY `id` (`id`);

ALTER TABLE `soundmachine_playlists`
  ADD KEY `machineid` (`item_id`),
  ADD KEY `songid` (`song_id`);
  
ALTER TABLE `site_settings`
  ADD UNIQUE KEY `setting` (`setting`);
COMMIT;

-- migrate:down

