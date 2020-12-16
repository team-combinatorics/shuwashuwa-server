DROP DATABASE IF EXISTS `shuwashuwa`;
CREATE DATABASE `shuwashuwa` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `shuwashuwa`;

DROP TABLE IF EXISTS `activity_info`;
CREATE TABLE `activity_info`
(
    `id`            INT UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '自增长，活动id',
    `create_time`   DATETIME                    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time`  DATETIME                    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `start_time`    DATETIME                    NOT NULL COMMENT '预计开始时间',
    `end_time`      DATETIME                    NOT NULL COMMENT '预计开始时间',
    `activity_name` VARCHAR(100)                NOT NULL COMMENT '活动名称',
    `location`      VARCHAR(100)                NOT NULL COMMENT '预约的教室位置',
    PRIMARY KEY pk_id (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

DROP TABLE IF EXISTS `activity_time_slot`;
CREATE TABLE `activity_time_slot`
(
    `id`           INT UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '自增长，活动id',
    `create_time`  DATETIME                    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME                    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `activity_id`  INT UNSIGNED                NOT NULL COMMENT '活动id',
    `time_slot`    INT UNSIGNED                NOT NULL COMMENT '活动时间段',
    `start_time`   DATETIME                    NOT NULL COMMENT '时间段开始',
    `end_time`     DATETIME                    NOT NULL COMMENT '时间段结束',
    INDEX idx_activity_id (activity_id),
    PRIMARY KEY pk_id (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`
(
    `id`           INT UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '自增长',
    `create_time`  DATETIME                    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME                    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `userid`       INT UNSIGNED                NOT NULL COMMENT 'user_id',
    `user_name`    VARCHAR(30)                          DEFAULT NULL COMMENT '真实姓名',
    `phone_number` VARCHAR(30)                          DEFAULT NULL COMMENT '用户手机号',
    `email`        VARCHAR(100)                         DEFAULT NULL COMMENT '用户邮箱',
    `identity`     VARCHAR(40)                          DEFAULT NULL COMMENT '类别：学生、教职工、校外人员',
    `department`   VARCHAR(40)                          DEFAULT NULL COMMENT '院系或工作部门',
    `student_id`   VARCHAR(15)                          DEFAULT NULL COMMENT '学号',
    INDEX idx_user_name (user_name),
    INDEX idx_phone_number (phone_number),
    UNIQUE uk_userid (userid),
    PRIMARY KEY pk_id (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

DROP TABLE IF EXISTS `cache_pic`;
CREATE TABLE `cache_pic`
(
    `id`           INT UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '自增长的表单id',
    `create_time`  DATETIME                    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME                    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `user_id`      INT UNSIGNED                NOT NULL COMMENT '上传用户的id',
    `pic_location` VARCHAR(100)                NOT NULL COMMENT '图片路径，注意手动改一下索引长度',
    INDEX idx_user_id (user_id),
    UNIQUE uk_pic_location (pic_location),
    PRIMARY KEY pk_id (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

DROP TABLE IF EXISTS `service_event`;
CREATE TABLE `service_event`
(
    `id`               INT UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '自增长的表单id',
    `create_time`      DATETIME                    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time`     DATETIME                    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `user_id`          INT UNSIGNED                NOT NULL COMMENT '发起维修请求的用户id',
    `volunteer_id`     INT UNSIGNED                         DEFAULT NULL COMMENT '负责维修的志愿者',
    `repairing_result` VARCHAR(100)                         DEFAULT NULL COMMENT '志愿者填写的维修结果',
    `feedback`         VARCHAR(100)                         DEFAULT NULL COMMENT '用户反馈信息',
    `activity_id`      INT                                  DEFAULT NULL COMMENT '预约活动id，审核通过后才确定',
    `time_slot`        INT                                  DEFAULT NULL COMMENT '预约时间段，审核通过后才确定',
    `problem_summary`  VARCHAR(100)                         DEFAULT NULL COMMENT '问题概括',
    `valid_form_id`    INT UNSIGNED                         DEFAULT NULL COMMENT '一个可用的维修单id，用于提取摘要',
    `status`           TINYINT                              DEFAULT 0 COMMENT '状态',
    `is_draft`         BOOLEAN                              DEFAULT 0 COMMENT '是否为草稿状态',
    `is_closed`        BOOLEAN                              DEFAULT 0 COMMENT '是否为关闭状态',
    INDEX idx_user_id (user_id),
    INDEX idx_volunteer_id (volunteer_id),
    INDEX idx_activity_id (activity_id),
    INDEX idx_status (status),
    INDEX idx_is_draft (is_draft),
    INDEX idx_is_closed (is_closed),
    PRIMARY KEY pk_id (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

DROP TABLE IF EXISTS `service_form`;
CREATE TABLE `service_form`
(
    `id`                    INT UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '自增长的表单id',
    `create_time`           DATETIME                    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time`          DATETIME                    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `brand`                 VARCHAR(15)                          DEFAULT NULL COMMENT '电脑品牌',
    `computer_model`        VARCHAR(30)                          DEFAULT NULL COMMENT '电脑型号',
    `cpu_model`             VARCHAR(30)                          DEFAULT NULL COMMENT 'cpu型号',
    `has_discrete_graphics` BOOLEAN                              DEFAULT NULL COMMENT '是否有独显',
    `graphics_model`        VARCHAR(30)                          DEFAULT NULL COMMENT '显卡型号',
    `laptop_type`           VARCHAR(30)                          DEFAULT NULL COMMENT '笔记本类型',
    `bought_time`           VARCHAR(30)                          DEFAULT NULL COMMENT '购买时间',
    `is_under_warranty`     BOOLEAN                              DEFAULT NULL COMMENT '是否在保',
    `problem_description`   VARCHAR(100)                         DEFAULT NULL COMMENT '问题描述',
    `problem_type`          VARCHAR(10)                          DEFAULT NULL COMMENT '问题类型（硬件/软件）',
    `reply_admin_id`        INT UNSIGNED                         DEFAULT NULL COMMENT '提供建议的人的id',
    `description_advice`    VARCHAR(100)                         DEFAULT NULL COMMENT '描述修改建议',
    `activity_id`           INT                                  DEFAULT NULL COMMENT '"活动id，草稿时用"',
    `time_slot`             INT                                  DEFAULT NULL COMMENT '"活动时间段"',
    `service_event_id`      INT UNSIGNED                NOT NULL COMMENT '关联的维修事件的id',
    INDEX idx_service_event_id (service_event_id),
    PRIMARY KEY pk_id (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

DROP TABLE IF EXISTS `service_pic`;
CREATE TABLE `service_pic`
(
    `id`              INT UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '自增长的表单id',
    `create_time`     DATETIME                    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time`    DATETIME                    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `service_form_id` INT UNSIGNED                         DEFAULT NULL COMMENT '关联的维修单id',
    `pic_location`    VARCHAR(100)                         DEFAULT NULL COMMENT '图片路径',
    INDEX idx_service_form_id (service_form_id),
    PRIMARY KEY pk_id (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`           INT UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '自增长',
    `create_time`  DATETIME                    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME                    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `openid`       VARCHAR(40)                 NOT NULL COMMENT '微信提供的用户id',
    `user_name`    VARCHAR(30)                          DEFAULT NULL COMMENT '该如何称呼您？',
    `phone_number` VARCHAR(30)                          DEFAULT NULL COMMENT '用户手机号',
    `email`        VARCHAR(100)                         DEFAULT NULL COMMENT '用户邮箱',
    `identity`     VARCHAR(40)                          DEFAULT NULL COMMENT '类别：学生、教职工、校外人员',
    `department`   VARCHAR(40)                          DEFAULT NULL COMMENT '院系或工作部门',
    `grade`        VARCHAR(40)                          DEFAULT NULL COMMENT '年级',
    `student_id`   VARCHAR(15)                          DEFAULT NULL COMMENT '学号',
    `comment`      TEXT                                 DEFAULT NULL COMMENT '备注',
    `is_volunteer` BOOLEAN                     NOT NULL DEFAULT 0 COMMENT '是否为志愿者',
    `is_admin`     BOOLEAN                     NOT NULL DEFAULT 0 COMMENT '是否为管理员',
    `is_su`        BOOLEAN                     NOT NULL DEFAULT 0 COMMENT '是否为超管',
    INDEX idx_user_name (user_name),
    INDEX idx_phone_number (phone_number),
    INDEX idx_email (email),
    INDEX idx_is_volunteer (is_volunteer),
    INDEX idx_is_admin (is_admin),
    INDEX idx_is_su (is_su),
    UNIQUE uk_openid (openid),
    PRIMARY KEY pk_id (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

DROP TABLE IF EXISTS `volunteer`;
CREATE TABLE `volunteer`
(
    `id`           INT UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '自增长',
    `create_time`  DATETIME                    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME                    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `userid`       INT UNSIGNED                NOT NULL COMMENT 'user_id',
    `user_name`    VARCHAR(30)                          DEFAULT NULL COMMENT '真实姓名',
    `phone_number` VARCHAR(30)                          DEFAULT NULL COMMENT '用户手机号',
    `email`        VARCHAR(100)                         DEFAULT NULL COMMENT '用户邮箱',
    `identity`     VARCHAR(40)                          DEFAULT NULL COMMENT '类别：学生、教职工、校外人员',
    `department`   VARCHAR(40)                          DEFAULT NULL COMMENT '院系或工作部门',
    `student_id`   VARCHAR(15)                          DEFAULT NULL COMMENT '学号',
    `order_count`  INT UNSIGNED                         DEFAULT 0 COMMENT '完成的单数',
    INDEX idx_user_name (user_name),
    INDEX idx_phone_number (phone_number),
    UNIQUE uk_userid (userid),
    PRIMARY KEY pk_id (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

DROP TABLE IF EXISTS `volunteer_application`;
CREATE TABLE `volunteer_application`
(
    `id`                     INT UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '自增长id',
    `create_time`            DATETIME                    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time`           DATETIME                    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `user_id`                INT UNSIGNED                NOT NULL COMMENT '申请用户的id',
    `reason_for_application` VARCHAR(100)                NOT NULL COMMENT '申请理由',
    `reply_by_admin`         VARCHAR(100)                         DEFAULT NULL COMMENT '管理员回复',
    `admin_id`               INT UNSIGNED                         DEFAULT NULL COMMENT '回复的管理员的id',
    `status`                 TINYINT                              DEFAULT 0 COMMENT '申请状态',
    `card_pic_location`      VARCHAR(30)                 NOT NULL COMMENT '申请者的身份证明的图片location',
    INDEX idx_user_id (user_id),
    INDEX idx_admin_id (admin_id),
    INDEX idx_status (status),
    PRIMARY KEY pk_id (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;


INSERT INTO user(openid, user_name, identity, is_su)
VALUES ('1da5505af2a5ba46a749eaa6b1a92003', 'shuwashuwa', '超级管理员', 1);