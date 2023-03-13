package icu.wakuwaku.shorturl.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Spike
 * @Description 调用Jackson objectMapper中的方法
 * @Date 2023/3/13
 */
public class JacksonUtils {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String writeValueAsString(Object value){
        try {
            return objectMapper.writeValueAsString(value);//object --> string
        }catch (JsonProcessingException e){
            e.printStackTrace();
            return "";
        }
    }

    public static <T> T readValue(String content,Class<T> valueType){
        try {
            return objectMapper.readValue(content,valueType); //string --> object
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T readValue(String content, TypeReference<T> valueTypeRef) {
        try {
            return objectMapper.readValue(content, valueTypeRef);//string --> object
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T readValue(InputStream src,Class<T> valueType){
        try {
            return objectMapper.readValue(src,valueType); //string --> object
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T convertValue(Object fromValue,Class<T> toValueType){
        return objectMapper.convertValue(fromValue,toValueType);
    }

}
