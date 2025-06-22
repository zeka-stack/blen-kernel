package dev.dong4j.zeka.kernel.common.util;

import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:13
 * @since 1.0.0
 */
class StringUtilsTest2 {

    /**
     * Test is any blank *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testIsAnyBlank() throws Exception {
        boolean result = StringUtils.isAnyBlank(null, "", "  ");
        assertTrue(result);
    }

    /**
     * Test is blank *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testIsBlank() throws Exception {
        boolean result = StringUtils.isBlank("");
        assertTrue(result);
    }

    /**
     * Test is none blank *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testIsNoneBlank() throws Exception {
        boolean result1 = StringUtils.isNoneBlank("aa", "aaa");
        assertTrue(result1);

        boolean result2 = StringUtils.isNoneBlank("", "aaa");
        assertFalse(result2);

        boolean result = StringUtils.isAllBlank("", "");
        assertTrue(result);

        boolean result3 = StringUtils.isAllBlank("", "aaa");
        assertFalse(result3);
    }

    /**
     * Test is not blank *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testIsNotBlank() throws Exception {
        boolean result = StringUtils.isNotBlank("null");
        assertTrue(result);
    }

    /**
     * Test is numeric *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testIsNumeric() throws Exception {
        boolean result = StringUtils.isNumeric("2");
        assertTrue(result);
    }

    /**
     * Test join *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testJoin() throws Exception {
        String result = StringUtils.join(Arrays.asList("1", "2"));
        assertEquals("1,2", result);
    }

    /**
     * Test join 2 *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testJoin2() throws Exception {
        String result = StringUtils.join(Arrays.asList("1", "2"), "delim");
        assertEquals("1delim2", result);
    }

    /**
     * Test join 3 *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testJoin3() throws Exception {
        String result = StringUtils.join(new Object[]{"arr"});
        assertEquals("replaceMeWithExpectedResult", result);
    }

    /**
     * Test join 4 *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testJoin4() throws Exception {
        String result = StringUtils.join(new Object[]{"arr"}, "delim");
        assertEquals("replaceMeWithExpectedResult", result);
    }

    /**
     * Test random uuid *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testRandomUUID() throws Exception {
        String result = StringUtils.randomUid();
        assertEquals("replaceMeWithExpectedResult", result);
    }

    /**
     * Test get uuid *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testGetUUID() throws Exception {
        String result = StringUtils.getUid();
        assertEquals("replaceMeWithExpectedResult", result);
    }

    /**
     * Test escape html *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testEscapeHtml() throws Exception {
        String result = StringUtils.escapeHtml("html");
        assertEquals("replaceMeWithExpectedResult", result);
    }

    /**
     * Test clean chars *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testCleanChars() throws Exception {
        String result = StringUtils.cleanChars("txt");
        assertEquals("replaceMeWithExpectedResult", result);
    }

    /**
     * Test format *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testFormat() throws Exception {
        String result = StringUtils.format("{}", "params");
        assertEquals("params", result);
    }

    /**
     * Test indexed format *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testIndexedFormat() throws Exception {
        String result = StringUtils.indexedFormat("{0}", "arguments");
        assertEquals("arguments", result);
    }

    /**
     * Test format 2 *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testFormat2() throws Exception {
        String result = StringUtils.format("{a}:{b}", new HashMap<String, String>(2) {
            private static final long serialVersionUID = -4478024216127456469L;

            {
                this.put("a", "aaa");
                this.put("b", "bbb");
            }
        });
        assertEquals("aaa:bbb", result);
    }

    /**
     * Test split *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testSplit() throws Exception {
        List<String> result = StringUtils.split(null, 'a', 0);
        assertEquals(Collections.singletonList("String"), result);
    }

    /**
     * Test split 2 *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testSplit2() throws Exception {
        List<String> result = StringUtils.split(null, 'a', 0, true, true);
        assertEquals(Collections.singletonList("String"), result);
    }

    /**
     * Test split trim *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testSplitTrim() throws Exception {
        List<String> result = StringUtils.splitTrim(null, 'a');
        assertEquals(Collections.singletonList("String"), result);
    }

    /**
     * Test split trim 2 *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testSplitTrim2() throws Exception {
        List<String> result = StringUtils.splitTrim(null, null);
        assertEquals(Collections.singletonList("String"), result);
    }

    /**
     * Test split 3 *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testSplit3() throws Exception {
        List<String> result = StringUtils.split(null, null, 0, true, true);
        assertEquals(Collections.singletonList("String"), result);
    }

    /**
     * Test split 4 *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testSplit4() throws Exception {
        List<String> result = StringUtils.split(null, 'a', true, true);
        assertEquals(Collections.singletonList("String"), result);
    }

    /**
     * Test split 5 *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testSplit5() throws Exception {
        String[] result = StringUtils.split("a,b", ",");
        assertArrayEquals(new String[]{"a", "b"}, result);
    }

    /**
     * Test split 6 *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testSplit6() throws Exception {
        String[] result = StringUtils.split(null, 0);
        assertArrayEquals(new String[]{"replaceMeWithExpectedResult"}, result);
    }

    /**
     * Test contains *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testContains() throws Exception {
        boolean result = StringUtils.contains(null, 'a');
        assertTrue(result);
    }

    /**
     * Test index of *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testIndexOf() throws Exception {
        int result = StringUtils.indexOf(null, 'a');
        assertEquals(0, result);
    }

    /**
     * Test index of 2 *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testIndexOf2() throws Exception {
        int result = StringUtils.indexOf(null, 'a', 0);
        assertEquals(0, result);
    }

    /**
     * Test index of 3 *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testIndexOf3() throws Exception {
        int result = StringUtils.indexOf(null, 'a', 0, 0);
        assertEquals(0, result);
    }

    /**
     * Test contains any *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testContainsAny() throws Exception {
        boolean result = StringUtils.containsAny("aabbcc", "aa");
        assertTrue(result);
    }

    /**
     * Test contains any ignore case *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testContainsAnyIgnoreCase() throws Exception {
        boolean result = StringUtils.containsAnyIgnoreCase("AAbbCC", "aa");
        assertTrue(result);
    }

    /**
     * Test sub before *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testSubBefore() throws Exception {
        String result = StringUtils.subBefore(null, null, true);
        assertEquals("replaceMeWithExpectedResult", result);
    }

    /**
     * Test sub after *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testSubAfter() throws Exception {
        String result = StringUtils.subAfter(null, null, true);
        assertEquals("replaceMeWithExpectedResult", result);
    }

    /**
     * Test sub between *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testSubBetween() throws Exception {
        String result = StringUtils.subBetween("tagabctag", "tag");
        assertEquals("abc", result);
    }

    /**
     * Test remove prefix *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testRemovePrefix() throws Exception {
        String result = StringUtils.removePrefix(null, null);
        assertEquals("replaceMeWithExpectedResult", result);
    }

    /**
     * Test sub suf *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testSubSuf() throws Exception {
        String result = StringUtils.subSuf(null, 0);
        assertEquals("replaceMeWithExpectedResult", result);
    }

    /**
     * Test remove prefix ignore case *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testRemovePrefixIgnoreCase() throws Exception {
        String result = StringUtils.removePrefixIgnoreCase(null, null);
        assertEquals("replaceMeWithExpectedResult", result);
    }

    /**
     * Test remove suf and lower first *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testRemoveSufAndLowerFirst() throws Exception {
        String result = StringUtils.removeSufAndLowerFirst(null, null);
        assertEquals("replaceMeWithExpectedResult", result);
    }

    /**
     * Test first char to lower *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testFirstCharToLower() throws Exception {
        String result = StringUtils.firstCharToLower("str");
        assertEquals("replaceMeWithExpectedResult", result);
    }

    /**
     * Test remove suffix *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testRemoveSuffix() throws Exception {
        String result = StringUtils.removeSuffix(null, null);
        assertEquals("replaceMeWithExpectedResult", result);
    }

    /**
     * Test remove suffix ignore case *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testRemoveSuffixIgnoreCase() throws Exception {
        String result = StringUtils.removeSuffixIgnoreCase(null, null);
        assertEquals("replaceMeWithExpectedResult", result);
    }

    /**
     * Test first char to upper *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testFirstCharToUpper() throws Exception {
        String result = StringUtils.firstCharToUpper("str");
        assertEquals("replaceMeWithExpectedResult", result);
    }

    /**
     * Test index of ignore case *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testIndexOfIgnoreCase() throws Exception {
        int result = StringUtils.indexOfIgnoreCase(null, null);
        assertEquals(0, result);
    }

    /**
     * Test index of 4 *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testIndexOf4() throws Exception {
        int result = StringUtils.indexOf(null, null, 0, true);
        assertEquals(0, result);
    }

    /**
     * Test last index of ignore case *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testLastIndexOfIgnoreCase() throws Exception {
        int result = StringUtils.lastIndexOfIgnoreCase(null, null);
        assertEquals(0, result);
    }

    /**
     * Test ordinal index of *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testOrdinalIndexOf() throws Exception {
        int result = StringUtils.ordinalIndexOf("str", "searchStr", 0);
        assertEquals(0, result);
    }

    /**
     * Test equals *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testEquals() throws Exception {
        boolean result = StringUtils.equals(null, null);
        assertTrue(result);
    }

    /**
     * Test equals 2 *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testEquals2() throws Exception {
        boolean result = StringUtils.equals(null, null, true);
        assertTrue(result);
    }

    /**
     * Test equals ignore case *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testEqualsIgnoreCase() throws Exception {
        boolean result = StringUtils.equalsIgnoreCase(null, null);
        assertTrue(result);
    }

    /**
     * Test builder *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testBuilder() throws Exception {
        StringBuilder result = StringUtils.builder();
        assertNull(result);
    }

    /**
     * Test builder 2 *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testBuilder2() throws Exception {
        StringBuilder result = StringUtils.builder(0);
        assertNull(result);
    }

    /**
     * Test builder 3 *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testBuilder3() throws Exception {
        StringBuilder result = StringUtils.builder("a", "b", "c");
        assertEquals("abc", result.toString());
    }

    /**
     * Test append builder *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testAppendBuilder() throws Exception {
        StringBuilder result = StringUtils.appendBuilder(new StringBuilder("aa"), "bb", "cc");
        assertEquals("aabbcc", result.toString());
    }

    /**
     * Test get reader *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testGetReader() throws Exception {
        StringReader result = StringUtils.getReader("aabbcc");
        assertEquals(97, result.read());
    }

    /**
     * Test get writer *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testGetWriter() throws Exception {
        StringWriter result = StringUtils.getWriter();
        assertNull(result);
    }

    /**
     * Test count *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testCount() throws Exception {
        int result = StringUtils.count(null, null);
        assertEquals(0, result);
    }

    /**
     * Test count 2 *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testCount2() throws Exception {
        int result = StringUtils.count(null, 'a');
        assertEquals(0, result);
    }

    /**
     * Test underline to hump *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testUnderlineToHump() throws Exception {
        String result = StringUtils.underlineToHump("para");
        assertEquals("replaceMeWithExpectedResult", result);
    }

    /**
     * Test hump to underline *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testHumpToUnderline() throws Exception {
        String result = StringUtils.humpToUnderline("para");
        assertEquals("replaceMeWithExpectedResult", result);
    }

    /**
     * Test line to hump *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testLineToHump() throws Exception {
        String result = StringUtils.lineToHump("para");
        assertEquals("replaceMeWithExpectedResult", result);
    }

    /**
     * Test hump to line *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testHumpToLine() throws Exception {
        String result = StringUtils.humpToLine("para");
        assertEquals("replaceMeWithExpectedResult", result);
    }

    /**
     * Test pad after *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testPadAfter() throws Exception {
        String result = StringUtils.padAfter(null, 0, null);
        assertEquals("replaceMeWithExpectedResult", result);
    }

    /**
     * Test str *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testStr() throws Exception {
        String result = StringUtils.str(null);
        assertEquals("replaceMeWithExpectedResult", result);
    }

    /**
     * Test center *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testCenter() throws Exception {
        String result = StringUtils.center(null, 0, null);
        assertEquals("replaceMeWithExpectedResult", result);
    }

    /**
     * Test clean text *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testCleanText() throws Exception {
        String result = StringUtils.cleanText("txt");
        assertEquals("replaceMeWithExpectedResult", result);
    }

    /**
     * Test clean identifier *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testCleanIdentifier() throws Exception {
        String result = StringUtils.cleanIdentifier("param");
        assertEquals("replaceMeWithExpectedResult", result);
    }

    /**
     * Test replace blank *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void testReplaceBlank() throws Exception {
        String result = StringUtils.replaceBlank("str");
        assertEquals("replaceMeWithExpectedResult", result);
    }
}

// Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme
