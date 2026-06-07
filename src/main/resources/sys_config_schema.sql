CREATE TABLE sys_config (
    id BIGINT PRIMARY KEY,
    config_name VARCHAR(100) NOT NULL,
    config_key VARCHAR(100) NOT NULL UNIQUE,
    config_value VARCHAR(500) NOT NULL,
    config_type VARCHAR(30) NOT NULL,
    remark VARCHAR(500),
    created_by VARCHAR(100),
    created_time TIMESTAMP WITH TIME ZONE,
    updated_by VARCHAR(100),
    updated_time TIMESTAMP WITH TIME ZONE
);

COMMENT ON TABLE sys_config IS '系统参数配置表';
COMMENT ON COLUMN sys_config.id IS '分布式雪花 ID';
COMMENT ON COLUMN sys_config.config_name IS '参数名称';
COMMENT ON COLUMN sys_config.config_key IS '参数键名';
COMMENT ON COLUMN sys_config.config_value IS '参数键值';
COMMENT ON COLUMN sys_config.config_type IS '参数类型（BUILT_IN-内置, CUSTOM-自定义）';
COMMENT ON COLUMN sys_config.remark IS '备注';
COMMENT ON COLUMN sys_config.created_by IS '创建人';
COMMENT ON COLUMN sys_config.created_time IS '创建时间';
COMMENT ON COLUMN sys_config.updated_by IS '更新人';
COMMENT ON COLUMN sys_config.updated_time IS '更新时间';
