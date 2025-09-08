package dev.dong4j.zeka.kernel.common.jackson;

import dev.dong4j.zeka.kernel.common.enums.SerializeEnum;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 多态序列化注解，用于配置字段的多态序列化行为
 * <p>
 * 该注解可以标记在字段上，用于指定该字段在进行多态序列化时的配置信息
 * 支持两种多态识别机制：基于枚举的类型映射和基于配置的类型映射
 * <p>
 * 主要特性：
 * - 支持基于SerializeEnum枚举的类型映射
 * - 支持基于@Type注解的类型映射配置
 * - 可以自定义类型标识字段名称
 * - 支持多种类型的映射关系配置
 * - 运行时保留，支持反射检查和动态处理
 * <p>
 * 使用方式：
 * 1. 基于枚举的方式：设置enumClass属性指定实现SerializeEnum接口的枚举类
 * 2. 基于配置的方式：使用types属性配置多个@Type映射关系
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.26 21:43
 * @since 1.0.0
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Polymorphic {

    /**
     * 类型标识字段名称
     * <p>
     * 指定JSON中用于标识对象类型的字段名称
     * 在序列化时会在该字段中写入类型标识符，反序列化时根据该字段值确定对象类型
     *
     * @return 类型标识字段名
     * @since 1.0.0
     */
    String value() default "";

    /**
     * 指定用于类型映射的枚举类
     * <p>
     * 当使用基于枚举的多态映射时，需要指定一个实现了SerializeEnum接口的枚举类
     * 枚举中的每个值都包含一个类型标识和对应的具体类型
     * 默认为SerializeEnum.class，表示不使用枚举映射
     *
     * @return 实现SerializeEnum接口的枚举类
     * @since 1.0.0
     */
    Class<? extends SerializeEnum> enumClass() default SerializeEnum.class;

    /**
     * 类型映射配置数组
     * <p>
     * 当不使用枚举映射时，可以通过该属性直接配置类型标识与具体类型的映射关系
     * 每个@Type注解定义一个映射关系，包含类型标识和对应的Java类
     *
     * @return 类型映射配置数组
     * @since 1.0.0
     */
    Type[] types() default {};

    /**
     * 类型映射配置注解，用于定义单个类型标识与具体类型的映射关系
     * <p>
     * 该注解用于在@Polymorphic注解的types属性中配置具体的映射关系
     * 每个@Type注解定义一个类型标识字符串与具体Java类的对应关系
     * <p>
     * 使用示例：
     * {@code @Type(value = "user", clz = UserInfo.class)}
     * {@code @Type(value = "admin", clz = AdminInfo.class)}
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2019.12.26 21:43
     * @since 1.0.0
     */
    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface Type {
        /**
         * 类型标识字符串
         * <p>
         * 在JSON中用于标识此类型的字符串值
         * 序列化时会将该值写入指定的标识字段中，反序列化时根据该值确定对象类型
         *
         * @return 类型标识字符串
         * @since 1.0.0
         */
        String value() default "";

        /**
         * 对应的Java类型
         * <p>
         * 指定与类型标识字符串对应的具体Java类
         * 该类必须实现IPolymorphic接口才能被正确地进行多态序列化和反序列化
         *
         * @return 对应的Java类型
         * @since 1.0.0
         */
        Class<?> clz();
    }
}
