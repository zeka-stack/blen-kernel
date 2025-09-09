package dev.dong4j.zeka.kernel.auth.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Contract;

/**
 * 认证用户角色实体类，用于描述系统中的角色信息和权限关系
 * <p>
 * 该类作为 RBAC（基于角色的访问控制）模型的核心组件，定义了角色的基本属性
 * 支持角色的唯一性识别和权限管理，为系统的权限控制提供基础支撑
 * <p>
 * 主要属性：
 * - 角色ID：角色的唯一标识符
 * - 角色名称：角色的显示名称，用于界面展示
 * - 角色键：角色的系统内部标识，用于权限判断
 * <p>
 * 实现了对象等价性比较和哈希值计算，基于角色ID和角色名称进行识别
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.26 21:44
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizationRole implements Serializable {
    /** 序列化版本号 */
    @Serial
    private static final long serialVersionUID = 1980476454419541600L;

    /** 角色唯一标识 */
    private Long id;
    /** 角色显示名称 */
    private String roleName;
    /** 角色系统内部标识键 */
    private String roleKey;

    /**
     * 计算对象的哈希值
     *
     * @return 基于角色ID和角色名称计算的哈希值
     * @since 1.0.0
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.roleName);
    }

    /**
     * 对象等价性比较
     *
     * @param o 待比较的对象
     * @return 如果两个角色的ID和名称都相同则返回 true
     * @since 1.0.0
     */
    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        AuthorizationRole role = (AuthorizationRole) o;
        return this.id.equals(role.id)
            && this.roleName.equals(role.roleName);
    }
}
