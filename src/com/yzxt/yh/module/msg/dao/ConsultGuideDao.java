package com.yzxt.yh.module.msg.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.msg.bean.ConsultGuide;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.StringUtil;

public class ConsultGuideDao extends HibernateSupport<ConsultGuide> implements BaseDao<ConsultGuide>
{
    public String insert(ConsultGuide consultGuide) throws Exception
    {
        super.save(consultGuide);
        return consultGuide.getId();
    }

    public int update(ConsultGuide consultGuide) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" update msg_consult_guide t ");
        sql.append(" set t.guide_context = ?");
        sql.append(", t.state = ?, t.guide_time = ?");
        sql.append(" where t.id = ?");
        params.add(consultGuide.getGuideContext(), Hibernate.STRING).add(consultGuide.getState(), Hibernate.INTEGER)
                .add(consultGuide.getGuideTime(), Hibernate.TIMESTAMP).add(consultGuide.getId(), Hibernate.STRING);
        int c = super.getSession().createSQLQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
        return c;
    }

    @SuppressWarnings("unchecked")
    public PageModel<ConsultGuide> getList(Map<String, Object> filter, int page, int pageSize)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" from msg_consult_guide t ");
        sql.append(" inner join sys_user su on su.id = t.cust_id");
        sql.append(" inner join sys_user sur on sur.id = t.doctor_id");
        //查询条件
        String consultTitle = (String) filter.get("consultTitle");
        if (StringUtil.isNotEmpty(consultTitle))
        {
            sql.append(" and t.consult_title like ").append(params.addLikePart(consultTitle));
        }
        String consultContext = (String) filter.get("consultContext");
        if (StringUtil.isNotEmpty(consultContext))
        {
            sql.append(" and t.consult_context like ").append(params.addLikePart(consultContext));
        }
        String MemberName = (String) filter.get("MemberName");
        if (StringUtil.isNotEmpty(MemberName))
        {
            sql.append(" and su.name_ like ").append(params.addLikePart(MemberName));
        }
        String consultState = (String) filter.get("consultState");
        if (StringUtil.isNotEmpty(consultState))
        {
            Integer state = Integer.parseInt(consultState);
            sql.append(" and t.state = ? ");
            params.add(state, Hibernate.INTEGER);
        }
        User user = (User) filter.get("curOper");
        // 权限
        if (Constant.USER_TYPE_DOCTOR.equals(user.getType()) || Constant.USER_TYPE_ADMIN.equals(user.getType()))
        {
            // 暂查所有记录
            sql.append(" and t.doctor_id = '").append(user.getId()).append("'");
        }
        else
        {
            sql.append(" and t.cust_id = '").append(user.getId()).append("'");
        }
        // 总启启记录数
        StringBuilder totalCountsql = new StringBuilder();
        totalCountsql.append("select count(*) as c ").append(sql);
        Integer totalCount = (Integer) super.getSession().createSQLQuery(totalCountsql.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
        // 分页记录
        StringBuilder pSql = new StringBuilder();
        pSql.append("select t.id, t.cust_id, t.consult_title, t.consult_context, t.state");
        pSql.append(", t.doctor_id, t.guide_context, t.consult_time, t.guide_time ");
        pSql.append(", su.name_ as memberName, sur.name_ as doctorName ");
        pSql.append(sql).append(" order by t.consult_time desc limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("cust_id", Hibernate.STRING).addScalar("consult_title", Hibernate.STRING)
                .addScalar("consult_context", Hibernate.STRING).addScalar("state", Hibernate.INTEGER)
                .addScalar("doctor_id", Hibernate.STRING).addScalar("guide_context", Hibernate.STRING)
                .addScalar("consult_time", Hibernate.TIMESTAMP).addScalar("guide_time", Hibernate.TIMESTAMP)
                .addScalar("memberName", Hibernate.STRING).addScalar("doctorName", Hibernate.STRING)
                .setParameters(params.getVals(), params.getTypes()).list();
        List<ConsultGuide> list = new ArrayList<ConsultGuide>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            ConsultGuide consultGuide = new ConsultGuide();
            idx = 0;
            consultGuide.setId((String) objs[idx]);
            idx++;
            consultGuide.setCustId((String) objs[idx]);
            idx++;
            consultGuide.setConsultTitle((String) objs[idx]);
            idx++;
            consultGuide.setConsultContext((String) objs[idx]);
            idx++;
            consultGuide.setState((Integer) objs[idx]);
            idx++;
            consultGuide.setDoctorId((String) objs[idx]);
            idx++;
            consultGuide.setGuideContext((String) objs[idx]);
            idx++;
            consultGuide.setConsultTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            consultGuide.setGuideTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            consultGuide.setMemberName((String) objs[idx]);
            idx++;
            consultGuide.setDoctorName((String) objs[idx]);
            idx++;
            list.add(consultGuide);
        }
        PageModel<ConsultGuide> pageModel = new PageModel<ConsultGuide>();
        pageModel.setTotal(totalCount);
        pageModel.setData(list);
        return pageModel;
    }

    @SuppressWarnings("unchecked")
    public ConsultGuide getDetail(String id) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" from msg_consult_guide t ");
        sql.append(" inner join sys_user su on su.id = t.cust_id");
        sql.append(" inner join sys_user sur on sur.id = t.doctor_id");
        sql.append(" where 1 = 1 and t.id = ?");
        params.add(id, Hibernate.STRING);
        StringBuilder pSql = new StringBuilder();
        pSql.append("select t.id, t.cust_id, t.consult_title, t.consult_context, t.state");
        pSql.append(", t.doctor_id, t.guide_context, t.consult_time, t.guide_time ");
        pSql.append(", su.name_ as memberName, sur.name_ as doctorName ");
        pSql.append(sql)/*.append(" order by t.state desc limit ?, ?")*/;
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("cust_id", Hibernate.STRING).addScalar("consult_title", Hibernate.STRING)
                .addScalar("consult_context", Hibernate.STRING).addScalar("state", Hibernate.INTEGER)
                .addScalar("doctor_id", Hibernate.STRING).addScalar("guide_context", Hibernate.STRING)
                .addScalar("consult_time", Hibernate.TIMESTAMP).addScalar("guide_time", Hibernate.TIMESTAMP)
                .addScalar("memberName", Hibernate.STRING).addScalar("doctorName", Hibernate.STRING)
                .setParameters(params.getVals(), params.getTypes()).list();
        ConsultGuide consultGuide = null;
        if (objsList != null && objsList.size() > 0)
        {
            consultGuide = new ConsultGuide();
            Object[] objs = objsList.get(0);
            int idx = 0;
            consultGuide.setId((String) objs[idx]);
            idx++;
            consultGuide.setCustId((String) objs[idx]);
            idx++;
            consultGuide.setConsultTitle((String) objs[idx]);
            idx++;
            consultGuide.setConsultContext((String) objs[idx]);
            idx++;
            consultGuide.setState((Integer) objs[idx]);
            idx++;
            consultGuide.setDoctorId((String) objs[idx]);
            idx++;
            consultGuide.setGuideContext((String) objs[idx]);
            idx++;
            consultGuide.setConsultTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            consultGuide.setGuideTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            consultGuide.setMemberName((String) objs[idx]);
            idx++;
            consultGuide.setDoctorName((String) objs[idx]);
            idx++;
        }
        return consultGuide;
    }
}
