package dev.dong4j.zeka.kernel.common.util;

import lombok.experimental.UtilityClass;

/**
 * <p>静态 String 池.
 * <p>提供常用的字符串常量，避免重复创建字符串对象，提高性能和内存使用效率.
 * <p>主要功能：
 * <ul>
 *     <li>定义常用的符号常量（如标点符号、特殊字符等）</li>
 *     <li>提供统一的字符串常量管理</li>
 *     <li>避免在代码中直接使用硬编码字符串</li>
 *     <li>提高代码可读性和维护性</li>
 * </ul>
 * <p>使用示例：
 * <pre>
 * // 使用常量代替硬编码字符串
 * String url = "http://example.com" + StringPool.SLASH + "api" + StringPool.SLASH + "users";
 * // 等价于: String url = "http://example.com/api/users";
 * </pre>
 * <p>技术特性：
 * <ul>
 *     <li>使用 lombok 的 @UtilityClass 注解，确保类为 final 且构造函数私有</li>
 *     <li>所有字段均为 public static final，提供全局访问</li>
 *     <li>按功能分组定义常量，便于查找和使用</li>
 * </ul>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.26 22:06
 * @since 1.0.0
 */
@UtilityClass
public final class StringPool {

    /** AMPERSAND */
    public static final String AMPERSAND = "&";
    /** AND */
    public static final String AND = "and";
    /** AT */
    public static final String AT = "@";
    /** ASTERISK */
    public static final String ASTERISK = "*";
    /** STAR */
    public static final String STAR = ASTERISK;
    /** SLASH */
    public static final String SLASH = "/";
    /** DOUBLE_SLASH */
    public static final String DOUBLE_SLASH = "#//";
    /** COLON */
    public static final String COLON = ":";
    /** COMMA */
    public static final String COMMA = ",";
    /** DASH */
    public static final String DASH = "-";
    /** DOLLAR */
    public static final String DOLLAR = "$";
    /** DOT */
    public static final String DOT = ".";
    /** EMPTY */
    public static final String EMPTY = "";
    /** EMPTY_JSON */
    public static final String EMPTY_JSON = "{}";
    /** EQUALS */
    public static final String EQUALS = "=";
    /** FALSE */
    public static final String FALSE = "false";
    /** HASH */
    public static final String HASH = "#";
    /** HAT */
    public static final String HAT = "^";
    /** LEFT_BRACE */
    public static final String LEFT_BRACE = "{";
    /** LEFT_BRACKET */
    public static final String LEFT_BRACKET = "(";
    /** LEFT_CHEV */
    public static final String LEFT_CHEV = "<";
    /** NEWLINE */
    public static final String NEWLINE = "\n";
    /** N */
    public static final String N = "n";
    /** NO */
    public static final String NO = "no";
    /** NULL */
    public static final String NULL = "null";
    /** OFF */
    public static final String OFF = "off";
    /** ON */
    public static final String ON = "on";
    /** PERCENT */
    public static final String PERCENT = "%";
    /** PIPE */
    public static final String PIPE = "|";
    /** PLUS */
    public static final String PLUS = "+";
    /** QUESTION_MARK */
    public static final String QUESTION_MARK = "?";
    /** EXCLAMATION_MARK */
    public static final String EXCLAMATION_MARK = "!";
    /** QUOTE */
    public static final String QUOTE = "\"";
    /** RETURN */
    public static final String RETURN = "\r";
    /** TAB */
    public static final String TAB = "\t";
    /** RIGHT_BRACE */
    public static final String RIGHT_BRACE = "}";
    /** RIGHT_BRACKET */
    public static final String RIGHT_BRACKET = ")";
    /** RIGHT_CHEV */
    public static final String RIGHT_CHEV = ">";
    /** SEMICOLON */
    public static final String SEMICOLON = ";";
    /** SINGLE_QUOTE */
    public static final String SINGLE_QUOTE = "'";
    /** BACKTICK */
    public static final String BACKTICK = "`";
    /** SPACE */
    public static final String SPACE = " ";
    /** TILDA */
    public static final String TILDA = "~";
    /** LEFT_SQ_BRACKET */
    public static final String LEFT_SQ_BRACKET = "[";
    /** RIGHT_SQ_BRACKET */
    public static final String RIGHT_SQ_BRACKET = "]";
    /** TRUE */
    public static final String TRUE = "true";
    /** UNDERSCORE */
    public static final String UNDERSCORE = "_";
    /** UTF_8 */
    public static final String UTF_8 = Charsets.UTF_8_NAME;
    /** GBK */
    public static final String GBK = Charsets.GBK_NAME;
    /** ISO_8859_1 */
    public static final String ISO_8859_1 = Charsets.ISO_8859_1_NAME;
    /** Y */
    public static final String Y = "y";
    /** YES */
    public static final String YES = "yes";
    /** ONE */
    public static final String ONE = "1";
    /** ZERO */
    public static final String ZERO = "0";
    /** DOLLAR_LEFT_BRACE */
    public static final String DOLLAR_LEFT_BRACE = "${";
    /** NULL_STRING */
    public static final String NULL_STRING = "N/A";
    /** any_Url_Patterns */
    public static final String ANY_URL_PATTERNS = SLASH + ASTERISK;
    /** DOUBLE_ASTERISK */
    public static final String DOUBLE_ASTERISK = ASTERISK + ASTERISK;
    /** any_PATH */
    public static final String ANY_PATH = SLASH + DOUBLE_ASTERISK;
}