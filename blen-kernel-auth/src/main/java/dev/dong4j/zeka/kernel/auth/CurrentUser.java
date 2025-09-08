package dev.dong4j.zeka.kernel.auth;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.dong4j.zeka.kernel.auth.entity.AuthorizationRole;
import dev.dong4j.zeka.kernel.auth.entity.AuthorizationUser;
import dev.dong4j.zeka.kernel.auth.enums.UserType;
import java.util.Set;

/**
 * <p>Description: 当前用户信息接口，定义了获取用户基本信息的方法，是系统中用户信息的统一抽象</p>
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
     * @return 用户ID
     * @since 1.0.0
     */
    Long getId();

    /**
     * 获取用户登录名
     *
     * @return 用户名
     * @since 1.0.0
     */
    String getUsername();

    /**
     * 获取用户手机号码
     *
     * @return 手机号
     * @since 1.0.0
     */
    String getMobile();

    /**
     * 获取用户电子邮箱
     *
     * @return 邮箱
     * @since 1.0.0
     */
    String getEmail();

    /**
     * 获取用户类型枚举值
     *
     * @return 用户类型
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
     * @return 租户ID
     * @since 1.0.0
     */
    Long getTenantId();

    /**
     * 获取租户应用ID
     *
     * @return 租户应用ID
     * @since 1.0.0
     */
    Long getTenantAppId();

    /**
     * 获取系统应用ID
     *
     * @return 系统应用ID
     * @since 1.0.0
     */
    Long getSystemAppId();

    /**
     * 获取用户标记，通常为登录邮箱后缀
     *
     * @return 用户标记
     * @since 1.0.0
     */
    String getMarked();

    /**
     * 获取用户所属角色集合
     *
     * @return 角色集合
     * @since 1.0.0
     */
    Set<AuthorizationRole> getRoles();

    /**
     * 获取公司ID
     *
     * @return 公司ID
     * @since 1.0.0
     */
    Long getCompanyId();

    /**
     * 获取用户扩展信息
     *
     * @return 扩展信息对象
     * @since 1.0.0
     */
    Object getExtend();
}
