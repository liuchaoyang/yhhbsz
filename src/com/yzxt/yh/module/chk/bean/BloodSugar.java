package com.yzxt.yh.module.chk.bean;

import java.sql.Timestamp;

import com.google.gson.annotations.SerializedName;

public class BloodSugar implements java.io.Serializable
{

    private static final long serialVersionUID = 1L;
    private String id;
    private String custId;
    private String deviceName;
    private String deviceMac;
    private Double latitude;
    private Double longitude;
    private String unit;
    @SerializedName("b_glucose")
    private Double bloodSugar;
    @SerializedName("b_g_type")
    private Integer bloodSugarType;
    private Integer mealType;
    @SerializedName("intervalTime")
    private Double timeToMeal;
    private Integer level;
    @SerializedName("warningReason")
    private String descript;
    private Timestamp checkTime;
    @SerializedName("uploadTime")
    private Timestamp createTime;
    private Integer checkType;

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

    public Double getBloodSugar()
    {
        return this.bloodSugar;
    }

    public void setBloodSugar(Double bloodSugar)
    {
        this.bloodSugar = bloodSugar;
    }

    public Integer getBloodSugarType()
    {
        return this.bloodSugarType;
    }

    public void setBloodSugarType(Integer bloodSugarType)
    {
        this.bloodSugarType = bloodSugarType;
    }

    public Integer getMealType()
    {
        return this.mealType;
    }

    public void setMealType(Integer mealType)
    {
        this.mealType = mealType;
    }

    public Double getTimeToMeal()
    {
        return this.timeToMeal;
    }

    public void setTimeToMeal(Double timeToMeal)
    {
        this.timeToMeal = timeToMeal;
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

}