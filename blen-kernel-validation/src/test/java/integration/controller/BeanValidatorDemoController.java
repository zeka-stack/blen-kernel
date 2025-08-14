package integration.controller;

import dev.dong4j.zeka.kernel.common.api.R;
import dev.dong4j.zeka.kernel.common.api.Result;
import integration.request.RequestForm;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>Description: </p>
 * bean 验证, 使用 @Valid
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.27 15:13
 * @since 1.0.0
 */
@RestController
@RequestMapping(value = "validator")
public class BeanValidatorDemoController {

    /**
     * org.jetbrains.annotations.NotNull 注解, 如果为 null 会抛 500
     *
     * @param request request
     * @return the result
     * @since 1.0.0
     */
    @GetMapping(value = "bean")
    public Result<Void> validate(@NotNull @Valid RequestForm request) {
        System.out.println(request.getName());
        return R.succeed();
    }

    /**
     * 未做验证
     *
     * @param request request
     * @return the result
     * @since 1.0.0
     */
    @GetMapping(value = "bean/1")
    public Result<Void> validate1(RequestForm request) {
        System.out.println(request.getName());
        return R.succeed();
    }

    /**
     * 这个接口不会抛异常, 因为 RequestForm 肯定不会为空, 因为在 spring 参数绑定时, 会实例化一个对象, 如果前端没有穿任何参数, 也只是所有字段为 null 的实例
     * 所以不可能为空.
     *
     * @param request request
     * @return the result
     * @since 1.0.0
     */
    @GetMapping(value = "bean/2")
    public Result<Void> validate2(@validation.constraints.NotNull RequestForm request) {
        System.out.println(request.getName());
        return R.succeed();
    }
}
