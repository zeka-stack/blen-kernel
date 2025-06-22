package dev.dong4j.zeka.kernel.test;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * <p>Description: 单元测试启动扩展类 </p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.03.18 17:30
 * @since 1.0.0
 */
@Slf4j
public class StarterExtension extends SpringExtension {

    /**
     * Before all *
     *
     * @param context context
     * @throws Exception exception
     * @since 1.0.0
     */
    @Override
    public void beforeAll(@NotNull ExtensionContext context) throws Exception {
        super.beforeAll(context);
    }
}
