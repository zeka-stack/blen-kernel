package dev.dong4j.zeka.kernel.common.util;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2021.07.22 16:17
 * @since 1.0.0
 */
@Slf4j
@UtilityClass
public class UrlUtils extends org.springframework.web.util.UriUtils {

    /**
     * Build url params by map
     *
     * @param map map
     * @return the string
     * @since 1.0.0
     */
    public static String buildUrlParamsByMap(Map<String, String> map) {
        if (map == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey()).append(StringPool.EQUALS).append(entry.getValue()).append(StringPool.AMPERSAND);
        }
        String s = sb.toString();
        if (s.endsWith(StringPool.AMPERSAND)) {
            s = StringUtils.subBefore(s, StringPool.AMPERSAND);
        }
        return s;
    }

    /**
     * Build map by url params
     *
     * @param param param
     * @return the map
     * @since 1.0.0
     */
    public static @NotNull Map<String, String> buildMapByUrlParams(String param) {
        Map<String, String> map = Maps.newHashMap();
        if (StringUtils.isBlank(param)) {
            return map;
        }
        if (param.startsWith(StringPool.QUESTION_MARK)) {
            param = StringUtils.subAfter(param, StringPool.QUESTION_MARK);
        }
        String[] params = param.split(StringPool.AMPERSAND);
        for (String s : params) {
            String[] p = s.split(StringPool.EQUALS);
            if (p.length == 2) {
                map.put(p[0], p[1]);
            }
        }
        return map;
    }

    /**
     * url 编码,同js decodeURIComponent
     *
     * @param source  url
     * @param charset 字符集
     * @return 编码后的url string
     * @since 1.0.0
     */
    @NotNull
    public static String encodeUrl(String source, @NotNull Charset charset) {
        return UrlUtils.encode(source, charset.name());
    }

    /**
     * url 解码
     *
     * @param source  url
     * @param charset 字符集
     * @return 解码url string
     * @since 1.0.0
     */
    @NotNull
    public static String decodeUrl(String source, @NotNull Charset charset) {
        return UrlUtils.decode(source, charset.name());
    }

    /**
     * 获取 url 路径
     *
     * @param uriStr 路径
     * @return url路径 path
     * @since 1.0.0
     */
    public static String getPath(String uriStr) {
        URI uri;

        try {
            uri = new URI(uriStr);
        } catch (URISyntaxException var3) {
            throw new RuntimeException(var3);
        }

        return uri.getPath();
    }

    /**
     * map 转为 url 参数, 默认需要参数编码
     *
     * @param source source
     * @return the string
     * @since 1.0.0
     */
    @NotNull
    public static String asUrlParams(@NotNull Map<String, String> source) {
        return asUrlParams(source, true);
    }

    /**
     * map 转为 url 参数
     *
     * @param source     source
     * @param urlEncoder url encoder
     * @return the string
     * @since 1.0.0
     */
    @NotNull
    public static String asUrlParams(@NotNull Map<String, String> source, boolean urlEncoder) {
        Map<String, String> tmp = Maps.newHashMap();
        source.forEach((k, v) -> {
            if (k != null) {
                try {
                    if (urlEncoder) {
                        tmp.put(k, URLEncoder.encode(v, Charsets.UTF_8_NAME));
                    } else {
                        tmp.put(k, v);
                    }
                } catch (UnsupportedEncodingException e) {
                    log.error("url encode error", e);
                }
            }
        });
        return Joiner.on("&").useForNull("").withKeyValueSeparator("=").join(tmp);
    }
}
