package dev.dong4j.zeka.kernel.common.util;

import lombok.experimental.UtilityClass;

/**
 * <p>char 常量池.
 * <p>提供常用的字符常量，避免重复创建字符对象，提高性能和内存使用效率.
 * <p>主要功能：
 * <ul>
 *     <li>定义常用的字符常量（如字母、标点符号、特殊字符等）</li>
 *     <li>提供统一的字符常量管理</li>
 *     <li>避免在代码中直接使用硬编码字符</li>
 *     <li>提高代码可读性和维护性</li>
 * </ul>
 * <p>使用示例：
 * <pre>
 * // 使用常量代替硬编码字符
 * StringBuilder sb = new StringBuilder();
 * sb.append(CharPool.LEFT_BRACE);  // 添加左花括号
 * sb.append("content");
 * sb.append(CharPool.RIGHT_BRACE); // 添加右花括号
 * // 结果: {content}
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
 * @date 2020.01.27 18:18
 * @since 1.0.0
 */
@UtilityClass
@SuppressWarnings("all")
public class CharPool {
    /** UPPER_A */
    public static final char UPPER_A = 'A';
    /** LOWER_A */
    public static final char LOWER_A = 'a';
    /** UPPER_Z */
    public static final char UPPER_Z = 'Z';
    /** LOWER_Z */
    public static final char LOWER_Z = 'z';
    /** DOT */
    public static final char DOT = '.';
    /** AT */
    public static final char AT = '@';
    /** LEFT_BRACE */
    public static final char LEFT_BRACE = '{';
    /** RIGHT_BRACE */
    public static final char RIGHT_BRACE = '}';
    /** LEFT_BRACKET */
    public static final char LEFT_BRACKET = '(';
    /** RIGHT_BRACKET */
    public static final char RIGHT_BRACKET = ')';
    /** DASH */
    public static final char DASH = '-';
    /** PERCENT */
    public static final char PERCENT = '%';
    /** PIPE */
    public static final char PIPE = '|';
    /** PLUS */
    public static final char PLUS = '+';
    /** QUESTION_MARK */
    public static final char QUESTION_MARK = '?';
    /** EXCLAMATION_MARK */
    public static final char EXCLAMATION_MARK = '!';
    /** EQUALS */
    public static final char EQUALS = '=';
    /** AMPERSAND */
    public static final char AMPERSAND = '&';
    /** ASTERISK */
    public static final char ASTERISK = '*';
    /** STAR */
    public static final char STAR = ASTERISK;
    /** BACK_SLASH */
    public static final char BACK_SLASH = '\\';
    /** COLON */
    public static final char COLON = ':';
    /** COMMA */
    public static final char COMMA = ',';
    /** DOLLAR */
    public static final char DOLLAR = '$';
    /** SLASH */
    public static final char SLASH = '/';
    /** HASH */
    public static final char HASH = '#';
    /** HAT */
    public static final char HAT = '^';
    /** LEFT_CHEV */
    public static final char LEFT_CHEV = '<';
    /** NEWLINE */
    public static final char NEWLINE = '\n';
    /** N */
    public static final char N = 'n';
    /** Y */
    public static final char Y = 'y';
    /** QUOTE */
    public static final char QUOTE = '\"';
    /** RETURN */
    public static final char RETURN = '\r';
    /** TAB */
    public static final char TAB = '\t';
    /** RIGHT_CHEV */
    public static final char RIGHT_CHEV = '>';
    /** SEMICOLON */
    public static final char SEMICOLON = ';';
    /** SINGLE_QUOTE */
    public static final char SINGLE_QUOTE = '\'';
    /** BACKTICK */
    public static final char BACKTICK = '`';
    /** SPACE */
    public static final char SPACE = ' ';
    /** TILDA */
    public static final char TILDA = '~';
    /** LEFT_SQ_BRACKET */
    public static final char LEFT_SQ_BRACKET = '[';
    /** RIGHT_SQ_BRACKET */
    public static final char RIGHT_SQ_BRACKET = ']';
    /** UNDERSCORE */
    public static final char UNDERSCORE = '_';
    /** ONE */
    public static final char ONE = '1';
    /** ZERO */
    public static final char ZERO = '0';
}