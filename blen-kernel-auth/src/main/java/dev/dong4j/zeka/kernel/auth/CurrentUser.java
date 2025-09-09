package dev.dong4j.zeka.kernel.auth;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.dong4j.zeka.kernel.auth.entity.AuthorizationRole;
import dev.dong4j.zeka.kernel.auth.entity.AuthorizationUser;
import dev.dong4j.zeka.kernel.auth.enums.UserType;
import java.util.Set;

/**
 * 当前用户信息抽象接口，定义了系统中用户身份认证和授权的核心数据结构
 * <p>
 * 该接口作为用户信息的统一抽象，为不同类型的用户（租户用户、平台用户、系统管理员等）
 * 提供一致的数据访问方式，支持多租户架构下的用户身份管理
 * <p>
 * 主要功能：
 * - 用户基本信息获取（ID、用户名、手机号、邮箱等）
 * - 用户类型和权限信息管理
 * - 多租户环境下的租户隔离
 * - 角色权限体系支持
 * - 业务扩展字段支持
 * <p>
 * 使用 Jackson 多态序列化支持，默认实现为 {@link AuthorizationUser}
 * 可通过 type 字段进行类型识别和反序列化
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.09.11 11:58
 * @since 1.0.0
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = AuthorizationUser.class)
public interface CurrentUser {

    /**
     * 获取用户唯一标识
     *
     * @return 用户ID，系统内唯一的用户标识符
     * @since 1.0.0
     */
    Long getId();

    /**
     * 获取用户登录名
     *
     * @return 用户名，用于系统登录的唯一标识
     * @since 1.0.0
     */
    String getUsername();

    /**
     * 获取用户手机号码
     *
     * @return 手机号，用于身份验证和消息通知
     * @since 1.0.0
     */
    String getMobile();

    /**
     * 获取用户电子邮箱
     *
     * @return 邮箱地址，用于身份验证和消息通知
     * @since 1.0.0
     */
    String getEmail();

    /**
     * 获取用户类型枚举值
     *
     * @return 用户类型，标识用户在系统中的角色分类
     * @since 1.0.0
     */
    UserType getUserType();

    /**
     * 获取企业类型标识
     *
     * @return 企业类型
     * @since 1.0.0
     */
    Integer getEnterpriseType();

    /**
     * 获取租户ID
     *
     * @return 租户ID，多租户架构下的租户唯一标识，默认为 0 表示无租户用户
     * @since 1.0.0
     */
    Long getTenantId();

    /**
     * 获取租户应用ID
     *
     * @return 租户应用ID，标识用户从哪个租户的哪个应用登录
     * @since 1.0.0
     */
    Long getTenantAppId();

    /**
     * 获取系统应用ID
     *
     * @return 系统应用ID，标识当前应用所属的系统应用
     * @since 1.0.0
     */
    Long getSystemAppId();

    /**
     * 获取用户标记
     *
     * @return 用户标记，通常为登录邮箱后缀，用于用户身份的额外标识
     * @since 1.0.0
     */
    String getMarked();

    /**
     * 获取用户所属角色集合
     *
     * @return 角色集合，包含用户拥有的所有角色信息，用于权限控制
     * @since 1.0.0
     */
    Set<AuthorizationRole> getRoles();

    /**
     * 获取公司ID
     *
     * @return 公司ID，用户所属公司的唯一标识
     * @since 1.0.0
     */
    Long getCompanyId();

    /**
     * 获取用户扩展信息
     *
     * @return 扩展信息对象，用于存储业务特定的用户附加数据
     * @since 1.0.0
     */
    Object getExtend();
}
