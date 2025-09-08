package dev.dong4j.zeka.kernel.common.support;

import com.google.common.collect.Maps;
import dev.dong4j.zeka.kernel.common.util.BeanUtils;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.Type;
import org.springframework.cglib.core.AbstractClassGenerator;
import org.springframework.cglib.core.ClassEmitter;
import org.springframework.cglib.core.CodeEmitter;
import org.springframework.cglib.core.Constants;
import org.springframework.cglib.core.Converter;
import org.springframework.cglib.core.EmitUtils;
import org.springframework.cglib.core.KeyFactory;
import org.springframework.cglib.core.Local;
import org.springframework.cglib.core.MethodInfo;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.cglib.core.Signature;
import org.springframework.cglib.core.TypeUtils;

/**
 * 基于Spring CGLIB的高性能 Bean 复制器，支持链式调用和自定义ClassLoader
 * <p>
 * 该类对Spring CGLIB的BeanCopier进行了改进，主要解决以下问题：
 * 1. 支持链式 Bean的复制操作
 * 2. 解决Spring Boot和CGLIB ClassLoader不一致的问题
 * 3. 提供更好的类加载器支持和安全性
 * <p>
 * 主要特性：
 * - 基于CGLIB字节码生成，性能极高无反射开销
 * - 支持自定义ClassLoader，适应复杂的类加载环境
 * - 支持类型转换器（Converter）进行字段的自定义转换
 * - 支持链式Bean复制，可以复制返回同一对象的方法
 * - 线程安全的实例创建和复用
 * <p>
 * 设计模式：
 * - 工厂模式：通过Generator生成特定的复制器实例
 * - 模板方法：抽象出通用的复制逻辑，具体实现由生成的字节码提供
 * - 代理模式：生成的类作为原始Bean操作的高效代理
 * <p>
 * 使用示例：
 * <pre>{@code
 * // 基本使用
 * BaseBeanCopier copier = BaseBeanCopier.create(Source.class, Target.class, false);
 * copier.copy(sourceObj, targetObj, null);
 *
 * // 使用转换器
 * BaseBeanCopier copier = BaseBeanCopier.create(Source.class, Target.class, true);
 * copier.copy(sourceObj, targetObj, (value, target, context) -> {
 *     // 自定义转换逻辑
 *     return convertedValue;
 * });
 * }</pre>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.26 21:43
 * @since 1.0.0
 */
@SuppressWarnings("all")
public abstract class BaseBeanCopier {
    /** Bean复制器的键工厂，用于生成缓存键 */
    private static final BeanCopierKey KEY_FACTORY = (BeanCopierKey) KeyFactory.create(BeanCopierKey.class);
    /** Converter类型的Type描述符，用于字节码生成 */
    private static final Type CONVERTER = TypeUtils.parseType("org.springframework.cglib.core.Converter");
    /** BaseBeanCopier类型的Type描述符，用于字节码生成 */
    private static final Type BEAN_COPIER = TypeUtils.parseType(BaseBeanCopier.class.getName());
    /** copy方法的签名，定义方法的参数和返回类型 */
    private static final Signature COPY = new Signature("copy",
        Type.VOID_TYPE,
        new Type[]{Constants.TYPE_OBJECT, Constants.TYPE_OBJECT, CONVERTER});
    /** Converter.convert方法的签名，用于调用转换器 */
    private static final Signature CONVERT = TypeUtils.parseSignature("Object convert(Object, Class, Object)");

    /**
     * 创建Bean复制器实例（不使用自定义ClassLoader）
     * <p>
     * 使用默认的ClassLoader创建高性能Bean复制器
     * 适用于大部分普通的Bean复制场景
     *
     * @param source       源对象类型
     * @param target       目标对象类型
     * @param useConverter 是否使用类型转换器
     * @return 创建的Bean复制器实例
     * @since 1.0.0
     */
    public static BaseBeanCopier create(Class<?> source, Class<?> target, boolean useConverter) {
        return BaseBeanCopier.create(source, target, null, useConverter);
    }

    /**
     * 创建Bean复制器实例（支持自定义ClassLoader）
     * <p>
     * 支持指定特定的ClassLoader创建Bean复制器
     * 适用于复杂的类加载环境，如模块化应用或OSGi环境
     * <p>
     * 注意：如果classLoader为null，将使用默认的ClassLoader
     *
     * @param source       源对象类型
     * @param target       目标对象类型
     * @param classLoader  自定义的类加载器，可为null
     * @param useConverter 是否使用类型转换器
     * @return 创建的Bean复制器实例
     * @since 1.0.0
     */
    public static BaseBeanCopier create(Class<?> source, Class<?> target, ClassLoader classLoader, boolean useConverter) {
        Generator gen;
        if (classLoader == null) {
            gen = new Generator();
        } else {
            gen = new Generator(classLoader);
        }
        gen.setSource(source);
        gen.setTarget(target);
        gen.setUseConverter(useConverter);
        return gen.create();
    }

    /**
     * 执行Bean属性复制操作
     * <p>
     * 该方法将源对象的属性值复制到目标对象的对应属性中
     * 复制过程中会根据配置决定是否使用转换器进行类型转换
     * <p>
     * 复制规则：
     * - 只复制同名且类型兼容的属性
     * - 支持链式Bean的复制操作
     * - 如果使用转换器，会对每个属性调用转换逻辑
     *
     * @param from      源对象实例
     * @param to        目标对象实例
     * @param converter 类型转换器，如果不使用转换器可为null
     * @since 1.0.0
     */
    public abstract void copy(Object from, Object to, Converter converter);

    /**
     * The interface Bean copier key.
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2019.12.26 21:46
     * @since 1.0.0
     */
    interface BeanCopierKey {
        /**
         * 实例化
         *
         * @param source       源
         * @param target       目标
         * @param useConverter 是否使用转换
         * @return object object
         * @since 1.0.0
         */
        Object newInstance(String source, String target, boolean useConverter);
    }

    /**
     * The type Generator.
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2019.12.26 21:46
     * @since 1.0.0
     */
    public static class Generator extends AbstractClassGenerator<Object> {
        /** SOURCE */
        private static final Source SOURCE = new Source(BaseBeanCopier.class.getName());
        /** Class loader */
        private final ClassLoader classLoader;
        /** Source */
        private Class<?> source;
        /** Target */
        private Class<?> target;
        /** Use converter */
        private boolean useConverter;

        /**
         * Instantiates a new Generator.
         *
         * @since 1.0.0
         */
        Generator() {
            super(SOURCE);
            this.classLoader = null;
        }

        /**
         * Instantiates a new Generator.
         *
         * @param classLoader the class loader
         * @since 1.0.0
         */
        Generator(ClassLoader classLoader) {
            super(SOURCE);
            this.classLoader = classLoader;
        }

        /**
         * Sets source.
         *
         * @param source the source
         * @since 1.0.0
         */
        public void setSource(@NotNull Class<?> source) {
            if (!Modifier.isPublic(source.getModifiers())) {
                this.setNamePrefix(source.getName());
            }
            this.source = source;
        }

        /**
         * Sets target.
         *
         * @param target the target
         * @since 1.0.0
         */
        public void setTarget(@NotNull Class<?> target) {
            if (!Modifier.isPublic(target.getModifiers())) {
                this.setNamePrefix(target.getName());
            }

            this.target = target;
        }

        /**
         * Sets use converter.
         *
         * @param useConverter the use converter
         * @since 1.0.0
         */
        public void setUseConverter(boolean useConverter) {
            this.useConverter = useConverter;
        }

        /**
         * Gets default class loader *
         *
         * @return the default class loader
         * @since 1.0.0
         */
        @Override
        protected ClassLoader getDefaultClassLoader() {
            return this.target.getClassLoader();
        }

        /**
         * Gets protection domain *
         *
         * @return the protection domain
         * @since 1.0.0
         */
        @Override
        protected ProtectionDomain getProtectionDomain() {
            return ReflectUtils.getProtectionDomain(this.source);
        }

        /**
         * First instance object
         *
         * @param type type
         * @return the object
         * @since 1.0.0
         */
        @Override
        protected Object firstInstance(Class type) {
            return ReflectUtils.newInstance(type);
        }

        /**
         * Next instance object
         *
         * @param instance instance
         * @return the object
         * @since 1.0.0
         */
        @Override
        protected Object nextInstance(Object instance) {
            return instance;
        }

        /**
         * Create base bean copier.
         *
         * @return the base bean copier
         * @since 1.0.0
         */
        public BaseBeanCopier create() {
            Object key = KEY_FACTORY.newInstance(this.source.getName(), this.target.getName(), this.useConverter);
            return (BaseBeanCopier) super.create(key);
        }

        /**
         * Generate class *
         *
         * @param v v
         * @since 1.0.0
         */
        @Override
        public void generateClass(ClassVisitor v) {
            Type sourceType = Type.getType(this.source);
            Type targetType = Type.getType(this.target);
            ClassEmitter ce = new ClassEmitter(v);
            ce.begin_class(Constants.V1_2,
                Constants.ACC_PUBLIC,
                this.getClassName(),
                BEAN_COPIER,
                null,
                Constants.SOURCE_FILE);

            EmitUtils.null_constructor(ce);
            CodeEmitter e = ce.begin_method(Constants.ACC_PUBLIC, COPY, null);

            // 支持链式 bean
            PropertyDescriptor[] getters = BeanUtils.getBeanGetters(this.source);
            PropertyDescriptor[] setters = BeanUtils.getBeanSetters(this.target);
            Map<String, Object> names = Maps.newHashMapWithExpectedSize(16);
            for (PropertyDescriptor getter : getters) {
                names.put(getter.getName(), getter);
            }

            Local targetLocal = e.make_local();
            Local sourceLocal = e.make_local();
            e.load_arg(1);
            e.checkcast(targetType);
            e.store_local(targetLocal);
            e.load_arg(0);
            e.checkcast(sourceType);
            e.store_local(sourceLocal);

            for (PropertyDescriptor setter : setters) {
                PropertyDescriptor getter = (PropertyDescriptor) names.get(setter.getName());
                if (getter != null) {
                    MethodInfo read = ReflectUtils.getMethodInfo(getter.getReadMethod());
                    MethodInfo write = ReflectUtils.getMethodInfo(setter.getWriteMethod());
                    if (this.useConverter) {
                        Type setterType = write.getSignature().getArgumentTypes()[0];
                        e.load_local(targetLocal);
                        e.load_arg(2);
                        e.load_local(sourceLocal);
                        e.invoke(read);
                        e.box(read.getSignature().getReturnType());
                        EmitUtils.load_class(e, setterType);
                        e.push(write.getSignature().getName());
                        e.invoke_interface(CONVERTER, CONVERT);
                        e.unbox_or_zero(setterType);
                        e.invoke(write);
                    } else if (compatible(getter, setter)) {
                        // 支持链式 bean
                        e.load_local(targetLocal);
                        e.load_local(sourceLocal);
                        e.invoke(read);
                        e.invoke(write);
                    }
                }
            }
            e.return_value();
            e.end_method();
            ce.end_class();
        }

        /**
         * Compatible boolean
         *
         * @param getter getter
         * @param setter setter
         * @return the boolean
         * @since 1.0.0
         */
        private static boolean compatible(@NotNull PropertyDescriptor getter, @NotNull PropertyDescriptor setter) {
            return setter.getPropertyType().isAssignableFrom(getter.getPropertyType());
        }
    }
}
