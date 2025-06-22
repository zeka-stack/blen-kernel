package dev.dong4j.zeka.kernel.test;

import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import dev.dong4j.zeka.kernel.common.constant.ConfigDefaultValue;
import dev.dong4j.zeka.kernel.common.util.StringPool;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.util.Properties;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2021.11.20 21:48
 * @since 2.1.0
 */
@UtilityClass
public class NacosTestmanUtils {

    /**
     * Gets instance *
     *
     * @param namespace   namespace
     * @param serviceName service name
     * @return the instance
     * @since 2.1.0
     */
    @SneakyThrows
    public String getInstance(String namespace, String serviceName) {
        return getInstance(ConfigDefaultValue.NACOS_SERVER, namespace, serviceName);
    }

    /**
     * Get instance
     *
     * @param serverAddr  server addr
     * @param namespace   namespace
     * @param serviceName service name
     * @return the string
     * @since 2.1.0
     */
    @SneakyThrows
    public String getInstance(String serverAddr, String namespace, String serviceName) {
        Properties properties = new Properties();
        properties.setProperty("serverAddr", serverAddr);
        properties.setProperty("namespace", namespace);

        NamingService naming = NamingFactory.createNamingService(properties);
        Instance instance = naming.selectOneHealthyInstance(serviceName);
        return instance.getIp() + StringPool.COLON + instance.getPort();
    }
}
