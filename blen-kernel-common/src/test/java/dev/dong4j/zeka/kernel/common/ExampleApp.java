package dev.dong4j.zeka.kernel.common;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.12.26 20:08
 * @since 1.0.0
 */
@Slf4j
public class ExampleApp {
    /**
     * Main
     *
     * @param args args
     * @since 1.0.0
     */
    public static void main(String[] args) {
        Pangu pangu = new Pangu();

        String text = pangu.spacingText("log.info(\"处理redis订阅消息,topic:[{}],body:[{}]\", new String(message.getChannel(), Charsets.UTF_8)," +
            " obj);");
        log.info(text);
    }
}
