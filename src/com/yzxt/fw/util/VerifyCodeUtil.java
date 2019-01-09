package com.yzxt.fw.util;

import org.apache.commons.lang.math.RandomUtils;

public class VerifyCodeUtil
{

    /**
     * 产生手机验证码
     * @return
     */
    public static String genForPhone()
    {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < 6; i++)
        {
            str.append(RandomUtils.nextInt(10));
        }
        return str.toString();
    }

}
