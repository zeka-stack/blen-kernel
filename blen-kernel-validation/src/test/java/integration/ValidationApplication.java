package integration;

import integration.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

/**
 * <p>Description: 只依赖 spring-boot-starter</p>
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
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:19
 * @since 1.0.0
 */
@Slf4j
@SpringBootApplication
public class ValidationApplication implements CommandLineRunner {
    /**
     * Main
     *
     * @param args args
     * @since 1.0.0
     */
    public static void main(String[] args) {
        SpringApplication.run(ValidationApplication.class);
    }

    /** Validator */
    @Resource
    private Validator validator;

    /**
     * Run
     *
     * @param args args
     * @since 1.0.0
     */
    @Override
    public void run(String... args) {
        UserVO user = new UserVO();
        user.setAge("111");
        user.setAddress("xxxxxxx");
        user.setEmail("ABCDE");
        user.setName("dong4j");

        Set<ConstraintViolation<UserVO>> violationSet = this.validator.validate(user);
        for (ConstraintViolation<UserVO> model : violationSet) {
            log.info("{}", model.getMessage());
        }
    }
}
