package com.yzxt.yh.module.chr.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.fw.pager.PageTran;
import com.yzxt.yh.constant.ConstChr;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.chr.bean.Visit;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.StringUtil;

public class ChrVisitDao extends HibernateSupport<Visit> implements BaseDao<Visit>
{
    public String insert(Visit visit) throws Exception
    {
        super.save(visit);
        return visit.getId();
    }

    /**
     * 更新随访列表
     * */
    public int update(Visit visit) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" update chr_visit t");
        sql.append(" set t.update_by = ?, t.update_time= ?, t.ishandled = ?, t.actual_flup_time = ? ");
        sql.append(" where id = ?");
        params.add(visit.getUpdateBy(), Hibernate.STRING);
        params.add(visit.getUpdateTime(), Hibernate.TIMESTAMP);
        params.add(visit.getIshandled(), Hibernate.INTEGER);
        params.add(visit.getActualFlupTime(), Hibernate.TIMESTAMP);
        params.add(visit.getId(), Hibernate.STRING);
        int c = super.getSession().createSQLQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
        return c;
    }

    @SuppressWarnings("unchecked")
    public PageModel<Visit> getList(Map<String, Object> filter, int page, int pageSize)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" from chr_visit t ");
        sql.append(" inner join sys_user su on su.id = t.cust_id");
        sql.append(" inner join sys_user sur on sur.id = t.doctor_id");
        sql.append(" where 1 = 1");
        //查询条件
        String memberName = (String) filter.get("memberName");
        if (StringUtil.isNotEmpty(memberName))
        {
            sql.append(" and su.name_ like ").append(params.addLikePart(memberName));
        }
        String ishandled = (String) filter.get("ishandled");
        if (StringUtil.isNotEmpty(ishandled))
        {
            Integer ishandle = Integer.parseInt(ishandled);
            sql.append(" and t.ishandled = ? ");
            params.add(ishandle, Hibernate.INTEGER);
        }
        String custId = (String) filter.get("custId");
        if (StringUtil.isNotEmpty(custId))
        {
            sql.append(" and t.cust_id = '").append(custId).append("'");
        }
        // 权限
        User user = (User) filter.get("curOper");
        if (Constant.USER_TYPE_DOCTOR.equals(user.getType()) || Constant.USER_TYPE_ADMIN.equals(user.getType()))
        {
            // 暂查所有记录
            sql.append(" and t.doctor_id = '").append(user.getId()).append("'");
        }
        else
        {
            sql.append(" and t.cust_id = '").append(user.getId()).append("'");
            sql.append(" and t.ishandled = '").append(ConstChr.CHR_ISHANDLED_YES).append("'");
        }
        // 总记录数
        StringBuilder totalCountsql = new StringBuilder();
        totalCountsql.append("select count(*) as c ").append(sql);
        Integer totalCount = (Integer) super.getSession().createSQLQuery(totalCountsql.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
        // 分页记录
        StringBuilder pSql = new StringBuilder();
        pSql.append("select t.id, t.cust_id, t.doctor_id, t.type_, t.grade, t.plan_flup_time");
        pSql.append(", t.ishandled, t.actual_flup_time, t.flup_grade, t.create_by, t.create_time ");
        pSql.append(", su.name_ as memberName, sur.name_ as doctorName ");
        pSql.append(sql).append(" order by t.create_time desc limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("cust_id", Hibernate.STRING).addScalar("doctor_id", Hibernate.STRING)
                .addScalar("type_", Hibernate.INTEGER).addScalar("grade", Hibernate.INTEGER)
                .addScalar("plan_flup_time", Hibernate.TIMESTAMP).addScalar("ishandled", Hibernate.INTEGER)
                .addScalar("actual_flup_time", Hibernate.TIMESTAMP).addScalar("flup_grade", Hibernate.INTEGER)
                .addScalar("create_by", Hibernate.STRING).addScalar("create_time", Hibernate.TIMESTAMP)
                .addScalar("memberName", Hibernate.STRING).addScalar("doctorName", Hibernate.STRING)
                .setParameters(params.getVals(), params.getTypes()).list();
        List<Visit> list = new ArrayList<Visit>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            Visit visit = new Visit();
            idx = 0;
            visit.setId((String) objs[idx]);
            idx++;
            visit.setCustId((String) objs[idx]);
            idx++;
            visit.setDoctorId((String) objs[idx]);
            idx++;
            visit.setType((Integer) objs[idx]);
            idx++;
            visit.setGrade((Integer) objs[idx]);
            idx++;
            visit.setPlanFlupTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            visit.setIshandled((Integer) objs[idx]);
            idx++;
            visit.setActualFlupTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            visit.setFlupGrade((Integer) objs[idx]);
            idx++;
            visit.setCreateBy((String) objs[idx]);
            idx++;
            visit.setCreateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            visit.setMemberName((String) objs[idx]);
            idx++;
            visit.setDoctorName((String) objs[idx]);
            idx++;
            list.add(visit);
        }
        PageModel<Visit> pageModel = new PageModel<Visit>();
        pageModel.setTotal(totalCount);
        pageModel.setData(list);
        return pageModel;
    }

    public Visit getVisitById(String id) throws Exception
    {
        return super.get(id);
    }

    /**
     * 查看随访记录人员列表
    */
    @SuppressWarnings("unchecked")
    public PageTran<Visit> queryChrVisitTran(Map<String, Object> filter, Timestamp synTime, int dir, int count)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select tu.name_, tu.id_card, t.id, t.cust_id,  t.type_, t.grade, t.create_time");
        sql.append(", (select MIN(cv.plan_flup_time) from chr_visit cv where  cv.ishandled = '1' and cv.type_=t.type_  and cv.cust_id = t.cust_id) next_flup_time");
        sql.append(", (select MAX(cv.actual_flup_time) from chr_visit cv where  cv.ishandled = '2' and cv.type_=t.type_  and cv.cust_id = t.cust_id) last_flup_time");
        //上次随访分级
        /* sql.append(", (select cv2.grade from chr_visit cv2 where cv2.actual_flup_time = (select MAX(cv.actual_flup_time) from chr_visit cv where  cv.ishandled = '2' and cv.type_=cm.type_  and cv.cust_id = cm.cust_id)) pre_flup_grade");*/
        sql.append(" from chr_visit t");
        sql.append(" inner join sys_user tu on tu.id = t.cust_id");
        sql.append(" where 1=1 and t.ishandled = ?");
        params.add(ConstChr.CHR_ISHANDLED_YES, Hibernate.INTEGER);
        // 查询条件
        // 权限
        String userId = (String) filter.get("userId");
        sql.append(" and exists(select smi.id from svb_member_info smi where smi.cust_id = t.cust_id and smi.doctor_id = ? and smi.state = ?)");
        params.add(userId, Hibernate.STRING);
        params.add(Constant.CUSTOMER_MEMBER_STATUS_YES, Hibernate.INTEGER);
        sql.append(" group by t.cust_id,t.type_");
        // 同步方式
        if (synTime == null)
        {
            sql.append(" order by t.create_time desc");
        }
        else if (dir < 0)
        {
            if (dir < 0)
            {
                sql.append(" and t.create_time < ? order by t.create_time desc");
            }
            else
            {
                sql.append(" and t.create_time > ? order by t.create_time asc");
            }
            params.add(synTime, Hibernate.TIMESTAMP);
        }
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString())
                .addScalar("name_", Hibernate.STRING).addScalar("id_card", Hibernate.STRING)
                .addScalar("id", Hibernate.STRING).addScalar("cust_id", Hibernate.STRING)
                .addScalar("type_", Hibernate.INTEGER).addScalar("grade", Hibernate.INTEGER)
                .addScalar("create_time", Hibernate.TIMESTAMP).addScalar("next_flup_time", Hibernate.TIMESTAMP)
                .addScalar("last_flup_time", Hibernate.TIMESTAMP).setParameters(params.getVals(), params.getTypes())
                .setMaxResults(count).list();
        List<Visit> list = new ArrayList<Visit>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            Visit v = new Visit();
            idx = 0;
            v.setMemberName((String) objs[idx]);
            idx++;
            v.setIdCard((String) objs[idx]);
            idx++;
            v.setId((String) objs[idx]);
            idx++;
            v.setCustId((String) objs[idx]);
            idx++;
            v.setType((Integer) objs[idx]);
            idx++;
            v.setGrade((Integer) objs[idx]);
            idx++;
            v.setCreateTime((Timestamp) objs[idx]);
            idx++;
            v.setNextFlupTime((Timestamp) objs[idx]);
            idx++;
            v.setLastFlupTime((Timestamp) objs[idx]);
            idx++;
            list.add(v);
        }
        return new PageTran<Visit>(list);
    }

    /*
     * 个人具体管理项目的随访列表
     * 个人血压随访或是血糖随访列表
     * */
    @SuppressWarnings("unchecked")
    public PageTran<Visit> queryVisitPerTran(Map<String, Object> filter, Timestamp synTime, int dir, int count)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select tu.name_, tu.id_card, t.id, t.cust_id,t.doctor_id, t.visit_no, t.type_, t.grade");
        sql.append(", t.plan_flup_time, t.ishandled, t.actual_flup_time, t.flup_grade, t.create_time");
        sql.append(",sur.name_ as doctorName");
        sql.append(" from chr_visit t");
        sql.append(" inner join sys_user tu on tu.id = t.cust_id");
        sql.append(" inner join sys_user sur on sur.id = t.doctor_id");
        // 查询条件
        sql.append(" where 1=1 and t.cust_id = ? ");

        // 权限
        String userId = (String) filter.get("userId");
        params.add(userId, Hibernate.STRING);
        String type = (String) filter.get("visitType");
        if (!type.equals(""))
        {
            sql.append("  and t.type_ = ?");
            params.add(Integer.parseInt(type), Hibernate.INTEGER);
        }
        sql.append(" and t.ishandled = ?");
        params.add(ConstChr.CHR_ISHANDLED_YES, Hibernate.INTEGER);
        // 同步方式
        if (synTime == null)
        {
            sql.append(" order by t.create_time desc");
        }
        else if (dir < 0)
        {
            if (dir < 0)
            {
                sql.append(" and t.create_time < ? order by t.create_time desc");
            }
            else
            {
                sql.append(" and t.create_time > ? order by t.create_time asc");
            }
            params.add(synTime, Hibernate.TIMESTAMP);
        }
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString())
                .addScalar("name_", Hibernate.STRING).addScalar("id_card", Hibernate.STRING)
                .addScalar("id", Hibernate.STRING).addScalar("cust_id", Hibernate.STRING)
                .addScalar("doctor_id", Hibernate.STRING).addScalar("visit_no", Hibernate.STRING)
                .addScalar("type_", Hibernate.INTEGER).addScalar("grade", Hibernate.INTEGER)
                .addScalar("plan_flup_time", Hibernate.TIMESTAMP).addScalar("ishandled", Hibernate.INTEGER)
                .addScalar("actual_flup_time", Hibernate.TIMESTAMP).addScalar("flup_grade", Hibernate.INTEGER)
                .addScalar("create_time", Hibernate.TIMESTAMP).addScalar("doctorName", Hibernate.STRING)
                .setParameters(params.getVals(), params.getTypes()).setMaxResults(count).list();
        List<Visit> list = new ArrayList<Visit>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            Visit visit = new Visit();
            idx = 0;
            visit.setMemberName((String) objs[idx]);
            idx++;
            visit.setIdCard((String) objs[idx]);
            idx++;
            visit.setId((String) objs[idx]);
            idx++;
            visit.setCustId((String) objs[idx]);
            idx++;
            visit.setDoctorId((String) objs[idx]);
            idx++;
            visit.setVisitNo((String) objs[idx]);
            idx++;
            visit.setType((Integer) objs[idx]);
            idx++;
            visit.setGrade((Integer) objs[idx]);
            idx++;
            visit.setPlanFlupTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            visit.setIshandled((Integer) objs[idx]);
            idx++;
            visit.setActualFlupTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            visit.setFlupGrade((Integer) objs[idx]);
            idx++;
            visit.setCreateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            visit.setDoctorName((String) objs[idx]);
            idx++;
            list.add(visit);
        }
        return new PageTran<Visit>(list);
    }

    /**
     * 医生得到会员未随访的合成列表
     * */
    @SuppressWarnings("unchecked")
    public PageTran<Visit> queryChrNOVisitTran(Map<String, Object> filter)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select tu.name_, tu.id_card, t.id, t.cust_id,  t.type_, t.grade, t.create_time");
        sql.append(", t.ishandled, t.visit_no");
        sql.append(", (select MIN(cv.plan_flup_time) from chr_visit cv where  cv.ishandled = '1' and cv.type_=t.type_  and cv.cust_id = t.cust_id) next_flup_time");
        sql.append(", (select MAX(cv.actual_flup_time) from chr_visit cv where  cv.ishandled = '2' and cv.type_=t.type_  and cv.cust_id = t.cust_id) last_flup_time");
        //上次随访分级
        /* sql.append(", (select cv2.grade from chr_visit cv2 where cv2.actual_flup_time = (select MAX(cv.actual_flup_time) from chr_visit cv where  cv.ishandled = '2' and cv.type_=cm.type_  and cv.cust_id = cm.cust_id)) pre_flup_grade");*/
        sql.append(" from chr_visit t");
        sql.append(" inner join sys_user tu on tu.id = t.cust_id");
        sql.append(" where 1=1 and t.ishandled = ?");
        params.add(ConstChr.CHR_ISHANDLED_NOT, Hibernate.INTEGER);
        // 查询条件
        String keyword = (String) filter.get("keyword");
        if (StringUtil.isNotEmpty(keyword))
        {
            sql.append(" and tu.name_ = ? ");
            params.add(keyword, Hibernate.STRING);
        }
        Integer type = (Integer) filter.get("type");
        if (type != null)
        {
            sql.append(" and t.type_ = ? ");
            params.add(type, Hibernate.INTEGER);
        }
        // 权限
        String userId = (String) filter.get("userId");
        sql.append(" and exists(select smi.id from svb_member_info smi where smi.cust_id = t.cust_id and smi.doctor_id = ? and smi.state = ?)");
        params.add(userId, Hibernate.STRING);
        params.add(Constant.CUSTOMER_MEMBER_STATUS_YES, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString())
                .addScalar("name_", Hibernate.STRING).addScalar("id_card", Hibernate.STRING)
                .addScalar("id", Hibernate.STRING).addScalar("cust_id", Hibernate.STRING)
                .addScalar("type_", Hibernate.INTEGER).addScalar("grade", Hibernate.INTEGER)
                .addScalar("create_time", Hibernate.TIMESTAMP).addScalar("ishandled", Hibernate.INTEGER)
                .addScalar("visit_no", Hibernate.STRING).addScalar("next_flup_time", Hibernate.TIMESTAMP)
                .addScalar("last_flup_time", Hibernate.TIMESTAMP).setParameters(params.getVals(), params.getTypes())
                .list();
        List<Visit> list = new ArrayList<Visit>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            Visit v = new Visit();
            idx = 0;
            v.setMemberName((String) objs[idx]);
            idx++;
            v.setIdCard((String) objs[idx]);
            idx++;
            v.setId((String) objs[idx]);
            idx++;
            v.setCustId((String) objs[idx]);
            idx++;
            v.setType((Integer) objs[idx]);
            idx++;
            v.setGrade((Integer) objs[idx]);
            idx++;
            v.setCreateTime((Timestamp) objs[idx]);
            idx++;
            v.setIshandled((Integer) objs[idx]);
            idx++;
            v.setVisitNo((String) objs[idx]);
            idx++;
            v.setNextFlupTime((Timestamp) objs[idx]);
            idx++;
            v.setLastFlupTime((Timestamp) objs[idx]);
            idx++;
            list.add(v);
        }
        return new PageTran<Visit>(list);
    }

    public int updatePlan(Visit visit)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" update chr_visit t");
        sql.append(" set t.update_by = ?, t.update_time= ?, t.plan_flup_time = ? ");
        sql.append(" where id = ?");
        params.add(visit.getUpdateBy(), Hibernate.STRING);
        params.add(visit.getUpdateTime(), Hibernate.TIMESTAMP);
        params.add(visit.getPlanFlupTime(), Hibernate.TIMESTAMP);
        params.add(visit.getId(), Hibernate.STRING);
        int c = super.getSession().createSQLQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
        return c;
    }
}
