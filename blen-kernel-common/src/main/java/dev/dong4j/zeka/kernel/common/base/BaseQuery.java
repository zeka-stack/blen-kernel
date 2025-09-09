package dev.dong4j.zeka.kernel.common.base;

import dev.dong4j.zeka.kernel.common.enums.DeletedEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * <p>控制器层分页查询参数基类.
 * <p>为控制器层的分页查询操作提供统一的参数封装和验证规范.
 * <p>主要功能：
 * <ul>
 *     <li>提供分页查询的通用参数（页码、每页数量）</li>
 *     <li>支持灵活的排序配置（正序、倒序）</li>
 *     <li>提供时间范围查询支持</li>
 *     <li>集成逻辑删除状态过滤</li>
 * </ul>
 * <p>参数说明：
 * <ul>
 *     <li>page：当前页码，默认从 1 开始</li>
 *     <li>limit：每页数量，默认 10 条</li>
 *     <li>ascs/descs：排序字段，支持多字段</li>
 *     <li>startTime/endTime：时间范围查询</li>
 * </ul>
 * <p>适用场景：
 * <ul>
 *     <li>列表页面的分页查询参数</li>
 *     <li>复杂查询条件的参数整合</li>
 *     <li>搜索页面的过滤条件封装</li>
 *     <li>API 接口的统一查询参数规范</li>
 * </ul>
 *
 * @param <T> 主键类型，必须实现 Serializable 接口
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.06.03 03:36
 * @since 1.0.0
 */
@Data
@SuperBuilder
@NoArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("java:S1845")
public abstract class BaseQuery<T extends Serializable> extends BaseForm<T> {
    /** serialVersionUID */
    @Serial
    private static final long serialVersionUID = -3550589993340031894L;

    /** PAGE */
    public static final String PAGE = "page";
    /** LIMIT */
    public static final String LIMIT = "limit";
    /** ASCS */
    public static final String ASCS = "ascs";
    /** DESCS */
    public static final String DESCS = "descs";
    /** START_TIME */
    public static final String START_TIME = "startTime";
    /** END_TIME */
    public static final String END_TIME = "endTime";

    /** 当前页 */
    @Builder.Default
    @Schema(description = "当前页, 默认 1")
    protected Integer page = 1;
    /** 每页的数量 */
    @Builder.Default
    @Schema(description = "每页的数量, 默认 10")
    protected Integer limit = 10;
    /** 正序排序字段 */
    @Schema(description = "正序排序字段")
    protected String ascs;
    /** 倒序排序字段 */
    @Schema(description = "倒序排序字段")
    protected String descs;
    /** Start time */
    @Schema(description = "待查询记录的开始时间. 格式: [yyyy-MM-dd HH:mm:ss]")
    protected Date startTime;
    /** End time */
    @Schema(description = "待查询记录的结束时间. 格式: [yyyy-MM-dd HH:mm:ss]")
    protected Date endTime;
    /** Deleted */
    @Builder.Default
    @Schema(description = "是否被删除, 默认未删除")
    protected DeletedEnum deleted = DeletedEnum.N;
}
