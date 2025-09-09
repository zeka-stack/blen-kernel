package dev.dong4j.zeka.kernel.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

/**
 * <p>模块标识和序列化配置注解.
 * <p>用于标识不同业务模块的实体类，为模块化架构和序列化管理提供支持.
 * <p>主要功能：
 * <ul>
 *     <li>为不同业务模块的实体类提供统一标识</li>
 *     <li>支持模块化架构下的实体分类管理</li>
 *     <li>为序列化框架提供模块信息</li>
 *     <li>支持自动化的代码生成和管理</li>
 * </ul>
 * <p>适用场景：
 * <ul>
 *     <li>微服务架构下的实体模块分类</li>
 *     <li>领域驱动设计中的实体标识</li>
 *     <li>多模块项目的实体管理</li>
 *     <li>序列化和数据交换的模块识别</li>
 * </ul>
 * <p>配置说明：
 * <ul>
 *     <li>modelName/value：模块名称标识，默认为 "F"</li>
 *     <li>使用 @AliasFor 实现属性别名支持</li>
 *     <li>支持简化的配置方式：@ModelSerial("MODULE_NAME")</li>
 *     <li>或完整配置：@ModelSerial(modelName = "MODULE_NAME")</li>
 * </ul>
 * <p>模块标识规范：
 * <ul>
 *     <li>使用大写字母作为模块的唯一标识</li>
 *     <li>建议使用业务含义明确的简称</li>
 *     <li>避免与其他模块标识冲突</li>
 *     <li>保持模块标识的稳定性和向下兼容</li>
 * </ul>
 * <p>集成支持：
 * <ul>
 *     <li>与 Spring 框架的集成支持</li>
 *     <li>与序列化框架（Jackson、Gson）的集成</li>
 *     <li>与 ORM 框架（MyBatis、JPA）的集成</li>
 *     <li>支持自动化构建和部署工具集成</li>
 * </ul>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.06.10 16:16
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ModelSerial {
    /** DEFAULT */
    String DEFAULT = "F";

    /**
     * Model name
     *
     * @return the module name
     * @since 1.0.0
     */
    @AliasFor("value")
    String modelName() default DEFAULT;

    /**
     * Value
     *
     * @return the string
     * @since 1.0.0
     */
    @AliasFor("modelName")
    String value() default DEFAULT;
}
