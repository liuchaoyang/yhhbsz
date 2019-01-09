package com.yzxt.yh.eif.surezen;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import com.yzxt.fw.util.IdGenerator;
import com.yzxt.yh.constant.ConstDevice;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.eif.mp.LacConvertResult;
import com.yzxt.yh.eif.mp.MpProxy;
import com.yzxt.yh.module.chk.bean.BloodSugar;
import com.yzxt.yh.module.chk.bean.PressurePulse;
import com.yzxt.yh.module.chk.bean.Pulse;
import com.yzxt.yh.module.chk.bean.Temperature;
import com.yzxt.yh.module.chk.bean.UserDevice;
import com.yzxt.yh.module.chk.service.BloodSugarService;
import com.yzxt.yh.module.chk.service.PressurePulseService;
import com.yzxt.yh.module.chk.service.PulseService;
import com.yzxt.yh.module.chk.service.TemperatureService;
import com.yzxt.yh.module.chk.service.UserDeviceService;
import com.yzxt.yh.module.cli.bean.CustPosition;
import com.yzxt.yh.module.cli.bean.Sleep;
import com.yzxt.yh.module.cli.bean.Step;
import com.yzxt.yh.module.cli.service.CustPositionService;
import com.yzxt.yh.module.cli.service.RemindService;
import com.yzxt.yh.module.cli.service.SleepQualityService;
import com.yzxt.yh.module.cli.service.SleepService;
import com.yzxt.yh.module.cli.service.StepService;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.service.CustomerService;
import com.yzxt.yh.util.StringUtil;

public class SurezenHandle
{
    private static Logger logger = Logger.getLogger(SurezenHandle.class);
    static SimpleDateFormat timeF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private UserDeviceService userDeviceService;
    private CustomerService customerService;
    private PulseService pulseService;
    private TemperatureService temperatureService;
    private PressurePulseService pressurePulseService;
    private BloodSugarService bloodSugarService;
    private CustPositionService custPositionService;
    private StepService stepService;
    private SleepService sleepService;
    private SleepQualityService sleepQualityService;
    private RemindService remindService;

    public UserDeviceService getUserDeviceService()
    {
        return userDeviceService;
    }

    public void setUserDeviceService(UserDeviceService userDeviceService)
    {
        this.userDeviceService = userDeviceService;
    }

    public CustomerService getCustomerService()
    {
        return customerService;
    }

    public void setCustomerService(CustomerService customerService)
    {
        this.customerService = customerService;
    }

    public PulseService getPulseService()
    {
        return pulseService;
    }

    public void setPulseService(PulseService pulseService)
    {
        this.pulseService = pulseService;
    }

    public TemperatureService getTemperatureService()
    {
        return temperatureService;
    }

    public void setTemperatureService(TemperatureService temperatureService)
    {
        this.temperatureService = temperatureService;
    }

    public PressurePulseService getPressurePulseService()
    {
        return pressurePulseService;
    }

    public void setPressurePulseService(PressurePulseService pressurePulseService)
    {
        this.pressurePulseService = pressurePulseService;
    }

    public BloodSugarService getBloodSugarService()
    {
        return bloodSugarService;
    }

    public void setBloodSugarService(BloodSugarService bloodSugarService)
    {
        this.bloodSugarService = bloodSugarService;
    }

    public CustPositionService getCustPositionService()
    {
        return custPositionService;
    }

    public void setCustPositionService(CustPositionService custPositionService)
    {
        this.custPositionService = custPositionService;
    }

    public StepService getStepService()
    {
        return stepService;
    }

    public void setStepService(StepService stepService)
    {
        this.stepService = stepService;
    }

    public SleepService getSleepService()
    {
        return sleepService;
    }

    public void setSleepService(SleepService sleepService)
    {
        this.sleepService = sleepService;
    }

    public SleepQualityService getSleepQualityService()
    {
        return sleepQualityService;
    }

    public void setSleepQualityService(SleepQualityService sleepQualityService)
    {
        this.sleepQualityService = sleepQualityService;
    }

    public RemindService getRemindService()
    {
        return remindService;
    }

    public void setRemindService(RemindService remindService)
    {
        this.remindService = remindService;
    }

    /**
     * 登录
     * 
     * 手表开机后，发送登录信息 T1，如：
     * V1.0.0,865621453864925,,T1,1,2015-11-20 16:35:12
     * 服务器响应后下发 S1 指令：
     * V1.0.0,865621453864925,8ad8aca24d904e71014d944e2ec60010,S1,1,2015-11-20 16:35:12,serverAddr:port
     * S1 中含标准时间即可(红色标识时间)用于对时。
     */
    public String login(IoSession ioSession, String[] reqInfos, String str, String operCode)
    {
        try
        {
            String token = IdGenerator.get32UUID();
            User user = createSession(ioSession, token, reqInfos[1]);
            if (user == null)
            {
                return new StringBuilder().append(reqInfos[0]).append(",").append(reqInfos[1]).append(",,S")
                        .append(operCode).append(",").append(SurezenConst.OPER_STATE_AUTH_FAIL).append(",")
                        .append(timeF.format(new Date())).append(",").toString();
            }
            // 回应腕表
            return new StringBuilder().append(reqInfos[0]).append(",").append(reqInfos[1]).append(",").append(token)
                    .append(",S").append(operCode).append(",").append(SurezenConst.OPER_STATE_SUCCESS).append(",")
                    .append(timeF.format(new Date())).append(",").toString();
        }
        catch (Exception e)
        {
            logger.error("河北循证腕表登录出错，" + str, e);
            return new StringBuilder().append(reqInfos[0]).append(",").append(reqInfos[1]).append(",,S")
                    .append(operCode).append(",").append(SurezenConst.OPER_STATE_UNKNOW_ERROR).append(",")
                    .append(timeF.format(new Date())).append(",").toString();
        }
    }

    /**
     * 连接心跳
     * 
     * V1.0.0,865621453864925,8ad8aca24d904e71014d944e2ec60010,T2,1,2015-11-20 16:35:12
     * 服务器响应后下发 S2 指令：
     * V1.0.0,865621453864925,8ad8aca24d904e71014d944e2ec60010,S2,1,2015-11-20 16:35:12
     */
    public String beat(IoSession ioSession, String[] reqInfos, String str, String operCode)
    {
        try
        {
            // 验证用户
            if (StringUtil.isEmpty(reqInfos[2]))
            {
                return new StringBuilder().append(reqInfos[0]).append(",").append(reqInfos[1]).append(",")
                        .append(reqInfos[2]).append(",S").append(operCode).append(",")
                        .append(SurezenConst.OPER_STATE_AUTH_FAIL).append(",").append(timeF.format(new Date()))
                        .toString();
            }
            return new StringBuilder().append(reqInfos[0]).append(",").append(reqInfos[1]).append(",")
                    .append(reqInfos[2]).append(",S").append(operCode).append(",")
                    .append(SurezenConst.OPER_STATE_SUCCESS).append(",").append(timeF.format(new Date())).toString();
        }
        catch (Exception e)
        {
            logger.error("河北循证腕表连接心跳出错，" + str, e);
            return new StringBuilder().append(reqInfos[0]).append(",").append(reqInfos[1]).append(",")
                    .append(reqInfos[2]).append(",S").append(operCode).append(",")
                    .append(SurezenConst.OPER_STATE_UNKNOW_ERROR).append(",").append(timeF.format(new Date()))
                    .toString();
        }
    }

    /**
     * 上传脉搏记录
     * 
     * 脉搏
     * V1.0.0,865621453864925,8ad8aca24d904e71014d944e2ec60010,T11,2015-11-20 16:35:12,1,pulse
     * pluse:脉搏数，整数
     * 服务器应答：
     * V1.0.0,865621453864925,8ad8aca24d904e71014d944e2ec60010,S11,1,2015-11-20 16:35:12
     */
    public String pulse(IoSession ioSession, String[] reqInfos, String str, String operCode)
    {
        try
        {
            // 获取用户信息
            User user = resumeSession(ioSession, reqInfos[2], reqInfos[1]);
            if (user == null)
            {
                return new StringBuilder().append(reqInfos[0]).append(",").append(reqInfos[1]).append(",")
                        .append(reqInfos[2]).append(",S").append(operCode).append(",")
                        .append(SurezenConst.OPER_STATE_AUTH_FAIL).append(",").append(timeF.format(new Date()))
                        .toString();
            }
            Integer pulseVal = reqInfos[6] != null && reqInfos[6].length() > 0 ? Integer.valueOf(reqInfos[6]) : null;
            if (pulseVal == null || pulseVal.intValue() <= 0)
            {
                return new StringBuilder().append(reqInfos[0]).append(",").append(reqInfos[1]).append(",")
                        .append(reqInfos[2]).append(",S").append(operCode).append(",")
                        .append(SurezenConst.OPER_STATE_INVALID_PARAM).append(",").append(timeF.format(new Date()))
                        .toString();
            }
            Pulse bean = new Pulse();
            bean.setCustId(user.getId());
            bean.setDeviceName(ConstDevice.DEVICE_TYPE_WA_SUREZEN);
            bean.setDeviceMac(reqInfos[1]);
            bean.setPulse(pulseVal);
            bean.setPulseType(Pulse.PULSE_TYPE_B);
            bean.setCheckTime(new Timestamp(timeF.parse(reqInfos[4]).getTime()));
            bean.setCheckType(Constant.DATA_CHECK_TYPE_DIVICE);
            bean.setAdditional(false);
            pulseService.save(bean);
            return new StringBuilder().append(reqInfos[0]).append(",").append(reqInfos[1]).append(",")
                    .append(reqInfos[2]).append(",S").append(operCode).append(",")
                    .append(SurezenConst.OPER_STATE_SUCCESS).append(",").append(timeF.format(new Date())).toString();
        }
        catch (Exception e)
        {
            logger.error("河北循证上传用户脉搏数据出错，" + str, e);
            return new StringBuilder().append(reqInfos[0]).append(",").append(reqInfos[1]).append(",")
                    .append(reqInfos[2]).append(",S").append(operCode).append(",")
                    .append(SurezenConst.OPER_STATE_UNKNOW_ERROR).append(",").append(timeF.format(new Date()))
                    .toString();
        }
    }

    /**
     * 温度
     * 
     * V1.0.0,865621453864925,8ad8aca24d904e71014d944e2ec60010,T12,1,2015-11-20 16:35:12,checkTime,temp
     * checkTime：检测时间，如2015-11-20 16:35:10。
     * Temp:温度值,如36.8。
     * 
     * 服务器应答： 
     * V1.0.0,865621453864925,8ad8aca24d904e71014d944e2ec60010,S12,1
     */
    public String temperature(IoSession ioSession, String[] reqInfos, String str, String operCode)
    {
        try
        {
            // 获取用户信息
            User user = resumeSession(ioSession, reqInfos[2], reqInfos[1]);
            if (user == null)
            {
                return new StringBuilder().append(reqInfos[0]).append(",").append(reqInfos[1]).append(",")
                        .append(reqInfos[2]).append(",S").append(operCode).append(",")
                        .append(SurezenConst.OPER_STATE_AUTH_FAIL).append(",").append(timeF.format(new Date()))
                        .toString();
            }
            Temperature bean = new Temperature();
            bean.setCustId(user.getId());
            bean.setDeviceName(ConstDevice.DEVICE_TYPE_WA_SUREZEN);
            bean.setDeviceMac(reqInfos[1]);
            bean.setTemperature(Double.valueOf(reqInfos[7]));
            bean.setCheckTime(new Timestamp(timeF.parse(reqInfos[6]).getTime()));
            bean.setCheckType(Constant.DATA_CHECK_TYPE_DIVICE);
            temperatureService.save(bean);
            return new StringBuilder().append(reqInfos[0]).append(",").append(reqInfos[1]).append(",")
                    .append(reqInfos[2]).append(",S").append(operCode).append(",")
                    .append(SurezenConst.OPER_STATE_SUCCESS).append(",").append(timeF.format(new Date())).toString();
        }
        catch (Exception e)
        {
            logger.error("河北循证上传用户温度数据出错，" + str, e);
            return new StringBuilder().append(reqInfos[0]).append(",").append(reqInfos[1]).append(",")
                    .append(reqInfos[2]).append(",S").append(operCode).append(",")
                    .append(SurezenConst.OPER_STATE_UNKNOW_ERROR).append(",").append(timeF.format(new Date()))
                    .toString();
        }
    }

    /**
     * 血压
     * V1.0.0,865621453864925,8ad8aca24d904e71014d944e2ec60010,T13,1,2015-11-20 16:35:12,checkTime,systolice pressures,diastolic pressures,pulse
     * checkTime：检测时间，如2015-11-20 16:35:10。
     * blood_pressure：数据类型
     * systolice pressures：收缩压
     * diastolic pressures：舒张压
     * pulse：脉搏
     */
    public String pressure(IoSession ioSession, String[] reqInfos, String str, String operCode)
    {
        try
        {
            // 获取用户信息
            User user = resumeSession(ioSession, reqInfos[2], reqInfos[1]);
            if (user == null)
            {
                return new StringBuilder().append(reqInfos[0]).append(",").append(reqInfos[1]).append(",")
                        .append(reqInfos[2]).append(",S").append(operCode).append(",")
                        .append(SurezenConst.OPER_STATE_AUTH_FAIL).append(",").append(timeF.format(new Date()))
                        .toString();
            }
            PressurePulse bean = new PressurePulse();
            bean.setCustId(user.getId());
            bean.setDeviceName(ConstDevice.DEVICE_TYPE_WA_SUREZEN);
            bean.setDeviceMac(reqInfos[1]);
            bean.setSBP(Integer.valueOf(reqInfos[7]));
            bean.setDBP(Integer.valueOf(reqInfos[8]));
            if (reqInfos[9] != null && reqInfos[9].length() > 0)
            {
                bean.setPulse(Integer.valueOf(reqInfos[9]));
            }
            bean.setCheckTime(new Timestamp(timeF.parse(reqInfos[6]).getTime()));
            bean.setCheckType(Constant.DATA_CHECK_TYPE_DIVICE);
            pressurePulseService.save(bean);
            return new StringBuilder().append(reqInfos[0]).append(",").append(reqInfos[1]).append(",")
                    .append(reqInfos[2]).append(",S").append(operCode).append(",")
                    .append(SurezenConst.OPER_STATE_SUCCESS).append(",").append(timeF.format(new Date())).toString();
        }
        catch (Exception e)
        {
            logger.error("河北循证上传用户血压数据出错，" + str, e);
            return new StringBuilder().append(reqInfos[0]).append(",").append(reqInfos[1]).append(",")
                    .append(reqInfos[2]).append(",S").append(operCode).append(",")
                    .append(SurezenConst.OPER_STATE_UNKNOW_ERROR).append(",").append(timeF.format(new Date()))
                    .toString();
        }
    }

    /**
     * 血糖
     * V1.0.0,865621453864925,8ad8aca24d904e71014d944e2ec60010,T14,1,2015-11-20 16:35:12,checkTime,blood sugar,Blood glucose value
     * checkTime：检测时间，如2015-11-20 16:35:10。
     * Blood sugar：血糖类型，1-空腹血糖；2-餐前血糖；3-餐后血糖；4-服糖2小时后血
     * Blood glucose value：血糖值
     * 
     * 服务器应答：
     * V1.0.0,865621453864925,8ad8aca24d904e71014d944e2ec60010,S14,1
     */
    public String sugar(IoSession ioSession, String[] reqInfos, String str, String operCode)
    {
        try
        {
            // 获取用户信息
            User user = resumeSession(ioSession, reqInfos[2], reqInfos[1]);
            if (user == null)
            {
                return new StringBuilder().append(reqInfos[0]).append(",").append(reqInfos[1]).append(",")
                        .append(reqInfos[2]).append(",S").append(operCode).append(",")
                        .append(SurezenConst.OPER_STATE_AUTH_FAIL).append(",").append(timeF.format(new Date()))
                        .toString();
            }
            BloodSugar bean = new BloodSugar();
            bean.setCustId(user.getId());
            bean.setDeviceName(ConstDevice.DEVICE_TYPE_WA_SUREZEN);
            bean.setDeviceMac(reqInfos[1]);
            String bloodSugarTypeStr = reqInfos[7];
            if ("3".equals(bloodSugarTypeStr))
            {
                bean.setBloodSugarType(3);
            }
            else if ("4".equals(bloodSugarTypeStr))
            {
                bean.setBloodSugarType(4);
            }
            else
            {
                bean.setBloodSugarType(1);
            }
            bean.setBloodSugar(Double.valueOf(reqInfos[8]));
            bean.setCheckTime(new Timestamp(timeF.parse(reqInfos[6]).getTime()));
            bean.setCheckType(Constant.DATA_CHECK_TYPE_DIVICE);
            bloodSugarService.save(bean);
            return new StringBuilder().append(reqInfos[0]).append(",").append(reqInfos[1]).append(",")
                    .append(reqInfos[2]).append(",S").append(operCode).append(",")
                    .append(SurezenConst.OPER_STATE_SUCCESS).append(",").append(timeF.format(new Date())).toString();
        }
        catch (Exception e)
        {
            logger.error("河北循证上传用户血糖数据出错，" + str, e);
            return new StringBuilder().append(reqInfos[0]).append(",").append(reqInfos[1]).append(",")
                    .append(reqInfos[2]).append(",S").append(operCode).append(",")
                    .append(SurezenConst.OPER_STATE_UNKNOW_ERROR).append(",").append(timeF.format(new Date()))
                    .toString();
        }
    }

    /**
     * 定位LBS
     * V1.0.0,865621453864925,8ad8aca24d904e71014d944e2ec60010,T31,1,2015-11-20 16:35:12,checkTime,mnc,lac,cellid
     * checkTime：检测时间，如2015-11-20 16:35:10。
     * Mnc: 运营商网络号，如46001
     * Lac: 位置区编码，如9536
     * Cellid: 基站编号，如31805
     * 
     * 服务器应答：
     * V1.0.0,865621453864925,8ad8aca24d904e71014d944e2ec60010,S31,1,2015-11-20 16:35:12
     */
    public String lbs(IoSession ioSession, String[] reqInfos, String str, String operCode)
    {
        try
        {
            // 获取用户信息
            User user = resumeSession(ioSession, reqInfos[2], reqInfos[1]);
            if (user == null)
            {
                return new StringBuilder().append(reqInfos[0]).append(",").append(reqInfos[1]).append(",")
                        .append(reqInfos[2]).append(",S").append(operCode).append(",")
                        .append(SurezenConst.OPER_STATE_AUTH_FAIL).append(",").append(timeF.format(new Date()))
                        .toString();
            }
            CustPosition bean = new CustPosition();
            bean.setCustId(user.getId());
            bean.setDeviceName(ConstDevice.DEVICE_TYPE_WA_SUREZEN);
            bean.setDeviceMac(reqInfos[1]);
            bean.setMnc(reqInfos[7]);
            bean.setLac(reqInfos[8]);
            bean.setCellid(reqInfos[9]);
            bean.setPositionMode(Constant.POSITION_MODE_LBS);
            bean.setCheckTime(new Timestamp(timeF.parse(reqInfos[6]).getTime()));
            bean.setCheckType(Constant.DATA_CHECK_TYPE_DIVICE);
            // 通过LBS数据获取GPS数据
            MpProxy mpServer = MpProxy.getInstance();
            if (mpServer != null)
            {
                LacConvertResult lcr = MpProxy.getInstance().lbsToGps(bean.getMnc().substring(0, 3),
                        bean.getMnc().substring(3), bean.getLac(), bean.getCellid());
                if (LacConvertResult.STATUS_SUCCESS == lcr.getStatus())
                {
                    bean.setLongitude(lcr.getLongitude());
                    bean.setLatitude(lcr.getLatitude());
                }
                else if (lcr != null)
                {
                    Object errorMsg = lcr.getErrorMsg();
                    if (errorMsg instanceof Exception)
                    {
                        logger.error("地图LBS转换失败，错误状态码：" + "，mnc：" + bean.getMnc() + "，lac：" + bean.getLac()
                                + "，cellid：" + bean.getCellid(), (Exception) errorMsg);
                    }
                    else
                    {
                        logger.error("地图LBS转换失败，错误状态码：" + "，错误消息：" + lcr.getErrorMsg() + "，mnc：" + bean.getMnc()
                                + "，lac：" + bean.getLac() + "，cellid：" + bean.getCellid());
                    }
                }
            }
            else
            {
                logger.error("地图服务未初始化，LBS转换失败.");
            }
            custPositionService.save(bean);
            return new StringBuilder().append(reqInfos[0]).append(",").append(reqInfos[1]).append(",")
                    .append(reqInfos[2]).append(",S").append(operCode).append(",")
                    .append(SurezenConst.OPER_STATE_SUCCESS).append(",").append(timeF.format(new Date())).toString();
        }
        catch (Exception e)
        {
            logger.error("河北循证上传用户定位LBS数据出错，" + str, e);
            return new StringBuilder().append(reqInfos[0]).append(",").append(reqInfos[1]).append(",")
                    .append(reqInfos[2]).append(",S").append(operCode).append(",")
                    .append(SurezenConst.OPER_STATE_UNKNOW_ERROR).append(",").append(timeF.format(new Date()))
                    .toString();
        }
    }

    /**
     * 定位 GPS
     * V1.0.0,865621453864925,8ad8aca24d904e71014d944e2ec60010,T32,1,2015-11-20 16:35:12,checkTime,longitude,latitude,height,speed,direction
     * checkTime：检测时间，如2015-11-20 16:35:10。
     * Longitude:经度。
     * Latitude：纬度。
     * Height：高度。
     * Speed：速度。
     * Direction：方向。
     * 
     * 服务器应答：
     * V1.0.0,865621453864925,8ad8aca24d904e71014d944e2ec60010,S32,1,2015-11-20 16:35:12
     */
    public String gps(IoSession ioSession, String[] reqInfos, String str, String operCode)
    {
        try
        {
            // 获取用户信息
            User user = resumeSession(ioSession, reqInfos[2], reqInfos[1]);
            if (user == null)
            {
                return new StringBuilder().append(reqInfos[0]).append(",").append(reqInfos[1]).append(",")
                        .append(reqInfos[2]).append(",S").append(operCode).append(",")
                        .append(SurezenConst.OPER_STATE_AUTH_FAIL).append(",").append(timeF.format(new Date()))
                        .toString();
            }
            CustPosition bean = new CustPosition();
            bean.setCustId(user.getId());
            bean.setDeviceName(ConstDevice.DEVICE_TYPE_WA_SUREZEN);
            bean.setDeviceMac(reqInfos[1]);
            bean.setLongitude(Double.valueOf(reqInfos[7]));
            bean.setLatitude(Double.valueOf(reqInfos[8]));
            if (reqInfos[9] != null && reqInfos[9].length() > 0)
            {
                bean.setAltitude(Integer.valueOf(reqInfos[9]));
            }
            if (reqInfos[10] != null && reqInfos[10].length() > 0)
            {
                bean.setSpeed(Double.valueOf(reqInfos[10]));
            }
            if (reqInfos[11] != null && reqInfos[11].length() > 0)
            {
                bean.setDirection(Integer.valueOf(reqInfos[11]));
            }
            bean.setPositionMode(Constant.POSITION_MODE_GPS);
            bean.setCheckTime(new Timestamp(timeF.parse(reqInfos[6]).getTime()));
            bean.setCheckType(Constant.DATA_CHECK_TYPE_DIVICE);
            custPositionService.save(bean);
            return new StringBuilder().append(reqInfos[0]).append(",").append(reqInfos[1]).append(",")
                    .append(reqInfos[2]).append(",S").append(operCode).append(",")
                    .append(SurezenConst.OPER_STATE_SUCCESS).append(",").append(timeF.format(new Date())).toString();
        }
        catch (Exception e)
        {
            logger.error("河北循证上传用户定位GPS数据出错，" + str, e);
            return new StringBuilder().append(reqInfos[0]).append(",").append(reqInfos[1]).append(",")
                    .append(reqInfos[2]).append(",S").append(operCode).append(",")
                    .append(SurezenConst.OPER_STATE_UNKNOW_ERROR).append(",").append(timeF.format(new Date()))
                    .toString();
        }
    }

    /**
     * 计步
     * V1.0.0,865621453864925,8ad8aca24d904e71014d944e2ec60010,T33,1,2015-11-20 16:35:12,checkTime,totalsteps,totaldis,calories
     * checkTime：检测时间，如2015-11-20 16:35:10。
     * Totalsteps：总步数，如106。
     * Totaldis：总距离，62。
     * Calories：总卡路里，3.89。
     * 
     * 服务器应答： 
     * V1.0.0,865621453864925,8ad8aca24d904e71014d944e2ec60010,S33,1,2015-11-20 16:35:12
     */
    public String step(IoSession ioSession, String[] reqInfos, String str, String operCode)
    {
        try
        {
            // 获取用户信息
            User user = resumeSession(ioSession, reqInfos[2], reqInfos[1]);
            if (user == null)
            {
                return new StringBuilder().append(reqInfos[0]).append(",").append(reqInfos[1]).append(",")
                        .append(reqInfos[2]).append(",S").append(operCode).append(",")
                        .append(SurezenConst.OPER_STATE_AUTH_FAIL).append(",").append(timeF.format(new Date()))
                        .toString();
            }
            Step bean = new Step();
            bean.setCustId(user.getId());
            bean.setDeviceName(ConstDevice.DEVICE_TYPE_WA_SUREZEN);
            bean.setDeviceMac(reqInfos[1]);
            bean.setTotalSteps(Integer.valueOf(reqInfos[7]));
            bean.setTotalDis(Integer.valueOf(reqInfos[8]));
            bean.setCalories(Double.valueOf(reqInfos[9]));
            bean.setCheckTime(new Timestamp(timeF.parse(reqInfos[6]).getTime()));
            bean.setCheckType(Constant.DATA_CHECK_TYPE_DIVICE);
            stepService.save(bean);
            return new StringBuilder().append(reqInfos[0]).append(",").append(reqInfos[1]).append(",")
                    .append(reqInfos[2]).append(",S").append(operCode).append(",")
                    .append(SurezenConst.OPER_STATE_SUCCESS).append(",").append(timeF.format(new Date())).toString();
        }
        catch (Exception e)
        {
            logger.error("河北循证上传用户计步数据出错，" + str, e);
            return new StringBuilder().append(reqInfos[0]).append(",").append(reqInfos[1]).append(",")
                    .append(reqInfos[2]).append(",S").append(operCode).append(",")
                    .append(SurezenConst.OPER_STATE_UNKNOW_ERROR).append(",").append(timeF.format(new Date()))
                    .toString();
        }
    }

    /**
     * 睡眠
     * V1.0.0,865621453864925,8ad8aca24d904e71014d944e2ec60010,T34,1,2015-11-20 16:35:12,start time,end time,Sleep quality
     * checkTime：检测时间，如2015-11-20 16:35:10。
     * start time：开始时间，如2015-11-08 20:00:50。
     * end time：结束时间，如2015-11-09 06:00:00。
     * Sleep quality: 睡眠质量， 如 0 - 没有睡眠数据；1 - 睡眠质量差； 2 - 一般； 3 - 良好。
     * 
     * 服务器应答：
     * V1.0.0,865621453864925,8ad8aca24d904e71014d944e2ec60010,S34,1,2015-11-20 16:35:12
     */
    public String sleep(IoSession ioSession, String[] reqInfos, String str, String operCode)
    {
        try
        {
            // 获取用户信息
            User user = resumeSession(ioSession, reqInfos[2], reqInfos[1]);
            if (user == null)
            {
                return new StringBuilder().append(reqInfos[0]).append(",").append(reqInfos[1]).append(",")
                        .append(reqInfos[2]).append(",S").append(operCode).append(",")
                        .append(SurezenConst.OPER_STATE_AUTH_FAIL).append(",").append(timeF.format(new Date()))
                        .toString();
            }
            Sleep bean = new Sleep();
            bean.setCustId(user.getId());
            bean.setDeviceName(ConstDevice.DEVICE_TYPE_WA_SUREZEN);
            bean.setDeviceMac(reqInfos[1]);
            bean.setStartTime(timeF.parse(reqInfos[6]));
            bean.setEndTime(timeF.parse(reqInfos[7]));
            String sleepQualityStr = reqInfos[8];
            if (sleepQualityStr != null && sleepQualityStr.length() > 0)
            {
                bean.setSleepQuality(Integer.valueOf(sleepQualityStr));
            }
            bean.setCheckTime(new Timestamp(timeF.parse(reqInfos[5]).getTime()));
            bean.setCheckType(Constant.DATA_CHECK_TYPE_DIVICE);
            sleepService.save(bean);
            return new StringBuilder().append(reqInfos[0]).append(",").append(reqInfos[1]).append(",")
                    .append(reqInfos[2]).append(",S").append(operCode).append(",")
                    .append(SurezenConst.OPER_STATE_SUCCESS).append(",").append(timeF.format(new Date())).toString();
        }
        catch (Exception e)
        {
            logger.error("河北循证上传用户睡眠数据出错，" + str, e);
            return new StringBuilder().append(reqInfos[0]).append(",").append(reqInfos[1]).append(",")
                    .append(reqInfos[2]).append(",S").append(operCode).append(",")
                    .append(SurezenConst.OPER_STATE_UNKNOW_ERROR).append(",").append(timeF.format(new Date()))
                    .toString();
        }
    }

    /**
     * 服务器未知下发
     * @param reqInfos
     * @param str
     * @return
     */
    public String otherS(String[] reqInfos, String str, String oper)
    {
        return new StringBuilder().append(reqInfos[0]).append(',').append(reqInfos[1]).append(',').append("0,")
                .append(reqInfos[3]).append(',').append(timeF.format(new Date())).append(",,").append(reqInfos[5])
                .append(",,T").append(oper.substring(1)).toString();
    }

    /**
     * 服务器未知上传
     * 
     * 服务器应答：
     * V1.0.0,865621453864925,8ad8aca24d904e71014d944e2ec60010,Sxxx,3,2015-11-20 16:35:12
     */
    public String otherWatchReq(IoSession ioSession, String[] reqInfos, String str, String operCode)
    {
        return new StringBuilder().append(reqInfos[0]).append(",").append(reqInfos[1]).append(",").append(reqInfos[2])
                .append(",S").append(operCode).append(",").append(SurezenConst.OPER_STATE_UNKNOW_COMMAND).append(",")
                .append(timeF.format(new Date())).toString();
    }

    /**
     * 获取用户信息
     * @param token不能为空，为登录时生产的值
     * @throws Exception 
     */
    private User createSession(IoSession ioSession, String token, String deviceNo) throws Exception
    {
        Map<String, Object> deviceInfo = new HashMap<String, Object>();
        deviceInfo.put("deviceType", ConstDevice.DEVICE_TYPE_WA_SUREZEN);
        deviceInfo.put("deviceSn", deviceNo);
        UserDevice userDevice = userDeviceService.getByDeviceInfo(deviceInfo);
        if (userDevice == null)
        {
            return null;
        }
        User user = new User();
        user.setId(userDevice.getCustId());
        ioSession.setAttribute(SurezenConst.SESSION_KEY_IMEI, deviceNo);
        ioSession.setAttribute(SurezenConst.SESSION_KEY_USER, user);
        ioSession.setAttribute(SurezenConst.SESSION_KEY_TOKEN, token);
        // 缓存回话信息
        SessionMap.getInstance().put(deviceNo, ioSession);
        return user;
    }

    /**
     * 恢复回话信息
     * @param token不能为空，为登录时生产的值
     * @throws Exception 
     */
    private User resumeSession(IoSession ioSession, String token, String deviceNo) throws Exception
    {
        if (token == null || token.length() == 0)
        {
            return null;
        }
        User user = (User) ioSession.getAttribute(SurezenConst.SESSION_KEY_USER);
        if (user != null && token.equals(ioSession.getAttribute(SurezenConst.SESSION_KEY_TOKEN)))
        {
            return user;
        }
        return createSession(ioSession, token, deviceNo);
    }

}
