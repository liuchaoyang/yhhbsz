package com.yzxt.yh.module.ach.bean;

import java.io.Serializable;

public class FamilyHistory implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String id;
    private String custId;
    private Integer relaType;
    private String disease;

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

    public Integer getRelaType()
    {
        return this.relaType;
    }

    public void setRelaType(Integer relaType)
    {
        this.relaType = relaType;
    }

    public String getDisease()
    {
        return this.disease;
    }

    public void setDisease(String disease)
    {
        this.disease = disease;
    }

}