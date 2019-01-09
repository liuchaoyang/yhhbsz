package com.yzxt.yh.module.chk.bean;

import java.sql.Timestamp;

import com.google.gson.annotations.SerializedName;

public class Pulse implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;
    // 心率
    public static final int PULSE_TYPE_A = 1;
    // 脉率
    public static final int PULSE_TYPE_B = 2;

    private String id;
    private String custId;
    private String deviceName;
    private String deviceMac;
    private Double latitude;
    private Double longitude;
    private String unit;
    private Integer pulse;
    private Integer pulseType;
    private Integer level;
    private String descript;
    private Timestamp checkTime;
    @SerializedName("uploadTime")
    private Timestamp createTime;
    private Integer checkType;
    // 非持久化字段
    // 是否通过采集其它指标附加的指标项
    private boolean additional;

    public String getId()
    {
        return this.id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getCustId()
    {
        return this.custId;
    }

    public void setCustId(String custId)
    {
        this.custId = custId;
    }

    public String getDeviceName()
    {
        return this.deviceName;
    }

    public void setDeviceName(String deviceName)
    {
        this.deviceName = deviceName;
    }

    public String getDeviceMac()
    {
        return this.deviceMac;
    }

    public void setDeviceMac(String deviceMac)
    {
        this.deviceMac = deviceMac;
    }

    public Double getLatitude()
    {
        return this.latitude;
    }

    public void setLatitude(Double latitude)
    {
        this.latitude = latitude;
    }

    public Double getLongitude()
    {
        return this.longitude;
    }

    public void setLongitude(Double longitude)
    {
        this.longitude = longitude;
    }

    public String getUnit()
    {
        return this.unit;
    }

    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    public Integer getPulse()
    {
        return this.pulse;
    }

    public void setPulse(Integer pulse)
    {
        this.pulse = pulse;
    }

    public Integer getPulseType()
    {
        return pulseType;
    }

    public void setPulseType(Integer pulseType)
    {
        this.pulseType = pulseType;
    }

    public Integer getLevel()
    {
        return this.level;
    }

    public void setLevel(Integer level)
    {
        this.level = level;
    }

    public String getDescript()
    {
        return this.descript;
    }

    public void setDescript(String descript)
    {
        this.descript = descript;
    }

    public Timestamp getCheckTime()
    {
        return this.checkTime;
    }

    public void setCheckTime(Timestamp checkTime)
    {
        this.checkTime = checkTime;
    }

    public Timestamp getCreateTime()
    {
        return this.createTime;
    }

    public void setCreateTime(Timestamp createTime)
    {
        this.createTime = createTime;
    }

    public Integer getCheckType()
    {
        return this.checkType;
    }

    public void setCheckType(Integer checkType)
    {
        this.checkType = checkType;
    }

    public boolean getAdditional()
    {
        return additional;
    }

    public void setAdditional(boolean additional)
    {
        this.additional = additional;
    }

}