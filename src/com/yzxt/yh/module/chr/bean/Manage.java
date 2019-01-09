package com.yzxt.yh.module.chr.bean;

import java.sql.Timestamp;

public class Manage implements java.io.Serializable
{

    private static final long serialVersionUID = 1L;
    private String id;
    private String custId;
    private Integer type;
    private Integer grade;
    private String createBy;
    private Timestamp createTime;

    private String memberName;
    private String[] mngType;
    private Timestamp nextFlupTime;
    private Timestamp lastFlupTime;
    private String idCard;
    private Integer preFlupGrade;

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

    public Integer getType()
    {
        return this.type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public Integer getGrade()
    {
        return this.grade;
    }

    public void setGrade(Integer grade)
    {
        this.grade = grade;
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

    public String getMemberName()
    {
        return memberName;
    }

    public void setMemberName(String memberName)
    {
        this.memberName = memberName;
    }

    public String[] getMngType()
    {
        return mngType;
    }

    public void setMngType(String[] mngType)
    {
        this.mngType = mngType;
    }

    public Timestamp getNextFlupTime()
    {
        return nextFlupTime;
    }

    public void setNextFlupTime(Timestamp nextFlupTime)
    {
        this.nextFlupTime = nextFlupTime;
    }

    public Timestamp getLastFlupTime()
    {
        return lastFlupTime;
    }

    public void setLastFlupTime(Timestamp lastFlupTime)
    {
        this.lastFlupTime = lastFlupTime;
    }

    public String getIdCard()
    {
        return idCard;
    }

    public void setIdCard(String idCard)
    {
        this.idCard = idCard;
    }

    public Integer getPreFlupGrade()
    {
        return preFlupGrade;
    }

    public void setPreFlupGrade(Integer preFlupGrade)
    {
        this.preFlupGrade = preFlupGrade;
    }

}