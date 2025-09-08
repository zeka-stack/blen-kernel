/*
 * Copyright (c) 2011-2020, baomidou (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package dev.dong4j.zeka.kernel.devtools.core.keywords;

import dev.dong4j.zeka.kernel.devtools.core.config.IKeyWordsHandler;
import java.util.List;
import java.util.Locale;

/**
 * 基类关键字处理
 *
 * @author nieqiurong 2020/5/8.
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2024.04.02 23:58
 * @since 1.0.0
 */
public abstract class BaseKeyWordsHandler implements IKeyWordsHandler {

    /** Key words */
    public List<String> keyWords;

    /**
     * Base key words handler
     *
     * @param keyWords key words
     * @since 1.0.0
     */
    public BaseKeyWordsHandler(List<String> keyWords) {
        this.keyWords = keyWords;
    }

    /**
     * Gets key words *
     *
     * @return the key words
     * @since 1.0.0
     */
    @Override
    public List<String> getKeyWords() {
        return keyWords;
    }

    /**
     * Is key words
     *
     * @param columnName column name
     * @return the boolean
     * @since 1.0.0
     */
    public boolean isKeyWords(String columnName) {
        return getKeyWords().contains(columnName.toUpperCase(Locale.ENGLISH));
    }
}
