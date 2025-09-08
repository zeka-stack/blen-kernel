package dev.dong4j.zeka.kernel.test;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * 单元测试启动扩展类，提供JUnit 5和Spring测试的集成支持
 * <p>
 * 该类继承自SpringExtension，为JUnit 5测试提供Spring框架的支持
 * 可以在测试执行前后进行自定义的初始化和清理操作
 * <p>
 * 主要功能：
 * - 集成Spring和JUnit 5的测试环境
 * - 支持依赖注入和上下文管理
 * - 提供测试生命周期的扩展点
 * - 支持测试环境的自定义初始化
 * <p>
 * 使用场景：需要在测试执行前进行特殊配置或环境准备的情况
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.03.18 17:30
 * @since 1.0.0
 */
@Slf4j
public class StarterExtension extends SpringExtension {

    /**
     * 在所有测试方法执行前的初始化操作
     * <p>
     * 重写父类方法，在Spring测试上下文初始化前进行自定义准备工作
     * 可以在此方法中添加全局测试环境的初始化逻辑
     *
     * @param context JUnit测试扩展上下文
     * @throws Exception 初始化过程中可能抛出的异常
     * @since 1.0.0
     */
    @Override
    public void beforeAll(@NotNull ExtensionContext context) throws Exception {
        super.beforeAll(context);
    }
}
