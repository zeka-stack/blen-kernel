package dev.dong4j.zeka.kernel.validation.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Nullable;

/**
 * 正则表达式工具类，提供常用的正则表达式常量和匹配方法
 *
 * 集成了大量常用的正则表达式常量，包括数字、字符、日期、电话、邮箱等验证
 * 提供便捷的正则匹配和查找方法，简化数据验证和字符串处理操作
 *
 * 主要功能：
 * - 数字相关正则（整数、小数、货币等）
 * - 字符相关正则（中文、英文、特殊字符等）
 * - 联系方式正则（手机号、邮箱、电话、QQ等）
 * - 证件相关正则（身份证、车牌号等）
 * - 网络相关正则（IP、域名、URL等）
 * - 日期时间正则验证
 * - 用户信息正则（用户名、密码等）
 * - 文件相关正则验证
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2019.12.25 01:58
 * @since 1.0.0
 */
@UtilityClass
@SuppressWarnings("all")
public class RegexUtils {
    /** 回车, 水平制表符, 空格, 换行 */
    public static final String BLANK_SYMBOL = "\\s*|\t|\r|\n";
    /** 数字: */
    public static final String NUMBER = "^[0-9]*$";
    /** n位的数字: */
    public static final String NUMBER_N = "^\\d{n}$";
    /** 至少n位的数字: */
    public static final String NUMBER_LEAST_N = "^\\d{n,}$";
    /** m-n位的数字: */
    public static final String NUMBER_M_N = "^\\d{m,n}$";
    /** 零和非零开头的数字: */
    public static final String ZERO_OR_NO_ZERO_NUMBER = "^(0|[1-9][0-9]*)$";
    /** 非零开头的最多带两位小数的数字: */
    public static final String NO_ZERO_TWO_DECIMAL = "^([1-9][0-9]*)+(.[0-9]{1,2})?$";
    /** 带1-2位小数的正数或负数: */
    public static final String ONE_OR_TWO_NUMBER_TWO_DECIMAL = "^(\\-)?\\d+(\\.\\d{1,2})?$";
    /** 正数、负数、和小数: */
    public static final String ALL_NUMBER = "^(\\-|\\+)?\\d+(\\.\\d+)?$";
    /** 有1~3位小数的正实数: */
    public static final String POSITIVE_REAL_NUMBER_AND_1_3_DECIMAL = "^[0-9]+(.[0-9]{1,3})?$";
    /** 非零的正整数: */
    public static final String NO_ZERO_POSITIVE_INTEGER = "^[1-9]\\d*$ 或 ^([1-9][0-9]*){1,3}$ 或 ^\\+?[1-9][0-9]*$";
    /** 非零的负整数: */
    public static final String NO_ZERO_NEGATIVE_INTEGER = "^-[1-9]\\d*$";
    /** 非负整数: */
    public static final String NOT_NEGATIVE_INTEGER = "^\\d+$";
    /** 非正整数: */
    public static final String NOT_POSITIVE_INTEGER = "^-[1-9]\\d*|0$";
    /** 非负浮点数: */
    public static final String NOT_NEGATIVE_FLOATING_NUMBER = "^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0$";
    /** 非正浮点数: */
    public static final String NOT_POSITIVE_FLOATING_NUMBER = "^(-([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*))|0?\\.0+|0$";
    /** 正浮点数: */
    public static final String POSITIVE_FLOATING_NUMBER = "^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$";
    /** 负浮点数: */
    public static final String NEGATIVE_FLOATING_NUMBER = "^-([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*)$";
    /** 浮点数: */
    public static final String FLOATING_NUMBER = "^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)$";
    /** 汉字: */
    public static final String CHINESE = "^[\\u4e00-\\u9fa5]{0,}$";
    /** 最少出现一个汉字字符: */
    public static final String LEAST_ONE_CHINESE = ".*[\\u4e00-\\u9fa5]+.*";
    /** 长度为3-20的所有字符: */
    public static final String ALL_LIMIT_3_20 = "^.{3,20}$";
    /** 由26个英文字母组成的字符串: */
    public static final String LETTER = "^[A-Za-z]+$";
    /** 由26个大写英文字母组成的字符串: */
    public static final String LETTER_UPPER = "^[A-Z]+$";
    /** 由26个小写英文字母组成的字符串: */
    public static final String LETTER_LOWER = "^[a-z]+$";
    /** 由数字和26个英文字母组成的字符串: */
    public static final String LETTER_NUMBER = "^[A-Za-z0-9]+$";
    /** 由数字、26个英文字母或者下划线组成的字符串: */
    public static final String NO_CHINESE = "^\\w+$";
    /** 中文、英文、数字包括下划线: */
    public static final String ALL = "^[\\u4E00-\\u9FA5A-Za-z0-9_]+$";
    /** 中文、英文、数字但不包括任何符号 */
    public static final String ALL_EXCLUDE_UNDERLINE = "^[\\u4E00-\\u9FA5A-Za-z0-9]+$";
    /** 中文、英文、数字但包括 . 号 */
    public static final String ALL_EXCLUDE_UNDERLINE_EXTEND = "^[\\u4E00-\\u9FA5A-Za-z0-9\\.]+$";
    /** 可以输入含有^%&',;=?$\"等字符: */
    public static final String SPECIAL_CHARACTER = "[^%&',;=?$\\x22]+";
    /** 禁止输入含有~的字符: */
    public static final String NO_WAVE = "[^~\\x22]+";
    /** formatter:off 域名 (http://domain 或者 domain 或者 domain:port) */
    public static final String DOMAIN = "^(?=^.{3,255}$)(http(s)?:\\/\\/)?(www\\.)?[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+(:\\d+)*(\\/\\w+\\.\\w+)*$";
    /** ip */
    public static final String IP = "^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])$";
    /** ip+port */
    public static final String IP_PORT = "^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5]):([0-9]|[1-9]\\d|[1-9]\\d{2}|[1-9]\\d{3}|[1-5]\\d{4}|6[0-4]\\d{3}|65[0-4]\\d{2}|655[0-2]\\d|6553[0-5])$";
    /** formatter:on HH:mm:ss 验证 */
    public static final String TIME = "([0-1][0-9]|2 [0-3]):([0-5][0-9]):([0-5][0-9])";
    /** 电话号码("XXX-XXXXXXX"、"XXXX-XXXXXXXX"、"XXX-XXXXXXX"、"XXX-XXXXXXXX"、"XXXXXXX"和"XXXXXXXX): */
    public static final String LANDLINE = "^(\\(\\d{3,4}-)|\\d{3.4}-)?\\d{7,8}$ ";
    /** 国内电话号码(0511-4405222、021-87888822): */
    public static final String DOMESTIC_LANDLINE = "\\d{3}-\\d{8}|\\d{4}-\\d{7}";
    /** 用户名 */
    public static final String USER_NAME = "^[a-zA-Z\\u4E00-\\u9FA5][a-zA-Z0-9_\\u4E00-\\u9FA5]{1,11}$";
    /** 密码 */
    public static final String USER_PASSWORD = "^.{6,32}$";
    /** 邮箱 */
    public static final String EMAIL = "^\\w+([-+.]*\\w+)*@([\\da-z](-[\\da-z])?)+(\\.{1,2}[a-z]+)+$";
    /** 手机号 */
    public static final String PHONE = "^1[3456789]\\d{9}$";
    /** 手机号或者邮箱 */
    public static final String EMAIL_OR_PHONE = EMAIL + "|" + PHONE;
    /** URL路径 */
    public static final String URL = "^(https?:\\/\\/)?([\\da-z\\.-]+)\\.([a-z\\.]{2,6})(:[\\d]+)?([\\/\\w\\.-]*)*\\/?$";
    /** 身份证校验,初级校验,具体规则有一套算法 */
    public static final String ID_CARD = "^\\d{15}$|^\\d{17}([0-9]|X)$";
    /** xml文件: */
    public static final String XML = "^([a-zA-Z]+-?)+[a-zA-Z0-9]+\\.[x|X][m|M][l|L]$";
    /** 双字节字符: (包括汉字在内,可以用来计算字符串的长度(一个双字节字符长度计2,ASCII字符计1)) */
    public static final String DOUBLE_BYTE_CHARACTER = "[^\\x00-\\xff]";
    /** 空白行的正则表达式: (可以用来删除空白行) */
    public static final String BLANK_LINE = "\\n\\s*\\r";
    /** 首尾空白字符的正则表达式:  (可以用来删除行首行尾的空白字符. 包括空格、制表符、换页符等等) */
    public static final String HEAD_AND_TAIL_BLANK_CHARACTERS = "^\\s*|\\s*$或(^\\s*)|(\\s*$)";
    /** 腾讯QQ号: (腾讯QQ号从10000开始) */
    public static final String QQ = "[1-9][0-9]{4,}";
    /** 中国邮政编码: (中国邮政编码为6位数字) */
    public static final String POST_OFFICE = "[1-9]\\d{5}(?!\\d)";
    /** 15或18位身份证: */
    public static final String ALL_CARD = "^\\d{15}|\\d{18}$";
    /** 15位身份证: */
    public static final String SHORT_CARD = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
    /** 18位身份证: */
    public static final String LONG_CARD = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}$";
    /** 短身份证号码(数字、字母x结尾): */
    public static final String SHORT_CARD_X = "^([0-9]){7,18}(x|X)?$ 或 ^\\d{8,18}|[0-9x]{8,18}|[0-9X]{8,18}?$";
    /** 帐号是否合法(字母开头,允许5-16字节,允许字母数字下划线): */
    public static final String ACCOUNT = "^[a-zA-Z][a-zA-Z0-9_]{4,15}$";
    /** 密码(以字母开头,长度在6~18之间,只能包含字母、数字和下划线): */
    public static final String PASSWORD = "^[a-zA-Z]\\w{5,17}$";
    /** 强密码(必须包含大小写字母和数字的组合,不能使用特殊字符,长度在8-10之间): */
    public static final String STRONG_PASSWORD = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,10}$";
    /** 日期格式: */
    public static final String DATE = "^\\d{4}-\\d{1,2}-\\d{1,2}";
    /** 一年的12个月(01～09和1～12): */
    public static final String MONTH = "^(0?[1-9]|1[0-2])$";
    /** 一个月的31天(01～09和1～31): */
    public static final String DAY = "^((0?[1-9])|((1|2)[0-9])|30|31)$";
    /** 一个0或者一个不以0开头的数字.我们还可以允许开头有一个负号: */
    public static final String ZERO_OR_NO_ZERO_OR_NEGATIVE_NUMBER = "^(0|-?[1-9][0-9]*)$";
    /** 有四种钱的表示形式我们可以接受:"10000.00" 和 "10,000.00", 和没有 "分" 的 "10000" 和 "10,000" */
    public static final String MONEY = "^[0-9]+(.[0-9]+)?$";
    /** 必须说明的是,小数点后面至少应该有1位数,所以"10."是不通过的,但是 "10" 和 "10.2" 是通过的: */
    public static final String LEAST_ONE_DECIMAL = "^[0-9]+(.[0-9]{2})?$";
    /** 1~2 位小数: */
    public static final String POSITIVE_REAL_NUMBER_AND_TWO_DECIMAL = "^[0-9]+(.[0-9]{1,2})?$";
    /** 1,345.2 */
    public static final String COMMA_AND_DECIMAL = "^[0-9]{1,3}(,[0-9]{3})*(.[0-9]{1,2})?$";
    /** 1到3个数字,后面跟着任意个 逗号+3个数字,逗号成为可选,而不是必须: */
    public static final String COMMA_OR_DECIMAL = "^([0-9]+|[0-9]{1,3}(,[0-9]{3})*)(.[0-9]{1,2})?$";
    /** 车牌号 */
    @SuppressWarnings("checkstyle:LineLength")
    public static final String VEHICLE_NUMBER = "^([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[a-zA-Z][-]?(([DF]((?![IO])[a-zA-Z0-9](?![IO]))[0-9]{4})|([0-9]{5}[DF]))|[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[-]?[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1})$";

    /**
     * 编译传入正则表达式和字符串去匹配,忽略大小写
     *
     * @param regex        正则
     * @param beTestString 字符串
     * @return {boolean}
     * @since 1.0.0
     */
    public static boolean match(String regex, String beTestString) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(beTestString);
        return matcher.matches();
    }

    /**
     * 编译传入正则表达式在字符串中寻找,如果匹配到则为true
     *
     * @param regex        正则
     * @param beTestString 字符串
     * @return {boolean}
     * @since 1.0.0
     */
    public static boolean find(String regex, String beTestString) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(beTestString);
        return matcher.find();
    }

    /**
     * 编译传入正则表达式在字符串中寻找,如果找到返回第一个结果
     * 找不到返回null
     *
     * @param regex         正则
     * @param beFoundString 字符串
     * @return {boolean}
     * @since 1.0.0
     */
    @Nullable
    public static String findResult(String regex, String beFoundString) {
        return findResult(regex, beFoundString, 0);
    }

    /**
     * 编译传入正则表达式在字符串中寻找,如果找到返回第i个结果
     * 找不到返回null
     *
     * @param regex         正则
     * @param beFoundString 字符串
     * @return {boolean}
     * @since 1.0.0
     */
    @Nullable
    public static String findResult(String regex, String beFoundString, int groupIndex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(beFoundString);
        if (matcher.find()) {
            return matcher.group(groupIndex);
        }
        return null;
    }

}
