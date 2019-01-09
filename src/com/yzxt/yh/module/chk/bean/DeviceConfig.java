package com.yzxt.yh.module.chk.bean;

import java.sql.Timestamp;

/**
 * 
 */
public class DeviceConfig implements java.io.Serializable
{

    private static final long serialVersionUID = 1L;

    private String id;
    private String deviceTypeCode;
    private String userName;
    private String val;
    private Integer seq;
    private String createBy;
    private Timestamp createTime;
    private String updateBy;
    private Timestamp updateTime;

    public String getId()
    {
        return this.id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getDeviceTypeCode()
    {
        return this.deviceTypeCode;
    }

    public void setDeviceTypeCode(String deviceTypeCode)
    {
        this.deviceTypeCode = deviceTypeCode;
    }

    public String getUserName()
    {
        return this.userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getVal()
    {
        return this.val;
    }

    public void setVal(String val)
    {
        this.val = val;
    }

    public Integer getSeq()
    {
        return this.seq;
    }

    public void setSeq(Integer seq)
    {
        this.seq = seq;
    }

    public String getCreateBy()
    {
        return createBy;
    }

    public void setCreateBy(String createBy)
    {
        this.createBy = createBy;
    }

    public Timestamp getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime)
    {
        this.createTime = createTime;
    }

    public String getUpdateBy()
    {
        return updateBy;
    }

    public void setUpdateBy(String updateBy)
    {
        this.updateBy = updateBy;
    }

    public Timestamp getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime)
    {
        this.updateTime = updateTime;
    }

}