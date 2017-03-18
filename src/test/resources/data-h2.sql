INSERT INTO `user` VALUES 
(1,'zahid7292@gmail.com','8987525008','Md Zahid Raza','$2a$10$77hLDsxw.pDU5eG/cPqlgOjQ8UQ.YLZjK2qHMmN3sD.qbkL8EOn/a','ROLE_ADMIN'),
(2,'jawed@gmail.com','8987525008','Md Jawed Akhtar','$2a$10$77hLDsxw.pDU5eG/cPqlgOjQ8UQ.YLZjK2qHMmN3sD.qbkL8EOn/a','ROLE_USER'),
(3,'taufeeque8@gmail.com','8987525008','Md Taufeeque Alam','$2a$10$77hLDsxw.pDU5eG/cPqlgOjQ8UQ.YLZjK2qHMmN3sD.qbkL8EOn/a','ROLE_USER');

INSERT INTO `category` VALUES 
(1,'Test Category 1');

INSERT INTO `sub_category` VALUES 
(1,'Test Sub Category 1',1),
(2,'Test Sub Category 1',1);

INSERT INTO `product` VALUES 
(1,NULL,NULL,1.50,20000,NULL,NULL,NULL,1000,'Product 1',NULL,10,1.00,4,1,2,3,'DOZEN','KG',1),
(2,NULL,NULL,456.56,20000,NULL,NULL,NULL,1000,'Product 2',NULL,10,5234523456.34,4,1,2,3,'DOZEN','KG',1),
(3,NULL,NULL,456.56,20000,NULL,NULL,NULL,1000,'Product 3',NULL,10,5234523456.34,4,1,2,3,'DOZEN','KG',2);
/*
CREATE TABLE `product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bin_qty` bigint(20) DEFAULT NULL,
  `calss_type` varchar(255) DEFAULT NULL,
  `conversion_factor` decimal(19,2) NOT NULL,
  `demand` bigint(20) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `item_code` varchar(255) DEFAULT NULL,
  `kanban_type` varchar(255) DEFAULT NULL,
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
*/
