package dev.dong4j.zeka.kernel.test.mock.mocker;

import dev.dong4j.zeka.kernel.common.util.RandomUtils;
import dev.dong4j.zeka.kernel.test.mock.MockConfig;
import dev.dong4j.zeka.kernel.test.mock.MockException;
import dev.dong4j.zeka.kernel.test.mock.Mocker;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

/**
 * <p>Description: Double对象模拟器</p>
 *
 * @param <T> the type parameter
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:20
 * @since 1.0.0
 */
@SuppressWarnings("all")
public class EnumMocker<T extends Enum> implements Mocker<Object> {

    /** Clazz */
    private final Class<?> clazz;

    /**
     * Instantiates a new Enum mocker.
     *
     * @param clazz the clazz
     * @since 1.0.0
     */
    @Contract(pure = true)
    public EnumMocker(Class<?> clazz) {
        this.clazz = clazz;
    }

    /**
     * Mocks t.
     *
     * @param mockConfig the mock config
     * @return the t
     * @since 1.0.0
     */
    @Override
    public T mock(@NotNull MockConfig mockConfig) {
        Enum[] enums = mockConfig.getcacheEnum(clazz.getName());
        if (enums == null) {
            try {
                Field field = clazz.getDeclaredField("$VALUES");
                field.setAccessible(true);
                enums = (Enum[]) field.get(clazz);
                if (enums.length == 0) {
                    throw new MockException("空的enum不能模拟");
                }
                mockConfig.cacheEnum(clazz.getName(), enums);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new MockException(e);
            }
        }
        return (T) enums[RandomUtils.nextInt(0, enums.length)];
    }

}
