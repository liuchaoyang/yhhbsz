package com.yzxt.yh.module.his.bean;

import java.sql.Timestamp;
import java.util.Date;

/**
 * HisAppoint entity. 
 * 预约挂号的实体类
 * @author h
 * 2016.1.21
 */

public class Appoint implements java.io.Serializable
{

    private static final long serialVersionUID = 1L;
    private String id;
    private String custId;
    private String deptId;
    private Integer areaId;
    private String departId;
    private String doctorId;
    public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	private String selfSymptom;
    private String firstVisit;
    private String memo;
    private Date appointTime;
    private Integer detailTime;
    private Integer status;
    private String resultExplain;
    private String createBy;
    private Timestamp createTime;
    private String updateBy;
    private Timestamp updateTime;

    //扩展字段
    private String deptName;
    private String cityName;
    private String departName;
    private String custName;

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

    public String getDeptId()
    {
        return deptId;
    }

    public void setDeptId(String deptId)
    {
        this.deptId = deptId;
    }

    public String getDepartId()
    {
        return departId;
    }

    public void setDepartId(String departId)
    {
        this.departId = departId;
    }

    public String getSelfSymptom()
    {
        return this.selfSymptom;
    }

    public void setSelfSymptom(String selfSymptom)
    {
        this.selfSymptom = selfSymptom;
    }

    public String getFirstVisit()
    {
        return this.firstVisit;
    }

    public void setFirstVisit(String firstVisit)
    {
        this.firstVisit = firstVisit;
    }

    public String getMemo()
    {
        return this.memo;
    }

    public void setMemo(String memo)
    {
        this.memo = memo;
    }

    public Date getAppointTime()
    {
        return appointTime;
    }

    public void setAppointTime(Date appointTime)
    {
        this.appointTime = appointTime;
    }

    public Integer getDetailTime()
    {
        return detailTime;
    }

    public void setDetailTime(Integer detailTime)
    {
        this.detailTime = detailTime;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public String getResultExplain()
    {
        return resultExplain;
    }

    public void setResultExplain(String resultExplain)
    {
        this.resultExplain = resultExplain;
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

    public String getDeptName()
    {
        return deptName;
    }

    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }

    public String getDepartName()
    {
        return departName;
    }

    public void setDepartName(String departName)
    {
        this.departName = departName;
    }

    public String getCustName()
    {
        return custName;
    }

    public void setCustName(String custName)
    {
        this.custName = custName;
    }

}