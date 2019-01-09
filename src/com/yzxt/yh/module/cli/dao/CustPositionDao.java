package com.yzxt.yh.module.cli.dao;

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
import com.yzxt.yh.module.cli.bean.CustPosition;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.StringUtil;

public class CustPositionDao extends HibernateSupport<CustPosition> implements BaseDao<CustPosition>
{
    /**
     * 插入客户位置信息
     */
    public String insert(CustPosition bean) throws Exception
    {
        super.save(bean);
        return bean.getId();
    }

    /**
     * 更新客户位置信息
     */
    public int update(CustPosition bean) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update CustPosition set deviceName = ?, deviceMac = ?, latitude = ?, longitude=?");
        sql.append(", speed = ?, direction = ?, altitude = ?, positionMode = ?, checkType = ?, checkTime = ?");
        sql.append(" where custId = ?");
        params.add(bean.getDeviceName(), Hibernate.STRING).add(bean.getDeviceMac(), Hibernate.STRING)
                .add(bean.getLatitude(), Hibernate.DOUBLE).add(bean.getLongitude(), Hibernate.DOUBLE)
                .add(bean.getSpeed(), Hibernate.DOUBLE).add(bean.getDirection(), Hibernate.INTEGER)
                .add(bean.getAltitude(), Hibernate.INTEGER).add(bean.getPositionMode(), Hibernate.STRING)
                .add(bean.getCheckType(), Hibernate.INTEGER).add(bean.getCheckTime(), Hibernate.TIMESTAMP)
                .add(bean.getCustId(), Hibernate.STRING);
        int c = super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
        return c;
    }

    /**
     * 判断客户是否保存过位置信息
     */
    public boolean getCustPostionExist(String custId) throws Exception
    {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(t.custId) as c from CustPosition t where t.custId = ?");
        Long c = (Long) super.getSession().createQuery(sql.toString()).setString(0, custId).uniqueResult();
        return c.longValue() > 0;
    }

    /**
     * 查询客户位置信息记录列表
     * @param filter
     * @return
     */
    @SuppressWarnings("unchecked")
    public PageModel<CustPosition> query(Map<String, Object> filter, int page, int pageSize)
    {
        PageModel<CustPosition> pageModel = new PageModel<CustPosition>();
        HibernateParams params = new HibernateParams();
        StringBuilder mSql = new StringBuilder();
        mSql.append(" from cli_cust_position t");
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
        String positionMode = (String) filter.get("positionMode");
        if (StringUtil.isNotEmpty(positionMode))
        {
            mSql.append(" and t.position_mode = ?");
            params.add(positionMode, Hibernate.STRING);
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
        pSql.append("select t.id, t.cust_id, t.latitude, t.longitude, t.speed, t.direction");
        pSql.append(", t.altitude, t.mnc, t.lac, t.cellid, t.position_mode, t.check_time");
        pSql.append(mSql);
        pSql.append(" order by t.check_time desc limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("cust_id", Hibernate.STRING).addScalar("latitude", Hibernate.DOUBLE)
                .addScalar("longitude", Hibernate.DOUBLE).addScalar("speed", Hibernate.DOUBLE)
                .addScalar("direction", Hibernate.INTEGER).addScalar("altitude", Hibernate.INTEGER)
                .addScalar("mnc", Hibernate.STRING).addScalar("lac", Hibernate.STRING)
                .addScalar("cellid", Hibernate.STRING).addScalar("position_mode", Hibernate.STRING)
                .addScalar("check_time", Hibernate.TIMESTAMP).setParameters(params.getVals(), params.getTypes()).list();
        List<CustPosition> list = new ArrayList<CustPosition>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            CustPosition bean = new CustPosition();
            idx = 0;
            bean.setId((String) objs[idx]);
            idx++;
            bean.setCustId((String) objs[idx]);
            idx++;
            bean.setLatitude((Double) objs[idx]);
            idx++;
            bean.setLongitude((Double) objs[idx]);
            idx++;
            bean.setSpeed((Double) objs[idx]);
            idx++;
            bean.setDirection((Integer) objs[idx]);
            idx++;
            bean.setAltitude((Integer) objs[idx]);
            idx++;
            bean.setMnc((String) objs[idx]);
            idx++;
            bean.setLac((String) objs[idx]);
            idx++;
            bean.setCellid((String) objs[idx]);
            idx++;
            bean.setPositionMode((String) objs[idx]);
            idx++;
            bean.setCheckTime((Timestamp) objs[idx]);
            idx++;
            list.add(bean);
        }
        pageModel.setTotal(total);
        pageModel.setData(list);
        return pageModel;
    }

    /**
     * 客户端查询位置列表
     * @param filter
     * @param sysTime
     * @param dir
     * @param count
     * @return
     * @author h
     * 2015.11.30
     */
    @SuppressWarnings("unchecked")
    public PageTran<CustPosition> queryTran(Map<String, Object> filter, Timestamp sysTime, int dir, int count)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id, t.cust_id, t.device_mac as deviceCode, t.latitude, t.longitude, t.speed");
        sql.append(", t.direction, t.altitude, t.mnc, t.lac, t.cellid, t.position_mode, t.check_type");
        sql.append(", t.check_time, t.create_time ");
        sql.append(" from cli_cust_position t");
        sql.append(" where 1 = 1 ");
        // 查询条件
        // 权限
        String custId = (String) filter.get("custId");
        sql.append(" and t.cust_id = ? ");
        params.add(custId, Hibernate.STRING);
        String deviceCode = (String) filter.get("deviceCode");
        sql.append(" and t.device_mac = ? ");
        params.add(deviceCode, Hibernate.STRING);
        String positionMode = (String) filter.get("positionMode");
        if (StringUtil.isNotEmpty(positionMode))
        {
            sql.append(" and t.position_mode = ? ");
            params.add(positionMode, Hibernate.STRING);
        }
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
                .addScalar("cust_id", Hibernate.STRING).addScalar("deviceCode", Hibernate.STRING)
                .addScalar("latitude", Hibernate.DOUBLE).addScalar("longitude", Hibernate.DOUBLE)
                .addScalar("speed", Hibernate.DOUBLE).addScalar("direction", Hibernate.INTEGER)
                .addScalar("altitude", Hibernate.INTEGER).addScalar("mnc", Hibernate.STRING)
                .addScalar("lac", Hibernate.STRING).addScalar("cellid", Hibernate.STRING)
                .addScalar("position_mode", Hibernate.STRING).addScalar("check_type", Hibernate.INTEGER)
                .addScalar("check_time", Hibernate.TIMESTAMP).addScalar("create_time", Hibernate.TIMESTAMP)
                .setParameters(params.getVals(), params.getTypes()).setMaxResults(count).list();
        List<CustPosition> list = new ArrayList<CustPosition>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            CustPosition bean = new CustPosition();
            idx = 0;
            bean.setId((String) objs[idx]);
            idx++;
            bean.setCustId((String) objs[idx]);
            idx++;
            bean.setDeviceCode((String) objs[idx]);
            idx++;
            bean.setLatitude((Double) objs[idx]);
            idx++;
            bean.setLongitude((Double) objs[idx]);
            idx++;
            bean.setSpeed((Double) objs[idx]);
            idx++;
            bean.setDirection((Integer) objs[idx]);
            idx++;
            bean.setAltitude((Integer) objs[idx]);
            idx++;
            bean.setMnc((String) objs[idx]);
            idx++;
            bean.setLac((String) objs[idx]);
            idx++;
            bean.setCellid((String) objs[idx]);
            idx++;
            bean.setPositionMode((String) objs[idx]);
            idx++;
            bean.setCheckType((Integer) objs[idx]);
            idx++;
            bean.setCheckTime((Timestamp) objs[idx]);
            idx++;
            bean.setCreateTime((Timestamp) objs[idx]);
            idx++;
            list.add(bean);
        }
        return new PageTran<CustPosition>(list);
    }

    /**
     * 查询客户最后一次的位置信息记录
     * @param filter
     * @return
     * @author h
     * 2015.11.30
     */
    @SuppressWarnings("unchecked")
    public CustPosition queryLastPostion(Map<String, Object> filter)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id, t.cust_id, t.device_name, t.device_mac, t.latitude, t.longitude, t.speed");
        sql.append(", t.direction, t.altitude, t.mnc, t.lac, t.cellid, t.position_mode, t.check_type");
        sql.append(", t.check_time, t.create_time ");
        sql.append(" from cli_cust_position t ");
        sql.append(" where 1= 1 and t.cust_id = ?  and t.device_mac = ?");
        params.add(filter.get("custId"), Hibernate.STRING);
        /*and t.device_name = ?
        params.add(filter.get("deviceId"), Hibernate.STRING);*/
        params.add(filter.get("deviceCode"), Hibernate.STRING);
        sql.append(" order by t.create_time desc ");
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("cust_id", Hibernate.STRING).addScalar("device_name", Hibernate.STRING)
                .addScalar("device_mac", Hibernate.STRING).addScalar("latitude", Hibernate.DOUBLE)
                .addScalar("longitude", Hibernate.DOUBLE).addScalar("speed", Hibernate.DOUBLE)
                .addScalar("direction", Hibernate.INTEGER).addScalar("altitude", Hibernate.INTEGER)
                .addScalar("mnc", Hibernate.STRING).addScalar("lac", Hibernate.STRING)
                .addScalar("cellid", Hibernate.STRING).addScalar("position_mode", Hibernate.STRING)
                .addScalar("check_type", Hibernate.INTEGER).addScalar("check_time", Hibernate.TIMESTAMP)
                .addScalar("create_time", Hibernate.TIMESTAMP).setParameters(params.getVals(), params.getTypes())
                .list();
        CustPosition bean = null;
        if (objsList != null && objsList.size() > 0)
        {
            Object[] objs = objsList.get(0);
            bean = new CustPosition();
            int idx = 0;
            bean.setId((String) objs[idx]);
            idx++;
            bean.setCustId((String) objs[idx]);
            idx++;
            bean.setDeviceName((String) objs[idx]);
            idx++;
            bean.setDeviceMac((String) objs[idx]);
            idx++;
            bean.setLatitude((Double) objs[idx]);
            idx++;
            bean.setLongitude((Double) objs[idx]);
            idx++;
            bean.setSpeed((Double) objs[idx]);
            idx++;
            bean.setDirection((Integer) objs[idx]);
            idx++;
            bean.setAltitude((Integer) objs[idx]);
            idx++;
            bean.setMnc((String) objs[idx]);
            idx++;
            bean.setLac((String) objs[idx]);
            idx++;
            bean.setCellid((String) objs[idx]);
            idx++;
            bean.setPositionMode((String) objs[idx]);
            idx++;
            bean.setCheckType((Integer) objs[idx]);
            idx++;
            bean.setCheckTime((Timestamp) objs[idx]);
            idx++;
            bean.setCreateTime((Timestamp) objs[idx]);
            idx++;
        }
        return bean;
    }

    /**
     * 查询客户当前位置信息记录
     * @param filter
     * @return
     * @author h
     * 2015.11.30
     */
    @SuppressWarnings("unchecked")
    public CustPosition queryCurrentPosition(Map<String, Object> filter)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id, t.cust_id, t.device_name, t.device_mac, t.latitude, t.longitude, t.speed");
        sql.append(", t.direction, t.altitude, t.mnc, t.lac, t.cellid, t.position_mode, t.check_type");
        sql.append(", t.check_time, t.create_time ");
        sql.append(" from cli_cust_position t ");
        sql.append(" where 1= 1 and t.cust_id = ? and t.device_mac = ?");
        params.add(filter.get("custId"), Hibernate.STRING);
        sql.append(" order by t.create_time desc ");
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("cust_id", Hibernate.STRING).addScalar("device_name", Hibernate.STRING)
                .addScalar("device_mac", Hibernate.STRING).addScalar("latitude", Hibernate.DOUBLE)
                .addScalar("longitude", Hibernate.DOUBLE).addScalar("speed", Hibernate.DOUBLE)
                .addScalar("direction", Hibernate.INTEGER).addScalar("altitude", Hibernate.INTEGER)
                .addScalar("mnc", Hibernate.STRING).addScalar("lac", Hibernate.STRING)
                .addScalar("cellid", Hibernate.STRING).addScalar("position_mode", Hibernate.STRING)
                .addScalar("check_type", Hibernate.INTEGER).addScalar("check_time", Hibernate.TIMESTAMP)
                .addScalar("create_time", Hibernate.TIMESTAMP).setParameters(params.getVals(), params.getTypes())
                .list();
        CustPosition bean = null;
        if (objsList != null && objsList.size() > 0)
        {
            Object[] objs = objsList.get(0);
            bean = new CustPosition();
            int idx = 0;
            bean.setId((String) objs[idx]);
            idx++;
            bean.setCustId((String) objs[idx]);
            idx++;
            bean.setDeviceName((String) objs[idx]);
            idx++;
            bean.setDeviceMac((String) objs[idx]);
            idx++;
            bean.setLatitude((Double) objs[idx]);
            idx++;
            bean.setLongitude((Double) objs[idx]);
            idx++;
            bean.setSpeed((Double) objs[idx]);
            idx++;
            bean.setDirection((Integer) objs[idx]);
            idx++;
            bean.setAltitude((Integer) objs[idx]);
            idx++;
            bean.setMnc((String) objs[idx]);
            idx++;
            bean.setLac((String) objs[idx]);
            idx++;
            bean.setCellid((String) objs[idx]);
            idx++;
            bean.setPositionMode((String) objs[idx]);
            idx++;
            bean.setCheckType((Integer) objs[idx]);
            idx++;
            bean.setCheckTime((Timestamp) objs[idx]);
            idx++;
            bean.setCreateTime((Timestamp) objs[idx]);
            idx++;
        }
        return bean;
    }

}
