package dev.dong4j.zeka.kernel.common.event;

/**
 * <p>Description: 异常事件 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.08.30 10:18
 * @since 1.6.0
 */
public class ExceptionEvent extends BaseEvent<Throwable> {
    private static final long serialVersionUID = 4718546244082863537L;

    /**
     * Instantiates a new Base event.
     *
     * @param source the source
     * @since 1.0.0
     */
    public ExceptionEvent(Throwable source) {
        super(source);
    }
}
