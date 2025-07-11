package dev.dong4j.zeka.kernel.common.util;

import cn.hutool.core.exceptions.ExceptionUtil;
import dev.dong4j.zeka.kernel.common.exception.BasicException;
import dev.dong4j.zeka.kernel.common.support.FastStringWriter;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * <p>Description: 异常处理工具类 </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.11.21 09:59
 * @since 1.0.0
 */
@UtilityClass
public class Exceptions extends ExceptionUtil {

    /**
     * 将 CheckedException 转换为 UncheckedException.
     *
     * @param e Throwable
     * @return {RuntimeException}
     * @since 1.0.0
     */
    @NotNull
    @Contract("null -> new")
    public static RuntimeException unchecked(Throwable e) {
        return unchecked(e.getMessage(), e);
    }

    /**
     * 将受检异常转换为 unchecked 异常
     *
     * @param message message
     * @param e       e
     * @return the runtime exception
     * @since 1.0.0
     */
    @NotNull
    public static RuntimeException unchecked(String message, Throwable e) {
        RuntimeException runtimeException;
        if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
            || e instanceof NoSuchMethodException) {
            runtimeException = new IllegalArgumentException(e);
        } else if (e instanceof InvocationTargetException) {
            runtimeException = new RuntimeException(((InvocationTargetException) e).getTargetException());
        } else if (e instanceof BasicException) {
            runtimeException = (BasicException) e;
        } else if (e instanceof RuntimeException) {
            runtimeException = (RuntimeException) e;
        } else {
            runtimeException = new RuntimeException(e);
        }
        return runtimeException;
    }

    /**
     * 代理异常解包
     *
     * @param wrapped 包装过得异常
     * @return 解包后的异常 throwable
     * @since 1.0.0
     */
    public static Throwable unwrap(Throwable wrapped) {
        Throwable unwrapped = wrapped;
        while (true) {
            if (unwrapped instanceof InvocationTargetException) {
                unwrapped = ((InvocationTargetException) unwrapped).getTargetException();
            } else if (unwrapped instanceof UndeclaredThrowableException) {
                unwrapped = ((UndeclaredThrowableException) unwrapped).getUndeclaredThrowable();
            } else {
                return unwrapped;
            }
        }
    }

    /**
     * 将 ErrorStack 转化为 String.
     *
     * @param ex Throwable
     * @return {String}
     * @since 1.0.0
     */
    @SuppressWarnings("all")
    public static String getStackTraceAsString(@NotNull Throwable ex) {
        FastStringWriter stringWriter = new FastStringWriter();
        ex.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    /**
     * 不采用 RuntimeException 包装,直接抛出,使异常更加精准
     *
     * @param <T>       泛型标记
     * @param throwable Throwable
     * @return Throwable t
     * @throws T 泛型
     * @since 1.0.0
     */
    @Contract("_ -> fail")
    @SuppressWarnings("unchecked")
    private static <T extends Throwable> T runtime(Throwable throwable) throws T {
        throw (T) throwable;
    }

}
