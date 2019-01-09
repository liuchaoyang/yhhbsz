package com.yzxt.yh.module.sys.dao;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.yh.module.sys.bean.LoginRecord;

public class LoginRecordDao extends HibernateSupport<LoginRecord> implements BaseDao<LoginRecord>
{
    /**
     * 插入用户登录信息
     */
    public String insert(LoginRecord loginRecord) throws Exception
    {
        super.save(loginRecord);
        return loginRecord.getId();
    }

}
