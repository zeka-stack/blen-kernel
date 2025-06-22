package dev.dong4j.zeka.kernel.spi;

import java.io.IOException;
import java.io.Writer;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.8.0
 * @email "mailto:dong4j@gmaidl.com"
 * @date 2021.02.26 17:47
 * @since 1.8.0
 */
@SuppressWarnings("all")
public class UnsafeStringWriter extends Writer {
    /** M buffer */
    private StringBuilder mBuffer;

    /**
     * Unsafe string writer
     *
     * @since 1.8.0
     */
    public UnsafeStringWriter() {
        lock = mBuffer = new StringBuilder();
    }

    /**
     * Unsafe string writer
     *
     * @param size size
     * @since 1.8.0
     */
    public UnsafeStringWriter(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("Negative buffer size");
        }

        lock = mBuffer = new StringBuilder();
    }

    /**
     * Write
     *
     * @param c c
     * @since 1.8.0
     */
    @Override
    public void write(int c) {
        mBuffer.append((char) c);
    }

    /**
     * Write
     *
     * @param cs cs
     * @throws IOException io exception
     * @since 1.8.0
     */
    @Override
    public void write(char[] cs) throws IOException {
        mBuffer.append(cs, 0, cs.length);
    }

    /**
     * Write
     *
     * @param cs  cs
     * @param off off
     * @param len len
     * @throws IOException io exception
     * @since 1.8.0
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
     * Write
     *
     * @param str str
     * @since 1.8.0
     */
    @Override
    public void write(String str) {
        mBuffer.append(str);
    }

    /**
     * Write
     *
     * @param str str
     * @param off off
     * @param len len
     * @since 1.8.0
     */
    @Override
    public void write(String str, int off, int len) {
        mBuffer.append(str, off, off + len);
    }

    /**
     * Append
     *
     * @param csq csq
     * @return the writer
     * @since 1.8.0
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
     * Append
     *
     * @param csq   csq
     * @param start start
     * @param end   end
     * @return the writer
     * @since 1.8.0
     */
    @Override
    public Writer append(CharSequence csq, int start, int end) {
        CharSequence cs = (csq == null ? "null" : csq);
        write(cs.subSequence(start, end).toString());
        return this;
    }

    /**
     * Append
     *
     * @param c c
     * @return the writer
     * @since 1.8.0
     */
    @Override
    public Writer append(char c) {
        mBuffer.append(c);
        return this;
    }

    /**
     * Close
     *
     * @since 1.8.0
     */
    @Override
    public void close() {
    }

    /**
     * Flush
     *
     * @since 1.8.0
     */
    @Override
    public void flush() {
    }

    /**
     * To string
     *
     * @return the string
     * @since 1.8.0
     */
    @Override
    public String toString() {
        return mBuffer.toString();
    }
}
