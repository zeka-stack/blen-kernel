package dev.dong4j.zeka.kernel.common.util;

import cn.hutool.core.lang.Snowflake;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>Description: 雪花算法生成全局唯一 id </p>
 * todo-dong4j : (2020年04月18日 9:22 下午) [待优化]
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dongshijie@gmail.com"
 * @date 2020.04.18 21:14
 * @since 1.0.0
 */
@UtilityClass
public class SnowflakeBuilder {

    /**
     * 活动机器编号
     *
     * @return work id
     * @since 1.0.0
     */
    private static Long getWorkId() {
        try {
            String hostAddress = Inet4Address.getLocalHost().getHostAddress();
            int[] ints = StringUtils.toCodePoints(hostAddress);
            int sums = 0;
            for (int b : ints) {
                sums += b;
            }
            return (long) (sums % 32);
        } catch (UnknownHostException e) {
            // 如果获取失败, 则使用随机数备用
            return RandomUtils.nextLong(0, 31);
        }
    }

    /**
     * 区域编号
     *
     * @return data center id
     * @since 1.0.0
     */
    private static Long getDataCenterId() {
        String hostName = SystemUtils.getHostName();
        if (StringUtils.isBlank(hostName)) {
            hostName = NetUtils.getLocalHost();
        }
        int[] ints = StringUtils.toCodePoints(hostName);
        int sums = 0;
        for (int i : ints) {
            sums += i;
        }
        return (long) (sums % 32);

    }

    /**
     * Builder long
     *
     * @return the long
     * @since 1.0.0
     */
    public static long builder() {
        return new Snowflake(getWorkId(), getDataCenterId()).nextId();
    }

    /**
     * Builder long
     *
     * @param workerId worker id
     * @param regionId region id
     * @return the long
     * @since 1.0.0
     */
    public static long builder(long workerId, long regionId) {
        return new Snowflake(workerId, regionId).nextId();
    }
}
