package com.yzxt.tran;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletInputStream;

import org.apache.log4j.Logger;

import com.google.gson.JsonObject;
import com.yzxt.fw.common.BaseAction;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.util.GsonUtil;
import com.yzxt.yh.constant.ConstOrg;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.chk.bean.AnalysisUricAcid;
import com.yzxt.yh.module.chk.bean.BloodOxygen;
import com.yzxt.yh.module.chk.bean.BloodSugar;
import com.yzxt.yh.module.chk.bean.PressurePulse;
import com.yzxt.yh.module.chk.bean.Temperature;
import com.yzxt.yh.module.chk.service.AnalysisUricAcidService;
import com.yzxt.yh.module.chk.service.BloodOxygenService;
import com.yzxt.yh.module.chk.service.BloodSugarService;
import com.yzxt.yh.module.chk.service.PressurePulseService;
import com.yzxt.yh.module.chk.service.TemperatureService;
import com.yzxt.yh.module.sys.bean.Customer;
import com.yzxt.yh.module.sys.bean.User;
import com.yzxt.yh.module.sys.service.CustomerService;
import com.yzxt.yh.module.sys.service.UserService;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.StringUtil;

/**
 * 河北循证一 体 机同步数据上传处理类
 *
 */
public class SurezenDataAction extends BaseAction
{
    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(SurezenDataAction.class);
    private static SimpleDateFormat ctDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public final static int STATE_SUCESS = 0;
    public final static int STATE_FAIL = 1;
    public final static int STATE_CUST_REPEAT = 2;

    private CustomerService customerService;
    private UserService userService;
    private PressurePulseService pressurePulseService;
    private BloodOxygenService bloodOxygenService;
    private BloodSugarService bloodSugarService;
    private TemperatureService temperatureService;
    private AnalysisUricAcidService analysisUricAcidService;;

    public CustomerService getCustomerService()
    {
        return customerService;
    }

    public void setCustomerService(CustomerService customerService)
    {
        this.customerService = customerService;
    }

    public UserService getUserService()
    {
        return userService;
    }

    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }

    public PressurePulseService getPressurePulseService()
    {
        return pressurePulseService;
    }

    public void setPressurePulseService(PressurePulseService pressurePulseService)
    {
        this.pressurePulseService = pressurePulseService;
    }

    public BloodOxygenService getBloodOxygenService()
    {
        return bloodOxygenService;
    }

    public void setBloodOxygenService(BloodOxygenService bloodOxygenService)
    {
        this.bloodOxygenService = bloodOxygenService;
    }

    public BloodSugarService getBloodSugarService()
    {
        return bloodSugarService;
    }

    public void setBloodSugarService(BloodSugarService bloodSugarService)
    {
        this.bloodSugarService = bloodSugarService;
    }

    public TemperatureService getTemperatureService()
    {
        return temperatureService;
    }

    public void setTemperatureService(TemperatureService temperatureService)
    {
        this.temperatureService = temperatureService;
    }

    public AnalysisUricAcidService getAnalysisUricAcidService()
    {
        return analysisUricAcidService;
    }

    public void setAnalysisUricAcidService(AnalysisUricAcidService analysisUricAcidService)
    {
        this.analysisUricAcidService = analysisUricAcidService;
    }

    /**
     * 上传用户档案
     * 
     * MachineNo   string  是   设备编号
     * IdCard  string  是   身份证号
     * Name    string      姓名
     * Sex string      性别
     * Birthday    dateTime        出生日期
     * Nation  string      民族
     * Tel string      电话
     * Job string      职业
     * Domicile    string      户籍地
     * Address string      住址
     * ArchiveDate DateTime    是   建档日期
     * Sign    string      签名
     * 
     * 如：
     * {
     * "MachineNo":"ZE85DF13L91BXRZ0",
     * "IdCard":"130102198001011234",
     * "Name":"测试",
     * "Birthday":"2015-1-1",
     * "Sex":"1",
     * "BloodType":"1",
     * "BloodRhType":"1",
     * "Nation":"1",
     * "Tel":"13212345678",
     * "Domicile":"",
     * "Address":"",
     * "Job":"",
     * "ArchiveDate":"2015-1-1",
     * "Sign": 15d9ab163bea3d935be0bcf84e1f65y5
     * }
     */
    public void arch()
    {
        String jsonStr = null;
        String sign = null;
        try
        {
            jsonStr = getRequestInfo();
            JsonObject obj = (JsonObject) GsonUtil.parse(jsonStr);
            String idCard = GsonUtil.toString(obj.get("IdCard"));
            sign = GsonUtil.toString(obj.get("Sign"));
            User existUser = userService.getUserByIdCard(idCard);
            if (existUser != null)
            {
                writeRst(STATE_CUST_REPEAT, "此身份证号用户已经存在。", sign);
                return;
            }
            String name = GsonUtil.toString(obj.get("Name"));
            String sex = GsonUtil.toString(obj.get("Sex"));
            Date birthday = DateUtil.getDateFromHtml(GsonUtil.toString(obj.get("Birthday")));
            String nation = GsonUtil.toString(obj.get("Nation"));
            if (nation != null && nation.length() == 1)
            {
                nation = "0" + nation;
            }
            String tel = GsonUtil.toString(obj.get("Tel"));
            String job = GsonUtil.toString(obj.get("Job"));
            // 户籍地
            // String domicile = GsonUtil.toString(obj.get("Domicile"));
            String address = GsonUtil.toString(obj.get("Address"));
            // 建档日期
            // String createDate = GsonUtil.toString(obj.get("ArchiveDate"));
            Customer cust = new Customer();
            User user = new User();
            user.setState(Constant.USER_STATE_EFFECTIVE);
            user.setType(Constant.USER_TYPE_CUSTOMER);
            user.setAccount(idCard);
            user.setPhone(tel);
            user.setIdCard(idCard);
            user.setName(name);
            user.setOrgId(ConstOrg.SUREZEN_ID);
            cust.setUser(user);
            if (StringUtil.isNotEmpty(sex))
            {
                cust.setSex(Integer.valueOf(sex));
            }
            cust.setBirthday(birthday);
            cust.setNational(nation);
            cust.setProfession(job);
            cust.setAddress(address);
            Result r = customerService.add(cust);
            if (Result.STATE_SUCESS == r.getState())
            {
                writeRst(STATE_SUCESS, "新增用户成功。", sign);
            }
            else
            {
                writeRst(STATE_FAIL, StringUtil.isNotEmpty(r.getMsg()) ? r.getMsg() : "新增用户失败。", sign);
            }
        }
        catch (Exception e)
        {
            writeRst(STATE_FAIL, "一 体 机档案上传错误。", sign);
            logger.error("河北循证一 体 机档案上传错误，档案信息：" + jsonStr, e);
        }
    }

    /**
     * 修改身高体重
     * 
     * MachineNo   string  是   设备编号
     * IdCard  string  是   身份证号
     * Height  double      身高
     * Weight  double      体重
     * ExamTime    DateTime    是   检查日期
     * Sign    string      签名
     * 
     * 如：
     * {
     * "IdCard":"130102198001011234",
     * "MachineNo":"ZE85DF13L91BXRZ0",
     * "Height":170,
     * "Weight":60,
     * "ExamTime":"2015-01-01 08:00:00",
     * "Sign": 15d9ab163bea3d935be0bcf84e1f65y5
     * }
     */
    public void hw()
    {
        String jsonStr = null;
        String sign = null;
        try
        {
            jsonStr = getRequestInfo();
            JsonObject obj = (JsonObject) GsonUtil.parse(jsonStr);
            String idCard = GsonUtil.toString(obj.get("IdCard"));
            sign = GsonUtil.toString(obj.get("Sign"));
            User existUser = userService.getUserByIdCard(idCard);
            if (existUser == null)
            {
                writeRst(STATE_FAIL, "用户不存在。", sign);
                return;
            }
            Customer cust = new Customer();
            cust.setUserId(existUser.getId());
            String heightStr = GsonUtil.toString(obj.get("Height"));
            if (StringUtil.isNotEmpty(heightStr))
            {
                cust.setHeight(Double.valueOf(heightStr) / 100d);
            }
            String weightStr = GsonUtil.toString(obj.get("Weight"));
            if (StringUtil.isNotEmpty(weightStr))
            {
                cust.setHeight(Double.valueOf(weightStr));
            }
            customerService.updateByHW(cust);
            writeRst(STATE_SUCESS, "身高体重保存成功。", sign);
        }
        catch (Exception e)
        {
            writeRst(STATE_FAIL, "身高体重上传错误。", sign);
            logger.error("身高体重上传错误，档案信息：" + jsonStr, e);
        }
    }

    private void writeRst(int state, String msg, String sign)
    {
        try
        {
            JsonObject obj = new JsonObject();
            obj.addProperty("RespCode", state);
            obj.addProperty("RespMsg", msg);
            obj.addProperty("Sign", sign);
            response.setHeader("Cache-Control", "no-cache");
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print(obj);
            out.flush();
            out.close();
        }
        catch (IOException e)
        {
            logger.error("输出河北循证一 体 机数据出错。", e);
        }
    }

    /**
     * 上传血压
     * @author h
     * 2015.10.26
     * {
     *  "IdCard":"130102198001011234",
     *  "MachineNo":"ZE85DF13L91BXRZ0",
     *  "Systolic":120,
     *  "Diastolic":75,
     *  "ExamTime":"2015-01-01 08:00:00",
     *  "Sign": 15d9ab163bea3d935be0bcf84e1f65y5
     * }
     */
    public void pressure()
    {
        String jsonStr = null;
        String sign = null;
        try
        {
            jsonStr = getRequestInfo();
            if (jsonStr == null)
            {
                writeRst(STATE_FAIL, "数据格式不正确。", sign);
                return;
            }
            JsonObject obj = (JsonObject) GsonUtil.parse(jsonStr);
            sign = GsonUtil.toString(obj.get("Sign"));
            String idCard = GsonUtil.toString(obj.get("IdCard"));
            User user = userService.getUserByIdCard(idCard);
            if (user == null)
            {
                writeRst(STATE_FAIL, "此身份证号用户不存在。", sign);
                return;
            }
            String deviceMac = GsonUtil.toString(obj.get("MachineNo"));
            Integer dbp = (Integer) GsonUtil.toInteger(obj.get("Diastolic"));
            Integer sbp = GsonUtil.toInteger(obj.get("Systolic"));
            Timestamp checkTime = getCheckTime(GsonUtil.toString(obj.get("ExamTime")));
            PressurePulse bean = new PressurePulse();
            bean.setCustId(user.getId());
            bean.setDBP(dbp);
            bean.setSBP(sbp);
            bean.setDeviceMac(deviceMac);
            bean.setCheckTime(checkTime);
            bean.setCheckType(Constant.DATA_CHECK_TYPE_PERSONAL_DIVICE);
            pressurePulseService.save(bean);
            writeRst(STATE_SUCESS, "上传成功。", sign);
        }
        catch (Exception e)
        {
            writeRst(STATE_FAIL, "血压数据上传错误。", sign);
            logger.error("血压数据上传错误，数据信息：" + jsonStr, e);
        }
    }

    /**
     * 上传血氧
     * {
     *  "IdCard":"130102198001011234",
     *  "MachineNo":"ZE85DF13L91BXRZ0",
     *  "Oxygen":98,
     *  "PulseRate":70, 
     *  "ExamTime":"2015-01-01 08:00:00",
     *  "Sign": 15d9ab163bea3d935be0bcf84e1f65y5
     * }
     * @author h
     * 2015.10.26
     */
    public void oxygen()
    {
        String jsonStr = null;
        String sign = null;
        try
        {
            jsonStr = getRequestInfo();
            if (jsonStr == null)
            {
                writeRst(STATE_FAIL, "数据格式不正确。", sign);
                return;
            }
            JsonObject obj = (JsonObject) GsonUtil.parse(jsonStr);
            sign = GsonUtil.toString(obj.get("Sign"));
            String idCard = GsonUtil.toString(obj.get("IdCard"));
            User user = userService.getUserByIdCard(idCard);
            if (user == null)
            {
                writeRst(STATE_FAIL, "此身份证号用户不存在。", sign);
                return;
            }
            String deviceMac = GsonUtil.toString(obj.get("MachineNo"));
            Integer bo = (Integer) GsonUtil.toInteger(obj.get("Oxygen"));
            Integer pulse = GsonUtil.toInteger(obj.get("PulseRate"));
            Timestamp checkTime = getCheckTime(GsonUtil.toString(obj.get("ExamTime")));
            BloodOxygen bean = new BloodOxygen();
            bean.setCustId(user.getId());
            bean.setBO(bo);
            bean.setPulse(pulse);
            bean.setDeviceMac(deviceMac);
            bean.setCheckTime(checkTime);
            bean.setCheckType(Constant.DATA_CHECK_TYPE_PERSONAL_DIVICE);
            bloodOxygenService.save(bean);
            writeRst(STATE_SUCESS, "上传成功。", sign);
        }
        catch (Exception e)
        {
            writeRst(STATE_FAIL, "血氧数据上传错误。", sign);
            logger.error("血氧数据上传错误，数据信息：" + jsonStr, e);
        }
    }

    /**
     * 上传血糖
     * {
     *  "IdCard":"130102198001011234",
     *  "MachineNo":"ZE85DF13L91BXRZ0",
     *  "Glucose":6
     *  "ExamTime":"2015-01-01 08:00:00",
     *  "Sign": 15d9ab163bea3d935be0bcf84e1f65y5
     * }
     * @author h
     * 2015.10.26
     */
    public void sugar()
    {
        String jsonStr = null;
        String sign = null;
        try
        {
            jsonStr = getRequestInfo();
            if (jsonStr == null)
            {
                writeRst(STATE_FAIL, "数据格式不正确。", sign);
                return;
            }
            JsonObject obj = (JsonObject) GsonUtil.parse(jsonStr);
            sign = GsonUtil.toString(obj.get("Sign"));
            String idCard = GsonUtil.toString(obj.get("IdCard"));
            User user = userService.getUserByIdCard(idCard);
            if (user == null)
            {
                writeRst(STATE_FAIL, "此身份证号用户不存在。", sign);
                return;
            }
            String deviceMac = GsonUtil.toString(obj.get("MachineNo"));
            Double bloodSugar = GsonUtil.toDouble(obj.get("Glucose"));
            Timestamp checkTime = getCheckTime(GsonUtil.toString(obj.get("ExamTime")));
            BloodSugar bean = new BloodSugar();
            bean.setCustId(user.getId());
            Integer bloodSugarType = 1;
            bean.setBloodSugarType(bloodSugarType);
            bean.setBloodSugar(bloodSugar);
            bean.setDeviceMac(deviceMac);
            bean.setCheckTime(checkTime);
            bean.setCheckType(Constant.DATA_CHECK_TYPE_PERSONAL_DIVICE);
            bloodSugarService.save(bean);
            writeRst(STATE_SUCESS, "上传成功。", sign);
        }
        catch (Exception e)
        {
            writeRst(STATE_FAIL, "血糖数据上传错误。", sign);
            logger.error("血糖数据上传错误，数据信息：" + jsonStr, e);
        }
    }

    /**
     * 上传体温
     * {
     *  "IdCard":"130102198001011234",
     *  "MachineNo":"ZE85DF13L91BXRZ0",
     *  "Temperature":36.6,
     *  "ExamTime":"2015-01-01 08:00:00",
     *  "Sign": 15d9ab163bea3d935be0bcf84e1f65y5
     * }
     * @author h
     * 2015.10.26
     */
    public void tempe()
    {
        String jsonStr = null;
        String sign = null;
        try
        {
            jsonStr = getRequestInfo();
            if (jsonStr == null)
            {
                writeRst(STATE_FAIL, "数据格式不正确。", sign);
                return;
            }
            JsonObject obj = (JsonObject) GsonUtil.parse(jsonStr);
            sign = GsonUtil.toString(obj.get("Sign"));
            String idCard = GsonUtil.toString(obj.get("IdCard"));
            User user = userService.getUserByIdCard(idCard);
            if (user == null)
            {
                writeRst(STATE_FAIL, "此身份证号用户不存在。", sign);
                return;
            }
            else
            {
                String deviceMac = GsonUtil.toString(obj.get("MachineNo"));
                Double temperature = GsonUtil.toDouble(obj.get("Temperature"));
                Timestamp checkTime = getCheckTime(GsonUtil.toString(obj.get("ExamTime")));
                Temperature bean = new Temperature();
                bean.setCustId(user.getId());
                bean.setTemperature(temperature);
                bean.setCheckType(Constant.DATA_CHECK_TYPE_PERSONAL_DIVICE);
                bean.setDeviceMac(deviceMac);
                bean.setCheckTime(checkTime);
                temperatureService.save(bean);
                writeRst(STATE_SUCESS, "上传成功。", sign);
            }
        }
        catch (Exception e)
        {
            writeRst(STATE_FAIL, "体温数据上传错误。", sign);
            logger.error("体温数据上传错误，数据信息：" + jsonStr, e);
        }
    }

    /**
     * 上传尿常规
     * {
     *  "IdCard":"130102198001011234",
     *  "MachineNo":"ZE85DF13L91BXRZ0",
     *  "URO":”+”,
     *  "BLD":”-”,
     *  "BIL":”-”,
     *  "KET":”-”,
     *  "GLU":”-”,
     *  "PRO":”-”,
     *  "PH":6,
     *  "NIT":”-”,
     *  "WBC":”-”,
     *  "SG":” 1.010”,
     *  "VC":”-”,
     *  "ExamTime":"2015-01-01 08:00:00",
     *  "Sign": 15d9ab163bea3d935be0bcf84e1f65y5
     * }
     * @author h
     * 2015.10.26
     */
    public void analyacid()
    {
        String jsonStr = null;
        String sign = null;
        try
        {
            jsonStr = getRequestInfo();
            if (jsonStr == null)
            {
                writeRst(STATE_FAIL, "数据格式不正确。", sign);
                return;
            }
            JsonObject obj = (JsonObject) GsonUtil.parse(jsonStr);
            sign = GsonUtil.toString(obj.get("Sign"));
            String idCard = GsonUtil.toString(obj.get("IdCard"));
            User user = userService.getUserByIdCard(idCard);
            if (user == null)
            {
                writeRst(STATE_FAIL, "此身份证号用户不存在。", sign);
                return;
            }
            String deviceMac = GsonUtil.toString(obj.get("MachineNo"));
            String ubg = GsonUtil.toString(obj.get("URO"));
            String bld = GsonUtil.toString(obj.get("BLD"));
            String bil = GsonUtil.toString(obj.get("BIL"));
            String ket = GsonUtil.toString(obj.get("KET"));
            String glu = GsonUtil.toString(obj.get("GLU"));
            String pro = GsonUtil.toString(obj.get("PRO"));
            String phstr = GsonUtil.toString(obj.get("PH"));
            Double ph = Double.parseDouble(phstr);
            String nit = GsonUtil.toString(obj.get("NIT"));
            String leu = GsonUtil.toString(obj.get("WBC"));
            String sgStr = GsonUtil.toString(obj.get("SG"));
            Double sg = Double.parseDouble(sgStr);
            String vc = GsonUtil.toString(obj.get("VC"));
            Timestamp checkTime = getCheckTime(GsonUtil.toString(obj.get("ExamTime")));
            AnalysisUricAcid bean = new AnalysisUricAcid();
            bean.setCustId(user.getId());
            bean.setUbg(ubg);
            bean.setBld(bld);
            bean.setBil(bil);
            bean.setKet(ket);
            bean.setGlu(glu);
            bean.setPro(pro);
            bean.setPh(ph);
            bean.setNit(nit);
            bean.setLeu(leu);
            bean.setSg(sg);
            bean.setVc(vc);
            bean.setCheckType(Constant.DATA_CHECK_TYPE_PERSONAL_DIVICE);
            bean.setDeviceMac(deviceMac);
            bean.setCheckTime(checkTime);
            analysisUricAcidService.save(bean);
            writeRst(STATE_SUCESS, "上传成功。", sign);
        }
        catch (Exception e)
        {
            writeRst(STATE_FAIL, "尿常规数据上传错误。", sign);
            logger.error("尿常规数据上传错误，数据信息：" + jsonStr, e);
        }
    }

    public static String handleAnalyacidVal(String val)
    {
        if (val == null)
        {
            return null;
        }
        return val.replaceFirst("\\s+.*", "");
    }

    private String getRequestInfo() throws Exception
    {
        ServletInputStream in = null;
        ByteArrayOutputStream outStream = null;
        try
        {
            in = request.getInputStream();
            outStream = new ByteArrayOutputStream();
            byte[] data = new byte[1024];
            int count = -1;
            while ((count = in.read(data, 0, 1024)) != -1)
            {
                outStream.write(data, 0, count);
            }
            return new String(outStream.toByteArray(), "UTF-8");
        }
        catch (Exception e)
        {
            logger.error("获取请求数据错误。", e);
            throw e;
        } finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (Exception e)
                {
                }
            }
            if (outStream != null)
            {
                try
                {
                    outStream.close();
                }
                catch (Exception e)
                {
                }
            }
        }
    }

    private Timestamp getCheckTime(String str) throws ParseException
    {
        if (str == null || str.length() == 0)
        {
            return null;
        }
        return new Timestamp(ctDf.parse(str).getTime());
    }

}
