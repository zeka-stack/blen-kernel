package dev.dong4j.zeka.kernel.common.util;

import dev.dong4j.zeka.kernel.common.ssl.DisableValidationTrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dongshijie@gmail.com"
 * @date 2020.01.27 17:22
 * @since 1.0.0
 */
@UtilityClass
public class HttpsUtils {

    /**
     * 返回忽略 https 证书的 SSLContext
     *
     * @return the ssl context
     * @throws NoSuchAlgorithmException no such algorithm exception
     * @throws KeyManagementException   key management exception
     * @since 1.0.0
     */
    public static @NotNull SSLContext getSslContext() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{DisableValidationTrustManager.INSTANCE}, null);
        return sslContext;
    }
}
