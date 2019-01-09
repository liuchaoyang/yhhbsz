package com.yzxt.yh.module.pwd.dao;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.yh.module.pwd.bean.FindPwd;

public class FindPwdDao extends HibernateSupport<FindPwd> implements BaseDao<FindPwd>
{
    /**
     * 加载角色信息
     */
    public FindPwd load(String id) throws Exception
    {
        return super.get(id);
    }

}
