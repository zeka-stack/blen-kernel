package dev.dong4j.zeka.kernel.spi;

import java.io.IOException;
import java.io.Writer;

/**
 * 非线程安全的字符串写入器，提供高性能的字符串写入操作
 * <p>
 * 该类继承自Writer，使用StringBuilder作为内部缓冲区
 * 专门用于单线程环境下的高性能字符串构建操作
 * <p>
 * 主要特性：
 * - 非线程安全，避免同步开销，提供更好的性能
 * - 基于StringBuilder实现，内存占用小
 * - 支持字符、字符数组、字符串的写入操作
 * - 支持CharSequence的追加操作
 * <p>
 * 注意：由于是非线程安全的实现，不应在多线程环境中使用
 * 如需线程安全的实现，请使用StringWriter类
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2021.02.26 17:47
 * @since 1.0.0
 */
@SuppressWarnings("all")
public class UnsafeStringWriter extends Writer {
    /** 内部字符串缓冲区，用于存储写入的字符数据 */
    private StringBuilder mBuffer;

    /**
     * 构造一个新的非线程安全字符串写入器
     * <p>
     * 使用默认初始容量创建内部StringBuilder缓冲区
     * 同时将缓冲区设置为同步锁对象
     *
     * @since 1.0.0
     */
    public UnsafeStringWriter() {
        lock = mBuffer = new StringBuilder();
    }

    /**
     * 构造一个指定初始容量的非线程安全字符串写入器
     * <p>
     * 使用指定的初始容量创建内部StringBuilder缓冲区
     * 注意：实际实现中忽略了size参数，始终使用默认容量
     *
     * @param size 初始容量大小（实际未使用）
     * @throws IllegalArgumentException 如果size为负数
     * @since 1.0.0
     */
    public UnsafeStringWriter(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("Negative buffer size");
        }

        lock = mBuffer = new StringBuilder();
    }

    /**
     * 写入单个字符
     * <p>
     * 将指定的字符追加到内部缓冲区中
     * 字符会被转换为char类型后写入
     *
     * @param c 要写入的字符（作为int传入）
     * @since 1.0.0
     */
    @Override
    public void write(int c) {
        mBuffer.append((char) c);
    }

    /**
     * 写入字符数组
     * <p>
     * 将整个字符数组的内容追加到内部缓冲区中
     * 等同于调用write(cs, 0, cs.length)
     *
     * @param cs 要写入的字符数组
     * @throws IOException 如果发生I/O错误（实际不会抛出）
     * @since 1.0.0
     */
    @Override
    public void write(char[] cs) throws IOException {
        mBuffer.append(cs, 0, cs.length);
    }

    /**
     * 写入字符数组的指定部分
     * <p>
     * 从字符数组的指定偏移位置开始，写入指定长度的字符到内部缓冲区
     * 会进行边界检查，确保不会越界访问
     *
     * @param cs  源字符数组
     * @param off 起始偏移位置
     * @param len 要写入的字符长度
     * @throws IOException             如果发生I/O错误（实际不会抛出）
     * @throws IndexOutOfBoundsException 如果偏移或长度参数无效
     * @since 1.0.0
     */
    @Override
    public void write(char[] cs, int off, int len) throws IOException {
        if ((off < 0) || (off > cs.length) || (len < 0) ||
            ((off + len) > cs.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        }

        if (len > 0) {
            mBuffer.append(cs, off, len);
        }
    }

    /**
     * 写入字符串
     * <p>
     * 将整个字符串追加到内部缓冲区中
     * 如果字符串为null，会将"null"字符串写入
     *
     * @param str 要写入的字符串
     * @since 1.0.0
     */
    @Override
    public void write(String str) {
        mBuffer.append(str);
    }

    /**
     * 写入字符串的指定部分
     * <p>
     * 从字符串的指定偏移位置开始，写入指定长度的字符到内部缓冲区
     * 实际调用StringBuilder.append(str, off, off + len)方法
     *
     * @param str 源字符串
     * @param off 起始偏移位置
     * @param len 要写入的字符长度
     * @since 1.0.0
     */
    @Override
    public void write(String str, int off, int len) {
        mBuffer.append(str, off, off + len);
    }

    /**
     * 追加字符序列
     * <p>
     * 将指定的字符序列追加到内部缓冲区中
     * 如果字符序列为null，会将"null"字符串写入
     *
     * @param csq 要追加的字符序列
     * @return 当前Writer实例，支持链式调用
     * @since 1.0.0
     */
    @Override
    public Writer append(CharSequence csq) {
        if (csq == null) {
            write("null");
        } else {
            write(csq.toString());
        }
        return this;
    }

    /**
     * 追加字符序列的指定部分
     * <p>
     * 从字符序列的指定起始位置到结束位置，追加子序列到内部缓冲区
     * 如果字符序列为null，会使用"null"字符串代替
     *
     * @param csq   源字符序列
     * @param start 起始位置（包含）
     * @param end   结束位置（不包含）
     * @return 当前Writer实例，支持链式调用
     * @since 1.0.0
     */
    @Override
    public Writer append(CharSequence csq, int start, int end) {
        CharSequence cs = (csq == null ? "null" : csq);
        write(cs.subSequence(start, end).toString());
        return this;
    }

    /**
     * 追加单个字符
     * <p>
     * 将指定的字符追加到内部缓冲区中
     * 这是一个便捷的字符追加方法
     *
     * @param c 要追加的字符
     * @return 当前Writer实例，支持链式调用
     * @since 1.0.0
     */
    @Override
    public Writer append(char c) {
        mBuffer.append(c);
        return this;
    }

    /**
     * 关闭写入器
     * <p>
     * 该方法为空实现，不执行任何操作
     * 因为StringBuilder不需要显式关闭资源
     *
     * @since 1.0.0
     */
    @Override
    public void close() {
    }

    /**
     * 刷新写入器
     * <p>
     * 该方法为空实现，不执行任何操作
     * 因为StringBuilder是内存操作，不需要刷新
     *
     * @since 1.0.0
     */
    @Override
    public void flush() {
    }

    /**
     * 返回字符串表示
     * <p>
     * 将内部缓冲区中的所有内容转换为字符串返回
     * 这是获取最终写入结果的主要方法
     *
     * @return 缓冲区中的所有内容组成的字符串
     * @since 1.0.0
     */
    @Override
    public String toString() {
        return mBuffer.toString();
    }
}
