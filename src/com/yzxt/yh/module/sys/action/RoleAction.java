package com.yzxt.yh.module.sys.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yzxt.fw.common.BaseAction;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.module.sys.bean.Resource;
import com.yzxt.yh.module.sys.bean.Role;
import com.yzxt.yh.module.sys.service.RoleService;
import com.yzxt.yh.util.StringUtil;

public class RoleAction extends BaseAction
{
    private static final long serialVersionUID = 1L;
    private Logger logger = Logger.getLogger(RoleAction.class);

    private RoleService roleService;
    private Role role;

    public void setRoleService(RoleService roleService)
    {
        this.roleService = roleService;
    }

    public Role getRole()
    {
        return role;
    }

    public void setRole(Role role)
    {
        this.role = role;
    }

    /**
     * 跳转到客户明细页面
     * @throws Exception 
     */
    public String toDetail() throws Exception
    {
        try
        {
            String operType = request.getParameter("operType");
            if ("view".equals(operType) || "update".equals(operType))
            {
                String id = request.getParameter("id");
                role = roleService.load(id);
            }
            else
            {
                role = new Role();
            }
            request.setAttribute("operType", operType);
            if ("view".equals(operType))
            {
                return "view";
            }
            else
            {
                return "edit";
            }
        }
        catch (Exception e)
        {
            logger.error("跳转至角色明细页面出错。", e);
            return "error";
        }
    }

    /**
     * 添加角色
     * @param cust
     */
    public void add()
    {
        Result r = null;
        try
        {
            String roleName = role.getRoleName();
            boolean roleExist = roleService.getRoleExist(roleName, null);
            if (roleExist)
            {
                r = new Result(Result.STATE_FAIL, "角色重复。", null);
            }
            else
            {
                r = roleService.add(role);
            }
        }
        catch (Exception e)
        {
            r = new Result(Result.STATE_FAIL, "新增角色错误。", null);
            logger.error("添加角色错误。", e);
        }
        super.write(r);
    }

    /**
     * 修改客户
     * @param cust
     */
    public void update()
    {
        Result r = null;
        try
        {
            String roleName = role.getRoleName();
            String id = role.getId();
            boolean roleExist = roleService.getRoleExist(roleName, id);
            if (roleExist)
            {
                r = new Result(Result.STATE_FAIL, "角色重复。", null);
            }
            else
            {
                r = roleService.update(role);
            }
        }
        catch (Exception e)
        {
            r = new Result(Result.STATE_FAIL, "修改角色错误。", null);
            logger.error("修改角色错误。", e);
        }
        super.write(r);
    }

    /**
     * 删除角色
     */
    public void delete()
    {
        Result r = null;
        try
        {
            // 当前操作人
            String id = request.getParameter("id");
            Role load = roleService.load(id);
            r = roleService.delete(load);
        }
        catch (Exception e)
        {
            r = new Result(Result.STATE_FAIL, "删除角色错误。", null);
            logger.error("删除角色错误。", e);
        }
        super.write(r);
    }

    /**
     * 查询角色
     */
    public void query()
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("roleName", StringUtil.trim(request.getParameter("roleName")));
            PageModel<Role> pageModel = roleService.query(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("查询角色错误。", e);
        }
    }

    /**
     * 获取菜单角色资源
     * @return
     * @throws Exception
     */
    public String loadRess() throws Exception
    {
        List<Resource> ress = roleService.getAllResources();
        request.setAttribute("resources", ress);
        return "main.jsp";
    }

    /**
     * 跳转到角色资源页面
     * @return
     */
    public String toRoleRess()
    {
        try
        {
            String roleId = request.getParameter("roleId");
            List<Resource> ress = roleService.getResourcesByRole(roleId);
            request.setAttribute("roleId", roleId);
            request.setAttribute("selRess", ress);
        }
        catch (Exception e)
        {
            logger.error("获取角色资源出错。", e);
        }
        return "roleRess";
    }

    /**
     * 修改角色菜单资源
     * @param cust
     */
    public void updateRoleRess()
    {
        Result r = null;
        try
        {
            String roleId = request.getParameter("roleId");
            String resIds = request.getParameter("resIds");
            r = roleService.updateRoleRess(roleId, resIds);
        }
        catch (Exception e)
        {
            r = new Result(Result.STATE_FAIL, "保存角色资源错误。", null);
            logger.error("保存角色资源错误。", e);
        }
        super.write(r);
    }

}
