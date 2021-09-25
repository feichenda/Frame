package com.lenovo.frame.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * gson工具类
 * @author feizai
 * @date 12/21/2020 021 10:05:39 PM
 */

public class GsonUtil {

    public static String mapToJson(Map map) {
        if (map == null) {
            return "";
        }
        Gson gson2 = new GsonBuilder().enableComplexMapKeySerialization().create();
        return gson2.toJson(map);
    }

    public static String ListToJson(List datas) {

        if (datas == null) {
            return "";
        }
        Gson gson = new Gson();
        return gson.toJson(datas);
    }

    public static String ObjectToJson(Object data) {
        if (data == null) {
            return "";
        }
        Gson gson = new Gson();
        return gson.toJson(data);
    }

    public static <T> T fromJson(String jsonData, Class<T> type) {
        return new Gson().fromJson(jsonData, type);
    }

    public static  <T> List<T> parseStringToList(String json, Class clazz) {
        Type type = new ParameterizedTypeImpl(clazz);
        List<T> list = new Gson().fromJson(json, type);
        return list;
    }

    private static class ParameterizedTypeImpl implements ParameterizedType {
        Class clazz;

        public ParameterizedTypeImpl(Class clz) {
            clazz = clz;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{clazz};
        }

        @Override
        public Type getRawType() {
            return List.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }


}
