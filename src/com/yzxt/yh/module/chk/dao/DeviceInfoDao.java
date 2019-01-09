package com.yzxt.yh.module.chk.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.ConstDevice;
import com.yzxt.yh.module.chk.bean.DeviceInfo;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.StringUtil;

/**
 * 设备基础数据类
 * @author h
 * 2015.6.12
 * */

public class DeviceInfoDao extends HibernateSupport<DeviceInfo> implements BaseDao<DeviceInfo> 
{
    /**
     * 平台插入基础数据
     * @author h
     * 2015.6.12
     * */
    public String insert(DeviceInfo deviceInfo) throws Exception
    {
        super.insert(deviceInfo);
        return deviceInfo.getCode();
    }
    
    /**
     * 平台修改基础数据
     * @author h
     * 2015.6.12
     * */
    public int  updateDevice(DeviceInfo deviceInfo) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" update chk_device_info cdi set cdi.name_ = ?, cdi.check_type = ?, cdi.state = ?, cdi.update_by = ?, update_time = ? ");
        sql.append(" where cdi.code = ?");
        params.add(deviceInfo.getName(), Hibernate.STRING);
        params.add(deviceInfo.getCheckType(), Hibernate.INTEGER);
        params.add(deviceInfo.getState(), Hibernate.INTEGER);
        params.add(deviceInfo.getUpdateBy(), Hibernate.STRING);
        params.add(deviceInfo.getUpdateTime(), Hibernate.TIMESTAMP);
        params.add(deviceInfo.getCode(), Hibernate.STRING);
        int c = super.getSession().createSQLQuery(sql.toString()).setParameters(params.getVals(), params.getTypes()).executeUpdate();
        return c;
    }
    
    /**
     * 平台分页查询设备基础数据类别信息
     * @author h
     * @param page pageSize filter
     * @return 
     * 2015.6.12
     * */
    @SuppressWarnings("unchecked")
    public PageModel<DeviceInfo> queryDeviceByPage(Map<String, Object> filter, int page, int pageSize)
    {
        PageModel<DeviceInfo> pageModel = new PageModel<DeviceInfo>();
        HibernateParams params = new HibernateParams();
        StringBuilder mSql = new StringBuilder();
        mSql.append(" from chk_device_info t");
        /*mSql.append(" inner join chk_device_config cdc");*/
        mSql.append(" where 1=1");
        // 查询条件
        String deviceCode = (String) filter.get("deviceCode");
        if (StringUtil.isNotEmpty(deviceCode))
        {
            mSql.append(" and t.code like ").append(params.addLikePart(deviceCode));
        }
        String deviceName = (String) filter.get("deviceName");
        if (StringUtil.isNotEmpty(deviceName))
        {
            mSql.append(" and t.name_ like ").append(params.addLikePart(deviceName));
        }
        String checkTypeStr = (String) filter.get("checkType");
        if (StringUtil.isNotEmpty(checkTypeStr))
        {
            Integer checkType = Integer.parseInt(checkTypeStr);
            mSql.append(" and t.check_type = ? ");
            params.add(checkType, Hibernate.INTEGER);
        }
        User user = (User) filter.get("user");
        /*// 权限
        if (Constant.USER_TYPE_ADMIN.equals(user.getType()))
        // 只有管理员才能查看
        {

        }
        else
        // 其它非管理员用户不能查询到记录
        {
            return pageModel;
        }*/
        // 总记录数
        StringBuilder totalCountsql = new StringBuilder();
        totalCountsql.append("select count(*) as c ").append(mSql);
        Integer totalCount = (Integer) super.getSession().createSQLQuery(totalCountsql.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
        // 分页记录
        StringBuilder pSql = new StringBuilder();
        pSql.append("select t.code, t.name_, t.img, t.check_type, t.state");
        pSql.append(", t.create_by, t.create_time, t.update_by, t.update_time");
        pSql.append(mSql).append(" order by t.create_time desc limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString()).addScalar("code", Hibernate.STRING)
                .addScalar("name_", Hibernate.STRING).addScalar("img", Hibernate.STRING)
                .addScalar("check_type", Hibernate.INTEGER).addScalar("state", Hibernate.INTEGER)
                .addScalar("create_by", Hibernate.STRING).addScalar("create_time", Hibernate.TIMESTAMP)
                .addScalar("update_by", Hibernate.STRING).addScalar("update_time", Hibernate.TIMESTAMP)
                .setParameters(params.getVals(), params.getTypes()).list();
        List<DeviceInfo> list = new ArrayList<DeviceInfo>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            DeviceInfo deviceInfo = new DeviceInfo();
            idx = 0;
            deviceInfo.setCode((String) objs[idx]);
            idx++;
            deviceInfo.setName((String) objs[idx]);
            idx++;
            deviceInfo.setImg((String) objs[idx]);
            idx++;
            deviceInfo.setCheckType((Integer) objs[idx]);
            idx++;
            deviceInfo.setState((Integer) objs[idx]);
            idx++;
            deviceInfo.setCreateBy((String) objs[idx]);
            idx++;
            deviceInfo.setCreateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            deviceInfo.setUpdateBy((String) objs[idx]);
            idx++;
            deviceInfo.setUpdateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            list.add(deviceInfo);
        }
        pageModel.setTotal(totalCount);
        pageModel.setData(list);
        return pageModel;
    }
    
    /**
     * 加载设备信息
     * @author h
     */
    public DeviceInfo get(String id)
    {
        return super.get(id);
    }

    @SuppressWarnings("unchecked")
    public List<DeviceInfo> getDeviceInfos(Map<String, Object> filter)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql =  new StringBuilder();
        sql.append(" from chk_device_info t ");
        sql.append(" where 1 = 1");
        sql.append(" and t.state = ?");
        params.add(ConstDevice.DEVICE_STATE_BIND, Hibernate.INTEGER);
        //查询条件
        StringBuilder pSql = new StringBuilder();
        pSql.append("select t.code, t.name_, t.img, t.check_type, t.state");
        pSql.append(", t.create_by, t.create_time, t.update_by, t.update_time");
        pSql.append(sql).append(" order by t.create_time desc ");
        /*pSql.append(sql).append(" order by t.create_time desc limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);*/
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString()).addScalar("code", Hibernate.STRING)
                .addScalar("name_", Hibernate.STRING).addScalar("img", Hibernate.STRING)
                .addScalar("check_type", Hibernate.INTEGER).addScalar("state", Hibernate.INTEGER)
                .addScalar("create_by", Hibernate.STRING).addScalar("create_time", Hibernate.TIMESTAMP)
                .addScalar("update_by", Hibernate.STRING).addScalar("update_time", Hibernate.TIMESTAMP)
                .setParameters(params.getVals(), params.getTypes()).list();
        List<DeviceInfo> list = new ArrayList<DeviceInfo>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            DeviceInfo deviceInfo = new DeviceInfo();
            idx = 0;
            deviceInfo.setCode((String) objs[idx]);
            idx++;
            deviceInfo.setName((String) objs[idx]);
            idx++;
            deviceInfo.setImg((String) objs[idx]);
            idx++;
            deviceInfo.setCheckType((Integer) objs[idx]);
            idx++;
            deviceInfo.setState((Integer) objs[idx]);
            idx++;
            deviceInfo.setCreateBy((String) objs[idx]);
            idx++;
            deviceInfo.setCreateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            deviceInfo.setUpdateBy((String) objs[idx]);
            idx++;
            deviceInfo.setUpdateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            list.add(deviceInfo);
        }
        return list;
    }

    public boolean getNameExist(String name, String infoId)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select count(t.id) as c from DeviceInfo t where 1=1");
        if (StringUtil.isNotEmpty(infoId))
        {
            sql.append(" and t.code <> ?");
            params.add(infoId, Hibernate.STRING);
        }
        sql.append(" and lower(t.name) = ?");
        params.add(name.toLowerCase(), Hibernate.STRING);
        Object c = (Long) super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes()).uniqueResult();
        return Long.valueOf(c.toString()).longValue() > 0;
    }
}
