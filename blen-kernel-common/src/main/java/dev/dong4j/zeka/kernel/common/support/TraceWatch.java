package dev.dong4j.zeka.kernel.common.support;

import org.jetbrains.annotations.NotNull;
import org.springframework.util.StopWatch;

import java.text.NumberFormat;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2021.05.11 15:19
 * @since 1.8.0
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
     * @since 1.8.0
     */
    public TraceWatch startings(String taskName) throws IllegalStateException {
        super.start(taskName);
        return this;
    }

    /**
     * {@link AutoCloseable} 自动关闭
     *
     * @since 1.8.0
     */
    @Override
    public void close() {
        super.stop();
    }

    /**
     * Sets keep task list *
     *
     * @param keepTaskList keep task list
     * @since 1.8.0
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
     * @since 1.8.0
     */
    @Override
    public @NotNull String shortSummary() {
        return "StopWatch '" + this.getId() + "': running time = " + this.getTotalTimeMillis() + " ms";
    }

    /**
     * Pretty print
     *
     * @return the string
     * @since 1.8.0
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
