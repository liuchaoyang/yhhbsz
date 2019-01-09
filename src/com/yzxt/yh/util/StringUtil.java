package com.yzxt.yh.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class StringUtil
{
    /**
     * 是否是空字体串
     * @param str
     * @return
     */
    public static boolean isEmpty(String str)
    {
        return str == null || str.length() == 0;
    }

    /**
     * 是否是非空字体串
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str)
    {
        return str != null && str.length() > 0;
    }

    /**
     * 截取前后空格
     * @param str
     * @return
     */
    public static String trim(String str)
    {
        return str != null ? str.trim() : "";
    }

    /**
     * 将空字符串转为长度为0的字符串
     * @param str
     * @return
     */
    public static String ensureStringNotNull(String str)
    {
        return str != null ? str : "";
    }

    /**
     * 分隔字符串,不带null或""
     * @param str
     * @return
     */
    public static String[] splitWithoutEmpty(String str, String regex)
    {
        List<String> list = splitIgnoreEmpty(str, regex);
        return list.toArray(new String[list.size()]);
    }

    /**
     * 分隔字符串,不带null或""
     * @param str
     * @return
     */
    public static List<String> splitIgnoreEmpty(String str, String regex)
    {
        List<String> list = new ArrayList<String>();
        if (str != null)
        {
            String[] strs = str.split(regex);
            for (String s : strs)
            {
                if (s != null)
                {
                    s = s.trim();
                    if (isNotEmpty(s))
                    {
                        list.add(s);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 去除包裹字符
     * @param str
     * @param warpCh
     * @return
     */
    public static String ensureNoWarp(String str, char warpCh)
    {
        if (str == null)
        {
            return str;
        }
        int len = str.length();
        if (len < 2 || str.charAt(0) != warpCh || str.charAt(len - 1) != warpCh)
        {
            return str;
        }
        return str.substring(1, len - 1);
    }
    /** 
     * 创建UUID 
     * @return 
     */  
    public static synchronized String makeUUID() {  
        Date date = new Date();  
        StringBuffer s = new StringBuffer(DateUtil.getTranTime(date));  
        return s.append((new Random().nextInt(900) + 100)).toString();  
    } 
    public static String GetMapToXML(Map<String,String> param){  
        StringBuffer sb = new StringBuffer();  
        sb.append("<xml>");  
        for (Map.Entry<String,String> entry : param.entrySet()) {   
               sb.append("<"+ entry.getKey() +">");  
               sb.append(entry.getValue());  
               sb.append("</"+ entry.getKey() +">");  
       }    
        sb.append("</xml>");  
        return sb.toString();  
    }  

}
