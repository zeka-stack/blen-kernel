package dev.dong4j.zeka.kernel.devtools;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 模板配置类，用于配置代码生成器需要生成的模板文件类型
 * <p>
 * 该类定义了各种代码文件模板的生成开关
 * 支持Controller、Service、Entity、Mapper等各层组件的选择性生成
 * 同时支持DTO、VO、Converter等辅助类的生成
 * <p>
 * 主要功能：
 * - 支持MVC各层组件的选择性生成
 * - 支持数据传输对象（DTO、VO）生成
 * - 支持数据转换器（Converter）生成
 * - 支持项目初始化文件生成
 * - 提供链式调用API
 * <p>
 * 使用@Accessors(chain = true)生成链式调用方法，方便配置使用
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:06
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class TemplatesConfig {
    /** Controller层模板常量 */
    public static final String CONTROLLER = "controller";
    /** Service服务层模板常量 */
    public static final String SERVICE = "service";
    /** 枚举类模板常量 */
    public static final String ENUM = "enum";
    /** Service实现类模板常量 */
    public static final String IMPL = "impl";
    /** 实体类模板常量 */
    public static final String ENTITY = "entity";
    /** 查询和表单对象模板常量 */
    public static final String QUERY = "query";
    /** 数据传输对象模板常量 */
    public static final String DTO = "dto";
    /** 数据访问层模板常量 */
    public static final String DAO = "dao";
    /** 数据转换器模板常量 */
    public static final String CONVERTER = "converter";
    /** MyBatis XML映射文件模板常量 */
    public static final String XML = "xml";
    /** 项目启动相关文件模板常量 */
    public static final String START = "start";
    /** 占位符常量 */
    public static final String PLACEHOLDER = "占位符";

    /** 是否生成Controller层代码 */
    private boolean controller = false;
    /** 是否生成Service服务层代码 */
    private boolean service = false;
    /** 是否生成枚举类代码 */
    private boolean enums = false;
    /** 是否生成Service实现类代码 */
    private boolean impl = false;
    /** 是否生成Entity实体类代码 */
    private boolean entity = false;
    /** 是否生成查询和表单对象代码 */
    private boolean query = false;
    /** 是否生成DTO数据传输对象代码 */
    private boolean dto = false;
    /** 是否生成Dao数据访问层代码 */
    private boolean dao = false;
    /** 是否生成数据转换器代码 */
    private boolean converter = false;
    /** 是否生成MyBatis XML映射文件 */
    private boolean xml = false;
    /** 是否生成项目启动相关文件（配置文件、启动类、单元测试主类） */
    private boolean start = false;

}
