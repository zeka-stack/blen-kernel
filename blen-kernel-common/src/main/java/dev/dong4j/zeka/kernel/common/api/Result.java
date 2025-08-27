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
 * <p>Description: 请求响应返回结构封装, 所有请求都要求返回此类.
 * {@code
 * Result<Data> result = R.success(data);
 * <p>
 * 反序列化:
 * Result<Data> result = JsonUtils.parse(json, new TypeReference<Result<Data>>(){});
 * Result result = JsonUtils.parse(json, Result.class);
 * }*
 * <p>
 * 由于 jackson 在反序列化抽象类时存在多态问题, 我们使用 {@link Result#TYPE_NAME} 来标识 json 需要被反序列化的 class.
 * 比较推荐的反序列化方式是根据不同的框架来进行反序列化, 避免使用 {@link Result}:
 * {@code
 * v8: Result result = JsonUtils.parse(json, R.class);
 * }*
 * </p>
 *
 * @param <T> parameter
 * @author dong4j
 * @version 1.2.4
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

    /** serialVersionUID */
    public static final long serialVersionUID = 1L;
    /** CODE */
    public static final String CODE = "code";
    /** SUCCESS */
    public static final String SUCCESS = "success";
    /** DATA */
    public static final String DATA = "data";
    /** MESSAGE */
    public static final String MESSAGE = "message";
    /** TRACE_ID */
    public static final String TRACE_ID = "traceId";
    /** EXTEND */
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
     * Result
     *
     * @param code    code
     * @param message message
     * @param data    data
     * @param traceId trace id
     * @since 2024.1.1
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
     * Is ok boolean
     *
     * @return the boolean
     * @since 2024.1.1
     */
    @JsonIgnore
    public boolean isOk() {
        return isOk(this);
    }

    /**
     * 请求是否成功
     *
     * @param result result
     * @return the boolean
     * @since 2024.1.1
     */
    @Contract("null -> false")
    public static boolean isOk(@Nullable Result<?> result) {
        return result != null && (SUCCESS_CODE.equals(result.getCode()) && result.isSuccess());
    }

    /**
     * Is fail boolean
     *
     * @return the boolean
     * @since 2024.1.1
     */
    @JsonIgnore
    public boolean isFail() {
        return isFail(this);
    }

    /**
     * 请求是否失败
     *
     * @param result result
     * @return the boolean
     * @since 2024.1.1
     */
    @Contract("null -> true")
    public static boolean isFail(@Nullable Result<?> result) {
        return !isOk(result);
    }

}
