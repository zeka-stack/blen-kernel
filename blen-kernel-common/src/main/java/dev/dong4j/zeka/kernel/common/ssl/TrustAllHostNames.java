package dev.dong4j.zeka.kernel.common.ssl;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * <p>Description: 信任所有 host name </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:10
 * @since 1.0.0
 */
public class TrustAllHostNames implements HostnameVerifier {
    /**
     * The constant INSTANCE.
     */
    public static final TrustAllHostNames INSTANCE = new TrustAllHostNames();

    /**
     * Verify boolean
     *
     * @param s          s
     * @param sslSession ssl session
     * @return the boolean
     * @since 1.0.0
     */
    @Override
    public boolean verify(String s, SSLSession sslSession) {
        return true;
    }
}
