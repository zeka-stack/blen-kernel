package dev.dong4j.zeka.kernel.common.constant;

import lombok.experimental.UtilityClass;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dongshijie@gmail.com"
 * @date 2020.02.13 16:00
 * @since 1.0.0
 */
@UtilityClass
public final class BasicConstant {
    /** BOOST_EXECUTOR */
    public static final String BOOST_EXECUTOR = "boostExecutor";
    /** DYNAMIC_EXECUTOR */
    public static final String DYNAMIC_EXECUTOR = "dynamicExecutor";
    /** BOOST_EXECUTORSERVICE */
    public static final String BOOST_EXECUTORSERVICE = "boostExecutorService";
    /** JSON */
    public static final String JSON = "json";
    /** TRACE_ID */
    public static final String TRACE_ID = "traceId";
    /** 客户端标识 */
    public static final String HEADER_CLIENT_ID = "X-Client-Id";
    /** 网关写入的原始请求地址 */
    public static final String X_GATEWAY_ORIGINAL = "X-Gatetay-Original";
    /** 网关写入的路由日志 */
    public static final String X_GATEWAY_ROUTER = "X-Gatetay-Router";
    /** 对 rest 和 agent 结果进行解包/装包 的字段名 */
    public static final String RESULT_WRAPPER_VALUE_KEY = "value";

    /** REQUEST_EXCEPTION_INFO_ATTR */
    public static final String REQUEST_EXCEPTION_INFO_ATTR = "javax.servlet.error.exception";

    /**
     * <p>Description: agent service 通用接口命名规则 </p>
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.11.22 12:16
     * @since 1.0.0
     */
    @UtilityClass
    public static class Agent {

        /** 通过id */
        public static final String FIND = "0100";
        /** 通过id */
        public static final String FIND_BATCH = "0101";
        /** 通过查询 */
        public static final String FIND_BY_QUERY = "0102";
        /** 所有 */
        public static final String FIND_ALL = "0103";
        /** 得到数 */
        public static final String COUNTS = "0104";
        /** 被查询数 */
        public static final String COUNTS_BY_QUERY = "0105";
        /** 列表 */
        public static final String LIST = "0106";
        /** 页面 */
        public static final String PAGE = "0107";

        /** 保存 */
        public static final String CREATE = "0200";
        /** 批量保存 */
        public static final String CREATE_BATCH = "0201";
        /** 保存忽略 */
        public static final String CREATE_IGNORE = "0202";
        /** 批量保存忽略 */
        public static final String CREATE_IGNORE_BATCH = "0203";
        /** 保存替换 */
        public static final String CREATE_REPLACE = "0204";
        /** 批量保存取代 */
        public static final String CREATE_REPLACE_BATCH = "0205";
        /** 保存更新 */
        public static final String CREATE_UPDATE = "0206";
        /** 批量保存更新 */
        public static final String CREATE_UPDATE_BATCH = "0207";

        /** 更新 */
        public static final String UPDATE = "0300";
        /** 批处理更新 */
        public static final String UPDATE_BATCH = "0301";

        /** 删除 */
        public static final String DELETE = "0400";
        /** 多删除 */
        public static final String DELETE_BATCH = "0401";
        /** 删除的条件 */
        public static final String DELETE_BY_CONDITION = "0402";
    }
}
