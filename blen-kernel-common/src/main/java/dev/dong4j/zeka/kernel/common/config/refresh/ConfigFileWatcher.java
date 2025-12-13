package dev.dong4j.zeka.kernel.common.config.refresh;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Set;
import java.util.function.Consumer;

import dev.dong4j.zeka.kernel.common.util.ConfigKit;
import lombok.extern.slf4j.Slf4j;

/**
 * 配置文件监控类
 * <p>
 * 用于监听指定目录下的配置文件变更事件, 当检测到配置文件被修改时, 触发回调通知
 *
 * @author zeka.stack.team
 * @version 1.0.0
 * @email "mailto:zeka.stack@gmail.com"
 * @date 2025.12.13
 * @since 2.0.0
 */
@Slf4j
public class ConfigFileWatcher implements Runnable {
    /**
     * 配置变更时的回调函数
     * <p>
     * 当配置发生变更时, 该回调将被触发并传入新的配置值
     *
     * @param configValue 新的配置值
     * @see Consumer
     */
    private final Consumer<String> onConfigChange;
    /**
     * 被监视的文件集合
     * <p>
     * 用于跟踪需要监听变化的文件路径
     */
    private final Set<String> watchedFiles;

    /**
     * 创建一个用于监控配置文件变化的监视器
     * <p>
     * 该构造函数初始化配置文件监视器, 指定需要监控的文件集合以及当配置文件发生变化时的回调函数
     *
     * @param watchedFiles   需要监控的配置文件名称集合
     * @param onConfigChange 当配置文件内容发生变化时触发的回调函数
     */
    public ConfigFileWatcher(Set<String> watchedFiles,
                             Consumer<String> onConfigChange) {
        this.onConfigChange = onConfigChange;
        this.watchedFiles = watchedFiles;
    }

    /**
     * 启动配置文件变更监控
     * <p>
     * 注册文件系统监视服务, 监听配置目录下的文件修改事件, 并在检测到配置文件变更时触发回调
     *
     * @throws IOException          当创建或使用 WatchService 时发生 I/O 错误
     * @throws InterruptedException 当线程在等待事件时被中断
     */
    @Override
    public void run() {
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            String configDir = ConfigKit.getConfigPath();
            Path configPath = Paths.get(configDir);
            configPath.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

            while (!Thread.currentThread().isInterrupted()) {
                WatchKey key = watchService.take();

                for (WatchEvent<?> event : key.pollEvents()) {
                    Path changed = (Path) event.context();
                    String filename = changed.toString();

                    if (watchedFiles.contains(filename)) {
                        log.info("[Watcher] 配置文件发生变更：{}", filename);
                        onConfigChange.accept(filename);
                    }
                }

                if (!key.reset()) {
                    break;
                }
            }
        } catch (IOException | InterruptedException e) {
            log.error("[Watcher] 配置文件监控异常: {}", e.getMessage());
            Thread.currentThread().interrupt(); // 恢复中断状态
        }
    }

}
