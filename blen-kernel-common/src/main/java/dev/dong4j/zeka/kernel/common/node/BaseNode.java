package dev.dong4j.zeka.kernel.common.node;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: 节点基类</p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 14:52
 * @since 1.0.0
 */
@Data
public class BaseNode implements INode {

    /**
     * 主键ID
     */
    protected Integer id;

    /**
     * 父节点ID
     */
    protected Integer parentId;

    /**
     * 子孙节点
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    protected List<INode> children = new ArrayList<>();

}
