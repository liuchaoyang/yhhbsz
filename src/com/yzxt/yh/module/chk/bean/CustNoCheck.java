package com.yzxt.yh.module.chk.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class CustNoCheck implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String custId;
    private String custName;
    private String idCard;
    private Integer noCheckDay;
    private String lastCheckItem;
    private Timestamp lastCheckTime;
    private Timestamp custRegTime;

    public String getCustId()
    {
        return custId;
    }

    public void setCustId(String custId)
    {
        this.custId = custId;
    }

    public String getCustName()
    {
        return custName;
    }

    public void setCustName(String custName)
    {
        this.custName = custName;
    }

    public String getIdCard()
    {
        return idCard;
    }

    public void setIdCard(String idCard)
    {
        this.idCard = idCard;
    }

    public Integer getNoCheckDay()
    {
        return noCheckDay;
    }

    public void setNoCheckDay(Integer noCheckDay)
    {
        this.noCheckDay = noCheckDay;
    }

    public String getLastCheckItem()
    {
        return lastCheckItem;
    }

    public void setLastCheckItem(String lastCheckItem)
    {
        this.lastCheckItem = lastCheckItem;
    }

    public Timestamp getLastCheckTime()
    {
        return lastCheckTime;
    }

    public void setLastCheckTime(Timestamp lastCheckTime)
    {
        this.lastCheckTime = lastCheckTime;
    }

    public Timestamp getCustRegTime()
    {
        return custRegTime;
    }

    public void setCustRegTime(Timestamp custRegTime)
    {
        this.custRegTime = custRegTime;
    }

}