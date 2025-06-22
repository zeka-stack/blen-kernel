package dev.dong4j.zeka.kernel.common.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>Description: 事件枚举 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.11.20 05:32
 * @since 3.0.0
 */
@Getter
@AllArgsConstructor
public enum EventEnum {
    /** Event log event enum */
    EVENT_LOG("log"),
    /** Event request event enum */
    EVENT_REQUEST("request");

    /** Name */
    private final String name;

}
