package dev.dong4j.zeka.kernel.devtools;

import dev.dong4j.zeka.kernel.common.util.SystemUtils;
import org.junit.jupiter.api.Test;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.03.20 16:19
 * @since 1.0.0
 */
class AutoGeneratorCodeTest {

    /**
     * Simple auto generator code
     *
     * @since 1.0.0
     */
    @Test
    void simpleAutoGeneratorCode() {
        AutoGeneratorCodeBuilder.onAutoGeneratorCode()
            // 设置存放自动生成的代码路径, 不填则默认当前项目下
            .withModelPath("")
            .withVersion("1.0.0")
            // 设置谁作者名, 默认读取 ZEKA_NAME_SPACE 变量
            .withAuthor(SystemUtils.USER_NAME)
            // 设置包名 (前缀默认为 dev.dong4j.zeka, 因此最终的包名为: dev.dong4j.zeka.${packageName})
            .withPackageName("starter.example.test")
            // 忽略前缀
            .withPrefix(new String[]{""})
            // 设置根据哪张表生成代码, 可写多张表
            .withTables(new String[]{"user"})
            // 设置需要生成的模板 不设置则全部生成
            .withTemplate(
                TemplatesConfig.DAO,
                TemplatesConfig.SERVICE,
                TemplatesConfig.IMPL,
                TemplatesConfig.ENTITY,
                TemplatesConfig.VO,
                TemplatesConfig.DTO,
                TemplatesConfig.WRAPPER,
                TemplatesConfig.XML,
                TemplatesConfig.START,
                TemplatesConfig.CONTROLLER
            )
            // 设置需要生成的配置
            .withComponets(
                PropertiesConfig.BOOT_CONFIG
            )
            .withModuleType(ModuleConfig.ModuleType.MULTI_MODULE)
            .build();
    }

}
