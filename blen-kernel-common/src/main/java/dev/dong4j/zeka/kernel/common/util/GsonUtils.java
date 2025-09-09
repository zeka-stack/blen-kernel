package dev.dong4j.zeka.kernel.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dev.dong4j.zeka.kernel.common.asserts.Assertions;
import dev.dong4j.zeka.kernel.common.constant.ConfigDefaultValue;
import dev.dong4j.zeka.kernel.common.enums.SerializeEnum;
import dev.dong4j.zeka.kernel.common.type.ConsumerObjectTypeAdapter;
import dev.dong4j.zeka.kernel.common.type.GsonEnumTypeAdapter;
import dev.dong4j.zeka.kernel.common.type.TypeBuilder;
import dev.dong4j.zeka.kernel.common.type.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * <p>GSON 封装, 简化 json 操作.
 * <p>List, Map 时间格式统一为 DEFAULT_TIMEFORMAT.
 * <p>此工具类只适合简单的 json 操作, 如果是复杂且严格的 json 推荐使用 {@link Jsons}.
 * <p>主要功能：
 * <ul>
 *     <li>对象与JSON字符串互转</li>
 *     <li>JSON美化输出</li>
 *     <li>泛型类型处理</li>
 *     <li>枚举类型序列化/反序列化</li>
 *     <li>线程安全的Gson实例管理</li>
 * </ul>
 * <p>使用示例：
 * <pre>
 * // 对象转JSON
 * Person person = new Person("张三", 25);
 * String json = GsonUtils.toJson(person);
 *
 * // JSON转对象
 * Person parsed = GsonUtils.fromJson(json, Person.class);
 *
 * // 美化JSON输出
 * String prettyJson = GsonUtils.toDebugJson(person);
 *
 * // 处理泛型类型
 * List<Person> list = new ArrayList<>();
 * list.add(person);
 * String jsonList = GsonUtils.toJson(list);
 * List<Person> parsedList = GsonUtils.jsonToList(jsonList, Person.class);
 *
 * // JSON转Map
 * Map<String, Object> map = GsonUtils.jsonToMap(json);
 * </pre>
 * <p>技术特性：
 * <ul>
 *     <li>基于Google Gson库实现</li>
 *     <li>提供线程安全的Gson实例</li>
 *     <li>支持自定义日期格式</li>
 *     <li>提供美化输出功能</li>
 *     <li>支持泛型类型处理</li>
 *     <li>集成枚举类型处理</li>
 *     <li>支持复杂对象的序列化/反序列化</li>
 * </ul>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:17
 * @since 1.0.0
 */
@Slf4j
@UtilityClass
public final class GsonUtils {
    /** DEFAULT_TIMEFORMAT */
    private static final String DEFAULT_TIMEFORMAT = ConfigDefaultValue.DEFAULT_DATE_FORMAT;
    /** GSON */
    private static final Gson GSON = StaticInnerClass.INSTANCE;
    /** ENUM_BUILDER */
    private static final GsonBuilder ENUM_BUILDER = setDateFormat(new GsonBuilder());
    /** ENUM_TYPE_ADAPTER */
    private static final Map<Class<?>, GsonEnumTypeAdapter<?>> ENUM_TYPE_ADAPTER = new HashMap<>();
    /** Type token cache */
    private final Map<com.google.gson.reflect.TypeToken<?>, TypeAdapter<?>> typeTokenCache = new ConcurrentHashMap<>();
    /** Calls */
    private final ThreadLocal<Map<com.google.gson.reflect.TypeToken<?>, FutureTypeAdapter<?>>> calls = new ThreadLocal<>();
    /** NULL_KEY_SURROGATE */
    private static final com.google.gson.reflect.TypeToken<?> NULL_KEY_SURROGATE = com.google.gson.reflect.TypeToken.get(Object.class);

    static {
        init();
    }

    /**
     * 扫描所有的 {@link SerializeEnum} 枚举类, 注册到 Gson 的全局枚举解析器
     *
     * @since 1.0.0
     */
    @SuppressWarnings("all")
    private static void init() {
        ConfigurationBuilder build = ConfigurationBuilder.build(ConfigDefaultValue.BASE_PACKAGES,
            new SubTypesScanner(false));
        build.setExpandSuperTypes(false);
        Reflections reflections = new Reflections(build);

        Set<Class<? extends SerializeEnum>> subTypesOf = reflections.getSubTypesOf(SerializeEnum.class);
        if (subTypesOf != null) {
            for (Class<? extends SerializeEnum> klass : subTypesOf) {
                if (klass.isInterface() || !klass.isEnum()) {
                    continue;
                }
                GsonEnumTypeAdapter<? extends SerializeEnum> gsonEnumTypeAdapter = new GsonEnumTypeAdapter(klass);
                ENUM_BUILDER.registerTypeAdapter(klass, gsonEnumTypeAdapter);
                ENUM_TYPE_ADAPTER.put(klass, gsonEnumTypeAdapter);
                if (log.isTraceEnabled()) {
                    log.trace("向 Gson 注册 SerializeEnum 子类: {}", klass);
                }
            }
        }
    }

    /**
     * Build enum enhance
     *
     * @param builder builder
     * @since 1.0.0
     */
    public static void buildEnumEnhance(GsonBuilder builder) {
        ENUM_TYPE_ADAPTER.forEach((k, v) -> setDateFormat(builder).registerTypeAdapter(k, v));
    }

    /**
     * To json string.
     * 将对象转换成 json 不格式化
     *
     * @param object the object     转成 json 的对象
     * @return the string           json 字符串
     * @since 1.0.0
     */
    @Contract("null -> fail")
    public static String toJson(Object object) {
        return toJson(object, false);
    }

    /**
     * To json string.
     * 将对象转换成 json, 需要设置是否格式化, 不能直接使用 GSON
     *
     * @param object   the object       转成 json 的对象
     * @param isPretty the is pretty    是否格式化
     * @return the string               json 字符串
     * @since 1.0.0
     */
    @Contract("null, _ -> fail")
    public static String toJson(Object object, boolean isPretty) {
        return toJson(object, isPretty, false);
    }

    /**
     * To json
     *
     * @param object   object
     * @param isPretty is pretty
     * @param isEnum   is enum
     * @return the string
     * @since 1.0.0
     */
    public static String toJson(Object object, boolean isPretty, boolean isEnum) {
        Assertions.notNull(object);
        GsonBuilder gsonBuilder = setDateFormat(new GsonBuilder());
        if (isPretty) {
            gsonBuilder.setPrettyPrinting();
        }

        if (isEnum) {
            buildEnumEnhance(gsonBuilder);
        }

        return gsonBuilder.create().toJson(object);
    }

    /**
     * Sets date format *
     *
     * @param gsonBuilder gson builder
     * @return the date format
     * @since 1.0.0
     */
    @NotNull
    private static GsonBuilder setDateFormat(@NotNull GsonBuilder gsonBuilder) {
        gsonBuilder.registerTypeAdapterFactory(ConsumerObjectTypeAdapter.FACTORY);
        return gsonBuilder.setDateFormat(DEFAULT_TIMEFORMAT);
    }


    /**
     * To debug json string.
     * 用输出 debug 日志时默认转换成格式化后的 json 字符串
     *
     * @param object the object
     * @return the string
     * @since 1.0.0
     */
    @Contract("null -> fail")
    public static String toDebugJson(Object object) {
        return toDebugJson(object, false);
    }

    /**
     * To debug json
     *
     * @param object object
     * @param isEnum is enum
     * @return the string
     * @since 1.0.0
     */
    public static String toDebugJson(Object object, boolean isEnum) {
        if (ObjectUtils.isEmpty(object)) {
            return StringPool.NULL_STRING;
        }
        if (object instanceof String) {
            try {
                object = JsonParser.parseString((String) object).getAsJsonObject();
            } catch (Exception e) {
                log.debug("Not a JSON Object: {}", object);
            }
        }
        return toJson(object, true, isEnum);
    }

    /**
     * To json string.
     * 将对象中时间转换为特定格式, 需要设置时间格式, 不能使用 GSON
     *
     * @param object     the object         转成 json 的对象
     * @param dateFormat the date format    时间格式
     * @return the string
     * @since 1.0.0
     */
    @Contract("null, _ -> fail")
    public static String toJson(Object object, String dateFormat) {
        Assertions.notNull(object);
        GsonBuilder gsonBuilder = setDateFormat(new GsonBuilder(), dateFormat);
        return gsonBuilder.create().toJson(object);
    }

    /**
     * Sets date format *
     *
     * @param gsonBuilder gson builder
     * @param dateFormat  date format
     * @return the date format
     * @since 1.0.0
     */
    @NotNull
    private static GsonBuilder setDateFormat(@NotNull GsonBuilder gsonBuilder, String dateFormat) {
        if (!StringUtils.hasText(dateFormat)) {
            dateFormat = DEFAULT_TIMEFORMAT;
        }
        return gsonBuilder.setDateFormat(dateFormat);
    }

    /**
     * From json to object t.
     * 将 json 转换为 bean
     *
     * @param <T>   the type parameter      bean 类型
     * @param json  the json                json 字符串
     * @param clazz the clazz               class 类型
     * @return the t
     * @since 1.0.0
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        Assertions.notBlank(json);
        Type type = TypeBuilder
            .newInstance(clazz)
            .build();
        return fromJson(json, type);
    }

    /**
     * From json
     *
     * @param <T>  parameter
     * @param json json
     * @param type type
     * @return the t
     * @since 1.0.0
     */
    public static <T> T fromJson(String json, Type type) {
        return fromJson(json, type, false);
    }

    /**
     * From json
     *
     * @param <T>    parameter
     * @param json   json
     * @param type   type
     * @param isEnum is enum
     * @return the t
     * @since 1.0.0
     */
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public static <T> T fromJson(String json, Type type, boolean isEnum) {
        Assertions.notBlank(json);

        Gson gson;
        if (isEnum) {
            gson = ENUM_BUILDER.create();
        } else {
            gson = GSON;
        }

        com.google.gson.reflect.TypeToken<T> typeToken =
            (com.google.gson.reflect.TypeToken<T>) com.google.gson.reflect.TypeToken.get(type);
        TypeAdapter<?> typeAdapter = getAdapter(typeToken, gson);
        if (typeAdapter != null) {
            return (T) typeAdapter.fromJson(json);
        }
        return gson.fromJson(json, type);
    }

    /**
     * Gets adapter *
     *
     * @param <T>  parameter
     * @param type type
     * @param gson gson
     * @return the adapter
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    private <T> TypeAdapter<T> getAdapter(com.google.gson.reflect.TypeToken<T> type, Gson gson) {
        TypeAdapter<?> cached = typeTokenCache.get(type == null ? NULL_KEY_SURROGATE : type);
        if (cached != null) {
            return (TypeAdapter<T>) cached;
        }

        Map<com.google.gson.reflect.TypeToken<?>, FutureTypeAdapter<?>> threadCalls = calls.get();
        boolean requiresThreadLocalCleanup = false;
        if (threadCalls == null) {
            threadCalls = new HashMap<>(16);
            calls.set(threadCalls);
            requiresThreadLocalCleanup = true;
        }

        // the key and value type parameters always agree
        FutureTypeAdapter<T> ongoingCall = (FutureTypeAdapter<T>) threadCalls.get(type);
        if (ongoingCall != null) {
            return ongoingCall;
        }

        try {
            FutureTypeAdapter<T> call = new FutureTypeAdapter<>();
            threadCalls.put(type, call);

            // 如果是 Object 类型, 则使用自定义的 ConsumerObjectTypeAdapter
            if (type != null && gson.getAdapter(type) instanceof com.google.gson.internal.bind.ObjectTypeAdapter) {
                TypeAdapter<T> typeAdapter = ConsumerObjectTypeAdapter.FACTORY.create(gson, type);
                typeTokenCache.put(type, typeAdapter);
                return typeAdapter;
            }
            return null;
        } finally {
            threadCalls.remove(type);

            if (requiresThreadLocalCleanup) {
                calls.remove();
            }
        }
    }

    /**
     * <p>Description: </p>
     *
     * @param <T> parameter
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2021.01.05 23:14
     * @since 1.0.0
     */
    static class FutureTypeAdapter<T> extends TypeAdapter<T> {
        /** Delegate */
        private TypeAdapter<T> delegate;

        /**
         * Sets delegate *
         *
         * @param typeAdapter type adapter
         * @since 1.0.0
         */
        public void setDelegate(TypeAdapter<T> typeAdapter) {
            if (this.delegate != null) {
                throw new AssertionError();
            }
            this.delegate = typeAdapter;
        }

        /**
         * Read
         *
         * @param in in
         * @return the t
         * @throws IOException io exception
         * @since 1.0.0
         */
        @Override
        public T read(JsonReader in) throws IOException {
            if (this.delegate == null) {
                throw new IllegalStateException();
            }
            return this.delegate.read(in);
        }

        /**
         * Write
         *
         * @param out   out
         * @param value value
         * @throws IOException io exception
         * @since 1.0.0
         */
        @Override
        public void write(JsonWriter out, T value) throws IOException {
            if (this.delegate == null) {
                throw new IllegalStateException();
            }
            this.delegate.write(out, value);
        }
    }

    /**
     * Json to list list.
     *
     * @param json the json
     * @return the list
     * @since 1.0.0
     */
    public static List<String> jsonToList(String json) {
        return jsonToList(json, String.class);
    }

    /**
     * From json to list list.
     * 将 json 转换为 List
     * {@code
     * List<String> list = JsonUtils.jsonToList(json, String.class);
     * List<User> userList = JsonUtils.jsonToList(json, User.class);
     * }********
     *
     * @param <T>   the type parameter
     * @param json  the json
     * @param clazz the clazz
     * @return the list
     * @since 1.0.0
     */
    public static <T> List<T> jsonToList(String json, Class<T> clazz) {
        Assertions.notBlank(json);
        Type type = TypeBuilder
            .newInstance(List.class)
            .addTypeParam(clazz)
            .build();
        return GSON.fromJson(json, type);
    }

    /**
     * 转成list中有map的
     *
     * @param <K>  the type parameter
     * @param <T>  the type parameter
     * @param json the json
     * @return list list
     * @since 1.0.0
     */
    @Contract("null -> null")
    public static <K, T> List<Map<K, T>> jsonToListMaps(String json) {
        return GSON.fromJson(json, new TypeToken<List<Map<K, T>>>() {
        }.getType());
    }

    /**
     * From json to map map.
     * 通过 Type 转换成 Map
     * <code>
     * </code>
     *
     * @param <K>  the type parameter
     * @param <V>  the type parameter
     * @param json the json     json 字符串
     * @return the map
     * @since 1.0.0
     */
    public static <K, V> Map<K, V> jsonToMap(String json) {
        Assertions.notBlank(json);
        return GSON.fromJson(json, new TypeToken<Map<K, V>>() {
        }.getType());
    }

    /**
     * 把将由逗号分隔符组成的字符串转换成简单的Json格式的字符串
     *
     * @param src the src
     * @return string string
     * @since 1.0.0
     */
    public static String stringToJson(String src) {
        return stringToJson(src, StringPool.COMMA);
    }

    /**
     * 把将由 separation 分隔符组成的字符串转换成简单的Json格式的字符串
     *
     * @param src        the src
     * @param separation the separation
     * @return the string
     * @since 1.0.0
     */
    public static String stringToJson(String src, String separation) {
        Assertions.notBlank(src);
        String[] srcs = src.split(separation);
        List<String> srcList = new ArrayList<>(Arrays.asList(srcs));
        return GSON.toJson(srcList);
    }

    /**
     * <p>Description: </p>
     *
     * @author dong4j
     * @version 1.0.0
     * @email "mailto:dong4j@gmail.com"
     * @date 2020.01.27 18:17
     * @since 1.0.0
     */
    private static class StaticInnerClass {
        /** INSTANCE */
        private static final Gson INSTANCE = setDateFormat(new GsonBuilder()).create();
    }
}
