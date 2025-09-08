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

import dev.dong4j.zeka.kernel.common.util.StringPool;
import dev.dong4j.zeka.kernel.common.util.StringUtils;
import dev.dong4j.zeka.kernel.devtools.core.config.ConstVal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 从数据库表到文件的命名策略
 *
 * @author YangHu, tangguo
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2024.04.02 23:58
 * @since 2016 /8/30
 */
public enum NamingStrategy {
    /**
     * 不做任何改变，原样输出
     */
    no_change,
    /**
     * 下划线转驼峰命名
     */
    underline_to_camel;

    /** CAPITAL_MODE */
    private static final Pattern CAPITAL_MODE = Pattern.compile("^[0-9A-Z/_]+$");

    /**
     * Underline to camel
     *
     * @param name name
     * @return the string
     * @since 1.0.0
     */
    public static String underlineToCamel(String name) {
        // 快速检查
        if (StringUtils.isBlank(name)) {
            // 没必要转换
            return StringPool.EMPTY;
        }
        String tempName = name;
        // 大写数字下划线组成转为小写 , 允许混合模式转为小写
        if (isCapitalMode(name) || isMixedMode(name)) {
            tempName = name.toLowerCase();
        }
        StringBuilder result = new StringBuilder();
        // 用下划线将原始字符串分割
        String[] camels = tempName.split(ConstVal.UNDERLINE);
        // 跳过原始字符串中开头、结尾的下换线或双重下划线
        // 处理真正的驼峰片段
        Arrays.stream(camels).filter(camel -> !StringUtils.isBlank(camel)).forEach(camel -> {
            if (StringUtils.isBlank(result)) {
                // 第一个驼峰片段，全部字母都小写
                result.append(camel.toLowerCase());
            } else {
                // 其他的驼峰片段，首字母大写
                result.append(capitalFirst(camel));
            }
        });
        return result.toString();
    }

    /**
     * 去掉指定的前缀
     *
     * @param name   ignore
     * @param prefix ignore
     * @return ignore string
     * @see #removePrefix(String, Set)
     * @since 1.0.0
     * @deprecated 3.4.1
     */
    @Deprecated
    public static String removePrefix(String name, String... prefix) {
        Set<String> set = new HashSet<>(Arrays.asList(prefix));
        return removePrefix(name, set);
    }

    /**
     * Remove prefix
     *
     * @param name   name
     * @param prefix prefix
     * @return the string
     * @since 1.0.0
     */
    public static String removePrefix(String name, Set<String> prefix) {
        if (StringUtils.isBlank(name)) {
            return StringPool.EMPTY;
        }
        if (null != prefix) {
            // 判断是否有匹配的前缀，然后截取前缀
            // 删除前缀
            return prefix.stream().filter(pf -> name.toLowerCase()
                    .matches(StringPool.HAT + pf.toLowerCase() + ".*"))
                .findFirst().map(pf -> name.substring(pf.length())).orElse(name);
        }
        return name;
    }

    /**
     * 判断是否包含prefix
     *
     * @param name   ignore
     * @param prefix ignore
     * @return ignore boolean
     * @since 1.0.0
     */
    public static boolean isPrefixContained(String name, String... prefix) {
        if (null == prefix || StringUtils.isBlank(name)) {
            return false;
        }
        return Arrays.stream(prefix).anyMatch(pf -> name.toLowerCase().matches(StringPool.HAT + pf.toLowerCase() + ".*"));
    }

    /**
     * 去掉下划线前缀且将后半部分转成驼峰格式
     *
     * @param name        ignore
     * @param tablePrefix ignore
     * @return ignore string
     * @see #removePrefix(String, Set)
     * @since 1.0.0
     * @deprecated 3.4.1
     */
    @Deprecated
    public static String removePrefixAndCamel(String name, String[] tablePrefix) {
        return underlineToCamel(removePrefix(name, tablePrefix));
    }

    /**
     * Remove prefix and camel
     *
     * @param name        name
     * @param tablePrefix table prefix
     * @return the string
     * @since 1.0.0
     */
    public static String removePrefixAndCamel(String name, Set<String> tablePrefix) {
        return underlineToCamel(removePrefix(name, tablePrefix));
    }


    /**
     * 实体首字母大写
     *
     * @param name 待转换的字符串
     * @return 转换后的字符串 string
     * @since 1.0.0
     */
    public static String capitalFirst(String name) {
        if (StringUtils.isNotBlank(name)) {
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        return StringPool.EMPTY;
    }

    /**
     * Is capital mode
     *
     * @param word word
     * @return the boolean
     * @since 1.0.0
     */
    public static boolean isCapitalMode(String word) {
        return null != word && CAPITAL_MODE.matcher(word).matches();
    }

    /**
     * Is mixed mode
     *
     * @param word word
     * @return the boolean
     * @since 1.0.0
     */
    private static boolean isMixedMode(String word) {
        return matches(".*[A-Z]+.*", word) && matches(".*[/_]+.*", word);
    }

    /**
     * Matches
     *
     * @param regex regex
     * @param input input
     * @return the boolean
     * @since 1.0.0
     */
    private static boolean matches(String regex, String input) {
        if (null == regex || null == input) {
            return false;
        }
        return Pattern.matches(regex, input);
    }

}
