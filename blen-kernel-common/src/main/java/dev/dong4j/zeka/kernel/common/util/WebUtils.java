package dev.dong4j.zeka.kernel.common.util;

import cn.hutool.http.useragent.Browser;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.google.common.collect.Maps;
import dev.dong4j.zeka.kernel.common.asserts.Assertions;
import dev.dong4j.zeka.kernel.common.context.AgentRequestContextHolder;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.util.ContentCachingRequestWrapper;

/**
 * <p>Description: Web工具类，用于处理HTTP请求相关的工具方法</p>
 * <p>
 * 主要功能：
 * <ul>
 *     <li>Cookie操作（读取、设置、删除）</li>
 *     <li>JSON响应处理</li>
 *     <li>客户端IP地址获取</li>
 *     <li>请求参数处理</li>
 *     <li>请求URL处理</li>
 *     <li>用户代理解析</li>
 *     <li>请求头处理</li>
 * </ul>
 * </p>
 * <p>
 * 使用示例：
 * <pre>
 * // 读取Cookie
 * String cookieValue = WebUtils.getCookieVal("sessionId");
 *
 * // 设置Cookie
 * WebUtils.setCookie(response, "sessionId", "123456", 3600);
 *
 * // 删除Cookie
 * WebUtils.removeCookie(response, "sessionId");
 *
 * // 返回JSON响应
 * Map<String, Object> result = new HashMap<>();
 * result.put("code", 200);
 * result.put("message", "success");
 * WebUtils.renderJson(response, result);
 *
 * // 获取客户端IP
 * String clientIp = WebUtils.getIp(request);
 *
 * // 获取请求参数
 * Map<String, String> params = WebUtils.getParameterMap(request);
 *
 * // 获取浏览器信息
 * String browser = WebUtils.getBrowser(request);
 * </pre>
 * </p>
 * <p>
 * 技术特性：
 * <ul>
 *     <li>继承Spring的WebUtils功能</li>
 *     <li>支持AJAX请求判断</li>
 *     <li>处理代理和负载均衡场景下的IP获取</li>
 *     <li>提供安全的Cookie操作</li>
 *     <li>支持JSON序列化响应</li>
 *     <li>集成UserAgent解析</li>
 * </ul>
 * </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.27 09:40
 * @since 1.0.0
 */
@Slf4j
@UtilityClass
@SuppressWarnings("checkstyle:MethodLimit")
public class WebUtils extends org.springframework.web.util.WebUtils {

    /**
     * User-Agent请求头名称
     */
    public static final String USER_AGENT_HEADER = "user-agent";

    /**
     * 未知标识
     */
    public static final String UN_KNOWN = "unknown";

    /**
     * 判断是否ajax请求
     * spring ajax 返回含有 ResponseBody 或者 RestController注解
     *
     * @param handlerMethod HandlerMethod
     * @return 是否ajax请求
     * @since 1.0.0
     */
    public static boolean isBody(HandlerMethod handlerMethod) {
        ResponseBody responseBody = ClassUtils.getAnnotation(handlerMethod, ResponseBody.class);
        return responseBody != null;
    }

    /**
     * 读取cookie值
     *
     * @param name cookie名称
     * @return cookie值
     * @since 1.0.0
     */
    public static String getCookieVal(String name) {
        HttpServletRequest request = WebUtils.getRequest();
        return getCookieVal(request, name);
    }

    /**
     * 获取 HttpServletRequest
     *
     * @return HttpServletRequest对象
     * @since 1.0.0
     */
    @NotNull
    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            requestAttributes = AgentRequestContextHolder.getRequestAttributes();
        }
        Assertions.notNull(requestAttributes, "当前线程中不存在 RequestAttributes");
        return ((ServletRequestAttributes) Objects.requireNonNull(requestAttributes)).getRequest();
    }

    /**
     * 读取cookie值
     *
     * @param request HttpServletRequest对象
     * @param name    cookie名称
     * @return cookie值
     * @since 1.0.0
     */
    @Nullable
    public static String getCookieVal(HttpServletRequest request, String name) {
        Cookie cookie = getCookie(request, name);
        return cookie != null ? cookie.getValue() : null;
    }

    /**
     * 获取COOKIE
     *
     * @param request HttpServletRequest对象
     * @param name    cookie名称
     * @return Cookie对象
     * @since 1.0.0
     */
    public static @Nullable Cookie getCookie(@NotNull HttpServletRequest request, @NotNull String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie ck : cookies) {
            if (StringUtils.equalsIgnoreCase(name, ck.getName())) {
                return ck;
            }
        }
        return null;
    }

    /**
     * 清除指定的cookie
     *
     * @param response HttpServletResponse对象
     * @param key      cookie键名
     * @since 1.0.0
     */
    public static void removeCookie(HttpServletResponse response, String key) {
        setCookie(response, key, null, 0);
    }

    /**
     * 设置cookie
     *
     * @param response        HttpServletResponse对象
     * @param name            cookie名称
     * @param value           cookie值
     * @param maxAgeInSeconds 最大存活时间（秒）
     * @since 1.0.0
     */
    public static void setCookie(@NotNull HttpServletResponse response, String name, @Nullable String value, int maxAgeInSeconds) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath(StringPool.SLASH);
        cookie.setMaxAge(maxAgeInSeconds);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    /**
     * 返回JSON响应
     *
     * @param response HttpServletResponse对象
     * @param result   结果对象
     * @since 1.0.0
     */
    public static void renderJson(HttpServletResponse response, Object result) {
        renderJson(response, result, MediaType.APPLICATION_JSON_VALUE);
    }

    /**
     * 返回JSON响应
     *
     * @param response    HttpServletResponse对象
     * @param result      结果对象
     * @param contentType 内容类型
     * @since 1.0.0
     */
    public static void renderJson(@NotNull HttpServletResponse response, Object result, String contentType) {
        response.setCharacterEncoding(StringPool.UTF_8);
        response.setContentType(contentType);
        try (PrintWriter out = response.getWriter()) {
            out.append(Jsons.toJson(result));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 获取客户端IP地址
     *
     * @return 客户端IP地址
     * @since 1.0.0
     */
    public static String getIp() {
        return getIp(WebUtils.getRequest());
    }

    /**
     * 获取客户端IP地址
     *
     * @param request HttpServletRequest对象
     * @return 客户端IP地址
     * @since 1.0.0
     */
    @Nullable
    public static String getIp(HttpServletRequest request) {
        Assertions.notNull(request, "HttpServletRequest is null");
        String ip = request.getHeader("X-Requested-For");
        if (StringUtils.isBlank(ip) || UN_KNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (StringUtils.isBlank(ip) || UN_KNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || UN_KNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || UN_KNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(ip) || UN_KNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(ip) || UN_KNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return StringUtils.isBlank(ip) ? NetUtils.ip(request) : ip.split(",")[0];
    }

    /**
     * 获取请求的URL: http[s]://www.xxx.xx[:port]
     *
     * @return 请求URL
     * @since 1.0.0
     */
    public static @NotNull String getUrl() {
        return getUrl(WebUtils.getRequest());
    }

    /**
     * 获取请求的URL
     *
     * @param request HttpServletRequest对象
     * @return 请求URL
     * @since 1.0.0
     */
    public static @NotNull String getUrl(@NotNull HttpServletRequest request) {
        String schemeAndHost = request.getScheme() + "://" + request.getServerName();
        // 默认端口输出
        int defaultPort = 80;
        if (request.getServerPort() != defaultPort) {
            schemeAndHost += ":" + request.getServerPort();
        }
        return schemeAndHost;
    }

    /***
     * 获取 request 中 json 字符串的内容
     *
     * @param request HttpServletRequest对象
     * @return 字符串内容
     * @since 1.0.0
     */
    public static String getRequestParamString(HttpServletRequest request) {
        try {
            return getRequestStr(request);
        } catch (Exception ex) {
            return StringPool.EMPTY;
        }
    }

    /**
     * 获取 request 请求内容
     *
     * @param request HttpServletRequest对象
     * @return 请求内容字符串
     * @since 1.0.0
     */
    public static String getRequestStr(@NotNull HttpServletRequest request) {
        String queryString = request.getQueryString();
        if (StringUtils.isNotBlank(queryString)) {
            return new String(queryString.getBytes(Charsets.ISO_8859_1), Charsets.UTF_8)
                .replaceAll("&amp;", StringPool.AMPERSAND)
                .replaceAll("%22", "\"");
        }
        return getRequestStr(request, getRequestBytes(request));
    }

    /**
     * 获取 request 请求内容
     *
     * @param request HttpServletRequest对象
     * @param buffer  字节数组缓冲区
     * @return 请求内容字符串
     * @since 1.0.0
     */
    @SneakyThrows
    @NotNull
    public static String getRequestStr(@NotNull HttpServletRequest request, byte[] buffer) {
        String str = new String(buffer, request.getCharacterEncoding()).trim();
        if (StringUtils.isBlank(str)) {
            StringBuilder sb = new StringBuilder();
            Enumeration<String> parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String key = parameterNames.nextElement();
                String value = request.getParameter(key);
                StringUtils.appendBuilder(sb, key, StringPool.EQUALS, value, StringPool.AMPERSAND);
            }
            str = StringUtils.removeSuffix(sb.toString(), StringPool.AMPERSAND);
        }
        return str.replaceAll("&amp;", StringPool.AMPERSAND);
    }

    /**
     * 获取 request 请求的 byte[] 数组
     *
     * @param request HttpServletRequest对象
     * @return 字节数组
     * @since 1.0.0
     */
    @SneakyThrows
    public static byte[] getRequestBytes(@NotNull HttpServletRequest request) {
        int contentLength = request.getContentLength();
        if (contentLength < 0) {
            return null;
        }
        byte[] buffer = new byte[contentLength];
        for (int i = 0; i < contentLength; ) {

            int readlen = request.getInputStream().read(buffer, i, contentLength - i);
            if (readlen == -1) {
                break;
            }
            i += readlen;
        }
        return buffer;
    }

    /**
     * 获取客户端IP地址,此方法用在proxy环境中
     *
     * @param req HttpServletRequest对象
     * @return 客户端IP地址
     * @since 1.0.0
     */
    public static String getRemoteAddr(@NotNull HttpServletRequest req) {
        String ip = req.getHeader("X-Forwarded-For");
        if (StringUtils.isNotBlank(ip)) {
            String[] ips = StringUtils.split(ip, StringPool.COMMA);
            for (String tmpip : Objects.requireNonNull(ips)) {
                if (StringUtils.isBlank(tmpip)) {
                    continue;
                }
                tmpip = tmpip.trim();
                if (isIpAddr(tmpip) && !tmpip.startsWith("10.") && !tmpip.startsWith("192.168.")
                    && !NetUtils.LOCAL_HOST.equals(tmpip)) {
                    return tmpip.trim();
                }
            }
        }
        ip = req.getHeader("x-real-ip");
        if (isIpAddr(ip)) {
            return ip;
        }
        ip = req.getRemoteAddr();
        if (ip.indexOf(CharPool.DOT) == -1) {
            ip = NetUtils.LOCAL_HOST;
        }
        return ip;
    }

    /**
     * 判断字符串是否是一个IP地址
     *
     * @param addr IP地址字符串
     * @return 是否为有效的IP地址
     * @since 1.0.0
     */
    @SuppressWarnings(value = {"PMD.UndefineMagicConstantRule", "checkstyle:ReturnCount"})
    public static boolean isIpAddr(String addr) {
        if (ObjectUtils.isEmpty(addr)) {
            return false;
        }
        String[] ips = StringUtils.split(addr, StringPool.DOT);
        if (Objects.requireNonNull(ips).length != 4) {
            return false;
        }
        try {
            int ipa = Integer.parseInt(ips[0]);
            int ipb = Integer.parseInt(ips[1]);
            int ipc = Integer.parseInt(ips[2]);
            int ipd = Integer.parseInt(ips[3]);
            return ipa >= 0 && ipa <= 255 && ipb >= 0 && ipb <= 255 && ipc >= 0
                && ipc <= 255 && ipd >= 0 && ipd <= 255;
        } catch (Exception e) {
            log.error("transformation error", e);
        }
        return false;
    }

    /**
     * 判断是否为搜索引擎
     *
     * @param req HttpServletRequest对象
     * @return 是否为搜索引擎
     * @since 1.0.0
     */
    @SuppressWarnings("D")
    public static boolean isRobot(@NotNull HttpServletRequest req) {
        String ua = req.getHeader("user-agent");
        if (StringUtils.isBlank(ua)) {
            return false;
        }
        return ua.contains("Baiduspider")
            || ua.contains("Googlebot")
            || ua.contains("sogou")
            || ua.contains("sina")
            || ua.contains("iaskspider")
            || ua.contains("ia_archiver")
            || ua.contains("Sosospider")
            || ua.contains("YoudaoBot")
            || ua.contains("yahoo")
            || ua.contains("yodao")
            || ua.contains("MSNBot")
            || ua.contains("Twiceler")
            || ua.contains("Sosoimagespider")
            || ua.contains("naver.com/robots")
            || ua.contains("Nutch")
            || ua.contains("spider");
    }

    /**
     * 获取COOKIE值
     *
     * @param request HttpServletRequest对象
     * @param name    cookie名称
     * @return cookie值
     * @since 1.0.0
     */
    @Nullable
    public static String getCookieValue(@NotNull HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie ck : cookies) {
            if (StringUtils.equalsIgnoreCase(name, ck.getName())) {
                return ck.getValue();
            }
        }
        return null;
    }

    /**
     * 设置COOKIE
     *
     * @param request  HttpServletRequest对象
     * @param response HttpServletResponse对象
     * @param name     cookie名称
     * @param value    cookie值
     * @param maxAge   最大存活时间
     * @since 1.0.0
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String name,
                                 String value, int maxAge) {
        setCookie(request, response, name, value, maxAge, true);
    }

    /**
     * 设置COOKIE
     *
     * @param request      HttpServletRequest对象
     * @param response     HttpServletResponse对象
     * @param name         cookie名称
     * @param value        cookie值
     * @param maxAge       最大存活时间
     * @param allSubDomain 是否应用于所有子域名
     * @since 1.0.0
     */
    @SuppressWarnings("checkstyle:ParameterNumber")
    public static void setCookie(HttpServletRequest request,
                                 HttpServletResponse response,
                                 String name,
                                 String value,
                                 int maxAge,
                                 boolean allSubDomain) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        if (allSubDomain) {
            String serverName = request.getServerName();
            String domain = getDomainOfServerName(serverName);
            if (domain != null && domain.indexOf(CharPool.DOT) != -1) {
                cookie.setDomain(CharPool.DOT + domain);
            }
        }
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 获取用户访问URL中的根域名
     * 例如: www.dlog.cn -> dlog.cn
     *
     * @param host 主机名
     * @return 根域名
     * @since 1.0.0
     */
    @Nullable
    @SuppressWarnings(value = {"PMD.UndefineMagicConstantRule", "checkstyle:ReturnCount"})
    public static String getDomainOfServerName(String host) {
        if (isIpAddr(host)) {
            return null;
        }
        String[] names = StringUtils.split(host, StringPool.DOT);
        int len = Objects.requireNonNull(names).length;
        if (len == 1) {
            return null;
        }
        int www = 3;
        if (len == www) {
            return makeup(names[len - 2], names[len - 1]);
        }
        if (len > www) {
            String dp = names[len - 2];
            if ("com".equalsIgnoreCase(dp)
                || "gov".equalsIgnoreCase(dp)
                || "net".equalsIgnoreCase(dp)
                || "edu".equalsIgnoreCase(dp)
                || "org".equalsIgnoreCase(dp)) {
                return makeup(names[len - 3], names[len - 2], names[len - 1]);
            } else {
                return makeup(names[len - 2], names[len - 1]);
            }
        }
        return host;
    }

    /**
     * 拼接域名字符串
     *
     * @param ps 域名片段数组
     * @return 拼接后的域名
     * @since 1.0.0
     */
    @NotNull
    private static String makeup(@NotNull String... ps) {
        StringBuilder s = new StringBuilder();
        for (int idx = 0; idx < ps.length; idx++) {
            if (idx > 0) {
                s.append('.');
            }
            s.append(ps[idx]);
        }
        return s.toString();
    }

    /**
     * 删除cookie
     *
     * @param request      HttpServletRequest对象
     * @param response     HttpServletResponse对象
     * @param name         cookie名称
     * @param allSubDomain 是否应用于所有子域名
     * @since 1.0.0
     */
    public static void deleteCookie(HttpServletRequest request,
                                    HttpServletResponse response, String name, boolean allSubDomain) {
        setCookie(request, response, name, "", 0, allSubDomain);
    }

    /**
     * 获取HTTP端口
     *
     * @param req HttpServletRequest对象
     * @return HTTP端口号
     * @since 1.0.0
     */
    public static int getHttpPort(@NotNull HttpServletRequest req) {
        try {
            return new URL(req.getRequestURL().toString()).getPort();
        } catch (MalformedURLException excp) {
            return 80;
        }
    }

    /**
     * 获取浏览器提交的整形参数
     *
     * @param req          HttpServletRequest对象
     * @param param        参数名
     * @param defaultValue 默认值
     * @return 参数值
     * @since 1.0.0
     */
    public static int getParam(@NotNull HttpServletRequest req, String param, int defaultValue) {
        return NumberUtils.toInt(req.getParameter(param), defaultValue);
    }

    /**
     * 获取浏览器提交的长整形参数
     *
     * @param req          HttpServletRequest对象
     * @param param        参数名
     * @param defaultValue 默认值
     * @return 参数值
     * @since 1.0.0
     */
    public static long getParam(@NotNull HttpServletRequest req, String param, long defaultValue) {
        return NumberUtils.toLong(req.getParameter(param), defaultValue);
    }

    /**
     * 获取浏览器提交的字符串参数
     *
     * @param req          HttpServletRequest对象
     * @param param        参数名
     * @param defaultValue 默认值
     * @return 参数值
     * @since 1.0.0
     */
    public static String getParam(@NotNull HttpServletRequest req, String param, String defaultValue) {
        String value = req.getParameter(param);
        return (StringUtils.isEmpty(value)) ? defaultValue : value;
    }

    /**
     * 获取请求参数映射
     *
     * @param request HttpServletRequest对象
     * @return 参数映射
     * @since 1.0.0
     */
    public static @NotNull Map<String, String> getParameterMap(@NotNull HttpServletRequest request) {
        ContentCachingRequestWrapper cachingRequestWrapper = new ContentCachingRequestWrapper(request);

        Map<String, String> returnMap = Maps.newHashMapWithExpectedSize(16);
        // request.getParameterMap() 返回的是一个Map类型的值,该返回值记录着前端 (如jsp页面) 所提交请求中的请求参数和请求参数值的映射关系. 这个返回值有个特别之处——只能读.
        Map<String, String[]> parameterMap = cachingRequestWrapper.getParameterMap();
        //  Map.Entry是Map声明的一个内部接口,此接口为泛型,定义为Entry. 它表示Map中的一个实体 (一个key-value对) . 接口中有getKey(),getValue方法.
        for (Map.Entry<String, String[]> en : parameterMap.entrySet()) {
            String value = cachingRequestWrapper.getParameter(en.getKey());
            try {
                returnMap.put(en.getKey(), URLDecoder.decode(value, Charsets.UTF_8_NAME));
            } catch (Exception e) {
                log.error("url decode error", e);
            }
        }
        return returnMap;
    }

    /**
     * 获取请求头映射
     *
     * @param request HttpServletRequest对象
     * @return 请求头映射
     * @since 1.0.0
     */
    public static @NotNull Map<String, String> getHeader(@NotNull HttpServletRequest request) {
        // 获取所有的消息头名称
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames == null) {
            return Collections.emptyMap();
        }
        Map<String, String> headers = Maps.newHashMapWithExpectedSize(16);
        // 获取获取的消息头名称,获取对应的值,并输出
        while (headerNames.hasMoreElements()) {
            String nextElement = headerNames.nextElement();
            headers.put(nextElement, request.getHeader(nextElement));
        }
        return headers;
    }

    /**
     * 获取指定请求头的值
     *
     * @param headerName 请求头名称
     * @return 请求头值
     * @since 1.0.0
     */
    public static String getHeader(String headerName) {
        Map<String, String> headers = getHeader(getRequest());
        return headers.get(headerName);
    }

    /**
     * 是否multipart/form-data or application/octet-stream表单提交方式
     *
     * @param request HttpServletRequest对象
     * @return 是否为multipart内容
     * @since 1.0.0
     */
    public static boolean isMultipartContent(@NotNull HttpServletRequest request) {
        if (!HttpMethod.POST.name().equalsIgnoreCase(request.getMethod())) {
            return false;
        }
        String contentType = request.getContentType();
        if (contentType == null) {
            return false;
        }
        contentType = contentType.toLowerCase(Locale.ENGLISH);
        return contentType.startsWith("multipart/") || MediaType.APPLICATION_OCTET_STREAM_VALUE.equals(contentType);
    }

    /**
     * 构建查询参数映射
     *
     * @param request HttpServletRequest对象
     * @return 查询参数映射
     * @since 1.0.0
     */
    @SuppressWarnings("D")
    public static @NotNull Map<String, Object> buildQueryParamsMap(@NotNull HttpServletRequest request) {
        ContentCachingRequestWrapper cachingRequestWrapper = new ContentCachingRequestWrapper(request);
        Map<String, Object> params = Maps.newHashMap();
        Enumeration<String> e = cachingRequestWrapper.getParameterNames();

        StringBuilder tmpbuff = new StringBuilder();
        if (e.hasMoreElements()) {
            while (e.hasMoreElements()) {
                String name = e.nextElement();
                String[] values = cachingRequestWrapper.getParameterValues(name);
                if (values.length == 1) {
                    if (StringUtils.isNotBlank(values[0])) {
                        params.put(name, values[0]);
                    }
                } else {
                    tmpbuff.setLength(0);
                    for (String value : values) {
                        if (StringUtils.isNotBlank(value)) {
                            tmpbuff.append(value.trim()).append(",");
                        }
                    }
                    if (!tmpbuff.isEmpty()) {
                        tmpbuff.deleteCharAt(tmpbuff.length() - 1);
                        params.put(name, tmpbuff.toString());
                    }
                }
            }
        }
        return params;
    }

    /**
     * 获取类型安全的请求映射
     *
     * @param request HttpServletRequest对象
     * @return 类型安全的请求映射
     * @since 1.0.0
     */
    private @NotNull Map<String, String> getTypesafeRequestMap(@NotNull HttpServletRequest request) {
        ContentCachingRequestWrapper cachingRequestWrapper = new ContentCachingRequestWrapper(request);
        Enumeration<?> requestParamNames = cachingRequestWrapper.getParameterNames();
        Map<String, String> typesafeRequestMap = Maps.newHashMap();

        while (requestParamNames.hasMoreElements()) {
            String requestParamName = (String) requestParamNames.nextElement();
            String requestParamValue = cachingRequestWrapper.getParameter(requestParamName);
            typesafeRequestMap.put(requestParamName, requestParamValue);
        }

        return typesafeRequestMap;
    }

    /**
     * 获取请求Body内容, 注意: 调用此方法后还需要二次读取 request时, HttpServletRequest 必须为 CacheRequestWrapper
     *
     * @param request HttpServletRequest对象
     * @return 请求Body内容
     * @since 1.0.0
     */
    public static String getBody(@NotNull HttpServletRequest request) {
        ContentCachingRequestWrapper cachingRequestWrapper = new ContentCachingRequestWrapper(request);
        String bodyInfo = "";
        try (InputStream is = cachingRequestWrapper.getInputStream()) {
            bodyInfo = IoUtils.toString(is, StandardCharsets.UTF_8);
        } catch (IOException ignored) {
        }
        return StringUtils.replaceBlank(bodyInfo);
    }

    /**
     * 获取缓存输入流
     *
     * @param body 字节数组
     * @return 缓存输入流
     * @since 1.0.0
     */
    @NotNull
    public static ServletInputStream getCacheInputStream(byte[] body) {
        ByteArrayInputStream bais = new ByteArrayInputStream(body);

        return new ServletInputStream() {

            @Override
            public int read() {
                return bais.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }
        };
    }

    /**
     * 将URL参数解析为映射
     *
     * @param url URL字符串
     * @return 参数映射
     * @since 1.0.0
     */
    public static @NotNull Map<String, Object> converterToMap(@NotNull String url) {
        try {
            Map<String, Object> params = new HashMap<>(16);
            for (String param : url.split(StringPool.AMPERSAND)) {
                String[] pair = param.split(StringPool.EQUALS);
                String key = URLDecoder.decode(pair[0], Charsets.UTF_8_NAME);
                String value = StringPool.EMPTY;
                if (pair.length > 1) {
                    value = URLDecoder.decode(pair[1], Charsets.UTF_8_NAME);
                }
                params.put(key, value);
            }
            return params;
        } catch (UnsupportedEncodingException ex) {
            log.error("{}", ex.getMessage());
        }
        return Collections.emptyMap();
    }

    /**
     * 获取浏览器信息
     *
     * @param request HttpServletRequest对象
     * @return 浏览器名称
     * @since 1.0.0
     */
    public static String getBrowser(@NotNull HttpServletRequest request) {
        UserAgent userAgent = UserAgentUtil.parse(request.getHeader("User-Agent"));
        Browser browser = userAgent.getBrowser();
        return browser.getName();
    }
}
