package com.yzxt.yh.module.sys.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.module.sys.bean.PushUser;
import com.yzxt.yh.module.sys.dao.PushUserDao;

@Transactional(ConstTM.DEFAULT)
public class PushUserService
{
    private PushUserDao pushUserDao;

    public PushUserDao getPushUserDao()
    {
        return pushUserDao;
    }

    public void setPushUserDao(PushUserDao pushUserDao)
    {
        this.pushUserDao = pushUserDao;
    }

    /**
     * 保存用户推送映射信息
     * @param idCard
     * @return
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(PushUser pushUser) throws Exception
    {
        int c = pushUserDao.update(pushUser);
        // 没有以前的记录，直接保存
        if (c <= 0)
        {
            pushUserDao.insert(pushUser);
        }
    }

    /**
     * 通过用户身份证号码查询用户
     * @param phone
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(PushUser pushUser) throws Exception
    {
        pushUserDao.delete(pushUser);
    }

}
