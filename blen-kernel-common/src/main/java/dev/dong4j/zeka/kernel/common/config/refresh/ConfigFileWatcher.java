package dev.dong4j.zeka.kernel.common.config.refresh;

import dev.dong4j.zeka.kernel.common.util.ConfigKit;
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
import lombok.extern.slf4j.Slf4j;

/**
 * 4: 实时监听配置文件变更，并判断是否与当前环境匹配（如 prod 环境监听 application-prod.yml，dev 环境不处理）。
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2025.07.29
 * @since 1.0.0
 */
@Slf4j
public class ConfigFileWatcher implements Runnable {
    private final Consumer<String> onConfigChange;
    private final Set<String> watchedFiles;

    public ConfigFileWatcher(Set<String> watchedFiles,
                             Consumer<String> onConfigChange) {
        this.onConfigChange = onConfigChange;
        this.watchedFiles = watchedFiles;
    }

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
