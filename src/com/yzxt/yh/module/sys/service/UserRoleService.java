package com.yzxt.yh.module.sys.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.module.sys.bean.Resource;
import com.yzxt.yh.module.sys.bean.UserRole;
import com.yzxt.yh.module.sys.dao.UserRoleDao;

@Transactional(ConstTM.DEFAULT)
public class UserRoleService
{
    private UserRoleDao userRoleDao;

    public UserRoleDao getUserRoleDao()
    {
        return userRoleDao;
    }

    public void setUserRoleDao(UserRoleDao userRoleDao)
    {
        this.userRoleDao = userRoleDao;
    }

    /**
     * 根据用户ID获得该用户的所有角色ID
     * @param userId
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Collection<String> getRoleIdsByUser(String userId) throws Exception
    {
        return userRoleDao.getRoleIdsByUser(userId);
    }

    /**
     * 根据角色ID的集合获取所有资源ID
     * This method is used to get role of right
     * @param roles
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Resource>[] getFuncs(String userId) throws Exception
    {
        return userRoleDao.getResourcesByUser(userId);
    }

    /**
     * 分页查询角色数据
     * @param filter
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<UserRole> query(Map<String, Object> filter, int page, int pageSize)
    {
        return userRoleDao.query(filter, page, pageSize);
    }

    /**
     * 新增客户
     * @param cust
     * @return
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result add(UserRole userRole) throws Exception
    {
        userRoleDao.insert(userRole);
        return new Result(Result.STATE_SUCESS, "保存成功。", null);
    }

    /**
     * 删除角色
     * @param cust
     * @return
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result deleteRolesByUser(String userId) throws Exception
    {
        userRoleDao.deleteRolesByUser(userId);
        return new Result(Result.STATE_SUCESS, "成功。", null);
    }

}
