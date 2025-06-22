package dev.dong4j.zeka.kernel.common.exception;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.11.18 00:26
 * @since 1.7.0
 */
public class AssertionFailedException extends ServiceInternalException {
    /** serialVersionUID */
    private static final long serialVersionUID = 7198353389256089258L;
    /** DEFAULT_MESSAGE */
    private static final String DEFAULT_MESSAGE = "服务内部错误: ";

    /**
     * Basic exception
     *
     * @since 1.7.0
     */
    public AssertionFailedException() {
        super();
    }

    /**
     * Base exception
     *
     * @param describe describe
     * @since 1.7.0
     */
    public AssertionFailedException(String describe) {
        super(DEFAULT_MESSAGE + describe);
    }
}
