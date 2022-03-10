package org.mmo.engine.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * JSON序列化和反序列化
 */
public class JsonUtil {

    /**
     * JSON序列化
     *
     * @note 通过方法序列化 get set方法属性会存储
     * @param object 传入对象
     * @return String
     */
    public static String toJSONString(Object object) {
        return JSON.toJSONString(object, new SerializerFeature[]{SerializerFeature.DisableCircularReferenceDetect});
    }

    /**
     * JSON序列化
     *
     * @note 通过属性存储字段
     * @param object 传入对象
     * @return String
     */
    public static String toJSONStringWithField(Object object) {
        return JSON.toJSONString(object, new SerializerFeature[]{SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.IgnoreNonFieldGetter});
    }

    /**
     * JSON序列化(默认写入类型名称)
     *
     * @param object 传入对象
     * @return String
     * @note 通过方法序列化 get 方法属性会存储
     */
    public static String toJSONStringWriteClassName(Object object) {
        return JSON.toJSONString(object, new SerializerFeature[]{SerializerFeature.WriteClassName, SerializerFeature.DisableCircularReferenceDetect});
    }

    /**
     * JSON序列化(默认写入类型名称)
     *
     * @note 通过属性存储字段
     * @param object 传入对象
     * @return String
     */
    public static String toJSONStringWriteClassNameWithField(Object object) {
        return JSON.toJSONString(object, new SerializerFeature[]{SerializerFeature.WriteClassName, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.IgnoreNonFieldGetter});
    }

    /**
     * JSON序列化时间(默认格式yyyy-MM-dd HH:mm:ss)
     *
     * @param dateTime 传入时间
     * @return String
     */
    public static String toJSONStringWithDateFormat(long dateTime) {
        Date localDate = new Date(dateTime);
        return toJSONStringWithDateFormat(localDate);
    }

    /**
     * JSON序列化时间(自定义格式)
     *
     * @param dateTime 传入时间
     * @param dateFormat
     * @return String
     */
    public static String toJSONStringWithDateFormat(long dateTime, String dateFormat) {
        Date localDate = new Date(dateTime);
        return toJSONStringWithDateFormat(localDate, dateFormat);
    }

    /**
     * JSON序列化时间(默认格式yyyy-MM-dd HH:mm:ss)
     *
     * @param date 传入时间结构
     * @return String
     */
    public static String toJSONStringWithDateFormat(Date date) {
        return JSON.toJSONStringWithDateFormat(date, "yyyy-MM-dd HH:mm:ss", new SerializerFeature[]{SerializerFeature.WriteDateUseDateFormat});
    }

    /**
     * JSON序列化时间(自定义格式)
     *
     * @param date 传入时间结构
     * @param dateFormat
     * @return String
     */
    public static String toJSONStringWithDateFormat(Date date, String dateFormat) {
        return JSON.toJSONStringWithDateFormat(date, dateFormat, new SerializerFeature[]{SerializerFeature.WriteDateUseDateFormat});
    }

    /**
     * JSON反序列化
     *
     * @param text 传入被反序列化的对象
     * @return Object
     */
    public static Object parse(String text) {
        return JSON.parse(text);
    }

    /**
     * JSON反序列化(强制转化为T类型)
     *
     * @param <T>
     * @param text 传入被反序列化的对象
     * @param type 要被反序列化成的类型
     * @return
     */
    public static <T extends Object> T parseObject(String text, TypeReference<T> type) {
        return (T) JSON.parseObject(text, type, new Feature[0]);
    }

    public static <T extends Object> Set<T> parseSet(String text, Class<T> clazz) {
        List<T> list = parseArray(text, clazz);
        if (list != null) {
            return new HashSet<>(list);
        }
        return null;
    }

    /**
     * JSON反序列化(反序列化为List)
     *
     * @param <T>
     * @param text 传入被反序列化的对象
     * @param clazz 要被反序列化成的类型
     * @return
     */
    public static <T extends Object> List<T> parseArray(String text, Class<T> clazz) {
        return (List<T>) JSON.parseArray(text, clazz);
    }

    /**
     * JSON反序列化(反序列化为List) Type[] types = new Type[] {String.class, Byte.class};
     *
     * @param text 传入被反序列化的对象
     * @param types 要被反序列化成的类型数组(可多个类型，按照顺序反序列化)
     * @return
     */
    public static List<Object> parseArray(String text, Type[] types) {
        return JSON.parseArray(text, types);
    }
    
    /**
     * 反序列化
     * @param text
     * @param clazz
     * @return
     */
    public static final <T> T parseObject(String text, Class<T> clazz) {
        return JSON.parseObject(text, clazz);
    }
    
}
