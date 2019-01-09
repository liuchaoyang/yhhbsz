package com.yzxt.tran;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.yzxt.fw.common.BaseAction;
import com.yzxt.yh.constant.ConstDevice;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.chk.bean.BloodSugar;
import com.yzxt.yh.module.chk.bean.PressurePulse;
import com.yzxt.yh.module.chk.bean.UserDevice;
import com.yzxt.yh.module.chk.service.BloodSugarService;
import com.yzxt.yh.module.chk.service.CheckDataService;
import com.yzxt.yh.module.chk.service.PressurePulseService;
import com.yzxt.yh.module.chk.service.UserDeviceService;
import com.yzxt.yh.util.DecimalUtil;
import com.yzxt.yh.util.StringUtil;

/**
 * 用户未登录数据上传处理类
 *
 */
public class GsmDataAction extends BaseAction
{
    private static final long serialVersionUID = 1L;

    private Logger logger = Logger.getLogger(GsmDataAction.class);

    // private static JsonParser parser = new JsonParser();
    // private static SimpleDateFormat urionDf = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss");
    // private static SimpleDateFormat cDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private CheckDataService checkDataService;

    private UserDeviceService userDeviceService;

    private PressurePulseService pressurePulseService;

    private BloodSugarService bloodSugarService;

    public CheckDataService getCheckDataService()
    {
        return checkDataService;
    }

    public void setCheckDataService(CheckDataService checkDataService)
    {
        this.checkDataService = checkDataService;
    }

    public UserDeviceService getUserDeviceService()
    {
        return userDeviceService;
    }

    public void setUserDeviceService(UserDeviceService userDeviceService)
    {
        this.userDeviceService = userDeviceService;
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

    /**
     * 优瑞恩电子臂式电子血压计
     * 
     * http请求方式:GET
     * 请求URL格式:
     * http://www.urion.cn/send.php?imei=394848483839313&tel=13714728810&iccid=01234567890123456789&imsi=012345678901234&user=1&sys=125&dia=85&pul=65&ano=0&time="2014-11-21/15:20:12" 
     * User：用户编号,Sys:高压,Dia：低压,Pul:心跳,Ano:是否心律不齐。0：正常；1：心律不齐,
     * Time:测量时间,Imei: 模块的imei 号码
     * 
     * 在传送血压值后, 服务器回复OK+系统时间, 这时更新血压计时间.
     * 另外闹铃参数同步返回。系统平台返回格式：OK201412220930&18:30:1&08:15:2&19:15:0&12:12:1，
     * 时间后面的012分别表示：0双用户，1爸爸，2妈妈，闹铃时间。可以任意组合定义。
     */
    public void bpUGprs() throws IOException
    {
        HttpServletResponse response = super.getResponse();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        try
        {
            String imei = request.getParameter("imei");
            String user = request.getParameter("user");
            // String time = request.getParameter("time");
            String sys = request.getParameter("sys");
            String dia = request.getParameter("dia");
            String pul = request.getParameter("pul");
            imei = StringUtil.ensureNoWarp(imei, '"');
            if (StringUtil.isEmpty(imei) || StringUtil.isEmpty(user) || StringUtil.isEmpty(sys)
                    || StringUtil.isEmpty(dia))
            {
                response.getWriter().write("Paramter value error.");
                response.getWriter().close();
                return;
            }
            Map<String, Object> deviceInfo = new HashMap<String, Object>();
            deviceInfo.put("deviceType", new String[]
            {ConstDevice.DEVICE_TYPE_BP_YZXT, ConstDevice.DEVICE_TYPE_BP_URION_GPRS});
            deviceInfo.put("deviceSn", imei);
            deviceInfo.put("deviceSnExt", user);
            UserDevice userDevice = userDeviceService.getByDeviceInfo(deviceInfo);
            if (userDevice == null)
            {
                // 写返回值
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
                response.getWriter().write("OK" + df.format(now));
                response.getWriter().close();
                logger.debug("优瑞恩GPRS电子血压计上传数据失败，imei:" + imei + ", user:" + user + "的用户设备绑定信息无效。");
                return;
            }
            // 数据校验正常，开始保存血压信息
            PressurePulse data = new PressurePulse();
            data.setCustId(userDevice.getCustId());
            data.setCheckTime(now);
            data.setDeviceName(userDevice.getDeviceType());
            data.setDeviceMac(imei);
            data.setCheckType(Constant.DATA_CHECK_TYPE_DIRECT);
            data.setSBP(Integer.parseInt(sys));
            data.setDBP(Integer.parseInt(dia));
            if (StringUtil.isNotEmpty(pul))
            {
                data.setPulse(Integer.parseInt(pul));
            }
            pressurePulseService.save(data);
            // 写返回值
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
            response.getWriter().write("OK" + df.format(now));
            response.getWriter().close();
        }
        catch (Exception e)
        {
            response.getWriter().write("Server error, please retry later.");
            response.getWriter().close();
        }
    }

    /**
     * 爱奥乐血压检测设备
     * 
    * 请求信息,参数名“data”
    * Weakup    VER User    Customer    Model Code  Device Type Serial number   SYS DIA PUL DataTime
    * 起始碼 版本号 用户号 客戶碼 机型代码    机种码 系列号                                    高压          低压         心率            年          月         日            时           分          SUM      校驗碼
    * 2     2    1     2     2       2     9                   3       3       3       2     2     2     2      2     4
    * 5A    23   0     00~99 0~FF    00~FF A00000000~L99999955 000~300 000-250 000~200 12~99 01~12 01~31 00~23  00~59 XXXX
    * 5A    24   1     21    06      01    A00027815           129     083     076     14    07    08    10     45    07E1   _460027031902771_114.297790,22.690753FFFFF
    * 用户请求数据：5A 24 1 21 0 601A00027815 129 083 076 140708104507E1_460027031902771_114.297790,22.690753FFFFF
    * -号后面是IMSI号与GPRS信息，如果以后不用GPRS部分，将全部填充为FF。
    * 
    * 回复信息
    * 起始碼           IP              端口      Sum1    年   月   日   时   分   Sum2    结束码
    * +   I   P   2A  79  EC  87  4E  1F  69  0E  07  08  0A  27  2C  O   K
    *             42  121 236 135 8014                高位加入单位标志       
    * Sum1:   为IP与端口每字节之HEX异或.  如示例Sum1＝69H = 2A^79^EC^87^4E^1F.
    * Sum2：为时间数据每字节之HEX异或，如示例Sum2＝2CH = 0E^07^08^0A^27
    * 样例，一笔正常的返回数据： +IP2A79EC874E1F690E07080A272COK
     */
    public void ialXy()
    {
        HttpServletResponse response = super.getResponse();
        HttpServletRequest request = super.getRequest();
        String paramData = request.getParameter("data");
        //String paramData = "5A241210601A00027815129083076140708104507E1_460027031902771_114.297790";
        // 返回值IP或端口部分，因为HTTP不需要，这里固定写死
        StringBuilder rtnStr = new StringBuilder("+IP2A79EC874E1F69");
        // 补充服务器时间
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR) % 100;
        int month = cal.get(Calendar.MONTH) + 1;
        int date = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int chkCode = year ^ month ^ date ^ hour ^ minute;
        rtnStr.append(DecimalUtil.toHexString(year, 2)).append(DecimalUtil.toHexString(month, 2))
                .append(DecimalUtil.toHexString(date, 2)).append(DecimalUtil.toHexString(hour, 2))
                .append(DecimalUtil.toHexString(minute, 2)).append(DecimalUtil.toHexString(chkCode, 2));
        try
        {
            // 机种码
            String deviceType = paramData.substring(9, 11);
            // 系列号
            String serialNumber = paramData.substring(11, 20);
            // 用户号
            String userNumber = paramData.substring(4, 5);
            // 检测时间
            // String checkTimeStr = paramData.substring(29, 39);
            // Timestamp checkTime = new Timestamp(ialDf.parse(checkTimeStr).getTime());
            Timestamp checkTime = new Timestamp(System.currentTimeMillis());
            if ("01".equals(deviceType))
            // 血压设备
            {
                // 用户绑定信息
                Map<String, Object> deviceInfo = new HashMap<String, Object>();
                deviceInfo.put("deviceType", ConstDevice.DEVICE_TYPE_BP_IAL);
                deviceInfo.put("deviceSn", serialNumber);
                deviceInfo.put("deviceSnExt", userNumber);
                UserDevice userDevice = userDeviceService.getByDeviceInfo(deviceInfo);
                if (userDevice == null)
                {
                    logger.error("爱奥乐血压计上传数据失败，系列号:" + serialNumber + ", 用户号:" + userNumber + "的用户设备绑定信息无效。");
                    rtnStr.append("E2");
                    response.getWriter().write(rtnStr.toString());
                    response.getWriter().close();
                    return;
                }
                // 高压
                String sys = paramData.substring(20, 23);
                // 低压
                String dia = paramData.substring(23, 26);
                // 心率
                String pul = paramData.substring(26, 29);
                // 数据校验正常，开始保存血压信息
                PressurePulse data = new PressurePulse();
                data.setCustId(userDevice.getCustId());
                data.setCheckTime(checkTime);
                data.setDeviceName(ConstDevice.DEVICE_TYPE_BP_IAL);
                data.setDeviceMac(serialNumber);
                data.setCheckType(Constant.DATA_CHECK_TYPE_DIRECT);
                data.setSBP(Integer.parseInt(sys));
                data.setDBP(Integer.parseInt(dia));
                if (StringUtil.isNotEmpty(pul))
                {
                    data.setPulse(Integer.parseInt(pul));
                }
                pressurePulseService.save(data);
            }
            else if ("02".equals(deviceType))
            {
                // 用户绑定信息
                Map<String, Object> deviceInfo = new HashMap<String, Object>();
                deviceInfo.put("deviceType", ConstDevice.DEVICE_TYPE_SG_IAL);
                deviceInfo.put("deviceSn", serialNumber);
                deviceInfo.put("deviceSnExt", userNumber);
                UserDevice userDevice = userDeviceService.getByDeviceInfo(deviceInfo);
                if (userDevice == null)
                {
                    logger.error("爱奥乐血糖计上传数据失败，系列号:" + serialNumber + ", 用户号:" + userNumber + "的用户设备绑定信息无效。URL:"
                            + request.getRequestURL());
                    rtnStr.append("E3");
                    response.getWriter().write(rtnStr.toString());
                    response.getWriter().close();
                    return;
                }
                // 血糖
                String sug = paramData.substring(26, 29);
                double sugVal = Double.parseDouble(sug) / 18d;
                // 数据校验正常，开始保存血压信息
                BloodSugar data = new BloodSugar();
                data.setCustId(userDevice.getCustId());
                data.setCheckTime(checkTime);
                data.setDeviceName(ConstDevice.DEVICE_TYPE_SG_IAL);
                data.setDeviceMac(serialNumber);
                data.setCheckType(Constant.DATA_CHECK_TYPE_DIRECT);
                data.setBloodSugar(sugVal);
                data.setBloodSugarType(1);
                bloodSugarService.save(data);
            }
            else
            {
                logger.error("爱奥乐上传数据失败，不支持此设备类型机种码 ：" + deviceType + "，data=" + paramData);
                rtnStr.append("E1");
                response.getWriter().write(rtnStr.toString());
                response.getWriter().close();
                return;
            }
            // 返回结果
            rtnStr.append("OK");
            response.getWriter().write(rtnStr.toString());
            response.getWriter().close();
        }
        catch (Exception e)
        {
            logger.error("爱奥乐检测设备上传数据错误，data=" + paramData + "，URL:" + request.getRequestURL(), e);
        }
    }

}
