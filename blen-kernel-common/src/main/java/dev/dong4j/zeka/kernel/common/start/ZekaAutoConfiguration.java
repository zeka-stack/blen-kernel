package dev.dong4j.zeka.kernel.common.start;

import dev.dong4j.zeka.kernel.common.enums.LibraryEnum;
import dev.dong4j.zeka.kernel.common.util.CollectionUtils;
import dev.dong4j.zeka.kernel.common.util.StartUtils;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 11:28
 * @since 1.0.0
 */
public interface ZekaAutoConfiguration extends InitializingBean, DisposableBean {

    /**
     * 构造方法执行完成后执行
     *
     * @since 1.0.0
     */
    @PostConstruct
    default void init() {
        StartUtils.loadComponent(processName(this.getClass().getName()));
        if (this.getLibraryType() != null) {
            Constant.COMPONENTS.add(this.getLibraryType());
        }

        if (CollectionUtils.isNotEmpty(this.getLibraryTypes())) {
            Constant.COMPONENTS.addAll(this.getLibraryTypes());
        }
    }

    /**
     * 处理实现类类名
     *
     * @param className the class name
     * @return the string
     * @since 1.0.0
     */
    static String processName(@NotNull String className) {
        int pos = className.indexOf("$$");
        if (pos > 0) {
            className = className.substring(0, className.indexOf("$$"));
        }
        return className;
    }

    /**
     * 启动完成后输出提示信息
     *
     * @return the library type
     * @since 1.0.0
     */
    default LibraryEnum getLibraryType() {
        return null;
    }

    /**
     * Gets library types *
     *
     * @return the library types
     * @since 1.7.1
     */
    default List<LibraryEnum> getLibraryTypes() {
        return Collections.emptyList();
    }

    /**
     * bean 初始化完成后执行
     *
     * @since 1.0.0
     */
    @Override
    default void afterPropertiesSet() {
        this.execute();
    }

    /**
     * 自动装配类检查
     *
     * @since 1.0.0
     */
    default void execute() {

    }

    /**
     * 容器销毁后执行
     *
     * @since 1.0.0
     */
    @Override
    default void destroy() {
        // todo-dong4j : (2019年10月14日 22:00) []
    }

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.2.3
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.01.27 11:28
     * @since 1.0.0
     */
    @UtilityClass
    class Constant {
        /** 保存所有的自动装配类名, 启动时输出 */
        public static final List<LibraryEnum> COMPONENTS = new ArrayList<>();
    }

}
