package dev.dong4j.zeka.kernel.devtools.core.config.converts.select;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 分支提供者
 *
 * @param <P> parameter
 * @param <T> parameter
 * @author hanchunlin  Created at 2020/6/11 17:19
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2024.04.02 23:58
 * @see BranchBuilder
 * @since 1.0.0
 */
public interface Branch<P, T> {

    /**
     * Tester
     *
     * @return 分支进入条件 predicate
     * @since 1.0.0
     */
    Predicate<P> tester();

    /**
     * Factory
     *
     * @return 值工厂 function
     * @since 1.0.0
     */
    Function<P, T> factory();

    /**
     * 工厂方法，快速创建分支
     *
     * @param <P>     参数类型
     * @param <T>     值类型
     * @param tester  测试器
     * @param factory 值工厂
     * @return 返回一个新的分支 branch
     * @since 1.0.0
     */
    static <P, T> Branch<P, T> of(Predicate<P> tester, Function<P, T> factory) {
        return new Branch<P, T>() {

            @Override
            public Predicate<P> tester() {
                return tester;
            }

            @Override
            public Function<P, T> factory() {
                return factory;
            }

        };
    }
}
