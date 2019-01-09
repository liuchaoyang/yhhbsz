package com.yzxt.tran;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.common.ResultTran;
import com.yzxt.fw.util.GsonUtil;
import com.yzxt.yh.module.chk.bean.DeviceConfig;
import com.yzxt.yh.module.chk.bean.DeviceInfo;
import com.yzxt.yh.module.chk.bean.UserDevice;
import com.yzxt.yh.module.chk.service.DeviceConfigService;
import com.yzxt.yh.module.chk.service.DeviceInfoService;
import com.yzxt.yh.module.chk.service.UserDeviceService;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.StringUtil;
import common.Logger;

/**
 * 设备接口类
 * @author huangGang
 * 2015.4.29
 */
public class DeviceTranAction extends BaseTranAction
{
    private static final long serialVersionUID = 1L;

    private Logger logger = Logger.getLogger(DeviceTranAction.class);

    private UserDeviceService userDeviceService;

    private DeviceInfoService deviceInfoService;

    private DeviceConfigService deviceConfigService;

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
     * 获取已绑定的设备数据接口
     * @author huangGang
     * 2015.4.29
     * */
    public void getPersonalList()
    {
        try
        {
            JsonObject paramJson = super.getParams();
            if (paramJson == null)
            {
                return;
            }
            int pageSize = paramJson.get("pageSize").getAsInt();
            String userId = GsonUtil.toString(paramJson.get("userId"));
            String custId = GsonUtil.toString(paramJson.get("custId"));

            Timestamp synTime = DateUtil.getSynTimeByStr(GsonUtil.toString(paramJson.get("lastSynTime")));
            JsonArray retJson = new JsonArray();
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("custId", custId);
            filter.put("userId", userId);
            // 拉取时间
            filter.put("sysTime", synTime);
            filter.put("maxSize", pageSize);
            List<UserDevice> userDevices = userDeviceService.queryDeviceTran(filter);
            if (userDevices != null && userDevices.size() > 0)
            {
                for (UserDevice userDevice : userDevices)
                {
                    JsonObject jsonObj = new JsonObject();
                    jsonObj.addProperty("userDeviceId", userDevice.getId());

                    jsonObj.addProperty("deviceCode", userDevice.getDeviceSn());
                    jsonObj.addProperty("deviceVal", userDevice.getDeviceSnExt());
                    jsonObj.addProperty("deviceName", userDevice.getDeviceName());
                    jsonObj.addProperty("deviceUser", StringUtil.ensureStringNotNull(userDevice.getDeviceUser()));
                    /*if (userDevice.getDeviceType().equals("BP-U-GPRS"))
                    {
                        jsonObj.addProperty("deviceId", "12345678");
                        jsonObj.addProperty("deviceName", "中兴电子血压计");
                    }
                    else if (userDevice.getDeviceType().equals("BP_IAL"))
                    {
                        jsonObj.addProperty("deviceId", "12345678910");
                        jsonObj.addProperty("deviceName", "爱奥乐电子血压计");
                    }
                    else if (userDevice.getDeviceType().equals("SG_IAL"))
                    {
                        jsonObj.addProperty("deviceId", "12345678911");
                        jsonObj.addProperty("deviceName", "爱奥乐电子血糖计");
                    }*/
                    jsonObj.addProperty("deviceImg", "");
                    /* String startTimeStr = null;
                     if (userDevice.getUpdateTime() != null)
                     {
                         startTimeStr = DateUtil.toSynTimeStr(new Timestamp(userDevice.getUpdateTime().getTime()));
                         startTimeStr = startTimeStr.replace('-', '/');
                         startTimeStr = startTimeStr.substring(0, 10);
                     }
                     jsonObj.addProperty("intakeTime", startTimeStr);*/
                    jsonObj.addProperty("deviceType", userDevice.getCheckType());
                    jsonObj.addProperty("custId", userDevice.getCustId());
                    jsonObj.addProperty("custName", userDevice.getCustName());
                    retJson.add(jsonObj);
                }
            }
            super.write(ResultTran.STATE_OK, "成功", retJson);
        }
        catch (Exception e)
        {
            logger.error("客户端刷新失败.", e);
            super.write(ResultTran.STATE_ERROR, "失败", null);
        }
    }

    /**
     * 客户端绑定设备
     * @author huangGang
     * 2015.4.29
     * */
    public void addBinding()
    {
        JsonObject paramJson = super.getParams();
        try
        {
            String userId = GsonUtil.toString(paramJson.get("userId"));
            //deviceId 设备id 查询设备基础表  查询出设备类型 然后插入
            String deviceId = GsonUtil.toString(paramJson.get("deviceId"));
            String deviceType = deviceId;
            //deviceCode 设备编码对应数据表中的device_sn（设备标志）
            // deviceUser 设备用户编码对应数据表中的device_sn_ext（设备标志） 
            String deviceSn = GsonUtil.toString(paramJson.get("deviceCode"));
            String deviceSnExt = GsonUtil.toString(paramJson.get("deviceUser"));
            // 如果是康康血压计，需要从康康服务器获取设备编号（目前deviceSn的值为二维码信息）
            // if("qrCode".equals(paramJson.get("bingType").getAsString())
            //先判断设备是否存在(不要判定)  然后判断是否绑定，然后绑定
            Map<String, Object> filter = new HashMap<String, Object>();
            filter.put("custId", userId);
            filter.put("deviceType", deviceType);
            filter.put("deviceSn", deviceSn);
            filter.put("deviceSnExt", deviceSnExt);
            boolean isExist = userDeviceService.getIsBind(filter);
            if (isExist)
            {
                super.write(ResultTran.STATE_ERROR, "设备已绑定", null);
                return;
            }
            else
            {
                UserDevice addBinding = new UserDevice();
                addBinding.setCustId(userId);
                addBinding.setDeviceSn(deviceSn);
                addBinding.setDeviceSnExt(deviceSnExt);
                addBinding.setDeviceType(deviceType);
                addBinding.setCreateBy(userId);
                addBinding.setUpdateBy(userId);
                Result r = userDeviceService.add(addBinding);
                if (Result.STATE_SUCESS == r.getState())
                {
                    super.write(ResultTran.STATE_OK, "绑定成功", null);
                    return;
                }
                else
                {
                    super.write(ResultTran.STATE_ERROR, StringUtil.isNotEmpty(r.getMsg()) ? r.getMsg() : "绑定失败", null);
                    return;
                }
            }
        }
        catch (Exception e)
        {
            logger.error("客户端设备绑定失败.", e);
            super.write(ResultTran.STATE_ERROR, "绑定错误", null);
        }
    }

    /**
     * 删除客户端绑定数据
     * @author huangGang
     */
    public void deteleBind() throws Exception
    {
        try
        {
            JsonObject paramJson = getParams();
            if (paramJson == null)
            {
                return;
            }
            // User user = super.getOperUser();
            // String userId = GsonUtil.toString(paramJson.get("userId"));
            String userDeviceId = GsonUtil.toString(paramJson.get("userDeviceId"));
            JsonArray retJson = new JsonArray();
            userDeviceService.delete(userDeviceId);
            super.write(ResultTran.STATE_OK, "删除成功", retJson);
        }
        catch (Exception e)
        {
            logger.error("客户端删除失败.", e);
            super.write(ResultTran.STATE_ERROR, "删除失败", null);
        }
    }

    /**
     * 加载绑定的基础数据
     * @author huangGang
     * 2015.4.30
     */
    public void loadBindData() throws Exception
    {
        try
        {
            JsonObject paramJson = getParams();
            if (paramJson == null)
            {
                return;
            }
            JsonArray retJosn = new JsonArray();
            Map<String, Object> filter = new HashMap<String, Object>();
            List<DeviceInfo> deviceInfos = deviceInfoService.loadDataTran(filter);
            if (deviceInfos != null && deviceInfos.size() > 0)
            {
                for (DeviceInfo deviceInfo : deviceInfos)
                {
                    JsonObject jsonObj = new JsonObject();
                    jsonObj.addProperty("id", deviceInfo.getCode());
                    jsonObj.addProperty("deviceName", deviceInfo.getName());
                    jsonObj.addProperty("deviceModel", deviceInfo.getCode());
                    jsonObj.addProperty("bingType", "");
                    List<DeviceConfig> deviceConfigs = deviceConfigService.loadDataTran(deviceInfo.getCode());
                    JsonArray cJosn = new JsonArray();
                    for (DeviceConfig deviceConfig : deviceConfigs)
                    {
                        JsonObject jsonConfig = new JsonObject();
                        jsonConfig.addProperty("deviceUser", deviceConfig.getUserName());
                        jsonConfig.addProperty("deviceVal", deviceConfig.getVal());
                        cJosn.add(jsonConfig);
                    }
                    jsonObj.add("deviceUserData", cJosn);
                    jsonObj.addProperty("deviceType", deviceInfo.getCheckType());
                    jsonObj.addProperty("state", deviceInfo.getState());
                    retJosn.add(jsonObj);
                }
            }
            super.write(ResultTran.STATE_OK, "获取基础设备数据成功", retJosn);
        }
        catch (Exception e)
        {
            logger.error("客户端获取基础设备数据失败.", e);
            super.write(ResultTran.STATE_ERROR, "获取基础设备数据失败", null);
        }
    }

}
