package com.yzxt.yh.module.ach.bean;

import java.io.Serializable;

public class LifeEnv implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String id;
    private String custId;
    private Integer envType;
    private String detail;

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

    public Integer getEnvType()
    {
        return this.envType;
    }

    public void setEnvType(Integer envType)
    {
        this.envType = envType;
    }

    public String getDetail()
    {
        return this.detail;
    }

    public void setDetail(String detail)
    {
        this.detail = detail;
    }

}