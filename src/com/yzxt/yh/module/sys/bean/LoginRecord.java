package com.yzxt.yh.module.sys.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class LoginRecord implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String id;
    private String userId;
    private Integer type;
    private Timestamp createTime;

    // 非持久化字段
    private String userName;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public Timestamp getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime)
    {
        this.createTime = createTime;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

}