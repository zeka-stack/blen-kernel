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
            // 设置存放自动生成的代码路径, 不填则默认当前项目下(src 上一级目录)
            .withModelPath("/Users/dong4j/Developer/0.Worker/opensource/zeka.stack/cubo-starter/cubo-dict-spring-boot/cubo-dict-spring-boot-core")
            .withVersion("1.0.0")
            .withCompany("Zeka.Stack")
            .withEmail("dong4j@gmail.com")
            // 设置谁作者名, 默认读取 ZEKA_NAME_SPACE 变量
            .withAuthor(SystemUtils.USER_NAME)
            // 设置包名 (前缀默认为 公司项目顶层包路径, 因此最终的包名为: ${公司项目顶层包路径}.${packageName})
            .withPackageName("starter.dict")
            // 忽略前缀
            .withPrefix(new String[]{"sys_"})
            // 设置根据哪张表生成代码, 可写多张表
            .withTables(new String[]{"sys_dictionary_type", "sys_dictionary_value"})
            // 设置需要生成的模板 不设置则全部生成
            .withTemplate(
                TemplatesConfig.DAO,
                TemplatesConfig.SERVICE,
                TemplatesConfig.ENUM,
                TemplatesConfig.IMPL,
                TemplatesConfig.ENTITY,
                TemplatesConfig.DTO,
                TemplatesConfig.QUERY,
                TemplatesConfig.CONVERTER,
                TemplatesConfig.XML,
                TemplatesConfig.START,
                TemplatesConfig.CONTROLLER,

                // 占位符而已, 避免手动删除逗号的烦恼
                TemplatesConfig.PLACEHOLDER
            )
            // 设置需要生成的配置
            .withComponets(
                // PropertiesConfig.BOOT_CONFIG
            )
            .withModuleType(ModuleConfig.ModuleType.SINGLE_MODULE)
            .build();
    }
}
