package com.yzxt.yh.module.sys.action;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.yzxt.fw.common.BaseAction;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.server.ConstSv;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.sys.bean.LoginRecord;
import com.yzxt.yh.module.sys.bean.Org;
import com.yzxt.yh.module.sys.bean.Resource;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.service.LoginRecordService;
import com.yzxt.yh.module.sys.service.OrgService;
import com.yzxt.yh.module.sys.service.UserRoleService;
import com.yzxt.yh.module.sys.service.UserService;
import com.yzxt.yh.util.FileUtil;
import com.yzxt.yh.util.StringUtil;

public class WelcomeAction extends BaseAction
{
    private static final long serialVersionUID = 1L;

    private Logger logger = Logger.getLogger(WelcomeAction.class);

    private UserRoleService userRoleService;

    private UserService userService;

    private OrgService orgService;

    private LoginRecordService loginRecordService;

    public UserRoleService getUserRoleService()
    {
        return userRoleService;
    }

    public void setUserRoleService(UserRoleService userRoleService)
    {
        this.userRoleService = userRoleService;
    }

    public UserService getUserService()
    {
        return userService;
    }

    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }

    public OrgService getOrgService()
    {
        return orgService;
    }

    public void setOrgService(OrgService orgService)
    {
        this.orgService = orgService;
    }

    public LoginRecordService getLoginRecordService()
    {
        return loginRecordService;
    }

    public void setLoginRecordService(LoginRecordService loginRecordService)
    {
        this.loginRecordService = loginRecordService;
    }

    public String login()
    {
        String account = getRequest().getParameter("account");
        account = account != null ? account.trim() : "";
        String password = getRequest().getParameter("password");
        password = password != null ? password : "";
        String formSubFlag = getRequest().getParameter("formSubFlag");
        getRequest().setAttribute("formSubFlag", formSubFlag);
        getRequest().setAttribute("account", account);
        String res = "";
        String errorInfo = "";
        User user = null;
        String showOrgId = request.getParameter("orgId");
        try
        {
            // 如果是直接输入登录地址
            if (!"1".equals(formSubFlag))
            {
                res = "login";
            }
            else
            {
                if (account != null && account.length() > 0)
                {
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
                    if (user != null)
                    {
                        if (user.getState() != null && user.getState() == 1)
                        {
                            if (StringUtil.isNotEmpty(password))
                            {
                                if (userService.getPasswordValid(user.getId(), password))
                                {
                                    Collection<String> roles = userRoleService.getRoleIdsByUser(user.getId());
                                    user.setRoles(roles);
                                    List<Resource>[] ress = userRoleService.getFuncs(user.getId());
                                    if (ress != null)
                                    {
                                        LoginRecord loginRecord = new LoginRecord();
                                        Timestamp now = new Timestamp(System.currentTimeMillis());
                                        loginRecord.setUserId(user.getId());
                                        loginRecord.setType(Constant.LOGIN_TYPE_WEB);
                                        loginRecord.setCreateTime(now);
                                        loginRecordService.add(loginRecord);
                                        request.setAttribute("resources", ress);
                                        getSession().setAttribute(ConstSv.SESSION_USER_KEY, user);
                                        res = "main";
                                        // 管理员不改变登录时的显示组织
                                        if (!Constant.USER_TYPE_ADMIN.equals(user.getType()))
                                        {
                                            showOrgId = user.getOrgId();
                                        }
                                    }
                                    else
                                    {
                                        errorInfo = "用户未分配权限,请联系管理员.";
                                        res = "login";
                                    }
                                }
                                else
                                {
                                    errorInfo = "密码错误.";
                                    res = "login";
                                }
                            }
                            else
                            {
                                errorInfo = "密码为空.";
                                res = "login";
                            }
                        }
                        else
                        {
                            errorInfo = "用户处于冻结状态.";
                            res = "login";
                        }
                    }
                    else
                    {
                        errorInfo = "用户名不存在.";
                        res = "login";
                    }
                }
                else
                {
                    errorInfo = "用户名为空.";
                    res = "login";
                }
            }
            // 登录成功，显示用户组织，其它情况使用默认页面传递的组织
            Org org = orgService.getShowOrg(showOrgId);
            if (org != null && StringUtil.isNotEmpty(org.getLogoPath()))
            {
                org.setLogoPath(FileUtil.encodePath(org.getLogoPath()));
            }
            request.setAttribute("org", org);
        }
        catch (Exception e)
        {
            logger.error("登录出错，用户：" + account, e);
        }
        getRequest().setAttribute("errorInfo", errorInfo);
        return res;
    }

    /**
     * 退出
     * 
     * @return
     */
    public String logout()
    {
        try
        {
            HttpSession httpSession = getSession();
            httpSession.removeAttribute(ConstSv.SESSION_USER_KEY);
            httpSession.invalidate();
            String orgId = StringUtil.trim(request.getParameter("orgId"));
            if (StringUtil.isNotEmpty(orgId))
            {
                Org org = orgService.getShowOrg(orgId);
                if (org != null && StringUtil.isNotEmpty(org.getLogoPath()))
                {
                    org.setLogoPath(FileUtil.encodePath(org.getLogoPath()));
                }
                request.setAttribute("org", org);
            }
        }
        catch (Exception e)
        {
            logger.error("登出出错。", e);
        }
        return "login";
    }

    /**
     * 修改密码
     * 
     * @return
     */
    public void changePwd() throws Exception
    {
        int state = Result.STATE_UNKNOWN;
        String msg = "";
        String oldPassword = getRequest().getParameter("oldPassword");
        String newPassword = getRequest().getParameter("newPassword");
        User user = (User) getCurrentUser();
        if (user != null)
        {
            boolean oldValid = userService.getPasswordValid(user.getId(), oldPassword);
            if (oldValid)
            {
                userService.updatePassword(user.getId(), newPassword);
                state = Result.STATE_SUCESS;
                msg = "密码修改成功。";
            }
            else
            {
                state = Result.STATE_FAIL;
                msg = "修改密码失败，原密码错误。";
            }
        }
        else
        {
            state = Result.STATE_FAIL;
            msg = "修改密码失败，连接超时，请重新登录。";
        }
        super.write(new Result(state, msg, null));
    }

    public String forword()
    {
        String toUrl = getRequest().getParameter("toUrl");
        getRequest().setAttribute("toUrl", toUrl);
        return "tofuncs";
    }

    public String home()
    {
        HttpSession session = getSession();
        User user = (User) session.getAttribute(ConstSv.SESSION_USER_KEY);
        if (Constant.USER_TYPE_CUSTOMER.equals(user.getType()))
        {
            return "customerHome";
        }
        else if (Constant.USER_TYPE_DOCTOR.equals(user.getType()))
        {
            return "doctorHome";
        }
        if (Constant.USER_TYPE_ADMIN.equals(user.getType()))
        {
            return "adminHome";
        }
        return null;
    }

}
