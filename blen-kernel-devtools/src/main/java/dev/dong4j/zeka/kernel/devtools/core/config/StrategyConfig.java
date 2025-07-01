/*
 * Copyright (c) 2011-2020, baomidou (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package dev.dong4j.zeka.kernel.devtools.core.config;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import dev.dong4j.zeka.kernel.common.util.StringUtils;
import dev.dong4j.zeka.kernel.devtools.core.config.po.LikeTable;
import dev.dong4j.zeka.kernel.devtools.core.config.po.TableFill;
import dev.dong4j.zeka.kernel.devtools.core.config.rules.NamingStrategy;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.util.ClassUtils;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * 策略配置项
 *
 * @author YangHu, tangguo, hubin
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2024.04.02 23:58
 * @since 2016 /8/30
 */
@Data
@Accessors(chain = true)
public class StrategyConfig {
    /** CLASS_FIELD_CACHE */
    private static final Map<Class<?>, List<Field>> CLASS_FIELD_CACHE = new ConcurrentHashMap<>();
    /** UNDERLINE */
    public static final char UNDERLINE = '_';
    /**
     * 是否大写命名
     */
    private boolean isCapitalMode = false;
    /**
     * 是否跳过视图
     */
    private boolean skipView = false;
    /**
     * 名称转换
     */
    private INameConvert nameConvert;
    /**
     * 数据库表映射到实体的命名策略
     */
    private NamingStrategy naming = NamingStrategy.no_change;
    /**
     * 数据库表字段映射到实体的命名策略
     * <p>未指定按照 naming 执行</p>
     */
    private NamingStrategy columnNaming = null;
    /**
     * 表前缀
     */
    @Setter(AccessLevel.NONE)
    private final Set<String> tablePrefix = new HashSet<>();
    /**
     * 字段前缀
     */
    @Setter(AccessLevel.NONE)
    private final Set<String> fieldPrefix = new HashSet<>();
    /**
     * 自定义继承的Entity类全称，带包名
     */
    @Setter(AccessLevel.NONE)
    private String superBaseEntityClass;
    /** Super extend entity class */
    @Setter(AccessLevel.NONE)
    private String superExtendEntityClass;
    /** Super with time entity class */
    @Setter(AccessLevel.NONE)
    private String superWithTimeEntityClass;
    /** Super with logic entity class */
    @Setter(AccessLevel.NONE)
    private String superWithLogicEntityClass;
    /**
     * 自定义基础的Entity类，公共字段
     */
    @Setter(AccessLevel.NONE)
    private final Set<String> superEntityColumns = new HashSet<>();
    /**
     * 自定义继承的Mapper类全称，带包名
     */
    private String superMapperClass = ConstVal.SUPER_MAPPER_CLASS;
    /**
     * 自定义继承的Service类全称，带包名
     */
    private String superServiceClass = ConstVal.SUPER_SERVICE_CLASS;
    /**
     * 自定义继承的ServiceImpl类全称，带包名
     */
    private String superServiceImplClass = ConstVal.SUPER_SERVICE_IMPL_CLASS;
    /**
     * 自定义继承的Controller类全称，带包名
     */
    private String superControllerClass;
    /**
     * 需要包含的表名，允许正则表达式（与exclude二选一配置）<br/>
     * 当{@link #enableSqlFilter}为true时，正则表达式无效.
     */
    @Setter(AccessLevel.NONE)
    private final Set<String> include = new HashSet<>();
    /**
     * 需要排除的表名，允许正则表达式<br/>
     * 当{@link #enableSqlFilter}为true时，正则表达式无效.
     */
    @Setter(AccessLevel.NONE)
    private final Set<String> exclude = new HashSet<>();
    /**
     * 实体是否生成 serialVersionUID
     */
    private boolean entitySerialVersionUID = true;
    /**
     * 【实体】是否生成字段常量（默认 false）<br>
     * -----------------------------------<br>
     * public static final String ID = "test_id";
     */
    private boolean entityColumnConstant = false;
    /**
     * 【实体】是否为构建者模型（默认 false）<br>
     * -----------------------------------<br>
     * public User setName(String name) { this.name = name; return this; }
     *
     * @deprecated 3.3.2 {@link #chainModel}
     */
    @Deprecated
    private boolean entityBuilderModel = false;

    /**
     * 【实体】是否为链式模型（默认 false）<br>
     * -----------------------------------<br>
     * public User setName(String name) { this.name = name; return this; }
     *
     * @since 3.3.2
     */
    private boolean chainModel = false;

    /**
     * 【实体】是否为lombok模型（默认 false）<br>
     * <a href="https://projectlombok.org/">document</a>
     */
    private boolean entityLombokModel = false;
    /**
     * Boolean类型字段是否移除is前缀（默认 false）<br>
     * 比如 : 数据库字段名称 : 'is_xxx',类型为 : tinyint. 在映射实体的时候则会去掉is,在实体类中映射最终结果为 xxx
     */
    private boolean entityBooleanColumnRemoveIsPrefix = false;
    /**
     * 生成 <code>@RestController</code> 控制器
     * <pre>
     *      <code>@Controller</code> -> <code>@RestController</code>
     * </pre>
     */
    private boolean restControllerStyle = false;
    /**
     * 驼峰转连字符
     * <pre>
     *      <code>@RequestMapping("/managerUserActionHistory")</code> -> <code>@RequestMapping("/manager-user-action-history")</code>
     * </pre>
     */
    private boolean controllerMappingHyphenStyle = true;
    /**
     * 是否生成实体时，生成字段注解
     */
    private boolean entityTableFieldAnnotationEnable = false;
    /**
     * 乐观锁属性名称
     */
    private String versionFieldName;
    /**
     * 逻辑删除属性名称
     */
    private String logicDeleteFieldName;
    /**
     * 表填充字段
     */
    private List<TableFill> tableFillList = null;
    /**
     * 启用sql过滤，语法不能支持使用sql过滤表的话，可以考虑关闭此开关.
     *
     * @since 3.3.1
     */
    private boolean enableSqlFilter = true;
    /**
     * 包含表名
     *
     * @since 3.3.0
     */
    private LikeTable likeTable;
    /**
     * 不包含表名
     *
     * @since 3.3.0
     */
    private LikeTable notLikeTable;

    /**
     * 大写命名、字段符合大写字母数字下划线命名
     *
     * @param word 待判断字符串
     * @return the boolean
     * @since 2024.2.0
     */
    public boolean isCapitalModeNaming(String word) {
        return isCapitalMode && NamingStrategy.isCapitalMode(word);
    }

    /**
     * 表名称包含指定前缀
     *
     * @param tableName 表名称
     * @return the boolean
     * @since 2024.2.0
     * @deprecated 3.3.2 {@link #startsWithTablePrefix(String)}
     */
    @Deprecated
    public boolean containsTablePrefix(String tableName) {
        return getTablePrefix().stream().anyMatch(tableName::contains);
    }

    /**
     * 表名称匹配表前缀
     *
     * @param tableName 表名称
     * @return the boolean
     * @since 3.3.2
     */
    public boolean startsWithTablePrefix(String tableName) {
        return getTablePrefix().stream().anyMatch(tableName::startsWith);
    }

    /**
     * Gets column naming *
     *
     * @return the column naming
     * @since 2024.2.0
     */
    public NamingStrategy getColumnNaming() {
        // 未指定以 naming 策略为准
        return Optional.ofNullable(columnNaming).orElse(naming);
    }

    /**
     * Sets table prefix *
     *
     * @param tablePrefix table prefix
     * @return the table prefix
     * @since 2024.2.0
     */
    public StrategyConfig setTablePrefix(String... tablePrefix) {
        this.tablePrefix.addAll(Arrays.asList(tablePrefix));
        return this;
    }

    /**
     * Include super entity columns
     *
     * @param fieldName field name
     * @return the boolean
     * @since 2024.2.0
     */
    public boolean includeSuperEntityColumns(String fieldName) {
        // 公共字段判断忽略大小写【 部分数据库大小写不敏感 】
        return superEntityColumns.stream().anyMatch(e -> e.equalsIgnoreCase(fieldName));
    }

    /**
     * Sets super entity columns *
     *
     * @param superEntityColumns super entity columns
     * @return the super entity columns
     * @since 2024.2.0
     */
    public StrategyConfig setSuperEntityColumns(String... superEntityColumns) {
        this.superEntityColumns.addAll(Arrays.asList(superEntityColumns));
        return this;
    }

    /**
     * Sets include *
     *
     * @param include include
     * @return the include
     * @since 2024.2.0
     */
    public StrategyConfig setInclude(String... include) {
        this.include.addAll(Arrays.asList(include));
        return this;
    }

    /**
     * Sets exclude *
     *
     * @param exclude exclude
     * @return the exclude
     * @since 2024.2.0
     */
    public StrategyConfig setExclude(String... exclude) {
        this.exclude.addAll(Arrays.asList(exclude));
        return this;
    }

    /**
     * Sets field prefix *
     *
     * @param fieldPrefixs field prefixs
     * @return the field prefix
     * @since 2024.2.0
     */
    public StrategyConfig setFieldPrefix(String... fieldPrefixs) {
        this.fieldPrefix.addAll(Arrays.asList(fieldPrefixs));
        return this;
    }

    /**
     * 设置实体父类
     *
     * @param superBaseEntityClass 类全名称
     * @return this super entity class
     * @since 2024.2.0
     */
    public StrategyConfig setSuperBaseEntityClass(String superBaseEntityClass) {
        this.superBaseEntityClass = superBaseEntityClass;
        return this;
    }

    /**
     * Sets super extend entity class *
     *
     * @param superExtendEntityClass super extend entity class
     * @return the super extend entity class
     * @since 2024.2.0
     */
    public StrategyConfig setSuperExtendEntityClass(String superExtendEntityClass) {
        this.superExtendEntityClass = superExtendEntityClass;
        return this;
    }

    /**
     * Sets super with time entity class *
     *
     * @param superWithTimeEntityClass super with time entity class
     * @return the super with time entity class
     * @since 2024.2.0
     */
    public StrategyConfig setSuperWithTimeEntityClass(String superWithTimeEntityClass) {
        this.superWithTimeEntityClass = superWithTimeEntityClass;
        return this;
    }

    /**
     * Sets super with logic entity class *
     *
     * @param superWithLogicEntityClass super with logic entity class
     * @return the super with logic entity class
     * @since 2024.2.0
     */
    public StrategyConfig setSuperWithLogicEntityClass(String superWithLogicEntityClass) {
        this.superWithLogicEntityClass = superWithLogicEntityClass;
        return this;
    }

    /**
     * <p>
     * 设置实体父类，该设置自动识别公共字段<br/>
     * 属性 superEntityColumns 改配置无需再次配置
     * </p>
     * <p>
     * 注意！！字段策略要在设置实体父类之前有效
     * </p>
     *
     * @param clazz 实体父类 Class
     * @return super entity class
     * @since 2024.2.0
     */
    public StrategyConfig setSuperBaseEntityClass(Class<?> clazz) {
        this.superBaseEntityClass = clazz.getName();
        return this;
    }

    /**
     * <p>
     * 设置实体父类，该设置自动识别公共字段<br/>
     * 属性 superEntityColumns 改配置无需再次配置
     * </p>
     *
     * @param clazz        实体父类 Class
     * @param columnNaming 字段命名策略
     * @return super entity class
     * @since 2024.2.0
     */
    public StrategyConfig setSuperBaseEntityClass(Class<?> clazz, NamingStrategy columnNaming) {
        this.columnNaming = columnNaming;
        this.superBaseEntityClass = clazz.getName();
        return this;
    }

    /**
     * Sets super service class *
     *
     * @param clazz clazz
     * @return the super service class
     * @since 2024.2.0
     */
    public StrategyConfig setSuperServiceClass(Class<?> clazz) {
        this.superServiceClass = clazz.getName();
        return this;
    }

    /**
     * Sets super service class *
     *
     * @param superServiceClass super service class
     * @return the super service class
     * @since 2024.2.0
     */
    public StrategyConfig setSuperServiceClass(String superServiceClass) {
        this.superServiceClass = superServiceClass;
        return this;
    }

    /**
     * Sets super service impl class *
     *
     * @param clazz clazz
     * @return the super service impl class
     * @since 2024.2.0
     */
    public StrategyConfig setSuperServiceImplClass(Class<?> clazz) {
        this.superServiceImplClass = clazz.getName();
        return this;
    }

    /**
     * Sets super service impl class *
     *
     * @param superServiceImplClass super service impl class
     * @return the super service impl class
     * @since 2024.2.0
     */
    public StrategyConfig setSuperServiceImplClass(String superServiceImplClass) {
        this.superServiceImplClass = superServiceImplClass;
        return this;
    }

    /**
     * Sets super controller class *
     *
     * @param clazz clazz
     * @return the super controller class
     * @since 2024.2.0
     */
    public StrategyConfig setSuperControllerClass(Class<?> clazz) {
        this.superControllerClass = clazz.getName();
        return this;
    }

    /**
     * Sets super controller class *
     *
     * @param superControllerClass super controller class
     * @return the super controller class
     * @since 2024.2.0
     */
    public StrategyConfig setSuperControllerClass(String superControllerClass) {
        this.superControllerClass = superControllerClass;
        return this;
    }

    /**
     * <p>
     * 父类 Class 反射属性转换为公共字段
     * </p>
     *
     * @param clazz 实体父类 Class
     * @since 2024.2.0
     */
    protected void convertSuperEntityColumns(Class<?> clazz) {
        List<Field> fields = getAllFields(clazz);
        this.superEntityColumns.addAll(fields.stream().map(field -> {
            TableId tableId = field.getAnnotation(TableId.class);
            if (tableId != null && StringUtils.isNotBlank(tableId.value())) {
                return tableId.value();
            }
            TableField tableField = field.getAnnotation(TableField.class);
            if (tableField != null && StringUtils.isNotBlank(tableField.value())) {
                return tableField.value();
            }
            if (null == columnNaming || columnNaming == NamingStrategy.no_change) {
                return field.getName();
            }
            return camelToUnderline(field.getName());
        }).collect(Collectors.toSet()));
    }


    /**
     * Gets super entity columns *
     *
     * @return the super entity columns
     * @since 2024.2.0
     */
    public Set<String> getSuperEntityColumns() {
        if (StringUtils.isNotBlank(this.superBaseEntityClass)) {
            try {
                Class<?> superEntity = toClassConfident(this.superBaseEntityClass);
                convertSuperEntityColumns(superEntity);
            } catch (Exception e) {
                // 当父类实体存在类加载器的时候,识别父类实体字段，不存在的情况就只有通过指定superEntityColumns属性了。
            }
        }
        return this.superEntityColumns;
    }

    /**
     * Camel to underline
     *
     * @param param param
     * @return the string
     * @since 2024.2.0
     */
    public static String camelToUnderline(String param) {
        if (StringUtils.isBlank(param)) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c) && i > 0) {
                sb.append(UNDERLINE);
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }

    /**
     * To class confident
     *
     * @param name name
     * @return the class
     * @since 2024.2.0
     */
    public static Class<?> toClassConfident(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets all fields *
     *
     * @param clazz clazz
     * @return the all fields
     * @since 2024.2.0
     */
    public static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fieldList = getFieldList(ClassUtils.getUserClass(clazz));
        return fieldList.stream()
            .filter(field -> {
                /* 过滤注解非表字段属性 */
                TableField tableField = field.getAnnotation(TableField.class);
                return (tableField == null || tableField.exist());
            }).collect(toList());
    }

    /**
     * Gets field list *
     *
     * @param clazz clazz
     * @return the field list
     * @since 2024.2.0
     */
    public static List<Field> getFieldList(Class<?> clazz) {
        if (Objects.isNull(clazz)) {
            return Collections.emptyList();
        }
        return computeIfAbsent(CLASS_FIELD_CACHE, clazz, k -> {
            Field[] fields = k.getDeclaredFields();
            List<Field> superFields = new ArrayList<>();
            Class<?> currentClass = k.getSuperclass();
            while (currentClass != null) {
                Field[] declaredFields = currentClass.getDeclaredFields();
                Collections.addAll(superFields, declaredFields);
                currentClass = currentClass.getSuperclass();
            }
            /* 排除重载属性 */
            Map<String, Field> fieldMap = excludeOverrideSuperField(fields, superFields);
            /*
             * 重写父类属性过滤后处理忽略部分，支持过滤父类属性功能
             * 场景：中间表不需要记录创建时间，忽略父类 createTime 公共属性
             * 中间表实体重写父类属性 ` private transient Date createTime; `
             */
            return fieldMap.values().stream()
                /* 过滤静态属性 */
                .filter(f -> !Modifier.isStatic(f.getModifiers()))
                /* 过滤 transient关键字修饰的属性 */
                .filter(f -> !Modifier.isTransient(f.getModifiers()))
                .collect(Collectors.toList());
        });
    }

    /**
     * Exclude override super field
     *
     * @param fields         fields
     * @param superFieldList super field list
     * @return the map
     * @since 2024.2.0
     */
    public static Map<String, Field> excludeOverrideSuperField(Field[] fields, List<Field> superFieldList) {
        // 子类属性
        Map<String, Field> fieldMap = Stream.of(fields).collect(toMap(Field::getName, identity(),
            (u, v) -> {
                throw new IllegalStateException(String.format("Duplicate key %s", u));
            },
            LinkedHashMap::new));
        superFieldList.stream().filter(field -> !fieldMap.containsKey(field.getName()))
            .forEach(f -> fieldMap.put(f.getName(), f));
        return fieldMap;
    }

    /**
     * Compute if absent
     *
     * @param <K>               parameter
     * @param <V>               parameter
     * @param concurrentHashMap concurrent hash map
     * @param key               key
     * @param mappingFunction   mapping function
     * @return the v
     * @since 2024.2.0
     */
    public static <K, V> V computeIfAbsent(Map<K, V> concurrentHashMap, K key, Function<? super K, ? extends V> mappingFunction) {
        V v = concurrentHashMap.get(key);
        if (v != null) {
            return v;
        }
        return concurrentHashMap.computeIfAbsent(key, mappingFunction);
    }
}
