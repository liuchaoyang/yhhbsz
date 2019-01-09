package com.yzxt.yh.module.chr.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.yh.module.chr.bean.Glycuresis;

public class ChrGlyInfoDao extends HibernateSupport<Glycuresis> implements BaseDao<Glycuresis>
{
    public String insert(Glycuresis info) throws Exception
    {
        super.save(info);
        return info.getId();
    }

    @SuppressWarnings("unchecked")
    public Glycuresis getGlyInfoByVisitId(String id) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" from chr_glycuresis t ");
        sql.append(" inner join sys_user su on su.id = t.cust_id");
        sql.append(" inner join sys_user sur on sur.id = t.doctor_id");
        sql.append(" inner join chr_visit cv on cv.id = t.visit_id");
        sql.append(" where 1=1 and t.visit_id = ?");
        params.add(id, Hibernate.STRING);
        StringBuilder mSql = new StringBuilder();
        mSql.append(" select t.id, t.cust_id, t.visit_id, t.flup_date, t.flup_type, t.flup_rsult");
        mSql.append(" , t.doctor_id, t.next_flup_time, t.hbp_symptom, t.hbp_symptom_other, t.hbp_bps");
        mSql.append(" , t.hbp_bpd, t.hbp_weight, t.hbp_physique, t.hbp_foot_back, t.hbp_other");
        mSql.append(" , t.hbp_smoking, t.hbp_drinking, t.hbp_food, t.hbp_psycrecovery, t.hbp_compliance");
        mSql.append(" , t.hbp_fast_glu, t.hbp_durgs_obey, t.hbp_drugs_untoward, t.hbp_low_suger, t.hbp_refer_why");
        mSql.append(" , t.hbp_refer_org, t.hbp_refer_obj, t.create_time, t.status");
        mSql.append(" , cv.visit_no");
        mSql.append(" , su.id_card, su.name_ as memberName, sur.name_ as doctorName").append(sql);
        List<Object[]> objsList = super.getSession().createSQLQuery(mSql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("cust_id", Hibernate.STRING).addScalar("visit_id", Hibernate.STRING)
                .addScalar("flup_date", Hibernate.DATE).addScalar("flup_type", Hibernate.INTEGER)
                .addScalar("flup_rsult", Hibernate.INTEGER).addScalar("doctor_id", Hibernate.STRING)
                .addScalar("next_flup_time", Hibernate.TIMESTAMP).addScalar("hbp_symptom", Hibernate.STRING)
                .addScalar("hbp_symptom_other", Hibernate.STRING).addScalar("hbp_bps", Hibernate.INTEGER)
                .addScalar("hbp_bpd", Hibernate.INTEGER).addScalar("hbp_weight", Hibernate.DOUBLE)
                .addScalar("hbp_physique", Hibernate.DOUBLE).addScalar("hbp_foot_back", Hibernate.INTEGER)
                .addScalar("hbp_other", Hibernate.STRING).addScalar("hbp_smoking", Hibernate.INTEGER)
                .addScalar("hbp_drinking", Hibernate.INTEGER).addScalar("hbp_food", Hibernate.INTEGER)
                .addScalar("hbp_psycrecovery", Hibernate.INTEGER).addScalar("hbp_compliance", Hibernate.INTEGER)
                .addScalar("hbp_fast_glu", Hibernate.DOUBLE).addScalar("hbp_durgs_obey", Hibernate.INTEGER)
                .addScalar("hbp_drugs_untoward", Hibernate.INTEGER).addScalar("hbp_low_suger", Hibernate.INTEGER)
                .addScalar("hbp_refer_why", Hibernate.STRING).addScalar("hbp_refer_org", Hibernate.STRING)
                .addScalar("hbp_refer_obj", Hibernate.STRING).addScalar("create_time", Hibernate.TIMESTAMP)
                .addScalar("status", Hibernate.INTEGER).addScalar("visit_no", Hibernate.STRING)
                .addScalar("id_card", Hibernate.STRING).addScalar("memberName", Hibernate.STRING)
                .addScalar("doctorName", Hibernate.STRING).setParameters(params.getVals(), params.getTypes()).list();
        Glycuresis glyInfo = null;
        if (objsList != null && objsList.size() > 0)
        {
            glyInfo = new Glycuresis();
            Object[] objs = objsList.get(0);
            int idx = 0;
            glyInfo.setId((String) objs[idx]);
            idx++;
            glyInfo.setCustId((String) objs[idx]);
            idx++;
            glyInfo.setVisitId((String) objs[idx]);
            idx++;
            glyInfo.setFlupDate((Date) objs[idx]);
            idx++;
            glyInfo.setFlupType((Integer) objs[idx]);
            idx++;
            glyInfo.setFlupRsult((Integer) objs[idx]);
            idx++;
            glyInfo.setDoctorId((String) objs[idx]);
            idx++;
            glyInfo.setNextFlupTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            glyInfo.setGlySymptom((String) objs[idx]);
            idx++;
            glyInfo.setGlySymptomOther((String) objs[idx]);
            idx++;
            glyInfo.setHbpBps((Integer) objs[idx]);
            idx++;
            glyInfo.setHbpBpd((Integer) objs[idx]);
            idx++;
            glyInfo.setHbpWeight((Double) objs[idx]);
            idx++;
            glyInfo.setHbpPhysique((Double) objs[idx]);
            idx++;
            glyInfo.setHbpFootBack((Integer) objs[idx]);
            idx++;
            glyInfo.setHbpOther((String) objs[idx]);
            idx++;
            glyInfo.setHbpSmoking((Integer) objs[idx]);
            idx++;
            glyInfo.setHbpDrinking((Integer) objs[idx]);
            idx++;
            glyInfo.setHbpFood((Integer) objs[idx]);
            idx++;
            glyInfo.setHbpPsycrecovery((Integer) objs[idx]);
            idx++;
            glyInfo.setHbpCompliance((Integer) objs[idx]);
            idx++;
            glyInfo.setHbpFastGlu((Double) objs[idx]);
            idx++;
            glyInfo.setHbpDurgsObey((Integer) objs[idx]);
            idx++;
            glyInfo.setHbpDrugsUntoward((Integer) objs[idx]);
            idx++;
            glyInfo.setHbpLowSuger((Integer) objs[idx]);
            idx++;
            glyInfo.setHbpReferWhy((String) objs[idx]);
            idx++;
            glyInfo.setHbpReferOrg((String) objs[idx]);
            idx++;
            glyInfo.setHbpReferObj((String) objs[idx]);
            idx++;
            glyInfo.setCreateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            glyInfo.setStatus((Integer) objs[idx]);
            idx++;
            glyInfo.setVisitNo((String) objs[idx]);
            idx++;
            glyInfo.setIdCard((String) objs[idx]);
            idx++;
            glyInfo.setMemberName((String) objs[idx]);
            idx++;
            glyInfo.setDoctorName((String) objs[idx]);
            idx++;
        }
        return glyInfo;
    }

}
