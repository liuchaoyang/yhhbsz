package com.yzxt.yh.module.chk.dao;

import java.util.List;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.yh.module.chk.bean.ExamInoculate;

public class ExamInoculateDao extends HibernateSupport<ExamInoculate> implements BaseDao<ExamInoculate>
{

    /**
     * 获取体检预防接种信息
     * @param examId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<ExamInoculate> getByExam(String examId)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t from ExamInoculate t where t.examId = ? order by t.seq asc");
        params.add(examId, Hibernate.STRING);
        List<ExamInoculate> list = super.getSession().createQuery(sql.toString())
                .setParameters(params.getVals(), params.getTypes()).list();
        return list;
    }

    /**
     * 删除体检预防接种信息
     * @param examId
     * @return
     */
    public int deleteByExam(String examId)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("delete from ExamInoculate t where t.examId = ?");
        params.add(examId, Hibernate.STRING);
        int c = super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
        return c;
    }

}
