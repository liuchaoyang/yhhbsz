package com.yzxt.tran;

import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yzxt.fw.common.ResultTran;
import com.yzxt.fw.util.GsonUtil;
import com.yzxt.yh.module.chk.bean.PressurePulse;
import com.yzxt.yh.module.sys.bean.Customer;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.service.CustomerService;
import com.yzxt.yh.module.sys.service.UserService;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.StringUtil;

public class WelcomeTranAction extends WelcomeBaseTranAction
{
    private static final long serialVersionUID = 1L;

    private Logger logger = Logger.getLogger(WelcomeTranAction.class);
    
    private CustomerService customerService;

    private UserService userService;
    public CustomerService getCustomerService() {
		return customerService;
	}
	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}
	public UserService getUserService() {
		return userService;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	/**
     * 客户端登录
     */
    public void login()
    {
        try
        {
            JsonObject obj = super.getParams();
            String account = GsonUtil.toString(obj.get("userAccount"));
            String password = GsonUtil.toString(obj.get("password"));
            if (StringUtil.isEmpty(account))
            {
                super.write(ResultTran.STATE_ERROR, "用户名为空。", null);
                return;
            }
            if (StringUtil.isEmpty(password))
            {
                super.write(ResultTran.STATE_ERROR, "密码为空。", null);
                return;
            }
            User user = null;
            if (account.indexOf('@') == -1)
            {
                if (!Character.isDigit(account.charAt(0)))
                // 帐号
                {
                    user = userService.getUserByAcount(account);
                }
                else if (account.length() == 11)
                // 手机号
                {
                    user = userService.getUserByPhone(account);
                }
                else if (account.length() == 15 || account.length() == 18)
                // 身份证
                {
                    user = userService.getUserByIdCard(account);
                }
                else
                // 帐号
                {
                    user = userService.getUserByAcount(account);
                }
            }
            else
            // 电子邮箱
            {
                user = userService.getUserByEmail(account);
            }
            if (user == null)
            {
                super.write(ResultTran.STATE_LOGIN_USER_NOT_EXIST, "用户不存在。", null);
                return;
            }
            if (user.getState() == null || user.getState() != 1)
            {
                super.write(ResultTran.STATE_ERROR, "用户处于冻结状态。", null);
                return;
            }
            if (userService.getPasswordValid(user.getId(), password))
            {
                Map<String, Object> data = super.handleLogin(user, obj);
                super.write(ResultTran.STATE_OK, "登录成功。", gson.toJson(data));
            }
            else
            {
                super.write(ResultTran.STATE_LOGIN_PWD_ERROR, "用户密码错误。", null);
                return;
            }
        }
        catch (Exception e)
        {
            super.write(ResultTran.STATE_ERROR, "客户端登录出错。", null);
            logger.error("客户端登录出错。", e);
            return;
        }
    }
    
}
