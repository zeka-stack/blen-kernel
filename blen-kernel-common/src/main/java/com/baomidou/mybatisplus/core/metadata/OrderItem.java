package com.baomidou.mybatisplus.core.metadata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 排序元素载体
 *
 * @author HCL Create at 2019/5/27
 * @version 1.3.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.03.11 01:34
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem implements Serializable {
    /** serialVersionUID */
    private static final long serialVersionUID = 1L;
    /** 需要进行排序的字段 */
    private String column;
    /**
     * 是否正序排列，默认 true
     */
    private boolean asc = true;

    /**
     * Asc
     *
     * @param column column
     * @return the order item
     * @since 2.1.0
     */
    public static OrderItem asc(String column) {
        return build(column, true);
    }

    /**
     * Desc
     *
     * @param column column
     * @return the order item
     * @since 2.1.0
     */
    public static OrderItem desc(String column) {
        return build(column, false);
    }

    /**
     * Ascs
     *
     * @param columns columns
     * @return the list
     * @since 2.1.0
     */
    public static List<OrderItem> ascs(String... columns) {
        return Arrays.stream(columns).map(OrderItem::asc).collect(Collectors.toList());
    }

    /**
     * Descs
     *
     * @param columns columns
     * @return the list
     * @since 2.1.0
     */
    public static List<OrderItem> descs(String... columns) {
        return Arrays.stream(columns).map(OrderItem::desc).collect(Collectors.toList());
    }

    /**
     * Build
     *
     * @param column column
     * @param asc    asc
     * @return the order item
     * @since 2.1.0
     */
    private static OrderItem build(String column, boolean asc) {
        return new OrderItem(column, asc);
    }
}

