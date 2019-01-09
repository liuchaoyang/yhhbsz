package com.yzxt.yh.module.chk.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.module.chk.bean.DeviceInfo;
import com.yzxt.yh.module.chk.dao.DeviceInfoDao;
import com.yzxt.yh.util.StringUtil;

@Transactional(ConstTM.DEFAULT)
public class DeviceInfoService
{
    private DeviceInfoDao deviceInfoDao;

    public DeviceInfoDao getDeviceInfoDao()
    {
        return deviceInfoDao;
    }

    public void setDeviceInfoDao(DeviceInfoDao deviceInfoDao)
    {
        this.deviceInfoDao = deviceInfoDao;
    }

    /**
     * 添加设备基础数据
     * @author h
     * @param
     * @return 
     * 2015.6.13
     * */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result addEquipment(DeviceInfo deviceInfo) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        deviceInfo.setCreateTime(now);
        deviceInfo.setUpdateTime(now);
        //判定设备是否存在
        DeviceInfo di = deviceInfoDao.get(deviceInfo.getCode());
        if (di != null)
        {
            return new Result(Result.STATE_FAIL, "设备已经添加", null);
        }
        else
        {
            String deviceInfoCode = deviceInfoDao.insert(deviceInfo);
            return new Result(Result.STATE_SUCESS, "保存成功。", deviceInfoCode);
        }
    }

    /**
     * 修改设备基础数据
     * @author h
     * @param
     * @return 
     * 2015.6.13
     * */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result updateEquipment(DeviceInfo deviceInfo) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        deviceInfo.setUpdateTime(now);
        String infoId = deviceInfo.getCode();
        String name = deviceInfo.getName();
        //判断修改是否重名
        if (StringUtil.isNotEmpty(name) && deviceInfoDao.getNameExist(name, infoId))
        {
            return new Result(Result.STATE_FAIL, "设备名称相同。", null);
        }
        int i = deviceInfoDao.updateDevice(deviceInfo);
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
    public PageModel<DeviceInfo> queryDeviceByPage(Map<String, Object> filter, int page, int pageSize)
    {
        return deviceInfoDao.queryDeviceByPage(filter, page, pageSize);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public DeviceInfo getDevice(String code)
    {
        return deviceInfoDao.get(code);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<DeviceInfo> loadDataTran(Map<String, Object> filter)
    {
        return deviceInfoDao.getDeviceInfos(filter);
    }

}
