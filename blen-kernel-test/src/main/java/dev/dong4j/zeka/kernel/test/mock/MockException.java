package dev.dong4j.zeka.kernel.test.mock;

import java.io.Serial;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:22
 * @since 1.0.0
 */
public class MockException extends RuntimeException {

    /** serialVersionUID */
    @Serial
    private static final long serialVersionUID = -2061772849386866982L;

    /**
     * Instantiates a new Mock exception.
     *
     * @param message the message
     * @since 1.0.0
     */
    public MockException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Mock exception.
     *
     * @param message the message
     * @param cause   the cause
     * @since 1.0.0
     */
    public MockException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Mock exception.
     *
     * @param cause the cause
     * @since 1.0.0
     */
    public MockException(Throwable cause) {
        super(cause);
    }

}
