package dev.dong4j.zeka.kernel.devtools.core.config.po;

import dev.dong4j.zeka.kernel.common.enums.EnabledEnum;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description: 枚举类型 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2024.04.03 18:37
 * @since x.x.x
 */
public enum EnumType {

    /** 自定义枚举, 自定义枚举:Integer:用户状态:AAA(0, "未审核"),:BBB(1, "审核中"),:CCC(2, "审核未通过"),:DDD(3,"已锁定"),:EEE(4,"正常"); */
    CUSTOM,
    /** 公共枚举(由框架层定义, 但业务层不一定使用的枚举), 由框架层提供, 公共枚举:EnabledEnum:可用状态 */
    COMMON,
    /** 通用枚举 (放在实体父类中, 目前只有 DeleteEnum), 由框架层提供, 通用枚举:删除状态:0: 未删除，删除就是当前数据的主键id用于代表唯一性 */
    GENERA;

    /**
     * CACHE
     *
     * @see EnabledEnum
     */
    public static final Map<String, String> CACHE;

    static {
        Map<String, String> map = new HashMap<>();
        map.put("EnabledEnum", "dev.dong4j.zeka.kernel.common.enums.EnabledEnum");
        CACHE = Collections.unmodifiableMap(map);
    }
}
