package integration.controller;

import dev.dong4j.zeka.kernel.common.api.R;
import dev.dong4j.zeka.kernel.common.api.Result;
import integration.vo.UserVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>Description: </p>
 * Validate (Valid 的扩展, 具有分组功能): 用在方法入参上无法单独提供嵌套验证功能. 不能用在成员属性 (字段) 上,也无法提示框架进行嵌套验证. 能配合嵌套验证注解 @Valid 进行嵌套验证.
 * Valid: 用在方法入参上无法单独提供嵌套验证功能. 能够用在成员属性 (字段) 上,提示验证框架进行嵌套验证. 能配合嵌套验证注解 @Valid 进行嵌套验证
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:19
 * @since 1.0.0
 */
@RestController
@RequestMapping("/v3")
public class ValidationController3 {

    /**
     * 实体验证, 必须使用 @Valid
     *
     * @param userVO the user vo
     * @return the result
     * @since 1.0.0
     */
    @PostMapping("/test")
    public Result<Void> test(@RequestBody UserVO userVO) {
        return R.failed();
    }

    /**
     * Test 1 result
     *
     * @param userVO user vo
     * @return the result
     * @since 1.0.0
     */
    @PostMapping("/test1")
    public Result<Void> test1(@Valid @RequestBody UserVO userVO) {
        return R.failed();
    }

    /**
     * Test 2 result
     *
     * @param userVO user vo
     * @return the result
     * @since 1.0.0
     */
    @GetMapping("/test2")
    public Result<Void> test2(@Valid UserVO userVO) {
        return R.failed();
    }

    /**
     * Test 3 result
     *
     * @param userVO user vo
     * @return the result
     * @since 1.0.0
     */
    @GetMapping("/test3")
    public Result<Void> test3(UserVO userVO) {
        return R.failed();
    }

}
