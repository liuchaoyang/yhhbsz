package com.yzxt.yh.constant;

/**
 * 推送消息常量类
 * 
 */
public class ConstPushMsg
{

    // 消息类型：透传消息
    public static final int ATTR_MSG_TYPE_COMMON = 0x0;

    // 消息类型：通知
    public static final int ATTR_MSG_TYPE_NOTICE = 0x1;

    // 目标设备类型安卓
    public static final int ATTR_DEVICE_TYPE_ANDROID = 0x2;

    // 目标设备类型IOS
    public static final int ATTR_DEVICE_TYPE_IOS = 0x4;

    // 百度设备类型安卓
    public static final int BAIDU_DEVICE_TYPE_ANDROID = 3;

    // 百度设备类型苹果
    public static final int BAIDU_DEVICE_TYPE_IOS = 4;

    // 通知自定义内容属性名
    public static final String MSG_CUSTOM_CONTENT_ATTR_NAME = "custom_content";

    // 通知主题信息
    public static final String MSG_TITLE_ATTR_NAME = "title";

    // 通知描述信息
    public static final String MSG_DESCRIPTION_ATTR_NAME = "description";

    // 推送消息主题属性名
    public static final String MSG_TOPIC_ATTR_NAME = "type";

    // 推送消息内容属性名(这个不是百度那边的属性名称)
    public static final String MSG_CONTENT_ATTR_NAME = "content";

    // 推送消息主题，血压预警
    public static final String MSG_TOPIC_XYYJ = "xyyj";

    // 推送消息主题，血糖预警
    public static final String MSG_TOPIC_XTYJ = "xtyj";

    // 推送消息主题，体脂预警
    public static final String MSG_TOPIC_TZYJ = "tzyj";

    // 推送消息主题， 电子围栏超出
    public static final String MSG_TOPIC_DZWL = "dzwl";

}
