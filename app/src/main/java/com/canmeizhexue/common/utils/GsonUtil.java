package com.canmeizhexue.common.utils;

import com.canmeizhexue.common.annotation.NotSerialize;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>Gson类库的封装工具类，专门负责解析json数据</p>
 *
 * @author canmeizhexue
 * @version 1.0 (2015/11/17)
 */
public class GsonUtil {
    private static Gson gson = null;

    static {
        if (gson == null) {
            gson = newInstance();
        }
    }

    /**
     * 自定义排除策略
     */
    private static class AppExclusionStrategy implements ExclusionStrategy {
        private final Class<?> typeToSkip;

        public AppExclusionStrategy() {
            this.typeToSkip = null;
        }

        public AppExclusionStrategy(Class<?> typeToSkip) {
            this.typeToSkip = typeToSkip;
        }

        public boolean shouldSkipClass(Class<?> clazz) {
            return (clazz == typeToSkip);
        }

        public boolean shouldSkipField(FieldAttributes f) {
            return f.getAnnotation(NotSerialize.class) != null;
        }
    }

    /**
     * 获取GSON转换模式，默认日期格式为“yyyy-MM-dd HH:mm:ss”
     *
     * @return
     */
    public static Gson newInstance() {
        GsonBuilder builder = new GsonBuilder();
        // 不转换没有 @Expose 注解的字段
        //builder.excludeFieldsWithoutExposeAnnotation();
        builder.registerTypeAdapter(Timestamp.class, new TimestampTypeAdapter()).setDateFormat("yyyy-MM-dd HH:mm:ss");
        builder.registerTypeHierarchyAdapter(byte[].class, new ByteArrayTypeAdapter());
        builder.disableHtmlEscaping();//不转义
        //指定排除策略
        builder.setExclusionStrategies(new AppExclusionStrategy());
        return builder.create();
    }

    /**
     * 获取GSON转换模式，设置时间格式为dataFat
     *
     * @param dataFat 时间格式
     * @return
     */
    public static Gson getGson(String dataFat) {
        GsonBuilder builder = new GsonBuilder();
        // 不转换没有 @Expose 注解的字段
        //builder.excludeFieldsWithoutExposeAnnotation();
        builder.registerTypeAdapter(Timestamp.class, new TimestampTypeAdapter()).setDateFormat(dataFat);
        builder.registerTypeHierarchyAdapter(byte[].class, new ByteArrayTypeAdapter());
        return builder.create();
    }

    /**
     * 将对象转换成json格式
     *
     * @param ts 待转换对象
     * @return
     */
    public static String objectToJson(Object ts) {
        String jsonStr = null;
        if (gson != null) {
            jsonStr = gson.toJson(ts);
        }
        return jsonStr;
    }

    /**
     * 将对象转换成json格式(并自定义日期格式)
     *
     * @param ts         待转换对象
     * @param dateformat 转换时间格式
     * @return
     */
    public static String objectToJsonDateSerializer(Object ts,
                                                    final String dateformat) {
        String jsonStr = null;
        gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(Date.class,
                        new JsonSerializer<Date>() {
                            public JsonElement serialize(Date src,
                                                         Type typeOfSrc,
                                                         JsonSerializationContext context) {
                                SimpleDateFormat format = new SimpleDateFormat(
                                        dateformat);
                                return new JsonPrimitive(format.format(src));
                            }
                        }).setDateFormat(dateformat).create();
        if (gson != null) {
            jsonStr = gson.toJson(ts);
        }
        return jsonStr;
    }

    /**
     * 将json格式转换成map对象
     *
     * @param jsonStr json格式字符串
     * @return
     */
    public static Map<?, ?> jsonToMap(String jsonStr) {
        Map<?, ?> objMap = null;
        if (gson != null) {
            Type type = new TypeToken<Map<?, ?>>() {
            }.getType();
            objMap = gson.fromJson(jsonStr, type);
        }
        return objMap;
    }

    /**
     * 将json转换成bean对象
     *
     * @param jsonStr json格式字符串
     * @param cl      转换的目标实体类
     * @param <T>     实体类型
     * @return
     */
    public static <T> T jsonToBean(String jsonStr, Class<T> cl) {
        T obj = null;
        if (gson != null) {
            obj = gson.fromJson(jsonStr, cl);
        }
        return obj;
    }

//json 与list 互转
//====================================

    /**
     * 将json格式转换成list对象
     *
     * @param jsonStr json格式字符串
     * @return
     */
    public static List<?> jsonToList(String jsonStr) {
        List<?> objList = null;
        if (gson != null) {
            Type type = new TypeToken<List<?>>() {
            }.getType();
            objList = gson.fromJson(jsonStr, type);
        }
        return objList;
    }

    /**
     * 将json格式转换成list对象，并准确指定类型
     *
     * @param jsonStr json字符串
     * @param type    转换类型
     * @param <T>     转换实体类型
     * @return
     */
    public static <T> List<T> jsonToList(String jsonStr, Type type) {
        List<T> objList = null;
        if (gson != null) {
            objList = gson.fromJson(jsonStr, type);
        }
        return objList;
    }

    /**
     * 将json格式转换成list对象，并准确指定类型
     *
     * @param jsonObj json节点
     * @param type    转换类型
     * @param <T>     转换实体类型
     * @return
     */
    public static <T> List<T> jsonToList(JsonElement jsonObj, Type type) {
        List<T> list = new ArrayList<>();
        try {
            if (gson != null) {
                list = gson.fromJson(jsonObj, type);
            }
        } catch (Exception e) {
        }
        return list;
    }

    /**
     * 将json格式字符串转换为lsit
     *
     * @param jsonString json字符串
     * @return
     */
    public static List<Map<String, Object>> listKeyMaps(String jsonString) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            if (gson != null) {
                list = gson.fromJson(jsonString,
                        new TypeToken<List<Map<String, Object>>>() {
                        }.getType());
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return list;
    }

    /**
     * 根据字段名获取json字符串中的某个key的内容
     *
     * @param jsonStr json字符串
     * @param key     key值
     * @return
     */
    public static Object getJsonValue(String jsonStr, String key) {
        Object rulsObj = null;
        Map<?, ?> rulsMap = jsonToMap(jsonStr);
        if (rulsMap != null && rulsMap.size() > 0) {
            rulsObj = rulsMap.get(key);
        }
        return rulsObj;
    }

    /**
     * 将json字符串转换为json对象
     *
     * @param jsonStr json字符串
     * @return
     */
    public static JsonArray jsonStr2JsonArray(String jsonStr) {
        JsonParser parser = new JsonParser();
        JsonElement el = parser.parse(jsonStr);
        JsonArray jsonArray = null;
        if (el.isJsonArray()) {//把JsonElement对象转换成JsonArray
            jsonArray = el.getAsJsonArray();
            return jsonArray;
        }
        return null;
    }

    /**
     * 将json字符串转换为json对象
     *
     * @param jsonStr json字符串
     * @return
     */
    public static JsonObject jsonStr2JsonObject(String jsonStr) {
        JsonParser parser = new JsonParser();
        if (null != jsonStr) {
            JsonElement el = parser.parse(jsonStr);
            JsonObject jsonObj = null;
            if (null != el && el.isJsonObject()) {//把JsonElement对象转换成JsonObject
                jsonObj = el.getAsJsonObject();
                return jsonObj;
            }
        }
        return null;
    }

    /**
     * <p>
     * <p>
     * 转换JsonElement对象为目标类型对象
     * <br/>注意，调用此方法前，需要先检查所需要转换的类型，本方法是否已经考虑到,
     * <br/>若还没考虑到，则需要自行拓展
     * <p>
     * </p>
     *
     * @param targetEntityType     目标类型
     * @param targetEntityTypeName 目标类型名称
     * @param obj                  待转换对象
     * @param <T>                  泛型类
     * @return
     */
    public static <T> T convertType(Class<T> targetEntityType, String targetEntityTypeName, JsonElement obj) {
        try {
            Object returnVal = null;
            if (null != obj && !"".equals(obj.toString())) {
                if (targetEntityTypeName.contains("Long") || targetEntityTypeName.contains("long")) {
                    returnVal = obj.getAsLong();
                } else if (targetEntityTypeName.contains("Boolean") || targetEntityTypeName.contains("boolean")) {
                    returnVal = obj.getAsBoolean();
                } else if (targetEntityTypeName.contains("Integer") || targetEntityTypeName.contains("int")) {
                    returnVal = obj.getAsInt();
                } else if (targetEntityTypeName.contains("String")) {
                    returnVal = obj.getAsString();
                } else if (targetEntityTypeName.contains("JsonArray")) {
                    returnVal = obj.getAsJsonArray();
                } else if (targetEntityTypeName.contains("BigInteger")) {
                    returnVal = obj.getAsBigInteger();
                } else if (targetEntityTypeName.contains("Byte") || targetEntityTypeName.contains("byte")) {
                    returnVal = obj.getAsByte();
                } else if (targetEntityTypeName.contains("JsonObject")) {
                    returnVal = obj.getAsJsonObject();
                } else if (targetEntityTypeName.contains("Double") || targetEntityTypeName.contains("double")) {
                    returnVal = obj.getAsDouble();
                } else if (targetEntityTypeName.contains("Float") || targetEntityTypeName.contains("float")) {
                    returnVal = obj.getAsFloat();
                }
                return (T) returnVal;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}



