package dev.dong4j.zeka.kernel.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>枚举字段序列化值注解.
 * <p>用于标识枚举类中的序列化字段，支持普通枚举类与数据库字段的映射和转换.
 * <p>主要功能：
 * <ul>
 *     <li>标识枚举类中用于序列化的字段</li>
 *     <li>支持枚举与数据库字段的自动映射</li>
 *     <li>提供灵活的序列化和反序列化机制</li>
 *     <li>支持多种数据类型的枚举映射</li>
 * </ul>
 * <p>适用场景：
 * <ul>
 *     <li>数据库枚举字段映射：如状态码、类型标识</li>
 *     <li>JSON 序列化：枚举与字符串或数字的转换</li>
 *     <li>API 数据交换：枚举值与外部系统兼容</li>
 *     <li>配置文件解析：枚举与配置值的绑定</li>
 * </ul>
 * <p>使用示例：
 * <pre>
 * &#64;TableName("student")
 * class Student {
 *     private Integer id;
 *     private String name;
 *     private GradeEnum grade;//数据库grade字段类型为int
 * }
 *
 * public enum GradeEnum {
 *     PRIMARY(1,"小学"),
 *     SECONDORY("2", "中学"),
 *     HIGH(3, "高中");
 *
 *     &#64;SerializeValue
 *     private final int code;
 *     private final String descp;
 * }
 * </pre>
 * <p>技术特性：
 * <ul>
 *     <li>支持基本数据类型和包装类型的序列化</li>
 *     <li>与 MyBatis、JPA 等 ORM 框架集成</li>
 *     <li>支持自定义的序列化和反序列化逻辑</li>
 *     <li>提供类型安全的枚举操作</li>
 * </ul>
 * <p>最佳实践：
 * <ul>
 *     <li>在枚举类的序列化字段上使用此注解</li>
 *     <li>保持枚举值与数据库存储值的一致性</li>
 *     <li>考虑向后兼容性设计枚举值</li>
 *     <li>为枚举提供清晰的文档说明</li>
 * </ul>
 *
 * @author yuxiaobin
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.03.23 19:10
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SerializeValue {

}
