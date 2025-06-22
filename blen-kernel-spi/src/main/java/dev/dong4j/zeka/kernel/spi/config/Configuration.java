package dev.dong4j.zeka.kernel.spi.config;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.8.0
 * @email "mailto:dong4j@gmaidl.com"
 * @date 2021.02.26 17:47
 * @since 1.8.0
 */
public interface Configuration {
    /**
     * Gets string *
     *
     * @param key key
     * @return the string
     * @since 1.8.0
     */
    default String getString(String key) {
        return convert(String.class, key, null);
    }

    /**
     * Gets string *
     *
     * @param key          key
     * @param defaultValue default value
     * @return the string
     * @since 1.8.0
     */
    default String getString(String key, String defaultValue) {
        return convert(String.class, key, defaultValue);
    }

    /**
     * Gets property *
     *
     * @param key key
     * @return the property
     * @since 1.8.0
     */
    default Object getProperty(String key) {
        return getProperty(key, null);
    }

    /**
     * Gets property *
     *
     * @param key          key
     * @param defaultValue default value
     * @return the property
     * @since 1.8.0
     */
    default Object getProperty(String key, Object defaultValue) {
        Object value = getInternalProperty(key);
        return value != null ? value : defaultValue;
    }

    /**
     * Gets internal property *
     *
     * @param key key
     * @return the internal property
     * @since 1.8.0
     */
    Object getInternalProperty(String key);

    /**
     * Contains key
     *
     * @param key key
     * @return the boolean
     * @since 1.8.0
     */
    default boolean containsKey(String key) {
        return getProperty(key) != null;
    }


    /**
     * Convert
     *
     * @param <T>          parameter
     * @param cls          cls
     * @param key          key
     * @param defaultValue default value
     * @return the t
     * @since 1.8.0
     */
    default <T> T convert(Class<T> cls, String key, T defaultValue) {
        // we only process String properties for now
        String value = (String) getProperty(key);

        if (value == null) {
            return defaultValue;
        }

        Object obj = value;
        if (cls.isInstance(value)) {
            return cls.cast(value);
        }

        if (Boolean.class.equals(cls) || Boolean.TYPE.equals(cls)) {
            obj = Boolean.valueOf(value);
        } else if (Number.class.isAssignableFrom(cls) || cls.isPrimitive()) {
            if (Integer.class.equals(cls) || Integer.TYPE.equals(cls)) {
                obj = Integer.valueOf(value);
            } else if (Long.class.equals(cls) || Long.TYPE.equals(cls)) {
                obj = Long.valueOf(value);
            } else if (Byte.class.equals(cls) || Byte.TYPE.equals(cls)) {
                obj = Byte.valueOf(value);
            } else if (Short.class.equals(cls) || Short.TYPE.equals(cls)) {
                obj = Short.valueOf(value);
            } else if (Float.class.equals(cls) || Float.TYPE.equals(cls)) {
                obj = Float.valueOf(value);
            } else if (Double.class.equals(cls) || Double.TYPE.equals(cls)) {
                obj = Double.valueOf(value);
            }
        } else if (cls.isEnum()) {
            obj = Enum.valueOf(cls.asSubclass(Enum.class), value);
        }

        return cls.cast(obj);
    }


}
