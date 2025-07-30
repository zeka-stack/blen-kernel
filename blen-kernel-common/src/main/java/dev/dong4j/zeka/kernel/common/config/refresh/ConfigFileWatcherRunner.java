package dev.dong4j.zeka.kernel.common.config.refresh;

import cn.hutool.core.util.StrUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

/**
 * 配置变更监听执行器
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2025.07.29
 * @since 1.0.0
 */
@Slf4j
public class ConfigFileWatcherRunner {

    private final Set<String> watched = ConcurrentHashMap.newKeySet();
    private final DynamicConfigLoader loader;
    private final List<ConfigChangedHandler> handlers;

    private final AtomicReference<Map<String, Object>> currentConfig = new AtomicReference<>();
    private volatile boolean started = false;

    public ConfigFileWatcherRunner(DynamicConfigLoader loader,
                                   ObjectProvider<List<ConfigChangedHandler>> handlerProvider) {
        this.loader = loader;
        this.handlers = handlerProvider.getIfAvailable(ArrayList::new)
            .stream()
            .sorted(AnnotationAwareOrderComparator.INSTANCE)
            .collect(Collectors.toList());
    }

    public void start() {
        if (started) {
            log.warn("配置监听线程已启动, 忽略重复启动");
            return;
        }
        started = true;

        // 初始化当前配置
        currentConfig.set(loader.loadCurrentEnvironmentConfig());
        // 启动监听线程
        final Thread watcherThread = new Thread(new ConfigFileWatcher(watched, this::handleChanged), "refresh-scope-watcher");
        watcherThread.setDaemon(true);
        watcherThread.start();
    }

    /** 提供对外注册监听文件路径的能力 */
    public void registerWatchedFile(String filePath) {
        if (StrUtil.isNotBlank(filePath)) {
            watched.add(filePath);
            log.info("注册新的配置文件监听路径: {}", filePath);
        }
    }

    /**
     * 回调配置变更处理逻辑
     *
     * @param changedFile 更改文件
     */
    private void handleChanged(String changedFile) {
        Map<String, Object> latest = loader.loadCurrentEnvironmentConfig();
        ConfigDiffer.DiffResult diff = ConfigDiffer.diff(currentConfig.get(), latest);

        if (!diff.hasDiff) {
            log.debug("配置无变化");
            return;
        }

        currentConfig.set(latest);
        log.warn("检测到配置文件发生变更: {} -> {}", changedFile, diff.changedKeys);

        for (ConfigChangedHandler handler : handlers) {
            try {
                handler.onChanged(changedFile, diff.changedKeys, latest);
            } catch (Exception e) {
                log.error("处理配置变更失败: {}", handler.getClass(), e);
            }
        }
    }
}
