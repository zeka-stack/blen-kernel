package dev.dong4j.zeka.kernel.common.dns.agent;

import dev.dong4j.zeka.kernel.common.dns.DnsCacheManipulator;
import dev.dong4j.zeka.kernel.common.util.FileUtils;
import java.io.File;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.06.09 19:53
 * @since 1.0.0
 */
class DcmAgentTest {
    /** Output file */
    private File outputFile;
    /** Output file path */
    private String outputFilePath;

    /**
     * Sets up *
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @BeforeEach
    void setUp() throws Exception {
        this.outputFile = new File("target/output.log");
        FileUtils.deleteQuietly(this.outputFile);
        FileUtils.touch(this.outputFile);
        assertEquals(0, this.outputFile.length());
        System.out.println("Prepared output file: " + this.outputFile.getAbsolutePath());

        this.outputFilePath = this.outputFile.getAbsolutePath();

        DnsCacheManipulator.clearDnsCache();
    }

    /**
     * Tear down
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @AfterEach
    void tearDown() throws Exception {
        System.out.println("============================================");
        System.out.println("Agent Output File Content");
        System.out.println("============================================");
        String text = FileUtils.readToString(this.outputFile);
        System.out.println(text);
    }

    /**
     * Test agentmain empty
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void test_agentmain_empty() throws Exception {
        DnsAgent.agentmain("   ");
    }

    /**
     * Test agentmain file
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void test_agentmain_file() throws Exception {
        DnsAgent.agentmain("file " + this.outputFilePath);

        List<String> content = FileUtils.readLines(this.outputFile);
        assertThat(content.get(0), containsString("No action in agent argument, do nothing!"));
    }

    /**
     * Test agentmain set
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void test_agentmain_set() throws Exception {
        DnsAgent.agentmain("set baidu.com 1.2.3.4");
        assertEquals("1.2.3.4", DnsCacheManipulator.getDnsCache("baidu.com").getIp());
    }

    /**
     * Test agentmain set to file
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void test_agentmain_set_toFile() throws Exception {
        DnsAgent.agentmain("set baidu.com 1.2.3.4 file " + this.outputFilePath);
        assertEquals("1.2.3.4", DnsCacheManipulator.getDnsCache("baidu.com").getIp());

        List<String> content = FileUtils.readLines(this.outputFile);
        assertEquals(DnsAgent.DCM_AGENT_SUCCESS_MARK_LINE, content.get(content.size() - 1));
    }

    /**
     * Test agentmain set multi ip
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void test_agentmain_set_MultiIp() throws Exception {
        DnsAgent.agentmain("set baidu.com 1.1.1.1 2.2.2.2");
        assertArrayEquals(new String[]{"1.1.1.1", "2.2.2.2"}, DnsCacheManipulator.getDnsCache("baidu.com").getIps());
    }

    /**
     * Test agentmain get
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void test_agentmain_get() throws Exception {
        DnsCacheManipulator.setDnsCache("baidu.com", "3.3.3.3");
        DnsAgent.agentmain("get baidu.com");
    }

    /**
     * Test agentmain rm
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void test_agentmain_rm() throws Exception {
        DnsCacheManipulator.setDnsCache("baidu.com", "3.3.3.3");
        DnsAgent.agentmain("rm baidu.com");

        assertNull(DnsCacheManipulator.getDnsCache("baidu.com"));
    }

    /**
     * Test agentmain rm with file
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void test_agentmain_rm_withFile() throws Exception {
        DnsCacheManipulator.setDnsCache("baidu.com", "3.3.3.3");
        assertNotNull(DnsCacheManipulator.getDnsCache("baidu.com"));
        DnsAgent.agentmain("rm  baidu.com file " + this.outputFilePath);

        assertNull(DnsCacheManipulator.getDnsCache("baidu.com"));
    }

    /**
     * Test agentmain list
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void test_agentmain_list() throws Exception {
        DnsAgent.agentmain("   list  ");
    }

    /**
     * Test agentmain clear
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void test_agentmain_clear() throws Exception {
        DnsCacheManipulator.setDnsCache("baidu.com", "3.3.3.3");
        DnsAgent.agentmain("   clear  ");
        assertEquals(0, DnsCacheManipulator.listDnsCache().size());
    }

    /**
     * Test agentmain set policy
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void test_agentmain_setPolicy() throws Exception {
        DnsAgent.agentmain("   setPolicy    345  ");
        assertEquals(345, DnsCacheManipulator.getDnsCachePolicy());
    }

    /**
     * Test agentmain get policy
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void test_agentmain_getPolicy() throws Exception {
        DnsCacheManipulator.setDnsCachePolicy(456);
        DnsAgent.agentmain("   getPolicy     ");
        assertEquals(456, DnsCacheManipulator.getDnsCachePolicy());
    }

    /**
     * Test agentmain set negative policy
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void test_agentmain_setNegativePolicy() throws Exception {
        DnsAgent.agentmain("   setNegativePolicy 42 ");
        assertEquals(42, DnsCacheManipulator.getDnsNegativeCachePolicy());
    }

    /**
     * Test agentmain get negative policy
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void test_agentmain_getNegativePolicy() throws Exception {
        DnsCacheManipulator.setDnsNegativeCachePolicy(45);
        DnsAgent.agentmain("   getNegativePolicy");
        assertEquals(45, DnsCacheManipulator.getDnsNegativeCachePolicy());
    }

    /**
     * Test agentmain skip no action arguments
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void test_agentmain_skipNoActionArguments() throws Exception {
        DnsAgent.agentmain("  arg1  arg2   ");
    }

    /**
     * Test agentmain action need more argument
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void test_agentmain_actionNeedMoreArgument() throws Exception {
        DnsCacheManipulator.setDnsNegativeCachePolicy(1110);

        DnsAgent.agentmain("  setNegativePolicy     file " + this.outputFilePath);

        assertEquals(1110, DnsCacheManipulator.getDnsNegativeCachePolicy());

        List<String> content = FileUtils.readLines(this.outputFile);
        assertThat(content.get(0), containsString("Error to do action setNegativePolicy"));
        assertThat(content.get(0), containsString("action setNegativePolicy need more argument!"));
    }

    /**
     * Test agentmain action too more argument
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void test_agentmain_actionTooMoreArgument() throws Exception {
        DnsCacheManipulator.setDnsNegativeCachePolicy(1111);

        DnsAgent.agentmain("  setNegativePolicy 737 HaHa  file " + this.outputFilePath);

        assertEquals(1111, DnsCacheManipulator.getDnsNegativeCachePolicy());

        List<String> content = FileUtils.readLines(this.outputFile);
        assertThat(content.get(0), containsString("Error to do action setNegativePolicy 737 HaHa"));
        assertThat(content.get(0), containsString("Too more arguments for Action setNegativePolicy! arguments: [737, HaHa]"));
    }

    /**
     * Test agentmain unknown action
     *
     * @throws Exception exception
     * @since 1.0.0
     */
    @Test
    void test_agentmain_unknownAction() throws Exception {
        DnsAgent.agentmain("  unknownAction  arg1  arg2   file " + this.outputFilePath);

        List<String> content = FileUtils.readLines(this.outputFile);
        assertThat(content.get(0), containsString("No action in agent argument, do nothing!"));
    }
}
