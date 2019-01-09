package com.yzxt.yh.module.cli.service;

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
import com.yzxt.yh.constant.ConstDevice;
import com.yzxt.yh.constant.ConstPushMsg;
import com.yzxt.yh.constant.ConstTM;
import com.yzxt.yh.module.chk.service.CheckWarnService;
import com.yzxt.yh.module.cli.bean.FenceWarn;
import com.yzxt.yh.module.cli.dao.FenceWarnDao;

@Transactional(ConstTM.DEFAULT)
public class FenceWarnService
{
    private FenceWarnDao fenceWarnDao;
    private CheckWarnService checkWarnService;

    public FenceWarnDao getFenceWarnDao()
    {
        return fenceWarnDao;
    }

    public void setFenceWarnDao(FenceWarnDao fenceWarnDao)
    {
        this.fenceWarnDao = fenceWarnDao;
    }

    public CheckWarnService getCheckWarnService()
    {
        return checkWarnService;
    }

    public void setCheckWarnService(CheckWarnService checkWarnService)
    {
        this.checkWarnService = checkWarnService;
    }

    /**
     * 保存超出电子围栏告警
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(FenceWarn fenceWarn) throws Exception
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        // 保存围栏告警
        String address = fenceWarn.getAddress();
        if (address != null && address.length() > 100)
        {
            fenceWarn.setAddress(address.substring(0, 100));
        }
        fenceWarn.setDescript("离开电子围栏安全区域，新位置：" + fenceWarn.getAddress() + "。");
        fenceWarn.setCreateTime(now);
        fenceWarnDao.insert(fenceWarn);
        // 告警推送
        Object[] relatedPersonnels = checkWarnService.getWarnRelatedPersonnels(fenceWarn.getCustId());
        String[] cust = (String[]) relatedPersonnels[0];
        String custName = cust[1] != null ? cust[1] : "";
        List<Object[]> pushSelfList = (List<Object[]>) relatedPersonnels[2];
        int pushAttr = ConstPushMsg.ATTR_MSG_TYPE_NOTICE;
        Map<String, Object> extMsgs = new HashMap<String, Object>();
        extMsgs.put(ConstPushMsg.MSG_TITLE_ATTR_NAME, "健康平台电子围栏预警通知");
        // 给客户自己推送信息，对于一个用户，同一种设备只有一个通道ID
        if (pushSelfList != null && pushSelfList.size() > 0)
        {
            for (Object[] objs : pushSelfList)
            {
                String selfWarnInfo = "您已经离开安全区域，您现在的位置是：" + fenceWarn.getAddress() + "。";
                pushAttr = ConstPushMsg.ATTR_MSG_TYPE_NOTICE | PushMsgUtil.getDeviceTypeAttr((Integer) objs[0]);
                JsonObject msg = new JsonObject();
                msg.addProperty(ConstPushMsg.MSG_TOPIC_ATTR_NAME, ConstPushMsg.MSG_TOPIC_DZWL);
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
                String fmWarnInfo = new StringBuilder("家庭成员").append(custName).append("已经离开安全区域，现在的位置是：")
                        .append(fenceWarn.getAddress()).append("。").toString();
                JsonObject msg = new JsonObject();
                msg.addProperty(ConstPushMsg.MSG_TOPIC_ATTR_NAME, ConstPushMsg.MSG_TOPIC_DZWL);
                msg.addProperty(ConstPushMsg.MSG_CONTENT_ATTR_NAME, fmWarnInfo);
                extMsgs.put(ConstPushMsg.MSG_DESCRIPTION_ATTR_NAME, fmWarnInfo);
                PushMsgUtil.pushBatch(aChannelIds, pushAttr, msg, extMsgs);
            }
        }
    }

    /**
     * 分页查询电子围栏告警信息
     * @param filter
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageModel<FenceWarn> query(Map<String, Object> filter, int page, int pageSize)
    {
        return fenceWarnDao.query(filter, page, pageSize);
    }

    /**
     * 客户端查询电子围栏告警信息
     * @param filter 过滤条件
     * @param sysTime 同步时间点
     * @param dir 同步方式，大于0：取时间点后的数据，小于0取时间点前的数据
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public PageTran<FenceWarn> queryTran(Map<String, Object> filter, Timestamp sysTime, int dir, int count)
    {
        return fenceWarnDao.queryTran(filter, sysTime, dir, count);
    }

}
