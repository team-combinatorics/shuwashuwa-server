DROP DATABASE IF EXISTS `shuwashuwa`;
CREATE DATABASE `shuwashuwa` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `shuwashuwa`;

DROP TABLE IF EXISTS `activity_info`;
CREATE TABLE `activity_info` (
    `id` INT UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '自增长，活动id',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `position` VARCHAR(100) DEFAULT NULL COMMENT '预约的教室位置',
    `starting_time` DATETIME DEFAULT NULL COMMENT '预计开始时间',
    PRIMARY KEY pk_id(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DROP TABLE IF EXISTS `service_form`;
CREATE TABLE `service_form` (
    `id` INT UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '自增长的表单id',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `brand` VARCHAR(15) DEFAULT NULL COMMENT '电脑品牌',
    `computer_model` VARCHAR(30) DEFAULT NULL COMMENT '电脑型号',
    `cpu_model` VARCHAR(30) DEFAULT NULL COMMENT 'cpu型号',
    `has_discrete_graphics` TINYINT DEFAULT NULL COMMENT '是否有独显',
    `graphics_model` VARCHAR(30) DEFAULT NULL COMMENT '显卡型号',
    `laptop_type` VARCHAR(30) DEFAULT NULL COMMENT '笔记本类型',
    `bought_time` DATE DEFAULT NULL COMMENT '购买时间',
    `is_under_warranty` BOOLEAN DEFAULT NULL COMMENT '是否在保',
    `problem_description` VARCHAR(100) DEFAULT NULL COMMENT '问题描述',
    `problem_type` VARCHAR(10) DEFAULT NULL COMMENT '问题类型（硬件/软件）',
    `decription_editing_advice` VARCHAR(100) DEFAULT NULL COMMENT '描述修改建议',
    `repairing_result` VARCHAR(100) DEFAULT NULL COMMENT '志愿者填写的维修结果',
    `status` TINYINT DEFAULT NULL COMMENT '维修单状态',
    `feedback` VARCHAR(100) DEFAULT NULL COMMENT '用户反馈信息',
    `activity_id` INT DEFAULT NULL COMMENT '预约活动id',
    `time_slot` INT DEFAULT NULL COMMENT '预约时间段',
    INDEX idx_activity_id(activity_id),
    PRIMARY KEY pk_id(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id` INT UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '自增长',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
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
    `is_volunteer` BOOLEAN NOT NULL DEFAULT 0 COMMENT '是否为志愿者',
    `is_admin` BOOLEAN NOT NULL DEFAULT 0 COMMENT '是否为管理员',
    `is_su` BOOLEAN NOT NULL DEFAULT 0 COMMENT '是否为超管',
    INDEX idx_user_name(user_name),
    INDEX idx_phone_number(phone_number),
    INDEX idx_email(email),
    INDEX idx_is_volunteer(is_volunteer),
    INDEX idx_is_admin(is_admin),
    INDEX idx_is_su(is_su),
    UNIQUE uk_openid(openid),
    PRIMARY KEY pk_id(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DROP TABLE IF EXISTS `volunteer_application`;
CREATE TABLE `volunteer_application` (
    `id` INT UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '自增长id',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `user_id` INT UNSIGNED NOT NULL COMMENT '申请用户的id',
    `reason_for_application` VARCHAR(100) DEFAULT NULL COMMENT '申请理由',
    `reply_by_admin` VARCHAR(100) DEFAULT NULL COMMENT '管理员回复',
    `status` TINYINT DEFAULT 0 COMMENT '申请状态',
    INDEX idx_user_id(user_id),
    INDEX idx_status(status),
    PRIMARY KEY pk_id(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DROP TABLE IF EXISTS `r_user_service_form`;
CREATE TABLE `r_user_service_form` (
    `id` INT UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '自增长',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `user_id` INT NOT NULL COMMENT 'userid',
    `service_form_id` INT NOT NULL COMMENT 'formid',
    INDEX idx_user_id(user_id),
    INDEX idx_service_form_id(service_form_id),
    UNIQUE uk_id(id),
    PRIMARY KEY pk_id(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DROP TABLE IF EXISTS `r_user_volunteer_application`;
CREATE TABLE `r_user_volunteer_application` (
    `id` INT UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '自增长',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `application_form_id` INT NOT NULL COMMENT '表单id',
    `user_id` INT NOT NULL COMMENT '用户id',
    INDEX idx_application_form_id(application_form_id),
    INDEX idx_user_id(user_id),
    PRIMARY KEY pk_id(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

