package dev.dong4j.zeka.kernel.common.type;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: https://my.oschina.net/MIKEWOO/blog/2994643 </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@gmail.com"
 * @date 2021.01.5 22:47
 * @since 1.0.0
 */
@SuppressWarnings("all")
public final class ConsumerObjectTypeAdapter extends TypeAdapter<Object> {
    /** FACTORY */
    public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
        @SuppressWarnings("unchecked")
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (type.getRawType() == Object.class) {
                return (TypeAdapter<T>) new ConsumerObjectTypeAdapter(gson);
            }
            return null;
        }
    };

    /** Gson */
    private final Gson gson;

    /**
     * Object type adapter
     *
     * @param gson gson
     * @since 1.0.0
     */
    ConsumerObjectTypeAdapter(Gson gson) {
        this.gson = gson;
    }

    /**
     * Read
     *
     * @param in in
     * @return the object
     * @throws IOException io exception
     * @since 1.0.0
     */
    @Override
    public Object read(JsonReader in) throws IOException {
        JsonToken token = in.peek();
        switch (token) {
            case BEGIN_ARRAY:
                List<Object> list = new ArrayList<Object>();
                in.beginArray();
                while (in.hasNext()) {
                    list.add(this.read(in));
                }
                in.endArray();
                return list;

            case BEGIN_OBJECT:
                Map<String, Object> map = new LinkedTreeMap<String, Object>();
                in.beginObject();
                while (in.hasNext()) {
                    map.put(in.nextName(), this.read(in));
                }
                in.endObject();
                return map;

            case STRING:
                return in.nextString();

            case NUMBER:
                String numberStr = in.nextString();
                if (numberStr.contains(".") || numberStr.contains("e")
                    || numberStr.contains("E")) {
                    return Double.parseDouble(numberStr);
                }
                if (Long.parseLong(numberStr) <= Integer.MAX_VALUE) {
                    return Integer.parseInt(numberStr);
                }
                return Long.parseLong(numberStr);

            case BOOLEAN:
                return in.nextBoolean();

            case NULL:
                in.nextNull();
                return null;

            default:
                throw new IllegalStateException();
        }
    }

    /**
     * Write
     *
     * @param out   out
     * @param value value
     * @throws IOException io exception
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @Override
    public void write(JsonWriter out, Object value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        TypeAdapter<Object> typeAdapter = (TypeAdapter<Object>) this.gson.getAdapter(value.getClass());
        if (typeAdapter instanceof ConsumerObjectTypeAdapter) {
            out.beginObject();
            out.endObject();
            return;
        }

        typeAdapter.write(out, value);
    }
}
