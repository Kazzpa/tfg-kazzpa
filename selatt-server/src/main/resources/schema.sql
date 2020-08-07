
CREATE TABLE `algorithm` (
  `name` varchar(255) NOT NULL,
  `language` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`name`)
);


CREATE TABLE `dataset` (
  `id` varchar(255) NOT NULL,
  `file_download_uri` varchar(255) DEFAULT NULL,
  `file_type` varchar(255) DEFAULT NULL,
  `filename` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `size` bigint NOT NULL,
  `user_uploader_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKg7g2l49cqd6w9fblgdr2cbehq` (`filename`,`file_download_uri`),
  KEY `FKn66t6yt1dqtgfdiyejqby0bk` (`user_uploader_id`),
  CONSTRAINT `FKn66t6yt1dqtgfdiyejqby0bk` FOREIGN KEY (`user_uploader_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
);


CREATE TABLE `result_filter` (
  `id` varchar(255) NOT NULL,
  `finished_date` datetime(6) DEFAULT NULL,
  `json_attributes` varchar(1024) DEFAULT NULL,
  `scorevns` bigint NOT NULL,
  `algorithm_name` varchar(255) DEFAULT NULL,
  `performed_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKmulfx2fo1fhddf2ppobs9gjo3` (`algorithm_name`),
  KEY `FK4h9afkcb6d0nsyovjd7op1gy1` (`performed_id`),
  CONSTRAINT `FK4h9afkcb6d0nsyovjd7op1gy1` FOREIGN KEY (`performed_id`) REFERENCES `dataset` (`id`),
  CONSTRAINT `FKmulfx2fo1fhddf2ppobs9gjo3` FOREIGN KEY (`algorithm_name`) REFERENCES `algorithm` (`name`)
);


CREATE TABLE `user` (
  `id` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKsb8bbouer5wak8vyiiy4pf2bx` (`username`)
);