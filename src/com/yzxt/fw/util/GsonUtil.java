package com.yzxt.fw.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public class GsonUtil
{
    private static JsonParser parser = new JsonParser();

    /**
     * 将字符串转为Json对象
     * @param str
     */
    public static JsonElement parse(String str)
    {
        if (str != null && str.length() > 0)
        {
            return parser.parse(str);
        }
        return null;
    }

    /**
      * 将JSON字符串元素转换为字符串
      * @param ele
      * @return
      */
    public static String toString(JsonElement ele)
    {
        return ele != null ? ele.getAsString() : "";
    }

    /**
     * 将JSON字符串元素转换为整型数
     * @param ele
     * @return
     */
    public static Integer toInteger(JsonElement ele)
    {
        String str = ele != null ? ele.getAsString() : null;
        return str != null && str.length() > 0 ? ele.getAsInt() : null;
    }

    /**
     * 将JSON字符串元素转换为整型数
     * @param ele
     * @return
     */
    public static int toInt(JsonElement ele, int defalutVal)
    {
        String str = ele != null ? ele.getAsString() : null;
        return str != null && str.length() > 0 ? ele.getAsInt() : defalutVal;
    }

    /**
     * 将JSON字符串元素转换为长整型数
     * @param ele
     * @return
     */
    public static Long toLong(JsonElement ele)
    {
        String str = ele != null ? ele.getAsString() : null;
        return str != null && str.length() > 0 ? ele.getAsLong() : null;
    }

    /**
     * 将JSON字符串元素转换为浮点型
     * @param ele
     * @return
     */
    public static Double toDouble(JsonElement ele)
    {
        String str = ele != null ? ele.getAsString() : null;
        return str != null && str.length() > 0 ? ele.getAsDouble() : null;
    }

    /**
     * 将字符数组转为JSON数组
     * @param type
     * @return
     */
    public static JsonArray strToJsonArr(String str, String split)
    {
        JsonArray jsonArr = new JsonArray();
        if (str != null)
        {
            String[] strs = str.split(split);
            for (String s : strs)
            {
                if (s != null && s.length() > 0)
                {
                    jsonArr.add(new JsonPrimitive(s));
                }
            }
        }
        return jsonArr;
    }

    /**
     * 将字符数组转为JSON数组
     * @param type
     * @return
     */
    public static String jsonArrToString(JsonArray jsonArr, String split)
    {
        StringBuilder sb = new StringBuilder();
        int len = jsonArr != null ? jsonArr.size() : 0;
        for (int i = 0; i < len; i++)
        {
            String str = jsonArr.get(i).toString();
            if (str != null && str.length() > 0)
            {
                sb.append(str).append(split);
            }
        }
        return sb.toString();
    }

}
