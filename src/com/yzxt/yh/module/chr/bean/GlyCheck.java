package com.yzxt.yh.module.chr.bean;

import java.sql.Timestamp;

import com.google.gson.annotations.SerializedName;

public class GlyCheck implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private String bId;
    @SerializedName("checkName")
    private String hbpCheckName;
    private Integer hbpCheckType;
    private Double hbpSugerBlood;
    @SerializedName("checkTime")
    private Timestamp hbpCheckTime;
    @SerializedName("checkData")
    private String hbpCheckRemark;

    public String getId()
    {
        return this.id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getbId()
    {
        return bId;
    }

    public void setbId(String bId)
    {
        this.bId = bId;
    }

    public String getHbpCheckName()
    {
        return this.hbpCheckName;
    }

    public void setHbpCheckName(String hbpCheckName)
    {
        this.hbpCheckName = hbpCheckName;
    }

    public Integer getHbpCheckType()
    {
        return this.hbpCheckType;
    }

    public void setHbpCheckType(Integer hbpCheckType)
    {
        this.hbpCheckType = hbpCheckType;
    }

    public Double getHbpSugerBlood()
    {
        return this.hbpSugerBlood;
    }

    public void setHbpSugerBlood(Double hbpSugerBlood)
    {
        this.hbpSugerBlood = hbpSugerBlood;
    }

    public Timestamp getHbpCheckTime()
    {
        return this.hbpCheckTime;
    }

    public void setHbpCheckTime(Timestamp hbpCheckTime)
    {
        this.hbpCheckTime = hbpCheckTime;
    }

    public String getHbpCheckRemark()
    {
        return this.hbpCheckRemark;
    }

    public void setHbpCheckRemark(String hbpCheckRemark)
    {
        this.hbpCheckRemark = hbpCheckRemark;
    }

}