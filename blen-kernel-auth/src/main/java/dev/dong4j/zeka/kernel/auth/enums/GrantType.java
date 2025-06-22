package dev.dong4j.zeka.kernel.auth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>Description: 认证方式 </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.26 20:41
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum GrantType {
    /**
     * 密码模式 这种模式是最不推荐的,因为 client 可能存了用户密码
     * 这种模式主要用来做遗留项目升级为 oauth2 的适配方案
     * 当然如果 client 是自家的应用,也是可以
     * 支持 refresh token
     */
    PASSWORD("password"),
    /**
     * 授权码模式 这种模式算是正宗的 oauth2 的授权模式
     * 设计了 auth code,通过这个 code 再获取 token
     * 支持 refresh token
     */
    CODE("authorization_code"),
    /**
     * 简化模式 这种模式比授权码模式少了 code 环节,回调 url 直接携带 token
     * 这种模式的使用场景是基于浏览器的应用
     * 这种模式基于安全性考虑,建议把 token 时效设置短一些
     * 不支持 refresh token
     */
    IMPLICIT("implicit"),
    /**
     * 客户端模式 这种模式直接根据 client 的 id 和密钥即可获取 token,无需用户参与
     * 这种模式比较合适消费 api 的后端服务,比如拉取一组用户信息等
     * 不支持 refresh token,主要是没有必要
     */
    CLIENT("client_credentials"),
    /** 刷新 token */
    REFRESH_TOKEN("refresh_token");

    /** Value */
    private final String value;
}
