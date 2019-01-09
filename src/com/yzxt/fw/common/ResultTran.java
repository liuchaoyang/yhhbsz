package com.yzxt.fw.common;

public class ResultTran
{

    //1：表示操作成功；
    //0：表示操作失败；
    //-1：服务器异常；
    //-2：会话超时
    //其他标识其他类型的错误，待约定
    //	"resultCode":"1"
    //	"resultMsg":"上传成功"
    // "data":  //实际所需要全部放入到该字段下
    public final static String STATE_ERROR = "0";
    public final static String STATE_OK = "1";
    public final static String STATEUSERUPDATE = "10";
    public final static String STATEUSEREXIST = "11";
    public final static String STATEIDCARDEXIST = "12";
    public final static String STATEPHONEEXIST = "13";
    public final static String STATEUSERUPDATEINFO = "修改成功";
    public final static String STATEUSEREXISTINFO = "用户名称已存在";
    public final static String STATEIDCARDEXISTINFO = "身份证号码存在";
    public final static String STATEPHONEEXISTINFO = "手机号码存在";

    // 登录用户不存在
    public final static String STATE_LOGIN_USER_NOT_EXIST = "-2";

    // 登录用户密码错
    public final static String STATE_LOGIN_PWD_ERROR = "-1";

    // 服务器错误
    public final static String STATE_SERVER_ERROR = "-1";
    // 登录会话超时
    public final static String STATE_SESSION_TIMEOUT = "-2";

}
