package com.yzxt.yh.module.sys.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yzxt.fw.common.BaseAction;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.module.sys.bean.Role;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.bean.UserRole;
import com.yzxt.yh.module.sys.service.RoleService;
import com.yzxt.yh.module.sys.service.UserRoleService;
import com.yzxt.yh.module.sys.service.UserService;
import com.yzxt.yh.util.StringUtil;

public class UserRoleAction extends BaseAction
{
    private static final long serialVersionUID = 1L;
    private Logger logger = Logger.getLogger(UserRoleAction.class);

    private UserRoleService userRoleService;
    private RoleService roleService;
    private UserService userService;

    public void setRoleService(RoleService roleService)
    {
        this.roleService = roleService;
    }

    public void setUserRoleService(UserRoleService userRoleService)
    {
        this.userRoleService = userRoleService;
    }

    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }

    /**
     * 查询用户角色
     * @param cust
     */
    public void query()
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            String userName = request.getParameter("userName");
            filter.put("userName", StringUtil.trim(userName));
            // 查询人
            filter.put("operUser", super.getCurrentUser());
            PageModel<UserRole> pageModel = userRoleService.query(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("查询用户角色错误。", e);
        }
    }

    /**
     * 查询角色
     * @param cust
     */
    public void queryRole()
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            PageModel<Role> pageModel = roleService.query(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("查询角色错误。", e);
        }
    }

    /**
     * 添加客户角色
     */
    public void addUserRole()
    {
        Result r = null;
        try
        {
            UserRole userRole = new UserRole();
            String userId = request.getParameter("userId");
            String roleId = request.getParameter("roleId");
            userRoleService.deleteRolesByUser(userId);
            if (StringUtil.isNotEmpty(roleId))
            {
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                userRoleService.add(userRole);
            }
            r = new Result(Result.STATE_SUCESS, "保存成功。", null);
        }
        catch (Exception e)
        {
            r = new Result(Result.STATE_FAIL, "新增用户角色错误。", null);
            logger.error("添加用户角色错误。", e);
        }
        super.write(r);
    }

    public String toUserRoleEdit()
    {
        String userId = request.getParameter("userId");
        User user = userService.loadUser(userId);
        request.setAttribute("user", user);
        return "detail";
    }

}
