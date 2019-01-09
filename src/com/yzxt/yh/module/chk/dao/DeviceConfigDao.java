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
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.chk.bean.DeviceConfig;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.StringUtil;

/**
 * 设备基础数据类
 * @author h
 * 2015.6.12
 * */

public class DeviceConfigDao extends HibernateSupport<DeviceConfig> implements BaseDao<DeviceConfig>
{
    /**
     * 平台插入基础数据
     * @author h
     * 2015.6.12
     * */
    public String insert(DeviceConfig deviceConfig) throws Exception
    {
        super.insert(deviceConfig);
        return deviceConfig.getId();
    }

    /**
     * 平台修改基础数据
     * @author h
     * 2015.6.12
     * */
    public int updateDevice(DeviceConfig deviceConfig) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" update DeviceConfig cdi set cdi.userName = ?, cdi.val = ?, cdi.updateBy = ?, updateTime = ? ");
        sql.append(" where cdi.id = ?");
        params.add(deviceConfig.getUserName(), Hibernate.STRING);
        params.add(deviceConfig.getVal(), Hibernate.STRING);
        params.add(deviceConfig.getUpdateBy(), Hibernate.STRING);
        params.add(deviceConfig.getUpdateTime(), Hibernate.TIMESTAMP);
        params.add(deviceConfig.getId(), Hibernate.STRING);
        return super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
    }

    /**
     * 平台分页查询设备基础数据类别信息
     * @author h
     * @param page pageSize filter
     * @return 
     * 2015.6.12
     * */
    @SuppressWarnings("unchecked")
    public PageModel<DeviceConfig> queryDeviceByPage(Map<String, Object> filter, int page, int pageSize)
    {
        PageModel<DeviceConfig> pageModel = new PageModel<DeviceConfig>();
        HibernateParams params = new HibernateParams();
        StringBuilder mSql = new StringBuilder();
        mSql.append(" from chk_device_config t");
        mSql.append(" where 1=1");
        String deviceTypeCode = (String) filter.get("deviceTypeCode");
        mSql.append(" and t.device_type_code = ? ");
        params.add(deviceTypeCode, Hibernate.STRING);
        User user = (User) filter.get("user");
        // 权限
        if (Constant.USER_TYPE_ADMIN.equals(user.getType()))
        // 只有管理员才能查看
        {

        }
        else
        // 其它非管理员用户不能查询到记录
        {
            return pageModel;
        }
        // 总启启记录数
        StringBuilder totalCountsql = new StringBuilder();
        totalCountsql.append("select count(*) as c ").append(mSql);
        Integer totalCount = (Integer) super.getSession().createSQLQuery(totalCountsql.toString())
                .addScalar("c", Hibernate.INTEGER).setParameters(params.getVals(), params.getTypes()).uniqueResult();
        // 分页记录
        StringBuilder pSql = new StringBuilder();
        pSql.append("select t.id, t.device_type_code, t.user_name, t.val, t.seq");
        pSql.append(", t.create_by, t.create_time, t.update_by, t.update_time ");
        pSql.append(mSql).append(" order by t.val asc limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("device_type_code", Hibernate.STRING).addScalar("user_name", Hibernate.STRING)
                .addScalar("val", Hibernate.STRING).addScalar("seq", Hibernate.INTEGER)
                .addScalar("create_by", Hibernate.STRING).addScalar("create_time", Hibernate.TIMESTAMP)
                .addScalar("update_by", Hibernate.STRING).addScalar("update_time", Hibernate.TIMESTAMP)
                .setParameters(params.getVals(), params.getTypes()).list();
        List<DeviceConfig> list = new ArrayList<DeviceConfig>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            DeviceConfig deviceConfig = new DeviceConfig();
            idx = 0;
            deviceConfig.setId((String) objs[idx]);
            idx++;
            deviceConfig.setDeviceTypeCode((String) objs[idx]);
            idx++;
            deviceConfig.setUserName((String) objs[idx]);
            idx++;
            deviceConfig.setVal((String) objs[idx]);
            idx++;
            deviceConfig.setSeq((Integer) objs[idx]);
            idx++;
            deviceConfig.setCreateBy((String) objs[idx]);
            idx++;
            deviceConfig.setCreateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            deviceConfig.setUpdateBy((String) objs[idx]);
            idx++;
            deviceConfig.setUpdateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            list.add(deviceConfig);
        }
        pageModel.setTotal(totalCount);
        pageModel.setData(list);
        return pageModel;
    }

    /**
     * 加载设备信息
     * @author h
     */
    public DeviceConfig get(String id)
    {
        return super.get(id);
    }

    /**
     * 加载设备配置信息
     * @author h
     * @param val 
     */
    @SuppressWarnings("unchecked")
    public DeviceConfig getConfig(String code, String val)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.user_name, t.id from chk_device_config t ");
        sql.append(" where 1= 1 and t.device_type_code = ? and t.val = ? ");
        params.add(code, Hibernate.STRING);
        params.add(val, Hibernate.STRING);
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString())
                .addScalar("user_name", Hibernate.STRING).addScalar("id", Hibernate.STRING)
                .setParameters(params.getVals(), params.getTypes()).list();
        DeviceConfig deviceConfig = null;
        if (objsList != null && objsList.size() > 0)
        {
            Object[] objs = objsList.get(0);
            deviceConfig = new DeviceConfig();
            int i = 0;
            deviceConfig.setUserName((String) objs[i++]);
        }
        return deviceConfig;
    }

    /**
     * 判断设备用户数配置是否存在
     * @param deviceConfig
     * @author h
     * @return boolean
     * 2015.6.13
     * */
    public boolean getDeviceConfig(DeviceConfig deviceConfig)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" select count(t.id) as c from chk_device_config t ");
        sql.append(" where 1= 1");
        sql.append(" and t.device_type_code = ?");
        params.add(deviceConfig.getDeviceTypeCode(), Hibernate.STRING);
        sql.append(" and (t.user_name = ? or t.val = ?)");
        params.add(deviceConfig.getUserName(), Hibernate.STRING);
        params.add(deviceConfig.getVal(), Hibernate.STRING);
        Object c = super.getSession().createSQLQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .uniqueResult();
        return Long.valueOf(c.toString()).longValue() > 0;
    }

    /**
     * 判断按键名称是否存在
     * @param userName
     * @param exceptUserId
     * @return
     * @throws Exception
     */
    public boolean getUserNameExist(String userName, String configCode, String exceptUserId) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select count(t.id) as c from DeviceConfig t where 1=1");
        if (StringUtil.isNotEmpty(exceptUserId))
        {
            sql.append(" and t.id <> ?");
            params.add(exceptUserId, Hibernate.STRING);
        }
        if (StringUtil.isNotEmpty(configCode))
        {
            sql.append(" and t.deviceTypeCode = ?");
            params.add(configCode, Hibernate.STRING);
        }
        sql.append(" and lower(t.userName) = ?");
        params.add(userName.toLowerCase(), Hibernate.STRING);
        Object c = (Long) super.getSession().createQuery(sql.toString())
                .setParameters(params.getVals(), params.getTypes()).uniqueResult();
        return Long.valueOf(c.toString()).longValue() > 0;
    }

    /**
     * 判断按键值是否存在
     * @param idCard
     * @param exceptUserId
     * @return
     * @throws Exception
     */
    public boolean getValExist(String configVal, String configCode, String exceptUserId) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select count(t.id) as c from DeviceConfig t where 1=1");
        if (StringUtil.isNotEmpty(exceptUserId))
        {
            sql.append(" and t.id <> ?");
            params.add(exceptUserId, Hibernate.STRING);
        }
        if (StringUtil.isNotEmpty(configCode))
        {
            sql.append(" and t.deviceTypeCode = ?");
            params.add(configCode, Hibernate.STRING);
        }
        sql.append(" and lower(t.val) = ?");
        params.add(configVal.toLowerCase(), Hibernate.STRING);
        Object c = super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .uniqueResult();
        return Long.valueOf(c.toString()).longValue() > 0;
    }

    /**
     * 判断按键值是否存在
     * @param idCard
     * @param exceptUserId
     * @return
     * @throws Exception
     */
    public boolean getSeqExist(Integer seq, String configCode, String exceptUserId) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select count(t.id) as c from DeviceConfig t where 1=1");
        if (StringUtil.isNotEmpty(exceptUserId))
        {
            sql.append(" and t.id <> ?");
            params.add(exceptUserId, Hibernate.STRING);
        }
        if (StringUtil.isNotEmpty(configCode))
        {
            sql.append(" and t.deviceTypeCode = ?");
            params.add(configCode, Hibernate.STRING);
        }
        sql.append(" and t.seq = ?");
        params.add(seq, Hibernate.INTEGER);
        Object c = super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .uniqueResult();
        return Long.valueOf(c.toString()).longValue() > 0;
    }

    @SuppressWarnings("unchecked")
    public List<DeviceConfig> getDeviceConfigs(String code)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder mSql = new StringBuilder();
        mSql.append(" from chk_device_config t");
        mSql.append(" where 1=1");
        mSql.append(" and t.device_type_code = ? ");
        params.add(code, Hibernate.STRING);
        // 分页记录
        StringBuilder pSql = new StringBuilder();
        pSql.append("select t.id, t.device_type_code, t.user_name, t.val, t.seq");
        pSql.append(", t.create_by, t.create_time, t.update_by, t.update_time ");
        pSql.append(mSql).append(" order by t.val asc ");
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("device_type_code", Hibernate.STRING).addScalar("user_name", Hibernate.STRING)
                .addScalar("val", Hibernate.STRING).addScalar("seq", Hibernate.INTEGER)
                .addScalar("create_by", Hibernate.STRING).addScalar("create_time", Hibernate.TIMESTAMP)
                .addScalar("update_by", Hibernate.STRING).addScalar("update_time", Hibernate.TIMESTAMP)
                .setParameters(params.getVals(), params.getTypes()).list();
        List<DeviceConfig> list = new ArrayList<DeviceConfig>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            DeviceConfig deviceConfig = new DeviceConfig();
            idx = 0;
            deviceConfig.setId((String) objs[idx]);
            idx++;
            deviceConfig.setDeviceTypeCode((String) objs[idx]);
            idx++;
            deviceConfig.setUserName((String) objs[idx]);
            idx++;
            deviceConfig.setVal((String) objs[idx]);
            idx++;
            deviceConfig.setSeq((Integer) objs[idx]);
            idx++;
            deviceConfig.setCreateBy((String) objs[idx]);
            idx++;
            deviceConfig.setCreateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            deviceConfig.setUpdateBy((String) objs[idx]);
            idx++;
            deviceConfig.setUpdateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            list.add(deviceConfig);
        }
        return list;
    }

}
