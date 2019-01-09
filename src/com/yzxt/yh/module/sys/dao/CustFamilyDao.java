package com.yzxt.yh.module.sys.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.module.sys.bean.CustFamily;
import com.yzxt.yh.module.sys.bean.CustFamilyAudit;
import com.yzxt.yh.module.sys.bean.Customer;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.StringUtil;

public class CustFamilyDao extends HibernateSupport<CustFamily> implements BaseDao<CustFamily>
{

    /**
     * 添加家庭关系
     * @author h
     * @return id
     * @param custFamily
     * */
    public String insert(CustFamily custFamily) throws Exception
    {
        super.insert(custFamily);
        return custFamily.getId();
    }
    
    /**
     * 修改家庭成员数据
     * @author h
     * @return 
     * 2015.6.18
     * */
    public int updateMemInfo(CustFamily custF)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" update CustFamily t set");
        sql.append(" t.memo = ?");
        sql.append(" where t.custId = ? and t.memberId = ?");
        params.add(custF.getMemo(), Hibernate.STRING);
        params.add(custF.getCustId(), Hibernate.STRING);
        params.add(custF.getMemberId(), Hibernate.STRING);
        return super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
    }
    /**
     * 获取家庭成员数据接口
     * @author h
     * @return 
     * 2015.6.18
     * */
    @SuppressWarnings("unchecked")
    public List<CustFamily> getFamily(Map<String, Object> filter)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" from sys_cust_family scf ");
        sql.append(" inner join sys_user tu on tu.id = scf.member_id");
        sql.append(" left join sys_file_desc sfd on sfd.id = tu.img_file_id");
        sql.append(" where 1=1 ");
        String userId = (String) filter.get("userId");
        sql.append(" and scf.cust_id = ?");
        params.add(userId, Hibernate.STRING);
        StringBuilder mSql = new StringBuilder();
        mSql.append(" select scf.id, scf.cust_id, scf.member_id,scf.memo, ");
        mSql.append(" tu.name_, tu.phone, sfd.path ").append(sql);
        List<Object[]> objsList = super.getSession().createSQLQuery(mSql.toString())
                .addScalar("id", Hibernate.STRING).addScalar("cust_id", Hibernate.STRING)
                .addScalar("member_id", Hibernate.STRING)
                .addScalar("memo", Hibernate.STRING)
                .addScalar("name_", Hibernate.STRING)
                .addScalar("phone", Hibernate.STRING)
                .addScalar("path", Hibernate.STRING)
                .setParameters(params.getVals(), params.getTypes())
                .list();
        List<CustFamily> list = new ArrayList<CustFamily>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            CustFamily custF = new CustFamily();
            idx = 0;
            custF.setId((String) objs[idx]);
            idx++;
            custF.setCustId((String) objs[idx]);
            idx++;
            custF.setMemberId((String) objs[idx]);
            idx++;
            custF.setMemo((String) objs[idx]);
            idx++;
            custF.setApplyName((String) objs[idx]);
            idx++;
            custF.setPhone((String) objs[idx]);
            idx++;
            custF.setPath((String) objs[idx]);
            idx++;
            list.add(custF);
        }
        return list;
    }
    
    /**
     * 删除家庭成员数据
     * @author h
     * @return 
     * 2015.6.18
     * */
    public void deleteMem(String id) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("delete from CustFamily t where id =  ? ");
        params.add(id, Hibernate.STRING);
        super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
    }

    @SuppressWarnings("unchecked")
    public CustFamily getCustFamily(Map<String, Object> filter)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id, t.cust_id, t.member_id, t.memo from sys_cust_family t where 1 = 1");
        sql.append(" and t.cust_id = ? and t.member_id = ? ");
        String custId = (String) filter.get("custId");
        String memberId = (String) filter.get("memberId");
        params.add(custId, Hibernate.STRING);
        params.add(memberId, Hibernate.STRING);
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString())
                .addScalar("id", Hibernate.STRING).addScalar("cust_id", Hibernate.STRING)
                .addScalar("member_id", Hibernate.STRING).addScalar("memo", Hibernate.STRING)
                .setParameters(params.getVals(), params.getTypes()).list();
        CustFamily custF = null;
        if (objsList != null && objsList.size() > 0)
        {
            custF = new CustFamily();
            Object[] objs = objsList.get(0);
            int idx = 0;
            custF.setId((String) objs[idx]);
            idx++;
            custF.setCustId((String) objs[idx]);
            idx++;
            custF.setMemberId((String) objs[idx]);
            idx++;
            custF.setMemo((String) objs[idx]);
            idx++;
        }
        return custF;
    }

    /**
     * 平台获取家庭列表
     * @param filter
     * @author h
     * @param j 
     * @param i 
     * @return
     * 2015.7.8
     * */
    @SuppressWarnings("unchecked")
    public PageModel<CustFamily> queryFamily(Map<String, Object> filter, int page, int pageSize)
    {
        PageModel<CustFamily> pageModel = new PageModel<CustFamily>();
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" from sys_cust_family t ");
        sql.append(" inner join sys_user su on su.id = t.member_id ");
        sql.append(" inner join sys_customer sc on sc.user_id = t.member_id");
        sql.append(" where 1 = 1");
        //查询条件
        String userId = (String)filter.get("userId");
        if (StringUtil.isNotEmpty(userId))
        {
            sql.append(" and t.cust_id = ? ");
            params.add(userId, Hibernate.STRING);
        }
        // 总记录数
        StringBuilder totalCountSql = new StringBuilder();
        totalCountSql.append("select count(t.id) as c ").append(sql);
        Integer total = (Integer) super.getSession().createSQLQuery(totalCountSql.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
        // 分页记录
        StringBuilder pSql = new StringBuilder();
        pSql.append("select t.id, t.cust_id, t.member_id, su.name_, su.id_card, su.phone,sc.sex,sc.birthday,t.memo");
        pSql.append(sql);
        pSql.append(" order by su.name_ desc limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString())
                .addScalar("id", Hibernate.STRING).addScalar("cust_id", Hibernate.STRING)
                .addScalar("member_id", Hibernate.STRING)
                .addScalar("name_", Hibernate.STRING).addScalar("id_card", Hibernate.STRING)
                .addScalar("phone", Hibernate.STRING)
                .addScalar("sex", Hibernate.INTEGER).addScalar("birthday", Hibernate.TIMESTAMP)
                .addScalar("memo", Hibernate.STRING)
                .setParameters(params.getVals(), params.getTypes()).list();
        List<CustFamily> list = new ArrayList<CustFamily>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            CustFamily custF = new CustFamily();
            idx = 0;
            custF.setId((String) objs[idx]);
            idx++;
            custF.setCustId((String) objs[idx]);
            idx++;
            custF.setMemberId((String) objs[idx]);
            idx++;
            custF.setApplyName((String) objs[idx]);
            idx++;
            custF.setIdCard((String) objs[idx]);
            idx++;
            custF.setPhone((String) objs[idx]);
            idx++;
            custF.setSex((Integer) objs[idx]);
            idx++;
            custF.setBirthday((Timestamp) objs[idx]);
            idx++;
            custF.setMemo((String) objs[idx]);
            idx++;
            list.add(custF);
        }
        pageModel.setTotal(total);
        pageModel.setData(list);
        return pageModel;
    }

    /**
     * 添加家庭关系
     * @author h
     * @return id
     * @param custFamily
     * */
    public CustFamily get(String id)
    {
        return super.get(id);
    }

	public CustFamily getFamilyUserId(String custId) {

        HibernateParams params = new HibernateParams();
        String hql = "select t from CustFamily t where t. memberId= ? ";
        params.add(custId, Hibernate.STRING);
        List<CustFamily> cust = super.getSession().createQuery(hql).setParameters(params.getVals(), params.getTypes())
                .list();
        return cust != null && !cust.isEmpty() ? cust.get(0) : null;

	}
	public CustFamilyAudit getFamilyUser(String custId) {

        HibernateParams params = new HibernateParams();
        String hql = "select t from CustFamilyAudit t where t. memberId= ? ";
        params.add(custId, Hibernate.STRING);
        List<CustFamilyAudit> cust = super.getSession().createQuery(hql).setParameters(params.getVals(), params.getTypes())
                .list();
        return cust != null && !cust.isEmpty() ? cust.get(0) : null;

	}
}
