package com.yzxt.yh.module.chr.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.yh.module.chr.bean.GlyCheck;

public class GlyCheckDao extends HibernateSupport<GlyCheck> implements BaseDao<GlyCheck>
{
    public String insert(GlyCheck glyCheck) throws Exception
    {
        super.save(glyCheck);
        return glyCheck.getId();
    }

    @SuppressWarnings("unchecked")
    public List<GlyCheck> getChecksByGlyId(String id)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder mSql = new StringBuilder();
        mSql.append(" from chr_gly_check t ");
        mSql.append(" inner join chr_glycuresis cg on cg.id = t.b_id ");
        mSql.append(" where 1 = 1 and t.b_id = ?");
        params.add(id, Hibernate.STRING);
        StringBuilder pSql = new StringBuilder();
        pSql.append(
                "select t.id, t.b_id,t.hbp_check_name, t.hbp_check_type, t.hbp_suger_blood, t.hbp_check_time, t.hbp_check_remark")
                .append(mSql);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("b_id", Hibernate.STRING).addScalar("hbp_check_name", Hibernate.STRING)
                .addScalar("hbp_check_type", Hibernate.INTEGER).addScalar("hbp_suger_blood", Hibernate.DOUBLE)
                .addScalar("hbp_check_time", Hibernate.TIMESTAMP).addScalar("hbp_check_remark", Hibernate.STRING)
                .setParameters(params.getVals(), params.getTypes()).list();
        List<GlyCheck> list = new ArrayList<GlyCheck>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            GlyCheck glyCheck = new GlyCheck();
            idx = 0;
            glyCheck.setId((String) objs[idx]);
            idx++;
            glyCheck.setbId((String) objs[idx]);
            idx++;
            glyCheck.setHbpCheckName((String) objs[idx]);
            idx++;
            glyCheck.setHbpCheckType((Integer) objs[idx]);
            idx++;
            glyCheck.setHbpSugerBlood((Double) objs[idx]);
            idx++;
            glyCheck.setHbpCheckTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            glyCheck.setHbpCheckRemark((String) objs[idx]);
            idx++;
            list.add(glyCheck);
        }
        return list;
    }
}
