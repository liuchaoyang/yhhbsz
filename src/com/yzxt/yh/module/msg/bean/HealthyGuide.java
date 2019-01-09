package com.yzxt.yh.module.msg.bean;

import java.sql.Timestamp;

public class HealthyGuide implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private String custId;
    private String preId;
    public String getPreId() {
		return preId;
	}

	public void setPreId(String preId) {
		this.preId = preId;
	}

	private String directReason;
    private String sportDirect;
    private String foodDirect;
    private String drugSuggest;
    private String memo;
    private String createBy;
    private Timestamp createTime;

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

    public String getDirectReason()
    {
        return this.directReason;
    }

    public void setDirectReason(String directReason)
    {
        this.directReason = directReason;
    }

    public String getSportDirect()
    {
        return this.sportDirect;
    }

    public void setSportDirect(String sportDirect)
    {
        this.sportDirect = sportDirect;
    }

    public String getFoodDirect()
    {
        return this.foodDirect;
    }

    public void setFoodDirect(String foodDirect)
    {
        this.foodDirect = foodDirect;
    }

    public String getDrugSuggest()
    {
        return this.drugSuggest;
    }

    public void setDrugSuggest(String drugSuggest)
    {
        this.drugSuggest = drugSuggest;
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