-- auto-generated definition
create table rule
(
    id            bigint auto_increment primary key,
    ip_from       bigint                              null comment 'IP范围开始地址',
    ip_to         bigint                              null comment 'IP范围结束地址',
    match_mode    varchar(20)                         null comment '域名匹配模式',
    name          varchar(100)                        null comment '匹配的域名',
    priority      int                                 null comment '匹配优先级',
    enabled       bit       default b'1'              null comment '是否启用',
    dispatch_mode varchar(20)                         null comment '分发模式, 如iphash、round-robin、random',
    deleted       bit       default b'0'              not null comment '状态:0: 未删除 1: 已删除 (公共字段)',
    create_time   timestamp default current_timestamp not null comment '创建时间 (公共字段)',
    update_time   timestamp default current_timestamp not null comment '最后更新时间 (公共字段)'
) comment '解析规则' charset = utf8mb4;


-- auto-generated definition
create table user
(
    id           int unsigned auto_increment comment '自增主键' primary key,
    phone        varchar(11)                                   not null comment '用户电话号码',
    user_name    varchar(64)                                   not null comment '用户名',
    gender       tinyint(4)                                    not null comment '性别',
    nick_name    varchar(64)                                   not null comment '姓名',
    company_name varchar(64)                                   not null comment '公司名称',
    password     varchar(128)                                  not null comment '密码',
    salt         varchar(128)                                  not null comment '密码加密盐值',
    api_key      varchar(50)                                   null comment 'open api key',
    secret_key   varchar(50)                                   null comment 'open api secret key',
    email        varchar(128)                                  null comment '用户邮箱',
    status       tinyint(4) unsigned default 0                 not null comment '用户状态:0: 未审核, 1: 审核中, 2: 审核未通过, 3: 已锁定, 4: 正常',
    deleted      bit                 default b'0'              not null comment '状态:0: 未删除 1: 已删除 (公共字段)',
    create_time  timestamp           default current_timestamp not null comment '创建时间 (公共字段)',
    update_time  timestamp           default current_timestamp not null comment '最后更新时间 (公共字段)'
) comment '用户信息表' charset = utf8mb4;

create index user_info_phone_index on user (phone);

create index user_info_username_index on user (user_name);

