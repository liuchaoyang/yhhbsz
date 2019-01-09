package com.yzxt.yh.module.chk.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;

import com.yzxt.fw.common.BaseDao;
import com.yzxt.fw.ext.hibernate.HibernateParams;
import com.yzxt.fw.ext.hibernate.HibernateSupport;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.ConstDevice;
import com.yzxt.yh.constant.ConstRole;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.chk.bean.UserDevice;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.StringUtil;

public class UserDeviceDao extends HibernateSupport<UserDevice> implements BaseDao<UserDevice>
{
    /**
     * 插入设备绑定信息
     * @param userDevice
     * @return
     * @throws Exception
     */
    public String insert(UserDevice userDevice) throws Exception
    {
        super.save(userDevice);
        return userDevice.getId();
    }

    /**
     * 更新绑定记录
     * @return 
     */
    public int update(UserDevice userDevice) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("update UserDevice set deviceType = ?, deviceSn = ?, deviceSnExt = ?");
        params.add(userDevice.getDeviceType(), Hibernate.STRING);
        params.add(userDevice.getDeviceSn(), Hibernate.STRING);
        params.add(userDevice.getDeviceSnExt(), Hibernate.STRING);
        /* if (StringUtil.isNotEmpty(userDevice.getIdCard()))
         {
             sql.append(", idCard = ?");
             params.add(userDevice.getIdCard(), Hibernate.STRING);
         }*/
        sql.append(", updateBy = ?, updateTime = ?");
        sql.append(" where id = ?");
        params.add(userDevice.getUpdateBy(), Hibernate.STRING);
        params.add(userDevice.getUpdateTime(), Hibernate.TIMESTAMP);
        params.add(userDevice.getId(), Hibernate.STRING);
        return super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
    }

    /**
     * 删除绑定记录
     */
    public void delete(String id) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("delete from UserDevice t where id = ?");
        params.add(id, Hibernate.STRING);
        super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .executeUpdate();
    }

    /**
     * 根据设备类型，设备标志，设备额外标志判断指定设备是否已经被绑定过
     * @param userDevice
     * @return
     * @throws Exception
     */
    public boolean isExist(UserDevice userDevice) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" select count(t.id) as c from UserDevice t");
        sql.append(" where 1=1");
        // 是否要排除指定ID
        if (StringUtil.isNotEmpty(userDevice.getId()))
        {
            sql.append(" and t.id <> ?");
            params.add(userDevice.getId(), Hibernate.STRING);
        }
        // 额外标志
        if (StringUtil.isEmpty(userDevice.getDeviceSnExt()))
        {
            sql.append(" and (t.deviceSnExt is null or t.deviceSnExt = '')");
        }
        else
        {
            sql.append(" and t.deviceSnExt = ?");
            params.add(userDevice.getDeviceSnExt(), Hibernate.STRING);
        }
        sql.append(" and t.deviceSn = ? and t.deviceType = ?");
        params.add(userDevice.getDeviceSn(), Hibernate.STRING);
        params.add(userDevice.getDeviceType(), Hibernate.STRING);
        Object c = super.getSession().createQuery(sql.toString()).setParameters(params.getVals(), params.getTypes())
                .uniqueResult();
        return Long.valueOf(c.toString()).longValue() > 0;
    }

    /**
     * 根据设备信息查询用户绑定信息
     * @param userDevice
     */
    @SuppressWarnings("unchecked")
    public UserDevice getByDeviceInfo(Map<String, Object> filter) throws Exception
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" select t from UserDevice t");
        sql.append(" where 1=1");
        // 设备参数值
        Object deviceTypeObj = filter.get("deviceType");
        String deviceSn = (String) filter.get("deviceSn");
        String deviceSnExt = (String) filter.get("deviceSnExt");
        // 额外标志
        if (StringUtil.isEmpty(deviceSnExt))
        {
            sql.append(" and (t.deviceSnExt is null or t.deviceSnExt = '')");
        }
        else
        {
            sql.append(" and t.deviceSnExt = ?");
            params.add(deviceSnExt, Hibernate.STRING);
        }
        if (deviceTypeObj instanceof String)
        {
            sql.append(" and t.deviceType = ?");
            params.add(deviceTypeObj, Hibernate.STRING);
        }
        else
        {
            String[] dts = (String[]) deviceTypeObj;
            sql.append(" and t.deviceType in(");
            for (int i = 0; i < dts.length; i++)
            {
                if (i > 0)
                {
                    sql.append(", ?");
                }
                else
                {
                    sql.append("?");
                }
                params.add(dts[i], Hibernate.STRING);
            }
            sql.append(")");
        }
        sql.append(" and t.deviceSn = ?");
        params.add(deviceSn, Hibernate.STRING);
        List<UserDevice> list = super.getSession().createQuery(sql.toString())
                .setParameters(params.getVals(), params.getTypes()).list();
        return list != null && list.size() > 0 ? list.get(0) : null;
    }

    /**
     * 分页查询
     * 
     * @param filter
     * @param page
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    public PageModel<UserDevice> queryUserDeviceByPage(Map<String, Object> filter, int page, int pageSize)
    {
        PageModel<UserDevice> pageModel = new PageModel<UserDevice>();
        HibernateParams params = new HibernateParams();
        StringBuilder mSql = new StringBuilder();
        mSql.append(" from chk_user_device t");
        mSql.append(" inner join sys_user su on su.id = t.cust_id");
        mSql.append(" inner join chk_device_info cdi on cdi.code = t.device_type");
        mSql.append(" left join chk_device_config cdc on cdc.val = t.device_sn_ext and cdc.device_type_code = t.device_type");
        mSql.append(" where 1=1");
        // 查询条件
        String deviceType = (String) filter.get("deviceType");
        if (StringUtil.isNotEmpty(deviceType))
        {
            mSql.append(" and t.device_type like ").append(params.addLikePart(deviceType));
        }
        String deviceSn = (String) filter.get("deviceSn");
        if (StringUtil.isNotEmpty(deviceSn))
        {
            mSql.append(" and t.device_sn like ").append(params.addLikePart(deviceSn));
        }
        String custName = (String) filter.get("custName");
        if (StringUtil.isNotEmpty(custName))
        {
            mSql.append(" and su.name_ like ").append(params.addLikePart(custName));
        }
        User user = (User) filter.get("user");
        // 如果是普通居民登录系统，则只能查看自己的数据
        /*if (Constant.USER_TYPE_RESIDENT.equals(user.getType()))
        {
            mSql.append(" and t.cust_ = ?");
            params.add(user.getIdCard(), Hibernate.STRING);
        }*/
        // 权限
        if (Constant.USER_TYPE_DOCTOR.equals(user.getType()))
        // 医生查询自己的会员记录
        {
            /*mSql.append(" and exists(select smi.id from svb_member_info smi where smi.cust_id = t.cust_id and smi.doctor_id = ? and smi.state = 1)");
            params.add(user.getId(), Hibernate.STRING);*/
            mSql.append(" and su.org_id = ?");
            params.add(user.getOrgId(), Hibernate.STRING);
        }
        else if (Constant.USER_TYPE_CUSTOMER.equals(user.getType()))
        // 普通客户查询自己的记录
        {
            mSql.append(" and t.cust_id = ?");
            params.add(user.getId(), Hibernate.STRING);
        }
        else if (Constant.USER_TYPE_ADMIN.equals(user.getType()))
        {
            // 不是管理员
            Collection<String> roles = user.getRoles();
            // 不是系统管理员，添加组织限制
            if (roles == null || !roles.contains(ConstRole.ADMIN))
            {
                mSql.append(" and exists(");
                mSql.append("select sor.id from sys_org sor where sor.full_id like ")
                        .append(params.addLikePart("/" + user.getOrgId() + "/")).append(" and sor.id = su.org_id");
                mSql.append(")");
            }
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
        pSql.append("select t.id, t.cust_id, t.device_type, t.device_sn, cdc.user_name");
        pSql.append(", t.create_by, t.create_time, t.update_by, t.update_time, su.name_ , cdi.name_ as deviceName");
        pSql.append(mSql).append(" order by t.update_time desc limit ?, ?");
        params.add(pageSize * (page - 1), Hibernate.INTEGER);
        params.add(pageSize, Hibernate.INTEGER);
        List<Object[]> objsList = super.getSession().createSQLQuery(pSql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("cust_id", Hibernate.STRING).addScalar("device_type", Hibernate.STRING)
                .addScalar("device_sn", Hibernate.STRING).addScalar("user_name", Hibernate.STRING)
                .addScalar("create_by", Hibernate.STRING).addScalar("create_time", Hibernate.TIMESTAMP)
                .addScalar("update_by", Hibernate.STRING).addScalar("update_time", Hibernate.TIMESTAMP)
                .addScalar("name_", Hibernate.STRING).addScalar("deviceName", Hibernate.STRING)
                .setParameters(params.getVals(), params.getTypes()).list();
        List<UserDevice> list = new ArrayList<UserDevice>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            UserDevice userDevice = new UserDevice();
            idx = 0;
            userDevice.setId((String) objs[idx]);
            idx++;
            userDevice.setCustId((String) objs[idx]);
            idx++;
            userDevice.setDeviceType((String) objs[idx]);
            idx++;
            userDevice.setDeviceSn((String) objs[idx]);
            idx++;
            userDevice.setDeviceSnExt((String) objs[idx]);
            idx++;
            userDevice.setCreateBy((String) objs[idx]);
            idx++;
            userDevice.setCreateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            userDevice.setUpdateBy((String) objs[idx]);
            idx++;
            userDevice.setUpdateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            userDevice.setCustName((String) objs[idx]);
            idx++;
            userDevice.setDeviceName((String) objs[idx]);
            idx++;
            list.add(userDevice);
        }

        pageModel.setTotal(totalCount);
        pageModel.setData(list);
        return pageModel;
    }

    /**
     * 客户端查询
     * @author huangGang
     * 2015.4.30
     * @param filter
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<UserDevice> queryDeviceTran(Map<String, Object> filter)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id, t.cust_id, t.device_type, t.device_sn, t.device_sn_ext ");
        sql.append(", t.create_by, t.create_time, t.update_by, t.update_time, cdi.check_type, cdi.name_, cdc.user_name as device_user");
        sql.append(", su.name_ as custName ");
        sql.append(" from chk_user_device t ");
        sql.append(" inner join chk_device_info cdi on cdi.code = t.device_type ");
        sql.append(" left join chk_device_config cdc on cdc.val = t.device_sn_ext and cdc.device_type_code = t.device_type");
        sql.append(" left join sys_user su on su.id = t.cust_id ");
        sql.append(" where 1=1");
        // custId :1、该参数不为空：只获取该参数用户对于的绑定设备列表。
        //2、该参数为空：获取userId对应的所有绑定设备列表。
        String custId = (String) filter.get("custId");
        String userId = (String) filter.get("userId");
        if (StringUtil.isNotEmpty(custId))
        {
            sql.append(" and  t.cust_id = ? ");
            params.add(custId, Hibernate.STRING);
        }
        else
        {
            sql.append(" and (t.cust_id in");
            //查询家庭成员
            sql.append(" (select scf.member_id from sys_cust_family scf where scf.cust_id = ?");
            sql.append(") or t.cust_id = ?)");
            params.add(userId, Hibernate.STRING);
            params.add(userId, Hibernate.STRING);
        }

        sql.append(" order by t.create_time desc");
        // 取数据条数
        // 查询数据
        List<Object[]> objsList = super.getSession().createSQLQuery(sql.toString()).addScalar("id", Hibernate.STRING)
                .addScalar("cust_id", Hibernate.STRING).addScalar("device_type", Hibernate.STRING)
                .addScalar("device_sn", Hibernate.STRING).addScalar("device_sn_ext", Hibernate.STRING)
                .addScalar("create_by", Hibernate.STRING).addScalar("create_time", Hibernate.TIMESTAMP)
                .addScalar("update_by", Hibernate.STRING).addScalar("update_time", Hibernate.TIMESTAMP)
                .addScalar("check_type", Hibernate.INTEGER).addScalar("name_", Hibernate.STRING)
                .addScalar("device_user", Hibernate.STRING).addScalar("custName", Hibernate.STRING)
                .setParameters(params.getVals(), params.getTypes()).list();
        System.out.println(sql);
        List<UserDevice> list = new ArrayList<UserDevice>();
        int idx = 0;
        for (Object[] objs : objsList)
        {
            UserDevice userDevice = new UserDevice();
            idx = 0;
            userDevice.setId((String) objs[idx]);
            idx++;
            userDevice.setCustId((String) objs[idx]);
            idx++;
            userDevice.setDeviceType((String) objs[idx]);
            idx++;
            userDevice.setDeviceSn((String) objs[idx]);
            idx++;
            userDevice.setDeviceSnExt((String) objs[idx]);
            idx++;
            userDevice.setCreateBy((String) objs[idx]);
            idx++;
            userDevice.setCreateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            userDevice.setUpdateBy((String) objs[idx]);
            idx++;
            userDevice.setUpdateTime(objs[idx] != null ? (Timestamp) objs[idx] : null);
            idx++;
            userDevice.setCheckType((Integer) objs[idx]);
            idx++;
            userDevice.setDeviceName((String) objs[idx]);
            idx++;
            userDevice.setDeviceUser((String) objs[idx]);
            idx++;
            userDevice.setCustName((String) objs[idx]);
            idx++;
            list.add(userDevice);
        }
        return list;
    }

    public boolean getIsBind(Map<String, Object> filter)
    {
        HibernateParams params = new HibernateParams();
        StringBuilder sql = new StringBuilder();
        sql.append(" select count(t.id) as c from UserDevice t");
        sql.append(" where 1=1");
        // 设备参数值
        String deviceType = (String) filter.get("deviceType");
        String deviceSn = (String) filter.get("deviceSn");
        String deviceSnExt = (String) filter.get("deviceSnExt");
        // 额外标志
        if (StringUtil.isEmpty(deviceSnExt))
        {
            sql.append(" and (t.deviceSnExt is null or t.deviceSnExt = '')");
        }
        else
        {
            sql.append(" and t.deviceSnExt = ?");
            params.add(deviceSnExt, Hibernate.STRING);
        }
        sql.append(" and t.deviceSn = ? and t.deviceType = ?");
        params.add(deviceSn, Hibernate.STRING);
        params.add(deviceType, Hibernate.STRING);
        Long c = (Long) super.getSession().createQuery(sql.toString())
                .setParameters(params.getVals(), params.getTypes()).uniqueResult();
        return c.longValue() > 0;
    }

    /**
     * 获取用户设备信息
     * @param id
     * @return
     */
    public UserDevice load(String id) throws Exception
    {
        return super.get(id);
    }

}
