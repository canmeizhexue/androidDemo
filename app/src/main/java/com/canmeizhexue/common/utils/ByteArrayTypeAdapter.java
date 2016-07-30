package com.canmeizhexue.common.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.io.IOException;
import java.lang.reflect.Type;

/**
* <p>
* 基于base64的GSON的数据压缩
* <br/>在客户端和服务端交互使用JSON的时候，
* 有byte[]类型的数据时候，JSON字符串会成倍增长，
* 每个字节都会转变成0x..的字符串 ，增加了数据交互的文件传输量。
* </p>
*
* @author canmeizhexue
* @version 1.0 (2015/11/18)
*/
public class ByteArrayTypeAdapter implements JsonSerializer<byte[]>, JsonDeserializer<byte[]> {
   /**
    * 转换器
    *
    * @param src       字节数组
    * @param typeOfSrc 类型
    * @param context   转换器上下文
    * @return
    */
   public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
       String base64 = Base64.encodeBytes(src);
       return new JsonPrimitive(base64);
   }

   /**
    * 转换器
    *
    * @param json    json节点
    * @param typeOfT 类型
    * @param context 转换器上下文
    * @return
    * @throws JsonParseException 转换异常
    */
   public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
       if (!(json instanceof JsonPrimitive)) {
           throw new JsonParseException("The date should be a string value");
       }
       try {
           byte[] base64 = Base64.decode(json.getAsString());
           return base64;
       } catch (IOException ex) {
       }
       return null;
   }
}
