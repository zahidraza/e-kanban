INSERT INTO `user` (user_id,email,mobile,username,password,role) VALUES
(1,'zahid7292@gmail.com','8987525008','Md Zahid Raza','$2a$10$77hLDsxw.pDU5eG/cPqlgOjQ8UQ.YLZjK2qHMmN3sD.qbkL8EOn/a','ROLE_ADMIN'),
(2,'jawed@gmail.com','8987525008','Md Jawed Akhtar','$2a$10$77hLDsxw.pDU5eG/cPqlgOjQ8UQ.YLZjK2qHMmN3sD.qbkL8EOn/a','ROLE_USER'),
(3,'taufeeque8@gmail.com','8987525008','Md Taufeeque Alam','$2a$10$77hLDsxw.pDU5eG/cPqlgOjQ8UQ.YLZjK2qHMmN3sD.qbkL8EOn/a','ROLE_USER');

INSERT INTO `category` (id,name) VALUES
(1,'Test Category 1'),
(2,'Test Category 2'),
(3,'Test Category 3');

INSERT INTO `sub_category` (id,name,category_id) VALUES
(1,'Test Sub Category 1',1),
(2,'Test Sub Category 2',1),
(3,'Test Sub Category 3',1);

INSERT INTO `product` (id,bin_qty,class_type,conversion_factor,demand,description,item_code,kanban_type,min_order_qty,
name,no_of_bins,packet_size,price,time_buffer,time_ordering,time_procurement,time_transportion,uom_consumption,uom_purchase,sub_category_id) VALUES
(1,NULL,NULL,1.50,NULL,NULL,NULL,NULL,1000,'Test Product 1',NULL,50,100.00,4,3,1,2,'DOZEN','KG',1),
(2,NULL,NULL,1.50,NULL,NULL,NULL,NULL,1000,'Test Product 2',NULL,50,100.00,4,3,1,2,'DOZEN','KG',1),
(3,NULL,NULL,1.50,NULL,NULL,NULL,NULL,1000,'Test Product 3',NULL,50,100.00,4,3,1,2,'DOZEN','KG',1);


INSERT INTO `section` (id,name) VALUES
(1,'Test Section 1'),
(2,'Test Section 2'),
(3,'Test Section 3');

INSERT INTO `supplier` (id,city,country,landmark,state,street,zip_code,contact_person,name,supplier_type) VALUES
(1,'BLR','IN',NULL,NULL,'Test Street 1','560001','Test Contact Person 1','Test Supplier 1','LOCAL'),
(2,'BLR','IN',NULL,NULL,'Test Street 1','560001','Test Contact Person 2','Test Supplier 2','NON_LOCAL'),
(3,'BLR','IN',NULL,NULL,'Test Street 3','560001','Test Contact Person 3','Test Supplier 3','LOCAL');

/*
CREATE TABLE `user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `last_updated` datetime DEFAULT NULL,
  `mobile` varchar(255) DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(255) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UK_ob8kqyqqgmefl0aco34akdtpe` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
CREATE TABLE `product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bin_qty` bigint(20) DEFAULT NULL,
  `calss_type` varchar(255) DEFAULT NULL,
  `conversion_factor` decimal(19,2) NOT NULL,
  `demand` bigint(20) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `item_code` varchar(255) DEFAULT NULL,
  `kanban_type` varchar(255) DEFAULT NULL,
  `last_updated` datetime DEFAULT NULL,
  `min_order_qty` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `no_of_bins` int(11) DEFAULT NULL,
  `packet_size` int(11) NOT NULL,
  `price` decimal(19,2) NOT NULL,
  `time_buffer` int(11) NOT NULL,
  `time_ordering` int(11) NOT NULL,
  `time_procurement` int(11) NOT NULL,
  `time_transportion` int(11) NOT NULL,
  `uom_consumption` varchar(255) NOT NULL,
  `uom_purchase` varchar(255) NOT NULL,
  `sub_category_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKd9gfnsjgfwjtaxl57udrbocsp` (`sub_category_id`),
  CONSTRAINT `FKd9gfnsjgfwjtaxl57udrbocsp` FOREIGN KEY (`sub_category_id`) REFERENCES `sub_category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

CREATE TABLE `supplier` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `city` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `landmark` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `street` varchar(255) DEFAULT NULL,
  `zip_code` varchar(255) DEFAULT NULL,
  `contact_person` varchar(255) NOT NULL,
  `last_updated` datetime DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `supplier_type` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_c3fclhmodftxk4d0judiafwi3` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
*/
