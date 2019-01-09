package com.yzxt.yh.module.chk.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class CheckData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private String custId;
    private String custName;
    private String idCard;
    private Timestamp checkTime;
    private String itemType;
    private String itemName;
    private String valDesc;
    private Integer level;
    private String describe;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

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

    public Timestamp getCheckTime()
    {
        return checkTime;
    }

    public void setCheckTime(Timestamp checkTime)
    {
        this.checkTime = checkTime;
    }

    public String getItemType()
    {
        return itemType;
    }

    public void setItemType(String itemType)
    {
        this.itemType = itemType;
    }

    public String getItemName()
    {
        return itemName;
    }

    public void setItemName(String itemName)
    {
        this.itemName = itemName;
    }

    public String getValDesc()
    {
        return valDesc;
    }

    public void setValDesc(String valDesc)
    {
        this.valDesc = valDesc;
    }

    public Integer getLevel()
    {
        return level;
    }

    public void setLevel(Integer level)
    {
        this.level = level;
    }

    public String getDescribe()
    {
        return describe;
    }

    public void setDescribe(String describe)
    {
        this.describe = describe;
    }

}
