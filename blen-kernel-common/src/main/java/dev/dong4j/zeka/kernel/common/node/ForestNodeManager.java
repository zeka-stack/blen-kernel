package dev.dong4j.zeka.kernel.common.node;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: 森林管理类</p>
 *
 * @param <T> the type parameter
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 14:53
 * @since 1.0.0
 */
public class ForestNodeManager<T extends INode> {

    /**
     * 森林的所有节点
     */
    private final List<T> list;

    /**
     * 森林的父节点ID
     */
    private final List<Integer> parentIds = new ArrayList<>();

    /**
     * Instantiates a new Forest node manager.
     *
     * @param items the items
     * @since 1.0.0
     */
    public ForestNodeManager(List<T> items) {
        list = items;
    }

    /**
     * 根据节点ID获取一个节点
     *
     * @param id 节点ID
     * @return 对应的节点对象 tree node at
     * @since 1.0.0
     */
    public INode getTreeNodeAt(int id) {
        for (INode forestNode : list) {
            if (forestNode.getId() == id) {
                return forestNode;
            }
        }
        return null;
    }

    /**
     * 增加父节点ID
     *
     * @param parentId 父节点ID
     * @since 1.0.0
     */
    public void addParentId(Integer parentId) {
        parentIds.add(parentId);
    }

    /**
     * 获取树的根节点(一个森林对应多颗树)
     *
     * @return 树的根节点集合 root
     * @since 1.0.0
     */
    public List<T> getRoot() {
        List<T> roots = new ArrayList<>();
        for (T forestNode : list) {
            if (forestNode.getParentId() == 0 || parentIds.contains(forestNode.getId())) {
                roots.add(forestNode);
            }
        }
        return roots;
    }

}
