package com.yzxt.yh.module.ach.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.fw.pager.PageTran;
import com.yzxt.yh.constant.ConstDict;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.ach.bean.Dossier;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.StringUtil;

public class DossierDao extends HibernateSupport<Dossier> implements BaseDao<Dossier>
{

    /**
     * 加载病历夹信息
     */
    @SuppressWarnings("unchecked")
    public Dossier load(String id) throws Exception
    {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id, t.cust_id, t.type_, t.memo, t.create_time");
        sql.append(", tu.name_ as cust_name, tdd.dict_detail_name as type_name");
        sql.append(" from ach_dossier t");
        sql.append(" inner join sys_user tu on tu.id = t.cust_id");
        sql.append(" inner join sys_dict_detail tdd on tdd.dict_detail_code = t.type_ and tdd.dict_code = '")
                .append(ConstDict.DOSSIER_TYPE).append("'");
        sql.append(" where t.id = ?");
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString())
        .addScalar("id", Hibernate.STRING).addScalar("cust_id", Hibernate.STRING).addScalar("type_", Hibernate.INTEGER)
                .addScalar("memo", Hibernate.STRING).addScalar("create_time", Hibernate.TIMESTAMP)
                .addScalar("cust_name", Hibernate.STRING).addScalar("type_name", Hibernate.STRING).setString(0, id)
                .list();
        Dossier dossier = null;
        if (objsList != null && !objsList.isEmpty())
        {
            dossier = new Dossier();
            Object[] objs = objsList.get(0);
            int idx = 0;
            dossier.setId((String) objs[idx]);
            idx++;
            dossier.setCustId((String) objs[idx]);
            idx++;
            dossier.setType((Integer) objs[idx]);
            idx++;
            dossier.setMemo((String) objs[idx]);
            idx++;
            dossier.setCreateTime((Timestamp) objs[idx]);
            idx++;
            dossier.setCustName((String) objs[idx]);
            idx++;
            dossier.setTypeName((String) objs[idx]);
            idx++;
        }
        return dossier;
    }

    /**
     * 添加病历夹
     * @throws Exception 
     */
    public String insert(Dossier dossier) throws Exception
    {
        super.insert(dossier);
        return dossier.getId();
    }

    /**
     * 分页查询病历夹数据
     * @param filter
     * @param page
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    public PageModel<Dossier> query(Map<String, Object> filter, int page, int pageSize)
    {
        PageModel<Dossier> pageModel = new PageModel<Dossier>();
        HibernateParams params = new HibernateParams();
        StringBuilder mSql = new StringBuilder();
        mSql.append(" from ach_dossier t");
        mSql.append(" inner join sys_dict_detail tdd on tdd.dict_detail_code = t.type_ and tdd.dict_code = '")
                .append(ConstDict.DOSSIER_TYPE).append("'");
        mSql.append(" where 1=1");
        User operUser = (User) filter.get("operUser");
        if (operUser == null)
        {
            return pageModel;
        }
        // 查询条件
        // 病历类型
        Integer type = (Integer) filter.get("type");
        if (type != null && type.intValue() > 0)
        {
            mSql.append(" and t.type_ = ?");
            params.add(type, Hibernate.INTEGER);
        }
        // 客户ID
        String custId = (String) filter.get("custId");
        // 客户查询是，未指定客户ID，默认查询自己的数据
        if (StringUtil.isEmpty(custId) && Constant.USER_TYPE_CUSTOMER.equals(operUser.getType()))
        {
            custId = operUser.getId();
        }
        if (StringUtil.isNotEmpty(custId))
        {
            mSql.append(" and t.cust_id = ?");
            params.add(custId, Hibernate.STRING);
        }
        // 创建时间
        Date startCreateDate = (Date) filter.get("startCreateDate");
        if (startCreateDate != null)
        {
            mSql.append(" and t.create_time >= ?");
            params.add(startCreateDate, Hibernate.DATE);
        }
        Date endCreateDate = (Date) filter.get("endCreateDate");
        if (endCreateDate != null)
        {
            mSql.append(" and t.create_time < ?");
            params.add(DateUtil.addDay(endCreateDate, 1), Hibernate.DATE);
        }
        // 权限判断
        // 医生查询同机构下用户数据
        if (Constant.USER_TYPE_DOCTOR.equals(operUser.getType()))
        {
            if (StringUtil.isNotEmpty(custId))
            {
                mSql.append(" and exists(select su.id from sys_user su where su.org_id = ? and su.id = ?)");
                params.add(operUser.getOrgId(), Hibernate.STRING);
                params.add(custId, Hibernate.STRING);
            }
            else
            {
                mSql.append(" and exists(select su.id from sys_user su where su.org_id = ? and su.id = t.cust_id)");
                params.add(operUser.getOrgId(), Hibernate.STRING);
            }
        }
        else if (Constant.USER_TYPE_CUSTOMER.equals(operUser.getType()))
        // 客户查询自己的或家庭成员的数据
        {
            if (!operUser.getId().equals(custId))
            {
                mSql.append(" and exists(select scf.id from sys_cust_family scf where scf.member_id = ? and scf.cust_id = ?)");
                params.add(custId, Hibernate.STRING);
                params.add(operUser.getId(), Hibernate.STRING);
            }
        }
        else
        {
            return pageModel;
        }
        // 总记录数
        StringBuilder totalCountSql = new StringBuilder();
        totalCountSql.append("select count(t.id) as c ").append(mSql);
        Integer total = (Integer) super.getSession().createSQLQuery(totalCountSql.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
        // 分页记录
        StringBuilder pSql = new StringBuilder();
        pSql.append("select t.id, t.type_, t.memo, t.create_time");
        pSql.append(", tdd.dict_detail_name as type_name");
        pSql.append(mSql);
        pSql.append(" order by t.create_time desc limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("type_", Hibernate.INTEGER).addScalar("memo", Hibernate.STRING)
                .addScalar("create_time", Hibernate.TIMESTAMP).addScalar("type_name", Hibernate.STRING)
                .setParameters(params.getVals(), params.getTypes()).list();
        List<Dossier> list = new ArrayList<Dossier>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            Dossier dossier = new Dossier();
            idx = 0;
            dossier.setId((String) objs[idx]);
            idx++;
            dossier.setType((Integer) objs[idx]);
            idx++;
            dossier.setMemo((String) objs[idx]);
            idx++;
            dossier.setCreateTime((Timestamp) objs[idx]);
            idx++;
            dossier.setTypeName((String) objs[idx]);
            idx++;
            list.add(dossier);
        }
        pageModel.setTotal(total);
        pageModel.setData(list);
        return pageModel;
    }

    /**
     * 客户端查询病历夹
     * @param filter
     * @param sysTime
     * @param count
     * @return
     */
    @SuppressWarnings("unchecked")
    public PageTran<Dossier> queryTran(Map<String, Object> filter, Timestamp sysTime, int count)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id, t.cust_id, t.type_, t.memo, t.create_by, t.create_time");
        sql.append(", tdd.dict_detail_name as type_name");
        sql.append(", (select group_concat(sfd.path order by st.seq) from ach_dossier_detail st");
        sql.append(" inner join sys_file_desc sfd on sfd.id = st.file_id where st.dossier_id = t.id) as paths");
        sql.append(" from ach_dossier t");
        sql.append(" inner join sys_dict_detail tdd on tdd.dict_detail_code = t.type_ and tdd.dict_code = '")
                .append(ConstDict.DOSSIER_TYPE).append("'");
        sql.append(" where 1=1");
        User operUser = (User) filter.get("operUser");
        if (operUser == null)
        {
            return new PageTran<Dossier>();
        }
        // 查询条件
        // 病历类型
        Integer type = (Integer) filter.get("type");
        if (type != null && type.intValue() > 0)
        {
            sql.append(" and t.type_ = ?");
            params.add(type, Hibernate.INTEGER);
        }
        // 客户ID
        String custId = (String) filter.get("custId");
        // 客户查询是，未指定客户ID，默认查询自己的数据
        if (StringUtil.isEmpty(custId) && Constant.USER_TYPE_CUSTOMER.equals(operUser.getType()))
        {
            custId = operUser.getId();
        }
        if (StringUtil.isNotEmpty(custId))
        {
            sql.append(" and t.cust_id = ?");
            params.add(custId, Hibernate.STRING);
        }
        // 权限判断
        // 医生查询同机构下用户数据
        if (Constant.USER_TYPE_DOCTOR.equals(operUser.getType()))
        {
            if (StringUtil.isNotEmpty(custId))
            {
                sql.append(" and exists(select su.id from sys_user su where su.org_id = ? and su.id = ?)");
                params.add(operUser.getOrgId(), Hibernate.STRING);
                params.add(custId, Hibernate.STRING);
            }
            else
            {
                sql.append(" and exists(select su.id from sys_user su where su.org_id = ? and su.id = t.cust_id)");
                params.add(operUser.getOrgId(), Hibernate.STRING);
            }
        }
        else if (Constant.USER_TYPE_CUSTOMER.equals(operUser.getType()))
        // 客户查询自己的或家庭成员的数据
        {
            if (!operUser.getId().equals(custId))
            {
                sql.append(" and exists(select scf.id from sys_cust_family scf where scf.member_id = ? and scf.cust_id = ?)");
                params.add(custId, Hibernate.STRING);
                params.add(operUser.getId(), Hibernate.STRING);
            }
        }
        else
        {
            return new PageTran<Dossier>();
        }
        // 同步方式
        if (sysTime == null)
        {
            sql.append(" order by t.create_time desc");
        }
        else
        {
            sql.append(" and t.create_time < ? order by t.create_time desc");
            params.add(sysTime, Hibernate.TIMESTAMP);
        }
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("cust_id", Hibernate.STRING).addScalar("type_", Hibernate.INTEGER)
                .addScalar("memo", Hibernate.STRING).addScalar("create_by", Hibernate.STRING)
                .addScalar("create_time", Hibernate.TIMESTAMP).addScalar("type_name", Hibernate.STRING)
                .addScalar("paths", Hibernate.STRING).setParameters(params.getVals(), params.getTypes()).list();
        List<Dossier> list = new ArrayList<Dossier>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            Dossier bean = new Dossier();
            idx = 0;
            bean.setId((String) objs[idx]);
            idx++;
            bean.setCustId((String) objs[idx]);
            idx++;
            bean.setType((Integer) objs[idx]);
            idx++;
            bean.setMemo((String) objs[idx]);
            idx++;
            bean.setCreateBy((String) objs[idx]);
            idx++;
            bean.setCreateTime((Timestamp) objs[idx]);
            idx++;
            bean.setTypeName((String) objs[idx]);
            idx++;
            bean.setPaths((String) objs[idx]);
            idx++;
            list.add(bean);
        }
        return new PageTran<Dossier>(list);
    }

}
