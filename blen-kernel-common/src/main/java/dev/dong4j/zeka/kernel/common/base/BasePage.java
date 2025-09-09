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
 * <p>分页数据封装基类.
 * <p>为分页查询结果提供统一的封装和处理，支持 MyBatis-Plus 分页组件集成.
 * <p>主要功能：
 * <ul>
 *     <li>封装分页查询的数据结果和分页信息</li>
 *     <li>支持 MyBatis-Plus IPage 的集成和传递</li>
 *     <li>提供便捷的分页数据构建方法</li>
 *     <li>支持 JSON 序列化和反序列化</li>
 * </ul>
 * <p>适用场景：
 * <ul>
 *     <li>分页查询结果的统一返回格式</li>
 *     <li>前后端分页数据交换</li>
 *     <li>列表查询的数据展示</li>
 *     <li>大数据量查询的性能优化</li>
 * </ul>
 * <p>技术特性：
 * <ul>
 *     <li>支持泛型设计，类型安全</li>
 *     <li>集成 Lombok 注解，简化代码</li>
 *     <li>完全的序列化支持</li>
 *     <li>build 方法已废弃，建议使用 IPage.wrapper</li>
 * </ul>
 *
 * @param <T> 实体类型，必须继承 AbstractBaseEntity
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
