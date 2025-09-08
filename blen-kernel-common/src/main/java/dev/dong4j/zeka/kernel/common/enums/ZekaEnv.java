package dev.dong4j.zeka.kernel.common.enums;

import dev.dong4j.zeka.kernel.common.constant.App;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Zeka框架环境配置枚举，定义了应用程序的部署环境类型
 * <p>
 * 该枚举定义了从开发到生产的完整部署环境链路
 * 每个环境都包含相应的日志级别配置，用于优化不同环境下的日志输出
 * <p>
 * 主要特性：
 * - 完整的环境链路：从本地开发到生产部署的全部环境
 * - 日志级别管理：为不同环境配置适当的日志级别
 * - 环境识别：提供环境名称的统一管理和识别
 * - 配置化支持：与Spring Profile系统完美集成
 * - 工具方法：提供环境转换和列表获取功能
 * <p>
 * 环境定义：
 * - <b>LOCAL</b>：本地开发环境，日志级别为DEBUG，开启所有调试信息
 * - <b>DEV</b>：开发环境，日志级别为DEBUG，用于开发阶段测试
 * - <b>TEST</b>：测试环境，日志级别为INFO，用于功能测试和集成测试
 * - <b>PREV</b>：预演环境（类似于Staging），日志级别为INFO，用于上线前的最后验证
 * - <b>PROD</b>：生产环境，日志级别为INFO，用于正式对外提供服务
 * <p>
 * 使用场景：
 * - Spring Boot应用的Profile配置和环境切换
 * - 不同环境下的日志级别控制
 * - 条件化的Bean初始化和配置加载
 * - 环境相关的业务逻辑判断
 * - 部署脚本和构建工具的环境识别
 * <p>
 * 使用示例：
 * <pre>{@code
 * // 获取当前环境
 * String activeProfile = environment.getActiveProfiles()[0];
 * ZekaEnv currentEnv = ZekaEnv.of(activeProfile);
 * 
 * // 根据环境设置日志级别
 * String logLevel = currentEnv.getLogLevel();
 * 
 * // 环境判断
 * if (currentEnv == ZekaEnv.PROD) {
 *     // 生产环境特殊处理
 * }
 * 
 * // 获取所有环境名称
 * List<String> allEnvs = ZekaEnv.getEnvList();
 * }</pre>
 * <p>
 * 注意事项：
 * - 环境名称应与Spring Boot的application-{profile}.yml配置文件保持一致
 * - 建议在不同环境下使用不同的配置参数和资源
 * - 生产环境下应关闭DEBUG级别日志以提高性能
 * - 环境转换方法of()会返回默认值 DEV，防止空指针异常
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 14:45
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum ZekaEnv {
    /** local 默认 */
    LOCAL(App.ENV_LOCAL, "DEBUG"),
    /** dev (开发) */
    DEV(App.ENV_DEV, "DEBUG"),
    /** test (测试) */
    TEST(App.ENV_TEST, "INFO"),
    /** staging (预演环境) */
    PREV(App.ENV_PREV, "INFO"),
    /** prod (正式) */
    PROD(App.ENV_PROD, "INFO");

    /** 等级 */
    private final String name;
    /** log 文件的级别 */
    private final String logLevel;

    /**
     * 获取环境列表
     *
     * @return 环境列表 env list
     * @since 1.0.0
     */
    public static List<String> getEnvList() {
        return Arrays.stream(ZekaEnv.values())
            .map(ZekaEnv::getName)
            .collect(Collectors.toList());
    }

    /**
     * 环境转换
     *
     * @param env the env
     * @return 环境 env
     * @since 1.0.0
     */
    public static ZekaEnv of(String env) {
        ZekaEnv[] values = ZekaEnv.values();
        for (ZekaEnv micaEnv : values) {
            if (micaEnv.name.equals(env)) {
                return micaEnv;
            }
        }
        return ZekaEnv.DEV;
    }
}
