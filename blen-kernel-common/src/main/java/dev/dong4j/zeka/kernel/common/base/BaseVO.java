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
 * <p>控制器层输出对象基类.
 * <p>为 Controller 层返回给前端的数据提供统一的基础类，仅限在视图层使用.
 * <p>主要功能：
 * <ul>
 *     <li>为控制器输出数据提供统一的基础结构</li>
 *     <li>继承 AbstractBaseEntity 的所有功能</li>
 *     <li>支持链式调用和建造者模式</li>
 *     <li>提供完整的 API 文档生成支持</li>
 * </ul>
 * <p>适用场景：
 * <ul>
 *     <li>RESTful API 的响应数据封装</li>
 *     <li>前端页面展示数据的统一格式</li>
 *     <li>第三方接口数据的返回封装</li>
 *     <li>移动端 APP 的数据交换格式</li>
 * </ul>
 * <p>使用约束：
 * <ul>
 *     <li>必须使用 Swagger 注解对字段进行文档标识</li>
 *     <li>禁止在服务层和数据层中使用</li>
 *     <li>应该包含业务展示相关的数据和格式</li>
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
public abstract class BaseVO<T extends Serializable> extends AbstractBaseEntity<T> {
    /** serialVersionUID */
    @Serial
    private static final long serialVersionUID = -3550589993340031894L;
}
