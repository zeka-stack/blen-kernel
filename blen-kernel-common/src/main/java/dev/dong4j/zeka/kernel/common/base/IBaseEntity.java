package dev.dong4j.zeka.kernel.common.base;

import java.io.Serializable;

/**
 * <p>基础实体接口.
 * <p>定义了所有实体类的基本结构和规范，主要提供 ID 字段的管理能力.
 * <p>作为所有实体类的顶层接口，保证了数据层和业务层的一致性.
 * <p>主要功能：
 * <ul>
 *     <li>定义通用的 ID 字段获取方法</li>
 *     <li>提供常用的字段名称常量</li>
 *     <li>支持任意类型的主键（泛型设计）</li>
 *     <li>继承 Serializable，支持序列化</li>
 * </ul>
 * <p>设计优势：
 * <ul>
 *     <li>统一的实体类规范和约束</li>
 *     <li>方便 ORM 框架的集成和使用</li>
 *     <li>支持反射和动态代理操作</li>
 *     <li>提供了常用操作的常量定义</li>
 * </ul>
 * <p>使用圼景：
 * <ul>
 *     <li>所有需要 ID 管理的实体类</li>
 *     <li>数据库表对应的实体映射</li>
 *     <li>API 数据传输对象的基础接口</li>
 *     <li>需要统一管理的业务实体</li>
 * </ul>
 *
 * @param <T> 主键类型，必须实现 Serializable 接口
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.18 16:11
 * @since 1.0.0
 */
public interface IBaseEntity<T extends Serializable> extends Serializable {
    /** ID */
    String ID = "id";
    /** GET_ID */
    String GET_ID = "getId";

    /**
     * Gets id *
     *
     * @return the serializable
     * @since 1.0.0
     */
    T getId();
}
