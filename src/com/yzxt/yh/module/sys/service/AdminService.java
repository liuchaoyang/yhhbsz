package com.yzxt.yh.module.sys.service;

import java.sql.Timestamp;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.ConstRole;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.bean.UserRole;
import com.yzxt.yh.module.sys.dao.AdminDao;
import com.yzxt.yh.module.sys.dao.UserDao;
import com.yzxt.yh.util.StringUtil;

@Transactional(ConstTM.DEFAULT)
public class AdminService
{
    private AdminDao adminDao;
    private UserDao userDao;
    private UserRoleService userRoleService;

    public AdminDao getAdminDao()
    {
        return adminDao;
    }

    public void setAdminDao(AdminDao adminDao)
    {
        this.adminDao = adminDao;
    }

    public UserDao getUserDao()
    {
        return userDao;
    }

    public void setUserDao(UserDao userDao)
    {
        this.userDao = userDao;
    }

    public UserRoleService getUserRoleService()
    {
        return userRoleService;
    }

    public void setUserRoleService(UserRoleService userRoleService)
    {
        this.userRoleService = userRoleService;
    }

    /**
     * 分页组织管理员数据
     * @param filter
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<User> query(Map<String, Object> filter, int page, int pageSize)
    {
        return adminDao.query(filter, page, pageSize);
    }

    /**
     * 新增机构管理员信息
     * @return
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result add(User user) throws Exception
    {
        String userId = user.getId();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        // 判断账号是否存在
        String account = user.getAccount();
        if (userDao.getAccountExist(account, null))
        {
            return new Result(Result.STATE_FAIL, "账号重复。", null);
        }
        String phone = user.getPhone();
        if (StringUtil.isNotEmpty(phone) && userDao.getPhoneExist(phone, null))
        {
            return new Result(Result.STATE_FAIL, "手机号重复。", null);
        }
        String email = user.getEmail();
        if (StringUtil.isNotEmpty(email) && userDao.getEmailExist(email, null))
        {
            return new Result(Result.STATE_FAIL, "Email重复。", null);
        }
        // 补充信息
        user.setState(Constant.USER_STATE_EFFECTIVE);
        user.setType(Constant.USER_TYPE_ADMIN);
        user.setCreateBy(user.getUpdateBy());
        user.setCreateTime(now);
        user.setUpdateTime(now);
        userDao.insert(user);
        userId = user.getId();
        // 补充登录密码
        userDao.addPassword(userId, Constant.USER_DEFAULT_PASSWROD);
        // 补充用户角色
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(ConstRole.ORG_ADMIN);
        userRoleService.add(userRole);
        return new Result(Result.STATE_SUCESS, "保存成功。", null);
    }

    /**
     * 修改机构管理员信息
     * @return
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result update(User user) throws Exception
    {
        String userId = user.getId();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        String phone = user.getPhone();
        if (StringUtil.isNotEmpty(phone) && userDao.getPhoneExist(phone, userId))
        {
            return new Result(Result.STATE_FAIL, "手机号重复。", null);
        }
        String email = user.getEmail();
        if (StringUtil.isNotEmpty(email) && userDao.getEmailExist(email, userId))
        {
            return new Result(Result.STATE_FAIL, "Email重复。", null);
        }
        // 补充信息
        user.setUpdateTime(now);
        userDao.updateByAdmin(user);
        return new Result(Result.STATE_SUCESS, "保存成功。", null);
    }

}
