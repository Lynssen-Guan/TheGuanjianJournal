# 用户表 sys_user

CREATE TABLE sys_user (
      id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
      username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
      password VARCHAR(255) NOT NULL COMMENT '密码哈希',
      nickname VARCHAR(50) DEFAULT NULL COMMENT '昵称',
      avatar VARCHAR(500) DEFAULT NULL COMMENT '头像',
      status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0禁用 1启用',
      last_login_time DATETIME DEFAULT NULL COMMENT '最后登录时间',
      created_at DATETIME NOT NULL COMMENT '创建时间',
      updated_at DATETIME NOT NULL COMMENT '更新时间',
      deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除 1已删除'
);

# 令牌刷新表
CREATE TABLE sys_refresh_token (
       id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
       user_id BIGINT NOT NULL COMMENT '用户ID',
       token VARCHAR(500) NOT NULL COMMENT '刷新令牌',
       expires_at DATETIME NOT NULL COMMENT '过期时间',
       revoked TINYINT NOT NULL DEFAULT 0 COMMENT '是否撤销：0否 1是',
       created_at DATETIME NOT NULL COMMENT '创建时间',
       updated_at DATETIME NOT NULL COMMENT '更新时间'
);

CREATE INDEX idx_refresh_token_user_id ON sys_refresh_token(user_id);
CREATE INDEX idx_refresh_token_token ON sys_refresh_token(token);