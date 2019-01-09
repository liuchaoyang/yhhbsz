package com.yzxt.yh.util;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

public class DecimalUtil
{
    private static DecimalFormat intF = new DecimalFormat("#0");

    private static Pattern integerPattern = Pattern.compile("^[+-]?\\d+");

    private static DecimalFormat commonFloat = new DecimalFormat("#0.##");

    private static DecimalFormat gpsF = new DecimalFormat("##0.######");

    // 格式化小数
    public static String toString(Double d)
    {
        if (d != null)
        {
            return commonFloat.format(d);
        }
        return "";
    }

    // 格式化小数
    public static String toString(Integer i)
    {
        if (i != null)
        {
            return intF.format(i);
        }
        return "";
    }

    // 格式化为整数
    public static String toIntegerString(Double d)
    {
        if (d == null)
        {
            return "";
        }
        return intF.format(d);
    }

    // 格式化为整数
    public static String toIntegerString(Integer i)
    {
        if (i == null)
        {
            return "";
        }
        return intF.format(i);
    }

    /**
     * 判断是否是整型
     * @param str
     * @return
     */
    public static boolean isInteger(String str)
    {
        if (str == null || str.length() == 0)
        {
            return false;
        }
        return integerPattern.matcher(str).matches();
    }

    /**
     * 先用isInteger判断是否是整数字符串
     * @param str
     * @return
     */
    public static int toInteger(String str)
    {
        if (str.charAt(0) == '+')
        {
            str = str.substring(1);
        }
        return Integer.valueOf(str);
    }

    /**
     * 十进制转16进制
     * @param i
     * @param len
     * @return
     */
    public static String toHexString(int i, int len)
    {
        String hex = Integer.toHexString(i).toUpperCase();
        if (len <= 0)
        {
            return hex;
        }
        int actualLen = hex.length();
        if (actualLen == len)
        {
            return hex;
        }
        else if (actualLen < len)
        {
            StringBuilder hexBuff = new StringBuilder(hex);
            while (actualLen < len)
            {
                hexBuff.insert(0, '0');
                actualLen++;
            }
            return hexBuff.toString();
        }
        else
        {
            return hex.substring(0, len);
        }
    }

    // 格式化GPS小数
    public static String toGpsString(Double i)
    {
        if (i != null)
        {
            return gpsF.format(i);
        }
        return "";
    }

}
