package com.yzxt.yh.module.chk.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.module.chk.bean.DeviceConfig;
import com.yzxt.yh.module.chk.dao.DeviceConfigDao;
import com.yzxt.yh.util.StringUtil;

@Transactional(ConstTM.DEFAULT)
public class DeviceConfigService
{
    private DeviceConfigDao deviceConfigDao;

    public DeviceConfigDao getDeviceConfigDao()
    {
        return deviceConfigDao;
    }

    public void setDeviceConfigDao(DeviceConfigDao deviceConfigDao)
    {
        this.deviceConfigDao = deviceConfigDao;
    }

    /**
     * 添加设备基础数据
     * @author h
     * @param
     * @return 
     * 2015.6.13
     * */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result addUserNum(DeviceConfig deviceConfig) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        deviceConfig.setCreateTime(now);
        deviceConfig.setUpdateTime(now);
        //判定设备用户是否已经配置
        String configCode = deviceConfig.getDeviceTypeCode();
        String userName = deviceConfig.getUserName();
        if (StringUtil.isNotEmpty(userName) && deviceConfigDao.getUserNameExist(userName, configCode, null))
        {
            return new Result(Result.STATE_FAIL, "按键名称重复。", null);
        }
        String configVal = deviceConfig.getVal();
        if (StringUtil.isNotEmpty(configVal) && deviceConfigDao.getValExist(configVal, configCode, null))
        {
            return new Result(Result.STATE_FAIL, "按键值重复。", null);
        }
        Integer seq = deviceConfig.getSeq();
        if (seq != null && deviceConfigDao.getSeqExist(seq, configCode, null))
        {
            return new Result(Result.STATE_FAIL, "顺序号重复。", null);
        }
        /*boolean isExist = deviceConfigDao.getDeviceConfig(deviceConfig);
        if (isExist)
        {
            return new Result(Result.STATE_FAIL, "设备用户已经配置", null);
        }
        else
        {
            String deviceConfigId = deviceConfigDao.insert(deviceConfig);
            return new Result(Result.STATE_SUCESS, "保存成功。", deviceConfigId);
        }*/
        String deviceConfigId = deviceConfigDao.insert(deviceConfig);
        return new Result(Result.STATE_SUCESS, "保存成功。", deviceConfigId);
    }

    /**
     * 修改设备用户配置数据
     * @author h
     * @param
     * @return 
     * 2015.6.13
     * */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result updateUserNum(DeviceConfig deviceConfig) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        deviceConfig.setUpdateTime(now);
        String configId = deviceConfig.getId();
        String configCode = deviceConfig.getDeviceTypeCode();
        String userName = deviceConfig.getUserName();
        if (StringUtil.isNotEmpty(userName) && deviceConfigDao.getUserNameExist(userName, configCode, configId))
        {
            return new Result(Result.STATE_FAIL, "按键名称重复。", null);
        }
        String configVal = deviceConfig.getVal();
        if (StringUtil.isNotEmpty(configVal) && deviceConfigDao.getValExist(configVal, configCode, configId))
        {
            return new Result(Result.STATE_FAIL, "按键值重复。", null);
        }
        Integer seq = deviceConfig.getSeq();
        if (seq != null && deviceConfigDao.getSeqExist(seq, configCode, configId))
        {
            return new Result(Result.STATE_FAIL, "顺序号重复。", null);
        }
        int i = deviceConfigDao.updateDevice(deviceConfig);
        if (i > 0)
        {
            return new Result(Result.STATE_SUCESS, "修改成功。", i);
        }
        else
        {
            return new Result(Result.STATE_FAIL, "修改失败。", i);
        }

    }

    /**
     * 查询设备基础数据
     * @author h
     * @param
     * @return 
     * 2015.6.13
     * */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<DeviceConfig> queryDeviceByPage(Map<String, Object> filter, int page, int pageSize)
    {
        return deviceConfigDao.queryDeviceByPage(filter, page, pageSize);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public DeviceConfig getConfig(String code, String val)
    {
        return deviceConfigDao.getConfig(code, val);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<DeviceConfig> loadDataTran(String code)
    {
        return deviceConfigDao.getDeviceConfigs(code);
    }

}
