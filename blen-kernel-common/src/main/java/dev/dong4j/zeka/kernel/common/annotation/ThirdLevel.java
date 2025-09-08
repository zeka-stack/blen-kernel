package dev.dong4j.zeka.kernel.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Description: 错误码来源 </p>
 * <p>A: 来源用户 (比如: 参数错误) </p>
 * <p>B: 来源系统 (比如: 空指针异常等) </p>
 * <p>C: 来源第三方服务 (比如: 调用短信或者邮件等三方服务) </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.06.10 16:09
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ThirdLevel {
}
