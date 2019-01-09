package com.yzxt.yh.module.chr.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.yh.module.chr.bean.PreDrug;

public class PreDrugDao extends HibernateSupport<PreDrug> implements BaseDao<PreDrug>
{

    public String insert(PreDrug preDrug) throws Exception
    {
        super.save(preDrug);
        return preDrug.getId();
    }

    @SuppressWarnings("unchecked")
    public List<PreDrug> getDrugsByPreId(String id)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder mSql = new StringBuilder();
        mSql.append(" from chr_pre_drug t ");
        mSql.append(" inner join chr_pressure cp on cp.id = t.h_id ");
        mSql.append(" where 1 = 1 and t.h_id = ?");
        params.add(id, Hibernate.STRING);
        StringBuilder pSql = new StringBuilder();
        pSql.append("select t.id, t.hbp_drugs_name,t.hbp_drugs_fy, t.hbp_drugs_count, t.h_id ").append(mSql);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("hbp_drugs_name", Hibernate.STRING).addScalar("hbp_drugs_fy", Hibernate.INTEGER)
                .addScalar("hbp_drugs_count", Hibernate.INTEGER).addScalar("h_id", Hibernate.STRING)
                .setParameters(params.getVals(), params.getTypes()).list();
        List<PreDrug> list = new ArrayList<PreDrug>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            PreDrug preDrug = new PreDrug();
            idx = 0;
            preDrug.setId((String) objs[idx]);
            idx++;
            preDrug.setHbpDrugsName((String) objs[idx]);
            idx++;
            preDrug.setHbpDrugsFy((Integer) objs[idx]);
            idx++;
            preDrug.setHbpDrugsCount((Integer) objs[idx]);
            idx++;
            preDrug.setHId((String) objs[idx]);
            idx++;
            list.add(preDrug);
        }
        return list;
    }

}
