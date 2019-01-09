package com.yzxt.yh.module.chk.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonObject;
import com.yzxt.fw.pager.PageModel;
import com.yzxt.fw.pager.PageTran;
import com.yzxt.fw.util.PushMsgUtil;
import com.yzxt.yh.constant.ConstCheckData;
import com.yzxt.yh.constant.ConstDevice;
import com.yzxt.yh.constant.ConstPushMsg;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.constant.Constant;
import com.yzxt.yh.module.chk.bean.BloodSugar;
import com.yzxt.yh.module.chk.bean.CheckWarn;
import com.yzxt.yh.module.chk.dao.BloodSugarDao;
import com.yzxt.yh.module.chk.dao.PhysiologIndexDao;
import com.yzxt.yh.module.chk.util.AnalysisCheckData;
import com.yzxt.yh.module.chk.util.AnalysisResult;
import com.yzxt.yh.module.svb.service.MemberInfoService;
import com.yzxt.yh.module.sys.service.UserService;
import com.yzxt.yh.util.DateUtil;
import com.yzxt.yh.util.DecimalUtil;

@Transactional(ConstTM.DEFAULT)
public class BloodSugarService
{
    private BloodSugarDao bloodSugarDao;
    private PhysiologIndexDao physiologIndexDao;
    private CheckWarnService checkWarnService;
    private UserService userService;
    private MemberInfoService memberInfoService;

    public BloodSugarDao getBloodSugarDao()
    {
        return bloodSugarDao;
    }

    public void setBloodSugarDao(BloodSugarDao bloodSugarDao)
    {
        this.bloodSugarDao = bloodSugarDao;
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
     * 上传血糖数据
     * @param bean
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(BloodSugar bean) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        // 根据检测值的有效性调整检测时间
        if (bean.getCheckTime() == null || bean.getCheckTime().getTime() > now.getTime())
        {
            bean.setCheckTime(now);
        }
        // 分析数据
        AnalysisResult anaRst = AnalysisCheckData.bloodSugar(bean.getBloodSugarType(), bean.getBloodSugar());
        bean.setLevel(anaRst.getLevel());
        bean.setDescript(anaRst.getDescript());
        // 保存数据
        bean.setCreateTime(now);
        bloodSugarDao.insert(bean);
        // 更新客户身体指标
        physiologIndexDao.update(bean);
        // 保存告警信息
        saveWarn(bean);
    }

    /**
     * 保存血糖告警信息
     * 
     * 正常血糖    空腹血糖＜6.1mmol/L
     * 空腹血糖受损    6.1mmol/L≤空腹血糖＜7.0mmol/L 和 糖负荷后2h血糖＜7.8mmol/L
     * 糖耐量异常      空腹血糖＜7.0mmol/L 和 7.8mmol/L≤糖负荷后2h血糖＜11.1mmol/L
     * 糖尿病          空腹血糖≥7.0mmol/L 或 糖负荷后2h血糖≥11.1mmol/L
     * 平台一级预警
     * 初次诊断为糖尿病、糖尿病前期
     * 平台二级预警
     * 3.9mmol/L≤任意血糖＜4.4mmol/L   或  13.9mmol/L≤任意血糖＜16.7mmol/L
     * 平台三级预警
     * 任意血糖＜3.9mmol/L  或  任意血糖≥16.7mmol/L
     * 
     * 餐前  6.1mmol/L≤7.0mmol/L 您本次的血糖为XX，血糖偏高，考虑为糖尿病前期，请择日再次测量！详情请咨询4006-139319。
     *   ≥7.0mmol/L  您本次的血糖为XX，血糖偏高，考虑为糖尿病的可能，请择日再次测量！详情请咨询4006-139319。
     * 餐后血糖值或服糖2小时血糖值  >= 11.1 mmol/L  您本次的血糖为XX，血糖偏高，考虑为糖尿病的可能，请择日再次测量！详情请咨询4006-139319。
     *   >= 7.8 mmol/L   您本次的血糖为XX，血糖偏高，考虑为糖尿病前期，请择日再次测量！详情请咨询4006-139319。
     * 任意血糖    任意血糖＜3.9mmol/L  您本次血糖测量值为**，血糖值过低，请马上补充能量，并持续测量关注血糖变化。
     *   3.9mmol/L≤任意血糖＜4.4mmol/L    您本次血糖测量值为**，血糖值较低，请及时补充能量，关注血糖变化。
     *   13.9mmol/L≤任意血糖＜16.7mmol/L  您本次血糖测量值较高，为避免糖尿病急性并发症的发生，请尽快到医院就诊。详情请咨询400-86-45600
     * 任意血糖≥16.7mmol/L 您本次血糖测量值过高，为避免糖尿病急性并发症的发生，请尽快到医院就诊。详情请咨询400-86-45600
     * 
     * @param bean
     * @return
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveWarn(BloodSugar bean) throws Exception
    {
        CheckWarn warn = new CheckWarn();
        double bloodSugar = bean.getBloodSugar();
        int bloodSugarType = bean.getBloodSugarType() != null ? bean.getBloodSugarType().intValue() : 0;
        // 告警描述
        StringBuilder valDesc = new StringBuilder();
        if (bloodSugarType == 1)
        {
            valDesc.append("空腹");
        }
        else if (bloodSugarType == 2)
        {
            valDesc.append("餐前");
        }
        else if (bloodSugarType == 3)
        {
            valDesc.append("餐后");
        }
        else if (bloodSugarType == 4)
        {
            valDesc.append("服糖2小时");
        }
        valDesc.append("血糖为").append(DecimalUtil.toString(bloodSugar)).append("mmol/L，");
        StringBuilder warnDesc = new StringBuilder();
        // 风险评估等级
        int level = 0;
        if (bloodSugar >= 4.4 && bloodSugar < 13.9)
        {
            //空腹或餐前血糖
            if (bloodSugarType == 1 || bloodSugarType == 2)
            {
                if (bloodSugar >= 7.0)
                {
                    level = 1;
                    warnDesc.append(valDesc).append("血糖偏高，考虑为糖尿病的可能，请择日再次测量！");
                    // warnDesc.append(valDesc).append("血糖偏高，考虑为糖尿病的可能，请择日再次测量！");
                }
                else if (bloodSugar >= 6.1)
                {
                    level = 1;
                    warnDesc.append(valDesc).append("血糖偏高，考虑为糖尿病前期，请择日再次测量！");
                    // warnDesc.append(valDesc).append("血糖偏高，考虑为糖尿病前期，请择日再次测量！");
                }
            }
            else if (bloodSugarType == 3 || bloodSugarType == 4)
            {
                if (bloodSugar >= 11.1)
                {
                    level = 1;
                    warnDesc.append(valDesc).append("血糖偏高，考虑为糖尿病的可能，请择日再次测量！");
                    // warnDesc.append(valDesc).append("血糖偏高，考虑为糖尿病的可能，请择日再次测量！");
                }
                else if (bloodSugarType >= 7.8)
                {
                    level = 1;
                    warnDesc.append(valDesc).append("血糖偏高，考虑为糖尿病前期，请择日再次测量！");
                    // warnDesc.append(valDesc).append("血糖偏高，考虑为糖尿病前期，请择日再次测量！");
                }
            }
        }
        else if (bloodSugar >= 16.7)
        {
            level = 3;
            warnDesc.append(valDesc).append("血糖值过高，为避免糖尿病急性并发症的发生，请尽快到医院就诊。");
            // warnDesc.append(valDesc).append("血糖值过高，为避免糖尿病急性并发症的发生，请尽快到医院就诊。");
        }
        else if (bloodSugar >= 13.9)
        {
            level = 2;
            warnDesc.append(valDesc).append("血糖值较高，为避免糖尿病急性并发症的发生，请尽快到医院就诊。");
            // warnDesc.append(valDesc).append("血糖值较高，为避免糖尿病急性并发症的发生，请尽快到医院就诊。");
        }
        else if (bloodSugar < 3.9)
        {
            level = 3;
            warnDesc.append(valDesc).append("血糖值过低，请马上补充能量，并持续测量关注血糖变化。");
            // warnDesc.append(valDesc).append("血糖值过低，请马上补充能量，并持续测量关注血糖变化。");
        }
        else if (bloodSugar < 4.4)
        {
            level = 2;
            warnDesc.append(valDesc).append("血糖值较低，请及时补充能量，关注血糖变化。");
            // warnDesc.append(valDesc).append("血糖值较低，请及时补充能量，关注血糖变化。");
        }
        if (level > 0)
        {
            // 当天已有告警
            Map<String, Object> warnFilter = new HashMap<String, Object>();
            warnFilter.put("type", ConstCheckData.BLOOD_SUGAR);
            warnFilter.put("custId", bean.getCustId());
            warnFilter.put("startDate", DateUtil.truncDay(bean.getCreateTime()));
            CheckWarn lastWarn = checkWarnService.getLatest(warnFilter);
            // 1、2级告警每天告警级别增加时，会产生新的告警
            if (level == 3 || lastWarn == null || lastWarn.getLevel() < level)
            {
                Object[] relatedPersonnels = checkWarnService.getWarnRelatedPersonnels(bean.getCustId());
                String[] cust = (String[]) relatedPersonnels[0];
                String custName = cust[1] != null ? cust[1] : "";

                warn.setId(bean.getId());
                warn.setCustId(bean.getCustId());
                warn.setType(ConstCheckData.BLOOD_SUGAR);
                warn.setLevel(level);
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
                extMsgs.put(ConstPushMsg.MSG_TITLE_ATTR_NAME, "健康平台血糖预警通知");
                // 给客户自己推送信息，对于一个用户，同一种设备只有一个通道ID
                if (pushSelfList != null && pushSelfList.size() > 0)
                {
                    for (Object[] objs : pushSelfList)
                    {
                        pushAttr = ConstPushMsg.ATTR_MSG_TYPE_NOTICE | PushMsgUtil.getDeviceTypeAttr((Integer) objs[0]);
                        JsonObject msg = new JsonObject();
                        msg.addProperty(ConstPushMsg.MSG_TOPIC_ATTR_NAME, ConstPushMsg.MSG_TOPIC_XTYJ);
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
                        msg.addProperty(ConstPushMsg.MSG_TOPIC_ATTR_NAME, ConstPushMsg.MSG_TOPIC_XTYJ);
                        msg.addProperty(ConstPushMsg.MSG_CONTENT_ATTR_NAME, fmWarnInfo);
                        extMsgs.put(ConstPushMsg.MSG_DESCRIPTION_ATTR_NAME, fmWarnInfo);
                        PushMsgUtil.pushBatch(aChannelIds, pushAttr, msg, extMsgs);
                    }
                }
            }
        }
    }

    /**
     * 查询血糖列表
     * @param filter 过滤条件
     * @param sysTime 同步时间点
     * @param dir 同步方式，大于0：取时间点后的数据，小于0取时间点前的数据
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageTran<BloodSugar> queryTran(Map<String, Object> filter, Timestamp sysTime, int dir, int count)
    {
        return bloodSugarDao.queryTran(filter, sysTime, dir, count);
    }

    /**
     * 查询血糖列表
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<BloodSugar> query(Map<String, Object> filter, int page, int pageSize)
    {
        return bloodSugarDao.query(filter, page, pageSize);
    }

}
