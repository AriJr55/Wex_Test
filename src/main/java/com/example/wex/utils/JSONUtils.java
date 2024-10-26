package com.example.wex.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public final class JSONUtils {

    public static final TypeReference<Map<String, Object>> mapReference = new TypeReference<>() {};
    public static final TypeReference<Map<String, String>> mapStringReference = new TypeReference<>() {};
    private static final ObjectMapper mapper;
    private static final Logger logger = LoggerFactory.getLogger(JSONUtils.class);
    private final static DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    static {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.registerModule(new JavaTimeModule());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd[['T']HH:mm[:ss]]");
        mapper.setDateFormat(df);
    }

    public static String readString(Object map, String key) {
        return readString(map, key, (String) null);
    }

    public static String readString(Object map, String key, String defaultValue) {
        Object o = readProperty(map, key, null);
        return o == null ? defaultValue : o.toString();
    }

    public static String readString(Object map, String key, Supplier<String> defaultValue) {
        Object o = readProperty(map, key, null);
        return o == null ? defaultValue.get() : o.toString();
    }

    public static Long readLong(Object map, String key) {
        return readLong(map, key, null);
    }

    public static Long readLong(Object map, String key, Long defaultValue) {
        Object result = readProperty(map, key, null);
        if (result == null) {
            return defaultValue;
        }
        if (Number.class.isAssignableFrom(result.getClass())) {
            return ((Number) result).longValue();
        } else {
            return Long.parseLong(result.toString());
        }
    }

    public static Integer readInt(Object map, String key) {
        return readInt(map, key, null);
    }

    public static Integer readInt(Object map, String key, Integer defaultValue) {
        Object result = readProperty(map, key, null);
        if (result == null) {
            return defaultValue;
        }
        if (Number.class.isAssignableFrom(result.getClass())) {
            return ((Number) result).intValue();
        } else {
            return Integer.parseInt(result.toString());
        }
    }


    public static Object readProperty(Object map, String key, Object defaultValue) {
        Object result = readProperty(map, key);
        return result == null ? defaultValue : result;
    }

    public static Object readProperty(Object map, String key) {
        if (map == null) {
            return null;
        }
        String[] keySplit = key.split("\\.");
        Object lastOne = map;
        for (String keyValue : keySplit) {
            if (lastOne == null) {
                return null;
            }
            if (lastOne instanceof Map) {
                Map lastMap = (Map) lastOne;
                lastOne = lastMap.get(keyValue);
            } else if (lastOne instanceof List) {
                List lastList = (List) lastOne;
                int index = Integer.parseInt(keyValue);
                if (lastList.size() > index) {
                    lastOne = lastList.get(index);
                } else {
                    lastOne = null;
                }
            } else {
                try {
                    lastOne = PropertyUtils.getProperty(lastOne, keyValue);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return lastOne;
    }

    public static <T> T jsonToObject(final String json, final Class<T> clazz) {
        if (json.trim().length() < 1) {
            return null;
        }
        T t = null;
        try {
            t = mapper.readValue(json, clazz);
        } catch (final Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return t;
    }

    public static String objectToJson(final Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void objectToJson(final Object obj, final OutputStream outputStream) {
        try {
            mapper.writeValue(outputStream, obj);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T jsonToObject(final String json, final TypeReference<T> type) {
        T t = null;
        try {
            t = mapper.readValue(json, type);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        return t;
    }

    public static <T> T jsonToObject(final URL json, final Class<T> clazz) {
        T t = null;
        try {
            t = mapper.readValue(json, clazz);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        return t;
    }

    public static <T> T jsonToObject(final URL json, final TypeReference<T> type) {
        T t = null;
        try {
            t = mapper.readValue(json, type);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        return t;
    }

    public static <T> T jsonToObject(final File json, final Class<T> clazz) {
        T t = null;
        try {
            t = mapper.readValue(json, clazz);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        return t;
    }

    public static <T> T jsonToObject(final File json, final TypeReference<T> type) {
        T t = null;
        try {
            t = mapper.readValue(json, type);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        return t;
    }
}
