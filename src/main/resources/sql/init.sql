
DROP TABLE IF EXISTS `user_info`;

CREATE TABLE `user_info` (
                           `userid` INT AUTO_INCREMENT PRIMARY KEY NOT NULL  COMMENT 'UUID',
                           `openid` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户登录名',
                           `user_name` varchar(70) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '用户密码',
                           `nick_name` varchar(30) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '昵称，建议为真实姓名',
                           `phone_number` varchar(15) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手机号',
                           `email` varchar(25) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮件地址',
                           `identity` varchar(25) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '身份信息',
                           `department` varchar(25) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '部门',
                           `grade` varchar(25) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '年级',
                           `student_id` varchar(25) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '学号',
                           `comment` text COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
                           `authority` INT NOT NULL COMMENT '权限',
                           UNIQUE KEY (`openid`),
                           KEY `normalIndex` (`userid`,`openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

