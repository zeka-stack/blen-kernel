package dev.dong4j.zeka.kernel.common.util;

import java.security.SecureRandom;
import java.util.Random;

/**
 * <p>Description: 一些常用的单例对象 </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:17
 * @since 1.0.0
 */
public final class Holder {

    /** RANDOM */
    public static final Random RANDOM = new Random();

    /** SECURE_RANDOM */
    public static final SecureRandom SECURE_RANDOM = new SecureRandom();
}
