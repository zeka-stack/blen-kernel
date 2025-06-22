package dev.dong4j.zeka.kernel.common.node;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>Description: 树型节点类</p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.26 20:43
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TreeNode extends BaseNode {

    /** Title */
    private String title;
    /** Key */
    private Integer key;
    /** Value */
    private Integer value;

}
