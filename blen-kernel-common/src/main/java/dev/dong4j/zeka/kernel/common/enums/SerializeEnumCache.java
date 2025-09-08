package dev.dong4j.zeka.kernel.common.enums;

import dev.dong4j.zeka.ZekaStack;
import dev.dong4j.zeka.kernel.common.constant.App;
import java.util.Set;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

/**
 * 缓存实现的此接口的枚举类, 目前在 EnumController 和 SubLauncherInitiation#advance 被用到
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2024.03.26 01:14
 * @since 1.0.0
 */
public class SerializeEnumCache {

    /** SUB_ENUMS */
    @SuppressWarnings("rawtypes")
    public static final Set<Class<? extends SerializeEnum>> SUB_ENUMS;

    static {
        // 查找 SerializeEnum 的实现类 (扫描框架包和业务包)
        ConfigurationBuilder build = ConfigurationBuilder.build(ZekaStack.class.getPackageName(),
            App.BASE_PACKAGES,
            Scanners.SubTypes);

        build.setExpandSuperTypes(false);
        Reflections reflections = new Reflections(build);

        SUB_ENUMS = reflections.getSubTypesOf(SerializeEnum.class);
    }

}
