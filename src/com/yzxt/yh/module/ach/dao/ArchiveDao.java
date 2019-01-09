package com.yzxt.yh.module.ach.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.ach.bean.Archive;
import com.yzxt.yh.module.sys.bean.Customer;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.StringUtil;

public class ArchiveDao extends HibernateSupport<Archive> implements BaseDao<Archive>
{

    /**
     * 加载档案信息
     */
    public Archive load(String id) throws Exception
    {
        return super.get(id);
    }

    /**
     * 查询用户档案列表
     * @param filter
     * @param page
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    public PageModel<Customer> query(Map<String, Object> filter, int page, int pageSize)
    {
        PageModel<Customer> pageModel = new PageModel<Customer>();
        HibernateParams params = new HibernateParams();
        StringBuilder mSql = new StringBuilder();
        mSql.append(" from sys_customer t");
        mSql.append(" inner join sys_user tu on tu.id = t.user_id");
        mSql.append(" left join sys_org tor on tor.id = tu.org_id");
        mSql.append(" where 1=1");
        // 查询条件
        String name = (String) filter.get("name");
        if (StringUtil.isNotEmpty(name))
        {
            mSql.append(" and tu.name_ like ").append(params.addLikePart(name));
        }
        String idCard = (String) filter.get("idCard");
        if (StringUtil.isNotEmpty(idCard))
        {
            mSql.append(" and tu.id_card like ").append(params.addLikePart(idCard));
        }
        Integer healthyStatus = (Integer) filter.get("healthyStatus");
        if (healthyStatus != null)
        {
            mSql.append(" and t.healthy_status = ?");
            params.add(healthyStatus, Hibernate.INTEGER);
        }
        //权限控制
        // 操作人
        User user = (User) filter.get("operUser");
        // 权限
        if (Constant.USER_TYPE_DOCTOR.equals(user.getType()))
        // 医生查询自己的会员记录
        {
            mSql.append(" and exists(select smi.id from svb_member_info smi where smi.cust_id = t.user_id and smi.doctor_id = ? and smi.state = ?)");
            params.add(user.getId(), Hibernate.STRING);
            params.add(Constant.CUSTOMER_MEMBER_STATUS_YES, Hibernate.INTEGER);
        }
        else if (Constant.USER_TYPE_CUSTOMER.equals(user.getType()))
        // 普通客户查询自己的记录
        {
            mSql.append(" and t.cust_id = ?");
            params.add(user.getId(), Hibernate.STRING);
        }
        else
        // 其它非管理员用户不能查询到记录
        {
            return pageModel;
        }
        // 总记录数
        StringBuilder totalCountSql = new StringBuilder();
        totalCountSql.append("select count(t.user_id) as c ").append(mSql);
        Integer total = (Integer) super.getSession().createSQLQuery(totalCountSql.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
        // 分页记录
        StringBuilder pSql = new StringBuilder();
        pSql.append("select t.user_id, t.sex, t.birthday, t.member_id, t.healthy_status");
        pSql.append(", tu.name_, tu.id_card, tu.phone, tor.orgName as org_name, '' as member_id");
        pSql.append(", (select max(smi.start_day) from svb_member_info smi where smi.cust_id = t.user_id and smi.state = ");
        pSql.append(Constant.CUSTOMER_MEMBER_STATUS_YES).append(") as start_day");
        pSql.append(", (select max(smi.end_day) from svb_member_info smi where smi.cust_id = t.user_id and smi.state = ");
        pSql.append(Constant.CUSTOMER_MEMBER_STATUS_YES).append(") as end_day");
        pSql.append(mSql);
        pSql.append(" order by t.update_time desc limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString())
                .addScalar("user_id", Hibernate.STRING).addScalar("sex", Hibernate.INTEGER)
                .addScalar("birthday", Hibernate.DATE).addScalar("member_id", Hibernate.STRING)
                .addScalar("healthy_status", Hibernate.INTEGER).addScalar("name_", Hibernate.STRING)
                .addScalar("id_card", Hibernate.STRING).addScalar("phone", Hibernate.STRING)
                .addScalar("org_name", Hibernate.STRING).addScalar("member_id", Hibernate.STRING)
                .addScalar("start_day", Hibernate.DATE).addScalar("end_day", Hibernate.DATE)
                .setParameters(params.getVals(), params.getTypes()).list();
        List<Customer> list = new ArrayList<Customer>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            Customer cust = new Customer();
            idx = 0;
            cust.setUserId((String) objs[idx]);
            idx++;
            cust.setSex((Integer) objs[idx]);
            idx++;
            cust.setBirthday((Date) objs[idx]);
            idx++;
            cust.setMemberId((String) objs[idx]);
            idx++;
            cust.setHealthyStatus((Integer) objs[idx]);
            idx++;
            cust.setName((String) objs[idx]);
            idx++;
            cust.setIdCard((String) objs[idx]);
            idx++;
            cust.setPhone((String) objs[idx]);
            idx++;
            cust.setOrgName((String) objs[idx]);
            idx++;
            cust.setMemberId((String) objs[idx]);
            idx++;
            cust.setStartDay((Date) objs[idx]);
            idx++;
            cust.setEndDay((Date) objs[idx]);
            idx++;
            list.add(cust);
        }
        pageModel.setTotal(total);
        pageModel.setData(list);
        return pageModel;
    }

    /**
     * 判断档案是否存在
     */
    public boolean isExist(String id) throws Exception
    {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(t.cust_id) as c from ach_archive t where t.cust_id = ?");
        int c = (Integer) super.getSession().createSQLQuery(sql.toString()).addScalar("c", Hibernate.INTEGER)
                .setParameter(0, id).uniqueResult();
        return c > 0;
    }

    /**
     * 更新档案
     */
    public int update(Archive archive) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update Archive t set");
        sql.append(" t.householdAddress = ?, t.streetName = ?, t.villageName = ?, t.medicalEstab = ?, t.telMedical = ?, t.updateTime = ?");
        sql.append(", t.workUnit = ?, t.contactName = ?, t.contactTelphone = ?");
        sql.append(", t.householdType = ?, t.national = ?, t.bloodType = ?, t.rhNagative = ?, t.degree = ?, t.maritalStatus = ?");
        sql.append(", t.payType = ?, t.otherPayType = ?, t.hoda = ?, t.otherHoda = ?, t.exposureHistory = ?, t.geneticHistory = ?");
        sql.append(", t.disabilityStatus = ?, t.otherDisName = ?");
        sql.append(" where t.custId = ?");
        params.add(archive.getHouseholdAddress(), Hibernate.STRING).add(archive.getStreetName(), Hibernate.STRING)
                .add(archive.getVillageName(), Hibernate.STRING).add(archive.getMedicalEstab(), Hibernate.STRING)
                .add(archive.getTelMedical(), Hibernate.STRING).add(archive.getUpdateTime(), Hibernate.TIMESTAMP)
                .add(archive.getWorkUnit(), Hibernate.STRING).add(archive.getContactName(), Hibernate.STRING)
                .add(archive.getContactTelphone(), Hibernate.STRING).add(archive.getHouseholdType(), Hibernate.INTEGER)
                .add(archive.getNational(), Hibernate.STRING).add(archive.getBloodType(), Hibernate.INTEGER)
                .add(archive.getRhNagative(), Hibernate.INTEGER).add(archive.getDegree(), Hibernate.INTEGER)
                .add(archive.getMaritalStatus(), Hibernate.INTEGER).add(archive.getPayType(), Hibernate.STRING)
                .add(archive.getOtherPayType(), Hibernate.STRING).add(archive.getHoda(), Hibernate.STRING)
                .add(archive.getOtherHoda(), Hibernate.STRING).add(archive.getExposureHistory(), Hibernate.STRING)
                .add(archive.getGeneticHistory(), Hibernate.STRING)
                .add(archive.getDisabilityStatus(), Hibernate.STRING).add(archive.getOtherDisName(), Hibernate.STRING)
                .add(archive.getCustId(), Hibernate.STRING);
        return super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
    }

}
