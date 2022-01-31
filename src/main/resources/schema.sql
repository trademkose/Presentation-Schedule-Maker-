CREATE TABLE IF NOT EXISTS `presentation` (
    `id` bigint(20) NOT NULL auto_increment,
    `name` varchar(200) NOT NULL,
    `minute` bigint(20) NOT NULL,
    PRIMARY KEY (`id`)    
);