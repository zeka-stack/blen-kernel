package dev.dong4j.zeka.kernel.common.util;

import lombok.experimental.UtilityClass;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
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

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
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

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.3.0
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
    /** 连接超时 */
    private static final int CONNECTION_TIME_OUT = 45000;
    /** 数据超时 */
    private static final int READ_TIME_OUT = 75000;
    /** 连接等待 */
    private static final int CONNECTION_REQUEST_TIME_OUT = 5000;
    /** 编码 */
    private static final String CHARSET = Charsets.UTF_8_NAME;

    /**
     * Post for object t
     *
     * @param <T>          parameter
     * @param url          url
     * @param requestBody  request body
     * @param contentType  content type
     * @param responseType response type
     * @return the t
     * @throws Exception exception
     * @since 1.0.0
     */
    public static <T> T postForObject(String url,
                                      Object requestBody,
                                      MediaType contentType,
                                      Class<T> responseType) throws Exception {
        return postForObjectWithHeader(url, requestBody, contentType, responseType, null);
    }

    /**
     * Post for object with header t
     *
     * @param <T>          parameter
     * @param url          url
     * @param requestBody  request body
     * @param contentType  content type
     * @param responseType response type
     * @param headers      headers
     * @return the t
     * @throws Exception exception
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
     * Gets for object with header *
     *
     * @param <T>          parameter
     * @param url          url
     * @param responseType response type
     * @param headerName   header name
     * @param headerValue  header value
     * @return the for object with header
     * @throws Exception exception
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
     * Gets for object *
     *
     * @param <T>          parameter
     * @param url          url
     * @param responseType response type
     * @return the for object
     * @throws Exception exception
     * @since 1.0.0
     */
    public static <T> T getForObject(String url, Class<T> responseType) throws Exception {
        return getForObject(url, responseType, getDefaultHeader(MediaType.APPLICATION_JSON));
    }

    /**
     * Gets default header *
     *
     * @param contentType content type
     * @return the default header
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
     * Gets for object *
     *
     * @param <T>          parameter
     * @param url          url
     * @param responseType response type
     * @param httpHeaders  http headers
     * @return the for object
     * @throws Exception exception
     * @since 1.0.0
     */
    private static <T> T getForObject(String url, Class<T> responseType, HttpHeaders httpHeaders) throws Exception {
        try (CloseableHttpClient httpClient = acceptsUntrustedCertsHttpClient()) {

            RestTemplate restTemplate = getRestTemplate(httpClient);

            HttpEntity<Object> httpEntity = new HttpEntity<>(null, httpHeaders);

            URI uri = new URI(url);
            String responseStr = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class).getBody();

            return JsonUtils.parse(responseStr, responseType);
        } catch (Exception e) {
            throw new Exception("网络异常或请求错误.", e);
        }
    }

    /**
     * Gets rest template *
     *
     * @param httpClient http client
     * @return the rest template
     * @since 1.0.0
     */
    @NotNull
    private static RestTemplate getRestTemplate(CloseableHttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        clientHttpRequestFactory.setConnectTimeout(CONNECTION_TIME_OUT);
        clientHttpRequestFactory.setReadTimeout(READ_TIME_OUT);
        clientHttpRequestFactory.setConnectionRequestTimeout(CONNECTION_REQUEST_TIME_OUT);
        clientHttpRequestFactory.setBufferRequestBody(false);

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
     * 接受未信任的请求
     *
     * @return closeable http client
     * @throws KeyStoreException        key store exception
     * @throws NoSuchAlgorithmException no such algorithm exception
     * @throws KeyManagementException   key management exception
     * @since 1.0.0
     */
    public static CloseableHttpClient acceptsUntrustedCertsHttpClient()
        throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        SSLContext sslContext = HttpsUtils.getSslContext();

        httpClientBuilder.setSSLContext(sslContext);
        HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
            .register("http", PlainConnectionSocketFactory.getSocketFactory())
            .register("https", sslSocketFactory)
            .build();

        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connMgr.setMaxTotal(MAX_CONNECTION_TOTAL);
        connMgr.setDefaultMaxPerRoute(ROUTE_MAX_COUNT);

        httpClientBuilder.setConnectionManager(connMgr);
        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(RETRY_COUNT, true));
        httpClientBuilder.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());

        return httpClientBuilder.build();
    }

    /**
     * Gets for object with header *
     *
     * @param <T>          parameter
     * @param url          url
     * @param headers      headers
     * @param responseType response type
     * @return the for object with header
     * @throws Exception exception
     * @since 1.0.0
     */
    public static <T> T getForObjectWithHeader(String url, Map<String, String[]> headers, Class<T> responseType) throws Exception {
        HttpHeaders httpHeaders = getDefaultHeader(MediaType.APPLICATION_JSON);
        fillHeaders(httpHeaders, headers);
        return getForObject(url, responseType, httpHeaders);
    }

    /**
     * Fill headers *
     *
     * @param httpHeaders http headers
     * @param headers     headers
     * @since 1.0.0
     */
    private static void fillHeaders(HttpHeaders httpHeaders, @NotNull Map<String, String[]> headers) {
        headers.forEach((key, value1) -> Stream.of(value1).forEach(value -> httpHeaders.add(key, value)));
    }
}
