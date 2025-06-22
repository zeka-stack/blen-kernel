package integration.controller;

import dev.dong4j.zeka.kernel.common.api.R;
import dev.dong4j.zeka.kernel.common.api.Result;
import integration.request.RequestFormWithCustomConstraint;
import org.jetbrains.annotations.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:19
 * @since 1.0.0
 */
@RestController
@Validated
@RequestMapping(value = "validator")
public class CustomValidatorDemoController {

    /**
     * Validate parameter result
     *
     * @param request request
     * @return the result
     * @since 1.0.0
     */
    @GetMapping(value = "custom")
    public Result<String> validateParameter(
        @NotNull @Valid RequestFormWithCustomConstraint request) {
        System.out.println(request.getName());
        return R.succeed("xxx");
    }

}

