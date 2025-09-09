package dev.dong4j.zeka.kernel.auth.context;

import dev.dong4j.zeka.kernel.auth.CurrentUser;
import java.io.Serializable;

/**
 * 安全上下文接口，定义了管理当前用户认证信息的基本操作
 * <p>
 * 该接口作为安全框架的核心组件，负责在请求处理过程中存储和传递用户认证状态
 * 支持在不同线程间安全地传递用户身份信息，确保系统的安全性和用户会话的一致性
 * <p>
 * 主要功能：
 * - 用户认证信息的存储和获取
 * - 线程安全的身份信息传递
 * - 支持序列化以便在分布式环境中传输
 * <p>
 * 具体实现由 {@link SecurityContextImpl} 提供，通过 {@link SecurityContextHolder} 进行管理
 * 支持多种存储策略：ThreadLocal、InheritableThreadLocal 和 Global
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.09.15 01:41
 * @since 1.0.0
 */
public interface SecurityContext extends Serializable {
    /**
     * 获取当前用户认证信息
     *
     * @return 当前用户认证对象，包含用户身份和权限信息
     * @since 1.0.0
     */
    CurrentUser getAuthentication();

    /**
     * 设置当前用户认证信息
     *
     * @param authentication 用户认证对象，包含用户身份和权限信息
     * @since 1.0.0
     */
    void setAuthentication(CurrentUser authentication);
}
