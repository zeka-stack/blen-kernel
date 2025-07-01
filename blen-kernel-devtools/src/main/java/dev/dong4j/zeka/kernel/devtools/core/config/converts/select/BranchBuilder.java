package dev.dong4j.zeka.kernel.devtools.core.config.converts.select;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 分支构建者
 *
 * @param <P> parameter
 * @param <T> parameter
 * @author hanchunlin  Created at 2020/6/11 17:22
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2024.04.02 23:58
 * @since 2024.2.0
 */
public interface BranchBuilder<P, T> {

    /**
     * 使用一个值工厂构造出一个分支
     *
     * @param factory 值工厂
     * @return 返回分支 branch
     * @since 2024.2.0
     */
    Branch<P, T> then(Function<P, T> factory);

    /**
     * 从值构建出一个分支
     *
     * @param value 值
     * @return 返回一个分支 branch
     * @since 2024.2.0
     */
    default Branch<P, T> then(T value) {
        return then(p -> value);
    }

    /**
     * 工厂函数，用于创建分支构建者
     *
     * @param <P>    参数类型
     * @param <T>    返回值类型
     * @param tester 测试器
     * @return 返回一个分支创建者 branch builder
     * @since 2024.2.0
     */
    static <P, T> BranchBuilder<P, T> of(Predicate<P> tester) {
        return factory -> Branch.of(tester, factory);
    }

}
