package com.yzxt.yh.module.sys.service;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.bean.UserPassword;
import com.yzxt.yh.module.sys.bean.UserSession;
import com.yzxt.yh.module.sys.dao.UserDao;
import com.yzxt.yh.module.sys.dao.UserSessionDao;
import com.yzxt.yh.util.DateUtil;

@Transactional(ConstTM.DEFAULT)
public class UserService
{
    private UserDao userDao;

    private UserSessionDao userSessionDao;

    public UserDao getUserDao()
    {
        return userDao;
    }

    public void setUserDao(UserDao userDao)
    {
        this.userDao = userDao;
    }

    public UserSessionDao getUserSessionDao()
    {
        return userSessionDao;
    }

    public void setUserSessionDao(UserSessionDao userSessionDao)
    {
        this.userSessionDao = userSessionDao;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveUser(User user) throws Exception
    {
        userDao.save(user);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public User getUser(String id) throws Exception
    {
        return userDao.get(id);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public User getUserByAcount(String account) throws Exception
    {
        return userDao.getUserByAcount(account);
    }

    /**
     * 通过用户身份证号码查询用户
     * @param idCard
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public User getUserByIdCard(String idCard)
    {
        return userDao.getUserByIdCard(idCard);
    }

    /**
     * 通过用户手机号码查询用户
     * @param phone
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public User getUserByPhone(String phone)
    {
        return userDao.getUserByPhone(phone);
    }

    /**
     * 通过用户电子邮箱查询用户
     * @param email
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public User getUserByEmail(String email)
    {
        return userDao.getUserByEmail(email);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean updatePassword(String userId, String newPassword) throws Exception
    {
        return userDao.updatePassword(userId, newPassword) > 0;
    }
    
    /**
     * 更新验证码
     * @param userId
     * 
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean updateCode(User user) throws Exception
    {
        return userDao.updateCode(user) > 0;
    }
    /**
     * 获取用户信息
     * 
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public User loadUser(String userId)
    {
        return userDao.get(userId);
    }
    
    /**
     * 验证用户密码是否有效
     * @param userId
     * @param newPassword
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public boolean getPasswordValid(String userId, String password) throws Exception
    {
        return userDao.getPasswordValid(userId, password);
    }
    
    /**
     * 判断是否是有效的客户端请求验证信息
     * 
     * @param ticket
     * @param userId
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public UserSession getValidSession(String ticket, String userId)
    {
        return userSessionDao.getValidSession(ticket, userId, new Timestamp(System.currentTimeMillis()));
    }
    
    /**
     * 保存客户端登录会话信息
     * 
     * @param userSession
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveSession(UserSession userSession)
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        userSession.setCreateTime(now);
        userSession.setExpiryTime(DateUtil.addDay(now, Constant.CLIENT_LOGIN_EXPIRY_DAYS));
        userSessionDao.save(userSession);
    }

    /**
     * 删除过期的会话
     * @param now
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteExpirySession(Date now)
    {
        userSessionDao.deleteExpirySession(now);
    }
    
    /**
    * 保存通讯录信息
    * 
    * @param userSession
     * @throws Exception 
    */
   @Transactional(propagation = Propagation.REQUIRED)
   public void save(User bean) throws Exception
   {
       Timestamp now = new Timestamp(System.currentTimeMillis());
       bean.setCreateTime(now);
       
       userDao.insert(bean);
   }
   /**
    * 通过用户Id查询用户密码
    * @param phone
    * @return
    */
   @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
   public UserPassword getPassword(String userId)
   {
       return userDao.getPassword(userId);
   }

}
