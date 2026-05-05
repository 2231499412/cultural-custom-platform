CREATE DATABASE IF NOT EXISTS custom_platform DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE custom_platform;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码',
    `nickname` VARCHAR(50) DEFAULT '' COMMENT '昵称',
    `avatar` VARCHAR(255) DEFAULT '' COMMENT '头像URL',
    `phone` VARCHAR(20) DEFAULT '' COMMENT '手机号',
    `email` VARCHAR(100) DEFAULT '' COMMENT '邮箱',
    `role` VARCHAR(20) NOT NULL DEFAULT 'user' COMMENT '角色: user/admin',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 模板表
CREATE TABLE IF NOT EXISTS `template` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '模板ID',
    `name` VARCHAR(100) NOT NULL COMMENT '模板名称',
    `category` VARCHAR(30) NOT NULL COMMENT '分类: ticket-票根 acrylic-亚克力 keychain-钥匙扣 badge-徽章',
    `description` VARCHAR(500) DEFAULT '' COMMENT '描述',
    `preview_url` VARCHAR(255) NOT NULL COMMENT '预览图URL',
    `structure_json` TEXT NOT NULL COMMENT '模板结构JSON(图层、可编辑区域等)',
    `price` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '基础价格',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-下架 1-上架',
    `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    KEY `idx_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='模板表';

-- 素材表
CREATE TABLE IF NOT EXISTS `material` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '素材ID',
    `name` VARCHAR(100) NOT NULL COMMENT '素材名称',
    `type` VARCHAR(30) NOT NULL COMMENT '类型: background-背景 border-边框 decoration-装饰 text-文字样式',
    `category` VARCHAR(30) DEFAULT '' COMMENT '所属分类',
    `url` VARCHAR(255) NOT NULL COMMENT '素材URL',
    `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    KEY `idx_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='素材表';

-- 定制记录表
CREATE TABLE IF NOT EXISTS `customization` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '定制ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `template_id` BIGINT NOT NULL COMMENT '模板ID',
    `custom_data_json` TEXT NOT NULL COMMENT '定制数据JSON(用户替换的图片、文字等)',
    `preview_url` VARCHAR(255) DEFAULT '' COMMENT '生成的预览图URL',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='定制记录表';

-- 订单表
CREATE TABLE IF NOT EXISTS `order_info` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `order_no` VARCHAR(50) NOT NULL COMMENT '订单号',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `customization_id` BIGINT NOT NULL COMMENT '定制记录ID',
    `total_price` DECIMAL(10,2) NOT NULL COMMENT '总价',
    `quantity` INT NOT NULL DEFAULT 1 COMMENT '数量',
    `status` VARCHAR(20) NOT NULL DEFAULT 'pending' COMMENT '状态: pending-待确认 confirmed-已确认 producing-生产中 shipped-已发货 completed-已完成 cancelled-已取消',
    `receiver_name` VARCHAR(50) NOT NULL COMMENT '收货人',
    `receiver_phone` VARCHAR(20) NOT NULL COMMENT '收货电话',
    `receiver_address` VARCHAR(255) NOT NULL COMMENT '收货地址',
    `remark` VARCHAR(500) DEFAULT '' COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 生产状态记录表
CREATE TABLE IF NOT EXISTS `production_status` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `order_id` BIGINT NOT NULL COMMENT '订单ID',
    `status_node` VARCHAR(30) NOT NULL COMMENT '状态节点',
    `remark` VARCHAR(255) DEFAULT '' COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='生产状态记录表';

-- ========== 种子数据 ==========

-- 模板数据
INSERT INTO `template` (`name`, `category`, `description`, `preview_url`, `structure_json`, `price`, `status`, `sort_order`) VALUES
('演唱会票根', 'ticket', '深蓝星空风格，适合演唱会、音乐现场等演出类纪念票根', '/uploads/templates/ticket-concert.png', '{"width":280,"height":560}', 29.90, 1, 1),
('音乐节票根', 'ticket', '活力橙红风格，适合音乐节、户外演出等大型活动纪念', '/uploads/templates/ticket-festival.png', '{"width":280,"height":560}', 29.90, 1, 2),
('体育赛事票根', 'ticket', '深邃绿金风格，适合足球、篮球等体育赛事纪念', '/uploads/templates/ticket-sports.png', '{"width":280,"height":560}', 29.90, 1, 3),
('电竞赛事票根', 'ticket', '赛博霓虹风格，适合电竞比赛、游戏赛事等纪念票根', '/uploads/templates/ticket-esports.png', '{"width":280,"height":560}', 29.90, 1, 4),
('应援亚克力立牌', 'acrylic', '粉紫渐变风格，适合追星应援、偶像周边定制', '/uploads/templates/acrylic-cheer.png', '{"width":300,"height":400}', 49.90, 1, 5),
('毕业纪念亚克力立牌', 'acrylic', '温暖校园风格，毕业季专属纪念摆件', '/uploads/templates/acrylic-graduation.png', '{"width":300,"height":400}', 49.90, 1, 6),
('宠物纪念亚克力立牌', 'acrylic', '温馨可爱风格，为你的毛孩子定制专属摆件', '/uploads/templates/acrylic-pet.png', '{"width":300,"height":400}', 49.90, 1, 7),
('头像钥匙扣', 'keychain', '圆形简约风格，上传头像即可定制随身小物', '/uploads/templates/keychain-avatar.png', '{"width":200,"height":200}', 19.90, 1, 8),
('情侣钥匙扣套装', 'keychain', '甜蜜粉色风格，一对两个，情侣闺蜜必备', '/uploads/templates/keychain-couple.png', '{"width":200,"height":200}', 35.90, 1, 9),
('赛事纪念徽章', 'badge', '金属质感风格，体育赛事、活动收藏级纪念品', '/uploads/templates/badge-event.png', '{"width":180,"height":180}', 15.90, 1, 10),
('个性签名徽章', 'badge', '文艺简约风格，写上你的专属个性宣言', '/uploads/templates/badge-motto.png', '{"width":180,"height":180}', 15.90, 1, 11),
('毕业纪念徽章', 'badge', '青春校园风格，毕业季限定收藏纪念品', '/uploads/templates/badge-school.png', '{"width":180,"height":180}', 15.90, 1, 12);

-- 素材数据
INSERT INTO `material` (`name`, `type`, `category`, `url`, `sort_order`) VALUES
('深蓝星空', 'background', 'ticket', '/uploads/materials/bg-dark-blue.png', 1),
('活力橙红', 'background', 'ticket', '/uploads/materials/bg-orange-red.png', 2),
('深邃墨绿', 'background', 'ticket', '/uploads/materials/bg-dark-green.png', 3),
('赛博霓虹', 'background', 'ticket', '/uploads/materials/bg-cyber-dark.png', 4),
('粉紫渐变', 'background', 'acrylic', '/uploads/materials/bg-pastel.png', 5),
('温暖米黄', 'background', 'acrylic', '/uploads/materials/bg-cream.png', 6),
('浅黄温馨', 'background', 'acrylic', '/uploads/materials/bg-light-yellow.png', 7),
('粉色渐变', 'background', 'keychain', '/uploads/materials/bg-pink-gradient.png', 8),
('白色圆形', 'background', 'keychain', '/uploads/materials/bg-white-circle.png', 9),
('金属质感', 'background', 'badge', '/uploads/materials/bg-metal.png', 10),
('米白文艺', 'background', 'badge', '/uploads/materials/bg-cream-badge.png', 11),
('深蓝学院', 'background', 'badge', '/uploads/materials/bg-navy-badge.png', 12),
('简约白边框', 'border', '', '/uploads/materials/border-white.png', 1),
('金色华丽边框', 'border', '', '/uploads/materials/border-gold.png', 2),
('虚线边框', 'border', '', '/uploads/materials/border-dashed.png', 3),
('手绘线条边框', 'border', '', '/uploads/materials/border-handdrawn.png', 4),
('五彩纸屑', 'decoration', '', '/uploads/materials/deco-confetti.png', 1),
('星星点缀', 'decoration', '', '/uploads/materials/deco-stars.png', 2),
('音符装饰', 'decoration', '', '/uploads/materials/deco-music.png', 3),
('花朵装饰', 'decoration', '', '/uploads/materials/deco-flowers.png', 4),
('胶片元素', 'decoration', '', '/uploads/materials/deco-film.png', 5),
('手绘爱心', 'decoration', '', '/uploads/materials/deco-heart.png', 6);

-- 插入默认管理员账号 (密码: admin123, BCrypt加密)
INSERT INTO `user` (`username`, `password`, `nickname`, `role`)
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '管理员', 'admin');
