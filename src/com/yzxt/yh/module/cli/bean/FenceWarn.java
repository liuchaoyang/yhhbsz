package com.yzxt.yh.module.cli.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class FenceWarn implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String id;
    // 客户ID
    private String custId;
    // 设备编号
    private String deviceNo;
    // 超出电子围栏是经度
    private Double longitude;
    // 超出电子围栏是纬度
    private Double latitude;
    // 告警时间
    private Timestamp warnTime;
    // 预计描述
    private String descript;
    // 告警时的地址
    private String address;
    // 创建时间
    private Timestamp createTime;

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

    public String getDeviceNo()
    {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo)
    {
        this.deviceNo = deviceNo;
    }

    public Double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(Double longitude)
    {
        this.longitude = longitude;
    }

    public Double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(Double latitude)
    {
        this.latitude = latitude;
    }

    public Timestamp getWarnTime()
    {
        return warnTime;
    }

    public void setWarnTime(Timestamp warnTime)
    {
        this.warnTime = warnTime;
    }

    public String getDescript()
    {
        return descript;
    }

    public void setDescript(String descript)
    {
        this.descript = descript;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public Timestamp getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime)
    {
        this.createTime = createTime;
    }

}
