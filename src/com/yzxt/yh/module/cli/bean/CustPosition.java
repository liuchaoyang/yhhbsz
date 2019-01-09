package com.yzxt.yh.module.cli.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class CustPosition implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private String custId;
    private String deviceName;
    private String deviceMac;
    private Double latitude;
    private Double longitude;
    private Double speed;
    private Integer direction;
    private Integer altitude;
    private String mnc;
    private String lac;
    private String cellid;
    private String positionMode;
    private Integer checkType;
    private Timestamp checkTime;
    private Timestamp createTime;

    //非扩展字段
    private String deviceCode;
    
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

    public Double getSpeed()
    {
        return speed;
    }

    public void setSpeed(Double speed)
    {
        this.speed = speed;
    }

    public Integer getDirection()
    {
        return direction;
    }

    public void setDirection(Integer direction)
    {
        this.direction = direction;
    }

    public Integer getAltitude()
    {
        return altitude;
    }

    public void setAltitude(Integer altitude)
    {
        this.altitude = altitude;
    }

    public String getMnc()
    {
        return mnc;
    }

    public void setMnc(String mnc)
    {
        this.mnc = mnc;
    }

    public String getLac()
    {
        return lac;
    }

    public void setLac(String lac)
    {
        this.lac = lac;
    }

    public String getCellid()
    {
        return cellid;
    }

    public void setCellid(String cellid)
    {
        this.cellid = cellid;
    }

    public String getPositionMode()
    {
        return positionMode;
    }

    public void setPositionMode(String positionMode)
    {
        this.positionMode = positionMode;
    }

    public Integer getCheckType()
    {
        return checkType;
    }

    public void setCheckType(Integer checkType)
    {
        this.checkType = checkType;
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

    public String getDeviceCode()
    {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode)
    {
        this.deviceCode = deviceCode;
    }

}