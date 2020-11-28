
DROP TABLE IF EXISTS `user_info`;

CREATE TABLE `user_info` (
                           `userid` INT AUTO_INCREMENT PRIMARY KEY NOT NULL  COMMENT 'UUID',
                           `openid` varchar(40)  NOT NULL COMMENT '用户登录名',
                           `user_name` varchar(70)  COMMENT '用户密码',
                           `nick_name` varchar(30)  DEFAULT NULL COMMENT '昵称，建议为真实姓名',
                           `phone_number` varchar(15)  DEFAULT NULL COMMENT '手机号',
                           `email` varchar(25)  DEFAULT NULL COMMENT '邮件地址',
                           `identity` varchar(25)  DEFAULT NULL COMMENT '身份信息',
                           `department` varchar(25)  DEFAULT NULL COMMENT '部门',
                           `grade` varchar(25)  DEFAULT NULL COMMENT '年级',
                           `student_id` varchar(25)  DEFAULT NULL COMMENT '学号',
                           `comment` text  DEFAULT NULL COMMENT '备注',
                           `authority` INT NOT NULL COMMENT '权限',
                           UNIQUE KEY (`openid`),
                           KEY `normalIndex` (`userid`,`openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DROP TABLE IF EXISTS `service_form`;

CREATE TABLE `service_form` (
                             `formid` INT AUTO_INCREMENT PRIMARY KEY NOT NULL  COMMENT '表单id',
                             `brand` varchar(100) DEFAULT NULL COMMENT '电脑品牌',
                             `computer_model` varchar(100) DEFAULT NULL COMMENT '电脑型号',
                             `cpu_model` varchar(100)  DEFAULT NULL COMMENT 'CPU型号',
                             `has_discrete_graphics` TINYINT  DEFAULT NULL COMMENT '是否有独显',
                             `graphics_model` varchar(100)  DEFAULT NULL COMMENT '显卡型号',
                             `laptop_type` varchar(100)  DEFAULT NULL COMMENT '笔记本类型',
                             `bought_time` DATE  DEFAULT NULL COMMENT '购买时间',
                             `is_under_warranty` TINYINT  DEFAULT NULL COMMENT '是否在保',
                             `problem_description` TEXT  DEFAULT NULL COMMENT '问题描述',
                             `problem_type` varchar(100)  DEFAULT NULL COMMENT '问题类型',
                             `decription_editing_advice` varchar(100) NOT NULL COMMENT '描述修改建议',
                             `repairing_result` varchar(100) NOT NULL COMMENT '志愿者填写的维修结果',
                             `status` INT NOT NULL COMMENT '维修单状态',
                             `feedback` varchar(100) NOT NULL COMMENT '用户反馈信息',
                             `activity_id` INT NOT NULL COMMENT '预约活动id',
                             `time_slot` INT NOT NULL COMMENT '预约时间段',

                             KEY `normalIndex` (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `user_service_form_merge` (
                                `userid` INT NOT NULL,
                                `formid` INT NOT NULL  COMMENT '表单id',
                                KEY `normalIndex` (`userid`, `formid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `volunteer_application` (
                                           `formid` INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
                                           `comment` varchar(100) NOT NULL,
                                           `status` tinyint,
                                           KEY `normalIndex` (`formid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `user_volunteer_application_merge` (
                                         `formid` INT NOT NULL,
                                         `userid` INT NOT NULL,
                                         KEY `normalIndex` (`formid`, `userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `activity_info` (
                                                    `activity_id` INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
                                                    `position` varchar(100) NOT NULL,
                                                    `starting_time` DATETIME,
                                                    KEY `normalIndex` (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;