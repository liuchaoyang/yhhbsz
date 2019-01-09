package com.yzxt.yh.module.chk.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonObject;
import com.yzxt.fw.common.Result;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.fw.pager.PageTran;
import com.yzxt.fw.util.PushMsgUtil;
import com.yzxt.yh.constant.ConstCheckData;
import com.yzxt.yh.constant.ConstDevice;
import com.yzxt.yh.constant.ConstPushMsg;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.chk.bean.CheckWarn;
import com.yzxt.yh.module.chk.bean.PressurePulse;
import com.yzxt.yh.module.chk.bean.Pulse;
import com.yzxt.yh.module.chk.dao.PhysiologIndexDao;
import com.yzxt.yh.module.chk.dao.PressurePulseDao;
import com.yzxt.yh.module.chk.util.AnalysisCheckData;
import com.yzxt.yh.module.chk.util.AnalysisResult;
import com.yzxt.yh.module.svb.service.MemberInfoService;
import com.yzxt.yh.module.sys.bean.CustFamily;
import com.yzxt.yh.module.sys.service.UserService;
import com.yzxt.yh.util.DateUtil;

/**
 * @author hg
 *
 */
@Transactional(ConstTM.DEFAULT)
public class PressurePulseService
{
    private PressurePulseDao pressurePulseDao;
    private PhysiologIndexDao physiologIndexDao;
    private CheckWarnService checkWarnService;
    private PulseService pulseService;
    private UserService userService;
    private MemberInfoService memberInfoService;

    public PressurePulseDao getPressurePulseDao()
    {
        return pressurePulseDao;
    }

    public void setPressurePulseDao(PressurePulseDao pressurePulseDao)
    {
        this.pressurePulseDao = pressurePulseDao;
    }

    public PhysiologIndexDao getPhysiologIndexDao()
    {
        return physiologIndexDao;
    }

    public void setPhysiologIndexDao(PhysiologIndexDao physiologIndexDao)
    {
        this.physiologIndexDao = physiologIndexDao;
    }

    public CheckWarnService getCheckWarnService()
    {
        return checkWarnService;
    }

    public void setCheckWarnService(CheckWarnService checkWarnService)
    {
        this.checkWarnService = checkWarnService;
    }

    public PulseService getPulseService()
    {
        return pulseService;
    }

    public void setPulseService(PulseService pulseService)
    {
        this.pulseService = pulseService;
    }

    public UserService getUserService()
    {
        return userService;
    }

    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }

    public MemberInfoService getMemberInfoService()
    {
        return memberInfoService;
    }

    public void setMemberInfoService(MemberInfoService memberInfoService)
    {
        this.memberInfoService = memberInfoService;
    }

    /**
     * 上传血压脉搏数据
     * @param bean
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(PressurePulse bean) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        // 根据检测值的有效性调整检测时间
        if (bean.getCheckTime() == null || bean.getCheckTime().getTime() > now.getTime())
        {
            bean.setCheckTime(now);
        }
        // 分析数据
        AnalysisResult anaRst = AnalysisCheckData.bloodPressure(bean.getDBP().doubleValue(), bean.getSBP()
                .doubleValue());
        bean.setLevel(anaRst.getLevel());
        bean.setDescript(anaRst.getDescript());
        // 保存数据
        bean.setCreateTime(now);
        bean.setType(1);
        pressurePulseDao.insert(bean);
        // 更新客户身体指标
        physiologIndexDao.update(bean);
        // 保存告警信息，血压计是全部数据同步过来的，这样有问题，暂只处理半小时内的告警
        saveWarn(bean);
        // 查看是否包含心率信息
        if (bean.getPulse() != null && bean.getPulse().intValue() > 0)
        {
            Pulse pulseBean = new Pulse();
            pulseBean.setAdditional(true);
            pulseBean.setCustId(bean.getCustId());
            pulseBean.setDeviceName(bean.getDeviceName());
            pulseBean.setDeviceMac(bean.getDeviceMac());
            pulseBean.setLatitude(bean.getLatitude());
            pulseBean.setLongitude(bean.getLongitude());
            pulseBean.setUnit(null);
            pulseBean.setPulse(bean.getPulse());
            pulseBean.setCheckTime(bean.getCheckTime());
            pulseBean.setCheckType(bean.getCheckType());
            pulseBean.setPulseType(Pulse.PULSE_TYPE_B);
            pulseService.save(pulseBean);
        }
    }

    /**
     * 保存血压告警信息
     * 
     * 高血压：
     * 正常血压            90mmHg≤收缩压＜120mmHg 和 60mmHg≤舒张压＜80mmHg
     * 正常高值血压        120mmHg≤收缩压≤139mmHg 和（或）80mmHg≤舒张压≤89mmHg
     * 平台一级预警
     * 1级高血压（轻度）   140mmHg≤收缩压≤159mmHg 和（或） 90mmHg≤舒张压≤99mmHg 
     * 平台二级预警    
     * 2级高血压（中度）   160mmHg≤收缩压≤179mmHg 和（或）100mmHg≤舒张压≤109mmHg
     * 平台三级预警    
     * 3级高血压（重度）   收缩压≥180mmHg 和（或）舒张压≥110
     * 
     * 不预警 90mmHg≤收缩压＜120mmHg 和 60mmHg≤舒张压＜80mmHg  您本次血压测量值正常，请保持健康的生活方式，合理饮食、适当运动。
     * 不预警 120mmHg≤收缩压≤139mmHg 和（或）80mmHg≤舒张压≤89mmHg   您的血压处于正常高值血压，请保持健康的生活方式，合理饮食适当运动，定期测量血压。
     * 1级预警    140mmHg≤收缩压≤159mmHg 和（或） 90mmHg≤舒张压≤99mmHg  您处于轻度高血压状态，请保持健康的生活方式以及愉悦的心情，控制食盐摄入量、维持正常体重、禁烟限酒，并每周进行血压检测。
     * 2级预警    160mmHg≤收缩压≤179mmHg 和（或）100mmHg≤舒张压≤109mmHg 您处于中度高血压状态，请先就医确诊。请保持健康的生活方式以及愉悦的心情，控制食盐摄入量、维持正常体重、禁烟限酒，并每3天进行血压检测，关注血压变化。
     * 3级预警    收缩压≥180mmHg 和（或）舒张压≥110 您处于重度高血压状态，请及时就医或服药。请保持健康的生活方式以及愉悦的心情，控制食盐摄入量、维持正常体重、禁烟限酒，并每天进行血压检测，关注血压变化。
     * 
     * @param bean
     * @return
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveWarn(PressurePulse bean) throws Exception
    {
        CheckWarn warn = new CheckWarn();
        // 收缩压等级
        int sbp = bean.getSBP();
        int sbpLevel = 0;
        if (sbp < 140)
        {
            sbpLevel = -1;
        }
        else if (sbp <= 159)
        {
            sbpLevel = 1;
        }
        else if (sbp <= 179)
        {
            sbpLevel = 2;
        }
        else
        {
            sbpLevel = 3;
        }
        // 舒张压等级
        int dbp = bean.getDBP();
        int dbpLevel = 0;
        if (dbp < 90)
        {
            dbpLevel = -1;
        }
        else if (dbp <= 99)
        {
            dbpLevel = 1;
        }
        else if (dbp <= 109)
        {
            dbpLevel = 2;
        }
        else
        {
            dbpLevel = 3;
        }
        // 取较高的分级标准为准
        int level = Math.max(sbpLevel, dbpLevel);
        if (level > 0)
        {
            // 当天已有告警
            Map<String, Object> warnFilter = new HashMap<String, Object>();
            warnFilter.put("type", ConstCheckData.BLOOD_PRESSURE);
            warnFilter.put("custId", bean.getCustId());
            warnFilter.put("startDate", DateUtil.truncDay(bean.getCreateTime()));
            CheckWarn lastWarn = checkWarnService.getLatest(warnFilter);
            // 1、2级告警每天告警级别增加时，会产生新的告警
            if (level == 3 || lastWarn == null || lastWarn.getLevel() < level)
            {
                Object[] relatedPersonnels = checkWarnService.getWarnRelatedPersonnels(bean.getCustId());
                String[] cust = (String[]) relatedPersonnels[0];
                String custName = cust[1] != null ? cust[1] : "";
                // 告警描述
                StringBuilder warnDesc = new StringBuilder();
                StringBuilder valDesc = new StringBuilder();
                valDesc.append("血压测量值收缩压为").append(sbp).append("mmHg，舒张压为").append(dbp).append("mmHg，");
                if (level == 1)
                {
                    warnDesc.append(valDesc).append("处于轻度高血压状态，请保持健康的生活方式以及愉悦的心情，控制食盐摄入量、维持正常体重、禁烟限酒，并每周进行血压检测。");
                    // warnDesc.append(valDesc).append("处于轻度高血压状态，请保持健康的生活方式以及愉悦的心情，控制食盐摄入量、维持正常体重、禁烟限酒，并每周进行血压检测。");
                }
                else if (level == 2)
                {
                    warnDesc.append(valDesc).append(
                            "处于中度高血压状态，请先就医确诊。请保持健康的生活方式以及愉悦的心情，控制食盐摄入量、维持正常体重、禁烟限酒，并每3天进行血压检测，关注血压变化。");
                    // warnDesc.append(valDesc).append("处于中度高血压状态，请先就医确诊。请保持健康的生活方式以及愉悦的心情，控制食盐摄入量、维持正常体重、禁烟限酒，并每3天进行血压检测，关注血压变化。");
                }
                else
                {
                    warnDesc.append(valDesc).append(
                            "处于重度高血压状态，请及时就医或服药。请保持健康的生活方式以及愉悦的心情，控制食盐摄入量、维持正常体重、禁烟限酒，并每天进行血压检测，关注血压变化。");
                    // warnDesc.append(valDesc).append("处于重度高血压状态，请及时就医或服药。请保持健康的生活方式以及愉悦的心情，控制食盐摄入量、维持正常体重、禁烟限酒，并每天进行血压检测，关注血压变化。");
                }

                warn.setId(bean.getId());
                warn.setCustId(bean.getCustId());
                warn.setType(ConstCheckData.BLOOD_PRESSURE);
                warn.setLevel(bean.getLevel());
                warn.setDescript(warnDesc.toString());
                warn.setState(Constant.WARNING_STATE_NOT_DEAL);
                warn.setWarnTime(bean.getCreateTime());
                warn.setName(custName);
                checkWarnService.save(warn);
                // 发送提醒信息
                String selfWarnInfo = new StringBuilder("您本次的").append(warnDesc).toString();
                // 告警推送
                List<Object[]> pushSelfList = (List<Object[]>) relatedPersonnels[2];
                int pushAttr = ConstPushMsg.ATTR_MSG_TYPE_NOTICE;
                Map<String, Object> extMsgs = new HashMap<String, Object>();
                extMsgs.put(ConstPushMsg.MSG_TITLE_ATTR_NAME, "健康平台血压预警通知");
                // 给客户自己推送通知信息，对于一个用户，同一种设备只有一个通道ID
                if (pushSelfList != null && pushSelfList.size() > 0)
                {
                    for (Object[] objs : pushSelfList)
                    {
                        pushAttr = ConstPushMsg.ATTR_MSG_TYPE_NOTICE | PushMsgUtil.getDeviceTypeAttr((Integer) objs[0]);
                        JsonObject msg = new JsonObject();
                        msg.addProperty(ConstPushMsg.MSG_TOPIC_ATTR_NAME, ConstPushMsg.MSG_TOPIC_XYYJ);
                        msg.addProperty(ConstPushMsg.MSG_CONTENT_ATTR_NAME, selfWarnInfo);
                        extMsgs.put(ConstPushMsg.MSG_DESCRIPTION_ATTR_NAME, selfWarnInfo);
                        PushMsgUtil.pushSingle((String) objs[1], pushAttr, msg, extMsgs);
                    }
                }
                // 给客户家庭成员推送信息
                List<Object[]> pushFmList = (List<Object[]>) relatedPersonnels[3];
                if (pushFmList != null && pushFmList.size() > 0)
                {
                    List<String> aChannelIds = new ArrayList<String>();
                    for (Object[] objs : pushFmList)
                    {
                        if (((Integer) objs[0]).intValue() == ConstDevice.DEVICE_TYPE_ANDROID)
                        {
                            aChannelIds.add((String) objs[1]);
                        }
                    }
                    if (!aChannelIds.isEmpty())
                    {
                        pushAttr = ConstPushMsg.ATTR_MSG_TYPE_NOTICE
                                | PushMsgUtil.getDeviceTypeAttr(ConstDevice.DEVICE_TYPE_ANDROID);
                        String fmWarnInfo = new StringBuilder("家庭成员").append(custName).append(warnDesc).toString();
                        JsonObject msg = new JsonObject();
                        msg.addProperty(ConstPushMsg.MSG_TOPIC_ATTR_NAME, ConstPushMsg.MSG_TOPIC_XYYJ);
                        msg.addProperty(ConstPushMsg.MSG_CONTENT_ATTR_NAME, fmWarnInfo);
                        extMsgs.put(ConstPushMsg.MSG_DESCRIPTION_ATTR_NAME, fmWarnInfo);
                        PushMsgUtil.pushBatch(aChannelIds, pushAttr, msg, extMsgs);
                    }
                }
            }
        }
    }

    /**
     * 查询所有用户的血压脉率列表
     * @param filter 过滤条件
     * @param sysTime 同步时间点
     * @param dir 同步方式，大于0：取时间点后的数据，小于0取时间点前的数据
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageTran<PressurePulse> queryTran(Map<String, Object> filter,Timestamp sysTime, int dir, int count)
    {
        return pressurePulseDao.queryTran(filter,sysTime, dir, count);
    }
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageTran<PressurePulse> queryMember(Map<String, Object> filter,Timestamp sysTime, int dir, int count)
    {
        return pressurePulseDao.queryMember(filter,sysTime, dir, count);
    }
    /**
    * 查询血压脉率列表
    * @param filter 过滤条件
    * @param sysTime 同步时间点
    * @param dir 同步方式，大于0：取时间点后的数据，小于0取时间点前的数据
    * @return
    */
   @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
   public PageTran<PressurePulse> queryTra(Map<String, Object> filter, Timestamp sysTime, int dir, int count)
   {
       return pressurePulseDao.queryTra(filter, sysTime, dir, count);
   }
    /**
     * 查询血压脉率列表
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<PressurePulse> query(Map<String, Object> filter, int page, int pageSize)
    {
        return pressurePulseDao.query(filter, page, pageSize);
    }
    
    @Transactional(propagation = Propagation.REQUIRED)
    public Result updatePre(PressurePulse pre)
    {
        Integer c = pressurePulseDao.updatePre(pre);
        if (c > 0)
        {
            return new Result(Result.STATE_SUCESS, "上传成功", c);
        }
        else
        {
            return new Result(Result.STATE_FAIL, "上传失败", c);
        }
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public Result updatePres(PressurePulse pre)
    {
        Integer c = pressurePulseDao.updatePres(pre);
        if (c > 0)
        {
            return new Result(Result.STATE_SUCESS, "上传成功", c);
        }
        else
        {
            return new Result(Result.STATE_FAIL, "上传失败", c);
        }
    }
	@Transactional(propagation = Propagation.REQUIRED)
	public List<PressurePulse> queryByCustId(String custId,String id) {
		
		return pressurePulseDao.queryByCustId(custId,id);
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<PressurePulse> queryById(String id) {
		
		return pressurePulseDao.queryById(id);
		
	}

	


}
