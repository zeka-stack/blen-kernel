package dev.dong4j.zeka.kernel.common.reflection.property;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.3.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.04.12 11:50
 * @since 1.0.0
 */
public class PropertyTokenizer implements Iterator<PropertyTokenizer> {
    /** Indexed name */
    @Getter
    private final String indexedName;
    /** Children */
    @Getter
    private final String children;
    /** Name */
    @Getter
    private String name;
    /** Index */
    @Getter
    private String index;

    /**
     * Property tokenizer
     *
     * @param fullname fullname
     * @since 1.0.0
     */
    public PropertyTokenizer(@NotNull String fullname) {
        int delim = fullname.indexOf('.');
        if (delim > -1) {
            this.name = fullname.substring(0, delim);
            this.children = fullname.substring(delim + 1);
        } else {
            this.name = fullname;
            this.children = null;
        }
        this.indexedName = this.name;
        delim = this.name.indexOf('[');
        if (delim > -1) {
            this.index = this.name.substring(delim + 1, this.name.length() - 1);
            this.name = this.name.substring(0, delim);
        }
    }

    /**
     * Has next boolean
     *
     * @return the boolean
     * @since 1.0.0
     */
    @Override
    public boolean hasNext() {
        return this.children != null;
    }

    /**
     * Next property tokenizer
     *
     * @return the property tokenizer
     * @since 1.0.0
     */
    @Override
    public PropertyTokenizer next() {
        return new PropertyTokenizer(this.children);
    }

    /**
     * Remove
     *
     * @since 1.0.0
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove is not supported, as it has no meaning in the context of properties.");
    }
}
