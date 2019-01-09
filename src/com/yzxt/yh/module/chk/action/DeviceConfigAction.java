package com.yzxt.yh.module.chk.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yzxt.fw.common.BaseAction;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.module.chk.bean.DeviceConfig;
import com.yzxt.yh.module.chk.service.DeviceConfigService;
import com.yzxt.yh.module.msg.bean.AskCatalog;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.StringUtil;

import common.Logger;

/**
 * 设备基础数据
 * @author h
 * @return
 */
public class DeviceConfigAction extends BaseAction
{
    
    private static final long serialVersionUID = 1L;
    
    private static Logger logger = Logger.getLogger(DeviceConfigAction.class);
    
    private DeviceConfig deviceConfig;
    
    private DeviceConfigService deviceConfigService;
    
    public DeviceConfig getDeviceConfig()
    {
        return deviceConfig;
    }

    public void setDeviceConfig(DeviceConfig deviceConfig)
    {
        this.deviceConfig = deviceConfig;
    }

    public DeviceConfigService getDeviceConfigService()
    {
        return deviceConfigService;
    }

    public void setDeviceConfigService(DeviceConfigService deviceConfigService)
    {
        this.deviceConfigService = deviceConfigService;
    }

    /**
     * 配置设备用户数
     * @author h
     * 2015.6.12
     * */
    public void addUserNum()
    {
        Result r = null;
        try
        {
            User operUser = (User) super.getCurrentUser();
            deviceConfig.setCreateBy(operUser.getId());
            deviceConfig.setUpdateBy(operUser.getId());
            r = deviceConfigService.addUserNum(deviceConfig);
            super.write(r);
        }
        catch (Exception e)
        {
            logger.error("添加设备基础数据出错", e);
            super.write("");
        }
    }
    
    /**
     * 修改设备用户数
     * @author h
     * 2015.6.12
     * */
    public void updateUserNum()
    {
        Result r = null;
        try
        {
            User operUser = (User) super.getCurrentUser();
            deviceConfig.setUpdateBy(operUser.getId());
            r = deviceConfigService.updateUserNum(deviceConfig);
            super.write(r);
        }
        catch (Exception e)
        {
            logger.error("修改设备基础数据出错", e);
            super.write("");
        }
    }
    
    /**
     * 查询设备用户数
     * @author h
     * 2015.6.12
     * */
    public void query()
    {
        try
        {
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("user", super.getCurrentUser());
            filter.put("deviceTypeCode", request.getParameter("code"));
            PageModel<DeviceConfig> pageModel = deviceConfigService.queryDeviceByPage(filter, getPage(), getRows());
            super.write(pageModel);
        }
        catch (Exception e)
        {
            logger.error("查询出错", e);
            super.write(e);
        }
    }
    
    /**
     * 用户绑定选择设备后设备配置户
     * */
    public void listConfigByJson()
    {
        String deviceTypeCode = request.getParameter("code");
        List<DeviceConfig> devConfigs = null;
        if (StringUtil.isNotEmpty(deviceTypeCode))
        {
            devConfigs = deviceConfigService.loadDataTran(deviceTypeCode);
        }
        JsonArray retJson = new JsonArray();
        if (devConfigs != null && devConfigs.size() > 0)
        {
            for (DeviceConfig config : devConfigs)
            {
                JsonObject jsonObj = new JsonObject();
                jsonObj.addProperty("id", config.getId());
                jsonObj.addProperty("configName", config.getUserName());
                jsonObj.addProperty("configVal", config.getVal());
                retJson.add(jsonObj);
            }
        }
        super.write(retJson);
    }
    

}
