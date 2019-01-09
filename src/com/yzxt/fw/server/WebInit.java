package com.yzxt.fw.server;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.yzxt.yh.eif.mp.MpProxy;
import com.yzxt.yh.eif.push.PushSch;
import com.yzxt.yh.eif.surezen.SurezenServer;

public class WebInit implements ServletContextListener
{
    private static Logger logger = Logger.getLogger(WebInit.class);

    private SurezenServer surezenServer = null;
    private PushSch pushSch = null;
    private MpProxy mpProxy = null;

    @Override
    public void contextInitialized(ServletContextEvent event)
    {
        try
        {
            String filePath = event.getServletContext().getRealPath("WEB-INF/config.properties");
            Config.getInstance().reload(filePath);
        }
        catch (Exception e)
        {
            logger.error("Read server configurate error!", e);
        }
        // 消息推送服务
        try
        {
            pushSch = new PushSch();
            pushSch.begin();
        }
        catch (Exception e)
        {
            logger.error("Start push msg service error!", e);
        }
        // 河北循证腕表服务
        try
        {
            surezenServer = new SurezenServer();
            surezenServer.begin();
        }
        catch (Exception e)
        {
            logger.error("Start SUREZEN(he bei xun zheng) service error!", e);
        }
        // 地图服务
        try
        {
            mpProxy = new MpProxy();
            mpProxy.begin();
        }
        catch (Exception e)
        {
            logger.error("Start map service error!", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event)
    {
        try
        {
            if (surezenServer != null)
            {
                surezenServer.shutdown();
            }
        }
        catch (Exception e)
        {
            logger.error("Shut down SUREZEN(he bei xun zheng) service error!", e);
        }
        try
        {
            if (pushSch != null)
            {
                pushSch.shutdown();
            }
        }
        catch (Exception e)
        {
            logger.error("Shut down push msg service error!", e);
        }
        try
        {
            if (mpProxy != null)
            {
                mpProxy.shutdown();
            }
        }
        catch (Exception e)
        {
            logger.error("Shut down map service error!", e);
        }
    }

}
