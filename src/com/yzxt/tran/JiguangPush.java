package com.yzxt.tran;

import java.util.logging.Logger;

import com.opensymphony.xwork2.util.logging.LoggerFactory;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.ClientConfig;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;

@SuppressWarnings({ "deprecation", "restriction" })
public class JiguangPush {
    private static final Logger log = (Logger) LoggerFactory.getLogger(JiguangPush.class);
    private static String masterSecret = "a057d6dddc5bd011757dafff";
    private static String appKey = "75cf0844e5ee2b0a456e6f1a";
    private static final String ALERT = "推送消息";    
    /**
     * 极光推送方法(采用java SDK)
     * @param alias
     * @param alert
     * @return PushResult
     */
    public static PushResult push(){
        ClientConfig clientConfig = ClientConfig.getInstance();
        JPushClient jpushClient = new JPushClient(masterSecret, appKey, 0, null,clientConfig);
        PushPayload payload = buildPushObject_android_tag_alertWithTitle();
        try {
            return jpushClient.sendPush(payload);
        } catch (APIConnectionException e) {
            ((com.opensymphony.xwork2.util.logging.Logger) log).error("Connection error. Should retry later. ", e);
            return null;
        } catch (APIRequestException e) {
            ((com.opensymphony.xwork2.util.logging.Logger) log).error("Error response from JPush server. Should review and fix it. ", e);
            log.info("HTTP Status: " + e.getStatus());
            log.info("Error Code: " + e.getErrorCode());
            log.info("Error Message: " + e.getErrorMessage());
            log.info("Msg ID: " + e.getMsgId());
            return null;
        }    
    }
    
    /**
     * 构建推送对象
     * @return
     */
    public static PushPayload buildPushObject_android_tag_alertWithTitle() {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.tag("userId"))
                .setNotification(Notification.android(ALERT,null, null))
                .build();
    }
}
    
