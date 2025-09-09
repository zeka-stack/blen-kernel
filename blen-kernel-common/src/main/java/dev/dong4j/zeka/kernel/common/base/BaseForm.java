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
 * <p>控制器层输入参数基类.
 * <p>为 Controller 层接收用户输入提供统一的基础类，仅限在视图层使用.
 * <p>主要功能：
 * <ul>
 *     <li>为控制器输入参数提供统一的基础结构</li>
 *     <li>继承 AbstractBaseEntity 的所有功能</li>
 *     <li>支持链式调用和建造者模式</li>
 *     <li>提供完整的参数验证支持</li>
 * </ul>
 * <p>适用场景：
 * <ul>
 *     <li>RESTful API 的请求参数封装</li>
 *     <li>表单提交的数据封装</li>
 *     <li>复杂查询条件的参数组织</li>
 *     <li>用户输入数据的统一验证入口</li>
 * </ul>
 * <p>使用约束：
 * <ul>
 *     <li>必须使用 validation 相关注解对字段进行标识</li>
 *     <li>禁止在服务层和数据层中使用</li>
 *     <li>应该包含业务验证规则和格式检查</li>
 *     <li>建议使用不可变对象设计</li>
 * </ul>
 *
 * @param <T> 主键类型，必须实现 Serializable 接口
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.18 16:11
 * @since 1.0.0
 */
@Data
@SuperBuilder
@NoArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class BaseForm<T extends Serializable> extends AbstractBaseEntity<T> {
    /** serialVersionUID */
    @Serial
    private static final long serialVersionUID = -3550589993340031894L;

}
