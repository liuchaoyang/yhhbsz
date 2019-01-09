package com.yzxt.yh.module.chk.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.ConstDevice;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.module.chk.bean.UserDevice;
import com.yzxt.yh.module.chk.dao.UserDeviceDao;
import com.yzxt.yh.module.cli.bean.WbConfig;
import com.yzxt.yh.module.cli.dao.WbConfigDao;
import com.yzxt.yh.util.StringUtil;

@Transactional(ConstTM.DEFAULT)
public class UserDeviceService
{
    private UserDeviceDao userDeviceDao;

    private WbConfigDao wbConfigDao;

    public UserDeviceDao getUserDeviceDao()
    {
        return userDeviceDao;
    }

    public void setUserDeviceDao(UserDeviceDao userDeviceDao)
    {
        this.userDeviceDao = userDeviceDao;
    }

    public WbConfigDao getWbConfigDao()
    {
        return wbConfigDao;
    }

    public void setWbConfigDao(WbConfigDao wbConfigDao)
    {
        this.wbConfigDao = wbConfigDao;
    }

    /**
     * 添加用户设备信息
     * @param userDevice
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result add(UserDevice userDevice) throws Exception
    {
        Result result = new Result();
        // 判断设备是否已经绑定过
        boolean isExist = userDeviceDao.isExist(userDevice);
        if (isExist)
        {
            result.setState(Result.STATE_FAIL);
            result.setMsg("此设备信息已经和其他用户绑定，添加用户设备信息失败！");
            return result;
        }
        Timestamp now = new Timestamp(System.currentTimeMillis());
        userDevice.setCreateTime(now);
        userDevice.setUpdateBy(userDevice.getCreateBy());
        userDevice.setUpdateTime(now);
        String id = userDeviceDao.insert(userDevice);
        //腕表的绑定还需要保存生成腕表的设置表数据
        if (userDevice.getDeviceType().equals(ConstDevice.DEVICE_TYPE_WA_SUREZEN))
        {
            //判断是否已经绑定。如果已经绑定，则需修改配置人的信息，否则，直接绑定。
            boolean isConfig = wbConfigDao.isConfig(userDevice.getDeviceSn());
            WbConfig bean = new WbConfig();
            bean.setCustId(userDevice.getCustId());
            bean.setCreateBy(userDevice.getCreateBy());
            bean.setUpdateBy(userDevice.getCreateBy());
            bean.setCreateTime(now);
            bean.setUpdateTime(now);
            if (isConfig)
            {
                //配置过就修改
                bean.setId(userDevice.getDeviceSn());
                bean.setDeviceId(id);
                wbConfigDao.update(bean);
            }
            else
            {
                //没有配置过就插入
                bean.setState(1);
                bean.setId(userDevice.getDeviceSn());
                bean.setDeviceId(id);
                wbConfigDao.insert(bean);
            }
        }
        result.setData(id);
        result.setState(Result.STATE_SUCESS);
        return result;
    }

    /**
     * 修改设备绑定信息
     * @param userDevice
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result update(UserDevice userDevice) throws Exception
    {
        Result result = new Result();
        // 判断设备是否已经绑定过
        boolean isExist = userDeviceDao.isExist(userDevice);
        if (isExist)
        {
            result.setState(Result.STATE_FAIL);
            result.setMsg("此设备信息已经和其他用户绑定，修改用户设备信息失败！");
            return result;
        }
        Timestamp now = new Timestamp(System.currentTimeMillis());
        userDevice.setUpdateTime(now);
        UserDevice udE = userDeviceDao.get(userDevice.getId());
        userDeviceDao.update(userDevice);
        wbConfigDao.updateId(udE.getDeviceSn(), userDevice.getDeviceSn());
        System.out.println(udE.getDeviceSn());
        result.setData(userDevice.getId());
        result.setState(Result.STATE_SUCESS);
        return result;
    }

    /**
     * 删除绑定记录
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String id) throws Exception
    {
        if (StringUtil.isNotEmpty(id))
        {
            userDeviceDao.delete(id);
        }
    }

    /**
     * 加载绑定记录
     * 
     * @param id
     * @return
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public UserDevice load(String id) throws Exception
    {
        return userDeviceDao.load(id);
    }

    /**
     * 根据设备信息查询用户绑定信息
     * @param userDevice
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public UserDevice getByDeviceInfo(Map<String, Object> filter) throws Exception
    {
        return userDeviceDao.getByDeviceInfo(filter);
    }

    /**
     * 分页查询
     * 
     * @param filter
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<UserDevice> queryUserDeviceByPage(Map<String, Object> filter, int page, int pageSize)
    {
        return userDeviceDao.queryUserDeviceByPage(filter, page, pageSize);
    }

    /**
     * 客户端查询
     * @author huangGang
     * 2015.4.30
     * @param filter
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<UserDevice> queryDeviceTran(Map<String, Object> filter)
    {
        return userDeviceDao.queryDeviceTran(filter);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public boolean getIsBind(Map<String, Object> filter)
    {
        return userDeviceDao.getIsBind(filter);
    }

}
