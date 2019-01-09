package com.yzxt.yh.module.ach.dao;

import java.util.List;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.yh.module.ach.bean.LifeEnv;

public class LifeEnvDao extends HibernateSupport<LifeEnv> implements BaseDao<LifeEnv>
{

    /**
     * 加载生活情况信息
     */
    @SuppressWarnings("unchecked")
    public List<LifeEnv> getByCust(String id) throws Exception
    {
        HibernateParams params = new HibernateParams();
        String sql = "select t from LifeEnv t where t.custId = ?";
        params.add(id, Hibernate.STRING);
        List<LifeEnv> achiveLifeEnvs = super.getSession().createQuery(sql)
                .setParameters(params.getVals(), params.getTypes()).list();
        return achiveLifeEnvs != null && !achiveLifeEnvs.isEmpty() ? achiveLifeEnvs : null;

    }

    public void deleteByCust(String custId)
    {
        HibernateParams params = new HibernateParams();
        String sql = "delete from LifeEnv t where t.custId = ?";
        params.add(custId, Hibernate.STRING);
        super.getSession().createQuery(sql).setParameters(params.getVals(), params.getTypes()).executeUpdate();
    }

}
