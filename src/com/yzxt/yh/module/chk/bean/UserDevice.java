package com.yzxt.yh.module.chk.bean;

import java.sql.Timestamp;

public class UserDevice implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private String custId;
    private String deviceType;
    private String deviceSn;
    private String deviceSnExt;
    private String createBy;
    private Timestamp createTime;
    private String updateBy;
    private Timestamp updateTime;

    private String custName;
    private String deviceName;
    private String deviceUser;
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

    public String getDeviceType()
    {
        return this.deviceType;
    }

    public void setDeviceType(String deviceType)
    {
        this.deviceType = deviceType;
    }

    public String getDeviceSn()
    {
        return this.deviceSn;
    }

    public void setDeviceSn(String deviceSn)
    {
        this.deviceSn = deviceSn;
    }

    public String getDeviceSnExt()
    {
        return this.deviceSnExt;
    }

    public void setDeviceSnExt(String deviceSnExt)
    {
        this.deviceSnExt = deviceSnExt;
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

    public String getCustName()
    {
        return custName;
    }

    public void setCustName(String custName)
    {
        this.custName = custName;
    }

    public String getDeviceName()
    {
        return deviceName;
    }

    public void setDeviceName(String deviceName)
    {
        this.deviceName = deviceName;
    }

    public String getDeviceUser()
    {
        return deviceUser;
    }

    public void setDeviceUser(String deviceUser)
    {
        this.deviceUser = deviceUser;
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