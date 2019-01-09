package com.yzxt.fw.util;

import java.util.List;
import java.util.Map;

import com.baidu.yun.push.model.PushBatchUniMsgRequest;
import com.baidu.yun.push.model.PushMsgToAllRequest;
import com.baidu.yun.push.model.PushMsgToSingleDeviceRequest;
import com.google.gson.JsonObject;
import com.yzxt.yh.constant.ConstDevice;
import com.yzxt.yh.constant.ConstPushMsg;
import com.yzxt.yh.eif.push.PushSch;
import com.yzxt.yh.util.StringUtil;

public class PushMsgUtil
{

    /**
     * 推送单个消息
     * @param attrs, 最后一位为消息类型，最后第二位至六位表示推送的设备类型，最后第二位为1表示会推送给安卓设备，最后第三位为1表示会推送给苹果设备
     * @return
     */
    public static boolean pushSingle(String channelId, int attrs, JsonObject msg, Map<String, Object> extMsgs)
    {
        PushMsgToSingleDeviceRequest pr = new PushMsgToSingleDeviceRequest();
        int deviceType = (attrs & ConstPushMsg.ATTR_DEVICE_TYPE_IOS) <= 0 ? ConstPushMsg.BAIDU_DEVICE_TYPE_ANDROID
                : ConstPushMsg.BAIDU_DEVICE_TYPE_IOS;
        int msgType = ConstPushMsg.ATTR_MSG_TYPE_NOTICE & attrs;
        msg = getBaiduMsg(msgType, deviceType, msg, extMsgs);
        pr.setChannelId(channelId);
        pr.setMessage(msg != null ? msg.toString() : "");
        pr.setMessageType(msgType);
        pr.setDeviceType(deviceType);
        return PushSch.add(pr);
    }

    /**
     * 推送广播消息
     * @param attrs, 最后一位为消息类型，最后第二位至六位表示推送的设备类型，最后第二位为1表示会推送给安卓设备，最后第三位为1表示会推送给苹果设备
     * @return
     */
    public static boolean pushAll(int attrs, JsonObject msg, Map<String, Object> extMsgs)
    {
        PushMsgToAllRequest pr = new PushMsgToAllRequest();
        int deviceType = (attrs & ConstPushMsg.ATTR_DEVICE_TYPE_IOS) <= 0 ? ConstPushMsg.BAIDU_DEVICE_TYPE_ANDROID
                : ConstPushMsg.BAIDU_DEVICE_TYPE_IOS;
        int msgType = ConstPushMsg.ATTR_MSG_TYPE_NOTICE & attrs;
        msg = getBaiduMsg(msgType, deviceType, msg, extMsgs);
        pr.setMessage(msg != null ? msg.toString() : "");
        pr.setMessageType(msgType);
        pr.setDeviceType(deviceType);
        return PushSch.add(pr);
    }

    /**
     * 推送批量消息
     * @param attrs, 最后一位为消息类型，最后第二位至六位表示推送的设备类型，最后第二位为1表示会推送给安卓设备，最后第三位为1表示会推送给苹果设备
     * @param extMsgs 对于通知等需要额外的标题、描述等。
     * @return
     */
    public static boolean pushBatch(List<String> channelIds, int attrs, JsonObject msg, Map<String, Object> extMsgs)
    {
        PushBatchUniMsgRequest pr = new PushBatchUniMsgRequest();
        int deviceType = (attrs & ConstPushMsg.ATTR_DEVICE_TYPE_IOS) <= 0 ? ConstPushMsg.BAIDU_DEVICE_TYPE_ANDROID
                : ConstPushMsg.BAIDU_DEVICE_TYPE_IOS;
        int msgType = ConstPushMsg.ATTR_MSG_TYPE_NOTICE & attrs;
        msg = getBaiduMsg(msgType, deviceType, msg, extMsgs);
        StringBuilder sb = new StringBuilder("[");
        int c = 0;
        for (String channelId : channelIds)
        {
            if (c > 0)
            {
                sb.append(",");
            }
            sb.append(channelId);
            c++;
        }
        sb.append("]");
        pr.setChannelIds(channelIds.toArray(new String[channelIds.size()]));
        pr.setMessage(msg != null ? msg.toString() : "");
        pr.setMessageType(msgType);
        pr.setDeviceType(deviceType);
        return PushSch.add(pr);
    }

    /**
     * 根据设备类型设置设备属性值
     * @param deviceType
     * @return
     */
    public static int getDeviceTypeAttr(int deviceType)
    {
        if (ConstDevice.DEVICE_TYPE_ANDROID == deviceType)
        {
            return ConstPushMsg.ATTR_DEVICE_TYPE_ANDROID;
        }
        else if (ConstDevice.DEVICE_TYPE_IOS == deviceType)
        {
            return ConstPushMsg.ATTR_DEVICE_TYPE_IOS;
        }
        return 0;
    }

    /**
     * 转为百度消息格式
     * 百度透传消息为文本，无格式要求，但平台对消息有格式要求
     * 安卓
     * {
     *  "title" : "hello",
     *  "description": "hello world" //必选
     *  "notification_builder_id": 0, //可选
     *  "notification_basic_style": 7, //可选
     *  "open_type":0, //可选
     *  "url": "http://developer.baidu.com", //可选
     *  "pkg_content":"", //可选
     *  "custom_content":{"key":"value"},
     *  }
     *  IOS
     *  {
     *  "aps": {
     *  "alert":"Message From Baidu Cloud Push-Service",
     *  "sound":"",  //可选
     *  "badge":0,    //可选
     *  },
     *  "key1":"value1",
     *  "key2":"value2"
     *  }
     */
    private static JsonObject getBaiduMsg(int msgType, int deviceType, JsonObject msg, Map<String, Object> extMsgs)
    {
        JsonObject bdObj = null;
        // 只有安卓支持透传消息
        if (msgType != ConstPushMsg.ATTR_MSG_TYPE_NOTICE && ConstPushMsg.BAIDU_DEVICE_TYPE_ANDROID == deviceType)
        {
            bdObj = msg;
        }
        else
        // 通知
        {
            if (ConstPushMsg.BAIDU_DEVICE_TYPE_ANDROID == deviceType)
            {
                bdObj = new JsonObject();
                String title = extMsgs != null ? (String) extMsgs.get(ConstPushMsg.MSG_CUSTOM_CONTENT_ATTR_NAME) : null;
                String description = extMsgs != null ? (String) extMsgs.get(ConstPushMsg.MSG_DESCRIPTION_ATTR_NAME)
                        : null;
                bdObj.addProperty("title", StringUtil.isNotEmpty(title) ? title : "健康预警通知");
                bdObj.addProperty("description", StringUtil.isNotEmpty(description) ? description : "健康预警通知");
                bdObj.add("custom_content", msg);
            }
            else if (ConstPushMsg.BAIDU_DEVICE_TYPE_IOS == deviceType)
            {
                bdObj = msg;
                if (bdObj == null)
                {
                    bdObj = new JsonObject();
                }
                JsonObject aps = new JsonObject();
                String title = extMsgs != null ? (String) extMsgs.get(ConstPushMsg.MSG_CUSTOM_CONTENT_ATTR_NAME) : null;
                aps.addProperty("alert", StringUtil.isNotEmpty(title) ? title : "健康预警通知");
                bdObj.add("aps", aps);
            }
            else
            {
                bdObj = msg;
            }
        }
        return bdObj;
    }

}
