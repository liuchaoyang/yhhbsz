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
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.chr.bean.Manage;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.StringUtil;

public class ChrManageDao extends HibernateSupport<Manage> implements BaseDao<Manage>
{
    public String insert(Manage manage) throws Exception
    {
        String[] mngType = manage.getMngType();
        String custId = manage.getCustId();
        Integer grade = manage.getGrade();
        String createBy = manage.getCreateBy();
        Timestamp createTime = manage.getCreateTime();
        int num[] = new int[mngType.length];
        int count = 0;
        String str = "";
        for (int i = 0; i < num.length; i++)
        {
            num[i] = Integer.parseInt(mngType[i]);
            Manage mng = new Manage();
            mng.setCustId(custId);
            mng.setType(num[i]);
            mng.setGrade(grade);
            mng.setCreateBy(createBy);
            mng.setCreateTime(createTime);
            boolean isChr = isChr(mng);
            if (isChr && mng.getType() == 1)
            {
                str = "该客户血压已经纳入慢病.";
                return str;
            }
            else if (isChr && mng.getType() == 2)
            {
                str = "该客户血糖已经纳入慢病.";
                return str;
            }
            else
            {
                super.getHibernateTemplate().save(mng);
                String id = mng.getId();
                if (StringUtil.isNotEmpty(id))
                {
                    count++;
                }
                else
                {
                    count--;
                }
            }
        }
        if (count == num.length)
        {
            return Integer.toString(count);
        }
        else if (count == 0)
        {
            return str;
        }
        else
        {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public PageModel<Manage> getList(Map<String, Object> filter, int page, int pageSize)
    {
        PageModel<Manage> pageModel = new PageModel<Manage>();
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" from chr_manage cm");
        sql.append(" inner join sys_user su on su.id = cm.cust_id");
        sql.append(" where 1=1");
        //查询条件
        String MemberName = (String) filter.get("MemberName");
        if (StringUtil.isNotEmpty(MemberName))
        {
            sql.append(" and su.name_ like ").append(params.addLikePart(MemberName));
        }
        String manageType = (String) filter.get("manageType");
        if (StringUtil.isNotEmpty(manageType))
        {
            Integer type = Integer.parseInt(manageType);
            sql.append(" and cm.type_ = ? ");
            params.add(type, Hibernate.INTEGER);
        }
        User user = (User) filter.get("curOper");
        /// 权限
        if (Constant.USER_TYPE_DOCTOR.equals(user.getType()))
        // 医生查询自己的会员记录
        {
            sql.append(" and exists(select smi.id from svb_member_info smi where smi.cust_id = cm.cust_id and smi.doctor_id = ? and smi.state = 1)");
            params.add(user.getId(), Hibernate.STRING);
        }
        else if (Constant.USER_TYPE_CUSTOMER.equals(user.getType()))
        // 普通客户查询自己的记录
        {
            sql.append(" and t.cust_id = ?");
            params.add(user.getId(), Hibernate.STRING);
        }
        /*else if (Constant.USER_TYPE_ADMIN.equals(user.getType()))
            // 普通客户查询自己的记录
            {
               
            }*/
        else
        // 其它非管理员用户不能查询到记录
        {
            return pageModel;
        }
        // 总启启记录数
        StringBuilder totalCountsql = new StringBuilder();
        totalCountsql.append("select count(*) as c ").append(sql);
        Integer totalCount = (Integer) super.getSession().createSQLQuery(totalCountsql.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
        // 分页记录
        StringBuilder pSql = new StringBuilder();
        pSql.append("select cm.id, cm.cust_id, cm.type_, cm.grade");
        pSql.append(", cm.create_by, cm.create_time");
        pSql.append(", su.name_ as memberName ");
        pSql.append(", (select MIN(cv.plan_flup_time) from chr_visit cv where  cv.ishandled = '1' and cv.type_=cm.type_  and cv.cust_id = cm.cust_id) next_flup_time");
        pSql.append(", (select MAX(cv.actual_flup_time) from chr_visit cv where  cv.ishandled = '2' and cv.type_=cm.type_  and cv.cust_id = cm.cust_id) last_flup_time");
        pSql.append(sql).append("   order by cm.create_time desc limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("cust_id", Hibernate.STRING).addScalar("type_", Hibernate.INTEGER)
                .addScalar("grade", Hibernate.INTEGER).addScalar("create_by", Hibernate.STRING)
                .addScalar("create_time", Hibernate.TIMESTAMP).addScalar("memberName", Hibernate.STRING)
                .addScalar("next_flup_time", Hibernate.TIMESTAMP).addScalar("last_flup_time", Hibernate.TIMESTAMP)
                .setParameters(params.getVals(), params.getTypes()).list();
        List<Manage> list = new ArrayList<Manage>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            Manage chrManage = new Manage();
            idx = 0;
            chrManage.setId((String) objs[idx]);
            idx++;
            chrManage.setCustId((String) objs[idx]);
            idx++;
            chrManage.setType((Integer) objs[idx]);
            idx++;
            chrManage.setGrade((Integer) objs[idx]);
            idx++;
            chrManage.setCreateBy((String) objs[idx]);
            idx++;
            chrManage.setCreateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            chrManage.setMemberName((String) objs[idx]);
            idx++;
            chrManage.setNextFlupTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            chrManage.setLastFlupTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            list.add(chrManage);
        }

        pageModel.setTotal(totalCount);
        pageModel.setData(list);
        return pageModel;
    }

    /**
     * 查询慢病人员列表
     * */
    @SuppressWarnings("unchecked")
    public PageTran<Manage> queryChrTran(Map<String, Object> filter, Timestamp sysTime, int dir, int count)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select tu.id, tu.name_, tu.id_card, t.type_, t.grade, t.create_time");
        sql.append(", (select MIN(cv.plan_flup_time) from chr_visit cv where  cv.ishandled = '1' and cv.type_=t.type_  and cv.cust_id = t.cust_id) next_flup_time");
        sql.append(", (select MAX(cv.actual_flup_time) from chr_visit cv where  cv.ishandled = '2' and cv.type_=t.type_  and cv.cust_id = t.cust_id) last_flup_time");
        //上次随访分级
        /* sql.append(", (select cv2.grade from chr_visit cv2 where cv2.actual_flup_time = (select MAX(cv.actual_flup_time) from chr_visit cv where  cv.ishandled = '2' and cv.type_=cm.type_  and cv.cust_id = cm.cust_id)) pre_flup_grade");*/
        sql.append(" from chr_manage t");
        sql.append(" inner join sys_user tu on tu.id = t.cust_id");
        sql.append(" where 1=1 ");
        // 查询条件
        // 权限
        String userId = (String) filter.get("userId");
        sql.append(" and exists(select smi.id from svb_member_info smi where smi.cust_id = t.cust_id and smi.doctor_id = ? and smi.state = ?)");
        params.add(userId, Hibernate.STRING);
        params.add(Constant.CUSTOMER_MEMBER_STATUS_YES, Hibernate.INTEGER);
        // 同步方式
        if (sysTime == null)
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
            params.add(sysTime, Hibernate.TIMESTAMP);
        }
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("name_", Hibernate.STRING).addScalar("id_card", Hibernate.STRING)
                .addScalar("type_", Hibernate.INTEGER).addScalar("grade", Hibernate.INTEGER)
                .addScalar("create_time", Hibernate.TIMESTAMP).addScalar("next_flup_time", Hibernate.TIMESTAMP)
                .addScalar("last_flup_time", Hibernate.TIMESTAMP)
                /*.addScalar("pre_flup_grade", Hibernate.INTEGER)*/
                .setParameters(params.getVals(), params.getTypes()).setMaxResults(count).list();
        List<Manage> list = new ArrayList<Manage>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            Manage m = new Manage();
            idx = 0;
            m.setId((String) objs[idx]);
            idx++;
            m.setMemberName((String) objs[idx]);
            idx++;
            m.setIdCard((String) objs[idx]);
            idx++;
            m.setType((Integer) objs[idx]);
            idx++;
            m.setGrade((Integer) objs[idx]);
            idx++;
            m.setCreateTime((Timestamp) objs[idx]);
            idx++;
            m.setNextFlupTime((Timestamp) objs[idx]);
            idx++;
            m.setLastFlupTime((Timestamp) objs[idx]);
            idx++;
            /* m.setPreFlupGrade((Integer) objs[idx]);
             idx++;*/
            list.add(m);
        }
        return new PageTran<Manage>(list);
    }

    public boolean isChr(Manage chrManage)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select count(cm.id) as c from chr_manage cm");
        sql.append(" where 1=1");
        // 是否要排除指定CuatID
        if (StringUtil.isNotEmpty(chrManage.getCustId()))
        {
            sql.append(" and cm.cust_id = ?");
            params.add(chrManage.getCustId(), Hibernate.STRING);
        }
        // 成员慢病类型
        if (StringUtil.isNotEmpty(chrManage.getType().toString()))
        {
            sql.append(" and cm.type_ = ?");
            params.add(chrManage.getType(), Hibernate.INTEGER);
        }
        Integer c = (Integer) super.getSession().createSQLQuery(sql.toString()).addScalar("c", Hibernate.INTEGER)
                .setParameters(params.getVals(), params.getTypes()).uniqueResult();
        return c.intValue() > 0;
    }

}
