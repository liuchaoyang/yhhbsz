package com.yzxt.yh.eif.mp;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class MpConst
{
    public static DecimalFormat def = new DecimalFormat("#0.0#####");
    public static SimpleDateFormat daf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // 发送操作成功
    public static final int OPER_STATE_SUCCESS = 1;
    // 发送失败，服务未启动
    public static final int OPER_STATE_SERVER_NOT_RUNNING = 2;
    // 发送失败，发送错误
    public static final int OPER_STATE_SEND_ERROR = 3;
    // 发送操作失败
    public static final int OPER_STATE_UNKNOW_ERROR = 9;

}
