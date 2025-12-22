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

import org.springframework.util.ClassUtils;

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

import dev.dong4j.zeka.kernel.common.util.StringUtils;
import dev.dong4j.zeka.kernel.devtools.core.config.po.LikeTable;
import dev.dong4j.zeka.kernel.devtools.core.config.po.TableFill;
import dev.dong4j.zeka.kernel.devtools.core.config.rules.NamingStrategy;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * 战略配置类
 * <p> 用于配置代码生成策略的各种参数, 包括命名策略, 表前缀, 字段前缀, 实体类继承关系等.
 * 支持链式调用以方便地设置多个配置项.
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2025.12.22
 * @since 2.0.0
 */
@Data
@Accessors(chain = true)
public class StrategyConfig {
    /** 策略配置项的字段缓存, 用于存储类与其字段的映射关系 */
    private static final Map<Class<?>, List<Field>> CLASS_FIELD_CACHE = new ConcurrentHashMap<>();
    /** 下划线字符 */
    public static final char UNDERLINE = '_';
    /** 是否启用大写命名模式 */
    private boolean isCapitalMode = false;
    /** 是否跳过视图 */
    private boolean skipView = false;
    /** 名称转换策略, 用于字段名的转换操作 */
    private INameConvert nameConvert;
    /** 数据库表映射到实体的命名策略 */
    private NamingStrategy naming = NamingStrategy.no_change;
    /** 数据库表字段映射到实体的命名策略 <p> 未指定按照 naming 执行 </p> */
    private NamingStrategy columnNaming = null;
    /** 表前缀集合, 用于指定需要匹配的表名前缀 */
    @Setter(AccessLevel.NONE)
    private final Set<String> tablePrefix = new HashSet<>();
    /** 字段前缀集合 */
    @Setter(AccessLevel.NONE)
    private final Set<String> fieldPrefix = new HashSet<>();
    /** 自定义基础的 Entity 类全称, 带包名 */
    @Setter(AccessLevel.NONE)
    private String superBaseEntityClass;
    /** 自定义继承的 Entity 扩展类全称, 带包名 */
    @Setter(AccessLevel.NONE)
    private String superExtendEntityClass;
    /** 自定义继承的带有时间戳的 Entity 类全称, 带包名 */
    @Setter(AccessLevel.NONE)
    private String superWithTimeEntityClass;
    /**
     * 逻辑实体类的父类全称, 带包名
     *
     * @since 1.0.0
     */
    @Setter(AccessLevel.NONE)
    private String superWithLogicEntityClass;
    /**
     * 自定义继承的公共字段集合
     * <p>
     * 包含从父类中反射获取的公共字段名称.
     *
     * @since 1.0.0
     */
    @Setter(AccessLevel.NONE)
    private final Set<String> superEntityColumns = new HashSet<>();
    /**
     * 自定义继承的 Mapper 类全称, 带包名
     *
     * @since 1.0.0
     */
    private String superMapperClass = ConstVal.SUPER_MAPPER_CLASS;
    /**
     * 自定义继承的 Service 类全称, 带包名
     *
     * @since 1.0.0
     */
    private String superServiceClass = ConstVal.SUPER_SERVICE_CLASS;
    /** 自定义继承的 ServiceImpl 类全称, 带包名 */
    private String superServiceImplClass = ConstVal.SUPER_SERVICE_IMPL_CLASS;
    /** 自定义继承的 Controller 类全称, 带包名 */
    private String superControllerClass;
    /**
     * 需要包含的表名, 允许正则表达式 (与 exclude 二选一配置)<br/>
     * 当 {@link #enableSqlFilter} 为 true 时, 正则表达式无效.
     */
    @Setter(AccessLevel.NONE)
    private final Set<String> include = new HashSet<>();
    /** 需要排除的表名, 允许正则表达式<br/>当 {@link #enableSqlFilter} 为 true 时, 正则表达式无效. */
    @Setter(AccessLevel.NONE)
    private final Set<String> exclude = new HashSet<>();
    /** 实体是否生成 serialVersionUID */
    @SuppressWarnings("PMD.LowerCamelCaseVariableNamingRule")
    private boolean entitySerialVersionUID = true;
    /** [实体] 是否生成字段常量 (默认 false)<br>public static final String ID="test_id"; */
    private boolean entityColumnConstant = false;
    /** 是否为构建者模型 (默认 false), 已弃用, 请使用 {@link #chainModel} */
    @Deprecated
    private boolean entityBuilderModel = false;

    /** [实体] 是否为链式模型 (默认 false)<br>public User setName(String name){this.name=name;return this;} */
    private boolean chainModel = false;

    /** 是否使用 Lombok 模型生成实体类 */
    private boolean entityLombokModel = false;
    /** 是否移除 Boolean 类型字段的 is 前缀, 默认为 false */
    private boolean entityBooleanColumnRemoveIsPrefix = false;
    /** 是否生成 @RestController 控制器 */
    private boolean restControllerStyle = false;
    /** 控制器映射是否使用连字符风格 (如 manager-user-action-history) */
    private boolean controllerMappingHyphenStyle = true;
    /**
     * 是否生成实体时, 生成字段注解
     *
     * @since 1.0.0
     */
    private boolean entityTableFieldAnnotationEnable = false;
    /** 乐观锁属性名称 */
    private String versionFieldName;
    /** 逻辑删除属性名称 */
    private String logicDeleteFieldName;
    /**
     * 表填充字段列表
     *
     * @since 1.0.0
     */
    private List<TableFill> tableFillList = null;
    /**
     * 启用 SQL 过滤功能
     * <p>
     * 当此选项为 true 时,SQL 查询将根据配置的 include 和 exclude 规则进行过滤.
     *
     * @since 1.0.0
     */
    private boolean enableSqlFilter = true;
    /** 包含的表名, 允许正则表达式 (与 exclude 二选一配置)<br/> 当 {@link #enableSqlFilter} 为 true 时, 正则表达式无效 */
    private LikeTable likeTable;
    /**
     * 不包含表名
     *
     * @since 1.0.0
     */
    private LikeTable notLikeTable;

    /**
     * 判断字符串是否符合大写命名规则
     * <p> 该方法根据当前配置的 isCapitalMode 和 NamingStrategy 判断给定字符串是否符合大写命名规则.</p>
     *
     * @param word 待判断的字符串
     * @return 如果字符串符合大写命名规则则返回 true, 否则返回 false
     * @since 1.0.0
     */
    public boolean isCapitalModeNaming(String word) {
        return isCapitalMode && NamingStrategy.isCapitalMode(word);
    }

    /**
     * 检查表名称是否包含指定的表前缀
     * <p> 遍历表前缀集合, 判断表名称是否包含任一前缀 </p>
     * <p> 该方法已弃用, 请使用 {@link #startsWithTablePrefix(String)} 替代 </p>
     *
     * @param tableName 表名称
     * @return 如果表名称包含任一表前缀则返回 true, 否则返回 false
     * @since 1.0.0
     * @deprecated 3.3.2 {@link #startsWithTablePrefix(String)}
     */
    @Deprecated
    public boolean containsTablePrefix(String tableName) {
        return getTablePrefix().stream().anyMatch(tableName::contains);
    }

    /**
     * 判断表名称是否以指定的表前缀开头
     * <p> 遍历所有配置的表前缀, 检查表名称是否以任一前缀开头 </p>
     *
     * @param tableName 表名称
     * @return 如果表名称以任一表前缀开头, 返回 true; 否则返回 false
     * @since 1.0.0
     */
    public boolean startsWithTablePrefix(String tableName) {
        return getTablePrefix().stream().anyMatch(tableName::startsWith);
    }

    /**
     * 获取列命名策略
     * <p> 如果未显式设置列命名策略, 则使用默认的命名策略 </p>
     *
     * @return 列命名策略, 若未设置则返回默认的命名策略
     * @since 1.0.0
     */
    public NamingStrategy getColumnNaming() {
        // 未指定以 naming 策略为准
        return Optional.ofNullable(columnNaming).orElse(naming);
    }

    /**
     * 设置表前缀
     * <p> 用于配置需要匹配的表前缀, 生成代码时会根据这些前缀过滤表名 </p>
     *
     * @param tablePrefix 表前缀数组, 支持多个前缀
     * @return 当前配置对象, 支持链式调用
     * @since 1.0.0
     */
    public StrategyConfig setTablePrefix(String... tablePrefix) {
        this.tablePrefix.addAll(Arrays.asList(tablePrefix));
        return this;
    }

    /**
     * 判断字段是否包含在超级实体列中
     * <p> 检查给定的字段名是否存在于配置的超级实体列集合中, 不区分大小写.</p>
     *
     * @param fieldName 要检查的字段名称
     * @return 如果字段存在于超级实体列中则返回 true, 否则返回 false
     * @since 1.0.0
     */
    public boolean includeSuperEntityColumns(String fieldName) {
        // 公共字段判断忽略大小写【 部分数据库大小写不敏感 】
        return superEntityColumns.stream().anyMatch(e -> e.equalsIgnoreCase(fieldName));
    }

    /**
     * 设置实体父类的公共字段
     * <p> 将传入的字段名称集合添加到实体父类的公共字段集合中 </p>
     *
     * @param superEntityColumns 公共字段名称数组
     * @return 当前 StrategyConfig 对象, 支持链式调用
     * @since 1.0.0
     */
    public StrategyConfig setSuperEntityColumns(String... superEntityColumns) {
        this.superEntityColumns.addAll(Arrays.asList(superEntityColumns));
        return this;
    }

    /**
     * 设置需要包含的表名, 允许正则表达式(与 exclude 二选一配置)<br/>
     * 当 {@link #enableSqlFilter} 为 true 时, 正则表达式无效.
     *
     * @param include 需要包含的表名数组
     * @return 当前配置对象, 支持链式调用
     * @since 1.0.0
     */
    public StrategyConfig setInclude(String... include) {
        this.include.addAll(Arrays.asList(include));
        return this;
    }

    /**
     * 设置需要排除的表名
     * <p> 将传入的表名添加到 exclude 集合中. 支持正则表达式, 当 {@link #enableSqlFilter} 为 true 时, 正则表达式无效.</p>
     *
     * @param exclude 需要排除的表名数组
     * @return 当前的 StrategyConfig 对象, 以便进行链式调用
     * @since 1.0.0
     */
    public StrategyConfig setExclude(String... exclude) {
        this.exclude.addAll(Arrays.asList(exclude));
        return this;
    }

    /**
     * 设置字段前缀
     * <p> 将给定的字段前缀添加到当前配置中 </p>
     *
     * @param fieldPrefixs 字段前缀数组
     * @return 当前 StrategyConfig 对象, 支持链式调用
     * @since 1.0.0
     */
    public StrategyConfig setFieldPrefix(String... fieldPrefixs) {
        this.fieldPrefix.addAll(Arrays.asList(fieldPrefixs));
        return this;
    }

    /**
     * 设置实体父类
     * <p> 该方法用于设置实体的父类全限定名, 并将其存储在 {@code superBaseEntityClass} 属性中.</p>
     *
     * @param superBaseEntityClass 实体父类的全限定名
     * @return 当前的 StrategyConfig 对象, 以便进行链式调用
     * @since 1.0.0
     */
    public StrategyConfig setSuperBaseEntityClass(String superBaseEntityClass) {
        this.superBaseEntityClass = superBaseEntityClass;
        return this;
    }

    /**
     * 设置实体扩展父类
     *
     * @param superExtendEntityClass 实体扩展父类的全限定名
     * @return 当前 StrategyConfig 对象, 支持链式调用
     * @since 1.0.0
     */
    public StrategyConfig setSuperExtendEntityClass(String superExtendEntityClass) {
        this.superExtendEntityClass = superExtendEntityClass;
        return this;
    }

    /**
     * 设置带有时间字段的实体父类
     * <p> 设置实体父类时, 该类将包含时间相关的字段 (如创建时间, 更新时间等).</p>
     *
     * @param superWithTimeEntityClass 带有时间字段的实体父类全限定名
     * @return 当前的 StrategyConfig 对象, 支持链式调用
     * @since 1.0.0
     */
    public StrategyConfig setSuperWithTimeEntityClass(String superWithTimeEntityClass) {
        this.superWithTimeEntityClass = superWithTimeEntityClass;
        return this;
    }

    /**
     * 设置带有逻辑删除功能的实体类全称
     *
     * @param superWithLogicEntityClass 带有逻辑删除功能的实体类全称
     * @return 当前 StrategyConfig 对象, 支持链式调用
     * @since 1.0.0
     */
    public StrategyConfig setSuperWithLogicEntityClass(String superWithLogicEntityClass) {
        this.superWithLogicEntityClass = superWithLogicEntityClass;
        return this;
    }

    /**
     * 设置实体父类
     * <p> 将传入的类对象转换为全限定类名并设置为实体父类 </p>
     *
     * @param clazz 实体父类 Class
     * @return this 实例, 支持链式调用
     * @since 1.0.0
     */
    public StrategyConfig setSuperBaseEntityClass(Class<?> clazz) {
        this.superBaseEntityClass = clazz.getName();
        return this;
    }

    /**
     * 设置实体父类, 并指定字段命名策略
     * <p> 该方法用于设置实体父类, 并同时指定字段命名策略. 设置后, 属性 superEntityColumns 将根据新的父类自动更新.</p>
     *
     * @param clazz        实体父类 Class
     * @param columnNaming 字段命名策略
     * @return 当前的 StrategyConfig 对象, 支持链式调用
     * @since 1.0.0
     */
    public StrategyConfig setSuperBaseEntityClass(Class<?> clazz, NamingStrategy columnNaming) {
        this.columnNaming = columnNaming;
        this.superBaseEntityClass = clazz.getName();
        return this;
    }

    /**
     * 设置超级 Service 类
     *
     * @param clazz 超级 Service 类的 Class 对象
     * @return 当前 StrategyConfig 实例, 用于链式调用
     * @since 1.0.0
     */
    public StrategyConfig setSuperServiceClass(Class<?> clazz) {
        this.superServiceClass = clazz.getName();
        return this;
    }

    /**
     * 设置自定义继承的 Service 类全称 (带包名)
     * <p> 用于指定生成的 Service 类继承的父类 </p>
     *
     * @param superServiceClass 自定义继承的 Service 类全称 (带包名)
     * @return 当前配置对象, 支持链式调用
     * @since 1.0.0
     */
    public StrategyConfig setSuperServiceClass(String superServiceClass) {
        this.superServiceClass = superServiceClass;
        return this;
    }

    /**
     * 设置自定义继承的 ServiceImpl 类全称, 带包名
     *
     * @param clazz 实体父类 Class
     * @return this
     * @since 1.0.0
     */
    public StrategyConfig setSuperServiceImplClass(Class<?> clazz) {
        this.superServiceImplClass = clazz.getName();
        return this;
    }

    /**
     * 设置自定义继承的 ServiceImpl 类全称, 带包名
     *
     * @param superServiceImplClass 自定义继承的 ServiceImpl 类全称, 带包名
     * @return 当前配置对象, 支持链式调用
     * @since 1.0.0
     */
    public StrategyConfig setSuperServiceImplClass(String superServiceImplClass) {
        this.superServiceImplClass = superServiceImplClass;
        return this;
    }

    /**
     * 设置控制器父类
     * <p> 设置自定义继承的 Controller 类全称, 带包名 </p>
     *
     * @param clazz 控制器父类 Class
     * @return 当前配置对象, 支持链式调用
     * @since 1.0.0
     */
    public StrategyConfig setSuperControllerClass(Class<?> clazz) {
        this.superControllerClass = clazz.getName();
        return this;
    }

    /**
     * 设置超级控制器类全限定名
     *
     * @param superControllerClass 超级控制器类的全限定名称
     * @return 当前 StrategyConfig 实例, 用于链式调用
     * @since 1.0.0
     */
    public StrategyConfig setSuperControllerClass(String superControllerClass) {
        this.superControllerClass = superControllerClass;
        return this;
    }

    /**
     * 将父类 Class 的字段转换为公共字段
     * <p>通过反射获取父类的所有字段, 并根据字段上的注解 (如 {@link TableId},{@link TableField}) 提取字段名, 或根据命名策略转换字段名</p>
     *
     * @param clazz 实体父类 Class
     * @since 1.0.0
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
     * 获取实体父类的公共字段集合
     * <p> 如果设置了实体父类 (superBaseEntityClass), 则会反射获取该类上的字段, 并转换为公共字段名称.</p>
     *
     * @return 实体父类的公共字段集合
     * @since 1.0.0
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
     * 将驼峰命名法转换为下划线命名法
     * <p> 将给定的字符串中的大写字母替换为下划线加小写字母的形式
     *
     * @param param 需要转换的字符串
     * @return 转换后的下划线命名法字符串
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
     * 将类名字符串转换为对应的 Class 对象
     * <p> 如果类名不存在, 则抛出运行时异常
     *
     * @param name 类名字符串
     * @return 对应的 Class 对象
     * @since 1.0.0
     */
    public static Class<?> toClassConfident(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取指定类的所有字段列表, 过滤掉不存在的字段
     * <p> 通过反射获取类的所有字段, 并过滤掉标注了 @TableField 且 exist() 返回 false 的字段
     *
     * @param clazz 指定的类
     * @return 过滤后的字段列表
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
     * 获取指定类的所有字段列表
     * <p> 该方法从给定的类及其所有父类中提取非静态, 非瞬态字段, 并排除被子类重写的父类字段.</p>
     *
     * @param clazz 指定的类
     * @return 包含所有符合条件字段的列表
     * @since 1.0.0
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
     * 排除父类字段覆盖的字段映射
     * <p> 将给定的字段数组转换为字段名到字段对象的映射, 并排除父类字段列表中已存在的字段 </p>
     *
     * @param fields         当前类的字段数组
     * @param superFieldList 父类字段列表
     * @return 包含当前类字段和父类字段的字段映射, 其中父类字段优先级低于当前类字段
     * @since 1.0.0
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
     * 使用给定的键和映射函数计算值, 并将其放入映射中, 如果键不存在.
     * <p> 该方法首先检查映射中是否已存在指定的键, 如果存在则直接返回对应的值; 如果不存在, 则使用提供的映射函数计算值, 并将其放入映射中.</p>
     *
     * @param <K>               映射的键类型
     * @param <V>               映射的值类型
     * @param concurrentHashMap 要操作的并发映射
     * @param key               要获取或计算的键
     * @param mappingFunction   用于计算值的函数, 当键不存在时调用
     * @return 与键关联的值, 如果键已存在则返回现有值, 否则返回新计算的值
     */
    public static <K, V> V computeIfAbsent(Map<K, V> concurrentHashMap, K key, Function<? super K, ? extends V> mappingFunction) {
        V v = concurrentHashMap.get(key);
        if (v != null) {
            return v;
        }
        return concurrentHashMap.computeIfAbsent(key, mappingFunction);
    }
}
