package com.yzxt.yh.module.sys.bean;

import java.io.Serializable;

public class PushUser implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String id;
    private String userId;
    private String pushChannelId;
    private Integer deviceType;
    private String pushUserId;

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

    public String getPushChannelId()
    {
        return pushChannelId;
    }

    public void setPushChannelId(String pushChannelId)
    {
        this.pushChannelId = pushChannelId;
    }

    public Integer getDeviceType()
    {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType)
    {
        this.deviceType = deviceType;
    }

    public String getPushUserId()
    {
        return pushUserId;
    }

    public void setPushUserId(String pushUserId)
    {
        this.pushUserId = pushUserId;
    }

}