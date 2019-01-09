package com.yzxt.yh.module.chr.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.yh.module.chr.bean.PreSport;

public class PreSportDao extends HibernateSupport<PreSport> implements BaseDao<PreSport>
{
    public String insert(PreSport preSport) throws Exception
    {
        super.save(preSport);
        return preSport.getId();
    }

    @SuppressWarnings("unchecked")
    public List<PreSport> getSportsByPreId(String id)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder mSql = new StringBuilder();
        mSql.append(" from chr_pre_sport t ");
        mSql.append(" inner join chr_pressure cp on cp.id = t.h_id ");
        mSql.append(" where 1 = 1 and t.h_id = ?");
        params.add(id, Hibernate.STRING);
        StringBuilder pSql = new StringBuilder();
        pSql.append("select t.id, t.hbp_spt_fy,t.hbp_spt_time, t.h_id ").append(mSql);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("hbp_spt_fy", Hibernate.INTEGER).addScalar("hbp_spt_time", Hibernate.INTEGER)
                .addScalar("h_id", Hibernate.STRING).setParameters(params.getVals(), params.getTypes()).list();
        List<PreSport> list = new ArrayList<PreSport>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            PreSport preSport = new PreSport();
            idx = 0;
            preSport.setId((String) objs[idx]);
            idx++;
            preSport.setHbpSptFy((Integer) objs[idx]);
            idx++;
            preSport.setHbpSptTime((Integer) objs[idx]);
            idx++;
            preSport.sethId((String) objs[idx]);
            idx++;
            list.add(preSport);
        }
        return list;
    }

}
