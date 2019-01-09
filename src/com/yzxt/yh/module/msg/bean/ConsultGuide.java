package com.yzxt.yh.module.msg.bean;

import java.sql.Timestamp;

public class ConsultGuide implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private String custId;
    private String consultTitle;
    private String consultContext;
    private Integer state;
    private String doctorId;
    private String guideContext;
    private Timestamp consultTime;
    private Timestamp guideTime;

    private String memberName;
    private String doctorName;

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

    public String getConsultTitle()
    {
        return this.consultTitle;
    }

    public void setConsultTitle(String consultTitle)
    {
        this.consultTitle = consultTitle;
    }

    public String getConsultContext()
    {
        return this.consultContext;
    }

    public void setConsultContext(String consultContext)
    {
        this.consultContext = consultContext;
    }

    public Integer getState()
    {
        return this.state;
    }

    public void setState(Integer state)
    {
        this.state = state;
    }

    public String getDoctorId()
    {
        return this.doctorId;
    }

    public void setDoctorId(String doctorId)
    {
        this.doctorId = doctorId;
    }

    public String getGuideContext()
    {
        return this.guideContext;
    }

    public void setGuideContext(String guideContext)
    {
        this.guideContext = guideContext;
    }

    public Timestamp getConsultTime()
    {
        return this.consultTime;
    }

    public void setConsultTime(Timestamp consultTime)
    {
        this.consultTime = consultTime;
    }

    public Timestamp getGuideTime()
    {
        return this.guideTime;
    }

    public void setGuideTime(Timestamp guideTime)
    {
        this.guideTime = guideTime;
    }

    public String getMemberName()
    {
        return memberName;
    }

    public void setMemberName(String memberName)
    {
        this.memberName = memberName;
    }

    public String getDoctorName()
    {
        return doctorName;
    }

    public void setDoctorName(String doctorName)
    {
        this.doctorName = doctorName;
    }

}