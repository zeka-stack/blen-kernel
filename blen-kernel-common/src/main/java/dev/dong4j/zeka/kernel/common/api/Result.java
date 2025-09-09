package dev.dong4j.zeka.kernel.common.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.dong4j.zeka.kernel.common.CoreBundle;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * <p>API 响应结果抽象基类.
 * <p>定义统一的 API 响应结构，保证所有请求返回的一致性和规范性.
 * <p>包含标准的响应字段：状态码、成功标识、数据内容、消息描述、跟踪标识等.
 * <p>支持 Jackson 多态序列化，解决抽象类反序列化问题.
 * <p>使用示例：
 * <pre>{@code
 * // 创建成功响应
 * Result<Data> result = R.succeed(data);
 *
 * // 反序列化
 * Result<Data> result = JsonUtils.parse(json, new TypeReference<Result<Data>>(){});
 * Result result = JsonUtils.parse(json, Result.class);
 * }</pre>
 * <p>由于 Jackson 在反序列化抽象类时存在多态问题，推荐使用具体的实现类进行反序列化：
 * <pre>{@code
 * Result result = JsonUtils.parse(json, R.class);
 * }</pre>
 *
 * @param <T> 响应数据的类型参数
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dongshijie@gmail.com"
 * @date 2020.02.06 17:44
 * @since 1.0.0
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@SuppressWarnings("all")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = R.class)
public abstract class Result<T> implements Serializable {

    /** 序列化版本号 */
    public static final long serialVersionUID = 1L;
    /** 响应状态码字段名 */
    public static final String CODE = "code";
    /** 成功标识字段名 */
    public static final String SUCCESS = "success";
    /** 数据内容字段名 */
    public static final String DATA = "data";
    /** 消息描述字段名 */
    public static final String MESSAGE = "message";
    /** 跟踪标识字段名 */
    public static final String TRACE_ID = "traceId";
    /** 扩展字段名 */
    public static final String EXTEND = "extend";
    /** 请求成功代码 */
    public static final Integer SUCCESS_CODE = 2000;
    /** 请求成功消息 */
    public static final String SUCCESS_MESSAGE = CoreBundle.message("success.message");
    /** 默认的失败代码 */
    public static final Integer FAILURE_CODE = 4000;
    /** 默认的失败消息 */
    public static final String FAILURE_MESSAGE = CoreBundle.message("failure.message");

    /** 请求响应状态码 */
    @Schema(description = "状态码", example = "2000")
    protected Integer code;
    /** 请求响应成功标识 */
    @Schema(description = "请求成功的状态", example = "true")
    protected boolean success;
    /** 请求响应的数据 */
    @Schema(description = "承载的数据")
    protected T data;
    /** 请求响应的消息 */
    @Schema(description = "返回的消息", example = "操作成功")
    protected String message = "";
    /** 请求响应的溯源标识 */
    @Schema(description = "溯源标识(业务无需关心此字段)", example = "1484501823002316800")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected String traceId;
    /** 扩展字段 */
    @Schema(description = "扩展字段(业务无需关心此字段, 非生产环境的异常信息会写入到此字段)", example = "N/A")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected Object extend;

    /**
     * <p>受保护的构造方法.
     * <p>初始化 Result 实例的所有字段，并根据状态码自动设置成功标识.
     *
     * @param code    响应状态码
     * @param message 响应消息描述
     * @param data    响应数据内容
     * @param traceId 跟踪标识，用于请求链路追踪
     * @since 1.0.0
     */
    @Contract(pure = true)
    protected Result(@NotNull Integer code, String message, T data, String traceId) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.success = (SUCCESS_CODE.equals(code));
        this.traceId = traceId;
    }

    /**
     * <p>判断请求是否成功 (实例方法).
     * <p>基于当前实例的状态码和成功标识进行判断.
     *
     * @return {@code true} 请求成功；{@code false} 请求失败
     * @since 1.0.0
     */
    @JsonIgnore
    public boolean isOk() {
        return isOk(this);
    }

    /**
     * <p>判断请求是否成功 (静态方法).
     * <p>检查 Result 对象的状态码是否为成功码且成功标识为 true.
     *
     * @param result 待检查的 Result 对象
     * @return {@code true} 请求成功；{@code false} 请求失败或对象为 null
     * @since 1.0.0
     */
    @Contract("null -> false")
    public static boolean isOk(@Nullable Result<?> result) {
        return result != null && (SUCCESS_CODE.equals(result.getCode()) && result.isSuccess());
    }

    /**
     * <p>判断请求是否失败 (实例方法).
     * <p>基于当前实例进行失败判断，与 isOk() 结果相反.
     *
     * @return {@code true} 请求失败；{@code false} 请求成功
     * @since 1.0.0
     */
    @JsonIgnore
    public boolean isFail() {
        return isFail(this);
    }

    /**
     * <p>判断请求是否失败 (静态方法).
     * <p>通过反向调用 isOk() 方法进行判断，null 对象视为失败.
     *
     * @param result 待检查的 Result 对象
     * @return {@code true} 请求失败或对象为 null；{@code false} 请求成功
     * @since 1.0.0
     */
    @Contract("null -> true")
    public static boolean isFail(@Nullable Result<?> result) {
        return !isOk(result);
    }

}
