package com.yzxt.yh.module.ach.dao;

import java.util.List;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.yh.module.ach.bean.PreviousHistory;

public class PreviousHistoryDao extends HibernateSupport<PreviousHistory> implements BaseDao<PreviousHistory>
{

    /**
     * 加载档案信息
     */
    @SuppressWarnings("unchecked")
    public List<PreviousHistory> getByCust(String id) throws Exception
    {
        HibernateParams params = new HibernateParams();
        String sql = "select t from PreviousHistory t where t.custId = ?";
        params.add(id, Hibernate.STRING);
        List<PreviousHistory> achivePreviousHistorys = super.getSession().createQuery(sql)
                .setParameters(params.getVals(), params.getTypes()).list();
        return achivePreviousHistorys != null && !achivePreviousHistorys.isEmpty() ? achivePreviousHistorys : null;

    }

    public void deleteByCust(String custId)
    {
        HibernateParams params = new HibernateParams();
        String sql = "delete from PreviousHistory t where t.custId = ?";
        params.add(custId, Hibernate.STRING);
        super.getSession().createQuery(sql).setParameters(params.getVals(), params.getTypes()).executeUpdate();

    }
}
