package com.yzxt.yh.module.ach.bean;

import java.io.Serializable;
import java.util.Date;

public class PreviousHistory implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String id;
    private String custId;
    private Integer type;
    private String name;
    private Date pastTime;

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

    public Integer getType()
    {
        return this.type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Date getPastTime()
    {
        return this.pastTime;
    }

    public void setPastTime(Date pastTime)
    {
        this.pastTime = pastTime;
    }

}