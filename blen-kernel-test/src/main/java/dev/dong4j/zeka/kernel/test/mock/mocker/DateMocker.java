package dev.dong4j.zeka.kernel.test.mock.mocker;

import dev.dong4j.zeka.kernel.common.util.RandomUtils;
import dev.dong4j.zeka.kernel.test.mock.MockConfig;
import dev.dong4j.zeka.kernel.test.mock.Mocker;

import java.util.Date;

/**
 * <p>Description: Date对象模拟器 </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:25
 * @since 1.0.0
 */
public class DateMocker extends AbstractDateMock implements Mocker<Date> {

    /**
     * Instantiates a new Date mocker.
     *
     * @param startTimePattern the start time pattern
     * @param endTime          the end time
     * @since 1.0.0
     */
    public DateMocker(String startTimePattern, String endTime) {
        super(startTimePattern, endTime);
    }

    /**
     * Mocks date.
     *
     * @param mockConfig the mock config
     * @return the date
     * @since 1.0.0
     */
    @Override
    public Date mock(MockConfig mockConfig) {
        return new Date(RandomUtils.nextLong(startTime, endTime));
    }

}
