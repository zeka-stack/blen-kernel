package dev.dong4j.zeka.kernel.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Contract;

import java.io.Serializable;
import java.util.Objects;

/**
 * <p>Description: 认证用户角色实体 </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.26 21:44
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizationRole implements Serializable {
    /** serialVersionUID */
    private static final long serialVersionUID = 1980476454419541600L;

    /** Id */
    private Long id;
    /** Role name */
    private String roleName;
    /** Role key */
    private String roleKey;

    /**
     * Hash code int
     *
     * @return the int
     * @since 1.0.0
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.roleName);
    }

    /**
     * Equals boolean
     *
     * @param o o
     * @return the boolean
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
