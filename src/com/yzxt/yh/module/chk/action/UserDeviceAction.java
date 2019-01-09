package com.yzxt.yh.module.chk.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yzxt.fw.common.BaseAction;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.yh.module.chk.bean.DeviceConfig;
import com.yzxt.yh.module.chk.bean.DeviceInfo;
import com.yzxt.yh.module.chk.bean.UserDevice;
import com.yzxt.yh.module.chk.service.DeviceConfigService;
import com.yzxt.yh.module.chk.service.DeviceInfoService;
import com.yzxt.yh.module.chk.service.UserDeviceService;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.StringUtil;
import common.Logger;

public class UserDeviceAction extends BaseAction
{
    private static final long serialVersionUID = 1L;

    Logger logger = Logger.getLogger(UserDeviceAction.class);

    private UserDevice userDevice;

    private UserDeviceService userDeviceService;

    private DeviceInfoService deviceInfoService;

    private DeviceConfigService deviceConfigService;

    public UserDevice getUserDevice()
    {
        return userDevice;
    }

    public void setUserDevice(UserDevice userDevice)
    {
        this.userDevice = userDevice;
    }

    public UserDeviceService getUserDeviceService()
    {
        return userDeviceService;
    }

    public void setUserDeviceService(UserDeviceService userDeviceService)
    {
        this.userDeviceService = userDeviceService;
    }

    public DeviceInfoService getDeviceInfoService()
    {
        return deviceInfoService;
    }

    public void setDeviceInfoService(DeviceInfoService deviceInfoService)
    {
        this.deviceInfoService = deviceInfoService;
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
     * 跳转到编辑页面
     * @return
     * @throws Exception 
     */
    public String toEdit() throws Exception
    {
        String id = request.getParameter("id");
        User user = (User) super.getCurrentUser();
        Map<String, Object> filter = new HashMap<String, Object>();
        List<DeviceInfo> deviceInfos = deviceInfoService.loadDataTran(filter);
        /*List<DeviceConfig> deviceConfigs = deviceConfigService.loadDataTran(deviceInfos.get(0).getCode());*/
        if (StringUtil.isEmpty(id))
        {
            UserDevice userDevice = new UserDevice();
            /* userDevice.setIdCard(user.getIdCard());*/
            List<DeviceConfig> deviceConfigs = new ArrayList<DeviceConfig>();
            request.setAttribute("deviceConfigs", deviceConfigs);
            userDevice.setCustId(user.getId());
            request.setAttribute("userDevice", userDevice);
            request.setAttribute("operType", "add");
        }
        else
        {
            UserDevice userDevice = userDeviceService.load(id);
            List<DeviceConfig> deviceConfigs = deviceConfigService.loadDataTran(userDevice.getDeviceType());
            request.setAttribute("deviceConfigs", deviceConfigs);
            request.setAttribute("userDevice", userDevice);
            request.setAttribute("operType", "update");
        }
        request.setAttribute("deviceInfos", deviceInfos);
        return "edit";
    }

    /**
     * 添加用户设备绑定信息
     * 
     * @return
     */
    public void add()
    {
        int state = Result.STATE_UNKNOWN;
        String msg = "";
        String newId = null;
        try
        {
            // 用户
            User user = (User) getCurrentUser();
            userDevice.setCreateBy(user.getId());
            Result r = userDeviceService.add(userDevice);
            newId = (String) r.getData();
            msg = r.getMsg();
            state = r.getState();
        }
        catch (Exception e)
        {
            state = Result.STATE_FAIL;
            msg = "保存失败！";
            logger.error(msg, e);
        }
        Result result = new Result();
        result.setState(state);
        result.setMsg(msg);
        result.setData(newId);
        super.write(result);
    }

    /**
     * 修改用户设备绑定信息
     * 
     * @return
     * @throws Exception
     */
    public void update()
    {
        int state = Result.STATE_UNKNOWN;
        String msg = "";
        try
        {
            // 用户
            User user = (User) getCurrentUser();
            userDevice.setUpdateBy(user.getId());
            Result r = userDeviceService.update(userDevice);
            state = r.getState();
            msg = r.getMsg();
        }
        catch (Exception e)
        {
            state = Result.STATE_FAIL;
            msg = "修改出错！";
            logger.error(msg, e);
        }
        Result result = new Result();
        result.setState(state);
        result.setMsg(msg);
        super.write(result);
    }

    /**
     * 删除用户设备绑定信息
     * 
     * @return
     * @throws Exception
     */
    public void delete()
    {
        int state = Result.STATE_UNKNOWN;
        String msg = "";
        try
        {
            String idsStr = request.getParameter("ids");
            String[] ids = StringUtil.splitWithoutEmpty(idsStr, ",");
            for (String id : ids)
            {
                userDeviceService.delete(id);
            }
            state = Result.STATE_SUCESS;
        }
        catch (Exception e)
        {
            state = Result.STATE_FAIL;
            msg = "删除出错！";
            logger.error(msg, e);
        }
        Result result = new Result();
        result.setState(state);
        result.setMsg(msg);
        super.write(result);
    }

    /**
     * 分页查询
     * 
     * @param filter
     * @param page
     * @param pageSize
     * @return
     */
    public void queryUserDevices()
    {
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put("user", super.getCurrentUser());
        // 查询条件
        String deviceType = request.getParameter("deviceType");
        String deviceSn = request.getParameter("deviceSn");
        String custName = request.getParameter("userName");
        filter.put("deviceType", StringUtil.trim(deviceType));
        filter.put("deviceSn", StringUtil.trim(deviceSn));
        filter.put("custName", StringUtil.trim(custName));
        // 查询人
        filter.put("operUser", super.getCurrentUser());
        PageModel<UserDevice> pageModel = userDeviceService.queryUserDeviceByPage(filter, getPage(), getRows());
        super.write(pageModel);
    }

    public String toOtherEdit() throws Exception
    {
        String id = request.getParameter("id");
        User user = (User) super.getCurrentUser();
        Map<String, Object> filter = new HashMap<String, Object>();
        List<DeviceInfo> deviceInfos = deviceInfoService.loadDataTran(filter);
        /*List<DeviceConfig> deviceConfigs = deviceConfigService.loadDataTran(deviceInfos.get(0).getCode());*/
        if (StringUtil.isEmpty(id))
        {
            UserDevice userDevice = new UserDevice();
            /* userDevice.setIdCard(user.getIdCard());*/
            List<DeviceConfig> deviceConfigs = new ArrayList<DeviceConfig>();
            request.setAttribute("deviceConfigs", deviceConfigs);
            userDevice.setCustId(user.getId());
            request.setAttribute("userDevice", userDevice);
            request.setAttribute("operType", "add");
        }
        else
        {
            UserDevice userDevice = userDeviceService.load(id);
            List<DeviceConfig> deviceConfigs = deviceConfigService.loadDataTran(userDevice.getDeviceType());
            request.setAttribute("deviceConfigs", deviceConfigs);
            request.setAttribute("userDevice", userDevice);
            request.setAttribute("operType", "update");
        }
        request.setAttribute("deviceInfos", deviceInfos);
        return "add";
    }
}
