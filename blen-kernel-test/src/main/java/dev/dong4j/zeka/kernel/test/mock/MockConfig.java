package dev.dong4j.zeka.kernel.test.mock;

import com.google.common.collect.Maps;
import dev.dong4j.zeka.kernel.test.mock.mocker.BigDecimalMocker;
import dev.dong4j.zeka.kernel.test.mock.mocker.BigIntegerMocker;
import dev.dong4j.zeka.kernel.test.mock.mocker.BooleanMocker;
import dev.dong4j.zeka.kernel.test.mock.mocker.ByteMocker;
import dev.dong4j.zeka.kernel.test.mock.mocker.CharacterMocker;
import dev.dong4j.zeka.kernel.test.mock.mocker.DateMocker;
import dev.dong4j.zeka.kernel.test.mock.mocker.DoubleMocker;
import dev.dong4j.zeka.kernel.test.mock.mocker.FloatMocker;
import dev.dong4j.zeka.kernel.test.mock.mocker.IntegerMocker;
import dev.dong4j.zeka.kernel.test.mock.mocker.LongMocker;
import dev.dong4j.zeka.kernel.test.mock.mocker.ShortMocker;
import dev.dong4j.zeka.kernel.test.mock.mocker.StringMocker;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Description: 模拟数据配置类</p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:05
 * @since 1.0.0
 */
@SuppressWarnings("checkstyle:MethodLimit")
public class MockConfig {
    /** BYTE_MOCKER */
    private static final ByteMocker BYTE_MOCKER = new ByteMocker();
    /** BOOLEAN_MOCKER */
    private static final BooleanMocker BOOLEAN_MOCKER = new BooleanMocker();
    /** CHARACTER_MOCKER */
    private static final CharacterMocker CHARACTER_MOCKER = new CharacterMocker();
    /** SHORT_MOCKER */
    private static final ShortMocker SHORT_MOCKER = new ShortMocker();
    /** INTEGER_MOCKER */
    private static final IntegerMocker INTEGER_MOCKER = new IntegerMocker();
    /** LONG_MOCKER */
    private static final LongMocker LONG_MOCKER = new LongMocker();
    /** FLOAT_MOCKER */
    private static final FloatMocker FLOAT_MOCKER = new FloatMocker();
    /** DOUBLE_MOCKER */
    private static final DoubleMocker DOUBLE_MOCKER = new DoubleMocker();
    /** BIG_INTEGER_MOCKER */
    private static final BigIntegerMocker BIG_INTEGER_MOCKER = new BigIntegerMocker();
    /** BIG_DECIMAL_MOCKER */
    private static final BigDecimalMocker BIG_DECIMAL_MOCKER = new BigDecimalMocker();
    /** STRING_MOCKER */
    private static final StringMocker STRING_MOCKER = new StringMocker();
    /** DATE_MOCKER */
    private static final DateMocker DATE_MOCKER = new DateMocker("1970-01-01", "2100-12-31");
    /**
     * Bean缓存
     */
    private final Map<String, Object> beanCache = Maps.newHashMap();
    /**
     * TypeVariable缓存
     */
    private final Map<String, Type> typeVariableCache = Maps.newHashMap();
    /**
     * enum缓存
     */
    private final Map<String, Enum[]> enumCache = Maps.newHashMap();
    /** Mocker context */
    private final Map<Class<?>, Mocker> mockerContext = Maps.newHashMap();
    /** Byte range */
    private final byte[] byteRange = {0, 127};
    /** Short range */
    private final short[] shortRange = {0, 1000};
    /** Int range */
    private final int[] intRange = {0, 10000};
    /** Float range */
    private final float[] floatRange = {0.0f, 10000.00f};
    /** Double range */
    private final double[] doubleRange = {0.0, 10000.00};
    /** Long range */
    private final long[] longRange = {0L, 10000L};
    /** Date range */
    private final String[] dateRange = {"1970-01-01", "2100-12-31"};
    /** Size range */
    private final int[] sizeRange = {2, 20};
    /**
     * 默认为中文
     */
    private StringEnum stringEnum = StringEnum.CHINESE;
    /** Enabled circle */
    private boolean enabledCircle = false;
    /** Char seed */
    private char[] charSeed =
        {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
            'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F',
            'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    /** String seed */
    private String[] stringSeed =
        {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
            "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F",
            "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    /**
     * Instantiates a new Mock config.
     *
     * @since 1.0.0
     */
    public MockConfig() {
        this.registerMocker(BYTE_MOCKER, byte.class, Byte.class);
        this.registerMocker(BOOLEAN_MOCKER, boolean.class, Boolean.class);
        this.registerMocker(CHARACTER_MOCKER, char.class, Character.class);
        this.registerMocker(SHORT_MOCKER, short.class, Short.class);
        this.registerMocker(INTEGER_MOCKER, Integer.class, int.class);
        this.registerMocker(LONG_MOCKER, long.class, Long.class);
        this.registerMocker(FLOAT_MOCKER, float.class, Float.class);
        this.registerMocker(DOUBLE_MOCKER, double.class, Double.class);
        this.registerMocker(BIG_INTEGER_MOCKER, BigInteger.class);
        this.registerMocker(BIG_DECIMAL_MOCKER, BigDecimal.class);
        this.registerMocker(STRING_MOCKER, String.class);
        this.registerMocker(DATE_MOCKER, Date.class);
    }

    /**
     * Register mocker.
     *
     * @param mocker the mocker
     * @param clazzs the clazzs
     * @since 1.0.0
     */
    public void registerMocker(Mocker mocker, @NotNull Class<?>... clazzs) {
        for (Class<?> clazz : clazzs) {
            this.mockerContext.put(clazz, mocker);
        }
    }

    /**
     * Cache bean.
     *
     * @param name the name
     * @param bean the bean
     * @since 1.0.0
     */
    public void cacheBean(String name, Object bean) {
        this.beanCache.put(name, bean);
    }

    /**
     * Gets bean.
     *
     * @param beanClassName the bean class name
     * @return the bean
     * @since 1.0.0
     */
    public Object getcacheBean(String beanClassName) {
        return this.beanCache.get(beanClassName);
    }

    /**
     * Cache enum.
     *
     * @param name  the name
     * @param enums the enums
     * @since 1.0.0
     */
    public void cacheEnum(String name, Enum[] enums) {
        this.enumCache.put(name, enums);
    }

    /**
     * Getcache enum enum [ ].
     *
     * @param enumClassName the enum class name
     * @return the enum [ ]
     * @since 1.0.0
     */
    public Enum[] getcacheEnum(String enumClassName) {
        return this.enumCache.get(enumClassName);
    }

    /**
     * Init mock config.
     *
     * @param type the type
     * @return the mock config
     * @since 1.0.0
     */
    public MockConfig init(Type type) {
        if (type instanceof ParameterizedType) {
            Class clazz = (Class) ((ParameterizedType) type).getRawType();
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            TypeVariable[] typeVariables = clazz.getTypeParameters();
            if (typeVariables != null && typeVariables.length > 0) {
                for (int index = 0; index < typeVariables.length; index++) {
                    this.typeVariableCache.put(typeVariables[index].getName(), types[index]);
                }
            }
        }
        return this;
    }

    /**
     * Is enabled circle boolean.
     *
     * @return the boolean
     * @since 1.0.0
     */
    public boolean isEnabledCircle() {
        return this.enabledCircle;
    }

    /**
     * Sets enabled circle.
     *
     * @param enabledCircle the enabled circle
     * @return the enabled circle
     * @since 1.0.0
     */
    public MockConfig setEnabledCircle(boolean enabledCircle) {
        this.enabledCircle = enabledCircle;
        return this;
    }

    /**
     * Gets variable type.
     *
     * @param name the name
     * @return the variable type
     * @since 1.0.0
     */
    public Type getVariableType(String name) {
        return this.typeVariableCache.get(name);
    }

    /**
     * Byte range mock config.
     *
     * @param min the min
     * @param max the max
     * @return the mock config
     * @since 1.0.0
     */
    public MockConfig byteRange(byte min, byte max) {
        this.byteRange[0] = min;
        this.byteRange[1] = max;
        return this;
    }

    /**
     * Short range mock config.
     *
     * @param min the min
     * @param max the max
     * @return the mock config
     * @since 1.0.0
     */
    public MockConfig shortRange(short min, short max) {
        this.shortRange[0] = min;
        this.shortRange[1] = max;
        return this;
    }

    /**
     * Int range mock config.
     *
     * @param min the min
     * @param max the max
     * @return the mock config
     * @since 1.0.0
     */
    public MockConfig intRange(int min, int max) {
        this.intRange[0] = min;
        this.intRange[1] = max;
        return this;
    }

    /**
     * Float range mock config.
     *
     * @param min the min
     * @param max the max
     * @return the mock config
     * @since 1.0.0
     */
    public MockConfig floatRange(float min, float max) {
        this.floatRange[0] = min;
        this.floatRange[1] = max;
        return this;
    }

    /**
     * Double range mock config.
     *
     * @param min the min
     * @param max the max
     * @return the mock config
     * @since 1.0.0
     */
    public MockConfig doubleRange(double min, double max) {
        this.doubleRange[0] = min;
        this.doubleRange[1] = max;
        return this;
    }

    /**
     * Long range mock config.
     *
     * @param min the min
     * @param max the max
     * @return the mock config
     * @since 1.0.0
     */
    public MockConfig longRange(long min, long max) {
        this.longRange[0] = min;
        this.longRange[1] = max;
        return this;
    }

    /**
     * Date range mock config.
     *
     * @param min the min
     * @param max the max
     * @return the mock config
     * @since 1.0.0
     */
    public MockConfig dateRange(String min, String max) {
        this.dateRange[0] = min;
        this.dateRange[1] = max;
        this.registerMocker(new DateMocker(min, max), Date.class);
        return this;
    }

    /**
     * Size range mock config.
     *
     * @param min the min
     * @param max the max
     * @return the mock config
     * @since 1.0.0
     */
    public MockConfig sizeRange(int min, int max) {
        this.sizeRange[0] = min;
        this.sizeRange[1] = max;
        return this;
    }

    /**
     * String seed mock config.
     *
     * @param stringSeed the string seed
     * @return the mock config
     * @since 1.0.0
     */
    public MockConfig stringSeed(String... stringSeed) {
        this.stringSeed = stringSeed;
        return this;
    }

    /**
     * Char seed mock config.
     *
     * @param charSeed the char seed
     * @return the mock config
     * @since 1.0.0
     */
    public MockConfig charSeed(char... charSeed) {
        this.charSeed = charSeed;
        return this;
    }

    /**
     * Get byte range byte [ ].
     *
     * @return the byte [ ]
     * @since 1.0.0
     */
    public byte[] getByteRange() {
        return this.byteRange;
    }

    /**
     * Get short range short [ ].
     *
     * @return the short [ ]
     * @since 1.0.0
     */
    public short[] getShortRange() {
        return this.shortRange;
    }

    /**
     * Get int range int [ ].
     *
     * @return the int [ ]
     * @since 1.0.0
     */
    public int[] getIntRange() {
        return this.intRange;
    }

    /**
     * Get float range float [ ].
     *
     * @return the float [ ]
     * @since 1.0.0
     */
    public float[] getFloatRange() {
        return this.floatRange;
    }

    /**
     * Get double range double [ ].
     *
     * @return the double [ ]
     * @since 1.0.0
     */
    public double[] getDoubleRange() {
        return this.doubleRange;
    }

    /**
     * Get long range long [ ].
     *
     * @return the long [ ]
     * @since 1.0.0
     */
    public long[] getLongRange() {
        return this.longRange;
    }

    /**
     * Get size range int [ ].
     *
     * @return the int [ ]
     * @since 1.0.0
     */
    public int[] getSizeRange() {
        return this.sizeRange;
    }

    /**
     * Get char seed char [ ].
     *
     * @return the char [ ]
     * @since 1.0.0
     */
    public char[] getCharSeed() {
        return this.charSeed;
    }

    /**
     * Get string seed string [ ].
     *
     * @return the string [ ]
     * @since 1.0.0
     */
    public String[] getStringSeed() {
        return this.stringSeed;
    }

    /**
     * Gets mocker.
     *
     * @param clazz the clazz
     * @return the mocker
     * @since 1.0.0
     */
    public Mocker<?> getMocker(Class<?> clazz) {
        return this.mockerContext.get(clazz);
    }

    /**
     * Gets string enum.
     *
     * @return the string enum
     * @since 1.0.0
     */
    public StringEnum getStringEnum() {
        return this.stringEnum;
    }

    /**
     * Sets string enum.
     *
     * @param stringEnum the string enum
     * @return the string enum
     * @since 1.0.0
     */
    public MockConfig setStringEnum(StringEnum stringEnum) {
        this.stringEnum = stringEnum;
        return this;
    }

    /**
     * The enum String enum.
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.01.27 18:05
     * @since 1.0.0
     */
    public enum StringEnum {
        /**
         * uuid
         */
        UUID,
        /**
         * 随机长度的字符
         */
        CHARACTER,
        /**
         * 汉字词语,句子
         */
        CHINESE,
        /**
         * http连接
         */
        HTTP,
        /**
         * https连接
         */
        HTTPS,
        /**
         * 超大文本
         */
        TEXT
    }
}
