package com.yzxt.yh.module.chk.bean;

import java.sql.Timestamp;

public class PhysiologIndex implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;
    private String custId;
    private Double height;
    private Double weight;
    private Integer sbp;
    private Integer dbp;
    private Double fpg;
    private Double h2pbg;
    private Double l2sugar;
    private Integer pulse;
    private Integer bo;
    private Double bfr;
    private Double temperature;
    private Double totalCholesterol;
    private Double uricAcid;
    private String lastCheckItem;
    private Timestamp lastCheckTime;
    private Timestamp lastRemindTime;
    private Integer remindIntervalDay;

    public String getCustId()
    {
        return this.custId;
    }

    public void setCustId(String custId)
    {
        this.custId = custId;
    }

    public Double getHeight()
    {
        return this.height;
    }

    public void setHeight(Double height)
    {
        this.height = height;
    }

    public Double getWeight()
    {
        return this.weight;
    }

    public void setWeight(Double weight)
    {
        this.weight = weight;
    }

    public Integer getSbp()
    {
        return this.sbp;
    }

    public void setSbp(Integer sbp)
    {
        this.sbp = sbp;
    }

    public Integer getDbp()
    {
        return this.dbp;
    }

    public void setDbp(Integer dbp)
    {
        this.dbp = dbp;
    }

    public Double getFpg()
    {
        return this.fpg;
    }

    public void setFpg(Double fpg)
    {
        this.fpg = fpg;
    }

    public Double getH2pbg()
    {
        return this.h2pbg;
    }

    public void setH2pbg(Double h2pbg)
    {
        this.h2pbg = h2pbg;
    }

    public Double getL2sugar()
    {
        return this.l2sugar;
    }

    public void setL2sugar(Double l2sugar)
    {
        this.l2sugar = l2sugar;
    }

    public Integer getPulse()
    {
        return this.pulse;
    }

    public void setPulse(Integer pulse)
    {
        this.pulse = pulse;
    }

    public Integer getBo()
    {
        return this.bo;
    }

    public void setBo(Integer bo)
    {
        this.bo = bo;
    }

    public Double getBfr()
    {
        return bfr;
    }

    public void setBfr(Double bfr)
    {
        this.bfr = bfr;
    }

    public Double getTemperature()
    {
        return this.temperature;
    }

    public void setTemperature(Double temperature)
    {
        this.temperature = temperature;
    }

    public Double getTotalCholesterol()
    {
        return this.totalCholesterol;
    }

    public void setTotalCholesterol(Double totalCholesterol)
    {
        this.totalCholesterol = totalCholesterol;
    }

    public Double getUricAcid()
    {
        return this.uricAcid;
    }

    public void setUricAcid(Double uricAcid)
    {
        this.uricAcid = uricAcid;
    }

    public String getLastCheckItem()
    {
        return lastCheckItem;
    }

    public void setLastCheckItem(String lastCheckItem)
    {
        this.lastCheckItem = lastCheckItem;
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

}