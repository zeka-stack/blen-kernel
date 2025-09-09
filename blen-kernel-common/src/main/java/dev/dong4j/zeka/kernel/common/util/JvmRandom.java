package dev.dong4j.zeka.kernel.common.util;

import java.io.Serial;
import java.util.Random;
import org.jetbrains.annotations.Contract;

/**
 * JVM共享随机数生成器，基于系统全局随机对象的线程安全包装器
 * <p>
 * 该类是一个专门设计的Random包装器，通过全局共享的随机种子对象提供随机数生成服务
 * 允许在类的所有成员之间共享随机种子，更准确的名称应该是“共享种子随机数生成器”
 * 专门优化了nextInt(int)和nextLong()方法，确保生成的数值始终为非负数
 * <p>
 * 主要特性：
 * - 共享种子：所有实例共享同一个随机种子，确保一致性
 * - 线程安全：底层使用线程安全的Random对象实现
 * - 非负数保证：所有生成的整数和长整数都是非负数
 * - 限制传统操作：禁用了一些可能破坏共享状态的方法
 * - 优化性能：避免了频繁创建随机数生成器的开销
 * <p>
 * 核心实现亮点：
 * <p>
 * <b>全局共享随机源</b>：
 * - 使用静态的SHARED_RANDOM对象作为随机数源
 * - 所有JvmRandom实例共享同一个随机种子
 * - 避免了多个随机数生成器之间的不一致性
 * <p>
 * <b>种子保护机制</b>：
 * - 禁用setSeed()方法，防止意外修改共享种子
 * - 通过constructed标志位控制种子设置权限
 * - 确保随机数序列的可预测性和一致性
 * <p>
 * <b>非负数保证</b>：
 * - nextInt()和nextInt(int)生成[0, max)范围的整数
 * - nextLong()生成[0, Long.MAX_VALUE)范围的长整数
 * - 通过位操作去除符号位，确保结果非负
 * <p>
 * 支持的随机数类型：
 * - <b>整数</b>：nextInt() 和 nextInt(int)，生成非负整数
 * - <b>长整数</b>：nextLong()，生成非负长整数
 * - <b>布尔值</b>：nextBoolean()，随机真/假值
 * - <b>浮点数</b>：nextFloat() 和 nextDouble()，[0.0, 1.0)范围
 * <p>
 * 不支持的操作（安全限制）：
 * - <b>setSeed(long)</b>：禁止修改共享种子
 * - <b>nextBytes(byte[])</b>：不支持字节数组填充
 * - <b>nextGaussian()</b>：不支持高斯分布随机数
 * <p>
 * 应用场景：
 * - 需要全局一致的随机数序列的应用
 * - 多线程环境下的随机数生成
 * - 需要确保随机数为非负数的场景
 * - 分布式系统中的节点标识生成
 * - 游戏开发中的随机事件生成
 * <p>
 * 使用示例：
 * <pre>
 * // 创建实例
 * JvmRandom random = new JvmRandom();
 *
 * // 生成非负整数
 * int positiveInt = random.nextInt();
 * int boundedInt = random.nextInt(100);
 *
 * // 生成非负长整数
 * long positiveLong = random.nextLong();
 *
 * // 生成其他类型
 * boolean flag = random.nextBoolean();
 * float f = random.nextFloat();
 * double d = random.nextDouble();
 * </pre>
 * <p>
 * 性能和安全考虑：
 * - 线程安全：底层Random对象是线程安全的
 * - 性能优化：避免了频繁创建随机数生成器的开销
 * - 内存效率：所有实例共享同一个底层对象
 * - 可预测性：全局共享种子确保随机数序列的一致性
 * <p>
 * 注意事项：
 * - 不适用于对随机性要求极高的加密场景
 * - 在需要完全独立随机数序列的情况下不适用
 * - setSeed操作被禁用，无法重现特定的随机数序列
 * - 部分随机数方法被禁用，功能有一定限制
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 14:54
 * @since 1.0.0
 */
public final class JvmRandom extends Random {

    /** serialVersionUID */
    @Serial
    private static final long serialVersionUID = 1L;
    /** SHARED_RANDOM */
    private static final Random SHARED_RANDOM = new Random();
    /** Constructed */
    private final boolean constructed;

    /**
     * Constructs a new instance.
     *
     * @since 1.0.0
     */
    JvmRandom() {
        this.constructed = true;
    }

    /**
     * Unsupported in 2.0.
     *
     * @param seed ignored
     * @since 1.0.0
     */
    @Contract(pure = true)
    @Override
    public synchronized void setSeed(long seed) {
        if (this.constructed) {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Unsupported in 2.0.
     *
     * @param byteArray ignored
     * @since 1.0.0
     */
    @Contract("_ -> fail")
    @Override
    public void nextBytes(byte[] byteArray) {
        throw new UnsupportedOperationException();
    }

    /**
     * <p>Returns the next pseudorandom, uniformly distributed int value from the Math.random() sequence.</p> Identical
     * to <code>nextInt(Integer.MAX_VALUE)</code> <p> N.B. All values are >= 0. </p>
     *
     * @return the random int
     * @since 1.0.0
     */
    @Override
    public int nextInt() {
        return this.nextInt(Integer.MAX_VALUE);
    }

    /**
     * <p>Returns a pseudorandom, uniformly distributed int value between <code>0</code> (inclusive) and the specified
     * value (exclusive), from the Math.random() sequence.</p>
     *
     * @param n the specified exclusive max-value
     * @return the random int
     * @throws IllegalArgumentException when <code>n &lt;= 0</code>
     * @since 1.0.0
     */
    @Override
    public int nextInt(int n) {
        return SHARED_RANDOM.nextInt(n);
    }

    /**
     * <p>Returns the next pseudorandom, uniformly distributed long value from the Math.random() sequence.</p> Identical
     * to <code>nextLong(Long.MAX_VALUE)</code> <p> N.B. All values are >= 0. </p>
     *
     * @return the random long
     * @since 1.0.0
     */
    @Override
    public long nextLong() {
        return nextLong(Long.MAX_VALUE);
    }

    /**
     * Get the next unsigned random long
     *
     * @return unsigned random long
     * @since 1.0.0
     */
    private static long next63bits() {
        // drop the sign bit to leave 63 random bits
        return SHARED_RANDOM.nextLong() & 0x7fffffffffffffffL;
    }

    /**
     * Count the number of bits required to represent a long number.
     *
     * @param num long number
     * @return number of bits required
     * @since 1.0.0
     */
    @Contract(pure = true)
    private static int bitsRequired(long num) {
        // Derived from Hacker's Delight, Figure 5-9
        long y = num;
        int n = 0;
        while (true) {
            // 64 = number of bits in a long
            if (num < 0) {
                return 64 - n;
            }
            if (y == 0) {
                return n;
            }
            n++;
            num = num << 1;
            y = y >> 1;
        }
    }

    /**
     * <p>Returns the next pseudorandom, uniformly distributed boolean value from the Math.random() sequence.</p>
     *
     * @return the random boolean
     * @since 1.0.0
     */
    @Override
    public boolean nextBoolean() {
        return SHARED_RANDOM.nextBoolean();
    }

    /**
     * <p>Returns the next pseudorandom, uniformly distributed float value between <code>0.0</code> and <code>1.0</code>
     * from the Math.random() sequence.</p>
     *
     * @return the random float
     * @since 1.0.0
     */
    @Override
    public float nextFloat() {
        return SHARED_RANDOM.nextFloat();
    }

    /**
     * <p>Synonymous to the Math.random() call.</p>
     *
     * @return the random double
     * @since 1.0.0
     */
    @Override
    public double nextDouble() {
        return SHARED_RANDOM.nextDouble();
    }

    /**
     * Next gaussian double
     *
     * @return the double
     * @since 1.0.0
     */
    @Contract(" -> fail")
    @Override
    public synchronized double nextGaussian() {
        throw new UnsupportedOperationException();
    }
}
