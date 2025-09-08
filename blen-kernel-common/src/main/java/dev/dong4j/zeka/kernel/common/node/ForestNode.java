package dev.dong4j.zeka.kernel.common.node;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>Description: 森林节点类</p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.26 20:42
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ForestNode extends BaseNode {

    /**
     * 节点内容
     */
    private Object content;

    /**
     * Instantiates a new Forest node.
     *
     * @param id       the id
     * @param parentId the parent id
     * @param content  the content
     * @since 1.0.0
     */
    public ForestNode(Integer id, Integer parentId, Object content) {
        this.id = id;
        this.parentId = parentId;
        this.content = content;
    }

}
