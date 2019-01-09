package com.yzxt.yh.module.sys.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yzxt.fw.common.BaseAction;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.module.sys.bean.Org;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.service.AdminService;
import com.yzxt.yh.module.sys.service.OrgService;
import com.yzxt.yh.module.sys.service.UserService;
import com.yzxt.yh.util.StringUtil;

public class AdminAction extends BaseAction
{
    private Logger logger = Logger.getLogger(AdminAction.class);
    private static final long serialVersionUID = 1L;

    private AdminService adminService;
    private UserService userService;
    private OrgService orgService;

    private User user;

    public AdminService getAdminService()
    {
        return adminService;
    }

    public void setAdminService(AdminService adminService)
    {
        this.adminService = adminService;
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

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    /**
     * 跳转到组织管理员查询页面
     * @throws Exception 
     */
    public String toQuery() throws Exception
    {
        try
        {
            return "list";
        }
        catch (Exception e)
        {
            logger.error("跳转组织管理员查询页面出错。", e);
            return "error";
        }
    }

    /**
     * 查询组织管理员
     * @throws Exception 
     */
    public void query() throws Exception
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("orgId", StringUtil.trim(request.getParameter("orgId")));
            // 查询人
            filter.put("operUser", super.getCurrentUser());
            PageModel<User> pageModel = adminService.query(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("查询页面出错。", e);
        }
    }

    /**
     * 跳转到组织管理员新增页面
     * @throws Exception 
     */
    public String toEdit() throws Exception
    {
        try
        {
            // 管理员用户的ID
            String id = request.getParameter("id");
            Org org = null;
            if (StringUtil.isEmpty(id))
            {
                user = new User();
                // 取组织ID
                String orgId = request.getParameter("orgId");
                org = orgService.getOrg(orgId);
                user.setOrgId(orgId);
            }
            else
            {
                user = userService.getUser(id);
                String orgId = user.getOrgId();
                org = orgService.getOrg(orgId);
            }
            request.setAttribute("user", user);
            request.setAttribute("org", org);
            return "edit";
        }
        catch (Exception e)
        {
            logger.error("跳转组织管理员查看页面出错。", e);
            return "error";
        }
    }

    /**
     * 保存组织管理员
     * @throws Exception 
     */
    public void save() throws Exception
    {
        Result r = null;
        try
        {
            // 当前操作人
            User curOper = (User) super.getCurrentUser();
            user.setUpdateBy(curOper.getId());
            String id = user.getId();
            if (StringUtil.isEmpty(id))
            {
                r = adminService.add(user);
            }
            else
            {
                r = adminService.update(user);
            }
        }
        catch (Exception e)
        {
            r = new Result(Result.STATE_FAIL, "保存组织管理错误。", null);
            logger.error("保存客户错误。", e);
        }
        super.write(r);
    }

}
