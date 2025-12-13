package dev.dong4j.zeka.kernel.common.config.refresh;

import java.util.Map;
import java.util.Set;

/**
 * 配置变更处理器接口
 * <p>
 * 用于处理配置文件变更事件, 当指定配置文件中的某些键值发生变化时, 该接口的实现类将被调用以执行相应的处理逻辑.
 * 适用于需要监听和响应配置变更的场景, 如动态配置更新, 缓存刷新等.
 *
 * @author zeka.stack.team
 * @version 1.0.0
 * @email "mailto:zeka.stack@gmail.com"
 * @date 2025.12.13
 * @since 2.0.0
 */
@FunctionalInterface
public interface ConfigChangedHandler {
    /**
     * 当文件或键发生变化时触发的回调方法
     * <p>
     * 用于处理指定文件及其相关键值变化后的逻辑, 提供最新的键值对数据以供处理.
     *
     * @param changedFile 发生变化的文件名
     * @param changedKeys 发生变化的键集合
     * @param latest      最新的键值对映射, 包含所有当前的键值数据
     */
    void onChanged(String changedFile, Set<String> changedKeys, Map<String, Object> latest);
}
