package dev.dong4j.zeka.kernel.common.config.refresh;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 配置文件监听运行器类
 * <p>
 * 负责监听配置文件的变化, 并在配置变更时加载最新配置并通知相关处理器进行处理. 适用于需要动态加载和响应配置变更的场景.
 *
 * @author zeka.stack.team
 * @version 1.0.0
 * @email "mailto:zeka.stack@gmail.com"
 * @date 2025.12.13
 * @since 2.0.0
 */
@Slf4j
public class ConfigFileWatcherRunner {

    /**
     * 被监视的项目集合
     * <p>
     * 使用 ConcurrentHashMap.newKeySet() 创建的线程安全集合, 用于存储当前被监视的项目名称.
     */
    private final Set<String> watched = ConcurrentHashMap.newKeySet();
    /**
     * 动态配置加载器
     * <p>
     * 用于加载和管理动态配置数据
     */
    private final DynamicConfigLoader loader;
    /**
     * 配置变更处理器列表
     * <p>
     * 用于存储所有注册的配置变更监听器
     */
    private final List<ConfigChangedHandler> handlers;

    /**
     * 当前配置信息的原子引用
     * <p>
     * 用于线程安全地存储和更新配置数据
     */
    private final AtomicReference<Map<String, Object>> currentConfig = new AtomicReference<>();
    /** 标记服务是否已启动 */
    private volatile boolean started = false;

    /**
     * 构造一个 ConfigFileWatcherRunner 实例
     * <p>
     * 使用指定的配置加载器和配置变更处理器提供者初始化配置文件监视器运行器
     *
     * @param loader          配置加载器, 用于加载动态配置
     * @param handlerProvider 配置变更处理器的提供者, 用于获取配置变更处理器列表
     * @throws 无明确抛出异常, 但如果 handlerProvider.getIfAvailable 返回 null, 可能会导致后续操作失败
     */
    public ConfigFileWatcherRunner(DynamicConfigLoader loader,
                                   ObjectProvider<List<ConfigChangedHandler>> handlerProvider) {
        this.loader = loader;
        this.handlers = handlerProvider.getIfAvailable(ArrayList::new)
            .stream()
            .sorted(AnnotationAwareOrderComparator.INSTANCE)
            .collect(Collectors.toList());
    }

    /**
     * 启动配置监听线程
     * <p>
     * 如果配置监听线程已启动, 则记录警告日志并返回. 否则, 标记为已启动, 并加载当前环境配置, 创建并启动配置文件监听线程.
     *
     * @param 无参数
     * @throws 无异常抛出
     */
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

    /**
     * 注册需要监听的配置文件路径
     * <p>
     * 如果提供的文件路径非空, 则将其添加到监听列表中, 并记录日志信息
     *
     * @param filePath 需要监听的配置文件路径
     */
    public void registerWatchedFile(String filePath) {
        if (StrUtil.isNotBlank(filePath)) {
            watched.add(filePath);
            log.info("注册新的配置文件监听路径: {}", filePath);
        }
    }

    /**
     * 处理配置文件变更
     * <p>
     * 加载当前环境的最新配置, 与当前配置进行比较. 如果存在差异, 则更新当前配置, 并通知所有配置变更处理器进行处理.
     *
     * @param changedFile 发生变更的配置文件路径
     * @throws Exception 如果处理配置变更过程中发生异常, 将记录错误日志但不会中断处理流程
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
