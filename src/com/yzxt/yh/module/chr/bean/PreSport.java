package com.yzxt.yh.module.chr.bean;

import com.google.gson.annotations.SerializedName;

public class PreSport implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    @SerializedName("sportFrency")
    private Integer hbpSptFy;
    @SerializedName("sportTime")
    private Integer hbpSptTime;
    private String hId;

    public String getId()
    {
        return this.id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public Integer getHbpSptFy()
    {
        return this.hbpSptFy;
    }

    public void setHbpSptFy(Integer hbpSptFy)
    {
        this.hbpSptFy = hbpSptFy;
    }

    public Integer getHbpSptTime()
    {
        return this.hbpSptTime;
    }

    public void setHbpSptTime(Integer hbpSptTime)
    {
        this.hbpSptTime = hbpSptTime;
    }

    public String gethId()
    {
        return hId;
    }

    public void sethId(String hId)
    {
        this.hId = hId;
    }

}