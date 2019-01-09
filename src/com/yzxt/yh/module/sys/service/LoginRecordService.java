package com.yzxt.yh.module.sys.service;

import java.sql.Timestamp;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.module.sys.bean.LoginRecord;
import com.yzxt.yh.module.sys.dao.LoginRecordDao;

@Transactional(ConstTM.DEFAULT)
public class LoginRecordService
{
    private LoginRecordDao loginRecordDao;

    public LoginRecordDao getLoginRecordDao()
    {
        return loginRecordDao;
    }

    public void setLoginRecordDao(LoginRecordDao loginRecordDao)
    {
        this.loginRecordDao = loginRecordDao;
    }

    /**
     * 新增用户登录信息
     * @param loginRecord
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void add(LoginRecord loginRecord) throws Exception
    {
        if (loginRecord.getCreateTime() != null)
        {
            loginRecord.setCreateTime(new Timestamp(System.currentTimeMillis()));
        }
        loginRecordDao.insert(loginRecord);
    }

}
