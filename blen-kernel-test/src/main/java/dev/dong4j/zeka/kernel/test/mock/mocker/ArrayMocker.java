package dev.dong4j.zeka.kernel.test.mock.mocker;

import com.google.common.collect.Maps;
import dev.dong4j.zeka.kernel.common.util.RandomUtils;
import dev.dong4j.zeka.kernel.test.mock.MockConfig;
import dev.dong4j.zeka.kernel.test.mock.Mocker;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * <p>Description: 数组模拟器</p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.26 21:42
 * @since 1.0.0
 */
@SuppressWarnings("all")
public class ArrayMocker implements Mocker<Object> {

    /** Type */
    private final Type type;

    /**
     * Instantiates a new Array mocker.
     *
     * @param type the type
     * @since 1.0.0
     */
    @Contract(pure = true)
    ArrayMocker(Type type) {
        this.type = type;
    }

    /**
     * Mock object
     *
     * @param mockConfig mock config
     * @return the object
     * @since 1.0.0
     */
    @Override
    public Object mock(MockConfig mockConfig) {
        // 创建有参数化的数组
        if (type instanceof GenericArrayType) {
            return createGenericArray(mockConfig);
        }
        return array(mockConfig);
    }

    /**
     * Array object
     *
     * @param mockConfig mock config
     * @return the object
     * @since 1.0.0
     */
    private Object array(@NotNull MockConfig mockConfig) {
        int size = RandomUtils.nextSize(mockConfig.getSizeRange()[0], mockConfig.getSizeRange()[1]);
        Class componentClass = ((Class) type).getComponentType();
        Object result = Array.newInstance(componentClass, size);
        BaseMocker baseMocker = new BaseMocker(componentClass);
        for (int index = 0; index < size; index++) {
            Array.set(result, index, baseMocker.mock(mockConfig));
        }
        return result;
    }

    /**
     * 由于GenericArrayType无法获得Class,所以递归创建多维数组
     *
     * @param mockConfig the mock config
     * @return the object
     * @since 1.0.0
     */
    private Object createGenericArray(MockConfig mockConfig) {
        GenericArrayType genericArrayType = (GenericArrayType) type;
        // 递归获取该数组的维数,以及最后的Class类型
        Map<Integer, Map<Class, Type[]>> map = map(mockConfig, genericArrayType, 0);
        Entry<Integer, Map<Class, Type[]>> entry = map.entrySet().iterator().next();
        Entry<Class, Type[]> baseEntry = entry.getValue().entrySet().iterator().next();
        int[] dimensions = new int[entry.getKey()];
        for (int index = 0; index < dimensions.length; index++) {
            dimensions[index] = RandomUtils.nextSize(mockConfig.getSizeRange()[0], mockConfig.getSizeRange()[1]);
        }
        // 创建多维数组每种维度的对象
        List<Object> list = new ArrayList<>(dimensions.length);
        Class clazz = baseEntry.getKey();
        for (int index = dimensions.length - 1; index >= 0; index--) {
            Object array = Array.newInstance(clazz, dimensions[index]);
            list.add(array);
            clazz = array.getClass();
        }
        // 实例化多维数组
        Object baseResult = new BaseMocker(baseEntry.getKey(), baseEntry.getValue()).mock(mockConfig);
        for (int i = 0; i < list.size(); i++) {
            Object array = list.get(i);
            for (int j = 0; j < dimensions[dimensions.length - i - 1]; j++) {
                Array.set(array, j, baseResult);
            }
            baseResult = array;
        }
        return baseResult;
    }

    /**
     * Map map.
     *
     * @param mockConfig       the mock config
     * @param genericArrayType the generic array type
     * @param dimension        the dimension
     * @return the map
     * @since 1.0.0
     */
    @SuppressWarnings("checkstyle:ReturnCount")
    private Map<Integer, Map<Class, Type[]>> map(MockConfig mockConfig, @NotNull GenericArrayType genericArrayType, int dimension) {
        Map<Integer, Map<Class, Type[]>> result = Maps.newHashMapWithExpectedSize(1);
        Type componentType = genericArrayType.getGenericComponentType();
        dimension++;
        if (componentType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) componentType;
            Map<Class, Type[]> map = Maps.newHashMapWithExpectedSize(1);
            map.put((Class) parameterizedType.getRawType(), parameterizedType.getActualTypeArguments());
            result.put(dimension, map);
            return result;
        }
        if (componentType instanceof GenericArrayType) {
            return map(mockConfig, (GenericArrayType) componentType, dimension);
        }
        if (componentType instanceof TypeVariable) {
            Map<Class, Type[]> map = Maps.newHashMapWithExpectedSize(1);
            map.put((Class) mockConfig.getVariableType(((TypeVariable) componentType).getName()), null);
            result.put(dimension, map);
            return result;
        }
        Map<Class, Type[]> map = Maps.newHashMapWithExpectedSize(1);
        map.put((Class) componentType, null);
        result.put(dimension, map);
        return result;
    }

}
