package com.yzxt.yh.module.chk.dao;

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
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.chk.bean.AnalysisUricAcid;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.StringUtil;

public class AnalysisUricAcidDao extends HibernateSupport<AnalysisUricAcid> implements BaseDao<AnalysisUricAcid>
{
    /**
     * 插入尿常规信息
     */
    public String insert(AnalysisUricAcid bean) throws Exception
    {
        super.save(bean);
        return bean.getId();
    }

    /**
     * 加载尿酸信息
     */
    public AnalysisUricAcid load(String custId) throws Exception
    {
        return super.get(custId);
    }

    /**
     * 获取客户最后做的尿常规数据
     * @param custId
     * @return
     */
    @SuppressWarnings("unchecked")
    public AnalysisUricAcid getLastestByCust(String custId)
    {
        StringBuilder sql = new StringBuilder();
        sql.append("select t from AnalysisUricAcid t where t.custId = ? order by t.checkTime desc");
        List<AnalysisUricAcid> list = super.getSession().createQuery(sql.toString()).setString(0, custId)
                .setString(0, custId).setFirstResult(0).setMaxResults(1).list();
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }

    /**
     * 查询尿常规列表
     * @param filter 过滤条件
     * @param sysTime 同步时间点
     * @param dir 同步方式，大于0：取时间点后的数据，小于0取时间点前的数据
     * @return
     */
    @SuppressWarnings("unchecked")
    public PageTran<AnalysisUricAcid> queryTran(Map<String, Object> filter, Timestamp sysTime, int dir, int count)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t from AnalysisUricAcid t where 1=1");
        // 查询条件
        String custId = (String) filter.get("custId");
        if (StringUtil.isNotEmpty(custId))
        {
            sql.append(" and t.custId = ?");
            params.add(custId, Hibernate.STRING);
        }
        else
        {
            return new PageTran<AnalysisUricAcid>();
        }
        // 同步方式
        if (sysTime == null)
        {
            sql.append(" order by t.createTime desc");
        }
        else if (dir < 0)
        {
            if (dir < 0)
            {
                sql.append(" and t.createTime < ? order by t.createTime desc");
            }
            else
            {
                sql.append(" and t.createTime > ? order by t.createTime asc");
            }
            params.add(sysTime, Hibernate.TIMESTAMP);
        }
        List<AnalysisUricAcid> list = super.getSession().createQuery(sql.toString())
                .setParameters(params.getVals(), params.getTypes()).setMaxResults(count).list();
        return new PageTran<AnalysisUricAcid>(list);
    }

    /**
     * 查询尿常规列表
     * @return
     */
    @SuppressWarnings("unchecked")
    public PageModel<AnalysisUricAcid> query(Map<String, Object> filter, int page, int pageSize)
    {
        PageModel<AnalysisUricAcid> pageModel = new PageModel<AnalysisUricAcid>();
        HibernateParams params = new HibernateParams();
        StringBuilder mSql = new StringBuilder();
        mSql.append(" from chk_analysis_uric_acid t");
        // mSql.append(" left join chk_check_warn tcw on tcw.id = t.id");
        mSql.append(" where 1=1");
        // 查询条件
        Date startDate = (Date) filter.get("startDate");
        if (startDate != null)
        {
            mSql.append(" and t.check_time >= ?");
            params.add(startDate, Hibernate.DATE);
        }
        Date endDate = (Date) filter.get("endDate");
        if (endDate != null)
        {
            mSql.append(" and t.check_time < ?");
            params.add(DateUtil.addDay(endDate, 1), Hibernate.DATE);
        }
        String custId = (String) filter.get("custId");
        if (StringUtil.isNotEmpty(custId))
        {
            mSql.append(" and t.cust_id = ?");
            params.add(custId, Hibernate.STRING);
        }
        else
        {
            // 操作人
            User user = (User) filter.get("operUser");
            // 权限
            if (Constant.USER_TYPE_CUSTOMER.equals(user.getType()))
            {
                mSql.append(" and t.cust_id = ?");
                params.add(user.getId(), Hibernate.STRING);
            }
            else if (Constant.USER_TYPE_DOCTOR.equals(user.getType()))
            // 医生查询自己的会员记录
            {
                mSql.append(" and exists(select smi.id from svb_member_info smi where smi.cust_id = t.cust_id and smi.doctor_id = ? and smi.state = 1)");
                params.add(user.getId(), Hibernate.STRING);
            }
            else
            // 其它非管理员用户不能查询到记录
            {
                return pageModel;
            }
        }
        // 总记录数
        StringBuilder totalCountSql = new StringBuilder();
        totalCountSql.append("select count(t.id) as c ").append(mSql);
        Integer total = (Integer) super.getSession().createSQLQuery(totalCountSql.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
        // 分页记录
        StringBuilder pSql = new StringBuilder();
        pSql.append("select t.id, t.leu, t.nit, t.pro, t.glu, t.ket, t.ubg, t.bil, t.ph");
        pSql.append(", t.sg, t.bld, t.vc, t.check_time");
        pSql.append(", t.level, t.descript");
        pSql.append(mSql);
        pSql.append(" order by t.check_time desc limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("leu", Hibernate.STRING).addScalar("nit", Hibernate.STRING)
                .addScalar("pro", Hibernate.STRING).addScalar("glu", Hibernate.STRING)
                .addScalar("ket", Hibernate.STRING).addScalar("ubg", Hibernate.STRING)
                .addScalar("bil", Hibernate.STRING).addScalar("ph", Hibernate.DOUBLE).addScalar("sg", Hibernate.DOUBLE)
                .addScalar("bld", Hibernate.STRING).addScalar("vc", Hibernate.STRING)
                .addScalar("check_time", Hibernate.TIMESTAMP).addScalar("level", Hibernate.INTEGER)
                .addScalar("descript", Hibernate.STRING).setParameters(params.getVals(), params.getTypes()).list();
        List<AnalysisUricAcid> list = new ArrayList<AnalysisUricAcid>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            AnalysisUricAcid au = new AnalysisUricAcid();
            idx = 0;
            au.setId((String) objs[idx]);
            idx++;
            au.setLeu((String) objs[idx]);
            idx++;
            au.setNit((String) objs[idx]);
            idx++;
            au.setPro((String) objs[idx]);
            idx++;
            au.setGlu((String) objs[idx]);
            idx++;
            au.setKet((String) objs[idx]);
            idx++;
            au.setUbg((String) objs[idx]);
            idx++;
            au.setBil((String) objs[idx]);
            idx++;
            au.setPh((Double) objs[idx]);
            idx++;
            au.setSg((Double) objs[idx]);
            idx++;
            au.setBld((String) objs[idx]);
            idx++;
            au.setVc((String) objs[idx]);
            idx++;
            au.setCheckTime((Timestamp) objs[idx]);
            idx++;
            au.setLevel((Integer) objs[idx]);
            idx++;
            au.setDescript((String) objs[idx]);
            idx++;
            list.add(au);
        }
        pageModel.setTotal(total);
        pageModel.setData(list);
        return pageModel;
    }

}
