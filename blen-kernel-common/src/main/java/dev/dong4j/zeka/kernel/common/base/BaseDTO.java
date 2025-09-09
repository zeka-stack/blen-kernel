package dev.dong4j.zeka.kernel.common.base;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * <p>服务层数据传输对象基类.
 * <p>为服务层与其他层之间的数据交换提供统一的基础类，仅在服务层内部使用.
 * <p>主要功能：
 * <ul>
 *     <li>为服务层数据传输提供统一的基础结构</li>
 *     <li>继承 AbstractBaseEntity 的所有功能</li>
 *     <li>支持链式调用和建造者模式</li>
 *     <li>提供完整的对象比较和字符串输出</li>
 * </ul>
 * <p>适用场景：
 * <ul>
 *     <li>在服务层内部在不同组件间传递数据</li>
 *     <li>业务逻辑处理中的中间数据结构</li>
 *     <li>服务层与 Repository 层的数据交换</li>
 *     <li>需要业务逻辑封装的复杂数据结构</li>
 * </ul>
 * <p>使用约束：
 * <ul>
 *     <li>禁止在 Controller 层使用作为参数或返回值</li>
 *     <li>禁止在数据持久化层直接使用</li>
 *     <li>应该包含业务相关的数据和逻辑</li>
 * </ul>
 *
 * @param <T> 主键类型，必须实现 Serializable 接口
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.08 21:49
 * @since 1.0.0
 */
@Data
@SuperBuilder
@NoArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class BaseDTO<T extends Serializable> extends AbstractBaseEntity<T> {
    /** serialVersionUID */
    @Serial
    private static final long serialVersionUID = 4484918372546552703L;

}
