package dev.dong4j.zeka.kernel.common.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.27 09:44
 * @since 1.0.0
 */
@Slf4j
class WebUtilsTest {

    /**
     * Is body
     *
     * @since 1.0.0
     */
    @Test
    void isBody() {
    }

    /**
     * Gets cookie val * @since 1.0.0
     */
    @Test
    void getCookieVal() {
    }

    /**
     * Gets request * @since 1.0.0
     */
    @Test
    void getRequest() {
    }

    /**
     * Gets cookie val 1 * @since 1.0.0
     */
    @Test
    void getCookieVal1() {
    }

    /**
     * Gets cookie * @since 1.0.0
     */
    @Test
    void getCookie() {
    }

    /**
     * Remove cookie
     *
     * @since 1.0.0
     */
    @Test
    void removeCookie() {
    }

    /**
     * Sets cookie * @since 1.0.0
     */
    @Test
    void setCookie() {
    }

    /**
     * Render json
     *
     * @since 1.0.0
     */
    @Test
    void renderJson() {
    }

    /**
     * Render json 1
     *
     * @since 1.0.0
     */
    @Test
    void renderJson1() {
    }

    /**
     * Gets ip * @since 1.0.0
     */
    @Test
    void getIp() {
        HttpServletRequest mockedRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.when(mockedRequest.getHeader("X-Requested-For")).thenReturn("192.168.2.2");
        log.info("{}", WebUtils.getIp(mockedRequest));
    }

    /**
     * Gets ip 1 * @since 1.0.0
     */
    @Test
    void getIp1() {
    }

    /**
     * Gets request param string * @since 1.0.0
     */
    @Test
    void getRequestParamString() {
    }

    /**
     * Gets request str * @since 1.0.0
     */
    @Test
    void getRequestStr() {
    }

    /**
     * Gets request str 1 * @since 1.0.0
     */
    @Test
    void getRequestStr1() {
    }

    /**
     * Gets request bytes * @since 1.0.0
     */
    @Test
    void getRequestBytes() {
    }

    /**
     * Gets remote addr * @since 1.0.0
     */
    @Test
    void getRemoteAddr() {
    }

    /**
     * Is ip addr
     *
     * @since 1.0.0
     */
    @Test
    void isIpAddr() {
    }

    /**
     * Is robot
     *
     * @since 1.0.0
     */
    @Test
    void isRobot() {
    }

    /**
     * Gets cookie value * @since 1.0.0
     */
    @Test
    void getCookieValue() {
    }

    /**
     * Sets cookie 1 * @since 1.0.0
     */
    @Test
    void setCookie1() {
    }

    /**
     * Sets cookie 2 * @since 1.0.0
     */
    @Test
    void setCookie2() {
    }

    /**
     * Gets domain of server name * @since 1.0.0
     */
    @Test
    void getDomainOfServerName() {
    }

    /**
     * Delete cookie
     *
     * @since 1.0.0
     */
    @Test
    void deleteCookie() {
    }

    /**
     * Gets http port * @since 1.0.0
     */
    @Test
    void getHttpPort() {
    }

    /**
     * Gets param * @since 1.0.0
     */
    @Test
    void getParam() {
    }

    /**
     * Gets param 1 * @since 1.0.0
     */
    @Test
    void getParam1() {
    }

    /**
     * Gets param 2 * @since 1.0.0
     */
    @Test
    void getParam2() {
    }

    /**
     * Gets parameter map * @since 1.0.0
     */
    @Test
    void getParameterMap() {
    }

    /**
     * Gets header * @since 1.0.0
     */
    @Test
    void getHeader() {
    }

    /**
     * Is multipart content
     *
     * @since 1.0.0
     */
    @Test
    void isMultipartContent() {
    }

    /**
     * Build query params map
     *
     * @since 1.0.0
     */
    @Test
    void buildQueryParamsMap() {
    }

    /**
     * Gets body * @since 1.0.0
     */
    @Test
    void getBody() {
    }

    /**
     * Gets url params * @since 1.0.0
     */
    @Test
    void getUrlParams() {
        String params = "?xxx=111&yyy=222&zzz=";
        log.info("{}", UrlUtils.buildMapByUrlParams(params));
    }

    /**
     * Gets url params by map * @since 1.0.0
     */
    @Test
    void getUrlParamsByMap() {
        String str = UrlUtils.buildUrlParamsByMap(new HashMap<String, String>() {
            private static final long serialVersionUID = -867566855248845416L;

            {
                this.put("xxx", "111");
                this.put("yyy", "222");
                this.put("zzz", "");
            }
        });

        log.info("{}", str);

        String url = "http://baidu.com";
        url += "?" + str;
        log.info("{}", url);
    }


    @Test
    void getUrl() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getScheme()).thenReturn("http");
        Mockito.when(request.getServerName()).thenReturn("www.aa.bb");
        Mockito.when(request.getServerPort()).thenReturn(80);

        log.info("{}", WebUtils.getUrl(request));
    }
}
