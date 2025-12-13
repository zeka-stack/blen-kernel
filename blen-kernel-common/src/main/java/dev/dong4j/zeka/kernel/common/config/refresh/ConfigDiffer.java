package dev.dong4j.zeka.kernel.common.config.refresh;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 配置差异比较工具类
 * <p>
 * 提供比较两个配置映射 (Map) 之间差异的功能, 用于识别配置项的变化情况. 该类主要用于检测配置项的增删改操作, 适用于配置管理, 配置校验等场景.
 *
 * @author zeka.stack.team
 * @version 1.0.0
 * @email "mailto:zeka.stack@gmail.com"
 * @date 2025.12.13
 * @since 2.0.0
 */
public class ConfigDiffer {

    /**
     * 差异结果类
     * <p>
     * 用于存储和表示两个对象之间差异的键集合, 包含发生变化的键以及是否具有差异的标识
     *
     * @author zeka.stack.team
     * @version 1.0.0
     * @email "mailto:zeka.stack@gmail.com"
     * @date 2025.12.13
     * @since 2.0.0
     */
    public static class DiffResult {
        /**
         * 已更改的键集合
         * <p>
         * 用于记录配置或数据中发生变化的键值对
         */
        public final Set<String> changedKeys;
        /** 表示当前对象是否与参考对象存在差异 */
        public final boolean hasDiff;

        /**
         * 构造一个 DiffResult 实例
         * <p>
         * 根据传入的变更键集合初始化 DiffResult 对象, 并判断是否存在差异
         *
         * @param changedKeys 包含变更键的集合
         */
        public DiffResult(Set<String> changedKeys) {
            this.changedKeys = changedKeys;
            this.hasDiff = !changedKeys.isEmpty();
        }
    }

    /**
     * 比较两个配置映射, 找出键值不同的键
     * <p>
     * 该方法将两个配置 Map 进行对比, 收集所有键值不同的键, 并返回 DiffResult 对象
     *
     * @param oldConfig 旧的配置映射
     * @param newConfig 新的配置映射
     * @return 包含所有键值不同的键的 DiffResult 对象
     */
    public static DiffResult diff(Map<String, Object> oldConfig, Map<String, Object> newConfig) {
        Set<String> changedKeys = new HashSet<>();

        Set<String> allKeys = new HashSet<>();
        allKeys.addAll(oldConfig.keySet());
        allKeys.addAll(newConfig.keySet());

        for (String key : allKeys) {
            Object oldValue = oldConfig.get(key);
            Object newValue = newConfig.get(key);

            if (!Objects.equals(oldValue, newValue)) {
                changedKeys.add(key);
            }
        }

        return new DiffResult(changedKeys);
    }
}
