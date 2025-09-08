package dev.dong4j.zeka.kernel.test.mock.util;

import java.util.Random;
import lombok.experimental.UtilityClass;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:07
 * @since 1.0.0
 */
@UtilityClass
public final class RandomUtils {

    /** RANDOM */
    private static final Random RANDOM = new Random();

    /**
     * Next boolean boolean.
     *
     * @return the boolean
     * @since 1.0.0
     */
    public static boolean nextBoolean() {
        return RANDOM.nextBoolean();
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
        return startInclusive + RANDOM.nextInt(endExclusive - startInclusive);
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
        return startInclusive + ((endInclusive - startInclusive) * RANDOM.nextDouble());
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
        return startInclusive + ((endInclusive - startInclusive) * RANDOM.nextFloat());
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
        return startInclusive + RANDOM.nextInt(endInclusive - startInclusive + 1);
    }

}
