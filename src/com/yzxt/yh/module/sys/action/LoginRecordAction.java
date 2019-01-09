package com.yzxt.yh.module.sys.action;

import com.yzxt.fw.common.BaseAction;
import com.yzxt.yh.module.sys.service.LoginRecordService;

public class LoginRecordAction extends BaseAction
{
    private static final long serialVersionUID = 1L;

    private LoginRecordService loginRecordService;

    public LoginRecordService getLoginRecordService()
    {
        return loginRecordService;
    }

    public void setLoginRecordService(LoginRecordService loginRecordService)
    {
        this.loginRecordService = loginRecordService;
    }

}
