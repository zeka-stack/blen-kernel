package dev.dong4j.zeka.kernel.common.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Description: 分页数据基类 </p>
 *
 * @param <T> parameter
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.09.04 14:29
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
public abstract class BasePage<T extends AbstractBaseEntity<?>> implements Serializable {

    /** serialVersionUID */
    @Serial
    private static final long serialVersionUID = 5982670181085815965L;
    /** Pagination */
    private IPage<T> pagination;

    /**
     * Build
     *
     * @param <T>        parameter
     * @param pagination pagination
     * @return the template user page
     * @since 1.0.0
     * @deprecated 使用 {@link com.baomidou.mybatisplus.core.metadata.IPage#wrapper} 代替
     */
    @Deprecated
    public static <T extends AbstractBaseEntity<?>> @NotNull BasePage<T> build(IPage<T> pagination) {
        BasePage<T> basePage = new BasePage<T>() {
            @Serial
            private static final long serialVersionUID = -6820396419783198219L;
        };

        // 由于 json 反序列化不能使用抽象类和接口, 这里强转为 Page
        basePage.setPagination(pagination);
        return basePage;
    }
}
