package com.yzxt.yh.eif.push;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;
import com.baidu.yun.push.auth.PushKeyPair;
import com.baidu.yun.push.client.BaiduPushClient;
import com.baidu.yun.push.constants.BaiduPushConstants;
import com.baidu.yun.push.model.PushBatchUniMsgRequest;
import com.baidu.yun.push.model.PushMsgToAllRequest;
import com.baidu.yun.push.model.PushMsgToSingleDeviceRequest;
import com.baidu.yun.push.model.PushRequest;
import com.yzxt.fw.server.Config;
import com.yzxt.yh.constant.ConstPushMsg;
import com.yzxt.yh.util.StringUtil;

public class PushSch extends Thread
{
    private Logger logger = Logger.getLogger(PushSch.class);
    private BaiduPushClient androidPush;
    private boolean needClose = false;
    private static LinkedList<PushRequest> q;

    public static boolean add(PushRequest pr)
    {
        if (q == null)
        {
            return false;
        }
        q.add(pr);
        return true;
    }

    private static PushRequest get()
    {
        return q.poll();
    }

    /**
     * 消息调度
     * @throws Exception
     */
    public void begin() throws Exception
    {
        Config config = Config.getInstance();
        String androidApiKey = config.getString("push.bd.android.apikey");
        String androidSecretKey = config.getString("push.bd.android.secretkey");
        if (StringUtil.isEmpty(androidApiKey) || StringUtil.isEmpty(androidSecretKey))
        {
            logger.error("未配置百度推送apiKey或secretKey，忽略消息推送服务。");
            return;
        }
        // 创建BaiduPushClient，访问SDK接口 
        androidPush = new BaiduPushClient(new PushKeyPair(androidApiKey, androidSecretKey),
                BaiduPushConstants.CHANNEL_REST_URL);
        // 注册YunLogHandler，获取本次请求的交互信息
        androidPush.setChannelLogHandler(new YunLogHandler()
        {
            @Override
            public void onHandle(YunLogEvent event)
            {
                // logger.debug(event.getMessage());
            }
        });
        q = new LinkedList<PushRequest>();
        // 启动处理线程
        needClose = false;
        super.setDaemon(true);
        super.start();
    }

    /**
     * 处理消息发送
     */
    public void run()
    {
        while (!needClose)
        {
            PushRequest pr = get();
            try
            {
                if (pr == null)
                {
                    // 暂停运行3秒钟
                    Thread.sleep(3000);
                    continue;
                }
                if (pr.getDevice() != null && pr.getDevice() == ConstPushMsg.BAIDU_DEVICE_TYPE_ANDROID)
                {
                    if (pr instanceof PushMsgToSingleDeviceRequest)
                    {
                        androidPush.pushMsgToSingleDevice((PushMsgToSingleDeviceRequest) pr);
                    }
                    else if (pr instanceof PushBatchUniMsgRequest)
                    {
                        androidPush.pushBatchUniMsg((PushBatchUniMsgRequest) pr);
                    }
                    else if (pr instanceof PushMsgToAllRequest)
                    {
                        androidPush.pushMsgToAll((PushMsgToAllRequest) pr);
                    }
                    else
                    {
                        logger.debug("未知的推送消息类型：" + pr.toString());
                    }
                }
            }
            catch (Exception e)
            {
                logger.error("百度推送处理请求出错。", e);
            }
        }
        q = null;
    }

    /**
     * 不需要处理资源
     */
    public void shutdown()
    {
        needClose = true;
    }

}
