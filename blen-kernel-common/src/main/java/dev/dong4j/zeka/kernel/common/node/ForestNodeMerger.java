package dev.dong4j.zeka.kernel.common.node;

import java.util.List;

/**
 * <p>Description: 森林节点归并类 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.26 20:42
 * @since 1.0.0
 */
public class ForestNodeMerger {

    /**
     * 将节点数组归并为一个森林 (多棵树)  (填充节点的children域)
     * 时间复杂度为O(n^2)
     *
     * @param <T>   T 泛型标记
     * @param items 节点域
     * @return 多棵树的根节点集合 list
     * @since 1.0.0
     */
    public static <T extends INode> List<T> merge(List<T> items) {
        ForestNodeManager<T> forestNodeManager = new ForestNodeManager<>(items);
        items.forEach(forestNode -> {
            if (forestNode.getParentId() != 0) {
                INode node = forestNodeManager.getTreeNodeAt(forestNode.getParentId());
                if (node != null) {
                    node.getChildren().add(forestNode);
                } else {
                    forestNodeManager.addParentId(forestNode.getId());
                }
            }
        });
        return forestNodeManager.getRoot();
    }

}
