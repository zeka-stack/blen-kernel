package dev.dong4j.zeka.kernel.common.util;

import dev.dong4j.zeka.kernel.common.asserts.Assertions;
import dev.dong4j.zeka.kernel.common.enums.RandomType;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * <p>Description: </p>
 * <p><code>RandomUtils</code> is a wrapper that supports all possible {@link Random} methods via the {@link
 * Math#random()}****** method and its system-wide <code>Random</code> object.
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:17
 * @since 1.0.0
 */
@SuppressWarnings("checkstyle:MethodLimit")
@UtilityClass
public class RandomUtils {

    /** S_INT */
    private static final String S_INT = "0123456789";
    /** S_STR */
    private static final String S_STR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    /** S_ALL */
    private static final String S_ALL = S_INT + S_STR;
    /** UNICODE_START */
    private static final int UNICODE_START = 19968;
    /** UNICODE_END */
    private static final int UNICODE_END = 40864;
    /** An instance of {@link JvmRandom}. */
    private static final Random JVM_RANDOM = new JvmRandom();

    /**
     * <p>Returns the next pseudorandom, uniformly distributed int value from the Math.random() sequence.</p> N.B.
     * All values are >= 0.
     *
     * @return the random int
     * @since 1.0.0
     */
    public static int nextInt() {
        return nextInt(JVM_RANDOM);
    }

    /**
     * <p>Returns the next pseudorandom, uniformly distributed int value from the given <code>random</code>
     * sequence.</p>
     *
     * @param random the Random sequence generator.
     * @return the random int
     * @since 1.0.0
     */
    public static int nextInt(@NotNull Random random) {
        return random.nextInt();
    }

    /**
     * <p>Returns a pseudorandom, uniformly distributed int value between <code>0</code> (inclusive) and the specified
     * value (exclusive), from the Math.random() sequence.</p>
     *
     * @param n the specified exclusive max-value
     * @return the random int
     * @since 1.0.0
     */
    public static int nextInt(int n) {
        return nextInt(JVM_RANDOM, n);
    }

    /**
     * <p>Returns a pseudorandom, uniformly distributed int value between <code>0</code> (inclusive) and the specified
     * value (exclusive), from the given Random sequence.</p>
     *
     * @param random the Random sequence generator.
     * @param n      the specified exclusive max-value
     * @return the random int
     * @since 1.0.0
     */
    public static int nextInt(@NotNull Random random, int n) {
        // check this cannot return 'n'
        return random.nextInt(n);
    }

    /**
     * <p>Returns the next pseudorandom, uniformly distributed long value from the Math.random() sequence.</p> N.B.
     * All values are >= 0.
     *
     * @return the random long
     * @since 1.0.0
     */
    public static long nextLong() {
        return nextLong(JVM_RANDOM);
    }

    /**
     * <p>Returns the next pseudorandom, uniformly distributed long value from the given Random sequence.</p>
     *
     * @param random the Random sequence generator.
     * @return the random long
     * @since 1.0.0
     */
    public static long nextLong(@NotNull Random random) {
        return random.nextLong();
    }

    /**
     * <p>Returns the next pseudorandom, uniformly distributed boolean value from the Math.random() sequence.</p>
     *
     * @return the random boolean
     * @since 1.0.0
     */
    public static boolean nextBoolean() {
        return nextBoolean(JVM_RANDOM);
    }

    /**
     * <p>Returns the next pseudorandom, uniformly distributed boolean value from the given random sequence.</p>
     *
     * @param random the Random sequence generator.
     * @return the random boolean
     * @since 1.0.0
     */
    public static boolean nextBoolean(@NotNull Random random) {
        return random.nextBoolean();
    }

    /**
     * <p>Returns the next pseudorandom, uniformly distributed float value between <code>0.0</code> and <code>1.0</code>
     * from the Math.random() sequence.</p>
     *
     * @return the random float
     * @since 1.0.0
     */
    public static float nextFloat() {
        return nextFloat(JVM_RANDOM);
    }

    /**
     * <p>Returns the next pseudorandom, uniformly distributed float value between <code>0.0</code> and <code>1.0</code>
     * from the given Random sequence.</p>
     *
     * @param random the Random sequence generator.
     * @return the random float
     * @since 1.0.0
     */
    public static float nextFloat(@NotNull Random random) {
        return random.nextFloat();
    }

    /**
     * <p>Returns the next pseudorandom, uniformly distributed float value between <code>0.0</code> and <code>1.0</code>
     * from the Math.random() sequence.</p>
     *
     * @return the random double
     * @since 1.0.0
     */
    public static double nextDouble() {
        return nextDouble(JVM_RANDOM);
    }

    /**
     * <p>Returns the next pseudorandom, uniformly distributed float value between <code>0.0</code> and <code>1.0</code>
     * from the given Random sequence.</p>
     *
     * @param random the Random sequence generator.
     * @return the random double
     * @since 1.0.0
     */
    public static double nextDouble(@NotNull Random random) {
        return random.nextDouble();
    }

    /**
     * Next int int.
     *
     * @param startInclusive the start inclusive
     * @param endExclusive   the end exclusive
     * @return the int
     * @since 1.0.0
     */
    public static int nextInt(int startInclusive, int endExclusive) {
        return startInclusive + JVM_RANDOM.nextInt(endExclusive - startInclusive);
    }

    /**
     * Next long long.
     *
     * @param startInclusive the start inclusive
     * @param endExclusive   the end exclusive
     * @return the long
     * @since 1.0.0
     */
    public static long nextLong(long startInclusive, long endExclusive) {
        return (long) nextDouble(startInclusive, endExclusive);
    }

    /**
     * Next double double.
     *
     * @param startInclusive the start inclusive
     * @param endInclusive   the end inclusive
     * @return the double
     * @since 1.0.0
     */
    public static double nextDouble(double startInclusive, double endInclusive) {
        return startInclusive + ((endInclusive - startInclusive) * JVM_RANDOM.nextDouble());
    }

    /**
     * Next float float.
     *
     * @param startInclusive the start inclusive
     * @param endInclusive   the end inclusive
     * @return the float
     * @since 1.0.0
     */
    public static float nextFloat(float startInclusive, float endInclusive) {
        return startInclusive + ((endInclusive - startInclusive) * JVM_RANDOM.nextFloat());
    }

    /**
     * Next size int.
     *
     * @param startInclusive the start inclusive
     * @param endInclusive   the end inclusive
     * @return the int
     * @since 1.0.0
     */
    public static int nextSize(int startInclusive, int endInclusive) {
        return startInclusive + JVM_RANDOM.nextInt(endInclusive - startInclusive + 1);
    }

    /**
     * 随机数生成
     *
     * @param count 字符长度
     * @return 随机数 string
     * @since 1.0.0
     */
    @NotNull
    public static String random(int count) {
        return RandomUtils.random(count, RandomType.ALL);
    }

    /**
     * 随机数生成
     *
     * @param count      字符长度
     * @param randomType 随机数类别
     * @return 随机数 string
     * @since 1.0.0
     */
    @NotNull
    public static String random(int count, RandomType randomType) {
        if (count == 0) {
            return "";
        }
        Assertions.isTrue(count > 0, "Requested random string length " + count + " is less than 0.");
        ThreadLocalRandom random = ThreadLocalRandom.current();
        char[] buffer = new char[count];
        for (int i = 0; i < count; i++) {
            if (RandomType.INT == randomType) {
                buffer[i] = S_INT.charAt(random.nextInt(S_INT.length()));
            } else if (RandomType.STRING == randomType) {
                buffer[i] = S_STR.charAt(random.nextInt(S_STR.length()));
            } else {
                buffer[i] = S_ALL.charAt(random.nextInt(S_ALL.length()));
            }
        }
        return new String(buffer);
    }

    /**
     * Gets long more than zero less than *
     *
     * @param n n
     * @return the long more than zero less than
     * @since 1.0.0
     */
    static long getLongMoreThanZeroLessThan(long n) {
        long res = getLongLessThan(n);
        while (res <= 0L) {
            res = getLongLessThan(n);
        }

        return res;
    }

    /**
     * Gets long less than *
     *
     * @param n n
     * @return the long less than
     * @since 1.0.0
     */
    static long getLongLessThan(long n) {
        long res = JVM_RANDOM.nextLong();
        return res % n;
    }

    /**
     * Gets long between *
     *
     * @param n n
     * @param m m
     * @return the long between
     * @since 1.0.0
     */
    static long getLongBetween(long n, long m) {
        if (m <= n) {
            return n;
        } else {
            long res = getLongMoreThanZero();
            return n + res % (m - n);
        }
    }

    /**
     * Gets long more than zero *
     *
     * @return the long more than zero
     * @since 1.0.0
     */
    static long getLongMoreThanZero() {
        long res = JVM_RANDOM.nextLong();
        while (res <= 0L) {
            res = JVM_RANDOM.nextLong();
        }

        return res;
    }

    /**
     * Gets integer less than *
     *
     * @param n n
     * @return the integer less than
     * @since 1.0.0
     */
    static int getIntegerLessThan(int n) {
        int res = JVM_RANDOM.nextInt();
        return res % n;
    }

    /**
     * Gets string with number *
     *
     * @param n n
     * @return the string with number
     * @since 1.0.0
     */
    static @NotNull String getStringWithNumber(int n) {
        int[] arg = new int[]{48, 58};
        return getString(n, arg);
    }

    /**
     * Gets string *
     *
     * @param n   n
     * @param arg arg
     * @return the string
     * @since 1.0.0
     */
    private static @NotNull String getString(int n, int[] arg) {
        StringBuilder res = new StringBuilder();

        for (int i = 0; i < n; ++i) {
            res.append(getChar(arg));
        }

        return res.toString();
    }

    /**
     * Gets char *
     *
     * @param arg arg
     * @return the char
     * @since 1.0.0
     */
    private static char getChar(int @NotNull [] arg) {
        int size = arg.length;
        int c = JVM_RANDOM.nextInt(size / 2);
        c *= 2;
        return (char) getIntegerBetween(arg[c], arg[c + 1]);
    }

    /**
     * Gets integer between *
     *
     * @param n n
     * @param m m
     * @return the integer between
     * @since 1.0.0
     */
    static int getIntegerBetween(int n, int m) {
        if (m == n) {
            return n;
        } else {
            int res = getIntegerMoreThanZero();
            return n + res % (m - n);
        }
    }

    /**
     * Gets integer more than zero *
     *
     * @return the integer more than zero
     * @since 1.0.0
     */
    static int getIntegerMoreThanZero() {
        int res = JVM_RANDOM.nextInt();
        while (res <= 0) {
            res = JVM_RANDOM.nextInt();
        }

        return res;
    }

    /**
     * Gets random string *
     *
     * @param length length
     * @return the random string
     * @since 1.0.0
     */
    static @NotNull String getRandomString(int length) {
        StringBuilder buffer = new StringBuilder(S_ALL);
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int range = buffer.length();

        for (int i = 0; i < length; ++i) {
            sb.append(buffer.charAt(random.nextInt(range)));
        }

        return sb.toString();
    }

    /**
     * Gets string shorten than *
     *
     * @param n n
     * @return the string shorten than
     * @since 1.0.0
     */
    static @NotNull String getStringShortenThan(int n) {
        int len = getIntegerMoreThanZeroLessThan(n);
        return getStringWithCharacter(len);
    }

    /**
     * Gets integer more than zero less than *
     *
     * @param n n
     * @return the integer more than zero less than
     * @since 1.0.0
     */
    static int getIntegerMoreThanZeroLessThan(int n) {
        int res = JVM_RANDOM.nextInt(n);
        while (res == 0) {
            res = JVM_RANDOM.nextInt(n);
        }

        return res;
    }

    /**
     * Gets string with character *
     *
     * @param n n
     * @return the string with character
     * @since 1.0.0
     */
    static @NotNull String getStringWithCharacter(int n) {
        int[] arg = new int[]{97, 123, 65, 91};
        return getString(n, arg);
    }

    /**
     * Gets string with num and cha shorten than *
     *
     * @param n n
     * @return the string with num and cha shorten than
     * @since 1.0.0
     */
    static @NotNull String getStringWithNumAndChaShortenThan(int n) {
        int len = getIntegerMoreThanZeroLessThan(n);
        return getStringWithNumAndCha(len);
    }

    /**
     * Gets string with num and cha *
     *
     * @param n n
     * @return the string with num and cha
     * @since 1.0.0
     */
    static @NotNull String getStringWithNumAndCha(int n) {
        int[] arg = new int[]{97, 123, 65, 91, 48, 58};
        return getString(n, arg);
    }

    /**
     * Gets string between *
     *
     * @param n n
     * @param m m
     * @return the string between
     * @since 1.0.0
     */
    static @NotNull String getStringBetween(int n, int m) {
        int len = getIntegerBetween(n, m);
        return getStringWithCharacter(len);
    }

    /**
     * Gets string with num and cha between *
     *
     * @param n n
     * @param m m
     * @return the string with num and cha between
     * @since 1.0.0
     */
    static @NotNull String getStringWithNumAndChaBetween(int n, int m) {
        int len = getIntegerBetween(n, m);
        return getStringWithNumAndCha(len);
    }

    /**
     * Gets string with prefix *
     *
     * @param n      n
     * @param prefix prefix
     * @return the string with prefix
     * @since 1.0.0
     */
    static @NotNull String getStringWithPrefix(int n, @NotNull String prefix) {
        int len = prefix.length();
        if (n <= len) {
            return prefix;
        } else {
            len = n - len;
            return prefix + getStringWithCharacter(len);
        }
    }

    /**
     * Gets string with suffix *
     *
     * @param n      n
     * @param suffix suffix
     * @return the string with suffix
     * @since 1.0.0
     */
    static @NotNull String getStringWithSuffix(int n, @NotNull String suffix) {
        int len = suffix.length();
        if (n <= len) {
            return suffix;
        } else {
            len = n - len;
            return getStringWithCharacter(len) + suffix;
        }
    }

    /**
     * Gets string with both *
     *
     * @param n      n
     * @param prefix prefix
     * @param suffix suffix
     * @return the string with both
     * @since 1.0.0
     */
    static @NotNull String getStringWithBoth(int n, @NotNull String prefix, @NotNull String suffix) {
        int len = prefix.length() + suffix.length();
        StringBuilder res = new StringBuilder(prefix);
        if (n <= len) {
            return res.append(suffix).toString();
        } else {
            len = n - len;
            res.append(getStringWithCharacter(len));
            res.append(suffix);
            return res.toString();
        }
    }

    /**
     * Gets chese word with prifix *
     *
     * @param n      n
     * @param prefix prefix
     * @return the chese word with prifix
     * @since 1.0.0
     */
    static @NotNull String getCheseWordWithPrifix(int n, @NotNull String prefix) {
        int len = prefix.length();
        if (n <= len) {
            return prefix;
        } else {
            len = n - len;
            return prefix + getCheseWord(len);
        }
    }

    /**
     * Gets chese word *
     *
     * @param len len
     * @return the chese word
     * @since 1.0.0
     */
    static @NotNull String getCheseWord(int len) {
        StringBuilder res = new StringBuilder();

        for (int i = 0; i < len; ++i) {
            char str = getCheseChar();
            res.append(str);
        }

        return res.toString();
    }

    /**
     * Gets chese char *
     *
     * @return the chese char
     * @since 1.0.0
     */
    private static char getCheseChar() {
        return (char) (UNICODE_START + JVM_RANDOM.nextInt(UNICODE_END - UNICODE_START));
    }

    /**
     * Gets chese word with suffix *
     *
     * @param n      n
     * @param suffix suffix
     * @return the chese word with suffix
     * @since 1.0.0
     */
    static @NotNull String getCheseWordWithSuffix(int n, @NotNull String suffix) {
        int len = suffix.length();
        if (n <= len) {
            return suffix;
        } else {
            len = n - len;
            return getCheseWord(len) + suffix;
        }
    }

    /**
     * Gets chese word with both *
     *
     * @param n      n
     * @param prefix prefix
     * @param suffix suffix
     * @return the chese word with both
     * @since 1.0.0
     */
    static @NotNull String getCheseWordWithBoth(int n, @NotNull String prefix, @NotNull String suffix) {
        int len = prefix.length() + suffix.length();
        StringBuilder res = new StringBuilder(prefix);
        if (n <= len) {
            return res.append(suffix).toString();
        } else {
            len = n - len;
            res.append(getCheseWord(len));
            res.append(suffix);
            return res.toString();
        }
    }

    /**
     * Gets boolean *
     *
     * @return the boolean
     * @since 1.0.0
     */
    static boolean getBoolean() {
        return getIntegerMoreThanZeroLessThan(3) == 1;
    }

    /**
     * Get random array int [ ]
     *
     * @param min min
     * @param max max
     * @param n   n
     * @return the int [ ]
     * @since 1.0.0
     */
    static int @Nullable [] getRandomArray(int min, int max, int n) {
        int len = max - min + 1;
        if (max >= min && n <= len) {
            int[] source = new int[len];

            int i = min;
            while (i < min + len) {
                source[i - min] = i++;
            }

            int[] result = new int[n];
            Random rd = new Random();

            for (int y = 0; y < result.length; ++y) {
                int index = Math.abs(rd.nextInt() % len--);
                result[y] = source[index];
                source[index] = source[len];
            }

            return result;
        } else {
            return null;
        }
    }

    /**
     * Gets random collection *
     *
     * @param min min
     * @param max max
     * @param n   n
     * @return the random collection
     * @since 1.0.0
     */
    static @NotNull Collection<Integer> getRandomCollection(int min, int max, int n) {
        Set<Integer> res = new HashSet<>();
        int mx = max;
        int mn = min;
        int i;
        if (n == max + 1 - min) {
            for (i = 1; i <= n; ++i) {
                res.add(i);
            }

        } else {
            for (i = 0; i < n; ++i) {
                int v = getIntegerBetween(mn, mx);
                if (v == mx) {
                    --mx;
                }

                if (v == mn) {
                    ++mn;
                }

                while (res.contains(v)) {
                    v = getIntegerBetween(mn, mx);
                    if (v == mx) {
                        mx = v;
                    }

                    if (v == mn) {
                        mn = v;
                    }
                }

                res.add(v);
            }

        }
        return res;
    }
}
