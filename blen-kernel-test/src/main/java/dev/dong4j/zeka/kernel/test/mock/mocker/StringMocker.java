package dev.dong4j.zeka.kernel.test.mock.mocker;

import dev.dong4j.zeka.kernel.common.util.RandomUtils;
import dev.dong4j.zeka.kernel.test.mock.MockConfig;
import dev.dong4j.zeka.kernel.test.mock.Mocker;
import dev.dong4j.zeka.kernel.test.mock.util.MockFileUtil;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Description: 模拟String对象</p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:19
 * @since 1.0.0
 */
public class StringMocker implements Mocker<String> {
    /** 汉语 种子内容 */
    private static final String STR = MockFileUtil.readerText(MockFileUtil.MOCK_FILE_NAME);

    /**
     * Mock string
     *
     * @param mockConfig mock config
     * @return the string
     * @since 1.0.0
     */
    @Override
    public String mock(@NotNull MockConfig mockConfig) {

        if (mockConfig.getStringEnum() == MockConfig.StringEnum.CHARACTER) {
            return mockerCharacter(mockConfig);
        }
        if (mockConfig.getStringEnum() == MockConfig.StringEnum.UUID) {
            return UUID.randomUUID().toString();
        }
        return mockChinese(mockConfig);
    }

    /**
     * 生成随机多个字符
     *
     * @param mockConfig the mock config
     * @return string string
     * @since 1.0.0
     */
    @NotNull
    private String mockerCharacter(@NotNull MockConfig mockConfig) {
        int size = RandomUtils.nextSize(mockConfig.getSizeRange()[0], mockConfig.getSizeRange()[1]);
        String[] stringSeed = mockConfig.getStringSeed();
        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            sb.append(stringSeed[RandomUtils.nextInt(0, stringSeed.length)]);
        }
        return sb.toString();
    }

    /**
     * 随机截取一段中文返回
     *
     * @param mockConfig the mock config
     * @return string string
     * @since 1.0.0
     */
    @NotNull
    private String mockChinese(@NotNull MockConfig mockConfig) {
        int size = RandomUtils.nextSize(mockConfig.getSizeRange()[0], mockConfig.getSizeRange()[1]);
        int index = RandomUtils.nextInt(0, STR.length() - 1 - size);
        return STR.substring(index, index + size);
    }

}
