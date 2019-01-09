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
import com.yzxt.yh.module.chk.bean.BodyFat;
import com.yzxt.yh.module.chk.bean.CheckWarn;
import com.yzxt.yh.module.chk.dao.BodyFatDao;
import com.yzxt.yh.module.chk.dao.PhysiologIndexDao;
import com.yzxt.yh.module.chk.util.AnalysisCheckData;
import com.yzxt.yh.module.chk.util.AnalysisResult;
import com.yzxt.yh.module.sys.bean.Customer;
import com.yzxt.yh.module.sys.dao.CustomerDao;
import com.yzxt.yh.util.DateUtil;

/**
 * 体脂率service类，保存到体脂检测表中,分析产生预警
 * @author h
 * 2015.8.10
 * */
@Transactional(ConstTM.DEFAULT)
public class BodyFatService
{
    private BodyFatDao bodyFatDao;
    private PhysiologIndexDao physiologIndexDao;
    private CheckWarnService checkWarnService;
    private CustomerDao customerDao;

    public BodyFatDao getBodyFatDao()
    {
        return bodyFatDao;
    }

    public void setBodyFatDao(BodyFatDao bodyFatDao)
    {
        this.bodyFatDao = bodyFatDao;
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

    public CustomerDao getCustomerDao()
    {
        return customerDao;
    }

    public void setCustomerDao(CustomerDao customerDao)
    {
        this.customerDao = customerDao;
    }

    /**
     * 保存体脂检测数据
     * @author h
     * 2015.8.10
     * @throws Exception 
     * */
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(BodyFat bean) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        // 根据检测值的有效性调整检测时间
        if (bean.getCheckTime() == null || bean.getCheckTime().getTime() > now.getTime())
        {
            bean.setCheckTime(now);
        }
        Customer cust = customerDao.get(bean.getCustId());
        // 分析数据 
        AnalysisResult anaRst = AnalysisCheckData.bodyFat(bean.getBfr(), cust.getSex());
        bean.setLevel(anaRst.getLevel());
        bean.setDescript(anaRst.getDescript());
        // 保存数据
        bean.setCreateTime(now);
        bodyFatDao.insert(bean);
        // 更新客户身体指标
        physiologIndexDao.update(bean);
        // 保存告警信息
        saveWarn(bean);
    }

    /**
     * 保存体脂数据预警
     * @author h
     * 2015.8.10
     * @throws Exception 
     * */
    @SuppressWarnings("unchecked")
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveWarn(BodyFat bean) throws Exception
    {
        // 风险评估等级
        int level = bean.getLevel() != null ? bean.getLevel().intValue() : 0;
        // 保存告警信息
        if (level > 0)
        {
            // 当天已有告警
            Map<String, Object> warnFilter = new HashMap<String, Object>();
            warnFilter.put("type", ConstCheckData.BFR);
            warnFilter.put("custId", bean.getCustId());
            warnFilter.put("startDate", DateUtil.truncDay(bean.getCreateTime()));
            CheckWarn lastWarn = checkWarnService.getLatest(warnFilter);
            // 每天告警级别增加时，会产生新的告警
            if (lastWarn == null || lastWarn.getLevel() < level)
            {
                Object[] relatedPersonnels = checkWarnService.getWarnRelatedPersonnels(bean.getCustId());
                String[] cust = (String[]) relatedPersonnels[0];
                String custName = cust[1] != null ? cust[1] : "";
                CheckWarn warn = new CheckWarn();
                warn.setId(bean.getId());
                warn.setCustId(bean.getCustId());
                warn.setType(ConstCheckData.BFR);
                warn.setLevel(level);
                warn.setDescript(bean.getDescript());
                warn.setState(Constant.WARNING_STATE_NOT_DEAL);
                warn.setWarnTime(bean.getCreateTime());
                checkWarnService.save(warn);
                // 发送提醒信息
                String selfWarnInfo = new StringBuilder("您本次的").append(bean.getDescript()).toString();
                // 告警推送
                List<Object[]> pushSelfList = (List<Object[]>) relatedPersonnels[2];
                int pushAttr = ConstPushMsg.ATTR_MSG_TYPE_NOTICE;
                Map<String, Object> extMsgs = new HashMap<String, Object>();
                extMsgs.put(ConstPushMsg.MSG_TITLE_ATTR_NAME, "健康平台体脂预警通知");
                // 给客户自己推送信息，对于一个用户，同一种设备只有一个通道ID
                if (pushSelfList != null && pushSelfList.size() > 0)
                {
                    for (Object[] objs : pushSelfList)
                    {
                        pushAttr = ConstPushMsg.ATTR_MSG_TYPE_NOTICE | PushMsgUtil.getDeviceTypeAttr((Integer) objs[0]);
                        JsonObject msg = new JsonObject();
                        msg.addProperty(ConstPushMsg.MSG_TOPIC_ATTR_NAME, ConstPushMsg.MSG_TOPIC_TZYJ);
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
                        String fmWarnInfo = new StringBuilder("家庭成员").append(custName).append(bean.getDescript())
                                .toString();
                        JsonObject msg = new JsonObject();
                        msg.addProperty(ConstPushMsg.MSG_TOPIC_ATTR_NAME, ConstPushMsg.MSG_TOPIC_TZYJ);
                        msg.addProperty(ConstPushMsg.MSG_CONTENT_ATTR_NAME, fmWarnInfo);
                        extMsgs.put(ConstPushMsg.MSG_DESCRIPTION_ATTR_NAME, fmWarnInfo);
                        PushMsgUtil.pushBatch(aChannelIds, pushAttr, msg, extMsgs);
                    }
                }
            }
        }
    }

    /**
     * 客户端查询体脂检测数据
     * @author h
     * @param filter 过滤条件
     * @param sysTime 同步时间点
     * @param dir 同步方式，大于0：取时间点后的数据，小于0取时间点前的数据
     * 2015.8.10
     * @throws Exception 
     * @return
     * */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageTran<BodyFat> queryTran(Map<String, Object> filter, Timestamp sysTime, int dir, int count)
    {
        return bodyFatDao.queryTran(filter, sysTime, dir, count);
    }

    /**
     * 平台端分页查询体脂检测数据
     * @author h
     * @param filter 过滤条件
     * @param page 页
     * @param pageSize 每页显示条数
     * 2015.8.10
     * @throws Exception 
     * @return
     * */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<BodyFat> query(Map<String, Object> filter, int page, int pageSize)
    {
        return bodyFatDao.queryFat(filter, page, pageSize);
    }
}
