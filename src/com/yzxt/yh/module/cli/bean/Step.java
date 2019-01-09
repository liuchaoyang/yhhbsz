package com.yzxt.yh.module.cli.bean;

import java.sql.Timestamp;

public class Step implements java.io.Serializable
{

    private static final long serialVersionUID = 1L;
    private String id;
    private String custId;
    private String deviceName;
    private String deviceMac;
    private Double latitude;
    private Double longitude;
    private String unit;
    private Integer totalSteps;
    private Integer totalDis;
    private Double calories;
    private Timestamp checkTime;
    private Timestamp createTime;
    private Integer checkType;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getCustId()
    {
        return custId;
    }

    public void setCustId(String custId)
    {
        this.custId = custId;
    }

    public String getDeviceName()
    {
        return deviceName;
    }

    public void setDeviceName(String deviceName)
    {
        this.deviceName = deviceName;
    }

    public String getDeviceMac()
    {
        return deviceMac;
    }

    public void setDeviceMac(String deviceMac)
    {
        this.deviceMac = deviceMac;
    }

    public Double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(Double latitude)
    {
        this.latitude = latitude;
    }

    public Double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(Double longitude)
    {
        this.longitude = longitude;
    }

    public String getUnit()
    {
        return unit;
    }

    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    public Integer getTotalSteps()
    {
        return totalSteps;
    }

    public void setTotalSteps(Integer totalSteps)
    {
        this.totalSteps = totalSteps;
    }

    public Integer getTotalDis()
    {
        return totalDis;
    }

    public void setTotalDis(Integer totalDis)
    {
        this.totalDis = totalDis;
    }

    public Double getCalories()
    {
        return calories;
    }

    public void setCalories(Double calories)
    {
        this.calories = calories;
    }

    public Timestamp getCheckTime()
    {
        return checkTime;
    }

    public void setCheckTime(Timestamp checkTime)
    {
        this.checkTime = checkTime;
    }

    public Timestamp getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime)
    {
        this.createTime = createTime;
    }

    public Integer getCheckType()
    {
        return checkType;
    }

    public void setCheckType(Integer checkType)
    {
        this.checkType = checkType;
    }

}