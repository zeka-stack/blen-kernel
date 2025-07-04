package dev.dong4j.zeka.kernel.common.convert;

import com.fasterxml.jackson.annotation.JsonCreator;
import dev.dong4j.zeka.kernel.common.util.ConvertUtils;
import dev.dong4j.zeka.kernel.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.lang.Nullable;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * <p>Description: 接收参数 同 jackson String -> Enum 转换 </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:06
 * @since 1.0.0
 */
@Slf4j
public class StringToEnumConverter implements ConditionalGenericConverter {
    /** 缓存 Enum 类信息,提升性能 */
    private static final ConcurrentMap<Class<?>, AccessibleObject> ENUM_CACHE_MAP = new ConcurrentHashMap<>(8);

    /**
     * Matches boolean
     *
     * @param sourceType source type
     * @param targetType target type
     * @return the boolean
     * @since 1.0.0
     */
    @Override
    public boolean matches(@NotNull TypeDescriptor sourceType, @NotNull TypeDescriptor targetType) {
        return true;
    }

    /**
     * Gets convertible types *
     *
     * @return the convertible types
     * @since 1.0.0
     */
    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(String.class, Enum.class));
    }

    /**
     * Convert object
     *
     * @param source     source
     * @param sourceType source type
     * @param targetType target type
     * @return the object
     * @since 1.0.0
     */
    @Nullable
    @Override
    @SuppressWarnings("checkstyle:ReturnCount")
    public Object convert(@Nullable Object source, @NotNull TypeDescriptor sourceType, @NotNull TypeDescriptor targetType) {
        if (source == null) {
            return null;
        }
        if (StringUtils.isBlank((String) source)) {
            return null;
        }
        Class<?> clazz = targetType.getType();
        AccessibleObject accessibleObject = ENUM_CACHE_MAP.computeIfAbsent(clazz, StringToEnumConverter::getAnnotation);
        String value = ((String) source).trim();
        // 如果为null,走默认的转换
        if (accessibleObject == null) {
            return valueOf(clazz, value);
        }
        try {
            return StringToEnumConverter.invoke(clazz, accessibleObject, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * Gets annotation *
     *
     * @param clazz clazz
     * @return the annotation
     * @since 1.0.0
     */
    @org.jetbrains.annotations.Nullable
    @Nullable
    private static AccessibleObject getAnnotation(@NotNull Class<?> clazz) {
        Set<AccessibleObject> accessibleObjects = new HashSet<>();
        // JsonCreator METHOD, CONSTRUCTOR
        Constructor<?>[] constructors = clazz.getConstructors();
        Collections.addAll(accessibleObjects, constructors);
        // methods
        Method[] methods = clazz.getDeclaredMethods();
        Collections.addAll(accessibleObjects, methods);
        for (AccessibleObject accessibleObject : accessibleObjects) {
            // 复用 jackson 的 JsonCreator注解
            JsonCreator jsonCreator = accessibleObject.getAnnotation(JsonCreator.class);
            if (jsonCreator != null && JsonCreator.Mode.DISABLED != jsonCreator.mode()) {
                accessibleObject.setAccessible(true);
                return accessibleObject;
            }
        }
        return null;
    }

    /**
     * Value of t
     *
     * @param <T>   parameter
     * @param clazz clazz
     * @param value value
     * @return the t
     * @since 1.0.0
     */
    @NotNull
    @SuppressWarnings("unchecked")
    private static <T extends Enum<T>> T valueOf(Class<?> clazz, String value) {
        return Enum.valueOf((Class<T>) clazz, value);
    }

    /**
     * Invoke object
     *
     * @param clazz            clazz
     * @param accessibleObject accessible object
     * @param value            value
     * @return the object
     * @throws IllegalAccessException    illegal access exception
     * @throws InvocationTargetException invocation target exception
     * @throws InstantiationException    instantiation exception
     * @since 1.0.0
     */
    @Contract("_, null, _ -> null")
    @Nullable
    private static Object invoke(Class<?> clazz, AccessibleObject accessibleObject, String value)
        throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (accessibleObject instanceof Constructor) {
            @SuppressWarnings("rawtypes") Constructor constructor = (Constructor) accessibleObject;
            Class<?> paramType = constructor.getParameterTypes()[0];
            // 类型转换
            Object object = ConvertUtils.convert(value, paramType);
            return constructor.newInstance(object);
        }
        if (accessibleObject instanceof Method) {
            Method method = (Method) accessibleObject;
            Class<?> paramType = method.getParameterTypes()[0];
            // 类型转换
            Object object = ConvertUtils.convert(value, paramType);
            return method.invoke(clazz, object);
        }
        return null;
    }

}
