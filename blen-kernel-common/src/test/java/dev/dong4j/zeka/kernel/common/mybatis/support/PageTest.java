package dev.dong4j.zeka.kernel.common.mybatis.support;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import dev.dong4j.zeka.kernel.common.api.R;
import dev.dong4j.zeka.kernel.common.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.12.13 11:56
 * @since 1.7.0
 */
@Slf4j
class PageTest {

    @Test
    void test_page() {
        Page<?> page = new Page<>();
        page.setTotal(10)
            .setSize(10)
            .setCurrent(2)
            .addOrder(OrderItem.asc("id"))
            .addOrder(OrderItem.desc("createTime"));
        log.info("{}", JsonUtils.toJson(R.succeed(page), true));
    }

}
