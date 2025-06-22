package dev.dong4j.zeka.kernel.common.dns;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.06.09 19:52
 * @since 1.5.0
 */
public class DnsCacheManipulatorException extends RuntimeException {
    /** serialVersionUID */
    private static final long serialVersionUID = -7843069964883320844L;

    /**
     * Dns cache manipulator exception
     *
     * @param message message
     * @since 1.5.0
     */
    public DnsCacheManipulatorException(String message) {
        super(message);
    }

    /**
     * Dns cache manipulator exception
     *
     * @param message message
     * @param cause   cause
     * @since 1.5.0
     */
    public DnsCacheManipulatorException(String message, Throwable cause) {
        super(message, cause);
    }
}
