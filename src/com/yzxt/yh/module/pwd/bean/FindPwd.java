package com.yzxt.yh.module.pwd.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class FindPwd implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private String userId;
    private String validateCode;
    private Timestamp outDate;

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getValidateCode()
    {
        return validateCode;
    }

    public void setValidateCode(String validateCode)
    {
        this.validateCode = validateCode;
    }

    public Timestamp getOutDate()
    {
        return outDate;
    }

    public void setOutDate(Timestamp outDate)
    {
        this.outDate = outDate;
    }

}