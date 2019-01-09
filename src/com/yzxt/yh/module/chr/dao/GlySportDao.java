package com.yzxt.yh.module.chr.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.yh.module.chr.bean.GlySport;

public class GlySportDao extends HibernateSupport<GlySport> implements BaseDao<GlySport>
{
    public String insert(GlySport glySport) throws Exception
    {
        super.save(glySport);
        return glySport.getId();
    }

    @SuppressWarnings("unchecked")
    public List<GlySport> getSportsByGlyId(String id)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder mSql = new StringBuilder();
        mSql.append(" from chr_gly_sport t ");
        mSql.append(" inner join chr_glycuresis cg on cg.id = t.b_id ");
        mSql.append(" where 1 = 1 and t.b_id = ?");
        params.add(id, Hibernate.STRING);
        StringBuilder pSql = new StringBuilder();
        pSql.append("select t.id, t.hbp_spt_fy,t.hbp_spt_time, t.b_id ").append(mSql);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("hbp_spt_fy", Hibernate.INTEGER).addScalar("hbp_spt_time", Hibernate.INTEGER)
                .addScalar("b_id", Hibernate.STRING).setParameters(params.getVals(), params.getTypes()).list();
        List<GlySport> list = new ArrayList<GlySport>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            GlySport glySport = new GlySport();
            idx = 0;
            glySport.setId((String) objs[idx]);
            idx++;
            glySport.setHbpSptFy((Integer) objs[idx]);
            idx++;
            glySport.setHbpSptTime((Integer) objs[idx]);
            idx++;
            glySport.setBId((String) objs[idx]);
            idx++;
            list.add(glySport);
        }
        return list;
    }
}
