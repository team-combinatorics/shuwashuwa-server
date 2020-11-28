DROP TABLE IF EXISTS `activity_info`;
CREATE TABLE `activity_info` (
                                 `activity_id` INT AUTO_INCREMENT PRIMARY KEY NOT NULL COMMENT '自增长，活动id',
                                 `position` VARCHAR(100) DEFAULT NULL COMMENT '预约的教室位置',
                                 `tarting_time` DATETIME DEFAULT NULL COMMENT '预计开始时间',
                                 UNIQUE KEY (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DROP TABLE IF EXISTS `service_form`;
CREATE TABLE `service_form` (
                                `formid` INT AUTO_INCREMENT PRIMARY KEY NOT NULL COMMENT '自增长的表单id',
                                `brand` VARCHAR(15) DEFAULT NULL COMMENT '电脑品牌',
                                `computer_model` VARCHAR(30) DEFAULT NULL COMMENT '电脑型号',
                                `cpu_model` VARCHAR(30) DEFAULT NULL COMMENT 'cpu型号',
                                `has_discrete_graphics` TINYINT DEFAULT NULL COMMENT '是否有独显',
                                `graphics_model` VARCHAR(30) DEFAULT NULL COMMENT '显卡型号',
                                `laptop_type` VARCHAR(30) DEFAULT NULL COMMENT '笔记本类型',
                                `bought_time` DATE DEFAULT NULL COMMENT '购买时间',
                                `is_under_warranty` TINYINT DEFAULT NULL COMMENT '是否在保',
                                `problem_description` VARCHAR(100) DEFAULT NULL COMMENT '问题描述',
                                `problem_type` VARCHAR(10) DEFAULT NULL COMMENT '问题类型（硬件/软件）',
                                `decription_editing_advice` VARCHAR(100) DEFAULT NULL COMMENT '描述修改建议',
                                `repairing_result` VARCHAR(100) DEFAULT NULL COMMENT '志愿者填写的维修结果',
                                `status` TINYINT DEFAULT NULL COMMENT '维修单状态',
                                `feedback` VARCHAR(100) DEFAULT NULL COMMENT '用户反馈信息',
                                `activity_id` INT DEFAULT NULL COMMENT '预约活动id',
                                `time_slot` INT DEFAULT NULL COMMENT '预约时间段',
                                KEY `normalIndex` (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
                             `userid` INT AUTO_INCREMENT PRIMARY KEY NOT NULL COMMENT '自增长',
                             `openid` VARCHAR(30) NOT NULL COMMENT '微信提供的用户id',
                             `user_name` VARCHAR(30) DEFAULT NULL COMMENT '用户姓名',
                             `nick_name` VARCHAR(30) DEFAULT NULL COMMENT '用户昵称',
                             `phone_number` VARCHAR(30) DEFAULT NULL COMMENT '用户手机号',
                             `email` VARCHAR(100) DEFAULT NULL COMMENT '用户邮箱',
                             `identity` VARCHAR(40) DEFAULT NULL COMMENT '类别：学生、教职工、校外人员',
                             `department` VARCHAR(40) DEFAULT NULL COMMENT '院系或工作部门',
                             `grade` VARCHAR(40) DEFAULT NULL COMMENT '年级',
                             `student_id` VARCHAR(15) DEFAULT NULL COMMENT '学号',
                             `comment` TEXT DEFAULT NULL COMMENT '备注',
                             `authority` INT NOT NULL COMMENT '权限',
                             UNIQUE KEY (`openid`),
                             KEY `normalIndex` (`userid`,`user_name`,`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DROP TABLE IF EXISTS `volunteer_application`;
CREATE TABLE `volunteer_application` (
                                         `formid` INT AUTO_INCREMENT PRIMARY KEY NOT NULL COMMENT '自增长，表单id',
                                         `comment` VARCHAR(100) DEFAULT NULL COMMENT '申请理由',
                                         `status` TINYINT DEFAULT NULL COMMENT '申请状态',
                                         KEY `normalIndex` (`formid`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

