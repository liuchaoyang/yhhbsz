package com.yzxt.yh.module.chk.bean;

import java.sql.Timestamp;

/**
 * 
 */
public class DeviceInfo implements java.io.Serializable
{

    private static final long serialVersionUID = 1L;

    private String code;
    private String name;
    private String img;
    private Integer checkType;
    private Integer state;
    private String createBy;
    private Timestamp createTime;
    private String updateBy;
    private Timestamp updateTime;

    public String getCode()
    {
        return this.code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getImg()
    {
        return this.img;
    }

    public void setImg(String img)
    {
        this.img = img;
    }

    public Integer getCheckType()
    {
        return checkType;
    }

    public void setCheckType(Integer checkType)
    {
        this.checkType = checkType;
    }

    public Integer getState()
    {
        return state;
    }

    public void setState(Integer state)
    {
        this.state = state;
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