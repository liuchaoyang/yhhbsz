package com.yzxt.yh.module.cli.service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzxt.fw.common.Result;
import com.yzxt.yh.constant.ConstMap;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.constant.ConstWb;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.eif.mp.MpConst;
import com.yzxt.yh.eif.mp.MpSender;
import com.yzxt.yh.eif.surezen.SurezenConst;
import com.yzxt.yh.eif.surezen.SurezenSender;
import com.yzxt.yh.module.cli.bean.WbConfig;
import com.yzxt.yh.module.cli.bean.WbUpdate;
import com.yzxt.yh.module.cli.dao.WbConfigDao;
import com.yzxt.yh.module.cli.dao.WbUpdateDao;
import com.yzxt.yh.module.sys.bean.FileDesc;
import com.yzxt.yh.module.sys.dao.FileDescDao;
import com.yzxt.yh.util.StringUtil;

@Transactional(ConstTM.DEFAULT)
public class WbConfigService
{
    private Logger logger = Logger.getLogger(CustPositionService.class);
    public WbConfigDao wbConfigDao;
    public WbUpdateDao wbUpdateDao;
    public FileDescDao fileDescDao;
    String version1 = "V1.0.0";

    public WbConfigDao getWbConfigDao()
    {
        return wbConfigDao;
    }

    public void setWbConfigDao(WbConfigDao wbConfigDao)
    {
        this.wbConfigDao = wbConfigDao;
    }

    public WbUpdateDao getWbUpdateDao()
    {
        return wbUpdateDao;
    }

    public void setWbUpdateDao(WbUpdateDao wbUpdateDao)
    {
        this.wbUpdateDao = wbUpdateDao;
    }

    public FileDescDao getFileDescDao()
    {
        return fileDescDao;
    }

    public void setFileDescDao(FileDescDao fileDescDao)
    {
        this.fileDescDao = fileDescDao;
    }

    /**
     * 1.1.1.2.1 获取心率所有设置参数
     * @param filter
     * @param userId
     * @param custId 对应的设备绑定人的ID
     * @param deviceId 设备ID-对应一类设备
     * @param deviceCode 设备code-对应具体的一个设备
     * 一个人可以带多个腕表
     * 2015.11.25
     * @author h
     * */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public WbConfig queryConfig(Map<String, Object> filter)
    {
        return wbConfigDao.queryConfig(filter);
    }

    /**
     * 1.1.1.2.3  设置定时检测参数接口 其中需要拼接下发数据存储到wbUpdate
     * @param bean
     * 一个人可以带多个腕表
     * 2015.11.25
     * @author h
     * @throws Exception 
     * */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result updateWbPluse(WbConfig bean) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        //先找到腕表的配置id，然后通过上传设置，修改配置里面的东西，然后拼接到修改表中
        /* [V1.0.0, 865621453864925, 8ad8aca24d904e71014d944e2ec60010,S85,1,2015-11-20 16:35:12, start time,endtime,span]

                 start time:开始时间，如10:00:05。
                 end time:结束时间，如10:58:09。
                 span:如60。
         */
        wbConfigDao.updateWbPluse(bean);
        String configData = new StringBuilder().append(version1).append(",").append(bean.getId()).append(",")
                .append(",S85").append(",").append(SurezenConst.OPER_STATE_SUCCESS).append(",").append(",")
                .append(bean.getPluseStartTime()).append(",").append(bean.getPluseEndTime()).append(",")
                .append(bean.getPluseSpan()).toString();
        /*判断手表是否在线，如果在线，就先下发设置，然后保存设置到服务器中，返回设置成功给客服端，
         * 如果手表不在线，则返回设置失败给客服端。
         */
        int sendStatus = SurezenSender.send(bean.getId(), configData);
        if (sendStatus == 1)
        {
            WbUpdate wu = new WbUpdate();
            wu.setUpdateTime(now);
            wu.setConfigItem(ConstWb.WB_TYPE_PLUSE);
            wu.setStatus(Constant.WB_SERVICE_FOR_SEND);
            wu.setWbId(bean.getId());
            wu.setConfigData(configData);
            wu.setUpdateBy(bean.getUpdateBy());
            String wuId = wbUpdateDao.insert(wu);
            if (StringUtil.isNotEmpty(wuId))
            {
                return new Result(Result.STATE_SUCESS, "设置脉搏参数成功", wuId);
            }
            else
            {
                return new Result(Result.STATE_FAIL, "设置脉搏参数失败", wuId);
            }
        }
        else if (sendStatus == 2)
        {
            return new Result(Result.STATE_FAIL, "设置脉搏参数失败，腕表不在线", "");
        }
        else
        {
            return new Result(Result.STATE_FAIL, "设置脉搏参数失败，未知错误", "");
        }
    }

    /**
     * 1.1.1.1.2    设置腕表身份信息
     * @param bean
     * 一个人可以带多个腕表
     * 2015.11.25
     * @author h
     * @throws Exception 
     * */
    @Transactional(propagation = Propagation.REQUIRED)
    public Result updateWbCard(WbConfig bean, FileDesc imgFileDesc) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        String imgFileId = null;
        // 保存图像文件
        if (imgFileDesc != null)
        {
            imgFileId = fileDescDao.insert(imgFileDesc);
        }
        if (StringUtil.isNotEmpty(imgFileId))
        {
            bean.setImgFileId(imgFileId);
        }
        wbConfigDao.updateWbCard(bean);
        /*下发拼接
        [V1.0.0, 865621453864925, 8ad8aca24d904e71014d944e2ec60010,
        S81,1,2015-11-20 16:35:12, age, sex ,height, weight ,step]
        V1.0.0,123456,,S81,1,,0,1,164.0,65.2,50,
        */
        String configData = "";
        if (bean.getBirthday() != null)
        {
            configData = new StringBuilder().append(version1).append(",").append(bean.getId()).append(",")
                    .append(",S81").append(",").append(SurezenConst.OPER_STATE_SUCCESS).append(",").append(",")
                    .append(getAge(bean.getBirthday())).append(",").append(bean.getSex()).append(",")
                    .append(bean.getHeight()).append(",").append(bean.getWeight()).append(",").append(bean.getStep())
                    .toString();
        }
        else
        {
            configData = new StringBuilder().append(version1).append(",").append(bean.getId()).append(",")
                    .append(",S81").append(",").append(SurezenConst.OPER_STATE_SUCCESS).append(",").append(",")
                    .append("").append(",").append(bean.getSex()).append(",").append(bean.getHeight()).append(",")
                    .append(bean.getWeight()).append(",").append(bean.getStep()).toString();
        }
        /*判断手表是否在线，如果在线，就先下发设置，然后保存设置到服务器中，返回设置成功给客服端，
         * 如果手表不在线，则返回设置失败给客服端。
         */
        int sendStatus = SurezenSender.send(bean.getId(), configData);
        if (sendStatus == 1)
        {
            WbUpdate wu = new WbUpdate();
            wu.setUpdateTime(now);
            wu.setConfigItem(ConstWb.WB_TYPE_CARD);
            wu.setStatus(Constant.WB_SERVICE_FOR_SEND);
            wu.setWbId(bean.getId());
            wu.setConfigData(configData);
            wu.setUpdateBy(bean.getUpdateBy());
            String wuId = wbUpdateDao.insert(wu);
            if (StringUtil.isNotEmpty(wuId))
            {
                return new Result(Result.STATE_SUCESS, "设置身份信息参数成功", wuId);
            }
            else
            {
                return new Result(Result.STATE_FAIL, "设置身份信息参数失败", wuId);
            }
        }
        else if (sendStatus == 2)
        {
            return new Result(Result.STATE_FAIL, "设置身份信息参数失败，腕表不在线", "");
        }
        else
        {
            return new Result(Result.STATE_FAIL, "设置身份信息参数失败，未知错误", "");
        }
        /*WbUpdate wu = new WbUpdate();
        wu.setUpdateTime(now);
        wu.setConfigItem(ConstWb.WB_TYPE_CARD);
        wu.setStatus(Constant.WB_SERVICE_FOR_SEND);
        wu.setWbId(bean.getId());
        wu.setConfigData(configData);
        wu.setUpdateBy(bean.getUpdateBy());
        String wuId = wbUpdateDao.insert(wu);*/
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public String getAge(Date birthDay) throws Exception
    {
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay))
        {
            throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        int age = yearNow - yearBirth;
        if (monthNow <= monthBirth)
        {
            if (monthNow == monthBirth)
            {
                //monthNow==monthBirth 
                if (dayOfMonthNow < dayOfMonthBirth)
                {
                    age--;
                }
            }
            else
            {
                //monthNow>monthBirth 
                age--;
            }
        }
        return age + "";
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Result updateWbStep(WbConfig bean) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        //先找到腕表的配置id，然后通过上传设置，修改配置里面的东西，然后拼接到修改表中
        /* [V1.0.0, 865621453864925, 8ad8aca24d904e71014d944e2ec60010,S88,1,2015-11-20 16:35:12, start time,endtime,span]

         start time:开始时间，如10:00:05。
         end time:结束时间，如10:58:09。
         span:如60。
         */
        wbConfigDao.updateWbStep(bean);
        String configData = new StringBuilder().append(version1).append(",").append(bean.getId()).append(",")
                .append(",S88").append(",").append(SurezenConst.OPER_STATE_SUCCESS).append(",").append(",")
                .append(bean.getStepStartTime()).append(",").append(bean.getStepEndTime()).append(",")
                .append(bean.getStepSpan()).toString();
        /*判断手表是否在线，如果在线，就先下发设置，然后保存设置到服务器中，返回设置成功给客服端，
         * 如果手表不在线，则返回设置失败给客服端。
         */
        int sendStatus = SurezenSender.send(bean.getId(), configData);
        if (sendStatus == 1)
        {
            WbUpdate wu = new WbUpdate();
            wu.setUpdateTime(now);
            wu.setConfigItem(ConstWb.WB_TYPE_STEP);
            wu.setStatus(Constant.WB_SERVICE_FOR_SEND);
            wu.setWbId(bean.getId());
            wu.setConfigData(configData);
            wu.setUpdateBy(bean.getUpdateBy());
            String wuId = wbUpdateDao.insert(wu);
            if (StringUtil.isNotEmpty(wuId))
            {
                return new Result(Result.STATE_SUCESS, "设置计步参数成功", wuId);
            }
            else
            {
                return new Result(Result.STATE_FAIL, "设置计步参数失败", wuId);
            }
        }
        else if (sendStatus == 2)
        {
            return new Result(Result.STATE_FAIL, "设置计步参数失败，腕表不在线", "");
        }
        else
        {
            return new Result(Result.STATE_FAIL, "设置计步参数失败，未知错误", "");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Result updateStepTarget(WbConfig bean)
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        bean.setUpdateTime(now);
        int i = wbConfigDao.updateStepTarget(bean);
        if (i > 0)
        {
            return new Result(Result.STATE_SUCESS, "设置计步目标参数成功", i);
        }
        else
        {
            return new Result(Result.STATE_FAIL, "设置计步目标参数失败", i);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Result updatePositConfig(WbConfig bean) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        //先找到腕表的配置id，然后通过上传设置，修改配置里面的东西，然后拼接到修改表中
        /* [V1.0.0, 865621453864925, 8ad8aca24d904e71014d944e2ec60010,S87,1,2015-11-20 16:35:12, start time,endtime,span,positionMode]
         start time:开始时间，如10:00:05。
         end time:结束时间，如10:58:09。
         span:如60。
         positionMode:定位方式，GPS 或 LBS。如果腕表不支持此模式，可以忽略此值。
         */
        wbConfigDao.updatePositConfig(bean);
        String configData = new StringBuilder().append(version1).append(",").append(bean.getId()).append(",")
                .append(",S87").append(",").append(SurezenConst.OPER_STATE_SUCCESS).append(",").append(",")
                .append(bean.getPositionStartTime()).append(",").append(bean.getPositionEndTime()).append(",")
                .append(bean.getPositionSpan()).toString();
        /*判断手表是否在线，如果在线，就先下发设置，然后保存设置到服务器中，返回设置成功给客服端，
         * 如果手表不在线，则返回设置失败给客服端。
         */
        int sendStatus = SurezenSender.send(bean.getId(), configData);
        if (sendStatus == 1)
        {
            WbUpdate wu = new WbUpdate();
            wu.setUpdateTime(now);
            wu.setConfigItem(ConstWb.WB_TYPE_POSITION);
            wu.setStatus(Constant.WB_SERVICE_FOR_SEND);
            wu.setWbId(bean.getId());
            wu.setConfigData(configData);
            wu.setUpdateBy(bean.getUpdateBy());
            String wuId = wbUpdateDao.insert(wu);
            if (StringUtil.isNotEmpty(wuId))
            {
                return new Result(Result.STATE_SUCESS, "设置定位参数成功", wuId);
            }
            else
            {
                return new Result(Result.STATE_FAIL, "设置定位参数失败", wuId);
            }
        }
        else if (sendStatus == 2)
        {
            return new Result(Result.STATE_FAIL, "设置定位参数失败，腕表不在线", "");
        }
        else
        {
            return new Result(Result.STATE_FAIL, "设置定位参数失败，未知错误", "");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Result updateSleepConfig(WbConfig bean) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        //先找到腕表的配置id，然后通过上传设置，修改配置里面的东西，然后拼接到修改表中
        /* [V1.0.0, 865621453864925, 8ad8aca24d904e71014d944e2ec60010,S89,1,2015-11-20 16:35:12, start time,endtime,status]

         start time:开始时间，如10:00:05。
         end time:结束时间，如10:58:09。
         status:0-关,1-开。

         */
        wbConfigDao.updateSleepConfig(bean);
        String configData = new StringBuilder().append(version1).append(",").append(bean.getId()).append(",")
                .append(",S89").append(",").append(SurezenConst.OPER_STATE_SUCCESS).append(",").append(",")
                .append(bean.getSleepStartTime()).append(",").append(bean.getSleepEndTime()).append(",")
                .append(bean.getSleepStatus()).toString();
        /*判断手表是否在线，如果在线，就先下发设置，然后保存设置到服务器中，返回设置成功给客服端，
         * 如果手表不在线，则返回设置失败给客服端。
         */
        int sendStatus = SurezenSender.send(bean.getId(), configData);
        if (sendStatus == 1)
        {
            WbUpdate wu = new WbUpdate();
            wu.setUpdateTime(now);
            wu.setConfigItem(ConstWb.WB_TYPE_SLEEP);
            wu.setStatus(Constant.WB_SERVICE_FOR_SEND);
            wu.setWbId(bean.getId());
            wu.setConfigData(configData);
            wu.setUpdateBy(bean.getUpdateBy());
            String wuId = wbUpdateDao.insert(wu);
            if (StringUtil.isNotEmpty(wuId))
            {
                return new Result(Result.STATE_SUCESS, "设置睡眠参数成功", wuId);
            }
            else
            {
                return new Result(Result.STATE_FAIL, "设置睡眠参数失败", wuId);
            }
        }
        else if (sendStatus == 2)
        {
            return new Result(Result.STATE_FAIL, "设置睡眠参数失败，腕表不在线", "");
        }
        else
        {
            return new Result(Result.STATE_FAIL, "设置睡眠参数失败，未知错误", "");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Result updateRemindConfig(WbConfig bean) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        //先找到腕表的配置id，然后通过上传设置，修改配置里面的东西，然后拼接到修改表中
        /* [V1.0.0, 865621453864925, 8ad8aca24d904e71014d944e2ec60010,S90,1,2015-11-20 16:35:12, remind time, remind content, status]

         Remind time:提醒时间，如10:00:05。
         Remind content:提醒内容，如该做什么了。
         status:0-关,1-开。

         */
        wbConfigDao.updateRemindConfig(bean);
        String configData = new StringBuilder().append(version1).append(",").append(bean.getId()).append(",")
                .append(",S90").append(",").append(SurezenConst.OPER_STATE_SUCCESS).append(",").append(",")
                .append(bean.getRemindList())
                /*.append(bean.getRemindContent()).append(",").append(bean.getRemindTime()).append(",")
                .append(bean.getRemindStatus())*/.toString();
        /*判断手表是否在线，如果在线，就先下发设置，然后保存设置到服务器中，返回设置成功给客服端，
         * 如果手表不在线，则返回设置失败给客服端。
         */
        int sendStatus = SurezenSender.send(bean.getId(), configData);
        if (sendStatus == 1)
        {
            WbUpdate wu = new WbUpdate();
            wu.setUpdateTime(now);
            wu.setConfigItem(ConstWb.WB_TYPE_REMIND);
            wu.setStatus(Constant.WB_SERVICE_FOR_SEND);
            wu.setWbId(bean.getId());
            wu.setConfigData(configData);
            wu.setUpdateBy(bean.getUpdateBy());
            String wuId = wbUpdateDao.insert(wu);
            if (StringUtil.isNotEmpty(wuId))
            {
                return new Result(Result.STATE_SUCESS, "设置提醒参数成功", wuId);
            }
            else
            {
                return new Result(Result.STATE_FAIL, "设置提醒参数失败", wuId);
            }
        }
        else if (sendStatus == 2)
        {
            return new Result(Result.STATE_FAIL, "设置提醒参数失败，腕表不在线", "");
        }
        else
        {
            return new Result(Result.STATE_FAIL, "设置提醒参数失败，未知错误", "");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Result updateSOSInfo(WbConfig bean) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        //先找到腕表的配置id，然后通过上传设置，修改配置里面的东西，然后拼接到修改表中
        /* [V1.0.0, 865621453864925, 8ad8aca24d904e71014d944e2ec60010,S84,1,2015-11-20 16:35:12, SOStext]
             SOStext:短信发送内容。
         */
        wbConfigDao.updateSOSInfo(bean);
        String configData = new StringBuilder().append(version1).append(",").append(bean.getId()).append(",")
                .append(",S84").append(",").append(SurezenConst.OPER_STATE_SUCCESS).append(",").append(",")
                .append(bean.getSosText()).toString();
        /*判断手表是否在线，如果在线，就先下发设置，然后保存设置到服务器中，返回设置成功给客服端，
         * 如果手表不在线，则返回设置失败给客服端。
         */
        int sendStatus = SurezenSender.send(bean.getId(), configData);
        if (sendStatus == 1)
        {
            WbUpdate wu = new WbUpdate();
            wu.setUpdateTime(now);
            wu.setConfigItem(ConstWb.WB_TYPE_SOS);
            wu.setStatus(Constant.WB_SERVICE_FOR_SEND);
            wu.setWbId(bean.getId());
            wu.setConfigData(configData);
            wu.setUpdateBy(bean.getUpdateBy());
            String wuId = wbUpdateDao.insert(wu);
            if (StringUtil.isNotEmpty(wuId))
            {
                return new Result(Result.STATE_SUCESS, "设置sos信息参数成功", wuId);
            }
            else
            {
                return new Result(Result.STATE_FAIL, "设置sos信息失败", wuId);
            }
        }
        else if (sendStatus == 2)
        {
            return new Result(Result.STATE_FAIL, "设置sos信息参数失败，腕表不在线", "");
        }
        else
        {
            return new Result(Result.STATE_FAIL, "设置sos信息参数失败，未知错误", "");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Result updateSOSConfig(WbConfig bean) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        //先找到腕表的配置id，然后通过上传设置，修改配置里面的东西，然后拼接到修改表中
        /* [V1.0.0, 865621453864925, 8ad8aca24d904e71014d944e2ec60010,S82,1,2015-11-20 16:35:12, num1,num2,num3]

                 Num1:第一个 SOS 号码，如13654673567。
                 Num2:第二个 SOS 号码，如13654673568。
                 Num3:第三个 SOS 号码，如13654673569。
         */
        wbConfigDao.updateSOSConfig(bean);
        String configData = new StringBuilder().append(version1).append(",").append(bean.getId()).append(",")
                .append(",S82").append(",").append(SurezenConst.OPER_STATE_SUCCESS).append(",").append(",")
                .append(bean.getSosNums()).toString();
        /*判断手表是否在线，如果在线，就先下发设置，然后保存设置到服务器中，返回设置成功给客服端，
         * 如果手表不在线，则返回设置失败给客服端。
         */
        int sendStatus = SurezenSender.send(bean.getId(), configData);
        if (sendStatus == 1)
        {
            WbUpdate wu = new WbUpdate();
            wu.setUpdateTime(now);
            wu.setConfigItem(ConstWb.WB_TYPE_SOS);
            wu.setStatus(Constant.WB_SERVICE_FOR_SEND);
            wu.setWbId(bean.getId());
            wu.setConfigData(configData);
            wu.setUpdateBy(bean.getUpdateBy());
            String wuId = wbUpdateDao.insert(wu);
            if (StringUtil.isNotEmpty(wuId))
            {
                return new Result(Result.STATE_SUCESS, "设置sos号码参数成功", wuId);
            }
            else
            {
                return new Result(Result.STATE_FAIL, "设置号码失败", wuId);
            }
        }
        else if (sendStatus == 2)
        {
            return new Result(Result.STATE_FAIL, "设置sos号码参数失败，腕表不在线", "");
        }
        else
        {
            return new Result(Result.STATE_FAIL, "设置sos号码参数失败，未知错误", "");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Result updatePhonesConfig(WbConfig bean) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        //先找到腕表的配置id，然后通过上传设置，修改配置里面的东西，然后拼接到修改表中
        /* [V1.0.0, 865621453864925, 8ad8aca24d904e71014d944e2ec60010,S91,1,2015-11-20 16:35:12, num1,num2,num3,num4]

         Num1:第一个亲情号码，如13654673567。
         Num2:第二个亲情号码，如13654673568。
         Num3:第三个亲情号码，如13654673569。
         num4:第三个亲情号码，如13654673570。
         2.3.12亲情号码更改
         */
        wbConfigDao.updatePhonesConfig(bean);
        String configData = new StringBuilder().append(version1).append(",").append(bean.getId()).append(",")
                .append(",S91").append(",").append(SurezenConst.OPER_STATE_SUCCESS).append(",").append(",")
                .append(bean.getPhones()).toString();
        /*判断手表是否在线，如果在线，就先下发设置，然后保存设置到服务器中，返回设置成功给客服端，
         * 如果手表不在线，则返回设置失败给客服端。
         */
        int sendStatus = SurezenSender.send(bean.getId(), configData);
        if (sendStatus == 1)
        {
            WbUpdate wu = new WbUpdate();
            wu.setUpdateTime(now);
            wu.setConfigItem(ConstWb.WB_TYPE_PHONES);
            wu.setStatus(Constant.WB_SERVICE_FOR_SEND);
            wu.setWbId(bean.getId());
            wu.setConfigData(configData);
            wu.setUpdateBy(bean.getUpdateBy());
            String wuId = wbUpdateDao.insert(wu);
            if (StringUtil.isNotEmpty(wuId))
            {
                return new Result(Result.STATE_SUCESS, "设置亲情号码参数成功", wuId);
            }
            else
            {
                return new Result(Result.STATE_FAIL, "设置亲情号码失败", wuId);
            }
        }
        else if (sendStatus == 2)
        {
            return new Result(Result.STATE_FAIL, "设置亲情号码参数失败，腕表不在线", "");
        }
        else
        {
            return new Result(Result.STATE_FAIL, "设置亲情号码参数失败，未知错误", "");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Result updateEleFence(WbConfig bean)
    {
        // 向地图服务推送围栏信息
        int mr = MpSender.sendFenceSet(ConstMap.CUST_CODE_PREFIX + bean.getCustId(), bean.getEleLongitude(),
                bean.getEleLatitude(), bean.getEleDistance());
        if (mr != MpConst.OPER_STATE_SUCCESS)
        {
            logger.debug("向地图服务发送告警围栏信息失败，错误码：" + mr);
        }
        Timestamp now = new Timestamp(System.currentTimeMillis());
        bean.setEleTime(now);
        int c = wbConfigDao.updateEleFence(bean);
        if (c > 0)
        {
            return new Result(Result.STATE_SUCESS, "设置电子围栏参数成功", c);
        }
        else
        {
            return new Result(Result.STATE_FAIL, "设置电子围栏参数失败", c);
        }
    }

}
