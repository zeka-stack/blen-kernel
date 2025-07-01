package dev.dong4j.zeka.kernel.devtools;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:06
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class TemplatesConfig {
    /** CONTROLLER */
    public static final String CONTROLLER = "controller";
    /** SERVICE */
    public static final String SERVICE = "service";
    /** ENUM */
    public static final String ENUM = "enum";
    /** IMPL */
    public static final String IMPL = "impl";
    /** ENTITY */
    public static final String ENTITY = "entity";
    /** VO */
    public static final String QUERY = "query";
    /** DTO */
    public static final String DTO = "dto";
    /** DAO */
    public static final String DAO = "dao";
    /** CONVERTER */
    public static final String CONVERTER = "converter";
    /** XML */
    public static final String XML = "xml";
    /** START */
    public static final String START = "start";
    /** PLACEHOLDER */
    public static final String PLACEHOLDER = "占位符";

    /** Controller */
    private boolean controller = false;
    /** Service */
    private boolean service = false;
    /** Enums */
    private boolean enums = false;
    /** IMPL */
    private boolean impl = false;
    /** Entity */
    private boolean entity = false;
    /** Vo */
    private boolean query = false;
    /** Dto */
    private boolean dto = false;
    /** Dao */
    private boolean dao = false;
    /** converter */
    private boolean converter = false;
    /** Xml */
    private boolean xml = false;
    /** 为 true 时, 生成配置文件, 启动类, 单元测试主类 */
    private boolean start = false;

}
