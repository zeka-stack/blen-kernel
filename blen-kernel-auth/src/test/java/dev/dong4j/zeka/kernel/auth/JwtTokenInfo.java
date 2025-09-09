package dev.dong4j.zeka.kernel.auth;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

/**
 * JWT 令牌信息测试实体类，用于单元测试中构造测试数据
 * <p>
 * 该类主要用于 JWT 相关功能的测试，提供简化的令牌信息数据结构
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

    /** 序列化版本号 */
    @Serial
    private static final long serialVersionUID = -3233750396963053550L;

    /** 测试用户 ID */
    private Long userId;
    /** 测试用户名 */
    private String username;
    /** 测试时间戳 */
    private Date time;
    /** 测试过期时间 */
}
