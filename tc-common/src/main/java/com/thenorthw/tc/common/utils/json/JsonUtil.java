package com.thenorthw.tc.common.utils.json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

/**
 * Created by theNorthW on 12/05/2017.
 * blog: thenorthw.com
 *
 * 该类主要是简化了对于json对象的获取
 * @autuor : theNorthW
 */
public class JsonUtil {
    /**
     * 设计思想:
     * 首先json一般有两种形式:
     * 1. jsonObject (key:value)
     * 2. jsonArray (jsonObject数组)
     *
     * 但是jsonObject中会有jsonArray,这时候估计就要进行递归比较了
     * */
    public static boolean compareJsonObject(JsonObject jsonObject1, JsonObject jsonObject2){
        for(Map.Entry<String,JsonElement> e1 : jsonObject1.entrySet()) {
            String key = e1.getKey();
            Object value = e1.getValue();
            Object object = jsonObject2.get(key);

            if(value instanceof JsonArray){
                //判断另一个是否也是JsonArray
                if(object instanceof JsonArray){
                    return compareJsonArray((JsonArray)value,(JsonArray)object);
                }else{
                    return false;
                }
            }else if(value instanceof JsonObject) {
                if(compareJsonObject((JsonObject)value,(JsonObject)object)){
                    return false;
                }
            }else{
                if(!value.equals(object)){
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean compareJsonArray(JsonArray jsonArray1,JsonArray jsonArray2){
        for(int i=0;i<jsonArray1.size();i++){
            JsonObject jsonObject1 = jsonArray1.get(i).getAsJsonObject();
            JsonObject jsonObject2 = jsonArray2.get(i).getAsJsonObject();

            if(compareJsonObject(jsonObject1,jsonObject2)){
                return false;
            }
        }
        return true;
    }


    public static String beanToJson(Object object){
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public static <T> T jsonToBean(String json,Class<T> tClass){
        Gson gson = new Gson();
        return gson.fromJson(json,tClass);

    }

}
