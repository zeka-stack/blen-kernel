package dev.dong4j.zeka.kernel.common.api;

import dev.dong4j.zeka.kernel.common.CoreBundle;
import dev.dong4j.zeka.kernel.common.annotation.BusinessLevel;
import dev.dong4j.zeka.kernel.common.annotation.ModelSerial;
import dev.dong4j.zeka.kernel.common.annotation.SystemLevel;
import dev.dong4j.zeka.kernel.common.assertion.BaseExceptionAssert;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>Description: 通用返回结果 </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.02 11:36
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
@ModelSerial
public enum BaseCodes implements BaseExceptionAssert {
    /** 成功 */
    SUCCESS(Integer.parseInt(Result.SUCCESS_CODE), Result.SUCCESS_MESSAGE),
    /** 默认没有数据响应 */
    @SystemLevel
    DEFAULT_NULL_DATA(2222, CoreBundle.message("code.default.null.data")),
    /** 默认的失败响应 */
    @SystemLevel
    FAILURE(Integer.parseInt(Result.FAILURE_CODE), Result.FAILURE_MESSAGE),
    /** 参数校验失败 */
    @BusinessLevel
    PARAM_VERIFY_ERROR(4100, CoreBundle.message("code.param.verify.error")),
    /** 数据不存在 */
    @SystemLevel
    DATA_ERROR(4101, CoreBundle.message("code.data.error")),
    /** 操作失败 */
    @SystemLevel
    OPTION_FAILURE(4102, CoreBundle.message("code.option.failure")),
    /** Config error base codes */
    @SystemLevel
    CONFIG_ERROR(7000, CoreBundle.message("code.config.error")),
    /** Server inner error base codes. */
    @SystemLevel
    SERVER_INNER_ERROR(5000, CoreBundle.message("code.server.inner.error")),
    /** Service invoke error base codes */
    @SystemLevel
    SERVICE_INVOKE_ERROR(5001, CoreBundle.message("code.service.invoke.error")),
    /** Agent exception */
    @SystemLevel
    AGENT_INVOKE_EXCEPTION(5002, CoreBundle.message("code.client.invoke.error")),
    /** Agent enable exception base codes */
    @SystemLevel
    AGENT_DISABLE_EXCEPTION(5003, CoreBundle.message("code.agent.disable.error")),
    /** Rpc error base codes */
    @SystemLevel
    RPC_ERROR(5004, CoreBundle.message("code.rpc.invoke.error")),
    /** Gateway not fund instances error base codes */
    GATEWAY_NOT_FUND_INSTANCES_ERROR(5005, CoreBundle.message("code.gateway.instances.error")),
    /** 路由配置错误 */
    GATEWAY_ROUTER_ERROR(5006, CoreBundle.message("code.gateway.router.error")),
    /** Agent service not found error base codes */
    @BusinessLevel
    AGENT_SERVICE_NOT_FOUND_ERROR(5007, CoreBundle.message("code.agent.not.found.error")),
    /** 服务器繁忙,请稍后重试 */
    @SystemLevel
    SERVER_BUSY(9998, CoreBundle.message("code.server.busy")),
    /** 服务器异常,无法识别的异常,尽可能对通过判断减少未定义异常抛出 */
    @SystemLevel
    SERVER_ERROR(9999, CoreBundle.message("code.server.error"));

    /** 返回码 */
    private final Integer code;
    /** 返回消息 */
    private final String message;
}
