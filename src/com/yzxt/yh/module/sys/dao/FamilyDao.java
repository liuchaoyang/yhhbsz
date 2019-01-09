package com.yzxt.yh.module.sys.dao;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.yh.module.sys.bean.CustFamily;

/**
 * 家庭圈
 * @author h
 * 2015.6.18
 * */
public class FamilyDao extends HibernateSupport<CustFamily> implements BaseDao<CustFamily>
{
    /**
     * 增加家庭关系
     * @author h
     * 2015.6.18
     */
    public String insert(CustFamily custFamily) throws Exception
    {
        super.insert(custFamily);
        return custFamily.getId();
    }
    
}
