package integration.controller;

import dev.dong4j.zeka.kernel.common.api.R;
import dev.dong4j.zeka.kernel.common.api.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * <p>Description: </p>
 * 字段验证, 使用 @Validated + 具体注解
 * BaseController 中统一添加了 @Validated, 所以不用在添加了
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:20
 * @since 1.0.0
 */
@RestController
@RequestMapping(value = "validator")
public class ParameterValidatorDemoController {

    /**
     * Validate parameter result
     *
     * @param name name
     * @return the result
     * @since 1.0.0
     */
    @GetMapping(value = "simple")
    public Result<String> validateParameter(@Size(min = 1, max = 5) String name) {
        return R.succeed("xxx");
    }

    /**
     * Validate parameter 2 result
     *
     * @param name name
     * @return the result
     * @since 1.0.0
     */
    @GetMapping(value = "simple2")
    public Result<Void> validateParameter2(@Size(min = 1, max = 5) String name) {
        return R.succeed();
    }

    /**
     * Validate parameter 3 result
     *
     * @param name name
     * @return the result
     * @since 1.0.0
     */
    @GetMapping(value = "simple3")
    public Result<Void> validateParameter3(@Size(min = 1, max = 5) String name) {
        return R.failed("返回失败测试");
    }

    /**
     * Validate parameter 4 result
     *
     * @param name name
     * @return the result
     * @since 1.0.0
     */
    @GetMapping(value = "simple4")
    public Result<Void> validateParameter4(@Size(min = 1, max = 5) String name) {
        return R.succeed();
    }

    /**
     * Validate parameter 5 result
     *
     * @param id id
     * @return the result
     * @since 1.0.0
     */
    @GetMapping(value = "simple5")
    public Result<Void> validateParameter5(@RequestHeader @NotNull Integer id) {
        return R.succeed();
    }

}
