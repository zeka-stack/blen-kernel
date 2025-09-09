package dev.dong4j.zeka.kernel.common.util;

import java.net.URI;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import javax.net.ssl.SSLContext;
import lombok.experimental.UtilityClass;
import org.apache.hc.client5.http.ConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.DefaultClientTlsStrategy;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.util.TimeValue;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

/**
 * <p>Description: HTTP客户端工具类，基于Apache HttpClient实现，提供HTTP请求相关的工具方法</p>
 * <p>
 * 主要功能：
 * <ul>
 *     <li>HTTP GET/POST请求发送</li>
 *     <li>HTTPS请求支持</li>
 *     <li>连接池管理</li>
 *     <li>请求重试机制</li>
 *     <li>自定义请求头</li>
 *     <li>JSON数据处理</li>
 * </ul>
 * </p>
 * <p>
 * 使用示例：
 * <pre>
 * // 发送GET请求
 * String result = HttpClientUtils.getForObject("https://api.example.com/data", String.class);
 *
 * // 发送POST请求
 * Map<String, Object> requestData = new HashMap<>();
 * requestData.put("name", "test");
 * requestData.put("value", 123);
 * MyResponse response = HttpClientUtils.postForObject("https://api.example.com/data", requestData, MediaType.APPLICATION_JSON, MyResponse.class);
 *
 * // 发送带自定义头部的GET请求
 * Map<String, String[]> headers = new HashMap<>();
 * headers.put("Authorization", new String[]{"Bearer token"});
 * MyResponse response = HttpClientUtils.getForObjectWithHeader("https://api.example.com/data", headers, MyResponse.class);
 *
 * // 发送带自定义头部的POST请求
 * MyResponse response = HttpClientUtils.postForObjectWithHeader("https://api.example.com/data", requestData, MediaType.APPLICATION_JSON, MyResponse.class, headers);
 * </pre>
 * </p>
 * <p>
 * 技术特性：
 * <ul>
 *     <li>基于Apache HttpClient 5.x实现</li>
 *     <li>支持连接池管理</li>
 *     <li>内置HTTPS证书信任机制</li>
 *     <li>支持请求重试策略</li>
 *     <li>集成Spring RestTemplate</li>
 *     <li>支持自定义请求头</li>
 *     <li>自动处理JSON序列化/反序列化</li>
 * </ul>
 * </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.03.04 14:02
 * @since 1.0.0
 */
@UtilityClass
public class HttpClientUtils {

    /** 最大连接数 */
    private static final int MAX_CONNECTION_TOTAL = 300;
    /** 路由并发数 */
    private static final int ROUTE_MAX_COUNT = 200;
    /** 重试次数 */
    private static final int RETRY_COUNT = 3;
    /** 连接超时时间(毫秒) */
    private static final int CONNECTION_TIME_OUT = 45000;
    /** 数据读取超时时间(毫秒) */
    private static final int READ_TIME_OUT = 75000;
    /** 连接请求超时时间(毫秒) */
    private static final int CONNECTION_REQUEST_TIME_OUT = 5000;
    /** 字符编码 */
    private static final String CHARSET = Charsets.UTF_8_NAME;

    /**
     * 发送POST请求并返回对象
     *
     * @param <T>          返回对象类型
     * @param url          请求URL
     * @param requestBody  请求体对象
     * @param contentType  内容类型
     * @param responseType 响应对象类型
     * @return 响应对象
     * @throws Exception 网络异常或请求错误
     * @since 1.0.0
     */
    public static <T> T postForObject(String url,
                                      Object requestBody,
                                      MediaType contentType,
                                      Class<T> responseType) throws Exception {
        return postForObjectWithHeader(url, requestBody, contentType, responseType, null);
    }

    /**
     * 发送带自定义头部的POST请求并返回对象
     *
     * @param <T>          返回对象类型
     * @param url          请求URL
     * @param requestBody  请求体对象
     * @param contentType  内容类型
     * @param responseType 响应对象类型
     * @param headers      自定义请求头
     * @return 响应对象
     * @throws Exception 网络异常或请求错误
     * @since 1.0.0
     */
    public static <T> T postForObjectWithHeader(String url,
                                                Object requestBody,
                                                MediaType contentType,
                                                Class<T> responseType,
                                                Map<String, String[]> headers) throws Exception {
        try (CloseableHttpClient httpClient = acceptsUntrustedCertsHttpClient()) {

            RestTemplate restTemplate = getRestTemplate(httpClient);

            // headers
            HttpHeaders httpHeaders = getDefaultHeader(contentType);
            if (!Objects.isNull(headers)) {
                fillHeaders(httpHeaders, headers);
            }

            HttpEntity<Object> httpEntity = new HttpEntity<>(requestBody, httpHeaders);
            return restTemplate.postForObject(url, httpEntity, responseType);
        } catch (Exception e) {
            throw new Exception("网络异常或请求错误.", e);
        }
    }

    /**
     * 发送带指定头部的GET请求并返回对象
     *
     * @param <T>          返回对象类型
     * @param url          请求URL
     * @param responseType 响应对象类型
     * @param headerName   头部名称
     * @param headerValue  头部值
     * @return 响应对象
     * @throws Exception 网络异常或请求错误
     * @since 1.0.0
     */
    public static <T> T getForObjectWithHeader(String url,
                                               Class<T> responseType,
                                               String headerName,
                                               String headerValue) throws Exception {
        HttpHeaders httpHeaders = getDefaultHeader(MediaType.APPLICATION_JSON);
        httpHeaders.add(headerName, headerValue);
        return getForObject(url, responseType, httpHeaders);
    }

    /**
     * 发送GET请求并返回对象
     *
     * @param <T>          返回对象类型
     * @param url          请求URL
     * @param responseType 响应对象类型
     * @return 响应对象
     * @throws Exception 网络异常或请求错误
     * @since 1.0.0
     */
    public static <T> T getForObject(String url, Class<T> responseType) throws Exception {
        return getForObject(url, responseType, getDefaultHeader(MediaType.APPLICATION_JSON));
    }

    /**
     * 获取默认请求头
     *
     * @param contentType 内容类型
     * @return 请求头对象
     * @since 1.0.0
     */
    private static HttpHeaders getDefaultHeader(MediaType contentType) {
        HttpHeaders headers = new HttpHeaders();
        List<MediaType> acceptableMediaTypes = new ArrayList<>();
        acceptableMediaTypes.add(MediaType.ALL);
        headers.setAccept(acceptableMediaTypes);
        headers.setContentType(contentType);
        headers.add("Connection", "Keep-Alive");
        headers.add("Content-Encoding", "gzip");
        headers.add("Vary", "Accept-Encoding");
        headers.add("Transfer-Encoding", "chunked");
        return headers;
    }

    /**
     * 发送GET请求并返回对象
     *
     * @param <T>          返回对象类型
     * @param url          请求URL
     * @param responseType 响应对象类型
     * @param httpHeaders  请求头
     * @return 响应对象
     * @throws Exception 网络异常或请求错误
     * @since 1.0.0
     */
    private static <T> T getForObject(String url, Class<T> responseType, HttpHeaders httpHeaders) throws Exception {
        try (CloseableHttpClient httpClient = acceptsUntrustedCertsHttpClient()) {

            RestTemplate restTemplate = getRestTemplate(httpClient);

            HttpEntity<Object> httpEntity = new HttpEntity<>(null, httpHeaders);

            URI uri = new URI(url);
            String responseStr = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class).getBody();

            return Jsons.parse(responseStr, responseType);
        } catch (Exception e) {
            throw new Exception("网络异常或请求错误.", e);
        }
    }

    /**
     * 获取RestTemplate对象
     *
     * @param httpClient HttpClient对象
     * @return RestTemplate对象
     * @since 1.0.0
     */
    @NotNull
    private static RestTemplate getRestTemplate(CloseableHttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        clientHttpRequestFactory.setConnectTimeout(CONNECTION_TIME_OUT);
        clientHttpRequestFactory.setReadTimeout(READ_TIME_OUT);
        clientHttpRequestFactory.setConnectionRequestTimeout(CONNECTION_REQUEST_TIME_OUT);

        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());

        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        messageConverters.forEach(m -> {
            if (m instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) m).setDefaultCharset(Charset.forName(CHARSET));
            }
        });
        restTemplate.setMessageConverters(messageConverters);
        return restTemplate;
    }

    /**
     * 创建接受未信任证书的HTTP客户端
     *
     * @return 可关闭的HTTP客户端
     * @throws KeyStoreException        密钥库异常
     * @throws NoSuchAlgorithmException 无此算法异常
     * @throws KeyManagementException   密钥管理异常
     * @since 1.0.0
     */
    public static CloseableHttpClient acceptsUntrustedCertsHttpClient()
        throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        // 创建信任所有证书的 SSLContext
        SSLContext sslContext = SSLContextBuilder.create()
            .loadTrustMaterial(TrustAllStrategy.INSTANCE)
            .build();

        // 构建支持 TLS 的策略（替代 SSLConnectionSocketFactory）
        var tlsStrategy = new DefaultClientTlsStrategy(sslContext, NoopHostnameVerifier.INSTANCE);

        // 连接管理器
        var connManager = PoolingHttpClientConnectionManagerBuilder.create()
            .setTlsSocketStrategy(tlsStrategy)
            .setMaxConnTotal(MAX_CONNECTION_TOTAL)
            .setMaxConnPerRoute(ROUTE_MAX_COUNT)
            .build();

        // KeepAlive 策略（默认可用）
        ConnectionKeepAliveStrategy keepAliveStrategy = (response, context) -> TimeValue.ofDays(30_000); // 30 秒

        // 构建 HttpClient
        return HttpClients.custom()
            .setConnectionManager(connManager)
            .setRetryStrategy(new DefaultHttpRequestRetryStrategy(RETRY_COUNT, TimeValue.ofSeconds(3)))
            .setKeepAliveStrategy(keepAliveStrategy)
            .build();
    }

    /**
     * 发送带自定义头部的GET请求并返回对象
     *
     * @param <T>          返回对象类型
     * @param url          请求URL
     * @param headers      自定义请求头
     * @param responseType 响应对象类型
     * @return 响应对象
     * @throws Exception 网络异常或请求错误
     * @since 1.0.0
     */
    public static <T> T getForObjectWithHeader(String url, Map<String, String[]> headers, Class<T> responseType) throws Exception {
        HttpHeaders httpHeaders = getDefaultHeader(MediaType.APPLICATION_JSON);
        fillHeaders(httpHeaders, headers);
        return getForObject(url, responseType, httpHeaders);
    }

    /**
     * 填充请求头
     *
     * @param httpHeaders 请求头对象
     * @param headers     自定义请求头映射
     * @since 1.0.0
     */
    private static void fillHeaders(HttpHeaders httpHeaders, @NotNull Map<String, String[]> headers) {
        headers.forEach((key, value1) -> Stream.of(value1).forEach(value -> httpHeaders.add(key, value)));
    }
}
