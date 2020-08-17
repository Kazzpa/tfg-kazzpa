
CREATE TABLE `algorithm` (
  `name` varchar(255) NOT NULL,
  `language` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `classifier_result` (
  `id` varchar(255) NOT NULL,
  `correctly_classified` double DEFAULT NULL,
  `finished_date` datetime(6) DEFAULT NULL,
  `mean_absolute_error` double DEFAULT NULL,
  `num_instances` double DEFAULT NULL,
  `seen` bit(1) NOT NULL,
  `algorithm_name` varchar(255) DEFAULT NULL,
  `feature_algorithm_name` varchar(255) DEFAULT NULL,
  `performed_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1un9my7slm4j1te6vm9adrsyy` (`algorithm_name`),
  KEY `FKfe81japge8sc64yh996ps2rq5` (`feature_algorithm_name`),
  KEY `FK2eeojv09s48yuknp7ktkutkmm` (`performed_id`),
  CONSTRAINT `FK1un9my7slm4j1te6vm9adrsyy` FOREIGN KEY (`algorithm_name`) REFERENCES `algorithm` (`name`),
  CONSTRAINT `FK2eeojv09s48yuknp7ktkutkmm` FOREIGN KEY (`performed_id`) REFERENCES `dataset` (`id`),
  CONSTRAINT `FKfe81japge8sc64yh996ps2rq5` FOREIGN KEY (`feature_algorithm_name`) REFERENCES `algorithm` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `feature_result` (
  `id` varchar(255) NOT NULL,
  `attributes_selected` varchar(1024) DEFAULT NULL,
  `finished_date` datetime(6) DEFAULT NULL,
  `algorithm_name` varchar(255) NOT NULL,
  `performed_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK7yggwig59ibbhqn7sb4537xid` (`algorithm_name`),
  KEY `FKe6lv328s1dn1qkase38k87pia` (`performed_id`),
  CONSTRAINT `FK7yggwig59ibbhqn7sb4537xid` FOREIGN KEY (`algorithm_name`) REFERENCES `algorithm` (`name`) ON DELETE CASCADE,
  CONSTRAINT `FKe6lv328s1dn1qkase38k87pia` FOREIGN KEY (`performed_id`) REFERENCES `dataset` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `user` (
  `id` varchar(255) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKsb8bbouer5wak8vyiiy4pf2bx` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

