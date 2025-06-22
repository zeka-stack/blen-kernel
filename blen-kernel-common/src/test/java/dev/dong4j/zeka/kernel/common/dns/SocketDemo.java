package dev.dong4j.zeka.kernel.common.dns;

import dev.dong4j.zeka.kernel.common.util.IoUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.06.09 19:53
 * @since 1.5.0
 */
class SocketDemo {
    /** encoding */
    private static final Charset encoding = StandardCharsets.UTF_8;

    @SneakyThrows
    @Test
    void test_socket() {
        new Socket("www.qq.com", 80);
        Socket client = new Socket("www.baidu.com", 80);

        client.setSoTimeout(30 * 1000);

        IoUtils.write("Hello world!", client.getOutputStream(), encoding);

        String input = IoUtils.toString(client.getInputStream(), encoding);
        System.out.println(input);
        System.out.println("bye!");

        client.close();
    }
}
