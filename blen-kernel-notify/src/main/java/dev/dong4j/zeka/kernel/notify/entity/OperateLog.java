package dev.dong4j.zeka.kernel.notify.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.05.08 20:32
 * @since 1.0.0
 */
@Data
public class OperateLog implements Serializable {

    /** serialVersionUID */
    @Serial
    private static final long serialVersionUID = -4589076318641916315L;
    /** Username */
    private String username;
    /** Operate time */
    private Date operateTime;
    /** Content */
    private Object content;
}
