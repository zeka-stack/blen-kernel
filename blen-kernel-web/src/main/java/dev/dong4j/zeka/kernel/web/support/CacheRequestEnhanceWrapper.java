package dev.dong4j.zeka.kernel.web.support;

import com.google.common.collect.Maps;
import dev.dong4j.zeka.kernel.common.util.Charsets;
import dev.dong4j.zeka.kernel.common.util.IoUtils;
import dev.dong4j.zeka.kernel.common.util.WebUtils;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodArgumentResolver;
import org.springframework.web.util.ContentCachingRequestWrapper;

/**
 * <p>Description: request 包装类, 用于自定义 header, body, params</p>
 * 从 body 中获取数据分 2 种情况
 * 1. content-type: application/x-www-form-urlencoded
 * 2. content-type: application/json
 * 将分别使用 {@link CacheRequestEnhanceWrapper#getParameter(String)}
 * 和 {@link CacheRequestEnhanceWrapper#getInputStream()} 获取请求参数, 都会封装到实体或者字段中
 * 具体看 controller 使用哪种方式接受参数
 * getParameter() --> @RequestParam
 * 1. url 参数
 * 2. form-data
 * 3. x-www-form-urlencoded
 * getInputStream() --> @RequestBody
 * 1. json
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.11.27 22:31
 * @since 1.0.0
 */
@Slf4j
@EqualsAndHashCode(callSuper = true)
public class CacheRequestEnhanceWrapper extends HttpServletRequestWrapper {
    /** 保存 io stream */
    @Getter
    @Setter
    protected byte[] body;
    /** Body string */
    @Getter
    protected String bodyString;
    /** Content type */
    protected String contentType;
    /** Header map */
    protected Map<String, String> headerMap = Maps.newHashMapWithExpectedSize(16);
    /** Params */
    protected Map<String, String[]> params = Maps.newHashMapWithExpectedSize(16);
    /** Request */
    @Getter
    protected ContentCachingRequestWrapper cachingRequestWrapper;

    /**
     * Instantiates a new Request wrapper.
     *
     * @param request the request
     * @since 1.0.0
     */
    public CacheRequestEnhanceWrapper(ContentCachingRequestWrapper request) {
        super(request);
        try {
            request.setCharacterEncoding(Charsets.UTF_8.name());
        } catch (UnsupportedEncodingException ignore) {
            // nothing to do
        }
        this.cachingRequestWrapper = request;
        this.params.putAll(request.getParameterMap());

        if (request.getContentAsByteArray().length == 0) {
            try (ServletInputStream inputStream = request.getInputStream()) {
                this.body = IoUtils.toByteArray(inputStream);
            } catch (IOException e) {
                log.error("读取 inputStream 错误", e);
            }
        } else {
            this.body = request.getContentAsByteArray();
        }

        this.bodyString = new String(this.body, StandardCharsets.UTF_8);
    }

    /**
     * Gets reader *
     *
     * @return the reader
     * @since 1.0.0
     */
    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    /**
     * 通过判断 content-type 类型来从不同的地方获取数据
     * 1. application/x-www-form-urlencoded --> RequestParameters
     * 2. application/json --> inputstream
     *
     * @return the input streams
     * @see ServletServerHttpRequest#getBody() ServletServerHttpRequest#getBody()
     * @since 1.0.0
     */
    @Override
    public ServletInputStream getInputStream() {
        return WebUtils.getCacheInputStream(this.body);
    }

    /**
     * add a header with given name and value
     *
     * @param name  the name
     * @param value the value
     * @since 1.0.0
     */
    public void addHeader(String name, String value) {
        this.headerMap.put(name, value);
    }

    /**
     * Gets header *
     *
     * @param name name
     * @return the header
     * @since 1.0.0
     */
    @Override
    public String getHeader(String name) {
        String headerValue = super.getHeader(name);
        if (this.headerMap.containsKey(name)) {
            headerValue = this.headerMap.get(name);
        }
        return headerValue;
    }

    /**
     * Gets header names *
     *
     * @return the header names
     * @since 1.0.0
     */
    @Override
    public Enumeration<String> getHeaderNames() {
        List<String> names = Collections.list(super.getHeaderNames());
        names.addAll(this.headerMap.keySet());
        return Collections.enumeration(names);
    }

    /**
     * Gets headers *
     *
     * @param name name
     * @return the headers
     * @since 1.0.0
     */
    @Override
    public Enumeration<String> getHeaders(String name) {
        List<String> values = Collections.list(super.getHeaders(name));
        if (this.headerMap.containsKey(name)) {
            values = Collections.singletonList(this.headerMap.get(name));
        }
        return Collections.enumeration(values);
    }

    /**
     * 通过判断 content-type 类型来从不同的地方获取数据
     * 1. application/x-www-form-urlencoded --> RequestParameters
     * 2. application/json --> inputstream
     *
     * @param name the name
     * @return the parameters
     * @see ServletServerHttpRequest#getBody()
     * @since 1.0.0
     */
    @Override
    public String getParameter(String name) {
        String[] values = this.params.get(name);
        return values == null || values.length == 0 ? null : values[0];
    }

    /**
     * Get parameter values string [ ]
     *
     * @param key key
     * @return the string [ ]
     * @since 1.0.0
     */
    @Override
    public String[] getParameterValues(String key) {
        return this.params.get(key);
    }

    /**
     * Sets parameter.
     *
     * @param key   the key
     * @param value the value
     * @since 1.0.0
     */
    public void setParameter(String key, String value) {
        if (value == null) {
            return;
        }
        this.params.put(key, new String[]{value});
    }

    /**
     * Sets parameter.
     *
     * @param key   the key
     * @param value the value
     * @since 1.0.0
     */
    public void setParameter(String key, String[] value) {
        if (value == null) {
            return;
        }
        this.params.put(key, value);
    }

    /**
     * Sets parameter.
     *
     * @param key   the key
     * @param value the value
     * @since 1.0.0
     */
    public void setParameter(String key, Object value) {
        if (value == null) {
            return;
        }
        this.params.put(key, new String[]{String.valueOf(value)});
    }

    /**
     * Gets parameter map *
     *
     * @return the parameter map
     * @since 1.0.0
     */
    @Override
    public Map<String, String[]> getParameterMap() {
        return this.params;
    }

    /**
     * Gets parameter names *
     *
     * @return the parameter names
     * @since 1.0.0
     */
    @Override
    public Enumeration<String> getParameterNames() {
        return new MyEnumeration<>(this.params.keySet().iterator());
    }

    /**
     * The type My enumeration.
     *
     * @param <E> the type parameter
     * @author dong4j
     * @version 1.2.3
     * @email "mailto:dong4j@gmail.com"
     * @date 2019.11.21 19:14
     * @since 1.0.0
     */
    static class MyEnumeration<E> implements Enumeration<E> {

        /** Iterator */
        private final Iterator<E> iterator;

        /**
         * Instantiates a new My enumeration.
         *
         * @param iterator the iterator
         * @since 1.0.0
         */
        @Contract(pure = true)
        MyEnumeration(Iterator<E> iterator) {
            this.iterator = iterator;
        }

        /**
         * Has more elements boolean
         *
         * @return the boolean
         * @since 1.0.0
         */
        @Override
        public boolean hasMoreElements() {
            return this.iterator.hasNext();
        }

        /**
         * Next element e
         *
         * @return the e
         * @since 1.0.0
         */
        @Override
        public E nextElement() {
            return this.iterator.next();
        }

    }

    /**
     * Gets content type *
     *
     * @return the content type
     * @since 1.0.0
     */
    @Override
    public String getContentType() {
        return this.contentType != null ? this.contentType : super.getContentType();
    }

    /**
     * 修改请求类型
     *
     * @param contentType the content type
     * @see AbstractMessageConverterMethodArgumentResolver#readWithMessageConverters
     * @since 1.0.0
     */
    @SuppressWarnings("all")
    public void setContentType(String contentType) {
        this.addHeader(HttpHeaders.CONTENT_TYPE.toLowerCase(), MediaType.APPLICATION_JSON_VALUE);
        this.contentType = contentType;
    }

    /**
     * 获取最原始的 request
     *
     * @param request request
     * @return HttpServletRequest org request
     * @since 1.0.0
     */
    @Contract("null -> null")
    public static ServletRequest getOrgRequest(ServletRequest request) {
        if (request instanceof CacheRequestEnhanceWrapper) {
            return ((CacheRequestEnhanceWrapper) request).getOrgRequest();
        }
        return request;
    }

    /**
     * 获取最原始的request
     *
     * @return HttpServletRequest org request
     * @since 1.0.0
     */
    private ServletRequest getOrgRequest() {
        return this.cachingRequestWrapper.getRequest();
    }
}
