package dev.dong4j.zeka.kernel.common.exception;

import dev.dong4j.zeka.kernel.common.api.BaseCodes;
import dev.dong4j.zeka.kernel.common.api.IResultCode;
import dev.dong4j.zeka.kernel.common.support.StrFormatter;
import dev.dong4j.zeka.kernel.common.util.ResultCodeUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Description: 基础异常类,所有自定义异常类都需要继承本类 </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.11.26 23:45
 * @since 1.0.0
 */
@Slf4j
@Getter
public class BaseException extends BasicException {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 返回码 */
    protected IResultCode resultCode;
    /** 异常消息参数 */
    protected Object[] args;

    /**
     * Instantiates a new Base exception.
     *
     * @since 1.0.0
     */
    public BaseException() {
        super(BaseCodes.FAILURE.getMessage());
        this.resultCode = BaseCodes.FAILURE;
    }

    /**
     * Instantiates a new Base exception.
     *
     * @param msg the msg
     * @since 1.0.0
     */
    public BaseException(String msg) {
        super(msg);
        this.resultCode = BaseCodes.FAILURE;
    }

    /**
     * Instantiates a new Base exception.
     *
     * @param code the code
     * @param msg  the msg
     * @since 1.0.0
     */
    public BaseException(int code, String msg) {
        super(msg);
        this.resultCode = new IResultCode() {
            private static final long serialVersionUID = 2590640370242410124L;

            @Override
            public String getMessage() {
                return msg;
            }

            @Override
            public Integer getCode() {
                return code;
            }
        };
    }

    public BaseException(String code, String msg) {
        super(code, msg);
        this.resultCode = new IResultCode() {
            private static final long serialVersionUID = 2590640370242410124L;

            /**
             * Gets message *
             *
             * @return the message
             * @since 1.6.0
             */
            @Override
            public String getMessage() {
                return msg;
            }

            @Override
            public Integer getCode() {
                if (ResultCodeUtils.isSpecialCodeFormat(code)) {
                    return ResultCodeUtils.convert(code);
                }
                return 5000;
            }
        };
    }

    /**
     * msg 占位符替换
     *
     * @param msg  msg
     * @param args args
     * @since 1.0.0
     */
    public BaseException(String msg, Object... args) {
        super(msg, args);
        this.resultCode = BaseCodes.FAILURE;
    }

    /**
     * Base exception
     *
     * @param cause cause
     * @since 1.0.0
     */
    public BaseException(Throwable cause) {
        super(cause);
        this.resultCode = BaseCodes.FAILURE;
    }

    /**
     * Base exception
     *
     * @param msg   msg
     * @param cause cause
     * @since 1.0.0
     */
    public BaseException(String msg, Throwable cause) {
        super(msg, cause);
        this.resultCode = BaseCodes.FAILURE;
    }

    /**
     * Instantiates a new Base exception.
     *
     * @param resultCode the response enum
     * @since 1.0.0
     */
    public BaseException(@NotNull IResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    /**
     * Base exception
     *
     * @param resultCode result code
     * @param cause      cause
     * @since 1.0.0
     */
    public BaseException(@NotNull IResultCode resultCode, Throwable cause) {
        super(resultCode.getMessage(), cause);
        this.resultCode = resultCode;
    }

    /**
     * Instantiates a new Base exception.
     *
     * @param code  the code
     * @param msg   the msg
     * @param cause cause
     * @since 1.0.0
     */
    public BaseException(int code, String msg, Throwable cause) {
        super(msg, cause);
        this.resultCode = new IResultCode() {
            private static final long serialVersionUID = 2590640370242410124L;

            @Override
            public String getMessage() {
                return msg;
            }

            @Override
            public Integer getCode() {
                return code;
            }

        };
    }

    /**
     * Instantiates a new Base exception.
     *
     * @param resultCode the response enum
     * @param args       the args
     * @param msg        msg        替换占位符后的消息
     * @since 1.0.0
     */
    public BaseException(IResultCode resultCode, Object[] args, String msg) {
        super(StrFormatter.mergeFormat(msg, args));
        this.resultCode = resultCode;
        this.args = args;
    }

    /**
     * Instantiates a new Base exception.
     *
     * @param resultCode the response enum
     * @param args       the args
     * @param msg        msg
     * @param cause      the cause
     * @since 1.0.0
     */
    public BaseException(IResultCode resultCode, Object[] args, String msg, Throwable cause) {
        super(StrFormatter.mergeFormat(msg, args), cause);
        this.resultCode = resultCode;
        this.args = args;
    }

    /**
     * Get code
     *
     * @return the string
     * @since 1.6.0
     */
    @Override
    public String getCode() {
        return ResultCodeUtils.generateCode(this.resultCode);
    }

}
