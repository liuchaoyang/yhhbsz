package com.yzxt.fw.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * 系统配置文件参获取类
 *
 * @author fan
 * @since 2014-08-20
 */
public class Config
{
    private Logger logger = Logger.getLogger(Config.class);

    private static Config instance;

    private static Properties p = new Properties();

    private Config()
    {
    }

    public static synchronized Config getInstance()
    {
        if (instance == null)
        {
            instance = new Config();
        }
        return instance;
    }

    /**
     * 重新加载配置文件
     * @throws IOException 
     */
    public void reload(String filePath) throws IOException
    {
        logger.debug("Read configurate start.");
        FileInputStream in = new FileInputStream(filePath);
        p.clear();
        p.load(in);
        in.close();
        logger.debug("Read configurate over.");
    }

    /**
     * 获取配置属性值
     * @param key
     * @return
     */
    public String getString(String key)
    {
        return p.getProperty(key);
    }

    /**
     * 获取配置属性值，如果未配置，则返回指定值
     * @param key
     * @return
     */
    public String getString(String key, String defaultVal)
    {
        String str = p.getProperty(key);
        return str != null && str.length() > 0 ? str : defaultVal;
    }

    /**
     * 获取配置属性值
     * @param key
     * @return
     */
    public Integer getInteger(String key)
    {
        String str = p.getProperty(key);
        return str != null && str.length() > 0 ? Integer.valueOf(str) : null;
    }

    /**
     * 获取配置属性值，如果未配置，则返回指定值
     * @param key
     * @return
     */
    public Integer getInteger(String key, Integer defaultVal)
    {
        Integer i = getInteger(key);
        return i != null ? i : defaultVal;
    }

    /**
     * 获取配置属性值
     * @param key
     * @return
     */
    public Boolean getBoolean(String key)
    {
        String str = p.getProperty(key);
        if ("true".equals(str))
        {
            return Boolean.TRUE;
        }
        else if ("false".equals(str))
        {
            return Boolean.FALSE;
        }
        else
        {
            return null;
        }
    }

    /**
     * 获取配置属性值，如果未配置，则返回指定值
     * @param key
     * @return
     */
    public Boolean getBoolean(String key, Boolean defaultVal)
    {
        Boolean b = getBoolean(key);
        return b != null ? b : defaultVal;
    }

}