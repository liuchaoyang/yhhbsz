package com.yzxt.yh.module.svb.bean;

import java.io.Serializable;
import java.util.Date;

public class MemberInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String id;
    private String custId;
    private String doctorId;
    private String orgId;
    private Date startDay;
    private Date endDay;
    private Integer state;
    private Integer type;

    public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	private String doctorName;
    private String memberName;
    private String idCard;

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

    public String getOrgId()
    {
        return orgId;
    }

    public void setOrgId(String orgId)
    {
        this.orgId = orgId;
    }

    public Date getStartDay()
    {
        return this.startDay;
    }

    public void setStartDay(Date startDay)
    {
        this.startDay = startDay;
    }

    public Date getEndDay()
    {
        return this.endDay;
    }

    public void setEndDay(Date endDay)
    {
        this.endDay = endDay;
    }

    public Integer getState()
    {
        return this.state;
    }

    public void setState(Integer state)
    {
        this.state = state;
    }

    public String getDoctorName()
    {
        return doctorName;
    }

    public void setDoctorName(String doctorName)
    {
        this.doctorName = doctorName;
    }

    public String getMemberName()
    {
        return memberName;
    }

    public void setMemberName(String memberName)
    {
        this.memberName = memberName;
    }

    public String getIdCard()
    {
        return idCard;
    }

    public void setIdCard(String idCard)
    {
        this.idCard = idCard;
    }

}