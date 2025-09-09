package dev.dong4j.zeka.kernel.common.support;

import java.text.NumberFormat;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.StopWatch;

/**
 * <p>增强版的性能监控计时器.
 * <p>继承自 Spring 的 StopWatch，提供更丰富的功能和更便捷的使用方式.
 * <p>实现了 AutoCloseable 接口，支持 try-with-resources 语法的自动资源管理.
 * <p>主要功能：
 * <ul>
 *     <li>高精度的执行时间监控和统计</li>
 *     <li>多任务的分段计时和性能分析</li>
 *     <li>自动资源管理和安全关闭</li>
 *     <li>友好的日志输出格式</li>
 * </ul>
 * <p>增强特性：
 * <ul>
 *     <li>支持链式调用的流式 API</li>
 *     <li>提供自定义的美化输出格式</li>
 *     <li>可配置的任务信息保留策略</li>
 * </ul>
 * <p>使用场景：
 * <ul>
 *     <li>方法执行时间的性能监控</li>
 *     <li>复杂业务流程的性能分析</li>
 *     <li>系统瓶颈识别和调优</li>
 *     <li>开发阶段的性能调试</li>
 * </ul>
 * <p>使用示例：
 * <pre>{@code
 * try (TraceWatch watch = new TraceWatch("business-process")) {
 *     watch.startings("step1");
 *     // 执行业务逻辑 1
 *     watch.stop();
 *
 *     watch.startings("step2");
 *     // 执行业务逻辑 2
 *     // 自动关闭时会停止计时
 * }
 * }</pre>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2021.05.11 15:19
 * @since 1.0.0
 */
public class TraceWatch extends StopWatch implements AutoCloseable {
    /** Keep task list */
    private boolean keepTaskList = true;

    /**
     * Startings
     *
     * @param taskName task name
     * @return the trace watch
     * @throws IllegalStateException illegal state exception
     * @since 1.0.0
     */
    public TraceWatch startings(String taskName) throws IllegalStateException {
        super.start(taskName);
        return this;
    }

    /**
     * {@link AutoCloseable} 自动关闭
     *
     * @since 1.0.0
     */
    @Override
    public void close() {
        super.stop();
    }

    /**
     * Sets keep task list *
     *
     * @param keepTaskList keep task list
     * @since 1.0.0
     */
    @Override
    public void setKeepTaskList(boolean keepTaskList) {
        super.setKeepTaskList(keepTaskList);
        this.keepTaskList = keepTaskList;
    }

    /**
     * Short summary
     *
     * @return the string
     * @since 1.0.0
     */
    @Override
    public @NotNull String shortSummary() {
        return "StopWatch '" + this.getId() + "': running time = " + this.getTotalTimeMillis() + " ms";
    }

    /**
     * Pretty print
     *
     * @return the string
     * @since 1.0.0
     */
    @Override
    public @NotNull String prettyPrint() {
        StringBuilder sb = new StringBuilder(this.shortSummary());
        sb.append('\n');
        if (!this.keepTaskList) {
            sb.append("No task info kept");
        } else {
            sb.append("---------------------------------------------\n");
            sb.append("ms         %     Task name\n");
            sb.append("---------------------------------------------\n");
            NumberFormat nf = NumberFormat.getNumberInstance();
            nf.setMinimumIntegerDigits(9);
            nf.setGroupingUsed(false);
            NumberFormat pf = NumberFormat.getPercentInstance();
            pf.setMinimumIntegerDigits(3);
            pf.setGroupingUsed(false);
            for (TaskInfo task : this.getTaskInfo()) {
                sb.append(nf.format(task.getTimeMillis())).append("  ");
                sb.append(pf.format((double) task.getTimeMillis() / this.getTotalTimeMillis())).append("  ");
                sb.append(task.getTaskName()).append("\n");
            }
        }
        return sb.toString();
    }

}
