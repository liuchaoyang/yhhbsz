package com.yzxt.yh.module.chk.bean;

import java.sql.Timestamp;

import com.google.gson.annotations.SerializedName;

public class BodyFat
{
    private String id;
    private String custId;
    private String deviceName;
    private String deviceMac;
    private Double latitude;
    private Double longitude;
    private String unit;
    private Double weight;
    private Double height;
    @SerializedName("subFatRate")
    private Double bfr;
    @SerializedName("visceralFatRate")
    private Double VFR;
    @SerializedName("muscleRate")
    private Double MR;
    private Double BMR;
    private Double bf;
    private Double water;
    private Double bone;
    private Double bodyage;
    private Integer level;
    @SerializedName("warningReason")
    private String descript;
    private Timestamp checkTime;
    @SerializedName("uploadTime")
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

    public Double getWeight()
    {
        return weight;
    }

    public void setWeight(Double weight)
    {
        this.weight = weight;
    }

    public Double getHeight()
    {
        return height;
    }

    public void setHeight(Double height)
    {
        this.height = height;
    }

    public Double getBfr()
    {
        return bfr;
    }

    public void setBfr(Double bfr)
    {
        this.bfr = bfr;
    }

    public Double getVFR()
    {
        return VFR;
    }

    public void setVFR(Double vFR)
    {
        VFR = vFR;
    }

    public Double getMR()
    {
        return MR;
    }

    public void setMR(Double mR)
    {
        MR = mR;
    }

    public Double getBMR()
    {
        return BMR;
    }

    public void setBMR(Double bMR)
    {
        BMR = bMR;
    }

    public Double getBf()
    {
        return bf;
    }

    public void setBf(Double bf)
    {
        this.bf = bf;
    }

    public Double getWater()
    {
        return water;
    }

    public void setWater(Double water)
    {
        this.water = water;
    }

    public Double getBone()
    {
        return bone;
    }

    public void setBone(Double bone)
    {
        this.bone = bone;
    }

    public Double getBodyage()
    {
        return bodyage;
    }

    public void setBodyage(Double bodyage)
    {
        this.bodyage = bodyage;
    }

    public Integer getLevel()
    {
        return level;
    }

    public void setLevel(Integer level)
    {
        this.level = level;
    }

    public String getDescript()
    {
        return descript;
    }

    public void setDescript(String descript)
    {
        this.descript = descript;
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
