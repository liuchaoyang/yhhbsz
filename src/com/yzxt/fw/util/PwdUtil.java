package com.yzxt.fw.util;

import org.apache.log4j.Logger;

public class PwdUtil
{
    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(PwdUtil.class);

    /**
     * 加密密码
     * @return
     */
    public static String encrypt(String pwd, String o)
    {
        if (pwd == null)
        {
            return "";
        }
        return pwd;
    }

}
