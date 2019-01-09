package com.yzxt.yh.module.rgm.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class SportLog implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private String sportId;
    private Timestamp sportTime;
    private Integer timeSpan;
    private String userId;
    private String createBy;
    private Timestamp createTime;
    private String updateBy;
    private Timestamp updateTime;

    //其他属性
    private String sportName;
    private String Type;
    private Double dayConsumeEnergy;
    private Integer sportType;
    private List<Sport> sportList;
    private String custName;

    public String getType()
    {
        return Type;
    }

    public void setType(String type)
    {
        Type = type;
    }

    public String getId()
    {
        return this.id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getSportId()
    {
        return this.sportId;
    }

    public void setSportId(String sportId)
    {
        this.sportId = sportId;
    }

    public Timestamp getSportTime()
    {
        return this.sportTime;
    }

    public void setSportTime(Timestamp sportTime)
    {
        this.sportTime = sportTime;
    }

    public Integer getTimeSpan()
    {
        return this.timeSpan;
    }

    public void setTimeSpan(Integer timeSpan)
    {
        this.timeSpan = timeSpan;
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

    public String getSportName()
    {
        return sportName;
    }

    public void setSportName(String sportName)
    {
        this.sportName = sportName;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public Double getDayConsumeEnergy()
    {
        return dayConsumeEnergy;
    }

    public void setDayConsumeEnergy(Double dayConsumeEnergy)
    {
        this.dayConsumeEnergy = dayConsumeEnergy;
    }

    public Integer getSportType()
    {
        return sportType;
    }

    public void setSportType(Integer sportType)
    {
        this.sportType = sportType;
    }

    public List<Sport> getSportList()
    {
        return sportList;
    }

    public void setSportList(List<Sport> sportList)
    {
        this.sportList = sportList;
    }

    public String getCustName()
    {
        return custName;
    }

    public void setCustName(String custName)
    {
        this.custName = custName;
    }

}