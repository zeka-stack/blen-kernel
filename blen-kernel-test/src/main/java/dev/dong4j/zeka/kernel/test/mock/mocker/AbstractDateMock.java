package dev.dong4j.zeka.kernel.test.mock.mocker;

import dev.dong4j.zeka.kernel.test.mock.MockException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.26 21:37
 * @since 1.0.0
 */
class AbstractDateMock {

    /** Start time */
    Long startTime;
    /** End time */
    Long endTime;

    /**
     * Instantiates a new Abstract date mock.
     *
     * @param startTimePattern the start time pattern
     * @param endTimePattern   the end time pattern
     * @since 1.0.0
     */
    AbstractDateMock(String startTimePattern, String endTimePattern) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            startTime = format.parse(startTimePattern).getTime();
            endTime = format.parse(endTimePattern).getTime();
        } catch (ParseException e) {
            throw new MockException("时间格式设置错误,设置如下格式yyyy-MM-dd ", e);
        }
    }
}
