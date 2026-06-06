CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY,
    username VARCHAR(50),
    avatar VARCHAR(255),
    email VARCHAR(100),
    phone VARCHAR(20),
    status SMALLINT,
    tenant_id VARCHAR(64) NOT NULL,
    created_by VARCHAR(100),
    created_time TIMESTAMP WITH TIME ZONE,
    updated_by VARCHAR(100),
    updated_time TIMESTAMP WITH TIME ZONE
);

COMMENT ON TABLE sys_user IS '用户基础表';
COMMENT ON COLUMN sys_user.id IS '分布式雪花 ID';
COMMENT ON COLUMN sys_user.username IS '用户昵称/显示名称';
COMMENT ON COLUMN sys_user.avatar IS '头像 URL';
COMMENT ON COLUMN sys_user.email IS '基础电子邮箱';
COMMENT ON COLUMN sys_user.phone IS '基础手机号';
COMMENT ON COLUMN sys_user.status IS '用户状态（0-正常，1-禁用）';
COMMENT ON COLUMN sys_user.tenant_id IS '多租户隔离 ID';
COMMENT ON COLUMN sys_user.created_by IS '创建人';
COMMENT ON COLUMN sys_user.created_time IS '创建时间';
COMMENT ON COLUMN sys_user.updated_by IS '更新人';
COMMENT ON COLUMN sys_user.updated_time IS '更新时间';

CREATE TABLE sys_user_credential (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    identity_type VARCHAR(30) NOT NULL,
    identifier VARCHAR(100) NOT NULL,
    credential VARCHAR(500),
    tenant_id VARCHAR(64) NOT NULL,
    created_by VARCHAR(100),
    created_time TIMESTAMP WITH TIME ZONE,
    updated_by VARCHAR(100),
    updated_time TIMESTAMP WITH TIME ZONE
);

COMMENT ON TABLE sys_user_credential IS '用户凭证/认证表';
COMMENT ON COLUMN sys_user_credential.id IS '主键';
COMMENT ON COLUMN sys_user_credential.user_id IS '关联 sys_user.id';
COMMENT ON COLUMN sys_user_credential.identity_type IS '凭证类型（如PASSWORD, PHONE, WECHAT等）';
COMMENT ON COLUMN sys_user_credential.identifier IS '唯一标识（如账号名、手机号、三方 OpenID）';
COMMENT ON COLUMN sys_user_credential.credential IS '加密凭证';
COMMENT ON COLUMN sys_user_credential.tenant_id IS '多租户隔离 ID';
COMMENT ON COLUMN sys_user_credential.created_by IS '创建人';
COMMENT ON COLUMN sys_user_credential.created_time IS '创建时间';
COMMENT ON COLUMN sys_user_credential.updated_by IS '更新人';
COMMENT ON COLUMN sys_user_credential.updated_time IS '更新时间';

CREATE TABLE sys_user_login_log (
    id BIGINT PRIMARY KEY,
    user_id BIGINT,
    login_account VARCHAR(100),
    identity_type VARCHAR(30),
    login_time TIMESTAMP WITH TIME ZONE NOT NULL,
    ip_address VARCHAR(50),
    location VARCHAR(100),
    browser VARCHAR(50),
    os VARCHAR(50),
    status SMALLINT,
    message VARCHAR(255),
    tenant_id VARCHAR(64) NOT NULL
);

COMMENT ON TABLE sys_user_login_log IS '用户登录日志表';
COMMENT ON COLUMN sys_user_login_log.id IS '主键';
COMMENT ON COLUMN sys_user_login_log.user_id IS '关联的用户 ID';
COMMENT ON COLUMN sys_user_login_log.login_account IS '用户输入的登录账号';
COMMENT ON COLUMN sys_user_login_log.identity_type IS '使用的登录凭证类型';
COMMENT ON COLUMN sys_user_login_log.login_time IS '登录发生的时间';
COMMENT ON COLUMN sys_user_login_log.ip_address IS '客户端 IP';
COMMENT ON COLUMN sys_user_login_log.location IS '地理位置';
COMMENT ON COLUMN sys_user_login_log.browser IS '浏览器类型';
COMMENT ON COLUMN sys_user_login_log.os IS '操作系统';
COMMENT ON COLUMN sys_user_login_log.status IS '登录状态（0-成功，1-失败）';
COMMENT ON COLUMN sys_user_login_log.message IS '失败原因描述';
COMMENT ON COLUMN sys_user_login_log.tenant_id IS '多租户隔离 ID';
