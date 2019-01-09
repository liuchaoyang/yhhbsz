package com.yzxt.yh.module.sys.dao;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.yh.module.sys.bean.User;

public class SeqDao extends HibernateSupport<User> implements BaseDao<User>
{
    /**
     * 获取序列下一个值
     */
    public int getNextVal(String seqName) throws Exception
    {
        String sql = "select nextval('" + seqName + "')";
        Integer i = (Integer) super.getSession().createSQLQuery(sql).uniqueResult();
        return i.intValue();
    }

}
