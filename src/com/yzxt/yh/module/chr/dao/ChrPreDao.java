package com.yzxt.yh.module.chr.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.yh.module.chr.bean.Pressure;

public class ChrPreDao extends HibernateSupport<Pressure> implements BaseDao<Pressure>
{

    public String insert(Pressure info) throws Exception
    {
        super.save(info);
        return info.getId();
    }

    @SuppressWarnings("unchecked")
    public Pressure getPreInfoByVisitId(String id)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" from chr_pressure t ");
        sql.append(" inner join sys_user su on su.id = t.cust_id");
        sql.append(" inner join sys_user sur on sur.id = t.doctor_id");
        sql.append(" inner join chr_visit cv on cv.id = t.visit_id");
        sql.append(" where 1=1 and t.visit_id = ?");
        params.add(id, Hibernate.STRING);
        StringBuilder mSql = new StringBuilder();
        mSql.append(" select t.id, t.cust_id, t.visit_id, t.flup_date, t.flup_type, t.flup_rsult");
        mSql.append(" , t.doctor_id, t.next_flup_time, t.hbp_symptom, t.hbp_symptom_other, t.hbp_bps");
        mSql.append(" , t.hbp_bpd, t.hbp_weight, t.hbp_physique, t.hbp_pulse, t.hbp_other");
        mSql.append(" , t.hbp_smoking, t.hbp_drinking, t.hbp_salarium, t.hbp_psycrecovery, t.hbp_compliance");
        mSql.append(" , t.hbp_help_check, t.hbp_durgs_obey, t.hbp_drugs_untoward, t.hbp_referWhy");
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
                .addScalar("hbp_physique", Hibernate.DOUBLE).addScalar("hbp_pulse", Hibernate.INTEGER)
                .addScalar("hbp_other", Hibernate.STRING).addScalar("hbp_smoking", Hibernate.INTEGER)
                .addScalar("hbp_drinking", Hibernate.DOUBLE).addScalar("hbp_salarium", Hibernate.INTEGER)
                .addScalar("hbp_psycrecovery", Hibernate.INTEGER).addScalar("hbp_compliance", Hibernate.INTEGER)
                .addScalar("hbp_help_check", Hibernate.STRING).addScalar("hbp_durgs_obey", Hibernate.INTEGER)
                .addScalar("hbp_drugs_untoward", Hibernate.STRING).addScalar("hbp_referWhy", Hibernate.STRING)
                .addScalar("hbp_refer_org", Hibernate.STRING).addScalar("hbp_refer_obj", Hibernate.STRING)
                .addScalar("create_time", Hibernate.TIMESTAMP).addScalar("status", Hibernate.INTEGER)
                .addScalar("visit_no", Hibernate.STRING).addScalar("id_card", Hibernate.STRING)
                .addScalar("memberName", Hibernate.STRING).addScalar("doctorName", Hibernate.STRING)
                .setParameters(params.getVals(), params.getTypes()).list();
        Pressure preInfo = null;
        if (objsList != null && objsList.size() > 0)
        {
            preInfo = new Pressure();
            Object[] objs = objsList.get(0);
            int idx = 0;
            preInfo.setId((String) objs[idx]);
            idx++;
            preInfo.setCustId((String) objs[idx]);
            idx++;
            preInfo.setVisitId((String) objs[idx]);
            idx++;
            preInfo.setFlupDate((Date) objs[idx]);
            idx++;
            preInfo.setFlupType((Integer) objs[idx]);
            idx++;
            preInfo.setFlupRsult((Integer) objs[idx]);
            idx++;
            preInfo.setDoctorId((String) objs[idx]);
            idx++;
            preInfo.setNextFlupTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            preInfo.setHbpSymptom((String) objs[idx]);
            idx++;
            preInfo.setHbpSymptomOther((String) objs[idx]);
            idx++;
            preInfo.setHbpBps((Integer) objs[idx]);
            idx++;
            preInfo.setHbpBpd((Integer) objs[idx]);
            idx++;
            preInfo.setHbpWeight((Double) objs[idx]);
            idx++;
            preInfo.setHbpPhysique((Double) objs[idx]);
            idx++;
            preInfo.setHbpPulse((Integer) objs[idx]);
            idx++;
            preInfo.setHbpOther((String) objs[idx]);
            idx++;
            preInfo.setHbpSmoking((Integer) objs[idx]);
            idx++;
            preInfo.setHbpDrinking((Double) objs[idx]);
            idx++;
            preInfo.setHbpSalarium((Integer) objs[idx]);
            idx++;
            preInfo.setHbpPsycrecovery((Integer) objs[idx]);
            idx++;
            preInfo.setHbpCompliance((Integer) objs[idx]);
            idx++;
            preInfo.setHbpHelpCheck((String) objs[idx]);
            idx++;
            preInfo.setHbpDurgsObey((Integer) objs[idx]);
            idx++;
            preInfo.setHbpDrugsUntoward((String) objs[idx]);
            idx++;
            preInfo.setHbpReferWhy((String) objs[idx]);
            idx++;
            preInfo.setHbpReferOrg((String) objs[idx]);
            idx++;
            preInfo.setHbpReferObj((String) objs[idx]);
            idx++;
            preInfo.setCreateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            preInfo.setStatus((Integer) objs[idx]);
            idx++;
            preInfo.setVisitNo((String) objs[idx]);
            idx++;
            preInfo.setIdcard((String) objs[idx]);
            idx++;
            preInfo.setMemberName((String) objs[idx]);
            idx++;
            preInfo.setDoctorName((String) objs[idx]);
            idx++;
        }
        return preInfo;
    }

}
