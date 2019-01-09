package com.yzxt.yh.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64Util
{

    private static String CODE = "UTF-8";

    /** 
     * BASE64字符串解密 
     *  
     * @param key 
     * @return 
     * @throws Exception 
     */
    public static String decodeString(String key)
    {
        String res = "";
        if (key == null || key.length() < 1)
        {
            return "";
        }
        try
        {
            res = new String(new BASE64Decoder().decodeBuffer(key), CODE);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return res;
    }

    /** 
     * BASE64字符串加密 
     *  
     * @param key 
     * @return 
     * @throws Exception 
     */
    public static String encodeString(String key)
    {
        if (key == null || key.length() < 1)
        {
            return "";
        }
        try
        {
            key = new String((new BASE64Encoder().encodeBuffer(key.getBytes()).getBytes()), CODE);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return key;
    }

    /** 
     * BASE64地址栏加密 
     *  
     * @param key 
     * @return 
     * @throws Exception 
     */
    public static String encode(String key)
    {
        return encodeURL(encodeString(key));
    }

    /** 
     * BASE64地址栏解密 
     *  
     * @param key 
     * @return 
     * @throws Exception 
     */
    public static String decode(String key)
    {
        return decodeString(decodeURL(key));
    }

    /** 
     * BASE64加密 
     *  
     * @param key 
     * @return 
     * @throws Exception 
     */
    public static String encodeURL(String key)
    {
        try
        {
            key = java.net.URLEncoder.encode(key, CODE);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return key;
    }

    /** 
     * BASE64解密 
     *  
     * @param key 
     * @return 
     * @throws Exception 
     */
    public static String decodeURL(String key)
    {
        try
        {
            key = java.net.URLDecoder.decode(key, CODE);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return key;
    }

}
