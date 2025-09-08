package dev.dong4j.zeka.kernel.common.api;

import dev.dong4j.zeka.kernel.common.CoreBundle;
import dev.dong4j.zeka.kernel.common.assertion.LowestExceptionAssert;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用返回结果码枚举，定义系统中常用的状态码和消息
 * <p>
 * 该枚举类实现了IResultCode接口和LowestExceptionAssert断言接口
 * 提供了系统中最常用的返回结果码定义，包括成功、失败、各种错误类型
 * <p>
 * 主要功能：
 * - 提供统一的成功和失败状态码
 * - 定义常见的业务错误类型（参数校验、数据错误等）
 * - 支持服务端错误的分类定义（内部错误、调用错误等）
 * - 集成国际化支持，消息内容支持多语言
 * - 提供断言机制，可以直接抛出对应的业务异常
 * <p>
 * 状态码规则：
 * - 2000: 成功
 * - 4xxx: 客户端错误（参数、数据、操作错误）
 * - 5xxx: 服务端错误（内部、调用、RPC错误）
 * - 7xxx: 配置错误
 * - 9xxx: 系统级错误（服务器繁忙、未知异常）
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.02 11:36
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum BaseCodes implements LowestExceptionAssert {
    /** 请求成功状态码，表示操作成功完成 */
    SUCCESS(Result.SUCCESS_CODE, Result.SUCCESS_MESSAGE),
    /** 默认空数据响应，表示请求成功但没有返回数据 */
    DEFAULT_NULL_DATA(2222, CoreBundle.message("code.default.null.data")),
    /** 默认失败响应，表示操作失败或出现错误 */
    FAILURE(Result.FAILURE_CODE, Result.FAILURE_MESSAGE),
    /** 参数校验失败，请求参数不符合预期或缺少必要参数 */
    PARAM_VERIFY_ERROR(4100, CoreBundle.message("code.param.verify.error")),
    /** 数据不存在或数据错误，请求的数据未找到或数据异常 */
    DATA_ERROR(4101, CoreBundle.message("code.data.error")),
    /** 操作失败，业务操作未成功执行或遇到错误 */
    OPTION_FAILURE(4102, CoreBundle.message("code.option.failure")),
    /** 配置错误，系统配置不正确或缺少必要的配置项 */
    CONFIG_ERROR(7000, CoreBundle.message("code.config.error")),
    /** 服务器内部错误，服务端处理过程中出现异常 */
    SERVER_INNER_ERROR(5000, CoreBundle.message("code.server.inner.error")),
    /** 服务调用错误，对外部服务的调用失败或超时 */
    SERVICE_INVOKE_ERROR(5001, CoreBundle.message("code.service.invoke.error")),
    /** 代理调用异常，客户端代理调用过程中出现异常 */
    AGENT_INVOKE_EXCEPTION(5002, CoreBundle.message("code.client.invoke.error")),
    /** 代理服务禁用异常，代理服务当前不可用或被禁用 */
    AGENT_DISABLE_EXCEPTION(5003, CoreBundle.message("code.agent.disable.error")),
    /** RPC调用错误，远程过程调用失败或出现异常 */
    RPC_ERROR(5004, CoreBundle.message("code.rpc.invoke.error")),
    /** 网关找不到实例错误，网关路由时找不到可用的服务实例 */
    GATEWAY_NOT_FUND_INSTANCES_ERROR(5005, CoreBundle.message("code.gateway.instances.error")),
    /** 网关路由配置错误，网关路由规则配置不正确或缺失 */
    GATEWAY_ROUTER_ERROR(5006, CoreBundle.message("code.gateway.router.error")),
    /** 代理服务未找到错误，指定的代理服务不存在或未注册 */
    AGENT_SERVICE_NOT_FOUND_ERROR(5007, CoreBundle.message("code.agent.not.found.error")),
    /** 服务器繁忙，当前服务器负载过高，请稍后重试 */
    SERVER_BUSY(9998, CoreBundle.message("code.server.busy")),
    /** 服务器未知异常，服务器出现无法识别的异常情况 */
    SERVER_ERROR(9999, CoreBundle.message("code.server.error"));

    /** HTTP状态码，整数类型 */
    private final Integer code;
    /** 返回消息内容，支持国际化 */
    private final String message;
}
