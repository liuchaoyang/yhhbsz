package com.yzxt.yh.module.sys.bean;

public class UserPassword implements java.io.Serializable
{

    private static final long serialVersionUID = 1L;

    private String userId;
    private String password;

    public String getUserId()
    {
        return this.userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getPassword()
    {
        return this.password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

}