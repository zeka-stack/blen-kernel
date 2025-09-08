package dev.dong4j.zeka.kernel.test;

import java.io.Serial;

/**
 * Zeka框架测试专用异常类，用于封装测试环境中的错误和异常情况
 * <p>
 * 该异常类继承自RuntimeException，专门用于测试环境中的错误处理
 * 可以用于标识测试配置错误、环境初始化失败等情况
 * <p>
 * 主要特性：
 * - 不需要显式捕获，可以自动传播
 * - 提供清晰的测试相关错误信息
 * - 支持序列化和反序列化
 * - 为测试错误诊断提供帮助
 * <p>
 * 使用场景：测试配置错误、环境初始化失败等需要明确标识的情况
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:22
 * @since 1.0.0
 */
class ZekaBootTestException extends RuntimeException {

    /** 序列化版本号，用于控制序列化兼容性 */
    @Serial
    private static final long serialVersionUID = -286968533868184170L;

    /**
     * 构造一个带有错误信息的Zeka测试异常
     * <p>
     * 创建一个包含详细错误信息的测试异常实例
     * 错误信息应该描述具体的测试问题和失败原因
     *
     * @param message 详细的错误信息描述
     * @since 1.0.0
     */
    ZekaBootTestException(String message) {
        super(message);
    }
}
