package dev.dong4j.zeka.kernel.common.support;

import dev.dong4j.zeka.kernel.common.util.StringPool;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.Nullable;

import java.io.Writer;

/**
 * <p>Description: 借助 StringBuilder 提供快读的字符串写出, 相比 jdk 的 StringWriter 非线程安全, 速度更快.  </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 14:47
 * @since 1.0.0
 */
public class FastStringWriter extends Writer {
    /** Builder */
    private final StringBuilder builder;

    /**
     * Instantiates a new Fast string writer.
     *
     * @since 1.0.0
     */
    public FastStringWriter() {
        this.builder = new StringBuilder(64);
    }

    /**
     * Instantiates a new Fast string writer.
     *
     * @param capacity the capacity
     * @since 1.0.0
     */
    public FastStringWriter(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Negative builderfer size");
        }
        this.builder = new StringBuilder(capacity);
    }

    /**
     * Instantiates a new Fast string writer.
     *
     * @param builder the builder
     * @since 1.0.0
     */
    public FastStringWriter(@Nullable StringBuilder builder) {
        this.builder = builder != null ? builder : new StringBuilder(64);
    }

    /**
     * Gets the underlying StringBuilder.
     *
     * @return StringBuilder builder
     * @since 1.0.0
     */
    public StringBuilder getBuilder() {
        return this.builder;
    }

    /**
     * To string string
     *
     * @return the string
     * @since 1.0.0
     */
    @Override
    public String toString() {
        return this.builder.toString();
    }

    /**
     * Write *
     *
     * @param c c
     * @since 1.0.0
     */
    @Override
    public void write(int c) {
        this.builder.append((char) c);
    }

    /**
     * Write *
     *
     * @param cbuilder cbuilder
     * @param off      off
     * @param len      len
     * @since 1.0.0
     */
    @Override
    public void write(char[] cbuilder, int off, int len) {
        if ((off < 0) || (off > cbuilder.length)
            || (len < 0) || ((off + len) > cbuilder.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }
        this.builder.append(cbuilder, off, len);
    }

    /**
     * Write *
     *
     * @param str str
     * @since 1.0.0
     */
    @Override
    public void write(String str) {
        this.builder.append(str);
    }

    /**
     * Write *
     *
     * @param str str
     * @param off off
     * @param len len
     * @since 1.0.0
     */
    @Override
    public void write(@NotNull String str, int off, int len) {
        this.builder.append(str, off, off + len);
    }

    /**
     * Append fast string writer
     *
     * @param csq csq
     * @return the fast string writer
     * @since 1.0.0
     */
    @Override
    public FastStringWriter append(CharSequence csq) {
        if (csq == null) {
            this.write(StringPool.NULL);
        } else {
            this.write(csq.toString());
        }
        return this;
    }

    /**
     * Append fast string writer
     *
     * @param csq   csq
     * @param start start
     * @param end   end
     * @return the fast string writer
     * @since 1.0.0
     */
    @Override
    public FastStringWriter append(CharSequence csq, int start, int end) {
        CharSequence cs = (csq == null ? StringPool.NULL : csq);
        this.write(cs.subSequence(start, end).toString());
        return this;
    }

    /**
     * Append fast string writer
     *
     * @param c c
     * @return the fast string writer
     * @since 1.0.0
     */
    @Override
    public FastStringWriter append(char c) {
        this.write(c);
        return this;
    }


    /**
     * Flush
     *
     * @since 1.0.0
     */
    @Override
    public void flush() {
    }

    /**
     * Close
     *
     * @since 1.0.0
     */
    @Override
    public void close() {
        this.builder.setLength(0);
        this.builder.trimToSize();
    }
}
