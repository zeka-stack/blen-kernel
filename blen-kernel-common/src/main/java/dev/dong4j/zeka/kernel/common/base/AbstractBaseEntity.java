package dev.dong4j.zeka.kernel.common.base;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * <p>抽象基础实体类.
 * <p>为所有基础实体提供通用的 ID 字段和相关功能，作为实体类的基础父类.
 * <p>集成了 Lombok 注解和 Swagger 文档注解，提供了完整的实体类功能.
 * <p>主要功能：
 * <ul>
 *     <li>提供通用的主键 ID 字段管理</li>
 *     <li>支持任意类型的主键（泛型设计）</li>
 *     <li>集成了常用的 Lombok 功能注解</li>
 *     <li>支持链式调用和建造者模式</li>
 * </ul>
 * <p>特性说明：
 * <ul>
 *     <li>实现了 IBaseEntity 接口，保证接口一致性</li>
 *     <li>使用 @SuperBuilder 支持继承链式构建</li>
 *     <li>支持 Swagger 文档自动生成</li>
 *     <li>完全的序列化支持</li>
 * </ul>
 * <p>使用场景：
 * <ul>
 *     <li>作为所有领域实体类的基础父类</li>
 *     <li>JPA/MyBatis 等 ORM 框架的实体映射</li>
 *     <li>API 接口的数据传输对象</li>
 *     <li>需要统一 ID 管理的所有实体</li>
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
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
public abstract class AbstractBaseEntity<T extends Serializable> implements IBaseEntity<T> {
    /** serialVersionUID */
    @Serial
    private static final long serialVersionUID = -3550589993340031894L;

    /** 实体 Id */
    @Schema(description = "实体ID 新增时可不填,修改时必填")
    private T id;

    /**
     * Pk val serializable
     *
     * @return the serializable
     * @since 1.0.0
     */
    @Override
    public T getId() {
        return this.id;
    }
}
