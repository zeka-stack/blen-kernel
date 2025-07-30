package dev.dong4j.zeka.kernel.common.config.refresh;

import java.util.Map;
import java.util.Set;

/**
 * 自定义文件变动处理逻辑
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2025.07.30
 * @since 1.0.0
 */
@FunctionalInterface
public interface ConfigChangedHandler {
    /**
     * 当配置变更时回调
     *
     * @param changedFile 变更的文件路径
     * @param changedKeys 变更的 key 集合（可能为空）
     * @param latest      最新的配置（已展平）
     */
    void onChanged(String changedFile, Set<String> changedKeys, Map<String, Object> latest);
}
