package dev.dong4j.zeka.kernel.auth;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.dong4j.zeka.kernel.auth.entity.AuthorizationRole;
import dev.dong4j.zeka.kernel.auth.entity.AuthorizationUser;
import dev.dong4j.zeka.kernel.auth.enums.UserType;

import java.util.Set;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.09.11 11:58
 * @since 1.6.0
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = AuthorizationUser.class)
public interface CurrentUser {

    /**
     * userId
     *
     * @return the id
     * @since 1.6.0
     */
    Long getId();

    /**
     * 用户名
     *
     * @return the username
     * @since 1.6.0
     */
    String getUsername();

    /**
     * 手机号
     *
     * @return the mobile
     * @since 1.6.0
     */
    String getMobile();

    /**
     * 邮箱
     *
     * @return the email
     * @since 1.6.0
     */
    String getEmail();

    /**
     * 用户类型
     *
     * @return the user type
     * @since 1.6.0
     */
    UserType getUserType();

    /**
     * 企业类型
     *
     * @return the integer
     * @since 1.6.0
     */
    Integer getEnterpriseType();

    /**
     * 租户 id
     *
     * @return the tenant id
     * @since 1.6.0
     */
    Long getTenantId();

    /**
     * Gets tenant app id *
     *
     * @return the tenant app id
     * @since 1.6.0
     */
    Long getTenantAppId();

    /**
     * Gets system app id *
     *
     * @return the system app id
     * @since 1.6.0
     */
    Long getSystemAppId();

    /**
     * 用户标记, 登录邮箱后缀
     *
     * @return the marked
     * @since 1.6.0
     */
    String getMarked();

    /**
     * Gets roles *
     *
     * @return the roles
     * @since 1.6.0
     */
    Set<AuthorizationRole> getRoles();

    /**
     * 获取公司 id
     *
     * @return the company id
     * @since 1.7.3
     */
    Long getCompanyId();

    /**
     * 扩展字段
     *
     * @return the extend
     * @since 2.0.0
     */
    Object getExtend();
}
