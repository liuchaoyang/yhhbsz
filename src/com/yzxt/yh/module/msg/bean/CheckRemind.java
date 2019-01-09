package com.yzxt.yh.module.msg.bean;

import java.sql.Timestamp;

public class CheckRemind implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;
    private String custId;
    private String custName;
    private String idCard;
    private String lastCheckItem;
    private Timestamp createUserTime;
    private Integer noCheckDays;
    private Timestamp lastCheckTime;
    private Timestamp lastRemindTime;
    private Integer remindIntervalDay;
    // 非持久化字段
    private String[] custIds;
    private String custPhone;
    private String docPhone;

    public String getCustId()
    {
        return custId;
    }

    public void setCustId(String custId)
    {
        this.custId = custId;
    }

    public String getCustName()
    {
        return custName;
    }

    public void setCustName(String custName)
    {
        this.custName = custName;
    }

    public String getIdCard()
    {
        return idCard;
    }

    public void setIdCard(String idCard)
    {
        this.idCard = idCard;
    }

    public String getLastCheckItem()
    {
        return lastCheckItem;
    }

    public void setLastCheckItem(String lastCheckItem)
    {
        this.lastCheckItem = lastCheckItem;
    }

    public Timestamp getCreateUserTime()
    {
        return createUserTime;
    }

    public void setCreateUserTime(Timestamp createUserTime)
    {
        this.createUserTime = createUserTime;
    }

    public Integer getNoCheckDays()
    {
        return noCheckDays;
    }

    public void setNoCheckDays(Integer noCheckDays)
    {
        this.noCheckDays = noCheckDays;
    }

    public Timestamp getLastCheckTime()
    {
        return lastCheckTime;
    }

    public void setLastCheckTime(Timestamp lastCheckTime)
    {
        this.lastCheckTime = lastCheckTime;
    }

    public Timestamp getLastRemindTime()
    {
        return lastRemindTime;
    }

    public void setLastRemindTime(Timestamp lastRemindTime)
    {
        this.lastRemindTime = lastRemindTime;
    }

    public Integer getRemindIntervalDay()
    {
        return remindIntervalDay;
    }

    public void setRemindIntervalDay(Integer remindIntervalDay)
    {
        this.remindIntervalDay = remindIntervalDay;
    }

    public String[] getCustIds()
    {
        return custIds;
    }

    public void setCustIds(String[] custIds)
    {
        this.custIds = custIds;
    }

    public String getCustPhone()
    {
        return custPhone;
    }

    public void setCustPhone(String custPhone)
    {
        this.custPhone = custPhone;
    }

    public String getDocPhone()
    {
        return docPhone;
    }

    public void setDocPhone(String docPhone)
    {
        this.docPhone = docPhone;
    }

}