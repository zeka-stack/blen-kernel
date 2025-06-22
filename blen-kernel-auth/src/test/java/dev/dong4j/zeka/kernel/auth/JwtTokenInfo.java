package dev.dong4j.zeka.kernel.auth;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.14 09:40
 * @since 1.0.0
 */
@Data
@Builder
public class JwtTokenInfo implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -3233750396963053550L;

    /** User id */
    private Long userId;
    /** Username */
    private String username;
    /** 时间戳 */
    private Date time;
    private Long exp;
}
