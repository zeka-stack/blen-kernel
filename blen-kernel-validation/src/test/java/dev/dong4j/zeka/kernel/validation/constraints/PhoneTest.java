package dev.dong4j.zeka.kernel.validation.constraints;

import dev.dong4j.zeka.kernel.validation.AuthContextConfiguration;
import dev.dong4j.zeka.kernel.validation.util.BeanValidator;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * <p>Description: 参数验证</p>
 * 1. springboot 使用 hibernate validator 校验 https://cloud.tencent.com/developer/article/1054194
 * 2. Spring Boot 参数校验 https://www.cnblogs.com/cjsblog/p/8946768.html
 * JSR 提供的校验注解 :
 * Null 被注释的元素必须为 null
 * NotNull 被注释的元素必须不为 null
 * AssertTrue 被注释的元素必须为 true
 * AssertFalse 被注释的元素必须为 false
 * Min(value) 被注释的元素必须是一个数字,其值必须大于等于指定的最小值
 * Max(value) 被注释的元素必须是一个数字,其值必须小于等于指定的最大值
 * DecimalMin(value) 被注释的元素必须是一个数字,其值必须大于等于指定的最小值
 * DecimalMax(value) 被注释的元素必须是一个数字,其值必须小于等于指定的最大值
 * Size(max=, min=)   被注释的元素的大小必须在指定的范围内
 * Digits (integer, fraction)     被注释的元素必须是一个数字,其值必须在可接受的范围内
 * Past 被注释的元素必须是一个过去的日期
 * Future 被注释的元素必须是一个将来的日期
 * Pattern(regex=,flag=) 被注释的元素必须符合指定的正则表达式
 * Hibernate Validator 提供的校验注解 :
 * NotBlank(message =)   验证字符串非 null,且长度必须大于 0
 * Email 被注释的元素必须是电子邮箱地址
 * Length(min=,max=) 被注释的字符串的大小必须在指定的范围内
 * NotEmpty 被注释的字符串的必须非空
 * Range(min=,max=,message=) 被注释的元素必须在合适的范围内
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.27 13:24
 * @since 1.0.0
 */
@Slf4j
class PhoneTest extends AuthContextConfiguration {

    /**
     * username 和 password 验证
     * phone 可为 null
     *
     * @since 1.0.0
     */
    @Test
    void test1() {
        LoginForm user = LoginForm.builder().username("")
            .password("")
            .code("")
            .uuid("")
            .build();

        try {
            Map<String, String> validateobject = BeanValidator.validateobject(user);
            if (validateobject != null && validateobject.size() > 0) {
                for (Map.Entry<String, String> entry : validateobject.entrySet()) {
                    log.error("{}->{}", entry.getKey(), entry.getValue());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }


    /**
     * phone 为 ""
     *
     * @since 1.0.0
     */
    @Test
    void test2() {
        LoginForm user = LoginForm.builder().username("admin")
            .password("123456")
            .code("xxx")
            .uuid("xxx")
            .phone("")
            .build();

        try {
            Map<String, String> validateobject = BeanValidator.validateobject(user);
            if (validateobject != null && validateobject.size() > 0) {
                for (Map.Entry<String, String> entry : validateobject.entrySet()) {
                    log.error("{}->{}", entry.getKey(), entry.getValue());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * phone 不符合电话号码规则
     *
     * @since 1.0.0
     */
    @Test
    void test3() {
        LoginForm user = LoginForm.builder().username("admin")
            .password("123456")
            .code("xxx")
            .uuid("xxx")
            .phone("111")
            .build();

        try {
            Map<String, String> validateobject = BeanValidator.validateobject(user);
            if (validateobject != null && validateobject.size() > 0) {
                for (Map.Entry<String, String> entry : validateobject.entrySet()) {
                    log.error("{}->{}", entry.getKey(), entry.getValue());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * phone 符合电话号码规则
     *
     * @since 1.0.0
     */
    @Test
    void test4() {
        LoginForm user = LoginForm.builder().username("admin")
            .password("123456")
            .code("xxx")
            .uuid("xxx")
            .phone("18000000000")
            .build();

        try {
            Map<String, String> validateobject = BeanValidator.validateobject(user);
            if (validateobject != null && validateobject.size() > 0) {
                for (Map.Entry<String, String> entry : validateobject.entrySet()) {
                    log.error("{}->{}", entry.getKey(), entry.getValue());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * The type Login form.
     *
     * @author dong4j
     * @version 1.2.3
     * @email "mailto:dong4j@gmail.com"
     * @date 2019.12.27 13:24
     * @since 1.0.0
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "LoginForm", description = "统一登录实体")
    private static class LoginForm implements Serializable {
        /** serialVersionUID */
        private static final long serialVersionUID = -2343233251882358359L;
        /** 用户名 */
        @NotBlank(message = "用户名不能为空")
        @Size(min = 4, max = 32, message = "用户名长度限制在 4 ~ 32 位")
        private String username;
        /** 密码 */
        @NotBlank(message = "密码不能为空")
        @Size(min = 6, max = 64, message = "密码长度限制在 6 ~ 64 位")
        private String password;
        /** Phone */
        @Phone
        private String phone;
        /** 验证码 */
        private String code;
        /** 与验证码绑定的 uuid */
        private String uuid;
    }
}


