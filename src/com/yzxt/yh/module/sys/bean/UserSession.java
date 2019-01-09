package com.yzxt.yh.module.sys.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class UserSession implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String ticket;

    private String userId;

    private Date expiryTime;

    private String memo;

    private Timestamp createTime;

    public String getTicket()
    {
        return ticket;
    }

    public void setTicket(String ticket)
    {
        this.ticket = ticket;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public Date getExpiryTime()
    {
        return expiryTime;
    }

    public void setExpiryTime(Date expiryTime)
    {
        this.expiryTime = expiryTime;
    }

    public String getMemo()
    {
        return memo;
    }

    public void setMemo(String memo)
    {
        this.memo = memo;
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