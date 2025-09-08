package dev.dong4j.zeka.kernel.common.mapstruct;

/**
 * <p>Description: 对象转换接口 </p>
 *
 * @param <S> source
 * @param <T> tageter
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 14:52
 * @since 1.0.0
 * @deprecated 请使用  {@link dev.dong4j.zeka.kernel.common.mapstruct.ServiceConverter}
 */
@Deprecated
public interface Converter<S, T> {

    /**
     * 正向转化 source -> tageter
     *
     * @param s the s
     * @return the t
     * @since 1.0.0
     */
    T to(S s);

    /**
     * 逆向转化 tageter -> source
     *
     * @param t the t
     * @return the s
     * @since 1.0.0
     */
    S from(T t);
}
