CREATE TABLE IF NOT EXISTS `user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `last_updated` datetime DEFAULT NULL,
  `mobile` varchar(255) DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(255) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UK_ob8kqyqqgmefl0aco34akdtpe` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `last_updated` datetime DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_46ccwnsi9409t36lurvtyljak` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `sub_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `last_updated` datetime DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `category_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_pxgji9v439t19r7qg0henfshu` (`name`),
  KEY `FKl65dyy5me2ypoyj8ou1hnt64e` (`category_id`),
  CONSTRAINT `FKl65dyy5me2ypoyj8ou1hnt64e` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bin_qty` bigint(20) DEFAULT NULL,
  `class_type` varchar(255) DEFAULT NULL,
  `conversion_factor` decimal(19,2) NOT NULL,
  `demand` bigint(20) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `ignore_sync` bit(1) DEFAULT NULL,
  `item_code` varchar(255) DEFAULT NULL,
  `kanban_type` varchar(255) DEFAULT NULL,
  `last_updated` datetime DEFAULT NULL,
  `min_order_qty` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `no_of_bins` int(11) DEFAULT NULL,
  `ordered_qty` bigint(20) NOT NULL,
  `packet_size` decimal(19,2) NOT NULL,
  `price` decimal(19,2) NOT NULL,
  `stk_on_floor` bigint(20) NOT NULL,
  `time_buffer` int(11) NOT NULL,
  `time_ordering` int(11) NOT NULL,
  `time_procurement` int(11) NOT NULL,
  `time_transportion` int(11) NOT NULL,
  `total_lead_time` int(11) DEFAULT NULL,
  `uom_consumption` varchar(255) NOT NULL,
  `uom_purchase` varchar(255) NOT NULL,
  `sub_category_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKd9gfnsjgfwjtaxl57udrbocsp` (`sub_category_id`),
  CONSTRAINT `FKd9gfnsjgfwjtaxl57udrbocsp` FOREIGN KEY (`sub_category_id`) REFERENCES `sub_category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `consumption` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `last_updated` datetime DEFAULT NULL,
  `month` int(11) DEFAULT NULL,
  `value` bigint(20) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `product_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `IDX1or8svduruiylnmg3rafwlr3n` (`product_id`),
  CONSTRAINT `FKpqu32kvws39mdn55qd7vyrxum` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `inventory` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bin_no` int(11) NOT NULL,
  `bin_state` varchar(255) NOT NULL,
  `last_updated` datetime DEFAULT NULL,
  `product_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKp7gj4l80fx8v0uap3b2crjwp5` (`product_id`),
  CONSTRAINT `FKp7gj4l80fx8v0uap3b2crjwp5` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `orders` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bins` varchar(255) NOT NULL,
  `completed_at` datetime DEFAULT NULL,
  `last_updated` datetime DEFAULT NULL,
  `order_state` varchar(255) DEFAULT NULL,
  `ordered_at` datetime DEFAULT NULL,
  `ordered_by` bigint(20) DEFAULT NULL,
  `product_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKnqxvs3gblolqvfbqk35psmp0q` (`ordered_by`),
  KEY `FK787ibr3guwp6xobrpbofnv7le` (`product_id`),
  CONSTRAINT `FK787ibr3guwp6xobrpbofnv7le` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `FKnqxvs3gblolqvfbqk35psmp0q` FOREIGN KEY (`ordered_by`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `section` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `last_updated` datetime DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_rwqtt5x96oahjdtqt20vxu4um` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `supplier` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `city` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `landmark` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `street` varchar(255) DEFAULT NULL,
  `zip_code` varchar(255) DEFAULT NULL,
  `contact_person` varchar(255) DEFAULT NULL,
  `last_updated` datetime DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `supplier_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_c3fclhmodftxk4d0judiafwi3` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `product_section` (
  `product_id` bigint(20) NOT NULL,
  `section_id` bigint(20) NOT NULL,
  PRIMARY KEY (`product_id`,`section_id`),
  KEY `FK7oly04vcl2d8coirk9q9e6479` (`section_id`),
  CONSTRAINT `FK6ihiqhtu9um4qu7w0q1djvjf5` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `FK7oly04vcl2d8coirk9q9e6479` FOREIGN KEY (`section_id`) REFERENCES `section` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `product_supplier` (
  `product_id` bigint(20) NOT NULL,
  `supplier_id` bigint(20) NOT NULL,
  PRIMARY KEY (`product_id`,`supplier_id`),
  KEY `FKojmkj7n4g02l3vj0lf10j7rer` (`supplier_id`),
  CONSTRAINT `FK9ycab4fchfe9g9uxleti557pv` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `FKojmkj7n4g02l3vj0lf10j7rer` FOREIGN KEY (`supplier_id`) REFERENCES `supplier` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
