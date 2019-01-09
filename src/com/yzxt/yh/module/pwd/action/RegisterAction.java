package com.yzxt.yh.module.pwd.action;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

import com.yzxt.fw.common.BaseAction;
import com.yzxt.fw.common.Result;
import com.yzxt.yh.module.pwd.service.RegisterService;
import com.yzxt.yh.module.sys.bean.Org;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.bean.UserPassword;
import com.yzxt.yh.module.sys.service.OrgService;
import com.yzxt.yh.module.sys.vo.RegisterVO;
import com.yzxt.yh.util.FileUtil;
import com.yzxt.yh.util.StringUtil;

public class RegisterAction extends BaseAction
{
    private Logger logger = Logger.getLogger(RegisterAction.class);
    private static final long serialVersionUID = 1L;
    private RegisterService registerService;
    private OrgService orgService;
    private RegisterVO registerVO;
    private String name;
    private String key;
    private User user;
    private String newPassword;
    private String newPassword2;

    public RegisterService getRegisterService()
    {
        return registerService;
    }

    public void setRegisterService(RegisterService registerService)
    {
        this.registerService = registerService;
    }

    public OrgService getOrgService()
    {
        return orgService;
    }

    public void setOrgService(OrgService orgService)
    {
        this.orgService = orgService;
    }

    public RegisterVO getRegisterVO()
    {
        return registerVO;
    }

    public void setRegisterVO(RegisterVO registerVO)
    {
        this.registerVO = registerVO;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public String getNewPassword()
    {
        return newPassword;
    }

    public void setNewPassword(String newPassword)
    {
        this.newPassword = newPassword;
    }

    public String getNewPassword2()
    {
        return newPassword2;
    }

    public void setNewPassword2(String newPassword2)
    {
        this.newPassword2 = newPassword2;
    }

    /**
     * 跳转到注册页面
     * @throws Exception 
     */
    public String to() throws Exception
    {
        String showOrgId = request.getParameter("orgId");
        Org org = orgService.getShowOrg(showOrgId);
        if (org != null && StringUtil.isNotEmpty(org.getLogoPath()))
        {
            org.setLogoPath(FileUtil.encodePath(org.getLogoPath()));
        }
        request.setAttribute("org", org);
        return "register";
    }

    /**
     * 跳转到重置密码页面
     * @throws Exception 
     */
    public String toResetPwd() throws Exception
    {
        String orgId = request.getParameter("orgId");
        Org org = orgService.getShowOrg(orgId);
        if (org != null && StringUtil.isNotEmpty(org.getLogoPath()))
        {
            org.setLogoPath(FileUtil.encodePath(org.getLogoPath()));
        }
        request.setAttribute("org", org);
        return "resetPwd";
    }

    /**
     * 用户注册
     */
    public void addCustomer()
    {
        Result r = null;

        try
        {
            String account = registerVO.getAccount();
            boolean orgExist = registerService.getAccountExist(account, null);
            if (orgExist)
            {
                r = new Result(Result.STATE_FAIL, "帐号重复。", null);
            }
            else
            {

                r = registerService.addCustomer(registerVO);
            }

        }
        catch (Exception e)
        {
            r = new Result(Result.STATE_FAIL, "用户注册错误。", null);
            logger.error("添加用户错误。", e);
        }
        super.write(r);
    }

    /**
     * 将重置密码链接发送到用户邮箱
     */
    public void updatePwdToEmail()
    {
        Result r = null;
        try
        {
            String name = registerVO.getName();
            String email = registerVO.getEmail();
            boolean userDetail = registerService.getUserDetail(name, email);
            if (userDetail)
            {
                r = registerService.updatePwdByMail(name, email);
            }
            else
            {
                r = new Result(Result.STATE_FAIL, "用户名或邮箱错误。", null);
            }

        }
        catch (Exception e)
        {
            r = new Result(Result.STATE_FAIL, "邮件发送错误。", null);
            logger.error("邮件发送错误。", e);
        }
        super.write(r);
    }

    /**
     * 通过邮箱链接回调进行密码重置
     * @return
     * @throws UnsupportedEncodingException
     */
    public String updatePassWord() throws UnsupportedEncodingException
    {
        String orgId = request.getParameter("orgId");
        try
        {
            String fw = null;
            Result r = registerService.updatePwd(name, key);
            int state = r.getState();
            if (state == 1)
            {
                user = registerService.getUserDetailByKey(name, key);
                request.setAttribute("user", user);
                orgId = user.getOrgId();
                fw = "reset";
            }
            else
            {
                request.setAttribute("msg", "重置密码信息已失效。");
                fw = "error";
            }
            Org org = orgService.getShowOrg(orgId);
            if (org != null && StringUtil.isNotEmpty(org.getLogoPath()))
            {
                org.setLogoPath(FileUtil.encodePath(org.getLogoPath()));
            }
            request.setAttribute("org", org);
            return fw;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return key;

    }

    /**
     * 重置密码
     */
    public void updateUserPwd()
    {
        Result r = null;
        try
        {
            UserPassword userPassword = new UserPassword();
            userPassword.setUserId(user.getId());
            userPassword.setPassword(newPassword2);
            int updatePwdNew = registerService.updatePwdNew(userPassword);
            if (updatePwdNew > 0)
            {
                r = new Result(Result.STATE_SUCESS, "密码重置成功。", null);
            }

        }
        catch (Exception e)
        {
            r = new Result(Result.STATE_FAIL, "邮件发送错误。", null);
            logger.error("邮件发送错误。", e);
        }
        super.write(r);
    }

}
