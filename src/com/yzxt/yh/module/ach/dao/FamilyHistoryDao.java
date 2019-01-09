package com.yzxt.yh.module.ach.dao;

import java.util.List;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.yh.module.ach.bean.FamilyHistory;

public class FamilyHistoryDao extends HibernateSupport<FamilyHistory> implements BaseDao<FamilyHistory>
{

    /**
     * 加载家庭史信息
     */
    @SuppressWarnings("unchecked")
    public List<FamilyHistory> getByCust(String id) throws Exception
    {
        HibernateParams params = new HibernateParams();
        String sql = "select t from FamilyHistory t where t.custId = ?";
        params.add(id, Hibernate.STRING);
        List<FamilyHistory> achiveFamilyHistorys = super.getSession().createQuery(sql)
                .setParameters(params.getVals(), params.getTypes()).list();
        return achiveFamilyHistorys != null && !achiveFamilyHistorys.isEmpty() ? achiveFamilyHistorys : null;

    }

    public void deleteByCust(String custId)
    {
        HibernateParams params = new HibernateParams();
        String sql = "delete from FamilyHistory t where t.custId = ?";
        params.add(custId, Hibernate.STRING);
        super.getSession().createQuery(sql).setParameters(params.getVals(), params.getTypes()).executeUpdate();

    }

}
