package com.yzxt.yh.module.svb.bean;

import java.sql.Timestamp;

public class MemberPayLog implements java.io.Serializable
{

    private static final long serialVersionUID = 1L;

    private String id;
    private String custId;
    private Double payAmount;
    private Integer payType;
    private String memo;
    private String createBy;
    private Timestamp createTime;

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

    public Double getPayAmount()
    {
        return this.payAmount;
    }

    public void setPayAmount(Double payAmount)
    {
        this.payAmount = payAmount;
    }

    public Integer getPayType()
    {
        return this.payType;
    }

    public void setPayType(Integer payType)
    {
        this.payType = payType;
    }

    public String getMemo()
    {
        return this.memo;
    }

    public void setMemo(String memo)
    {
        this.memo = memo;
    }

    public String getCreateBy()
    {
        return this.createBy;
    }

    public void setCreateBy(String createBy)
    {
        this.createBy = createBy;
    }

    public Timestamp getCreateTime()
    {
        return this.createTime;
    }

    public void setCreateTime(Timestamp createTime)
    {
        this.createTime = createTime;
    }

}