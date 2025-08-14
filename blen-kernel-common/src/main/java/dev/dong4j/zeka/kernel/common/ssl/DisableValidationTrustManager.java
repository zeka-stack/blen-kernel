package dev.dong4j.zeka.kernel.common.ssl;

import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

/**
 * <p>Description: 不进行证书校验 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 17:31
 * @since 1.0.0
 */
@SuppressWarnings("java:S4830")
public class DisableValidationTrustManager implements X509TrustManager {

    /**
     * The constant INSTANCE.
     */
    public static final X509TrustManager INSTANCE = new DisableValidationTrustManager();

    /**
     * Check client trusted *
     *
     * @param x509Certificates x 509 certificates
     * @param s                s
     * @since 1.0.0
     */
    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
        // nothing do to
    }

    /**
     * Check server trusted *
     *
     * @param x509Certificates x 509 certificates
     * @param s                s
     * @since 1.0.0
     */
    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
        // nothing do to
    }

    /**
     * Get accepted issuers x 509 certificate [ ]
     *
     * @return the x 509 certificate [ ]
     * @since 1.0.0
     */
    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
