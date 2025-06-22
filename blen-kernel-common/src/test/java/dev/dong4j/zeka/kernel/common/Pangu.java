package dev.dong4j.zeka.kernel.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.12.26 20:07
 * @since 1.7.0
 */
public class Pangu {

    /**
     * Pangu
     *
     * @since 1.7.0
     */
    public Pangu() {
    }

    /**
     * CJK: Chinese, Japanese, Korean
     * ANS: Alphabet, Number, Symbol
     */
    private static final Pattern CJK_ANS = Pattern.compile(
        "([\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}])" +
            "([a-z0-9`~@\\$%\\^&\\*\\-_\\+=\\|\\\\/])",
        Pattern.CASE_INSENSITIVE
    );
    /** ANS_CJK */
    private static final Pattern ANS_CJK = Pattern.compile(
        "([a-z0-9`~!\\$%\\^&\\*\\-_\\+=\\|\\\\;:,\\./\\?])" +
            "([\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}])",
        Pattern.CASE_INSENSITIVE
    );

    /** CJK_QUOTE */
    private static final Pattern CJK_QUOTE = Pattern.compile(
        "([\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}])" +
            "([\"'])"
    );
    /** QUOTE_CJK */
    private static final Pattern QUOTE_CJK = Pattern.compile(
        "([\"'])" +
            "([\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}])"
    );
    /** FIX_QUOTE */
    private static final Pattern FIX_QUOTE = Pattern.compile("([\"'])(\\s*)(.+?)(\\s*)([\"'])");

    /** CJK_BRACKET_CJK */
    private static final Pattern CJK_BRACKET_CJK = Pattern.compile(
        "([\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}])" +
            "([\\({\\[]+(.*?)[\\)}\\]]+)" +
            "([\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}])"
    );
    /** CJK_BRACKET */
    private static final Pattern CJK_BRACKET = Pattern.compile(
        "([\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}])" +
            "([\\(\\){}\\[\\]<>])"
    );
    /** BRACKET_CJK */
    private static final Pattern BRACKET_CJK = Pattern.compile(
        "([\\(\\){}\\[\\]<>])" +
            "([\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}])"
    );
    /** FIX_BRACKET */
    private static final Pattern FIX_BRACKET = Pattern.compile("([(\\({\\[)]+)(\\s*)(.+?)(\\s*)([\\)}\\]]+)");

    /** CJK_HASH */
    private static final Pattern CJK_HASH = Pattern.compile(
        "([\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}])" +
            "(#(\\S+))"
    );
    /** HASH_CJK */
    private static final Pattern HASH_CJK = Pattern.compile(
        "((\\S+)#)" +
            "([\\p{InHiragana}\\p{InKatakana}\\p{InBopomofo}\\p{InCJKCompatibilityIdeographs}\\p{InCJKUnifiedIdeographs}])"
    );

    /**
     * Performs a paranoid text spacing on {@code text}.
     *
     * @param text the string you want to process, must not be {@code null}.
     * @return a comfortable and readable version of {@code text} for paranoiac.
     * @since 1.7.0
     */
    public String spacingText(String text) {
        // CJK and quotes
        Matcher cqMatcher = CJK_QUOTE.matcher(text);
        text = cqMatcher.replaceAll("$1 $2");

        Matcher qcMatcher = QUOTE_CJK.matcher(text);
        text = qcMatcher.replaceAll("$1 $2");

        Matcher fixQuoteMatcher = FIX_QUOTE.matcher(text);
        text = fixQuoteMatcher.replaceAll("$1$3$5");

        // CJK and brackets
        String oldText = text;
        Matcher cbcMatcher = CJK_BRACKET_CJK.matcher(text);
        String newText = cbcMatcher.replaceAll("$1 $2 $4");
        text = newText;

        if (oldText.equals(newText)) {
            Matcher cbMatcher = CJK_BRACKET.matcher(text);
            text = cbMatcher.replaceAll("$1 $2");

            Matcher bcMatcher = BRACKET_CJK.matcher(text);
            text = bcMatcher.replaceAll("$1 $2");
        }

        Matcher fixBracketMatcher = FIX_BRACKET.matcher(text);
        text = fixBracketMatcher.replaceAll("$1$3$5");

        // CJK and hash
        Matcher chMatcher = CJK_HASH.matcher(text);
        text = chMatcher.replaceAll("$1 $2");

        Matcher hcMatcher = HASH_CJK.matcher(text);
        text = hcMatcher.replaceAll("$1 $3");

        // CJK and ANS
        Matcher caMatcher = CJK_ANS.matcher(text);
        text = caMatcher.replaceAll("$1 $2");

        Matcher acMatcher = ANS_CJK.matcher(text);
        text = acMatcher.replaceAll("$1 $2");

        return text;
    }

    /**
     * Performs a paranoid text spacing on {@code inputFile} and generate a new file {@code outputFile}.
     *
     * @param inputFile  an existing file to process, must not be {@code null}.
     * @param outputFile the processed file, must not be {@code null}.
     * @throws IOException if an error occurs.
     * @since 1.1.0
     */
    public void spacingFile(File inputFile, File outputFile) throws IOException {
        // TODO: support charset
        outputFile.getParentFile().mkdirs();

        try (FileReader fr = new FileReader(inputFile);
             BufferedReader br = new BufferedReader(fr);

             FileWriter fw = new FileWriter(outputFile, false);
             BufferedWriter bw = new BufferedWriter(fw)) {
            String line = br.readLine(); // readLine() do not contain newline char

            while (line != null) {
                line = this.spacingText(line);

                // TODO: keep file's raw newline char from difference OS platform
                bw.write(line);
                bw.newLine();

                line = br.readLine();
            }
        }
    }

}
