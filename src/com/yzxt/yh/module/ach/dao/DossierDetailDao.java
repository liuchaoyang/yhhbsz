package com.yzxt.yh.module.ach.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.yh.module.ach.bean.DossierDetail;

public class DossierDetailDao extends HibernateSupport<DossierDetail> implements BaseDao<DossierDetail>
{

    /**
     * 添加病历夹病历图片
     * @throws Exception
     */
    public String insert(DossierDetail dossierDetail) throws Exception
    {
        super.insert(dossierDetail);
        return dossierDetail.getId();
    }

    /**
     * 获取病历夹明细
     * @param dossierId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<DossierDetail> getByDossier(String dossierId)
    {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id, tfd.path");
        sql.append(" from ach_dossier_detail t");
        sql.append(" inner join sys_file_desc tfd on tfd.id = t.file_id");
        sql.append(" where t.dossier_id = ?");
        sql.append(" order by t.seq asc");
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("path", Hibernate.STRING).setString(0, dossierId).list();
        List<DossierDetail> list = new ArrayList<DossierDetail>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            DossierDetail dd = new DossierDetail();
            idx = 0;
            dd.setId((String) objs[idx]);
            idx++;
            dd.setPath((String) objs[idx]);
            idx++;
            list.add(dd);
        }
        return list;
    }

}
