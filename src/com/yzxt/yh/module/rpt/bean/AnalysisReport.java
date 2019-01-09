package com.yzxt.yh.module.rpt.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class AnalysisReport implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private String custId;
    private String pressId;
    
    public String getPressId() {
		return pressId;
	}

	public void setPressId(String pressId) {
		this.pressId = pressId;
	}

	private Date examBeginTime;
    private Date examEndTime;
    private Integer reportType;
    private String suggest;
    private String createBy;
    private Timestamp createTime;
    private String updateBy;
    private Timestamp updateTime;
    // 非持久化属性
    private String customerName;
    private String idCard;
    private String phone;
    private Date birthday;
    private Integer sex;
    private Object[] data;

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

    public Date getExamBeginTime()
    {
        return examBeginTime;
    }

    public void setExamBeginTime(Date examBeginTime)
    {
        this.examBeginTime = examBeginTime;
    }

    public Date getExamEndTime()
    {
        return examEndTime;
    }

    public void setExamEndTime(Date examEndTime)
    {
        this.examEndTime = examEndTime;
    }

    public Integer getReportType()
    {
        return reportType;
    }

    public void setReportType(Integer reportType)
    {
        this.reportType = reportType;
    }

    public String getSuggest()
    {
        return suggest;
    }

    public void setSuggest(String suggest)
    {
        this.suggest = suggest;
    }

    public String getCreateBy()
    {
        return createBy;
    }

    public void setCreateBy(String createBy)
    {
        this.createBy = createBy;
    }

    public Timestamp getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime)
    {
        this.createTime = createTime;
    }

    public String getUpdateBy()
    {
        return updateBy;
    }

    public void setUpdateBy(String updateBy)
    {
        this.updateBy = updateBy;
    }

    public Timestamp getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime)
    {
        this.updateTime = updateTime;
    }

    public String getCustomerName()
    {
        return customerName;
    }

    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }

    public String getIdCard()
    {
        return idCard;
    }

    public void setIdCard(String idCard)
    {
        this.idCard = idCard;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public Date getBirthday()
    {
        return birthday;
    }

    public void setBirthday(Date birthday)
    {
        this.birthday = birthday;
    }

    public Integer getSex()
    {
        return sex;
    }

    public void setSex(Integer sex)
    {
        this.sex = sex;
    }

    public Object[] getData()
    {
        return data;
    }

    public void setData(Object[] data)
    {
        this.data = data;
    }

}