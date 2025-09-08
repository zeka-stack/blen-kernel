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
package dev.dong4j.zeka.kernel.devtools.core.config.rules;

/**
 * 表字段类型
 *
 * @author hubin
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2024.04.02 23:58
 * @since 2017 -01-11
 */
public enum DbColumnType implements IColumnType {
    /** Base byte db column type */
// 基本类型
    BASE_BYTE("byte", null),
    /** Base short db column type */
    BASE_SHORT("short", null),
    /** Base char db column type */
    BASE_CHAR("char", null),
    /** Base int db column type */
    BASE_INT("int", null),
    /** Base long db column type */
    BASE_LONG("long", null),
    /** Base float db column type */
    BASE_FLOAT("float", null),
    /** Base double db column type */
    BASE_DOUBLE("double", null),
    /** Base boolean db column type */
    BASE_BOOLEAN("boolean", null),

    /** Byte db column type */
// 包装类型
    BYTE("Byte", null),
    /** Short db column type */
    SHORT("Short", null),
    /** Character db column type */
    CHARACTER("Character", null),
    /** Integer db column type */
    INTEGER("Integer", null),
    /** Long db column type */
    LONG("Long", null),
    /** Float db column type */
    FLOAT("Float", null),
    /** Double db column type */
    DOUBLE("Double", null),
    /** Boolean db column type */
    BOOLEAN("Boolean", null),
    /** String db column type */
    STRING("String", null),

    /** Date sql db column type */
// sql 包下数据类型
    DATE_SQL("Date", "java.sql.Date"),
    /** Time db column type */
    TIME("Time", "java.sql.Time"),
    /** Timestamp db column type */
    TIMESTAMP("Timestamp", "java.sql.Timestamp"),
    /** Blob db column type */
    BLOB("Blob", "java.sql.Blob"),
    /** Clob db column type */
    CLOB("Clob", "java.sql.Clob"),

    /** Local date db column type */
// java8 新时间类型
    LOCAL_DATE("LocalDate", "java.time.LocalDate"),
    /** Local time db column type */
    LOCAL_TIME("LocalTime", "java.time.LocalTime"),
    /** Year db column type */
    YEAR("Year", "java.time.Year"),
    /** Year month db column type */
    YEAR_MONTH("YearMonth", "java.time.YearMonth"),
    /** Local date time db column type */
    LOCAL_DATE_TIME("LocalDateTime", "java.time.LocalDateTime"),
    /** Instant db column type */
    INSTANT("Instant", "java.time.Instant"),

    /** Byte array db column type */
// 其他杂类
    BYTE_ARRAY("byte[]", null),
    /** Object db column type */
    OBJECT("Object", null),
    /** Date db column type */
    DATE("Date", "java.util.Date"),
    /** Big integer db column type */
    BIG_INTEGER("BigInteger", "java.math.BigInteger"),
    /** Big decimal db column type */
    BIG_DECIMAL("BigDecimal", "java.math.BigDecimal");

    /**
     * 类型
     */
    private final String type;

    /**
     * 包路径
     */
    private final String pkg;

    /**
     * Db column type
     *
     * @param type type
     * @param pkg  pkg
     * @since 1.0.0
     */
    DbColumnType(final String type, final String pkg) {
        this.type = type;
        this.pkg = pkg;
    }

    /**
     * Gets type *
     *
     * @return the type
     * @since 1.0.0
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * Gets pkg *
     *
     * @return the pkg
     * @since 1.0.0
     */
    @Override
    public String getPkg() {
        return pkg;
    }
}
