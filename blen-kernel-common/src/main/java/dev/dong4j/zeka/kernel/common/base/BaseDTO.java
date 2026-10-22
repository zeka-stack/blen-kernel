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
 * <p>为服务层与其他层之间的数据交换提供统一的基础类，用于 Service 与 Controller 间的数据契约.
 * <p>主要功能：
 * <ul>
 *     <li>为服务层数据传输提供统一的基础结构</li>
 *     <li>继承 AbstractBaseEntity 的所有功能</li>
 *     <li>支持链式调用和建造者模式</li>
 *     <li>提供完整的对象比较和字符串输出</li>
 * </ul>
 * <p>适用场景：
 * <ul>
 *     <li>在 Service 与 Controller 等组件间传递数据</li>
 *     <li>业务逻辑处理中的中间数据结构</li>
 *     <li>业务输出模型的显式字段约束</li>
 *     <li>需要业务逻辑封装的复杂数据结构</li>
 * </ul>
 * <p>使用约束：
 * <ul>
 *     <li>可以作为 Controller 返回值；请求输入使用 Form，避免直接暴露持久层 PO</li>
 *     <li>禁止在数据持久化层直接使用</li>
 *     <li>只描述传输数据，业务处理逻辑应保留在 Service</li>
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
