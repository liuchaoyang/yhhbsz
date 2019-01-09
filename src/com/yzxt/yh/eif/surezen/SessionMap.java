package com.yzxt.yh.eif.surezen;

import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.session.IoSession;

public class SessionMap
{
    private static Object lock = new Object();
    private static SessionMap sessionMap = null;
    private Map<String, IoSession> map = new HashMap<String, IoSession>();

    private SessionMap()
    {
    }

    /**
     * 获取唯一实例
     */
    public static SessionMap getInstance()
    {
        if (sessionMap == null)
        {
            synchronized (lock)
            {
                if (sessionMap == null)
                {
                    sessionMap = new SessionMap();
                }
            }
        }
        return sessionMap;
    }

    /**
     * 保存session会话
     */
    public void put(String key, IoSession session)
    {
        this.map.put(key, session);
    }

    /**
     * 去除session会话
     */
    public void remove(String key)
    {
        this.map.remove(key);
    }

    /**
     * 根据key查找缓存的session
     */
    public IoSession get(String key)
    {
        return this.map.get(key);
    }

}
