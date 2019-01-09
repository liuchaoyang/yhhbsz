package com.yzxt.yh.module.rgm.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class HealthyPlan implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private String userId;
    private String name;
    private Integer type;
    private Date startDate;
    private Date endDate;
    private Integer state;
    private String targetValue;
    private String targetValue2;
    private String targetValue3;
    private String createBy;
    private Timestamp createTime;
    private String updateBy;
    private Timestamp updateTime;
    // 非持久化字段
    private String idCard;
    private String startValDesc;

    private String endValDesc;

    private String targetValDesc;

    private String statusDesc;

    public String getId()
    {
        return this.id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getType()
    {
        return this.type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public Date getStartDate()
    {
        return this.startDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public Date getEndDate()
    {
        return this.endDate;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    public Integer getState()
    {
        return this.state;
    }

    public void setState(Integer state)
    {
        this.state = state;
    }

    public String getTargetValue()
    {
        return this.targetValue;
    }

    public void setTargetValue(String targetValue)
    {
        this.targetValue = targetValue;
    }

    public String getTargetValue2()
    {
        return this.targetValue2;
    }

    public void setTargetValue2(String targetValue2)
    {
        this.targetValue2 = targetValue2;
    }

    public String getTargetValue3()
    {
        return targetValue3;
    }

    public void setTargetValue3(String targetValue3)
    {
        this.targetValue3 = targetValue3;
    }

    public String getCreateBy()
    {
        return this.createBy;
    }

    public void setCreateBy(String createBy)
    {
        this.createBy = createBy;
    }

    public Timestamp getCreateTime()
    {
        return this.createTime;
    }

    public void setCreateTime(Timestamp createTime)
    {
        this.createTime = createTime;
    }

    public String getUpdateBy()
    {
        return this.updateBy;
    }

    public void setUpdateBy(String updateBy)
    {
        this.updateBy = updateBy;
    }

    public Timestamp getUpdateTime()
    {
        return this.updateTime;
    }

    public void setUpdateTime(Timestamp updateTime)
    {
        this.updateTime = updateTime;
    }

    public String getIdCard()
    {
        return idCard;
    }

    public void setIdCard(String idCard)
    {
        this.idCard = idCard;
    }

    public String getStartValDesc()
    {
        return startValDesc;
    }

    public void setStartValDesc(String startValDesc)
    {
        this.startValDesc = startValDesc;
    }

    public String getEndValDesc()
    {
        return endValDesc;
    }

    public void setEndValDesc(String endValDesc)
    {
        this.endValDesc = endValDesc;
    }

    public String getTargetValDesc()
    {
        return targetValDesc;
    }

    public void setTargetValDesc(String targetValDesc)
    {
        this.targetValDesc = targetValDesc;
    }

    public String getStatusDesc()
    {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc)
    {
        this.statusDesc = statusDesc;
    }

}