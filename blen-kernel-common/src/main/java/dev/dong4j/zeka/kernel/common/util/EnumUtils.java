package dev.dong4j.zeka.kernel.common.util;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import sun.reflect.ConstructorAccessor;
import sun.reflect.FieldAccessor;
import sun.reflect.ReflectionFactory;

/**
 * <p>Description: 枚举工具类</p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:22
 * @since 1.0.0
 */
@Slf4j
@UtilityClass
@SuppressWarnings("all")
public class EnumUtils {
    /** 枚举类缓存(懒加载的方式) */
    private static final Map<Class<?>, Object> ENUM_MAP = new ConcurrentHashMap<>();

    /**
     * 根据条件获取枚举对象
     *
     * @param <T>       the type parameter
     * @param cla       枚举类
     * @param predicate 筛选条件
     * @return enum optional
     * @since 1.0.0
     */
    public static <T> Optional<T> of(@NotNull Class<T> cla, Predicate<T> predicate) {
        if (!cla.isEnum()) {
            log.info("Class 不是枚举类 cla ={}", cla);
            return Optional.empty();
        }
        Object obj = ENUM_MAP.get(cla);
        T[] ts;
        if (obj == null) {
            ts = cla.getEnumConstants();
            ENUM_MAP.put(cla, ts);
        } else {
            ts = (T[]) obj;
        }
        return Arrays.stream(ts).filter(predicate).findAny();
    }

    /**
     * 通过枚举的 index 获取枚举
     *
     * @param <T>     the type parameter
     * @param clazz   the clazz
     * @param ordinal the ordinal   需要的枚举值在设定的枚举类中的顺序, 以 0 开始
     * @return t t
     * @author xiehao
     * @since 1.0.0
     */
    public static <T extends Enum<T>> T indexOf(@NotNull Class<T> clazz, int ordinal) {
        return clazz.getEnumConstants()[ordinal];
    }

    /**
     * 传入的参数 name 指的是枚举值的名称, 一般是大写加下划线的
     *
     * @param <T>   the type parameter
     * @param clazz the clazz
     * @param name  the name
     * @return Enum T
     * @author xiehao
     * @since 1.0.0
     */
    @NotNull
    public static <T extends Enum<T>> T nameOf(Class<T> clazz, String name) {
        return Enum.valueOf(clazz, name);
    }

    /**
     * <p>Description: 为 Enum 动态增加枚举值 </p>
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.12.03 14:33
     * @since 1.7.0
     */
    public static class DynamicEnum {
        /** reflectionFactory */
        private static final ReflectionFactory REFLECTION_FACTORY = ReflectionFactory.getReflectionFactory();

        /**
         * 动态添加枚举值
         *
         * @param <T>        枚举
         * @param enumType   枚举类型
         * @param enumName   枚举名
         * @param paramClass 枚举中包含的字段类型
         * @param paramValue 枚举中包含的字段对应的值
         * @since 1.7.0
         */
        @SuppressWarnings("unchecked")
        public static <T extends Enum<?>> void addEnum(Class<T> enumType,
                                                       String enumName,
                                                       Class<?>[] paramClass,
                                                       Object[] paramValue) {

            if (!Enum.class.isAssignableFrom(enumType)) {
                throw new RuntimeException("class " + enumType + " is not an instance of Enum");
            }

            // 1. 查找枚举的 $VALUES 字段
            Field valuesField = null;
            Field[] fields = enumType.getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().contains("$VALUES")) {
                    valuesField = field;
                    break;
                }
            }
            AccessibleObject.setAccessible(new Field[]{valuesField}, true);

            try {

                // 2. 获取所有枚举值
                T[] previousValues = (T[]) Objects.requireNonNull(valuesField).get(enumType);
                List<T> values = new ArrayList<>(Arrays.asList(previousValues));

                // 3. 创建一个新的枚举值
                T newValue = (T) makeEnum(enumType,
                    enumName,
                    values.size(),
                    paramClass,
                    paramValue
                );

                // 4. 添加到枚举列表
                values.add(newValue);
                Object object = values.toArray((T[]) Array.newInstance(enumType, 0));
                // 5. 重设枚举
                setFailsafeFieldValue(valuesField, null, object);

                // 6. 清除缓存
                cleanEnumCache(enumType);

            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }

        /**
         * 创建一个新的枚举值
         *
         * @param enumClass        枚举类型
         * @param enumName         枚举名
         * @param ordinal          下标
         * @param additionalTypes  枚举中包含的字段类型
         * @param additionalValues 枚举中包含的字段对应的值
         * @return the object 枚举
         * @throws Exception exception
         * @since 1.7.0
         */
        private static Object makeEnum(Class<?> enumClass,
                                       String enumName,
                                       int ordinal,
                                       Class<?>[] additionalTypes,
                                       Object[] additionalValues) throws Exception {
            Object[] parms = new Object[additionalValues.length + 2];
            parms[0] = enumName;
            parms[1] = ordinal;
            System.arraycopy(additionalValues, 0, parms, 2, additionalValues.length);
            return enumClass.cast(getConstructorAccessor(enumClass, additionalTypes).newInstance(parms));
        }

        /**
         * 获取枚举构造方法
         *
         * @param enumClass                枚举类型
         * @param additionalParameterTypes 枚举中包含的字段类型
         * @return the constructor accessor 枚举构造方法
         * @throws NoSuchMethodException no such method exception
         * @since 1.7.0
         */
        private static ConstructorAccessor getConstructorAccessor(Class<?> enumClass, Class<?>[] additionalParameterTypes)
            throws NoSuchMethodException {
            Class<?>[] parameterTypes = new Class[additionalParameterTypes.length + 2];
            parameterTypes[0] = String.class;
            parameterTypes[1] = int.class;
            System.arraycopy(additionalParameterTypes, 0, parameterTypes, 2, additionalParameterTypes.length);
            return REFLECTION_FACTORY.newConstructorAccessor(enumClass.getDeclaredConstructor(parameterTypes));
        }

        /**
         * 设置枚举值
         *
         * @param field  枚举字段
         * @param target 枚举
         * @param value  枚举值
         * @throws NoSuchFieldException   no such field exception
         * @throws IllegalAccessException illegal access exception
         * @since 1.7.0
         */
        private static void setFailsafeFieldValue(Field field, Object target, Object value) throws NoSuchFieldException,
            IllegalAccessException {

            field.setAccessible(true);
            // 修改 final 字段
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            int modifiers = modifiersField.getInt(field);

            // blank out the final bit in the modifiers int
            modifiers &= ~Modifier.FINAL;
            modifiersField.setInt(field, modifiers);

            FieldAccessor fieldAccessor = REFLECTION_FACTORY.newFieldAccessor(field, false);
            fieldAccessor.set(target, value);
        }

        /**
         * 清除缓存
         *
         * @param enumClass 枚举字段
         * @throws NoSuchFieldException   no such field exception
         * @throws IllegalAccessException illegal access exception
         * @since 1.7.0
         */
        private static void cleanEnumCache(Class<?> enumClass) throws NoSuchFieldException, IllegalAccessException {
            blankField(enumClass, "enumConstantDirectory");
            blankField(enumClass, "enumConstants");
        }

        /**
         * Blank field
         *
         * @param enumClass enum class
         * @param fieldName field name
         * @throws NoSuchFieldException   no such field exception
         * @throws IllegalAccessException illegal access exception
         * @since 1.7.0
         */
        private static void blankField(Class<?> enumClass, String fieldName) throws NoSuchFieldException,
            IllegalAccessException {
            for (Field field : Class.class.getDeclaredFields()) {
                if (field.getName().contains(fieldName)) {
                    AccessibleObject.setAccessible(new Field[]{field}, true);
                    setFailsafeFieldValue(field, enumClass, null);
                    break;
                }
            }
        }

    }

}
