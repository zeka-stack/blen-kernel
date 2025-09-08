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
 * <p>Description: controller 分页查询参数</p>
 *
 * @param <T> parameter
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
