package dev.dong4j.zeka.kernel.auth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * OAuth2 授权模式枚举，定义了系统支持的所有 OAuth2 认证模式
 * <p>
 * 该枚举类定义了符合 OAuth2 标准的五种授权模式，每种模式适用于不同的应用场景
 * 支持从传统的密码模式到现代的授权码模式，满足不同安全级别的需求
 * <p>
 * 支持的授权模式：
 * - PASSWORD：密码模式，适用于遗留系统升级和内部应用
 * - CODE：授权码模式，标准的 OAuth2 授权流程
 * - IMPLICIT：简化模式，适用于浏览器应用
 * - CLIENT：客户端模式，适用于后端服务调用
 * - REFRESH_TOKEN：令牌刷新模式
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.26 20:41
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum GrantType {
    /**
     * 密码模式
     * <p>
     * 这种模式是最不推荐的，因为客户端可能存储了用户密码
     * 主要用于遗留项目升级为 OAuth2 的适配方案
     * 当然如果客户端是自家的应用，也是可以的
     * 支持 refresh token
     */
    PASSWORD("password"),
    /**
     * 授权码模式
     * <p>
     * 这种模式算是正宗的 OAuth2 的授权模式
     * 设计了授权码（auth code），通过这个码再获取 token
     * 支持 refresh token
     */
    CODE("authorization_code"),
    /**
     * 简化模式
     * <p>
     * 这种模式比授权码模式少了授权码环节，回调 URL 直接携带 token
     * 这种模式的使用场景是基于浏览器的应用
     * 这种模式基于安全性考虑，建议把 token 时效设置短一些
     * 不支持 refresh token
     */
    IMPLICIT("implicit"),
    /**
     * 客户端模式
     * <p>
     * 这种模式直接根据客户端的 ID 和密钥即可获取 token，无需用户参与
     * 这种模式比较合适消费 API 的后端服务，比如拉取一组用户信息等
     * 不支持 refresh token，主要是没有必要
     */
    CLIENT("client_credentials"),
    /** 刷新 token 模式 */
    REFRESH_TOKEN("refresh_token");

    /** OAuth2 授权模式值 */
    private final String value;
}
