package com.yzxt.tran;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.common.ResultTran;
import com.yzxt.fw.pager.PageTran;
import com.yzxt.fw.util.GsonUtil;
import com.yzxt.yh.constant.ConstFilePath;
import com.yzxt.yh.module.cli.bean.CustPosition;
import com.yzxt.yh.module.cli.bean.WbConfig;
import com.yzxt.yh.module.cli.service.CustPositionService;
import com.yzxt.yh.module.cli.service.WbConfigService;
import com.yzxt.yh.module.sys.bean.FileDesc;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.FileUtil;
import com.yzxt.yh.util.StringUtil;

/**
* 腕表配置接口类：主要的接口包括十三个大类的接口，总计四十个接口：包括查询和上传（设置）
* 2015.11.24
* @author h
*/
public class WbConfigTranAction extends BaseTranAction
{
    private static final long serialVersionUID = 1L;

    private static Logger logger = Logger.getLogger(WbConfigTranAction.class);

    public WbConfigService wbConfigService;

    public CustPositionService custPositionService;

    public WbConfigService getWbConfigService()
    {
        return wbConfigService;
    }

    public void setWbConfigService(WbConfigService wbConfigService)
    {
        this.wbConfigService = wbConfigService;
    }

    public CustPositionService getCustPositionService()
    {
        return custPositionService;
    }

    public void setCustPositionService(CustPositionService custPositionService)
    {
        this.custPositionService = custPositionService;
    }

    /**
     * 1.1.1.1.1    获取腕表身份信息
     * @param userId
     * @param custId 对应的设备绑定人的ID
     * @param deviceId 设备ID-对应一类设备
     * @param deviceCode 设备code-对应具体的一个设备
     * 一个人可以带多个腕表
     * 2015.12.2
     * @author h
     * */
    public void queryWbCard()
    {
        JsonObject jObj = new JsonObject();
        try
        {
            JsonObject paramJson = super.getParams();
            if (paramJson == null)
            {
                super.write(ResultTran.STATE_ERROR, "客户端参数异常", jObj);
            }
            Map<String, Object> filter = new HashMap<String, Object>();
            String userId = GsonUtil.toString(paramJson.get("userId"));

            String custId = GsonUtil.toString(paramJson.get("custId"));
            filter.put("custId", custId);
            String deviceId = GsonUtil.toString(paramJson.get("deviceId"));
            filter.put("deviceId", deviceId);
            String deviceCode = GsonUtil.toString(paramJson.get("deviceCode"));
            filter.put("deviceCode", deviceCode);
            WbConfig wbConfig = wbConfigService.queryConfig(filter);
            if (wbConfig != null)
            {
                jObj.addProperty("deviceCode", wbConfig.getId());
                jObj.addProperty("head", wbConfig.getImgFilePath());
                jObj.addProperty("sex", wbConfig.getSex());
                jObj.addProperty("name", wbConfig.getWbName());
                jObj.addProperty("height", wbConfig.getHeight());
                jObj.addProperty("name", wbConfig.getWbName());
                jObj.addProperty("weight", wbConfig.getWeight());
                if (wbConfig.getBirthday() != null)
                {
                    String birthdaySr = (wbConfig.getBirthday().toString()).replace('-', '/');
                    jObj.addProperty("birthday", birthdaySr);
                }
                else
                {
                    jObj.addProperty("birthday", "");
                }
                jObj.addProperty("step", wbConfig.getStep());
                super.write(ResultTran.STATE_OK, "获取数据成功", jObj);
            }
            else
            {
                super.write(ResultTran.STATE_OK, "此腕表信息未上传", jObj);
            }
        }
        catch (Exception e)
        {
            logger.error("服务端异常,获取数据失败", e);
            super.write(ResultTran.STATE_ERROR, "服务端异常", jObj);
        }
    }

    /**
     * 1.1.1.1.2    设置腕表身份信息
     * @param userId
     * @param custId 对应的设备绑定人的ID
     * @param deviceId 设备ID-对应一类设备
     * @param deviceCode 设备code-对应具体的一个设备
     * 一个人可以带多个腕表
     * 2015.12.2
     * @author h
     * */
    public void updateWbCard()
    {
        Result r = null;
        JsonObject jObj = new JsonObject();
        try
        {
            JsonObject paramJson = super.getParams();
            if (paramJson == null)
            {
                super.write(ResultTran.STATE_ERROR, "客户端参数异常", jObj);
            }
            String userId = GsonUtil.toString(paramJson.get("userId"));
            String wbName = GsonUtil.toString(paramJson.get("name"));
            String custId = GsonUtil.toString(paramJson.get("custId"));
            String deviceId = GsonUtil.toString(paramJson.get("deviceId"));
            String deviceCode = GsonUtil.toString(paramJson.get("deviceCode"));
            String headIcon = GsonUtil.toString(paramJson.get("head"));
            String imgPath = "";
            Integer sex = GsonUtil.toInteger(paramJson.get("sex"));
            Double height = GsonUtil.toDouble(paramJson.get("height"));
            Double weight = GsonUtil.toDouble(paramJson.get("weight"));
            Date birthday = DateUtil.getFromTranDate(GsonUtil.toString(paramJson.get("birthday")));
            Integer step = GsonUtil.toInteger(paramJson.get("step"));
            WbConfig bean = new WbConfig();
            bean.setId(deviceCode);
            bean.setCustId(custId);
            bean.setWbName(wbName);
            bean.setDeviceId(deviceId);
            bean.setSex(sex);
            bean.setHeight(height);
            bean.setWeight(weight);
            bean.setBirthday(birthday);
            bean.setStep(step);
            bean.setUpdateBy(userId);
            FileDesc imgFileDesc = null;
            if (StringUtil.isNotEmpty(headIcon))
            {
                String[] fileInfo = SysTransAction.decodeFileInfo(headIcon);
                if (fileInfo != null)
                {
                    String temFileFullPath = FileUtil.getFullPath(fileInfo[0]);
                    File temFile = new File(temFileFullPath);
                    if (temFile.exists())
                    {
                        imgFileDesc = FileUtil.save(temFile, fileInfo[1], ConstFilePath.USER_IMG_FOLDER, true);
                        imgPath = "pub/cf_img.do?pt=" + FileUtil.encodePath(imgFileDesc.getPath());
                    }
                }
            }
            r = wbConfigService.updateWbCard(bean, imgFileDesc);
            System.out.println(r.getState());
            super.write(String.valueOf(r.getState()), r.getMsg(), jObj);
        }
        catch (Exception e)
        {
            logger.error("服务端异常,获取数据失败", e);
            super.write(ResultTran.STATE_ERROR, "服务端异常", jObj);
        }
    }

    /**
     * 1.1.1.2.1 获取心率所有设置参数
     * @param userId
     * @param custId 对应的设备绑定人的ID
     * @param deviceId 设备ID-对应一类设备
     * @param deviceCode 设备code-对应具体的一个设备
     * 一个人可以带多个腕表
     * 2015.11.25
     * @author h
     * */
    public void acquireWbPluse()
    {
        JsonObject jObj = new JsonObject();
        try
        {
            JsonObject paramJson = super.getParams();
            if (paramJson == null)
            {
                super.write(ResultTran.STATE_ERROR, "客户端参数异常", jObj);
            }
            Map<String, Object> filter = new HashMap<String, Object>();
            String userId = GsonUtil.toString(paramJson.get("userId"));

            String custId = GsonUtil.toString(paramJson.get("custId"));
            filter.put("custId", custId);
            String deviceId = GsonUtil.toString(paramJson.get("deviceId"));
            filter.put("deviceId", deviceId);
            String deviceCode = GsonUtil.toString(paramJson.get("deviceCode"));
            filter.put("deviceCode", deviceCode);
            WbConfig wbConfig = wbConfigService.queryConfig(filter);
            if (wbConfig != null)
            {
                jObj.addProperty("deviceCode", wbConfig.getId());
                jObj.addProperty("maxPulse", wbConfig.getPluseMax());
                jObj.addProperty("minPulse", wbConfig.getPluseMin());
                jObj.addProperty("startTime", wbConfig.getPluseStartTime());
                jObj.addProperty("endTime", wbConfig.getPluseEndTime());
                jObj.addProperty("span", wbConfig.getPluseSpan());
                jObj.addProperty("warning", wbConfig.getPluseWarning());
                super.write(ResultTran.STATE_OK, "获取数据成功", jObj);
            }
            else
            {
                super.write(ResultTran.STATE_OK, "腕表心率未设置", jObj);
            }
        }
        catch (Exception e)
        {
            logger.error("服务端异常,获取数据失败", e);
            super.write(ResultTran.STATE_ERROR, "服务端异常", jObj);
        }
    }

    /**
     * 1.1.1.2.3  设置定时检测参数接口
     * @param userId
     * @param custId 对应的设备绑定人的ID
     * @param deviceId 设备ID-对应一类设备
     * @param deviceCode 设备code-对应具体的一个设备
     * 一个人可以带多个腕表
     * 2015.11.25
     * @author h
     * */
    public void updateWbPluse()
    {
        Result r = null;
        JsonObject jObj = new JsonObject();
        try
        {
            JsonObject paramJson = super.getParams();
            if (paramJson == null)
            {
                super.write(ResultTran.STATE_ERROR, "客户端参数异常", jObj);
            }
            String userId = GsonUtil.toString(paramJson.get("userId"));
            String custId = GsonUtil.toString(paramJson.get("custId"));
            String deviceId = GsonUtil.toString(paramJson.get("deviceId"));
            String deviceCode = GsonUtil.toString(paramJson.get("deviceCode"));
            String pluseStartTime = GsonUtil.toString(paramJson.get("startTime"));
            String pluseEndTime = GsonUtil.toString(paramJson.get("endTime"));
            Integer pluseSpan = GsonUtil.toInteger(paramJson.get("span"));
            WbConfig bean = new WbConfig();
            bean.setId(deviceCode);
            bean.setCustId(custId);
            bean.setDeviceId(deviceId);
            bean.setPluseStartTime(pluseStartTime);
            bean.setPluseEndTime(pluseEndTime);
            bean.setPluseSpan(pluseSpan);
            bean.setUpdateBy(userId);
            r = wbConfigService.updateWbPluse(bean);
            super.write(String.valueOf(r.getState()), r.getMsg(), jObj);
        }
        catch (Exception e)
        {
            logger.error("服务端异常,设置参数数据失败", e);
            super.write(ResultTran.STATE_ERROR, "服务端异常", jObj);
        }
    }

    /**
     * 1.1.1.3.1  获取计步所有设置参数
     * @param userId
     * @param custId 对应的设备绑定人的ID
     * @param deviceId 设备ID-对应一类设备
     * @param deviceCode 设备code-对应具体的一个设备
     * 一个人可以带多个腕表
     * 2015.11.25
     * @author h
     * */
    public void acquireWbStep()
    {
        JsonObject jObj = new JsonObject();
        try
        {
            JsonObject paramJson = super.getParams();
            if (paramJson == null)
            {
                super.write(ResultTran.STATE_ERROR, "客户端参数异常", jObj);
            }
            Map<String, Object> filter = new HashMap<String, Object>();
            String userId = GsonUtil.toString(paramJson.get("userId"));
            String custId = GsonUtil.toString(paramJson.get("custId"));
            filter.put("custId", custId);
            String deviceId = GsonUtil.toString(paramJson.get("deviceId"));
            filter.put("deviceId", deviceId);
            String deviceCode = GsonUtil.toString(paramJson.get("deviceCode"));
            filter.put("deviceCode", deviceCode);
            WbConfig wbConfig = wbConfigService.queryConfig(filter);
            if (wbConfig != null)
            {
                jObj.addProperty("deviceCode", wbConfig.getId());
                jObj.addProperty("stepTaget", wbConfig.getStepTarget());
                jObj.addProperty("startTime", wbConfig.getStepStartTime());
                jObj.addProperty("endTime", wbConfig.getStepEndTime());
                jObj.addProperty("span", wbConfig.getStepSpan());
                super.write(ResultTran.STATE_OK, "获取数据成功", jObj);
            }
            else
            {
                super.write(ResultTran.STATE_OK, "此腕表计步参数未设置", jObj);
            }
        }
        catch (Exception e)
        {
            logger.error("服务端异常,获取数据失败", e);
            super.write(ResultTran.STATE_ERROR, "服务端异常", jObj);
        }
    }

    /**
     * 1.1.1.2.3  设置定时检测参数接口
     * @param userId
     * @param custId 对应的设备绑定人的ID
     * @param deviceId 设备ID-对应一类设备
     * @param deviceCode 设备code-对应具体的一个设备
     * 一个人可以带多个腕表
     * 2015.11.25
     * @author h
     * */
    public void updateWbStep()
    {
        Result r = null;
        JsonObject jObj = new JsonObject();
        try
        {
            JsonObject paramJson = super.getParams();
            if (paramJson == null)
            {
                super.write(ResultTran.STATE_ERROR, "客户端参数异常", jObj);
            }
            String userId = GsonUtil.toString(paramJson.get("userId"));
            String custId = GsonUtil.toString(paramJson.get("custId"));
            String deviceId = GsonUtil.toString(paramJson.get("deviceId"));
            String deviceCode = GsonUtil.toString(paramJson.get("deviceCode"));
            Integer stepTaget = GsonUtil.toInteger(paramJson.get("stepTaget"));
            WbConfig bean = new WbConfig();
            bean.setId(deviceCode);
            bean.setCustId(custId);
            bean.setDeviceId(deviceId);
            bean.setStepTarget(stepTaget);
            bean.setUpdateBy(userId);
            r = wbConfigService.updateStepTarget(bean);
            super.write(String.valueOf(r.getState()), r.getMsg(), jObj);
        }
        catch (Exception e)
        {
            logger.error("服务端异常,获取数据失败", e);
            super.write(ResultTran.STATE_ERROR, "服务端异常", jObj);
        }
    }

    /**
     * 1.1.1.3.3    设置定时记步参数接口
     * @param userId
     * @param custId 对应的设备绑定人的ID
     * @param deviceId 设备ID-对应一类设备
     * @param deviceCode 设备code-对应具体的一个设备
     * 一个人可以带多个腕表
     * 2015.11.27
     * @author h
     * */
    public void updateStepConfig()
    {
        Result r = null;
        JsonObject jObj = new JsonObject();
        try
        {
            JsonObject paramJson = super.getParams();
            if (paramJson == null)
            {
                super.write(ResultTran.STATE_ERROR, "客户端参数异常", jObj);
            }
            String userId = GsonUtil.toString(paramJson.get("userId"));
            String custId = GsonUtil.toString(paramJson.get("custId"));
            String deviceId = GsonUtil.toString(paramJson.get("deviceId"));
            String deviceCode = GsonUtil.toString(paramJson.get("deviceCode"));
            String stepStartTime = GsonUtil.toString(paramJson.get("startTime"));
            String stepEndTime = GsonUtil.toString(paramJson.get("endTime"));
            Integer stepSpan = GsonUtil.toInteger(paramJson.get("span"));
            WbConfig bean = new WbConfig();
            bean.setId(deviceCode);
            bean.setCustId(custId);
            bean.setDeviceId(deviceId);
            bean.setStepStartTime(stepStartTime);
            bean.setStepEndTime(stepEndTime);
            bean.setStepSpan(stepSpan);
            bean.setUpdateBy(userId);
            r = wbConfigService.updateWbStep(bean);
            super.write(String.valueOf(r.getState()), r.getMsg(), jObj);
        }
        catch (Exception e)
        {
            logger.error("服务端异常,获取数据失败", e);
            super.write(ResultTran.STATE_ERROR, "服务端异常", jObj);
        }
    }

    /**
     * 1.1.1.4.1    获取定位参数
     * @param userId
     * @param custId 对应的设备绑定人的ID
     * @param deviceId 设备ID-对应一类设备
     * @param deviceCode 设备code-对应具体的一个设备
     * 一个人可以带多个腕表
     * 2015.11.25
     * @author h
     * */
    public void acquireWbPosition()
    {
        JsonObject jObj = new JsonObject();
        try
        {
            JsonObject paramJson = super.getParams();
            if (paramJson == null)
            {
                super.write(ResultTran.STATE_ERROR, "客户端参数异常", jObj);
            }
            Map<String, Object> filter = new HashMap<String, Object>();
            String userId = GsonUtil.toString(paramJson.get("userId"));
            String custId = GsonUtil.toString(paramJson.get("custId"));
            filter.put("custId", custId);
            String deviceId = GsonUtil.toString(paramJson.get("deviceId"));
            filter.put("deviceId", deviceId);
            String deviceCode = GsonUtil.toString(paramJson.get("deviceCode"));
            filter.put("deviceCode", deviceCode);
            WbConfig wbConfig = wbConfigService.queryConfig(filter);
            if (wbConfig != null)
            {
                jObj.addProperty("deviceCode", wbConfig.getId());
                jObj.addProperty("startTime", wbConfig.getPositionStartTime());
                jObj.addProperty("endTime", wbConfig.getPositionEndTime());
                jObj.addProperty("span", wbConfig.getPositionSpan());
                super.write(ResultTran.STATE_OK, "获取数据成功", jObj);
            }
            else
            {
                super.write(ResultTran.STATE_OK, "此腕表定位参数未设置", jObj);
            }
        }
        catch (Exception e)
        {
            logger.error("服务端异常,获取数据失败", e);
            super.write(ResultTran.STATE_ERROR, "服务端异常", jObj);
        }
    }

    /**
     * 1.1.1.4.2    设置定时定位参数
     * @param userId
     * @param custId 对应的设备绑定人的ID
     * @param deviceId 设备ID-对应一类设备
     * @param deviceCode 设备code-对应具体的一个设备
     * 一个人可以带多个腕表
     * 2015.11.27
     * @author h
     * */
    public void updatePositConfig()
    {
        Result r = null;
        JsonObject jObj = new JsonObject();
        try
        {
            JsonObject paramJson = super.getParams();
            if (paramJson == null)
            {
                super.write(ResultTran.STATE_ERROR, "客户端参数异常", jObj);
            }
            String userId = GsonUtil.toString(paramJson.get("userId"));
            String custId = GsonUtil.toString(paramJson.get("custId"));
            String deviceId = GsonUtil.toString(paramJson.get("deviceId"));
            String deviceCode = GsonUtil.toString(paramJson.get("deviceCode"));
            String positionStartTime = GsonUtil.toString(paramJson.get("startTime"));
            String positionEndTime = GsonUtil.toString(paramJson.get("endTime"));
            Integer positionSpan = GsonUtil.toInteger(paramJson.get("span"));
            WbConfig bean = new WbConfig();
            bean.setId(deviceCode);
            bean.setCustId(custId);
            bean.setDeviceId(deviceId);
            bean.setPositionStartTime(positionStartTime);
            bean.setPositionEndTime(positionEndTime);
            bean.setPositionSpan(positionSpan);
            bean.setUpdateBy(userId);
            r = wbConfigService.updatePositConfig(bean);
            super.write(String.valueOf(r.getState()), r.getMsg(), jObj);
        }
        catch (Exception e)
        {
            logger.error("服务端异常,获取数据失败", e);
            super.write(ResultTran.STATE_ERROR, "服务端异常", jObj);
        }
    }

    /**
     * 1.1.1.5.1 获取睡眠参数
     * @param userId
     * @param custId 对应的设备绑定人的ID
     * @param deviceId 设备ID-对应一类设备
     * @param deviceCode 设备code-对应具体的一个设备
     * 一个人可以带多个腕表
     * 2015.11.25
     * @author h
     * */
    public void acquireWbSleep()
    {
        JsonObject jObj = new JsonObject();
        try
        {
            JsonObject paramJson = super.getParams();
            if (paramJson == null)
            {
                super.write(ResultTran.STATE_ERROR, "客户端参数异常", jObj);
            }
            Map<String, Object> filter = new HashMap<String, Object>();
            String userId = GsonUtil.toString(paramJson.get("userId"));
            String custId = GsonUtil.toString(paramJson.get("custId"));
            filter.put("custId", custId);
            String deviceId = GsonUtil.toString(paramJson.get("deviceId"));
            filter.put("deviceId", deviceId);
            String deviceCode = GsonUtil.toString(paramJson.get("deviceCode"));
            filter.put("deviceCode", deviceCode);
            WbConfig wbConfig = wbConfigService.queryConfig(filter);
            if (wbConfig != null)
            {
                jObj.addProperty("deviceCode", wbConfig.getId());
                jObj.addProperty("startTime", wbConfig.getSleepStartTime());
                jObj.addProperty("endTime", wbConfig.getSleepEndTime());
                jObj.addProperty("status", wbConfig.getSleepStatus());
                super.write(ResultTran.STATE_OK, "获取数据成功", jObj);
            }
            else
            {
                super.write(ResultTran.STATE_OK, "此腕表睡眠参数未设置", jObj);
            }
        }
        catch (Exception e)
        {
            logger.error("服务端异常,获取数据失败", e);
            super.write(ResultTran.STATE_ERROR, "服务端异常", jObj);
        }
    }

    /**
     * 1.1.1.5.2 睡眠时间设置
     * @param userId
     * @param custId 对应的设备绑定人的ID
     * @param deviceId 设备ID-对应一类设备
     * @param deviceCode 设备code-对应具体的一个设备
     * 一个人可以带多个腕表
     * 2015.11.27
     * @author h
     * */
    public void updateSleepConfig()
    {
        Result r = null;
        JsonObject jObj = new JsonObject();
        try
        {
            JsonObject paramJson = super.getParams();
            if (paramJson == null)
            {
                super.write(ResultTran.STATE_ERROR, "客户端参数异常", jObj);
            }
            String userId = GsonUtil.toString(paramJson.get("userId"));
            String custId = GsonUtil.toString(paramJson.get("custId"));
            String deviceId = GsonUtil.toString(paramJson.get("deviceId"));
            String deviceCode = GsonUtil.toString(paramJson.get("deviceCode"));
            String sleepStartTime = GsonUtil.toString(paramJson.get("startTime"));
            String sleepEndTime = GsonUtil.toString(paramJson.get("endTime"));
            Integer sleepStatus = GsonUtil.toInteger(paramJson.get("status"));
            WbConfig bean = new WbConfig();
            bean.setId(deviceCode);
            bean.setCustId(custId);
            bean.setDeviceId(deviceId);
            bean.setSleepStartTime(sleepStartTime);
            bean.setSleepEndTime(sleepEndTime);
            bean.setSleepStatus(sleepStatus);
            bean.setUpdateBy(userId);
            r = wbConfigService.updateSleepConfig(bean);
            super.write(String.valueOf(r.getState()), r.getMsg(), jObj);
        }
        catch (Exception e)
        {
            logger.error("服务端异常,获取数据失败", e);
            super.write(ResultTran.STATE_ERROR, "服务端异常", jObj);
        }
    }

    /**
     * 1.1.1.5.3    睡眠监测开关接口
     * @param userId
     * @param custId 对应的设备绑定人的ID
     * @param deviceId 设备ID-对应一类设备
     * @param deviceCode 设备code-对应具体的一个设备
     * 一个人可以带多个腕表
     * 2015.11.27
     * @author h
     * */
    public void updateSleepState()
    {
        Result r = null;
        JsonObject jObj = new JsonObject();
        try
        {
            JsonObject paramJson = super.getParams();
            if (paramJson == null)
            {
                super.write(ResultTran.STATE_ERROR, "客户端参数异常", jObj);
            }
            String userId = GsonUtil.toString(paramJson.get("userId"));
            String custId = GsonUtil.toString(paramJson.get("custId"));
            String deviceId = GsonUtil.toString(paramJson.get("deviceId"));
            String deviceCode = GsonUtil.toString(paramJson.get("deviceCode"));
            Integer sleepStatus = GsonUtil.toInteger(paramJson.get("status"));
            WbConfig bean = new WbConfig();
            bean.setCustId(custId);
            bean.setDeviceId(deviceId);
            bean.setSleepStatus(sleepStatus);
            /*r = wbConfigService.updateStepConfig(bean);*/
            super.write(ResultTran.STATE_ERROR, "没有此腕表信息", jObj);
        }
        catch (Exception e)
        {
            logger.error("服务端异常,获取数据失败", e);
            super.write(ResultTran.STATE_ERROR, "服务端异常", jObj);
        }
    }

    /**
     * 1.1.1.9.1    获取所有提醒信息
     * @param userId
     * @param custId 对应的设备绑定人的ID
     * @param deviceId 设备ID-对应一类设备
     * @param deviceCode 设备code-对应具体的一个设备
     * 一个人可以带多个腕表
     * 2015.11.25
     * @author h
     * */
    public void acquireWbRemind()
    {
        JsonObject jObj = new JsonObject();
        try
        {
            JsonObject paramJson = super.getParams();
            if (paramJson == null)
            {
                super.write(ResultTran.STATE_ERROR, "客户端参数异常", jObj);
            }
            Map<String, Object> filter = new HashMap<String, Object>();
            String userId = GsonUtil.toString(paramJson.get("userId"));
            String custId = GsonUtil.toString(paramJson.get("custId"));
            filter.put("custId", custId);
            String deviceId = GsonUtil.toString(paramJson.get("deviceId"));
            filter.put("deviceId", deviceId);
            String deviceCode = GsonUtil.toString(paramJson.get("deviceCode"));
            filter.put("deviceCode", deviceCode);
            WbConfig wbConfig = wbConfigService.queryConfig(filter);
            JsonArray jsonList = new JsonArray();
            if (wbConfig != null)
            {
                /*jObj.addProperty("deviceCode", wbConfig.getId());
                jObj.addProperty("deviceCode", wbConfig.getId());*/
                if (wbConfig.getRemindList() == null || StringUtil.isEmpty(wbConfig.getRemindList())
                        || wbConfig.getRemindList().equals("^^"))
                {

                }
                else
                {
                    String remindStr = wbConfig.getRemindList();
                    String remind = (remindStr.substring(1, remindStr.length() - 1));
                    String[] remindArray = remind.split("\\^");

                    for (int i = 0; i < remindArray.length; i++)
                    {
                        JsonObject remindJObj = new JsonObject();
                        String[] remindA = remindArray[i].split("#");
                        remindJObj.addProperty("deviceCode", wbConfig.getId());
                        remindJObj.addProperty("remindTime", remindA[0]);
                        remindJObj.addProperty("remindType", remindA[1]);
                        remindJObj.addProperty("remindContent", remindA[2]);
                        jsonList.add(remindJObj);
                        /*jObj.addProperty("jsonList", remindJObj.toString());*/
                    }
                }
                /*jObj.addProperty("remindContent", wbConfig.getRemindContent());
                jObj.addProperty("remindTime", wbConfig.getRemindTime());
                jObj.addProperty("status", wbConfig.getRemindStatus());*/
                /*jObj.addProperty(property, value)*/
                super.write(ResultTran.STATE_OK, "获取数据成功", jsonList);
            }
            else
            {
                super.write(ResultTran.STATE_OK, "此腕表提醒参数信息", jObj);
            }
        }
        catch (Exception e)
        {
            logger.error("服务端异常,获取数据失败", e);
            super.write(ResultTran.STATE_ERROR, "服务端异常", jObj);
        }
    }

    /**
     * 1.1.1.5.2 提醒设置
     * @param userId
     * @param custId 对应的设备绑定人的ID
     * @param deviceId 设备ID-对应一类设备
     * @param deviceCode 设备code-对应具体的一个设备
     * 一个人可以带多个腕表
     * 2015.11.27
     * @author h
     * */
    public void updateRemindConfig()
    {
        Result r = null;
        JsonObject jObj = new JsonObject();
        try
        {
            JsonObject paramJson = super.getParams();
            if (paramJson == null)
            {
                super.write(ResultTran.STATE_ERROR, "客户端参数异常", jObj);
            }
            String userId = GsonUtil.toString(paramJson.get("userId"));
            String custId = GsonUtil.toString(paramJson.get("custId"));
            String deviceId = GsonUtil.toString(paramJson.get("userDeviceId"));
            String deviceCode = GsonUtil.toString(paramJson.get("deviceCode"));
            //提醒json数组
            JsonArray remindJson = paramJson.getAsJsonArray("remindList");
            String remindList = "";
            String remind[] = new String[remindJson.size()];
            if (remindJson.size() > 0)
            {
                /*[V1.0.0, 865621453864925, 8ad8aca24d904e71014d944e2ec60010,S90,1,2015-11-20 16:35:12
                 * , ^remind time1#remind type1#remindcontent 1^remind time2#remind type2#remindcontent 2^…]
                */
                /*for(int j = 0; j < remindJson.size();j++)
                {*/
                for (int i = 0; i < remindJson.size(); i++)
                {
                    JsonObject remindJsons = remindJson.get(i).getAsJsonObject();
                    String remindType = GsonUtil.toString(remindJsons.get("remindType"));
                    String remindContent = GsonUtil.toString(remindJsons.get("remindContent"));
                    String remindTime = GsonUtil.toString(remindJsons.get("remindTime"));
                    remind[i] = "^" + remindTime + "#" + remindType + "#" + remindContent;
                    remindList += remind[i];
                }
                remindList += "^";
            }
            else
            {
                remindList = "^^";
            }

            /*}*/
            /*String remindContent = GsonUtil.toString(paramJson.get("remindContent"));
            String remindTime = GsonUtil.toString(paramJson.get("remindTime"));
            Integer remindStatus = GsonUtil.toInteger(paramJson.get("status"));*/
            WbConfig bean = new WbConfig();
            bean.setId(deviceCode);
            bean.setCustId(custId);
            bean.setDeviceId(deviceId);
            bean.setRemindList(remindList);
            /* bean.setRemindContent(remindContent);
             bean.setRemindTime(remindTime);
             bean.setRemindStatus(remindStatus);*/
            bean.setUpdateBy(userId);
            r = wbConfigService.updateRemindConfig(bean);
            super.write(String.valueOf(r.getState()), r.getMsg(), jObj);
        }
        catch (Exception e)
        {
            logger.error("服务端异常,获取数据失败", e);
            super.write(ResultTran.STATE_ERROR, "服务端异常", jObj);
        }
    }

    /**
     * 1.1.1.10.1   获取快速拨号参数
     * @param userId
     * @param custId 对应的设备绑定人的ID
     * @param deviceId 设备ID-对应一类设备
     * @param deviceCode 设备code-对应具体的一个设备
     * 一个人可以带多个腕表
     * 2015.11.25
     * @author h
     * */
    @SuppressWarnings("unused")
    public void acquireWbPhones()
    {
        JsonObject jObj = new JsonObject();
        try
        {
            JsonObject paramJson = super.getParams();
            if (paramJson == null)
            {
                super.write(ResultTran.STATE_ERROR, "客户端参数异常", jObj);
            }
            Map<String, Object> filter = new HashMap<String, Object>();
            String userId = GsonUtil.toString(paramJson.get("userId"));
            String custId = GsonUtil.toString(paramJson.get("custId"));
            filter.put("custId", custId);
            String deviceId = GsonUtil.toString(paramJson.get("deviceId"));
            filter.put("deviceId", deviceId);
            String deviceCode = GsonUtil.toString(paramJson.get("deviceCode"));
            filter.put("deviceCode", deviceCode);
            WbConfig wbConfig = wbConfigService.queryConfig(filter);
            /* System.out.println(split);
             JSONArray phonesJson = new JSONArray();
             phonesJson.put(split[0]);
             phonesJson.put(split[1]);
             phonesJson.put(split[2]);
             phonesJson.put(split[3]);*/
            /*phonesJson = JSONArray.toJSONObject(split);*/
            if (wbConfig != null)
            {
                jObj.addProperty("deviceCode", wbConfig.getId());
                /*String[] split = wbConfig.getPhones().split(",");*/
                jObj.addProperty("phones", wbConfig.getPhones());
                super.write(ResultTran.STATE_OK, "获取数据成功", jObj);
            }
            else
            {
                super.write(ResultTran.STATE_OK, "此腕表快速拨号参数未设置", jObj);
            }
        }
        catch (Exception e)
        {
            logger.error("服务端异常,获取数据失败", e);
            super.write(ResultTran.STATE_ERROR, "服务端异常", jObj);
        }
    }

    /**
     * 1.1.1.10.2   设置快速拨号号码
     * @param userId
     * @param custId 对应的设备绑定人的ID
     * @param deviceId 设备ID-对应一类设备
     * @param deviceCode 设备code-对应具体的一个设备
     * 一个人可以带多个腕表
     * 2015.11.27
     * @author h
     * */
    public void updatePhonesConfig()
    {
        Result r = null;
        JsonObject jObj = new JsonObject();
        try
        {
            JsonObject paramJson = super.getParams();
            if (paramJson == null)
            {
                super.write(ResultTran.STATE_ERROR, "客户端参数异常", jObj);
            }
            String userId = GsonUtil.toString(paramJson.get("userId"));
            String custId = GsonUtil.toString(paramJson.get("custId"));
            String deviceId = GsonUtil.toString(paramJson.get("deviceId"));
            String deviceCode = GsonUtil.toString(paramJson.get("deviceCode"));
            /*String phones = GsonUtil.jsonArrToString(paramJson.get("phones").getAsJsonArray(), ",");*/
            JsonArray phonesJson = paramJson.get("phones").getAsJsonArray();
            String phone1 = GsonUtil.toString(phonesJson.get(0));
            String phone2 = GsonUtil.toString(phonesJson.get(1));
            String phone3 = GsonUtil.toString(phonesJson.get(2));
            String phone4 = GsonUtil.toString(phonesJson.get(3));
            String phones = new StringBuilder().append(phone1).append(",").append(phone2).append(",").append(phone3)
                    .append(",").append(phone4).toString();
            /*String phones = GsonUtil.jsonArrToString(phonesJson, ",");*/
            /*String phones = GsonUtil.toString(paramJson.get("phones"));
            String phonesStr = phones.substring(1, phones.length()-1);*/
            /*String phonesJson = GsonUtil.jsonArrToString(paramJson.get("phones").getAsJsonArray(), ",");*/
            System.out.println(phones);
            System.out.println(phones);
            WbConfig bean = new WbConfig();
            bean.setId(deviceCode);
            bean.setCustId(custId);
            bean.setDeviceId(deviceId);
            bean.setPhones(phones);
            System.out.println(bean.getPhones());
            bean.setUpdateBy(userId);
            r = wbConfigService.updatePhonesConfig(bean);
            super.write(String.valueOf(r.getState()), r.getMsg(), jObj);
        }
        catch (Exception e)
        {
            logger.error("服务端异常,获取数据失败", e);
            super.write(ResultTran.STATE_ERROR, "服务端异常", jObj);
        }
    }

    /**
     * 1.1.1.12.1   获取SOS服务器设置参数
     * @param userId
     * @param custId 对应的设备绑定人的ID
     * @param deviceId 设备ID-对应一类设备
     * @param deviceCode 设备code-对应具体的一个设备
     * 一个人可以带多个腕表
     * 2015.11.25
     * @author h
     * */
    @SuppressWarnings("unused")
    public void acquireWbSos()
    {
        JsonObject jObj = new JsonObject();
        try
        {
            JsonObject paramJson = super.getParams();
            if (paramJson == null)
            {
                super.write(ResultTran.STATE_ERROR, "客户端参数异常", jObj);
            }
            Map<String, Object> filter = new HashMap<String, Object>();
            String userId = GsonUtil.toString(paramJson.get("userId"));
            String custId = GsonUtil.toString(paramJson.get("custId"));
            filter.put("custId", custId);
            String deviceId = GsonUtil.toString(paramJson.get("deviceId"));
            filter.put("deviceId", deviceId);
            String deviceCode = GsonUtil.toString(paramJson.get("deviceCode"));
            filter.put("deviceCode", deviceCode);
            WbConfig wbConfig = wbConfigService.queryConfig(filter);
            /*Arrays.toString(split);
            JSONArray sosNumsJson = new JSONArray();
            sosNumsJson.put(split[0]);
            sosNumsJson.put(split[1]);
            sosNumsJson.put(split[2]);*/
            if (wbConfig != null)
            {
                jObj.addProperty("deviceCode", wbConfig.getId());
                /*String[] split = wbConfig.getSosNums().split(",");*/
                /*jObj.addProperty("SOSNums", Arrays.toString(split));*/
                jObj.addProperty("SOSNums", wbConfig.getSosNums());
                jObj.addProperty("SOStext", wbConfig.getSosText());
                super.write(ResultTran.STATE_OK, "获取数据成功", jObj);
            }
            else
            {
                super.write(ResultTran.STATE_OK, "此腕表信息SOS号码未设置", jObj);
            }
        }
        catch (Exception e)
        {
            logger.error("服务端异常,获取数据失败", e);
            super.write(ResultTran.STATE_ERROR, "服务端异常", jObj);
        }
    }

    /**
     * 1.1.1.12.2   设置SOS号码
     * @param userId
     * @param custId 对应的设备绑定人的ID
     * @param deviceId 设备ID-对应一类设备
     * @param deviceCode 设备code-对应具体的一个设备
     * 一个人可以带多个腕表
     * 2015.11.27
     * @author h
     * */
    public void updateSOSConfig()
    {
        Result r = null;
        JsonObject jObj = new JsonObject();
        try
        {
            JsonObject paramJson = super.getParams();
            if (paramJson == null)
            {
                super.write(ResultTran.STATE_ERROR, "客户端参数异常", jObj);
            }
            String userId = GsonUtil.toString(paramJson.get("userId"));
            String custId = GsonUtil.toString(paramJson.get("custId"));
            String deviceId = GsonUtil.toString(paramJson.get("deviceId"));
            String deviceCode = GsonUtil.toString(paramJson.get("deviceCode"));
            /*String sosNums = GsonUtil.toString(paramJson.get("SOSNums"));*/
            JsonArray sosNumsJson = paramJson.get("SOSNums").getAsJsonArray();
            String sosNums1 = GsonUtil.toString(sosNumsJson.get(0));
            String sosNums2 = GsonUtil.toString(sosNumsJson.get(1));
            String sosNums3 = GsonUtil.toString(sosNumsJson.get(2));
            String sosNums = new StringBuilder().append(sosNums1).append(",").append(sosNums2).append(",")
                    .append(sosNums3).toString();
            WbConfig bean = new WbConfig();
            bean.setId(deviceCode);
            bean.setCustId(custId);
            bean.setDeviceId(deviceId);
            bean.setSosNums(sosNums);
            bean.setUpdateBy(userId);
            r = wbConfigService.updateSOSConfig(bean);
            super.write(String.valueOf(r.getState()), r.getMsg(), jObj);
        }
        catch (Exception e)
        {
            logger.error("服务端异常,获取数据失败", e);
            super.write(ResultTran.STATE_ERROR, "服务端异常", jObj);
        }
    }

    /**
     * 1.1.1.12.3   设置SOS短信息
     * @param userId
     * @param custId 对应的设备绑定人的ID
     * @param deviceId 设备ID-对应一类设备
     * @param deviceCode 设备code-对应具体的一个设备
     * 一个人可以带多个腕表
     * 2015.11.27
     * @author h
     * */
    public void updateSOSInfo()
    {
        Result r = null;
        JsonObject jObj = new JsonObject();
        try
        {
            JsonObject paramJson = super.getParams();
            if (paramJson == null)
            {
                super.write(ResultTran.STATE_ERROR, "客户端参数异常", jObj);
            }
            String userId = GsonUtil.toString(paramJson.get("userId"));
            String custId = GsonUtil.toString(paramJson.get("custId"));
            String deviceId = GsonUtil.toString(paramJson.get("deviceId"));
            String deviceCode = GsonUtil.toString(paramJson.get("deviceCode"));
            String sosText = GsonUtil.toString(paramJson.get("SOStext"));
            WbConfig bean = new WbConfig();
            bean.setId(deviceCode);
            bean.setCustId(custId);
            bean.setDeviceId(deviceId);
            bean.setSosText(sosText);
            bean.setUpdateBy(userId);
            r = wbConfigService.updateSOSInfo(bean);
            super.write(String.valueOf(r.getState()), r.getMsg(), jObj);
        }
        catch (Exception e)
        {
            logger.error("服务端异常,获取数据失败", e);
            super.write(ResultTran.STATE_ERROR, "服务端异常", jObj);
        }
    }

    /**
     * 1.1.1.13.3    获取腕表定位数据
     * @param userId
     * @param custId 对应的设备绑定人的ID
     * @param deviceId 设备ID-对应一类设备
     * @param deviceCode 设备code-对应具体的一个设备
     * 一个人可以带多个腕表
     * 2015.11.25
     * @author h
     * */
    public void positionList()
    {
        JsonObject jObj = new JsonObject();
        try
        {
            JsonObject obj = super.getParams();
            initQuery(obj);
            User operUser = (User) super.getOperUser();
            Map<String, Object> filter = new HashMap<String, Object>();
            String userId = GsonUtil.toString(obj.get("userId"));
            String custId = GsonUtil.toString(obj.get("custId"));
            filter.put("custId", custId);
            String deviceId = GsonUtil.toString(obj.get("deviceId"));
            filter.put("deviceId", deviceId);
            String deviceCode = GsonUtil.toString(obj.get("deviceCode"));
            filter.put("deviceCode", deviceCode);
            String positionMode = GsonUtil.toString(obj.get("positionMode"));
            filter.put("positionMode", positionMode);
            PageTran<CustPosition> pageTran = custPositionService.queryTran(filter, synTime, synType, pageSize);
            super.write(ResultTran.STATE_OK, "获取数据成功", pageTran);
        }
        catch (Exception e)
        {
            logger.error("服务端异常,获取数据失败", e);
            super.write(ResultTran.STATE_ERROR, "服务端异常", jObj);
        }
    }

    /**
     * 1.1.1.13.1   查询腕表最后一次定位数据
     * @param userId
     * @param custId 对应的设备绑定人的ID
     * @param deviceId 设备ID-对应一类设备
     * @param deviceCode 设备code-对应具体的一个设备
     * 一个人可以带多个腕表
     * 2015.11.30
     * @author h
     * */
    public void queryLastPostion()
    {
        JsonObject jObj = new JsonObject();
        try
        {
            JsonObject paramJson = super.getParams();
            if (paramJson == null)
            {
                super.write(ResultTran.STATE_ERROR, "客户端参数异常", jObj);
            }
            User operUser = (User) super.getOperUser();
            Map<String, Object> filter = new HashMap<String, Object>();
            String userId = GsonUtil.toString(paramJson.get("userId"));
            String custId = GsonUtil.toString(paramJson.get("custId"));
            filter.put("custId", custId);
            String deviceId = GsonUtil.toString(paramJson.get("deviceId"));
            filter.put("deviceId", deviceId);
            String deviceCode = GsonUtil.toString(paramJson.get("deviceCode"));
            filter.put("deviceCode", deviceCode);
            /*String positionMode = GsonUtil.toString(paramJson.get("positionMode"));
            filter.put("positionMode", positionMode);*/
            CustPosition bean = custPositionService.queryLastPostion(filter);
            if (bean != null)
            {
                jObj.addProperty("checkTime", bean.getCheckTime().toString());
                jObj.addProperty("positionMode", bean.getPositionMode());
                jObj.addProperty("longitude", bean.getLongitude());
                jObj.addProperty("latitude", bean.getLatitude());
                jObj.addProperty("speed", bean.getSpeed());
                jObj.addProperty("direction", bean.getDirection());
                jObj.addProperty("altitude", bean.getAltitude());
                jObj.addProperty("mnc", bean.getMnc());
                jObj.addProperty("lac", bean.getLac());
                jObj.addProperty("cellid", bean.getCellid());
                jObj.addProperty("address", "");
                super.write(ResultTran.STATE_OK, "获取数据成功", jObj);
            }
            else
            {
                super.write(ResultTran.STATE_OK, "此腕表定位数据为空", jObj);
            }
        }
        catch (Exception e)
        {
            logger.error("服务端异常,获取数据失败", e);
            super.write(ResultTran.STATE_ERROR, "服务端异常", jObj);
        }
    }

    /**
     * 1.1.1.13.2   查询腕表当前定位数据
     * @param userId
     * @param custId 对应的设备绑定人的ID
     * @param deviceId 设备ID-对应一类设备
     * @param deviceCode 设备code-对应具体的一个设备
     * 一个人可以带多个腕表
     * 2015.11.30
     * @author h
     * */
    public void queryCurrentPosition()
    {
        JsonObject jObj = new JsonObject();
        try
        {
            JsonObject paramJson = super.getParams();
            if (paramJson == null)
            {
                super.write(ResultTran.STATE_ERROR, "客户端参数异常", jObj);
            }
            User operUser = (User) super.getOperUser();
            Map<String, Object> filter = new HashMap<String, Object>();
            String userId = GsonUtil.toString(paramJson.get("userId"));
            String custId = GsonUtil.toString(paramJson.get("custId"));
            filter.put("custId", custId);
            String deviceId = GsonUtil.toString(paramJson.get("deviceId"));
            filter.put("deviceId", deviceId);
            String deviceCode = GsonUtil.toString(paramJson.get("deviceCode"));
            filter.put("deviceCode", deviceCode);
            /*String positionMode = GsonUtil.toString(paramJson.get("positionMode"));
            filter.put("positionMode", positionMode);*/
            CustPosition bean = custPositionService.queryCurrentPosition(filter);
            if (bean != null)
            {
                jObj.addProperty("checkTime", bean.getCheckTime().toString());
                jObj.addProperty("positionMode", bean.getPositionMode());
                jObj.addProperty("longitude", bean.getLongitude());
                jObj.addProperty("latitude", bean.getLatitude());
                jObj.addProperty("speed", bean.getSpeed());
                jObj.addProperty("direction", bean.getDirection());
                jObj.addProperty("altitude", bean.getAltitude());
                jObj.addProperty("mnc", bean.getMnc());
                jObj.addProperty("lac", bean.getLac());
                jObj.addProperty("cellid", bean.getCellid());
                jObj.addProperty("address", "");
                super.write(ResultTran.STATE_OK, "获取数据成功", jObj);
            }
            else
            {
                super.write(ResultTran.STATE_OK, "当前此腕表定位数据为空", jObj);
            }
        }
        catch (Exception e)
        {
            logger.error("服务端异常,获取数据失败", e);
            super.write(ResultTran.STATE_ERROR, "服务端异常", jObj);
        }
    }

    /**
     * 1.1.1.13.4   查询腕表设置的电子围栏信息
     * @param userId
     * @param custId 对应的设备绑定人的ID
     * @param deviceId 设备ID-对应一类设备
     * @param deviceCode 设备code-对应具体的一个设备
     * 一个人可以带多个腕表
     * 2015.11.30
     * @author h
     * */
    public void queryEleFence()
    {
        JsonObject jObj = new JsonObject();
        try
        {
            JsonObject paramJson = super.getParams();
            if (paramJson == null)
            {
                super.write(ResultTran.STATE_ERROR, "客户端参数异常", jObj);
            }
            User operUser = (User) super.getOperUser();
            Map<String, Object> filter = new HashMap<String, Object>();
            String userId = GsonUtil.toString(paramJson.get("userId"));
            String custId = GsonUtil.toString(paramJson.get("custId"));
            filter.put("custId", custId);
            String deviceId = GsonUtil.toString(paramJson.get("userDeviceId"));
            filter.put("deviceId", deviceId);
            String deviceCode = GsonUtil.toString(paramJson.get("deviceCode"));
            filter.put("deviceCode", deviceCode);
            /*String positionMode = GsonUtil.toString(paramJson.get("positionMode"));
            filter.put("positionMode", positionMode);*/
            WbConfig bean = wbConfigService.queryConfig(filter);
            /*CustPosition bean = custPositionService.queryCurrentPosition(filter);*/
            if (bean != null)
            {
                jObj.addProperty("deviceCode", bean.getId());
                jObj.addProperty("longitude", bean.getEleLongitude());
                jObj.addProperty("latitude", bean.getEleLatitude());
                jObj.addProperty("address", bean.getEleAddress());
                jObj.addProperty("distance", bean.getEleDistance());
                if (bean.getEleTime() == null)
                {
                    jObj.addProperty("time", "");
                }
                else
                {
                    jObj.addProperty("time", bean.getEleTime().toString());
                }
                super.write(ResultTran.STATE_OK, "获取数据成功", jObj);
            }
            else
            {
                super.write(ResultTran.STATE_OK, "腕表电子围栏信息为空", jObj);
            }
        }
        catch (Exception e)
        {
            logger.error("服务端异常,获取数据失败", e);
            super.write(ResultTran.STATE_ERROR, "服务端异常", jObj);
        }
    }

    /**
     *1.1.1.13.6    获取超出电子围栏定位数据
     * @param userId
     * @param custId 对应的设备绑定人的ID
     * @param deviceId 设备ID-对应一类设备
     * @param deviceCode 设备code-对应具体的一个设备
     * 一个人可以带多个腕表
     * 2015.11.25
     * @author h
     * */
    public void eleFenceList()
    {
        JsonObject jObj = new JsonObject();
        try
        {
            JsonObject obj = super.getParams();
            initQuery(obj);
            User operUser = (User) super.getOperUser();
            Map<String, Object> filter = new HashMap<String, Object>();
            String userId = GsonUtil.toString(obj.get("userId"));
            String custId = GsonUtil.toString(obj.get("custId"));
            filter.put("custId", custId);
            String deviceId = GsonUtil.toString(obj.get("deviceId"));
            filter.put("deviceId", deviceId);
            String deviceCode = GsonUtil.toString(obj.get("deviceCode"));
            filter.put("deviceCode", deviceCode);
            String positionMode = GsonUtil.toString(obj.get("positionMode"));
            filter.put("positionMode", positionMode);
            PageTran<CustPosition> pageTran = custPositionService.queryTran(filter, synTime, synType, pageSize);
            super.write(ResultTran.STATE_OK, "获取数据成功", pageTran);
        }
        catch (Exception e)
        {
            logger.error("服务端异常,获取数据失败", e);
            super.write(ResultTran.STATE_ERROR, "服务端异常", jObj);
        }
    }

    /**
     * 1.1.1.13.5   设置电子围栏信息
     * @param userId
     * @param custId 对应的设备绑定人的ID
     * @param deviceId 设备ID-对应一类设备
     * @param deviceCode 设备code-对应具体的一个设备
     * 一个人可以带多个腕表
     * 2015.11.27
     * @author h
     * */
    public void updateEleFence()
    {
        Result r = null;
        JsonObject jObj = new JsonObject();
        try
        {
            JsonObject paramJson = super.getParams();
            if (paramJson == null)
            {
                super.write(ResultTran.STATE_ERROR, "客户端参数异常", jObj);
            }
            String userId = GsonUtil.toString(paramJson.get("userId"));
            /*String custId = GsonUtil.toString(paramJson.get("custId"));*/
            String deviceId = GsonUtil.toString(paramJson.get("deviceId"));
            String eleAddress = GsonUtil.toString(paramJson.get("address"));
            String deviceCode = GsonUtil.toString(paramJson.get("deviceCode"));
            Double eleLongitude = GsonUtil.toDouble(paramJson.get("longitude"));
            Double eleLatitude = GsonUtil.toDouble(paramJson.get("latitude"));
            Integer eleDistance = GsonUtil.toInteger(paramJson.get("distance"));
            WbConfig bean = new WbConfig();
            bean.setId(deviceCode);
            bean.setCustId(userId);
            bean.setDeviceId(deviceId);
            bean.setEleAddress(eleAddress);
            bean.setEleLongitude(eleLongitude);
            bean.setEleLatitude(eleLatitude);
            bean.setEleDistance(eleDistance);
            bean.setUpdateBy(userId);
            r = wbConfigService.updateEleFence(bean);
            super.write(String.valueOf(r.getState()), r.getMsg(), jObj);
        }
        catch (Exception e)
        {
            logger.error("服务端异常,获取数据失败", e);
            super.write(ResultTran.STATE_ERROR, "服务端异常", jObj);
        }
    }
}
