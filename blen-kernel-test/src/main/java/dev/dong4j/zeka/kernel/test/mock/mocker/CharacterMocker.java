package dev.dong4j.zeka.kernel.test.mock.mocker;

import dev.dong4j.zeka.kernel.common.util.RandomUtils;
import dev.dong4j.zeka.kernel.test.mock.MockConfig;
import dev.dong4j.zeka.kernel.test.mock.Mocker;

/**
 * <p>Description: Character对象模拟器</p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 18:19
 * @since 1.0.0
 */
public class CharacterMocker implements Mocker<Character> {

    /**
     * Mocks character.
     *
     * @param mockConfig the mock config
     * @return the character
     * @since 1.0.0
     */
    @Override
    public Character mock(MockConfig mockConfig) {
        char[] charSeed = mockConfig.getCharSeed();
        return charSeed[RandomUtils.nextInt(0, charSeed.length)];
    }

}
