package dev.dong4j.zeka.kernel.common.basic.util;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import org.jetbrains.annotations.Contract;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.2.4
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.02.19 14:38
 * @since 1.0.0
 */
@Data
public class UserInfo implements Serializable {

    /** serialVersionUID */
    @Serial
    private static final long serialVersionUID = 1L;

    /** Id */
    private String id;
    /** Username */
    private String username;
    /** Password */
    private String password;

    /**
     * User info
     *
     * @since 1.0.0
     */
    @Contract(pure = true)
    public UserInfo() {
    }

}
