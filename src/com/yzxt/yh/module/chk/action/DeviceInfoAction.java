package com.yzxt.yh.module.chk.action;

import java.util.HashMap;
import java.util.Map;

import com.yzxt.fw.common.BaseAction;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.module.chk.bean.DeviceConfig;
import com.yzxt.yh.module.chk.bean.DeviceInfo;
import com.yzxt.yh.module.chk.service.DeviceInfoService;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.StringUtil;

import common.Logger;

/**
 * 设备基础数据
 * @author h
 * @return
 */
public class DeviceInfoAction extends BaseAction
{
    
    private static final long serialVersionUID = 1L;
    
    private static Logger logger = Logger.getLogger(DeviceInfoAction.class);
    
    private DeviceInfo deviceInfo;
    
    private DeviceInfoService deviceInfoService;
    
    public DeviceInfo getDeviceInfo()
    {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo)
    {
        this.deviceInfo = deviceInfo;
    }

    public DeviceInfoService getDeviceInfoService()
    {
        return deviceInfoService;
    }

    public void setDeviceInfoService(DeviceInfoService deviceInfoService)
    {
        this.deviceInfoService = deviceInfoService;
    }

    /**
     * 添加设备基础数据
     * @author h
     * 2015.6.12
     * */
    public void addEquipment()
    {
        Result r = null;
        try
        {
            User operUser = (User) super.getCurrentUser();
            deviceInfo.setCreateBy(operUser.getId());
            deviceInfo.setUpdateBy(operUser.getId());
            r = deviceInfoService.addEquipment(deviceInfo);
            super.write(r);
        }
        catch (Exception e)
        {
            logger.error("添加设备基础数据出错", e);
            super.write("");
        }
    }
    
    /**
     * 添加设备基础数据
     * @author h
     * 2015.6.12
     * */
    public void updateEquipment()
    {
        Result r = null;
        try
        {
            User operUser = (User) super.getCurrentUser();
            deviceInfo.setUpdateBy(operUser.getId());
            r = deviceInfoService.updateEquipment(deviceInfo);
            super.write(r);
        }
        catch (Exception e)
        {
            logger.error("修改设备基础数据出错", e);
            super.write("");
        }
    }
    
    /**
     * 查询设备基础数据
     * @author h
     * 2015.6.12
     * */
    public void query()
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("user", super.getCurrentUser());
            // 查询条件
            String deviceCode = request.getParameter("deviceCode");
            String deviceName = request.getParameter("deviceName");
            String checkType = request.getParameter("checkType");
            filter.put("deviceCode", StringUtil.trim(deviceCode));
            filter.put("deviceName", StringUtil.trim(deviceName));
            filter.put("checkType", StringUtil.trim(checkType));
            PageModel<DeviceInfo> pageModel = deviceInfoService.queryDeviceByPage(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("查询出错", e);
            super.write(e);
        }
    }
    
    /**
     * 跳转到设备用户数
     * @author h
     * 2015.6.12
     * */
    public String showDetails()
    {
        try
        {
            String code = request.getParameter("code");
            DeviceInfo di = deviceInfoService.getDevice(code);
            request.setAttribute("deviceInfo", di);
            return "detail";
        }
        catch (Exception e)
        {
            logger.error("跳转设备用户数失败", e);
        }
        return null;
    }

}
