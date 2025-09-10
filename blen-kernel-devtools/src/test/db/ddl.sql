# 自定义枚举: 会自动生成枚举类
# 公共枚举: 直接使用已有的枚举类
create table rule
(
    `id`            bigint auto_increment primary key,
    `ip_from`       bigint       null comment 'IP范围开始地址',
    `ip_to`         bigint       null comment 'IP范围结束地址',
    `match_mode`    varchar(20)  null comment '域名匹配模式',
    `name`          varchar(100) null comment '匹配的域名',
    `priority`      int          null comment '匹配优先级',
    `enabled` bit default b'1' null comment '公共枚举:EnabledEnum:可用状态',
    `dispatch_mode` varchar(20)  null comment '分发模式, 如iphash、round-robin、random',
    `create_time`   timestamp    not null default current_timestamp comment '创建时间 (公共字段)',
    `update_time`   timestamp    not null default current_timestamp on update current_timestamp comment '最后更新时间 (公共字段)',
    `deleted`       bigint       not null default 0 comment '通用枚举:删除状态:0: 未删除，删除就是当前数据的主键id用于代表唯一性'
) engine = INNODB
  auto_increment = 0
  default charset = utf8mb4
  collate = utf8mb4_general_ci comment = '解析规则';

create table user
(
    `id`           bigint unsigned auto_increment comment '自增主键' primary key,
    `phone`        varchar(11)  not null comment '用户电话号码',
    `user_name`    varchar(64)  not null comment '用户名',
    `gender`       tinyint(4)   not null comment '性别',
    `nick_name`    varchar(64)  not null comment '姓名',
    `company_name` varchar(64)  not null comment '公司名称',
    `password`     varchar(128) not null comment '密码',
    `salt`         varchar(128) not null comment '密码加密盐值',
    `api_key`      varchar(50)  null comment 'open api key',
    `secret_key`   varchar(50)  null comment 'open api secret key',
    `email`        varchar(128) null comment '用户邮箱',
    `state`        tinyint(2) unsigned   default 0 not null comment '自定义枚举:Integer:用户状态:AAA(0, "未审核"),:BBB(1, "审核中"),:CCC(2, "审核未通过"),:DDD(3,"已锁定"),:EEE(4,"正常");',
    `create_time`  timestamp    not null default current_timestamp comment '创建时间 (公共字段)',
    `update_time`  timestamp    not null default current_timestamp on update current_timestamp comment '最后更新时间 (公共字段)',
    `deleted`      bigint       not null default 0 comment '通用枚举:删除状态:0: 未删除，删除就是当前数据的主键id用于代表唯一性'
) engine = INNODB
  auto_increment = 0
  default charset = utf8mb4
  collate = utf8mb4_general_ci comment = '用户信息表';

create table user_role
(
    `id` bigint unsigned auto_increment comment '自增主键' primary key
) engine = INNODB
  auto_increment = 0
  default charset = utf8mb4
  collate = utf8mb4_general_ci comment = '用户_角色关联';


-- (Q@ 查询参数前缀, 会自动生成 xxxQuery 类的字段)
create table example
(
    `id`          bigint auto_increment primary key,
    `name`        varchar(100) null comment 'Q@姓名',
    `gender`      tinyint(2) unsigned   default 2 not null comment 'Q@自定义枚举:Integer:性别:WOMAN(0, "女性"),:MAN(1, "男性"),:UNKNOWN(2, "未知");',
    `hobby`       tinyint(2) unsigned   default 2 not null comment '自定义枚举:Integer:爱好:WOMAN(0, "女性"),:MAN(1, "男性"),:UNKNOWN(2, "未知");',
    `birthday`    timestamp    not null comment '生日',
    `enabled` bit default b'1' null comment '公共枚举:EnabledEnum:可用状态',
    `create_time` timestamp    not null default current_timestamp comment '创建时间 (公共字段)',
    `update_time` timestamp    not null default current_timestamp on update current_timestamp comment '最后更新时间 (公共字段)',
    `deleted`     bigint       not null default 0 comment '通用枚举:删除状态:0: 未删除，删除就是当前数据的主键id用于代表唯一性'
) engine = INNODB
  auto_increment = 0
  default charset = utf8mb4
  collate = utf8mb4_general_ci comment = '示例';
