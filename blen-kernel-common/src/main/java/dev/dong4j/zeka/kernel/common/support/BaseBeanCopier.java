package dev.dong4j.zeka.kernel.common.support;

import com.google.common.collect.Maps;
import dev.dong4j.zeka.kernel.common.util.BeanUtils;
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

import java.beans.PropertyDescriptor;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;
import java.util.Map;

/**
 * <p>Description: spring cglib 修改 </p>
 * 1. 支持链式 bean
 * 2. 自定义的 BeanCopier 解决 spring boot 和 cglib ClassLoader classLoader 不一致的问题
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.26 21:46
 * @since 1.0.0
 */
@SuppressWarnings("all")
public abstract class BaseBeanCopier {
    /** KEY_FACTORY */
    private static final BeanCopierKey KEY_FACTORY = (BeanCopierKey) KeyFactory.create(BeanCopierKey.class);
    /** CONVERTER */
    private static final Type CONVERTER = TypeUtils.parseType("org.springframework.cglib.core.Converter");
    /** BEAN_COPIER */
    private static final Type BEAN_COPIER = TypeUtils.parseType(BaseBeanCopier.class.getName());
    /** COPY */
    private static final Signature COPY = new Signature("copy",
        Type.VOID_TYPE,
        new Type[]{Constants.TYPE_OBJECT, Constants.TYPE_OBJECT, CONVERTER});
    /** CONVERT */
    private static final Signature CONVERT = TypeUtils.parseSignature("Object convert(Object, Class, Object)");

    /**
     * Create base bean copier.
     *
     * @param source       the source
     * @param target       the target
     * @param useConverter the use converter
     * @return the base bean copier
     * @since 1.0.0
     */
    public static BaseBeanCopier create(Class<?> source, Class<?> target, boolean useConverter) {
        return BaseBeanCopier.create(source, target, null, useConverter);
    }

    /**
     * Create base bean copier.
     *
     * @param source       the source
     * @param target       the target
     * @param classLoader  the class loader
     * @param useConverter the use converter
     * @return the base bean copier
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
     * 拷贝
     *
     * @param from      源
     * @param to        目标
     * @param converter 转换器
     * @since 1.0.0
     */
    public abstract void copy(Object from, Object to, Converter converter);

    /**
     * The interface Bean copier key.
     *
     * @author dong4j
     * @version 1.2.3
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
     * @version 1.2.3
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
