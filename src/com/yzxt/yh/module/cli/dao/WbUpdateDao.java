package com.yzxt.yh.module.cli.dao;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.yh.module.cli.bean.WbUpdate;

public class WbUpdateDao extends HibernateSupport<WbUpdate> implements BaseDao<WbUpdate>
{
    /**
     * 增加需要下发的的数据
     * */
    public String insert(WbUpdate bean) throws Exception
    {
        super.save(bean);
        return bean.getId();
    }
}
