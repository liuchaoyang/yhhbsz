package com.yzxt.yh.eif.surezen;

public class SurezenConst
{
    public static final String SESSION_KEY_IMEI = "imei";
    public static final String SESSION_KEY_USER = "user";
    public static final String SESSION_KEY_TOKEN = "token";

    // 腕表操作成功
    public static final int OPER_STATE_SUCCESS = 1;
    // 腕表用户认证失败
    public static final int OPER_STATE_AUTH_FAIL = 2;
    // 未知指令
    public static final int OPER_STATE_UNKNOW_COMMAND = 3;
    // 错误参数值
    public static final int OPER_STATE_INVALID_PARAM = 4;
    // 未知指令
    public static final int OPER_STATE_UNKNOW_ERROR = 9;

    // 腕表下发成功
    public static final int SEND_STATE_SUCCESS = 1;
    // 腕表下发失败，腕表不在线
    public static final int SEND_STATE_OFFLINE = 2;
    // 腕表下发失败，未知错误
    public static final int SEND_STATE_UNKNOW_ERROR = 9;

}
