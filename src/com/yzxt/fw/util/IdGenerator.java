package com.yzxt.fw.util;

import java.util.UUID;

public class IdGenerator
{

    public static String get32UUID()
    {
        String uuid = UUID.randomUUID().toString();
        return uuid.replace("-", "");
    }

    public static String getUUID()
    {
        return UUID.randomUUID().toString();
    }

}
