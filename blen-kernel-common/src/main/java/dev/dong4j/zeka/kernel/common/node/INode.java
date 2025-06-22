package dev.dong4j.zeka.kernel.common.node;

import java.util.List;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 14:52
 * @since 1.0.0
 */
public interface INode {

    /**
     * 主键
     *
     * @return Integer id
     * @since 1.0.0
     */
    Integer getId();

    /**
     * 父主键
     *
     * @return Integer parent id
     * @since 1.0.0
     */
    Integer getParentId();

    /**
     * 子孙节点
     *
     * @return List children
     * @since 1.0.0
     */
    List<INode> getChildren();

}
