package com.yzxt.yh.module.chr.bean;

import java.sql.Timestamp;
import java.util.Date;

public class Visit implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    private String id;
    private String custId;
    private String doctorId;
    private String visitNo;
    private Integer type;
    private Integer grade;
    private Date planFlupTime;
    private Integer ishandled;
    private Timestamp actualFlupTime;
    private Integer flupGrade;
    private Integer status;
    private String createBy;
    private Timestamp createTime;
    private String updateBy;
    private Timestamp updateTime;

    private String memberName;
    private Date flupDate;
    private String flupDateStr;
    private String doctorName;

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

    public String getDoctorId()
    {
        return this.doctorId;
    }

    public void setDoctorId(String doctorId)
    {
        this.doctorId = doctorId;
    }

    public String getVisitNo()
    {
        return visitNo;
    }

    public void setVisitNo(String visitNo)
    {
        this.visitNo = visitNo;
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

    public Date getPlanFlupTime()
    {
        return planFlupTime;
    }

    public void setPlanFlupTime(Date planFlupTime)
    {
        this.planFlupTime = planFlupTime;
    }

    public Integer getIshandled()
    {
        return this.ishandled;
    }

    public void setIshandled(Integer ishandled)
    {
        this.ishandled = ishandled;
    }

    public Timestamp getActualFlupTime()
    {
        return this.actualFlupTime;
    }

    public void setActualFlupTime(Timestamp actualFlupTime)
    {
        this.actualFlupTime = actualFlupTime;
    }

    public Integer getFlupGrade()
    {
        return this.flupGrade;
    }

    public void setFlupGrade(Integer flupGrade)
    {
        this.flupGrade = flupGrade;
    }

    public Integer getStatus()
    {
        return this.status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
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

    public String getUpdateBy()
    {
        return this.updateBy;
    }

    public void setUpdateBy(String updateBy)
    {
        this.updateBy = updateBy;
    }

    public Timestamp getUpdateTime()
    {
        return this.updateTime;
    }

    public void setUpdateTime(Timestamp updateTime)
    {
        this.updateTime = updateTime;
    }

    public String getMemberName()
    {
        return memberName;
    }

    public void setMemberName(String memberName)
    {
        this.memberName = memberName;
    }

    public Date getFlupDate()
    {
        return flupDate;
    }

    public void setFlupDate(Date flupDate)
    {
        this.flupDate = flupDate;
    }

    public String getFlupDateStr()
    {
        return flupDateStr;
    }

    public void setFlupDateStr(String flupDateStr)
    {
        this.flupDateStr = flupDateStr;
    }

    public String getDoctorName()
    {
        return doctorName;
    }

    public void setDoctorName(String doctorName)
    {
        this.doctorName = doctorName;
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