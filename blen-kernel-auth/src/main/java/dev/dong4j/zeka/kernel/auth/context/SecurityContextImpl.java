package dev.dong4j.zeka.kernel.auth.context;

import dev.dong4j.zeka.kernel.auth.CurrentUser;
import java.io.Serial;

/**
 * 安全上下文的默认实现类，负责存储和管理当前用户的认证信息
 * <p>
 * 该类作为 {@link SecurityContext} 接口的标准实现，提供了安全上下文的基本操作功能
 * 包含用户认证数据的存储、获取、比较和哈希操作
 * <p>
 * 主要功能：
 * - 用户认证信息的安全存储
 * - 对象等价性和哈希值计算
 * - 支持序列化，可在分布式系统中传输和存储
 * - 线程安全的数据访问和修改
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.09.15 01:41
 * @since 1.0.0
 */
public class SecurityContextImpl implements SecurityContext {
    /** 序列化版本号 */
    @Serial
    private static final long serialVersionUID = -2828643112455883285L;

    /** 用户认证信息 */
    private CurrentUser authentication;

    /**
     * 默认构造函数，创建空的安全上下文
     *
     * @since 1.0.0
     */
    public SecurityContextImpl() {

    }

    /**
     * 带参数的构造函数，创建包含特定认证信息的安全上下文
     *
     * @param authentication 用户认证信息
     * @since 1.0.0
     */
    public SecurityContextImpl(CurrentUser authentication) {
        this.authentication = authentication;
    }

    /**
     * 对象等价性比较
     *
     * @param obj 待比较的对象
     * @return 如果两个对象的认证信息相同则返回 true
     * @since 1.0.0
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SecurityContextImpl test) {

            if ((this.getAuthentication() == null) && (test.getAuthentication() == null)) {
                return true;
            }

            return (this.getAuthentication() != null) && (test.getAuthentication() != null)
                && this.getAuthentication().equals(test.getAuthentication());
        }

        return false;
    }

    /**
     * 获取用户认证信息
     *
     * @return 当前的用户认证对象
     * @since 1.0.0
     */
    @Override
    public CurrentUser getAuthentication() {
        return this.authentication;
    }

    /**
     * 计算对象的哈希值
     *
     * @return 哈希值，基于认证信息计算
     * @since 1.0.0
     */
    @Override
    public int hashCode() {
        if (this.authentication == null) {
            return -1;
        } else {
            return this.authentication.hashCode();
        }
    }

    /**
     * 设置用户认证信息
     *
     * @param authentication 用户认证信息
     * @since 1.0.0
     */
    @Override
    public void setAuthentication(CurrentUser authentication) {
        this.authentication = authentication;
    }

    /**
     * 返回对象的字符串表示
     *
     * @return 包含认证信息的字符串描述
     * @since 1.0.0
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());

        if (this.authentication == null) {
            sb.append(": Null authentication");
        } else {
            sb.append(": Authentication: ").append(this.authentication);
        }

        return sb.toString();
    }
}
