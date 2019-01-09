package com.yzxt.yh.module.chr.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.yh.module.chr.bean.GlyDrug;

public class GlyDrugDao extends HibernateSupport<GlyDrug> implements BaseDao<GlyDrug>
{
    public String insert(GlyDrug glyDrug) throws Exception
    {
        super.save(glyDrug);
        return glyDrug.getId();
    }

    @SuppressWarnings("unchecked")
    public List<GlyDrug> getDrugsByGlyId(String id)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder mSql = new StringBuilder();
        mSql.append(" from chr_gly_drug t ");
        mSql.append(" inner join chr_glycuresis cg on cg.id = t.b_id ");
        mSql.append(" where 1 = 1 and t.b_id = ?");
        params.add(id, Hibernate.STRING);
        StringBuilder pSql = new StringBuilder();
        pSql.append(
                "select t.id, t.hbp_drugs_name,t.hbp_drugs_fy, t.hbp_drugs_count, t.type_, t.b_id, t.gly_insulin_type, t.gly_insulin_use_method ")
                .append(mSql);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("hbp_drugs_name", Hibernate.STRING).addScalar("hbp_drugs_fy", Hibernate.INTEGER)
                .addScalar("hbp_drugs_count", Hibernate.INTEGER).addScalar("type_", Hibernate.INTEGER)
                .addScalar("b_id", Hibernate.STRING).addScalar("gly_insulin_type", Hibernate.STRING)
                .addScalar("gly_insulin_use_method", Hibernate.STRING)
                .setParameters(params.getVals(), params.getTypes()).list();
        List<GlyDrug> list = new ArrayList<GlyDrug>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            GlyDrug glyDrug = new GlyDrug();
            idx = 0;
            glyDrug.setId((String) objs[idx]);
            idx++;
            glyDrug.setHbpDrugsName((String) objs[idx]);
            idx++;
            glyDrug.setHbpDrugsFy((Integer) objs[idx]);
            idx++;
            glyDrug.setHbpDrugsCount((Integer) objs[idx]);
            idx++;

            glyDrug.setType((Integer) objs[idx]);
            idx++;
            glyDrug.setBId((String) objs[idx]);
            idx++;
            glyDrug.setGlyInsulinType((String) objs[idx]);
            idx++;
            glyDrug.setGlyInsulinUseMethod((String) objs[idx]);
            idx++;
            list.add(glyDrug);
        }
        return list;
    }

}
