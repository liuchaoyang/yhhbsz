package com.yzxt.yh.module.sys.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.module.sys.bean.Resource;
import com.yzxt.yh.module.sys.bean.Role;
import com.yzxt.yh.module.sys.dao.RoleDao;

@Transactional(ConstTM.DEFAULT)
public class RoleService
{
    private RoleDao roleDao;

    public RoleDao getRoleDao()
    {
        return roleDao;
    }

    public void setRoleDao(RoleDao roleDao)
    {
        this.roleDao = roleDao;
    }

    /**
     * 加载角色信息
     * @return
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Role load(String roleId) throws Exception
    {
        Role role = roleDao.load(roleId);
        return role;
    }

    /**
     * 新增用户角色
     * @return
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result add(Role role) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        role.setCreateTime(now);
        role.setUpdateTime(now);
        role.setType(2);
        roleDao.insert(role);
        return new Result(Result.STATE_SUCESS, "保存成功。", null);
    }

    /**
     * 更新角色信息
     * @return
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result update(Role role) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        role.setUpdateTime(now);
        roleDao.update(role);
        return new Result(Result.STATE_SUCESS, "保存成功。", null);
    }

    /**
     * 删除角色信息
     * @param cust
     * @return
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result delete(Role role) throws Exception
    {
        roleDao.delete(role);
        return new Result(Result.STATE_SUCESS, "删除成功。", null);
    }

    /**
     * 分页查询角色数据
     * @param filter
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<Role> query(Map<String, Object> filter, int page, int pageSize)
    {
        return roleDao.query(filter, page, pageSize);
    }

    /**
     * 判断角色是否存在
     * @param roleName
     * @param exceptRoleId
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public boolean getRoleExist(String roleName, String exceptRoleId) throws Exception
    {
        return roleDao.getRoleExist(roleName, exceptRoleId);
    }

    /**
     * 查询所有菜单
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Resource> getAllResources() throws Exception
    {
        return roleDao.getAllResources();
    }

    /**
     * 查询所有菜单
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Resource> getResourcesByRole(String roleId) throws Exception
    {
        return roleDao.getResourcesByRole(roleId);
    }

    /**
     * 更新角色资源信息
     * @param cust
     * @return
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result updateRoleRess(String roleId, String resourceIds) throws Exception
    {
        roleDao.updateRoleRess(roleId, resourceIds);
        return new Result(Result.STATE_SUCESS, "保存成功。", null);
    }

}
